package com.wujiuye.hotkit.rpc.myfeign.annotation;

import java.lang.annotation.*;

/**
 * 打印请求和响应日记
 *
 * @author wujiuye 2020/07/01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ShowLog {

    /**
     * 显示请求日记
     *
     * @return
     */
    boolean request() default true;

    /**
     * 显示响应日记
     *
     * @return
     */
    boolean response() default true;

}
