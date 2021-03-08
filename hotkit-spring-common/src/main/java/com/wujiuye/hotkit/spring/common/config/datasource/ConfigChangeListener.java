package com.wujiuye.hotkit.spring.common.config.datasource;

/**
 * 配置改变监听器
 *
 * @author wujiuye 2020/09/15
 */
public interface ConfigChangeListener<T> {

    /**
     * 配置改变监听器
     *
     * @param newData 新的数据
     */
    void onUpdate(T newData);

}
