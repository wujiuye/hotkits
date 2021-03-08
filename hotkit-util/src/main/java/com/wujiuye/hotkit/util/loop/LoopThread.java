package com.wujiuye.hotkit.util.loop;

import com.wujiuye.hotkit.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 批量数据轮询
 *
 * @author wujiuye
 * @version 1.0 on 2020/05/27
 */
public class LoopThread<T> extends Thread {

    private BlockingQueue<T> queue;
    private volatile boolean singl = false;
    /**
     * 批量数据处理器
     */
    private LoopHandler<T> loopHandler;
    /**
     * 当队列中达到batchHandleSize时调用批量数据处理器处理
     */
    private int batchHandleSize;
    /**
     * 队列大小，为-1时使用无界队列
     */
    private int queueSize;
    /**
     * 超时时长（当等待时长超过该时间时，如果队列还未达到batchHandleSize，也触发一次批量处理）
     */
    private long timeout;
    private TimeUnit timeUnit;

    /**
     * 批量数据轮询处理线程
     *
     * @param loopHandler     批量数据处理器
     * @param batchHandleSize 批量数据大小，达到该数字立即调用批量数据处理器处理
     * @param queueSize       队列大小，-1为无界阻塞队列
     * @param timeout         超时时间，从队列中取数据超过这个时间则调用批量数据处理器处理一次数据
     * @param timeUnit        超时时间单位
     */
    public LoopThread(LoopHandler<T> loopHandler,
                      int batchHandleSize, int queueSize,
                      long timeout, TimeUnit timeUnit) {
        this.loopHandler = loopHandler;
        this.batchHandleSize = batchHandleSize;
        this.queueSize = queueSize;
        if (this.queueSize < 0) {
            this.queue = new LinkedBlockingQueue<>();
        } else {
            this.queue = new ArrayBlockingQueue<>(this.queueSize);
        }
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public void put(T data) {
        if (!this.queue.offer(data)) {
            // 发不进队列则使用当前线程直接处理
            List<T> dataArray = new ArrayList<>(1);
            dataArray.add(data);
            try {
                loopHandler.handle(dataArray);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void run() {
        while (!singl && !Thread.interrupted()) {
            List<T> dataArray = new ArrayList<>();
            long startTime = System.currentTimeMillis();
            while (dataArray.size() < this.batchHandleSize) {
                try {
                    T data = queue.poll(1, TimeUnit.SECONDS);
                    if (data == null) {
                        // 超时 this.timeout 如果队列未够 batchHandleSize 也直接处理
                        long ms = System.currentTimeMillis() - startTime;
                        if (timeUnit.convert(ms, TimeUnit.MILLISECONDS) > this.timeout) {
                            break;
                        }
                        continue;
                    }
                    dataArray.add(data);
                } catch (InterruptedException e) {
                    // 超时 this.timeout 如果队列未够 batchHandleSize 也直接处理
                    long ms = System.currentTimeMillis() - startTime;
                    if (timeUnit.convert(ms, TimeUnit.MILLISECONDS) > this.timeout) {
                        break;
                    }
                }
            }
            if (!CollectionUtils.isEmpty(dataArray)) {
                try {
                    loopHandler.handle(dataArray);
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void stopTask() {
        singl = true;
    }

}
