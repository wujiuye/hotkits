package com.wujiuye.hotkit.spring.common.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * 线程池
 * 不固定线程数量，没有核心线程数、没有队列；
 * 根据系统实时负载、CPU使用率、内存使用率决定是否走拒绝策略（默认拒绝策略为：使用当前提交任务的线程执行任务）
 *
 * @author wujiuye 2020/08/17
 */
public class SystemLoadAutoThreadPoolExecutor extends CustomizableThreadFactory
        implements AsyncTaskExecutor {

    private final static Logger logger = LoggerFactory.getLogger(AsyncTaskExecutor.class);

    /**
     * 系统负载阈值
     */
    private final double systemLoadThreshol;
    /**
     * cpu使用率阈值
     */
    private final double cpuUseRateThreshold;
    /**
     * 内存使用率阈值
     */
    private final double memoryUseRateThreshold;

    private ThreadPoolExecutor executor;


    private RejectedExecutionHandler rejectedExecutionHandler;

    public SystemLoadAutoThreadPoolExecutor() {
        this(0.75, 0.80, 0.90, null);
    }

    /**
     * 创建线程池
     *
     * @param systemLoadThreshol       系统负载阈值[0,1]
     * @param cpuUseRateThreshold      cpu使用率阈值[0,1)
     * @param memoryUseRateThreshold   内存使用率阈值[0,1)
     * @param rejectedExecutionHandler 拒绝策略
     */
    public SystemLoadAutoThreadPoolExecutor(double systemLoadThreshol, double cpuUseRateThreshold, double memoryUseRateThreshold,
                                            RejectedExecutionHandler rejectedExecutionHandler) {
        this.systemLoadThreshol = systemLoadThreshol;
        this.cpuUseRateThreshold = cpuUseRateThreshold;
        this.memoryUseRateThreshold = memoryUseRateThreshold;
        if (rejectedExecutionHandler == null) {
            rejectedExecutionHandler = (r, executor) -> r.run();
        }
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }

    protected ThreadPoolExecutor getThreadPoolExecutor() {
        if (executor == null) {
            synchronized (this) {
                if (executor == null) {
                    initExecutor();
                }
            }
        }
        return executor;
    }

    private void initExecutor() {
        executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                10L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                this,
                (r, executor) -> r.run());
    }

    private boolean allowSubmitTask() {
        if (SystemLoadAvgListener.getCurrentLoad() > this.systemLoadThreshol) {
            logger.debug("current load usage is:{}", SystemLoadAvgListener.getCurrentLoad());
            return false;
        }
        if (SystemLoadAvgListener.getCurrentCpuUsage() > this.cpuUseRateThreshold) {
            logger.debug("current cpu usage is:{}", SystemLoadAvgListener.getCurrentCpuUsage());
            return false;
        }
        if (SystemLoadAvgListener.getMemoryUsage() > this.memoryUseRateThreshold) {
            logger.debug("current memory usage is:{}", SystemLoadAvgListener.getMemoryUsage());
            return false;
        }
        return true;
    }

    @Override
    public void execute(Runnable runnable) {
        if (!allowSubmitTask()) {
            if (rejectedExecutionHandler != null) {
                logger.debug("拒绝执行，使用当前线程执行");
                rejectedExecutionHandler.rejectedExecution(runnable, getThreadPoolExecutor());
                return;
            }
        }
        getThreadPoolExecutor().execute(runnable);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        this.execute(task);
    }

    @Override
    public Future<?> submit(Runnable task) {
        RunnableFuture<Void> ftask = new FutureTask<>(task, null);
        this.execute(task);
        return ftask;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        RunnableFuture<T> ftask = new FutureTask<>(task);
        this.execute(ftask);
        return ftask;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = super.newThread(runnable);
        thread.setUncaughtExceptionHandler((t, e) -> logger.warn("线程抛出异常：{}==> ", t.getName(), e));
        return thread;
    }

}
