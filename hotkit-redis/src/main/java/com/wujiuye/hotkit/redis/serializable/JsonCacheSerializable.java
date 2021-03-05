package com.wujiuye.hotkit.redis.serializable;

import com.msyc.common.json.JsonUtils;

/**
 * json序列化
 *
 * @author wujiuye 2020/09/23
 */
public class JsonCacheSerializable extends BaseCacheSerializable {

    @Override
    public String doSerialize(Object value) {
        return JsonUtils.toJsonString(value);
    }

    @Override
    public <T> T doDeserialize(String value, Class<T> tClass) {
        return JsonUtils.fromJson(value, tClass);
    }

}
