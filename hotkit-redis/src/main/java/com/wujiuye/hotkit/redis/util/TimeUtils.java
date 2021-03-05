package com.wujiuye.hotkit.redis.util;

import java.util.concurrent.TimeUnit;

/**
 * 时间工具
 *
 * @author wujiuye 2020/07/31
 */
public class TimeUtils {

    /**
     * 休眠指定秒
     *
     * @param seconds 休眠多少秒
     */
    public static void sleep(long seconds) {
        sleep(seconds, TimeUnit.SECONDS);
    }

    /**
     * 休眠
     *
     * @param time
     * @param unit
     */
    public static void sleep(long time, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(time));
        } catch (InterruptedException ignored) {
        }
    }

}
