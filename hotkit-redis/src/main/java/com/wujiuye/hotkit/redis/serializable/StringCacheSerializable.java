package com.wujiuye.hotkit.redis.serializable;

/**
 * 使用Java对象的toString方法
 *
 * @author wujiuye 2020/09/23
 */
public class StringCacheSerializable extends BaseCacheSerializable {

    @Override
    protected String doSerialize(Object value) {
        return value.toString();
    }

    @Override
    protected <T> T doDeserialize(String value, Class<T> tClass) {
        throw new UnsupportedOperationException("StringCacheSerializable不支持类型[" + tClass.getName() + "]反序列化");
    }

}
