package com.wujiuye.hotkit.json;

import com.wujiuye.hotkit.json.gson.GsonParser;
import com.wujiuye.hotkit.json.jackson.JacksonParser;
import com.wujiuye.hotkit.json.util.StringUtils;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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

    private static AtomicReference<SerializeConfig> defaultSerializeConfig
            = new AtomicReference<>(new SerializeConfig());

    public static void setDefaultSerializeConfig(SerializeConfig config) {
        defaultSerializeConfig.set(config);
    }

    public static <T> String toJsonString(T obj) {
        SerializeConfig config = defaultSerializeConfig.get();
        return chooseJsonParser.toJsonString(obj, config);
    }

    public static <T> T fromJson(String jsonStr, Class<T> tClass) {
        if (StringUtils.isNullOrEmpty(jsonStr)) {
            return null;
        }
        return chooseJsonParser.fromJson(jsonStr, tClass);
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

    public static <T> List<T> fromJsonArray(InputStream jsonIn, TypeReference<List<T>> typeReference) {
        return chooseJsonParser.fromJsonArray(jsonIn, typeReference);
    }

    public static <K, V> Map<K, V> fromJsonMap(String jsonStr, TypeReference<Map<K, V>> typeReference) {
        if (StringUtils.isNullOrEmpty(jsonStr)) {
            return null;
        }
        return chooseJsonParser.fromJsonMap(jsonStr, typeReference);
    }

    public static <K, V> Map<K, V> fromJsonMap(InputStream jsonIn, TypeReference<Map<K, V>> typeReference) {
        return chooseJsonParser.fromJsonMap(jsonIn, typeReference);
    }

}
