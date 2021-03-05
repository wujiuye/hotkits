package com.wujiuye.hotkit.json.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.lang.reflect.Modifier;

/**
 * 自定义的gson序列化策略
 *
 * @author wujiuye 2020/04/26
 */
public class GsonExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        // 如果字段被transient关键字修饰，则不参与序列化(Gson默认已经支持)
        return fieldAttributes.hasModifier(Modifier.TRANSIENT);
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }

}
