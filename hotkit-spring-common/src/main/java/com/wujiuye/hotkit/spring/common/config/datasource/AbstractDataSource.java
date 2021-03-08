package com.wujiuye.hotkit.spring.common.config.datasource;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象动态数据源
 *
 * @author wujiuye 2020/09/15
 */
public abstract class AbstractDataSource<T, R> implements ReadOnlyDataSource<T, R> {

    private Converter<T, R> converter;
    private R data;

    private List<ConfigChangeListener<R>> listeners;

    public AbstractDataSource(Converter<T, R> converter) {
        this.converter = converter;
        this.listeners = new ArrayList<>();
    }

    @SafeVarargs
    public final void addListeners(ConfigChangeListener<R>... listeners) {
        if (listeners.length > 0) {
            this.listeners.addAll(Arrays.stream(listeners).collect(Collectors.toList()));
        }
    }

    public void setListeners(List<ConfigChangeListener<R>> listeners) {
        if (CollectionUtils.isEmpty(listeners)) {
            return;
        }
        this.listeners.addAll(listeners);
    }

    /**
     * 加载配置
     */
    public void loadConfig() {
        T data = this.load();
        R result = null;
        if (data != null) {
            result = converter.convert(data);
        }
        this.data = result;
        doNotify(result);
    }

    protected void doNotify(R data) {
        if (listeners != null) {
            for (ConfigChangeListener<R> listener : listeners) {
                listener.onUpdate(data);
            }
        }
    }

    @Override
    public R read() {
        return this.data;
    }

}
