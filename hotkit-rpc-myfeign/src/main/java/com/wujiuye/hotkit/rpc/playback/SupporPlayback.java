package com.wujiuye.hotkit.rpc.playback;

import java.lang.annotation.*;

/**
 * 给方法添加此注解，让方法支持回放
 *
 * @author wujiuye 2020/07/21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface SupporPlayback {

    /**
     * 业务分类
     *
     * @return
     */
    String value() default "default";

    /**
     * 最大回放次数
     *
     * @return
     */
    int maxPlaybackCount() default 3;

    /**
     * 方法抛出异常时重放操作
     *
     * @return
     */
    Class<? extends Throwable>[] playbackOnThrowables() default {Throwable.class};

    /**
     * 返回值过滤，支持表达式
     * 获取返回值：$return
     * 获取返回值xx字段的值：$return.xx
     * 获取参数1：$param1
     * 获取参数1的xx字段的值：$param1.xx
     * 获取参数2：$param2
     * ....
     *
     * @return
     */
    String expression() default "";

}
