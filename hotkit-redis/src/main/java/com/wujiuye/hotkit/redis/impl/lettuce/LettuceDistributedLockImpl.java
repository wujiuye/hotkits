package com.wujiuye.hotkit.redis.impl.lettuce;

import com.wujiuye.hotkit.redis.template.AbstractDistributedLock;
import com.wujiuye.hotkit.redis.util.TimeUtils;
import io.lettuce.core.SetArgs;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author wujiuye 2020/09/23
 */
public class LettuceDistributedLockImpl extends AbstractDistributedLock {

    public LettuceDistributedLockImpl(String lockKey) {
        super(lockKey);
    }

    public LettuceDistributedLockImpl(String lockKey, int lockExpireTime) {
        super(lockKey, lockExpireTime);
    }

    private RedisPubSubCommands<String, String> getCommand() {
        return LettuceClusterDelegate.chooseDefaultClusterConnection().sync();
    }

    @Override
    public void lock() {
        // 可重入
        if (curThread == Thread.currentThread()) {
            return;
        }
        String value = getValue();
        // 自旋锁
        for (; ; ) {
            if ("OK".equals(getCommand().set(lockKey, value, SetArgs.Builder.nx().ex(lockExpireTime)))) {
                curThread = Thread.currentThread();
                return;
            }
            TimeUtils.sleep(1, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void unlock() {
        if (curThread == null || curThread != Thread.currentThread()) {
            return;
        }
        if (getValue().equalsIgnoreCase(getCommand().get(lockKey))) {
            curThread = null;
            getCommand().del(lockKey);
        }
    }

    @Override
    public boolean tryLock() {
        return tryLock(0, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean tryLock(int timeout, TimeUnit timeUnit) {
        // 可重入
        if (curThread == Thread.currentThread()) {
            return true;
        }
        String value = getValue();
        int cntWaitMs = 0;
        final long maxWaitMs = timeUnit.toMillis(timeout);
        // 自旋锁
        for (; ; ) {
            if ("OK".equals(getCommand().set(lockKey, value, SetArgs.Builder.nx().ex(lockExpireTime)))) {
                curThread = Thread.currentThread();
                return true;
            }
            if (maxWaitMs == 0) {
                return false;
            }
            if (cntWaitMs >= maxWaitMs) {
                return false;
            }
            TimeUtils.sleep(1, TimeUnit.MILLISECONDS);
            cntWaitMs++;
        }
    }


}
