package com.wujiuye.hotkit.util.time;

/**
 * 耗时统计器
 *
 * @author wujiuye 2020/07/02
 */
public class TimeCalculation {

    private long startTime;
    private long endTime;

    private TimeCalculation() {
        this.startTime = System.currentTimeMillis();
    }

    public static TimeCalculation start() {
        return new TimeCalculation();
    }

    /**
     * 计算耗时，单位毫秒
     *
     * @return
     */
    public long consumeTimeMs() {
        this.endTime = System.currentTimeMillis();
        return this.endTime - startTime;
    }

    /**
     * 计算耗时，返回可视化的耗时
     *
     * @return
     */
    public String consumeTimeFormat() {
        long ms = this.consumeTimeMs();
        return formatTime(ms);
    }

    /**
     * 将耗时时间戳格式化输出
     *
     * @param timesmp 耗时时间戳，单位毫秒
     * @return
     */
    private static String formatTime(long timesmp) {
        long second = timesmp / 1000;
        long minute = second / 60;
        long hour = minute / 60;
        StringBuilder builder = new StringBuilder();
        if (hour > 0) {
            builder.append(hour).append(" hour ");
        }
        if (minute > 0) {
            builder.append(minute % 60).append(" minute ");
        }
        if (second > 0) {
            builder.append(second % 60).append(" second ");
        }
        builder.append(timesmp % 1000).append(" millisecond");
        return builder.toString();
    }

}
