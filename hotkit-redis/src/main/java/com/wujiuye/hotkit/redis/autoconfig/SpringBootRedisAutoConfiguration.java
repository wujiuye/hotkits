package com.wujiuye.hotkit.redis.autoconfig;

import com.wujiuye.hotkit.redis.autowired.RedisCacheTemplateBeanPostProcessor;
import com.wujiuye.hotkit.redis.multidb.DynamicRedisDataSourceAop;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wujiuye 2020/10/28
 */
@Import({ClusterConfig.class})
@Configuration
public class SpringBootRedisAutoConfiguration implements BeanFactoryAware {

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        BeanFactoryHolder.setBeanFactory((ConfigurableBeanFactory) beanFactory);
    }

    /**
     * Spring自动注入支持
     *
     * @return
     */
    @Bean
    public RedisCacheTemplateBeanPostProcessor redisCacheTemplateBeanPostProcessor() {
        return new RedisCacheTemplateBeanPostProcessor();
    }

    /**
     * 多DB声明式切换支持
     *
     * @return
     */
    @Bean
    public DynamicRedisDataSourceAop dynamicRedisDataSourceAop() {
        return new DynamicRedisDataSourceAop();
    }

}
