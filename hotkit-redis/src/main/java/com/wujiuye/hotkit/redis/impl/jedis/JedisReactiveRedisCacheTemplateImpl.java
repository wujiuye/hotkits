package com.wujiuye.hotkit.redis.impl.jedis;

import com.wujiuye.hotkit.redis.template.ClusterReactiveRedisCacheTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Jedis不支持响应式编程API
 *
 * @author wujiuye 2020/11/02
 */
public class JedisReactiveRedisCacheTemplateImpl extends ClusterReactiveRedisCacheTemplate {

    public JedisReactiveRedisCacheTemplateImpl(String cluster) {
        super(cluster);
    }

    @Override
    public Mono<Void> setExpireTime(Object key, int seconds) {
        return Mono.error(new UnsupportedOperationException("Jedis 不支持响应式"));
    }

    @Override
    public Mono<Void> delKey(Object key) {
        return Mono.error(new UnsupportedOperationException("Jedis 不支持响应式"));
    }

    @Override
    public Mono<Void> setValue(Object key, Object value) {
        return Mono.error(new UnsupportedOperationException("Jedis 不支持响应式"));
    }

    @Override
    public <T> Mono<T> getValue(Object key, Class<T> valueClass) {
        return Mono.error(new UnsupportedOperationException("Jedis 不支持响应式"));
    }

    @Override
    public Mono<Void> putMap(Object firstKey, Object scondKey, Object value) {
        return Mono.error(new UnsupportedOperationException("Jedis 不支持响应式"));
    }

    @Override
    public <T> Mono<T> getMap(Object firstKey, Object scondKey, Class<T> valueClass) {
        return Mono.error(new UnsupportedOperationException("Jedis 不支持响应式"));
    }

    @Override
    public Flux<String> getMapKeys(Object firstKey) {
        return Flux.error(new UnsupportedOperationException("Jedis 不支持响应式"));
    }

}
