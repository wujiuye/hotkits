package com.wujiuye.hotkit.redis.impl.lettuce;

import com.msyc.common.json.util.StringUtils;
import com.wujiuye.hotkit.redis.multidb.RedisDataSourceHodler;
import com.wujiuye.hotkit.redis.template.ClusterRedisCacheTemplate;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Lettuce实现
 *
 * @author wujiuye 2020/11/02
 */
public class LettuceRedisCacheTemplateImpl extends ClusterRedisCacheTemplate {

    public LettuceRedisCacheTemplateImpl(String cluster) {
        super(cluster);
    }

    private RedisPubSubCommands<String, String> getCommand() {
        return LettuceClusterDelegate.getStatefulRedisPubSubConnection(getCluster(),
                RedisDataSourceHodler.getDataSource()).sync();
    }

    @Override
    public void setExpireTime(Object key, int seconds) {
        getCommand().expire(getKeyCacheSerializable().serialize(key), seconds);
    }

    @Override
    public void delKey(Object key) {
        getCommand().del(getKeyCacheSerializable().serialize(key));
    }

    @Override
    public void setValue(Object key, Object value) {
        getCommand().set(getKeyCacheSerializable().serialize(key), getValueCacheSerializable().serialize(value));
    }

    @Override
    public <T> T getValue(Object key, Class<T> valueClass) {
        String value = getCommand().get(getKeyCacheSerializable().serialize(key));
        if (StringUtils.isNullOrEmpty(value)) {
            return null;
        }
        return getValueCacheSerializable().deserialize(value, valueClass);
    }

    @Override
    public void putMap(Object firstKey, Object scondKey, Object value) {
        getCommand().hset(getKeyCacheSerializable().serialize(firstKey),
                getKeyCacheSerializable().serialize(scondKey), getValueCacheSerializable().serialize(value));
    }

    @Override
    public <T> T getMap(Object firstKey, Object scondKey, Class<T> valueClass) {
        String value = getCommand().hget(getKeyCacheSerializable().serialize(firstKey),
                getKeyCacheSerializable().serialize(scondKey));
        if (StringUtils.isNullOrEmpty(value)) {
            return null;
        }
        return getValueCacheSerializable().deserialize(value, valueClass);
    }

    @Override
    public Set<String> getMapKeys(Object firstKey) {
        List<String> value = getCommand().hkeys(getKeyCacheSerializable().serialize(firstKey));
        if (CollectionUtils.isEmpty(value)) {
            return new HashSet<>();
        }
        return new HashSet<>(value);
    }

}
