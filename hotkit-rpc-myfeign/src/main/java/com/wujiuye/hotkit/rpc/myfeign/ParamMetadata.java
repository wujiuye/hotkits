package com.wujiuye.hotkit.rpc.myfeign;

import java.lang.annotation.Annotation;

/**
 * 方法参数映射
 *
 * @author wujiuye 2020/07/02
 */
public class ParamMetadata {

    /**
     * 参数上的注解
     */
    private Annotation[] paramAnnotations;
    /**
     * 参数类型
     */
    private Class<?> paramType;
    /**
     * 是否基本数据类型
     */
    private boolean isPrimitive;
    /**
     * 参数在方法参数列表的索引
     */
    private int paramIndexOnMethod;

    public Annotation[] getParamAnnotations() {
        return paramAnnotations;
    }

    public void setParamAnnotations(Annotation[] paramAnnotations) {
        this.paramAnnotations = paramAnnotations;
    }

    public Class<?> getParamType() {
        return paramType;
    }

    public void setParamType(Class<?> paramType) {
        this.paramType = paramType;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public void setPrimitive(boolean primitive) {
        isPrimitive = primitive;
    }

    public int getParamIndexOnMethod() {
        return paramIndexOnMethod;
    }

    public void setParamIndexOnMethod(int paramIndexOnMethod) {
        this.paramIndexOnMethod = paramIndexOnMethod;
    }

}
