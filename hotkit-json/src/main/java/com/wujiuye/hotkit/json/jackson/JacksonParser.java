package com.wujiuye.hotkit.json.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.wujiuye.hotkit.json.JsonParser;
import com.wujiuye.hotkit.json.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jackson
 *
 * @author wujiuye 2020/04/26
 */
public class JacksonParser implements JsonParser {

    private final static ObjectMapperSub FROM_JSON_JACKSON;
    private static Map<String, ObjectMapperSub> OBJECT_MAPPER_MAP = new HashMap<>();

    static {
        ObjectMapperSub objectMapper = new ObjectMapperSub();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule timeModule = new SimpleModule();
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        timeModule.addDeserializer(Date.class, new DateDeserializer());
        timeModule.addDeserializer(Double.class, new DoubleDeserializer());
        timeModule.addDeserializer(String.class, new StringDeserializer());
        objectMapper.registerModule(timeModule);
        FROM_JSON_JACKSON = objectMapper;
    }

    private ObjectMapperSub getObjectMapper(boolean serializeNulls, String datePattern) {
        String key = (datePattern == null ? "null" : datePattern) + "::" + serializeNulls;
        ObjectMapperSub objectMapper = OBJECT_MAPPER_MAP.get(key);
        if (objectMapper == null) {
            synchronized (this) {
                objectMapper = OBJECT_MAPPER_MAP.get(key);
                if (objectMapper == null) {
                    objectMapper = new ObjectMapperSub();
                    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                    if (!serializeNulls) {
                        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    }
                    SimpleModule timeModule = new SimpleModule();
                    timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(8, datePattern));
                    timeModule.addSerializer(Date.class, new DateSerializer(datePattern));
                    timeModule.addSerializer(Double.class, new DoubleSerializer());
                    timeModule.addSerializer(String.class, new StringSerializer());
                    objectMapper.registerModule(timeModule);
                    Map<String, ObjectMapperSub> newMap = new HashMap<>(OBJECT_MAPPER_MAP);
                    newMap.put(key, objectMapper);
                    OBJECT_MAPPER_MAP = newMap;
                }
            }
        }
        return objectMapper;
    }

    @Override
    public <T> String toJsonString(T obj, boolean serializeNulls, String datePattern) {
        try {
            ObjectMapperSub objectMapper = getObjectMapper(serializeNulls, datePattern);
            JacksonExclusionStrategy strategy = JacksonExclusionStrategy.getInstance(obj.getClass());
            if (strategy != null) {
                objectMapper.putStrategy(obj.getClass(), strategy);
            }
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(String jsonStr, Class<T> tClass) {
        try {
            return FROM_JSON_JACKSON.readValue(jsonStr, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(InputStream jsonIn, Class<T> tClass) {
        try {
            return FROM_JSON_JACKSON.readValue(jsonIn, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(String jsonStr, Type type) {
        try {
            TypeFactory typeFactory = FROM_JSON_JACKSON.getTypeFactory();
            JavaType javaType = typeFactory.constructType(type);
            return FROM_JSON_JACKSON.readValue(jsonStr, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(InputStream jsonIn, Type type) {
        try {
            TypeFactory typeFactory = FROM_JSON_JACKSON.getTypeFactory();
            JavaType javaType = typeFactory.constructType(type);
            return FROM_JSON_JACKSON.readValue(jsonIn, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> fromJsonArray(String jsonStr, TypeReference<List<T>> typeReference) {
        try {
            return FROM_JSON_JACKSON.readValue(jsonStr, new com.fasterxml.jackson.core.type.TypeReference<List<T>>() {
                @Override
                public Type getType() {
                    return typeReference.getType();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <K, V> Map<K, V> fromJsonMap(String jsonStr, TypeReference<Map<K, V>> typeReference) {
        try {
            return FROM_JSON_JACKSON.readValue(jsonStr, new com.fasterxml.jackson.core.type.TypeReference<Map<K, V>>() {
                @Override
                public Type getType() {
                    return typeReference.getType();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
