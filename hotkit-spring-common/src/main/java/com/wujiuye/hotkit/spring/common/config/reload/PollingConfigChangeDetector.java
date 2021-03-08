package com.wujiuye.hotkit.spring.common.config.reload;

import com.wujiuye.hotkit.util.concurrent.NameThreadFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 轮询配置改变探测器
 *
 * @author wujiuye 2020/09/15
 */
public abstract class PollingConfigChangeDetector extends AbstractConfigChangeDetector {

    private String key;
    private long time;
    private TimeUnit timeUnit;
    private final static ScheduledExecutorService executorService;

    static {
        executorService = new ScheduledThreadPoolExecutor(1,
                new NameThreadFactory("config-polling-", true));
        Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdownNow));
    }

    public PollingConfigChangeDetector(String key, long time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
        this.key = key;
    }

    @PostConstruct
    public void onInit() {
        reload(key);
        executorService.scheduleAtFixedRate(() -> reload(key), time, time, timeUnit);
    }

}
