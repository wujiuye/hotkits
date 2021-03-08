package com.wujiuye.hotkit.spring.common.config.reload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 抽象配置改变探测器
 *
 * @author wujiuye 2020/09/15
 */
public abstract class AbstractConfigChangeDetector implements ConfigChangeDetector {

    @Autowired
    private AbstractEnvironment environment;
    @Autowired
    private ContextRefresher contextRefresher;

    @Override
    public void reload(String key) {
        MutablePropertySources mutablePropertySources = environment.getPropertySources();
        Map<String, Object> configMap = loadConfig(key);
        if (configMap == null) {
            return;
        }
        if (mutablePropertySources.contains(key)) {
            if (change(((MapPropertySource) mutablePropertySources.get(key)).getSource(), configMap)) {
                mutablePropertySources.replace(key, new MapPropertySource(key, configMap));
                contextRefresher.refresh();
            } else {
                configMap.clear(); // help gc
            }
        } else {
            mutablePropertySources.addLast(new MapPropertySource(key, configMap));
            contextRefresher.refresh();
        }
    }

    /**
     * 子类实现加载
     *
     * @param key PropertySource的名称
     * @return
     */
    protected abstract Map<String, Object> loadConfig(String key);

    /**
     * 新旧配置对比判断是否需要refresh
     *
     * @param oldMap 当前环境变量
     * @param newMap 新加载的配置信息
     * @return
     */
    private boolean change(final Map<String, Object> oldMap, final Map<String, Object> newMap) {
        if (CollectionUtils.isEmpty(oldMap) && CollectionUtils.isEmpty(newMap)) {
            return false;
        }
        if (CollectionUtils.isEmpty(oldMap) || CollectionUtils.isEmpty(newMap)) {
            return true;
        }
        if (oldMap.size() != newMap.size()) {
            return true;
        }
        for (Map.Entry<String, Object> entry : newMap.entrySet()) {
            if (!oldMap.containsKey(entry.getKey())) {
                return true;
            }
            if (!oldMap.get(entry.getKey()).equals(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将对象转为map
     *
     * @param prefix
     * @param object
     * @return
     */
    protected Map<String, Object> obj2Map(String prefix, Object object) {
        Map<String, Object> map = new HashMap<>();
        if (object instanceof Map) {
            ((Map) object).entrySet().forEach(entry -> {
                Map.Entry _entry = (Map.Entry) entry;
                map.put(prefix + "." + _entry.getKey(), _entry.getValue());
            });
        } else {
            Class<?> supper = object.getClass();
            Field[] fields;
            do {
                fields = supper.getDeclaredFields();
                for (Field field : fields) {
                    if ((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
                        continue;
                    }
                    ReflectionUtils.makeAccessible(field);
                    Object value = ReflectionUtils.getField(field, object);
                    map.put(prefix + "." + field.getName(), value);
                }
                supper = supper.getSuperclass();
            } while (supper != Object.class);
        }
        return map;
    }

}
