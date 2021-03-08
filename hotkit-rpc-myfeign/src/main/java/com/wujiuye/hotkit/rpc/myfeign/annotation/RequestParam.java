package com.wujiuye.hotkit.rpc.myfeign.annotation;

import java.lang.annotation.*;

/**
 * @author wujiuye 2020/07/01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface RequestParam {

    /**
     * 参数名
     *
     * @return
     */
    String value();

}
