package com.wujiuye.hotkit.redis.serializable;

/**
 * 缓存序列化
 *
 * @author wujiuye 2020/09/23
 */
public interface CacheSerializable {

    /**
     * 序列化
     *
     * @param value
     * @return
     */
    String serialize(Object value);

    /**
     * 反序列化
     *
     * @param value
     * @param <T>
     * @return
     */
    <T> T deserialize(String value, Class<T> tClass);

}
