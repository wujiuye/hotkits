package com.wujiuye.hotkit.util.concurrent;

import com.wujiuye.hotkit.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

/**
 * 需要异步执行的任务可使用AsyncSupporJob完成
 *
 * @author wujiuye 2020/05/12
 */
public class AsyncActionSupper {

    /**
     * 数据转Runable 异步执行业务逻辑
     *
     * @param <T>
     */
    @FunctionalInterface
    public interface DataToRunableAction<T> {
        /**
         * 异常执行业务逻辑
         *
         * @param item 一个item对应一个任务
         */
        void doAction(T item);
    }

    /**
     * 数据关联的任务
     */
    public abstract class JobRunnable implements Runnable {
        private Object data;

        public JobRunnable(Object data) {
            this.data = data;
        }

        public Object getData() {
            return data;
        }

    }

    /**
     * 任务异常处理器
     */
    public abstract static class ExceptionHandler<T> {
        /**
         * 统一处理异常 (非线程安全)
         *
         * @param e 异常
         */
        public abstract void onException(T item, Exception e);
    }

    private ExecutorService executorService;
    private int threads;
    private Semaphore semaphore;
    private CountDownLatch latch;
    private List<JobRunnable> runnables;
    private ExceptionHandler exceptionHandler;

    public AsyncActionSupper(ExecutorService executorService) {
        this.executorService = executorService;
        threads = 1;
        this.runnables = new ArrayList<>();
    }

    public AsyncActionSupper settingExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * 设置使用多少个线程数来执行这些任务
     *
     * @param threads 线程数
     * @return
     */
    public AsyncActionSupper settingThreads(int threads) {
        this.threads = threads;
        if (threads > 1) {
            semaphore = new Semaphore(threads);
        }
        return this;
    }

    /**
     * 提交一个任务
     *
     * @param runnable 任务
     * @return
     */
    public AsyncActionSupper submitOne(JobRunnable runnable) {
        if (runnable == null) {
            return this;
        }
        this.runnables.add(runnable);
        return this;
    }

    /**
     * 将列表数据转为Runnable并提交
     *
     * @param data   数据，没项对应一个任务
     * @param action Runnable要做的事情
     * @param <T>
     * @return
     */
    public <T> AsyncActionSupper toRunable(Collection<T> data, DataToRunableAction<T> action) {
        if (CollectionUtils.isEmpty(data)) {
            this.runnables = new ArrayList<>(0);
            return this;
        }
        this.runnables = data.parallelStream()
                .filter(Objects::nonNull)
                .map(item -> new JobRunnable(item) {
                    @Override
                    public void run() {
                        action.doAction(item);
                    }
                })
                .collect(Collectors.toList());
        return this;
    }

    public <K, T> AsyncActionSupper toRunable(Map<K, T> data, DataToRunableAction<T> action) {
        if (CollectionUtils.isEmpty(data)) {
            this.runnables = new ArrayList<>(0);
            return this;
        }
        this.runnables = data.entrySet().stream()
                .filter(Objects::nonNull)
                .map(Map.Entry::getValue)
                .map(values -> new JobRunnable(values) {
                    @Override
                    public void run() {
                        action.doAction(values);
                    }
                }).collect(Collectors.toList());
        return this;
    }

    /**
     * 执行所有任务
     *
     * @throws InterruptedException
     */
    public void executes() throws InterruptedException {
        executesAsync();
        await();
    }

    public void executesAsync() throws InterruptedException {
        if (runnables.isEmpty()) {
            return;
        }
        // 线程数少于2则在当前线程执行任务
        if (threads < 2) {
            if (exceptionHandler == null) {
                runnables.forEach(Runnable::run);
            } else {
                runnables.forEach(runnable -> {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        exceptionHandler.onException(runnable.getData(), e);
                    }
                });
            }
            return;
        }
        // 控制使用目标线程数执行任务
        latch = new CountDownLatch(runnables.size());
        runnables.forEach(item -> {
            try {
                semaphore.acquire();
                executorService.submit(() -> {
                    try {
                        item.run();
                    } catch (Exception e) {
                        if (exceptionHandler != null) {
                            exceptionHandler.onException(item.getData(), e);
                        }
                        throw e;
                    } finally {
                        semaphore.release();
                        latch.countDown();
                    }
                });
            } catch (InterruptedException ignored) {
            }
        });
    }

    public void await() throws InterruptedException {
        if (latch != null) {
            latch.await();
        }
    }

}
