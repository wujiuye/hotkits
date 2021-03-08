package com.wujiuye.hotkit.spring.common.executor;

import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.*;

/**
 * 信号量隔离的虚拟线程池（ExecutorService）
 * 使用方式：
 *
 * @author wujiuye 2020/08/17
 * @see SemaphoreExecutorContext
 */
public class SemaphoreExecutorService implements AsyncTaskExecutor, Thread.UncaughtExceptionHandler {

    /**
     * 真正执行任务的线程池
     */
    private AsyncTaskExecutor executor;
    /**
     * 限号量隔离业务
     */
    private Semaphore semaphore;

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    /**
     * 创建线程号隔离的ExecutorService
     *
     * @param executor  真正执行任务的线程池
     * @param semaphore 信号量
     */
    SemaphoreExecutorService(AsyncTaskExecutor executor, Semaphore semaphore) {
        this.executor = executor;
        this.semaphore = semaphore;
    }

    /**
     * 创建线程号隔离的ExecutorService
     *
     * @param executor 真正执行任务的线程池
     * @param threads  最多只能同时占用多少个线程
     */
    SemaphoreExecutorService(AsyncTaskExecutor executor, int threads) {
        this.executor = executor;
        this.semaphore = new Semaphore(threads);
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

    /**
     * 使用信号量控制任务的并行执行数量
     *
     * @param task 提交的任务，submit也会走这个方法
     */
    @Override
    public void execute(Runnable task) {
        this.executor.execute(() -> {
            Thread.currentThread().setUncaughtExceptionHandler(this);
            try {
                semaphore.acquire();
            } catch (InterruptedException ignored) {
            }
            try {
                task.run();
            } finally {
                semaphore.release();
                Thread.currentThread().setUncaughtExceptionHandler(null);
            }
        });
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(t, e);
        }
    }

}
