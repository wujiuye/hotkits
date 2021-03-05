package com.wujiuye.hotkit.redis.impl.lettuce;

import com.wujiuye.hotkit.json.util.StringUtils;
import com.wujiuye.hotkit.redis.multidb.ReactiveRedisDataSourceHodler;
import com.wujiuye.hotkit.redis.template.ClusterReactiveRedisCacheTemplate;
import io.lettuce.core.pubsub.api.reactive.RedisPubSubReactiveCommands;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Lettuce-响应式编程API
 *
 * @author wujiuye 2020/11/02
 */
public class LettuceReactiveRedisCacheTemplateImpl extends ClusterReactiveRedisCacheTemplate {

    public LettuceReactiveRedisCacheTemplateImpl(String cluster) {
        super(cluster);
    }

    /**
     * 不支持切换DB
     *
     * @return
     */
    private Mono<RedisPubSubReactiveCommands<String, String>> getCommand() {
        return ReactiveRedisDataSourceHodler.currentDatabase()
                .map(db -> {
                    return LettuceClusterDelegate.getStatefulRedisPubSubConnection(getCluster(), db).reactive();
                });
    }

    @Override
    public Mono<Void> setExpireTime(Object key, int seconds) {
        return getCommand().flatMap(command -> command.expire(getKeyCacheSerializable().serialize(key), seconds)).then();
    }

    @Override
    public Mono<Void> delKey(Object key) {
        return getCommand().flatMap(command -> command.del(getKeyCacheSerializable().serialize(key))).then();
    }

    @Override
    public Mono<Void> setValue(Object key, Object value) {
        return getCommand().flatMap(command -> command.set(getKeyCacheSerializable().serialize(key), getValueCacheSerializable().serialize(value))).then();
    }

    @Override
    public <T> Mono<T> getValue(Object key, Class<T> valueClass) {
        return getCommand().flatMap(command -> command.get(getKeyCacheSerializable().serialize(key)))
                .flatMap(result -> {
                    if (StringUtils.isNullOrEmpty(result)) {
                        return Mono.empty();
                    }
                    T obj = getValueCacheSerializable().deserialize(result, valueClass);
                    return Mono.just(obj);
                });
    }

    @Override
    public Mono<Void> putMap(Object firstKey, Object scondKey, Object value) {
        return getCommand().flatMap(command -> command.hset(getKeyCacheSerializable().serialize(firstKey),
                getKeyCacheSerializable().serialize(scondKey), getValueCacheSerializable().serialize(value)))
                .then();
    }

    @Override
    public <T> Mono<T> getMap(Object firstKey, Object scondKey, Class<T> valueClass) {
        return getCommand().flatMap(command -> command.hget(getKeyCacheSerializable().serialize(firstKey),
                getKeyCacheSerializable().serialize(scondKey)))
                .flatMap(result -> {
                    if (StringUtils.isNullOrEmpty(result)) {
                        return Mono.empty();
                    }
                    T obj = getValueCacheSerializable().deserialize(result, valueClass);
                    return Mono.just(obj);
                });
    }

    @Override
    public Flux<String> getMapKeys(Object firstKey) {
        return getCommand().flatMapMany(command -> command.hkeys(getKeyCacheSerializable().serialize(firstKey)));
    }

}
