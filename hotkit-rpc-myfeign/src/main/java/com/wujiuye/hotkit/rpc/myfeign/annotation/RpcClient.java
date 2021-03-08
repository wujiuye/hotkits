package com.wujiuye.hotkit.rpc.myfeign.annotation;

import java.lang.annotation.*;

/**
 * @author wujiuye 2020/07/01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RpcClient {

    /**
     * 接口的host+port部分
     *
     * @return
     */
    String url();

    /**
     * 是否开启重试功能
     *
     * @return
     */
    boolean enableRetry() default true;

    /**
     * 重试最大次数
     *
     * @return
     */
    int retryMax() default 1;

}
