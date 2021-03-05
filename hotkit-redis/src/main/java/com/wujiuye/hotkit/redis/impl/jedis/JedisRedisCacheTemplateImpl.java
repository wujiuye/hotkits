package com.wujiuye.hotkit.redis.impl.jedis;

import com.wujiuye.hotkit.redis.multidb.RedisDataSourceHodler;
import com.wujiuye.hotkit.redis.serializable.CacheSerializable;
import com.wujiuye.hotkit.redis.template.ClusterRedisCacheTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 支持切换DB的缓存模版
 * <p>
 * 约定：ex[0]传db
 *
 * @author wujiuye 2020/09/23
 */
public class JedisRedisCacheTemplateImpl extends ClusterRedisCacheTemplate {

    private CacheSerializable keyCacheSerializable;
    private CacheSerializable valueCacheSerializable;

    public JedisRedisCacheTemplateImpl(String cluster) {
        super(cluster);
        keyCacheSerializable = super.getKeyCacheSerializable();
        valueCacheSerializable = super.getValueCacheSerializable();
    }

    @Override
    public CacheSerializable getKeyCacheSerializable() {
        return keyCacheSerializable;
    }

    @Override
    public void setExpireTime(Object key, int seconds) {
        try (Jedis jedis = getJedis()) {
            jedis.expire(keyCacheSerializable.serialize(key), seconds);
        }
    }

    @Override
    public void delKey(Object key) {
        try (Jedis jedis = getJedis()) {
            jedis.del(keyCacheSerializable.serialize(key));
        }
    }

    @Override
    public CacheSerializable getValueCacheSerializable() {
        return valueCacheSerializable;
    }

    private Jedis getJedis() {
        int db = RedisDataSourceHodler.getDataSource();
        JedisPool jedisPool = JedisClusterDelegate.chooseJedisPool(getCluster());
        if (jedisPool == null) {
            throw new NullPointerException("cluster " + getCluster() + " not found define.");
        }
        Jedis jedis = jedisPool.getResource();
        if (jedis == null) {
            throw new NullPointerException("get conn by jedis pool fail...");
        }
        jedis.select(db);
        return jedis;
    }

    @Override
    public void setValue(Object key, Object value) {
        try (Jedis jedis = getJedis()) {
            jedis.set(keyCacheSerializable.serialize(key), valueCacheSerializable.serialize(value));
        }
    }

    @Override
    public <T> T getValue(Object key, Class<T> valueClass) {
        try (Jedis jedis = getJedis()) {
            String value = jedis.get(keyCacheSerializable.serialize(key));
            if (value == null) {
                return null;
            }
            return valueCacheSerializable.deserialize(value, valueClass);
        }
    }

    @Override
    public void putMap(Object firstKey, Object scondKey, Object value) {
        try (Jedis jedis = getJedis()) {
            String key = keyCacheSerializable.serialize(firstKey);
            String field = keyCacheSerializable.serialize(scondKey);
            jedis.hset(key, field, valueCacheSerializable.serialize(value));
        }
    }

    @Override
    public <T> T getMap(Object firstKey, Object scondKey, Class<T> valueClass) {
        try (Jedis jedis = getJedis()) {
            String key = keyCacheSerializable.serialize(firstKey);
            String field = keyCacheSerializable.serialize(scondKey);
            String value = jedis.hget(key, field);
            if (value == null) {
                return null;
            }
            return valueCacheSerializable.deserialize(value, valueClass);
        }
    }

    @Override
    public Set<String> getMapKeys(Object firstKey) {
        try (Jedis jedis = getJedis()) {
            return jedis.hkeys(keyCacheSerializable.serialize(firstKey));
        }
    }

}
