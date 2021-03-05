package com.wujiuye.hotkit.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 适配gson和jackson
 *
 * @param <T>
 * @author wujiuye 2020/05/26
 */
public abstract class TypeReference<T> {

    protected final Type _type;

    protected TypeReference() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new IllegalArgumentException("Internal error: MsycTypeReference constructed without actual type information");
        } else {
            this._type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        }
    }

    public Type getType() {
        return this._type;
    }

}
