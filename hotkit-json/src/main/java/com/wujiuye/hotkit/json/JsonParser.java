package com.wujiuye.hotkit.json;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author wujiuye 2020/04/26
 */
public interface JsonParser {

    <T> String toJsonString(T obj, boolean serializeNulls, String pattern);

    <T> T fromJson(String jsonStr, Class<T> tClass);

    <T> T fromJson(InputStream jsonIn, Class<T> tClass);

    <T> T fromJson(String jsonStr, Type type);

    <T> T fromJson(InputStream jsonIn, Type type);

    <T> List<T> fromJsonArray(String jsonStr, TypeReference<List<T>> typeReference);

    <K, V> Map<K, V> fromJsonMap(String jsonStr, TypeReference<Map<K, V>> typeReference);

}
