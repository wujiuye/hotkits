package com.wujiuye.hotkit.redis.template;

import com.wujiuye.hotkit.redis.impl.lettuce.LettuceReactiveRedisCacheTemplateImpl;
import com.wujiuye.hotkit.redis.serializable.CacheSerializable;
import com.wujiuye.hotkit.redis.serializable.JsonCacheSerializable;
import com.wujiuye.hotkit.redis.serializable.StringCacheSerializable;
import com.wujiuye.hotkit.redis.util.RedisSpiLoader;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * redis缓存访问模版方法-响应式API，不支持使用Jedis
 *
 * @author wujiuye 2020/09/23
 * @see LettuceReactiveRedisCacheTemplateImpl
 */
public interface ReactiveRedisCacheTemplate {

    /**
     * 获取值对象缓存序列化策略
     *
     * @return
     */
    default CacheSerializable getValueCacheSerializable() {
        return RedisSpiLoader.loadFistNotDefaultService(this.getClass().getClassLoader(),
                CacheSerializable.class, JsonCacheSerializable.class);
    }

    /**
     * 获取key对象缓存序列化策略
     *
     * @return
     */
    default CacheSerializable getKeyCacheSerializable() {
        return RedisSpiLoader.loadFistNotDefaultService(this.getClass().getClassLoader(),
                CacheSerializable.class, StringCacheSerializable.class);
    }

    /**
     * 为key设置过期时间
     *
     * @param key     缓存key
     * @param seconds 过期时间，单位秒
     */
    Mono<Void> setExpireTime(Object key, int seconds);

    /**
     * 删除key
     *
     * @param key 缓存key
     */
    Mono<Void> delKey(Object key);

    /**
     * set
     *
     * @param key   缓存key
     * @param value 缓存对象
     */
    Mono<Void> setValue(Object key, Object value);

    /**
     * 从缓存获取对象
     *
     * @param key        缓存key
     * @param valueClass 值类型
     * @param <T>
     * @return
     */
    <T> Mono<T> getValue(Object key, Class<T> valueClass);

    /**
     * hset
     *
     * @param firstKey 缓存key
     * @param scondKey 缓存field
     * @param value    缓存value
     */
    Mono<Void> putMap(Object firstKey, Object scondKey, Object value);

    /**
     * hget
     *
     * @param firstKey   缓存key
     * @param scondKey   缓存field
     * @param valueClass 缓存value的类型
     * @param <T>
     * @return
     */
    <T> Mono<T> getMap(Object firstKey, Object scondKey, Class<T> valueClass);

    /**
     * hkeys
     *
     * @param firstKey 缓存key
     * @return
     */
    Flux<String> getMapKeys(Object firstKey);

}
