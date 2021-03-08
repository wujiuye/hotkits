package com.wujiuye.hotkit.util.spi;

import java.lang.annotation.*;

/**
 * @author wujiuye 2020/10/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SpiOrder {

    /**
     * 排序值
     *
     * @return
     */
    int value() default Integer.MIN_VALUE;

}
