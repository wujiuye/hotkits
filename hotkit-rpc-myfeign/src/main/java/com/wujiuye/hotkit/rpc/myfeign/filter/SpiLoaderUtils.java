package com.wujiuye.hotkit.rpc.myfeign.filter;

import java.util.ServiceLoader;

/**
 * SPI加载工具类
 *
 * @author wujiuye 2020/07/21
 */
public class SpiLoaderUtils {

    public static <T> T getFirstNotDefaultService(Class<T> tClass, Class<? extends T> defaultClass) {
        ServiceLoader<T> tServiceLoader = ServiceLoader.load(tClass);
        T def = null;
        for (T obj : tServiceLoader) {
            if (obj.getClass() != defaultClass) {
                return obj;
            } else {
                def = obj;
            }
        }
        return def;
    }

}
