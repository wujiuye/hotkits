package com.wujiuye.hotkit.redis.util;

import java.util.ServiceLoader;

/**
 * SPI加载器
 *
 * @author wujiuye 2020/08/21
 */
public class RedisSpiLoader {

    /**
     * 加载第一次非指定默认类型的接口实现类
     *
     * @param classLoader      类加载器
     * @param interfaceClass   接口
     * @param defaultImplClass 默认接口实现类
     * @param <T>
     * @return 实现类的实例，如果不存在，则返回的就是defaultImplClass的实例
     */
    public static <T> T loadFistNotDefaultService(ClassLoader classLoader,
                                                  Class<T> interfaceClass, Class<? extends T> defaultImplClass) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(interfaceClass, classLoader);
        T resultImpl = null;
        T defaultResult = null;
        for (T impl : serviceLoader) {
            if (impl.getClass() != defaultImplClass) {
                resultImpl = impl;
                break;
            } else {
                defaultResult = impl;
            }
        }
        if (resultImpl == null) {
            if (defaultResult == null) {
                try {
                    defaultResult = defaultImplClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    //
                }
            }
        }
        return resultImpl == null ? defaultResult : resultImpl;
    }

}
