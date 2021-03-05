package com.wujiuye.hotkit.json.jackson;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * jackson自定义忽略字段策略，统一使用TRANSIENT关键字
 *
 * @author wujiuye 2020/05/08
 */
public class JacksonExclusionStrategy extends SimpleFilterProvider {

    @JsonFilter("myFilter")
    public interface MyFilter {
    }

    private JacksonExclusionStrategy() {

    }

    private static Map<Class<?>, JacksonExclusionStrategy> STRATEGY_MAP = new HashMap<>();

    private static JacksonExclusionStrategy createStrategy(Class<?> cla) {
        // 忽略被transient声明的字段
        Field[] fields = cla.getDeclaredFields();
        StringBuilder ignoreFields = new StringBuilder();
        for (Field field : fields) {
            if ((field.getModifiers() & Modifier.TRANSIENT) == Modifier.TRANSIENT) {
                if (ignoreFields.length() > 0) {
                    ignoreFields.append(",");
                }
                ignoreFields.append(field.getName());
            }
        }
        if (ignoreFields.length() == 0) {
            return null;
        }
        String ignoreStr = ignoreFields.toString();
        JacksonExclusionStrategy jacksonExclusionStrategy = new JacksonExclusionStrategy();
        // 调用 SimpleBeanPropertyFilter 的 serializeAllExcept 方法
        // 或
        // 重写 SimpleBeanPropertyFilter 的 serializeAsField 方法过滤属性
        jacksonExclusionStrategy.addFilter("myFilter", SimpleBeanPropertyFilter.serializeAllExcept(ignoreStr.split(",")));
        return jacksonExclusionStrategy;
    }

    public static JacksonExclusionStrategy getInstance(final Class<?> cla) {
        JacksonExclusionStrategy strategy = STRATEGY_MAP.get(cla);
        if (strategy == null) {
            synchronized (cla) {
                strategy = STRATEGY_MAP.get(cla);
                if (strategy == null) {
                    strategy = createStrategy(cla);
                    Map<Class<?>, JacksonExclusionStrategy> newMap = new HashMap<>(STRATEGY_MAP);
                    newMap.put(cla, strategy);
                    STRATEGY_MAP = newMap;
                }
            }
        }
        return strategy;
    }

}
