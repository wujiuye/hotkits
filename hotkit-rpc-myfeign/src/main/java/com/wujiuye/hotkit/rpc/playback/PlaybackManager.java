package com.wujiuye.hotkit.rpc.playback;

import com.wujiuye.hotkit.util.CollectionUtils;
import com.wujiuye.hotkit.util.concurrent.NameThreadFactory;
import com.wujiuye.hotkit.util.time.TimeCalculation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 负责action消费配置初始化
 *
 * @author wujiuye 2020/07/20
 */
public class PlaybackManager {

    private final static Logger logger = LoggerFactory.getLogger(PlaybackManager.class);

    /**
     * 执行回放操作的线程池
     */
    private static ExecutorService executorService;
    /**
     * 对象工厂
     */
    private static ObjectFactory objectFactory;
    /**
     * 回放监听器
     */
    private static List<ActionConsumListener> consumListeners;

    private static ScheduledExecutorService scheduledExecutorService;

    private PlaybackManager() {

    }

    public static class Builder {
        private int threads;
        private int queueSize = Integer.MAX_VALUE;
        private ExecutorService executorService;
        private ObjectFactory objectFactory;
        private List<ActionConsumListener> consumListeners = new ArrayList<>();

        /**
         * 设置执行回放操作的线程池的线程数
         *
         * @param threads
         * @return
         */
        public Builder threads(int threads) {
            this.threads = threads;
            return this;
        }

        /**
         * 线程池队列大小
         *
         * @param queueSize
         * @return
         */
        public Builder queueSize(int queueSize) {
            this.queueSize = queueSize;
            return this;
        }

        /**
         * 设置对象工厂
         *
         * @param objectFactory
         * @return
         */
        public Builder objectFactory(ObjectFactory objectFactory) {
            this.objectFactory = objectFactory;
            return this;
        }

        /**
         * 注册action回放监听器
         *
         * @param listeners
         * @return
         */
        public Builder registConsumListener(List<ActionConsumListener> listeners) {
            if (!CollectionUtils.isEmpty(listeners)) {
                this.consumListeners.addAll(listeners);
            }
            return this;
        }

        /**
         * 自定义线程池
         *
         * @param executorService 用于执行回放操作的线程池
         * @return
         */
        public Builder executorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public static ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    /**
     * 初始化
     *
     * @param builder
     */
    public static void init(Builder builder) {
        if (PlaybackManager.executorService == null) {
            synchronized (PlaybackManager.class) {
                if (PlaybackManager.executorService == null) {
                    if (builder.executorService != null) {
                        PlaybackManager.executorService = builder.executorService;
                    } else {
                        int threads = builder.threads > 0 ? builder.threads : 1;
                        PlaybackManager.executorService = new ThreadPoolExecutor(threads, threads,
                                60, TimeUnit.SECONDS,
                                new LinkedBlockingQueue<>(builder.queueSize),
                                new NameThreadFactory("playback-", true));
                    }
                    PlaybackManager.objectFactory = builder.objectFactory;
                    PlaybackManager.consumListeners = builder.consumListeners;
                    start();
                }
            }
        }
    }

    /**
     * 开启定时任务，定时回放失败操作
     */
    private static void start() {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new NameThreadFactory("scheduled-playback-", true));
        submitNextTask(scheduledExecutorService, 5);
    }

    /**
     * 自适应调整回放时间间隔
     *
     * @param scheduledExecutorService 调度任务线程池
     * @param period                   延时执行，单位分钟
     */
    private static void submitNextTask(ScheduledExecutorService scheduledExecutorService, long period) {
        scheduledExecutorService.schedule(() -> {
                    TimeCalculation calculation = TimeCalculation.start();
                    new FileActionConsumer(executorService, consumListeners).consumLog();
                    long ms = calculation.consumeTimeMs();
                    if ((ms / 1000 / 60) > period) {
                        long newPeriod = ms / 1000 / 60;
                        submitNextTask(scheduledExecutorService, newPeriod);
                    } else if (period >= 15) {
                        submitNextTask(scheduledExecutorService, 5);
                    } else {
                        submitNextTask(scheduledExecutorService, period);
                    }
                },
                period, TimeUnit.MINUTES);
    }

    /**
     * 停止或重启回放功能
     */
    public static void restart(boolean open) {
        if (scheduledExecutorService != null) {
            List<Runnable> runnables = scheduledExecutorService.shutdownNow();
            logger.info("被取消的定时回放任务数量为：{}", runnables.size());
            scheduledExecutorService = null;
        }
        if (open) {
            start();
        }
    }

}
