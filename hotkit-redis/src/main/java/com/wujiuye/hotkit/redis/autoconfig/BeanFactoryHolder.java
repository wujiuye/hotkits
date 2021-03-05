package com.wujiuye.hotkit.redis.autoconfig;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * @author wujiuye 2020/10/28
 */
public final class BeanFactoryHolder {

    private volatile static ConfigurableBeanFactory beanFactory;

    static void setBeanFactory(ConfigurableBeanFactory beanFactory) {
        BeanFactoryHolder.beanFactory = beanFactory;
    }

    public static <T> T getBean(Class<T> tclass) {
        return beanFactory.getBean(tclass);
    }

}
