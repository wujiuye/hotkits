package com.wujiuye.hotkit.spring.common.config.reload;

/**
 * 配置改变探测器
 *
 * @author wujiuye 2020/09/15
 */
public interface ConfigChangeDetector {

    /**
     * 重加载配置
     *
     * @param key PropertySource的名称
     */
    void reload(String key);

}
