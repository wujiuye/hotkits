package com.wujiuye.hotkit.redis.multidb;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * redis动态数据源切换切面实现，站在不应该使用多db的角度，让多db支持使用动态切换方式，不污染模版类
 * （⚠️当然，作者并不推荐使用多db特性，只是考虑到很多项目都在使用这个特性）
 *
 * @author wujiuye 2020/10/19
 */
@Aspect
public class DynamicRedisDataSourceAop {

    private final static Set<String> REACTOR_API_CLASS = new HashSet<>();

    static {
        REACTOR_API_CLASS.add("org.reactivestreams.Publisher");
        REACTOR_API_CLASS.add("reactor.core.publisher.Mono");
        REACTOR_API_CLASS.add("reactor.core.publisher.Flux");
    }

    /**
     * 定义redis动态db切换切点
     */
    @Pointcut(value = "@annotation(com.msyc.common.redis.multidb.RedisDataSource)")
    public void point() {
    }

    /**
     * 实现动态切换DB
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "point()")
    public Object aroudAop(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String returnTypeClassName = method.getReturnType().getName();
        RedisDataSource redisDataSource = AnnotationUtils.findAnnotation(method, RedisDataSource.class);
        int db = (int) AnnotationUtils.getValue(redisDataSource, "db");
        if (REACTOR_API_CLASS.contains(returnTypeClassName)) {
            return ReactiveRedisDataSourceHodler.doOnDatabase(pjp.proceed(), db);
        }
        RedisDataSourceHodler.switchDataSouce(db);
        try {
            return pjp.proceed();
        } finally {
            RedisDataSourceHodler.clearDataSource();
        }
    }

}
