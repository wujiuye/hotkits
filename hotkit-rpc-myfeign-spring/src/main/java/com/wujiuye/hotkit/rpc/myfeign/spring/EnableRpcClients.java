package com.wujiuye.hotkit.rpc.myfeign.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启Rpc客户端
 *
 * @author wujiuye 2020/07/01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RpcClientBeanDefinitionRegistrar.class)
public @interface EnableRpcClients {

    /**
     * 扫描的包
     *
     * @return
     */
    String[] basePackages() default {};

}
