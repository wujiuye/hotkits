package com.wujiuye.hotkit.spring.common.config.datasource;

/**
 * 只读数据源
 *
 * @param <T>
 * @param <R>
 * @author wujiuye 2020/09/15
 */
public interface ReadOnlyDataSource<T, R> {

    /**
     * 加载配置
     *
     * @return
     */
    T load();

    /**
     * 读取配置
     *
     * @return
     */
    R read();

}
