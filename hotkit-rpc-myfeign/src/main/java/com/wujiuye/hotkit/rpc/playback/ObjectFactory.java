package com.wujiuye.hotkit.rpc.playback;

/**
 * 对象工厂
 *
 * @author wujiuye 2020/07/20
 */
public interface ObjectFactory {

    /**
     * 根据bean名称、类名获取实例
     *
     * @param beanName  spring beanName，可能为空
     * @param className
     * @return
     */
    Object getObject(String beanName, String className);

}
