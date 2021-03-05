package com.wujiuye.hotkit.redis.serializable;

/**
 * 处理基本数据类型
 *
 * @author wujiuye 2020/11/05
 */
public abstract class BaseCacheSerializable implements CacheSerializable {

    @Override
    public String serialize(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof Boolean) {
            return value.toString();
        }
        return doSerialize(value);
    }

    protected abstract String doSerialize(Object value);

    @Override
    public <T> T deserialize(String value, Class<T> tClass) {
        if (value == null) {
            return null;
        }
        if (tClass == String.class) {
            return (T) value;
        }
        if (tClass == Integer.class) {
            return (T) new Integer(value);
        }
        if (tClass == Long.class) {
            return (T) new Long(value);
        }
        if (tClass == Float.class) {
            return (T) new Float(value);
        }
        if (tClass == Double.class) {
            return (T) new Double(value);
        }
        if (tClass == Boolean.class) {
            return (T) new Boolean(value);
        }
        return doDeserialize(value, tClass);
    }

    protected abstract <T> T doDeserialize(String value, Class<T> tClass);

}
