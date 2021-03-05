package com.wujiuye.hotkit.json;

import com.wujiuye.hotkit.json.gson.GsonParser;
import com.wujiuye.hotkit.json.jackson.JacksonParser;
import com.wujiuye.hotkit.json.util.StringUtils;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * json序列化适配器
 *
 * @author wujiuye 2020/04/26
 */
public class JsonUtils {

    private static JsonParser chooseJsonParser;

    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            classLoader.loadClass("com.google.gson.Gson");
            chooseJsonParser = new GsonParser();
        } catch (ClassNotFoundException e) {
            try {
                classLoader.loadClass("com.fasterxml.jackson.databind.ObjectMapper");
                chooseJsonParser = new JacksonParser();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("未找到任何json包，请先在当前项目的依赖配置文件中加入 gson或fackson");
            }
        }
    }

    public static <T> String toJsonString(T obj) {
        return toJsonString(obj, false, null);
    }

    public static <T> String toJsonString(T obj, boolean serializeNulls) {
        return toJsonString(obj, serializeNulls, null);
    }

    /**
     * 序列化
     *
     * @param obj
     * @param serializeNulls 是否需要序列化值为null的字段
     * @param <T>
     * @return
     */
    public static <T> String toJsonString(T obj, boolean serializeNulls, String datePattern) {
        return chooseJsonParser.toJsonString(obj, serializeNulls, datePattern);
    }

    public static <T> T fromJson(String jsonStr, Class<T> tClass) {
        if (StringUtils.isNullOrEmpty(jsonStr)) {
            return null;
        }
        return chooseJsonParser.fromJson(jsonStr, tClass);
    }

    public static <K, V> Map<K, V> fromJsonMap(String jsonStr, TypeReference<Map<K, V>> typeReference) {
        if (StringUtils.isNullOrEmpty(jsonStr)) {
            return null;
        }
        return chooseJsonParser.fromJsonMap(jsonStr, typeReference);
    }

    public static <T> T fromJson(InputStream jsonIn, Class<T> tClass) {
        return chooseJsonParser.fromJson(jsonIn, tClass);
    }

    public static <T> T fromJson(String jsonStr, Type type) {
        return chooseJsonParser.fromJson(jsonStr, type);
    }

    public static <T> T fromJson(InputStream jsonIn, Type type) {
        return chooseJsonParser.fromJson(jsonIn, type);
    }

    public static <T> List<T> fromJsonArray(String jsonStr, TypeReference<List<T>> typeReference) {
        if (StringUtils.isNullOrEmpty(jsonStr)) {
            return null;
        }
        return chooseJsonParser.fromJsonArray(jsonStr, typeReference);
    }

}
