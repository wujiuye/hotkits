package com.wujiuye.hotkit.util;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类
 *
 * @author wujiuye 2020/05/14
 */
public class CollectionUtils {

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

}
