package com.wujiuye.hotkit.spring.common.executor;

import java.lang.annotation.*;

/**
 * 业务线程池配置
 *
 * @author wujiuye 2020/08/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface BusinessThread {

    /**
     * 业务标识
     * 如果为空，默认取类名
     *
     * @return
     */
    String businessId() default "";

    /**
     * 信号量
     * 为业务从线程池中申请的线程数
     *
     * @return
     */
    int threads() default 1;

}
