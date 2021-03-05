package com.wujiuye.hotkit.redis;

import com.wujiuye.hotkit.redis.impl.jedis.JedisDistributedLockImpl;
import com.wujiuye.hotkit.redis.impl.jedis.JedisReactiveRedisCacheTemplateImpl;
import com.wujiuye.hotkit.redis.impl.jedis.JedisRedisCacheTemplateImpl;
import com.wujiuye.hotkit.redis.impl.lettuce.LettuceDistributedLockImpl;
import com.wujiuye.hotkit.redis.impl.lettuce.LettuceReactiveRedisCacheTemplateImpl;
import com.wujiuye.hotkit.redis.impl.lettuce.LettuceRedisCacheTemplateImpl;
import com.wujiuye.hotkit.redis.template.DistributedLock;
import com.wujiuye.hotkit.redis.template.ReactiveRedisCacheTemplate;
import com.wujiuye.hotkit.redis.template.RedisCacheTemplate;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 适配Jedis的工具类
 *
 * @author wujiuye 2020/09/23
 */
public class RedisUtils {

    private final static Constructor<? extends RedisCacheTemplate> REDIS_CACHE_TEMPLATE_CONSTRUCTOR;
    private final static Constructor<? extends DistributedLock> KEY_DISTRIBUTED_LOCK_CONSTRUCTOR;
    private final static Constructor<? extends DistributedLock> KEY_AND_TIMEOUT_DISTRIBUTED_LOCK_CONSTRUCTOR;
    private final static Constructor<? extends ReactiveRedisCacheTemplate> REACTIVE_REDIS_CACHE_TEMPLATE_CONSTRUCTOR;

    static {
        Constructor<? extends RedisCacheTemplate> redistCacheTemplateConstructor = null;
        Constructor<? extends DistributedLock> keyDistributedLockConstructor = null;
        Constructor<? extends DistributedLock> keyTimeoutDistributedLockConstructor = null;
        Constructor<? extends ReactiveRedisCacheTemplate> reactiveRedisCacheTemplateConstructor = null;
        try {
            Class.forName("redis.clients.jedis.Jedis");
            redistCacheTemplateConstructor = JedisRedisCacheTemplateImpl.class.getConstructor(String.class);
            keyDistributedLockConstructor = JedisDistributedLockImpl.class.getConstructor(String.class);
            keyTimeoutDistributedLockConstructor = JedisDistributedLockImpl.class.getConstructor(String.class, int.class);
            try {
                Class.forName("reactor.core.publisher.Mono");
                reactiveRedisCacheTemplateConstructor = JedisReactiveRedisCacheTemplateImpl.class.getConstructor(String.class);
            } catch (ClassNotFoundException e) {
                // 不需要响应式API
            }
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            try {
                Class.forName("io.lettuce.core.RedisURI");
                redistCacheTemplateConstructor = LettuceRedisCacheTemplateImpl.class.getConstructor(String.class);
                keyDistributedLockConstructor = LettuceDistributedLockImpl.class.getConstructor(String.class);
                keyTimeoutDistributedLockConstructor = LettuceDistributedLockImpl.class.getConstructor(String.class, int.class);
                reactiveRedisCacheTemplateConstructor = LettuceReactiveRedisCacheTemplateImpl.class.getConstructor(String.class);
            } catch (ClassNotFoundException | NoSuchMethodException e2) {
                // 使用其它框架
            }
        }
        REDIS_CACHE_TEMPLATE_CONSTRUCTOR = redistCacheTemplateConstructor;
        KEY_DISTRIBUTED_LOCK_CONSTRUCTOR = keyDistributedLockConstructor;
        KEY_AND_TIMEOUT_DISTRIBUTED_LOCK_CONSTRUCTOR = keyTimeoutDistributedLockConstructor;
        REACTIVE_REDIS_CACHE_TEMPLATE_CONSTRUCTOR = reactiveRedisCacheTemplateConstructor;
    }

    private static Map<String, RedisCacheTemplate> TEMPLATE_MAP = new HashMap<>();
    private static Map<String, ReactiveRedisCacheTemplate> REACTIVE_TEMPLATE_MAP = new HashMap<>();

    /**
     * 获取集群对应的缓存操作模版
     *
     * @param cluster 集群名称
     * @return
     */
    public static RedisCacheTemplate getTemplate(String cluster) {
        RedisCacheTemplate template = TEMPLATE_MAP.get(cluster);
        if (template != null) {
            return template;
        }
        synchronized (RedisUtils.class) {
            try {
                template = REDIS_CACHE_TEMPLATE_CONSTRUCTOR.newInstance(cluster);
            } catch (Exception e) {
                throw new RuntimeException("不能初始化RedisCacheTemplate，集群名称：" + cluster);
            }
            Map<String, RedisCacheTemplate> newMap = new HashMap<>(TEMPLATE_MAP);
            newMap.put(cluster, template);
            TEMPLATE_MAP = newMap;
        }
        return template;
    }

    /**
     * 获取集群对应的缓存操作响应式模版
     *
     * @param cluster 集群名称
     * @return
     */
    public static ReactiveRedisCacheTemplate getReactiveTemplate(String cluster) {
        ReactiveRedisCacheTemplate template = REACTIVE_TEMPLATE_MAP.get(cluster);
        if (template != null) {
            return template;
        }
        synchronized (RedisUtils.class) {
            try {
                template = REACTIVE_REDIS_CACHE_TEMPLATE_CONSTRUCTOR.newInstance(cluster);
            } catch (Exception e) {
                throw new RuntimeException("不能初始化ReactiveRedisCacheTemplate，集群名称：" + cluster);
            }
            Map<String, ReactiveRedisCacheTemplate> newMap = new HashMap<>(REACTIVE_TEMPLATE_MAP);
            newMap.put(cluster, template);
            REACTIVE_TEMPLATE_MAP = newMap;
        }
        return template;
    }

    /**
     * 创建一个分布式锁
     *
     * @param lockKey 锁的key
     * @return
     */
    public static DistributedLock newDistributedLock(String lockKey) {
        try {
            return KEY_DISTRIBUTED_LOCK_CONSTRUCTOR.newInstance(lockKey);
        } catch (Exception e) {
            throw new RuntimeException("创建分布式锁失败：" + e.getMessage());
        }
    }

    /**
     * 创建一个分布式锁
     *
     * @param lockKey     锁的key
     * @param lockTimeout 锁的超时时间
     * @return
     */
    public static DistributedLock newDistributedLock(String lockKey, int lockTimeout) {
        try {
            return KEY_AND_TIMEOUT_DISTRIBUTED_LOCK_CONSTRUCTOR.newInstance(lockKey, lockTimeout);
        } catch (Exception e) {
            throw new RuntimeException("创建分布式锁失败：" + e.getMessage());
        }
    }

}
