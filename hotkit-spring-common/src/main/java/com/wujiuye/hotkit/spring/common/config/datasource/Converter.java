package com.wujiuye.hotkit.spring.common.config.datasource;

/**
 * 数据转换器
 *
 * @author wujiuye 2020/09/15
 */
@FunctionalInterface
public interface Converter<T, R> {

    /**
     * 数据转换
     *
     * @param data
     * @return
     */
    R convert(T data);

    /**
     * 默认转换器
     */
    Converter DEFAULT = data -> data;

}