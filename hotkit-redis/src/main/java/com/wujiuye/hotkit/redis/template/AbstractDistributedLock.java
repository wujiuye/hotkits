package com.wujiuye.hotkit.redis.template;

import com.wujiuye.hotkit.redis.util.HostUtils;

/**
 * 抽象分布式锁
 *
 * @author wujiuye 2020/10/28
 */
public abstract class AbstractDistributedLock implements DistributedLock {

    /**
     * 锁的key
     */
    protected String lockKey;
    /**
     * 业务执行时间不能超过这个时间，单位秒
     */
    protected int lockExpireTime;
    /**
     * 当前持有锁的线程
     */
    protected Thread curThread;

    public AbstractDistributedLock(String lockKey) {
        this(lockKey, 60);
    }

    public AbstractDistributedLock(String lockKey, int lockExpireTime) {
        this.lockExpireTime = lockExpireTime;
        this.lockKey = lockKey;
    }

    @Override
    public Thread current() {
        return curThread;
    }

    protected static String getValue() {
        return HostUtils.getHostNamePid() + ":" + Thread.currentThread().getId();
    }

}
