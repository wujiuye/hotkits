package com.wujiuye.hotkit.util.loop;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 批量数据轮询分组
 *
 * @author wujiuye
 * @version 1.0 on 2020/05/27
 */
public class LoopThreadGroup<T> {

    private final LoopThread<T>[] loopThreads;
    private volatile AtomicInteger index = new AtomicInteger(0);

    /**
     * 批量数据轮询主题构造方法
     *
     * @param loopHandler     批量数据处理器
     * @param batchHandleSize 批量数据大小，达到该数字立即调用批量数据处理器处理
     * @param queueSize       队列大小，-1为无界阻塞队列
     * @param timeout         超时时间，从队列中取数据超过这个时间则调用批量数据处理器处理一次数据
     * @param timeUnit        超时时间单位
     * @param threads         线程数
     */
    public LoopThreadGroup(LoopHandler<T> loopHandler,
                           int batchHandleSize, int queueSize,
                           long timeout, TimeUnit timeUnit, int threads) {
        loopThreads = new LoopThread[Math.max(threads, 2)];
        for (int index = 0; index < loopThreads.length; index++) {
            loopThreads[index] = new LoopThread<>(loopHandler, batchHandleSize, queueSize, timeout, timeUnit);
            loopThreads[index].start();
        }
    }

    public LoopThread<T> nextLoopThread() {
        return loopThreads[Math.abs(index.getAndIncrement() % loopThreads.length)];
    }

    public void destroy() {
        for (LoopThread<T> loopThread : loopThreads) {
            loopThread.stopTask();
        }
    }

}
