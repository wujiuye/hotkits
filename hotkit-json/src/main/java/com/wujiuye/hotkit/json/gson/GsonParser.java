package com.wujiuye.hotkit.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wujiuye.hotkit.json.JsonParser;
import com.wujiuye.hotkit.json.SerializeConfig;
import com.wujiuye.hotkit.json.TypeReference;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * gson
 *
 * @author wujiuye 2020/04/26
 */
public class GsonParser implements JsonParser {

    private static final Gson FROM_JSON_GSON;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter(null))
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(null))
                .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
                .registerTypeAdapter(String.class, new StringTypeAdapter())
                .addDeserializationExclusionStrategy(new GsonExclusionStrategy());
        FROM_JSON_GSON = gsonBuilder.create();
    }

    @Override
    public <T> String toJsonString(T obj, SerializeConfig config) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter(config.getDateFormat()))
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(config.getDateFormat()))
                .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
                .registerTypeAdapter(String.class, new StringTypeAdapter())
                .addSerializationExclusionStrategy(new GsonExclusionStrategy());
        if (config.isSerializeNulls()) {
            gsonBuilder.serializeNulls();
        }
        return gsonBuilder.create().toJson(obj);
    }

    @Override
    public <T> T fromJson(String jsonStr, Class<T> tClass) {
        return FROM_JSON_GSON.fromJson(jsonStr, tClass);
    }

    @Override
    public <T> T fromJson(InputStream jsonIn, Class<T> tClass) {
        return FROM_JSON_GSON.fromJson(new InputStreamReader(jsonIn), tClass);
    }

    @Override
    public <T> T fromJson(String jsonStr, Type type) {
        return FROM_JSON_GSON.fromJson(jsonStr, type);
    }

    @Override
    public <T> T fromJson(InputStream jsonIn, Type type) {
        return FROM_JSON_GSON.fromJson(new InputStreamReader(jsonIn), type);
    }

    @Override
    public <T> List<T> fromJsonArray(String jsonStr, TypeReference<List<T>> typeReference) {
        return FROM_JSON_GSON.fromJson(jsonStr, typeReference.getType());
    }

    @Override
    public <T> List<T> fromJsonArray(InputStream jsonIn, TypeReference<List<T>> typeReference) {
        return FROM_JSON_GSON.fromJson(new InputStreamReader(jsonIn), typeReference.getType());
    }

    @Override
    public <K, V> Map<K, V> fromJsonMap(String jsonStr, TypeReference<Map<K, V>> typeReference) {
        return FROM_JSON_GSON.fromJson(jsonStr, typeReference.getType());
    }

    @Override
    public <K, V> Map<K, V> fromJsonMap(InputStream jsonIn, TypeReference<Map<K, V>> typeReference) {
        return FROM_JSON_GSON.fromJson(new InputStreamReader(jsonIn), typeReference.getType());
    }

}
