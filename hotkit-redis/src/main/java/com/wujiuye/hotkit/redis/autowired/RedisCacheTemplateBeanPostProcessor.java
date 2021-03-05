package com.wujiuye.hotkit.redis.autowired;

import com.wujiuye.hotkit.redis.RedisUtils;
import com.wujiuye.hotkit.redis.template.ReactiveRedisCacheTemplate;
import com.wujiuye.hotkit.redis.template.RedisCacheTemplate;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.InjectionMetadata;

import java.lang.reflect.Proxy;

/**
 * 实现自动注入
 *
 * @author wujiuye 2020/05/07
 */
public class RedisCacheTemplateBeanPostProcessor extends AnnotationInjectedBeanPostProcessor<RedisReference> {

    @Override
    protected Class<RedisReference> getAnnotationType() {
        return RedisReference.class;
    }

    @Override
    protected Object getInjectedObject(RedisReference annotation, Object bean,
                                       String beanName, Class<?> injectedType,
                                       InjectionMetadata.InjectedElement injectedElement) throws Exception {
        if (injectedType == RedisCacheTemplate.class) {
            // 注入代理而不注意原始对象，为后期实现动态配置做支持
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{RedisCacheTemplate.class},
                    (proxy, method, args) -> method.invoke(RedisUtils.getTemplate(annotation.value()), args));
        }
        if (injectedType == ReactiveRedisCacheTemplate.class) {
            // 注入代理而不注意原始对象，为后期实现动态配置做支持
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{ReactiveRedisCacheTemplate.class},
                    (proxy, method, args) -> method.invoke(RedisUtils.getReactiveTemplate(annotation.value()), args));
        }
        throw new BeanCreationException("Unsupported type！");
    }

}
