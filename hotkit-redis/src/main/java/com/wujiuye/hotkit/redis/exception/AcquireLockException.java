package com.wujiuye.hotkit.redis.exception;

/**
 * 请求获取锁失败时抛出的异常
 *
 * @author wujiuye 2020/10/30
 */
public class AcquireLockException extends RuntimeException {

    public AcquireLockException() {
        super("thread " + Thread.currentThread().getName() + " acquire lock fail!");
    }

}
