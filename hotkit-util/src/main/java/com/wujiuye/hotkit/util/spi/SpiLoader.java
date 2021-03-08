package com.wujiuye.hotkit.util.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * SPI加载器
 *
 * @author wujiuye 2020/08/21
 */
public class SpiLoader {

    /**
     * 加载所有的接口实现类
     *
     * @param classLoader    类加载器
     * @param interfaceClass 接口
     * @param <T>
     * @return 注册的所有接口实例
     */
    public static <T> List<T> loadAllService(ClassLoader classLoader, Class<T> interfaceClass) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(interfaceClass, classLoader);
        List<T> resultImpl = new ArrayList<>();
        for (T impl : serviceLoader) {
            resultImpl.add(impl);
        }
        return resultImpl;
    }

    /**
     * 加载所有的接口实现类
     *
     * @param classLoader    类加载器
     * @param interfaceClass 接口
     * @param <T>
     * @return 注册的所有接口实例
     */
    public static <T> List<T> loadAllServiceAndSort(ClassLoader classLoader, Class<T> interfaceClass) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(interfaceClass, classLoader);
        List<T> resultImpl = new ArrayList<>();
        for (T impl : serviceLoader) {
            resultImpl.add(impl);
        }
        resultImpl.sort((o1, o2) -> {
            SpiOrder order1 = o1.getClass().getAnnotation(SpiOrder.class);
            SpiOrder order2 = o2.getClass().getAnnotation(SpiOrder.class);
            int orderValue1 = order1 == null ? Integer.MAX_VALUE : order1.value();
            int orderValue2 = order1 == null ? Integer.MAX_VALUE : order2.value();
            return orderValue1 - orderValue2;
        });
        return resultImpl;
    }

    /**
     * 加载第一个接口实现类
     *
     * @param classLoader    类加载器
     * @param interfaceClass 接口
     * @param <T>
     * @return 实现类的实例，如果不存在，则返回null
     */
    public static <T> T loadFistService(ClassLoader classLoader, Class<T> interfaceClass) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(interfaceClass, classLoader);
        T resultImpl = null;
        for (T impl : serviceLoader) {
            resultImpl = impl;
            break;
        }
        return resultImpl;
    }

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
