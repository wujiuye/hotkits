package com.wujiuye.hotkit.spring.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author wuijuye 2020/06/10
 */
@Configuration
public class SpringContextHolder implements ImportBeanDefinitionRegistrar, ApplicationContextAware, EnvironmentAware {

    private static ApplicationContext applicationContext;
    private static Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SpringContextHolder.class);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition(SpringContextHolder.class.getName(), builder.getBeanDefinition());
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) registry;
        // 立即创建SpringContextUtils，调用Aware方法
        beanFactory.getBean(SpringContextHolder.class);
    }

    @Override
    public void setEnvironment(Environment environment) {
        SpringContextHolder.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static void registerBean(String beanName, Object bean) {
        ((ConfigurableListableBeanFactory) Objects.requireNonNull(applicationContext.getAutowireCapableBeanFactory()))
                .registerSingleton(beanName, bean);
    }

    public static String getValue(String key) {
        String value = environment.getProperty(key);
        if (StringUtils.isEmpty(value)) {
            throw new NullPointerException("unfind key：" + key);
        }
        return value;
    }

    public static boolean containsBean(String beanName) {
        return applicationContext.containsBean(beanName);
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

}