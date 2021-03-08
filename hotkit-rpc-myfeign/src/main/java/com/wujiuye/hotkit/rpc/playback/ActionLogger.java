package com.wujiuye.hotkit.rpc.playback;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wujiuye 2020/07/20
 */
public interface ActionLogger {

    /**
     * 追加Spring Bean方法调用操作
     *
     * @param beanName
     * @param tClass
     * @param method
     * @param maxPlaybackCnt 最大回放重试次数
     * @param params
     */
    void apendAction(String beanName, Class<?> tClass, Method method, int maxPlaybackCnt, Map<Integer, Object> params);

    /**
     * 追加方法调用
     *
     * @param tClass
     * @param method
     * @param maxPlaybackCnt 最大回放重试次数
     * @param params
     */
    default void apendAction(Class<?> tClass, Method method, int maxPlaybackCnt, Object... params) {
        if (params.length > 0) {
            Map<Integer, Object> objectMap = new HashMap<>();
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    objectMap.put(i, params[i]);
                }
            }
            apendAction(tClass, method, maxPlaybackCnt, objectMap);
        } else {
            apendAction(tClass, method, maxPlaybackCnt);
        }
    }

    /**
     * 追加不是Spring Bean或者没有设置BeanName的操作
     *
     * @param tClass
     * @param method
     * @param maxPlaybackCnt 最大回放重试次数
     * @param params
     */
    default void apendAction(Class<?> tClass, Method method, int maxPlaybackCnt, Map<Integer, Object> params) {
        apendAction(null, tClass, method, maxPlaybackCnt, params);
    }

    /**
     * 追加不需要参数的方法调用
     *
     * @param tClass
     * @param method
     * @param maxPlaybackCnt 最大回放重试次数
     */
    default void apendAction(Class<?> tClass, Method method, int maxPlaybackCnt) {
        apendAction(null, tClass, method, maxPlaybackCnt, null);
    }

}
