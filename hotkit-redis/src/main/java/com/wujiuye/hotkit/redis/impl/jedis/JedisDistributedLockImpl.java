package com.wujiuye.hotkit.redis.impl.jedis;

import com.wujiuye.hotkit.redis.template.AbstractDistributedLock;
import com.wujiuye.hotkit.redis.util.TimeUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.TimeUnit;

/**
 * 使用Jedis实现的Redis分布式锁
 *
 * @author wujiuye 2020/09/23
 */
public class JedisDistributedLockImpl extends AbstractDistributedLock {

    private JedisPool jedisPool;

    public JedisDistributedLockImpl(String lockKey) {
        this(lockKey, 60);
    }

    public JedisDistributedLockImpl(String lockKey, int lockExpireTime) {
        super(lockKey, lockExpireTime);
        this.jedisPool = JedisClusterDelegate.chooseDefaultJedisPool()
                .orElseThrow(() -> new NullPointerException("未找到任意Redis集群"));
    }

    private Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        if (jedis == null) {
            throw new NullPointerException("get jedis fail...");
        }
        jedis.select(0);
        return jedis;
    }

    @Override
    public void lock() {
        // 可重入
        if (curThread == Thread.currentThread()) {
            return;
        }
        try (Jedis jedis = getJedis()) {
            String value = getValue();
            // 自旋锁
            for (; ; ) {
                if ("OK".equals(jedis.set(lockKey, value, new SetParams().nx().ex(lockExpireTime)))) {
                    curThread = Thread.currentThread();
                    return;
                }
                TimeUtils.sleep(1, TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public void unlock() {
        if (curThread == null || curThread != Thread.currentThread()) {
            return;
        }
        try (Jedis jedis = getJedis()) {
            if (getValue().equalsIgnoreCase(jedis.get(lockKey))) {
                curThread = null;
                jedis.del(lockKey);
            }
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
        try (Jedis jedis = getJedis()) {
            String value = getValue();
            int cntWaitMs = 0;
            final long maxWaitMs = timeUnit.toMillis(timeout);
            // 自旋锁
            for (; ; ) {
                if ("OK".equals(jedis.set(lockKey, value, new SetParams().nx().ex(lockExpireTime)))) {
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

}
