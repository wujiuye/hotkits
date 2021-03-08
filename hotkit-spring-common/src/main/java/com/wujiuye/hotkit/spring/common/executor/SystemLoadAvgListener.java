package com.wujiuye.hotkit.spring.common.executor;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 系统负载监听器
 *
 * @author wujiuye 2020/08/18
 */
public final class SystemLoadAvgListener {

    private final static ScheduledExecutorService scheduledExecutorService;

    private static volatile double currentLoad;
    private static volatile double currentCpuUsage;
    private static volatile double memoryUsage;

    static {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new CustomizableThreadFactory("system-load"));
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
                int processors = osBean.getAvailableProcessors();
                // 负载除以cpu核心数等于 平均每cpu的负载
                // 1核cpu的最大负载：1.0
                // n核cpu的最大负载：n * 1.0
                currentLoad = osBean.getSystemLoadAverage() / processors;
                // cpu使用率
                currentCpuUsage = osBean.getSystemCpuLoad();
                long totalMemory = osBean.getTotalPhysicalMemorySize();
                long freeMemory = osBean.getFreePhysicalMemorySize();
                memoryUsage = 1.0 * (totalMemory - freeMemory) / totalMemory;
                // log.debug("currentLoad={},currentCpuUsage={},memoryUsage={}",
                //      currentLoad, currentCpuUsage, memoryUsage);
            } catch (Throwable throwable) {
                //
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static double getCurrentLoad() {
        return currentLoad;
    }

    public static double getCurrentCpuUsage() {
        return currentCpuUsage;
    }

    public static double getMemoryUsage() {
        return memoryUsage;
    }

}
