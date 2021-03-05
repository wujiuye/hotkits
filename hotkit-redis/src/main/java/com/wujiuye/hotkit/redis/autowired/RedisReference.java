package com.wujiuye.hotkit.redis.autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RedisCacheTemplate or ReactiveRedisCacheTemplate 自动注入
 *
 * @author wujiuye 2020/05/07
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisReference {

    /**
     * 哪个集群
     *
     * @return
     */
    String value();

}
