package com.wujiuye.hotkit.rpc.playback;

import com.wujiuye.hotkit.json.JsonUtils;
import com.wujiuye.hotkit.json.TypeReference;
import com.wujiuye.hotkit.json.util.StringUtils;
import com.wujiuye.hotkit.util.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 反射调用工具类
 *
 * @author wujiuye 2020/07/20
 */
class ActionInvokeUtils {

    private final static Logger log = LoggerFactory.getLogger(ActionInvokeUtils.class);

    /**
     * 调用方法回放操作
     *
     * @param object 目标对象
     * @param record 失败操作记录
     * @return
     */
    public static Object invoke(Object object, ActionRecord record) {
        PlaybackContext.setCurLog(record);
        try {
            Class<?>[] paramTypes = getParamTypes(record.getMethodDescriptor());
            Method method = object.getClass().getMethod(record.getMethodName(), paramTypes);
            Object[] args = getParams(paramTypes, record.getParams());
            return method.invoke(object, args);
        } catch (NoSuchMethodException e) {
            log.error("not found method: {}", record);
            return null;
        } catch (Exception e) {
            log.error("invoke method error: " + e.getMessage(), e);
            return null;
        } finally {
            PlaybackContext.clear();
        }
    }

    /**
     * 获取方法参数类型
     *
     * @param methodDescriptor 方法描述符
     * @return
     * @throws ClassNotFoundException
     */
    private static Class<?>[] getParamTypes(String methodDescriptor) throws ClassNotFoundException {
        if (StringUtils.isNullOrEmpty(methodDescriptor)) {
            return new Class[0];
        }
        return ReflectUtils.getArgumentTypes(methodDescriptor);
    }

    /**
     * 获取调用方法所需参数
     *
     * @param paramTypes 参数类型数组
     * @param paramMap   参数映射
     * @return
     */
    private static Object[] getParams(Class<?>[] paramTypes, Map<Integer, Object> paramMap) {
        if (paramTypes == null || paramTypes.length == 0) {
            return new Object[0];
        }
        Object[] args = new Object[paramTypes.length];
        if (paramMap == null) {
            return args;
        }
        for (Map.Entry<Integer, Object> entry : paramMap.entrySet()) {
            int index = entry.getKey();
            if (index < 0 || index > args.length) {
                continue;
            }
            if (paramTypes[index] == entry.getValue().getClass()) {
                args[index] = entry.getValue();
                continue;
            }
            if (paramTypes[index].isPrimitive() && entry.getValue().getClass().isPrimitive()) {
                if (paramTypes[index] == String.class) {
                    args[index] = entry.getValue().toString();
                } else if (paramTypes[index] == Boolean.class || paramTypes[index] == boolean.class) {
                    args[index] = Boolean.valueOf(entry.getValue().toString());
                } else if (paramTypes[index] == Integer.class || paramTypes[index] == int.class) {
                    args[index] = Integer.valueOf(entry.getValue().toString());
                } else if (paramTypes[index] == Long.class || paramTypes[index] == long.class) {
                    args[index] = Long.valueOf(entry.getValue().toString());
                } else if (paramTypes[index] == Double.class || paramTypes[index] == double.class) {
                    args[index] = new BigDecimal(entry.getValue().toString()).doubleValue();
                } else if (paramTypes[index] == Float.class || paramTypes[index] == float.class) {
                    args[index] = new BigDecimal(entry.getValue().toString()).floatValue();
                }
                continue;
            }
            if (paramTypes[index] == BigDecimal.class) {
                args[index] = new BigDecimal(entry.getValue().toString());
                continue;
            }
            if (entry.getValue() instanceof Map) {
                if (Map.class.isAssignableFrom(paramTypes[index])) {
                    args[index] = entry.getValue();
                } else {
                    String json = JsonUtils.toJsonString(entry.getValue());
                    if (paramTypes[index].isArray()) {
                        continue;
                    }
                    if (List.class.isAssignableFrom(paramTypes[index])) {
                        java.lang.reflect.Type type = paramTypes[index].getGenericSuperclass();
                        args[index] = JsonUtils.fromJsonArray(json, new TypeReference<List<Object>>() {
                            @Override
                            public java.lang.reflect.Type getType() {
                                return type;
                            }
                        });
                    } else {
                        args[index] = JsonUtils.fromJson(json, paramTypes[index]);
                    }
                }
            }
        }
        return args;
    }

}
