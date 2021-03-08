package com.wujiuye.hotkit.util.time;

import com.wujiuye.hotkit.util.SignalManager;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 时间工具
 *
 * @author wujiuye 2020/07/31
 */
public class TimeUtils {

    private final static AtomicBoolean SIGN = new AtomicBoolean(Boolean.FALSE);

    private static volatile long CURRENT = System.currentTimeMillis();

    static {
        Thread thread = new Thread(() -> {
            CURRENT = System.currentTimeMillis();
            while (!SIGN.get()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException ignored) {
                }
                CURRENT = CURRENT + 1L;
            }
        });
        thread.setName("time");
        thread.setDaemon(true);
        thread.start();
        SignalManager.registToFirst(signal -> SIGN.compareAndSet(Boolean.FALSE, Boolean.TRUE));
    }

    public static long currentTimeMillis() {
        return CURRENT;
    }

    /**
     * 停止等待条件
     *
     * @author wuijuye 2020/07/31
     */
    @FunctionalInterface
    public interface Condition {
        /**
         * 是否满足停止修改条件
         *
         * @return
         */
        boolean condition();
    }

    /**
     * 有限时等待
     *
     * @param maxSeconds 最大等待的秒数
     * @param condition  满足条件则停止
     */
    public static void sleepWait(long maxSeconds, Condition condition) {
        int cnt = 0;
        while (!condition.condition()) {
            sleep(1);
            cnt++;
            if (cnt >= maxSeconds) {
                break;
            }
        }
    }

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
