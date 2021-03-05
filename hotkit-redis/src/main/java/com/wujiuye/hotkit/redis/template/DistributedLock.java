package com.wujiuye.hotkit.redis.template;

import com.wujiuye.hotkit.redis.exception.AcquireLockException;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author wujiuye 2020/09/23
 */
public interface DistributedLock {

    /**
     * 获取当前持有锁的线程
     *
     * @return
     */
    Thread current();

    /**
     * 阻塞等待获取到锁（实现的是自旋锁）
     */
    void lock();

    /**
     * 释放锁
     */
    void unlock();

    /**
     * 尝试获取锁
     *
     * @return
     */
    boolean tryLock();

    /**
     * 尝试获取锁
     *
     * @param timeout  等待超时
     * @param timeUnit 超时时间单位
     * @return
     */
    boolean tryLock(int timeout, TimeUnit timeUnit);

    /**
     * 将任务包装到分布式锁中执行
     *
     * @param function 注意：非异步执行，只是借用Runnable的API
     */
    default void doOnLock(Runnable function) throws AcquireLockException {
        if (tryLock()) {
            try {
                function.run();
            } finally {
                unlock();
            }
        } else {
            throw new AcquireLockException();
        }
    }

}
