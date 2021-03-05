package com.wujiuye.hotkit.redis.template;

import com.wujiuye.hotkit.redis.impl.jedis.JedisRedisCacheTemplateImpl;
import com.wujiuye.hotkit.redis.serializable.CacheSerializable;
import com.wujiuye.hotkit.redis.serializable.JsonCacheSerializable;
import com.wujiuye.hotkit.redis.serializable.StringCacheSerializable;
import com.wujiuye.hotkit.redis.util.RedisSpiLoader;

import java.util.Set;

/**
 * redis缓存访问模版方法
 *
 * @author wujiuye 2020/09/23
 * @see JedisRedisCacheTemplateImpl
 */
public interface RedisCacheTemplate {

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
    void setExpireTime(Object key, int seconds);

    /**
     * 删除key
     *
     * @param key 缓存key
     */
    void delKey(Object key);

    /**
     * set
     *
     * @param key   缓存key
     * @param value 缓存对象
     */
    void setValue(Object key, Object value);

    /**
     * 从缓存获取对象
     *
     * @param key        缓存key
     * @param valueClass 值类型
     * @param <T>
     * @return
     */
    <T> T getValue(Object key, Class<T> valueClass);

    /**
     * hset
     *
     * @param firstKey 缓存key
     * @param scondKey 缓存field
     * @param value    缓存value
     */
    void putMap(Object firstKey, Object scondKey, Object value);

    /**
     * hget
     *
     * @param firstKey   缓存key
     * @param scondKey   缓存field
     * @param valueClass 缓存value的类型
     * @param <T>
     * @return
     */
    <T> T getMap(Object firstKey, Object scondKey, Class<T> valueClass);

    /**
     * hkeys
     *
     * @param firstKey 缓存key
     * @return
     */
    Set<String> getMapKeys(Object firstKey);

}
