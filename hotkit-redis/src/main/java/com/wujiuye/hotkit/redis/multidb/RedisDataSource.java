package com.wujiuye.hotkit.redis.multidb;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 切换db
 * 站在不应该使用多db的角度，让多db支持使用动态切换方式，不污染模版类
 *
 * @author wujiuye 2020/10/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisDataSource {

    int value() default 0;

    /**
     * 切换的db，配置value即可
     *
     * @return
     */
    @AliasFor("value")
    int db() default 0;

}
