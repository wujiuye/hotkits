package com.wujiuye.hotkit.redis.impl.jedis;

import com.wujiuye.hotkit.redis.autoconfig.BeanFactoryHolder;
import com.wujiuye.hotkit.redis.autoconfig.ClusterConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.util.Pool;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 为以后该模块拆分为独立服务而做的适配层
 *
 * @author wujiuye 2020/09/23
 */
class JedisClusterDelegate {

    private static final Supplier<ClusterConfig> CLUSTER_CONFIG_SUPPLIER = () -> BeanFactoryHolder.getBean(ClusterConfig.class);

    private static Map<String, JedisPool> CLUSTER_JEDIS_POOL_MAP = new HashMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!CLUSTER_JEDIS_POOL_MAP.isEmpty()) {
                CLUSTER_JEDIS_POOL_MAP.values().forEach(Pool::close);
            }
        }));
    }

    /**
     * 随机选择一个集群
     *
     * @return
     */
    public static Optional<JedisPool> chooseDefaultJedisPool() {
        Optional<JedisPool> jedisPool = CLUSTER_JEDIS_POOL_MAP.values().stream().findFirst();
        if (jedisPool.isPresent()) {
            return jedisPool;
        } else {
            ClusterConfig clusterConfig = CLUSTER_CONFIG_SUPPLIER.get();
            if (clusterConfig == null) {
                return jedisPool;
            }
            clusterConfig.getCluster().forEach((key, value) -> chooseJedisPool(key));
            return CLUSTER_JEDIS_POOL_MAP.values().stream().findFirst();
        }
    }

    /**
     * 获取集群对应的连接池
     *
     * @param cluster 集群名称
     * @return
     */
    public static JedisPool chooseJedisPool(String cluster) {
        JedisPool jedisPool = CLUSTER_JEDIS_POOL_MAP.get(cluster);
        if (jedisPool == null) {
            synchronized (JedisClusterDelegate.class) {
                if (CLUSTER_JEDIS_POOL_MAP.containsKey(cluster)) {
                    return CLUSTER_JEDIS_POOL_MAP.get(cluster);
                }
                jedisPool = createJedisPool(cluster);
                Map<String, JedisPool> newMap = new HashMap<>(CLUSTER_JEDIS_POOL_MAP);
                newMap.put(cluster, jedisPool);
                CLUSTER_JEDIS_POOL_MAP = newMap;
            }
        }
        return jedisPool;
    }

    /**
     * 为指定集群创建Jedis连接池
     *
     * @param cluster 集群名称
     * @return
     */
    private static JedisPool createJedisPool(String cluster) {
        ClusterConfig clusterConfig = CLUSTER_CONFIG_SUPPLIER.get();
        if (clusterConfig == null) {
            throw new NullPointerException("Spring环境未初始化完成！");
        }
        ClusterConfig.Cluster chooseCluster = clusterConfig.getCluster().get(cluster);
        if (chooseCluster == null) {
            throw new NullPointerException("集群" + cluster + "未配置！");
        }
        ClusterConfig.ClusterConnection connectionCfg = chooseCluster.getConnection();
        return new JedisPool(createJedisPoolConfigOrGetDefault(chooseCluster.getPool()),
                connectionCfg.getHost(), connectionCfg.getPort(),
                5000, connectionCfg.getPwd());
    }

    /**
     * 创建连接池配置对象
     *
     * @param poolConfig 该集群的连接池配置
     * @return
     */
    private static JedisPoolConfig createJedisPoolConfigOrGetDefault(ClusterConfig.ClusterPool poolConfig) {
        if (poolConfig == null) {
            return defaultJedisPoolConfig();
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(poolConfig.getMaxIdle());
        config.setMaxTotal(poolConfig.getSize());
        config.setMinIdle(poolConfig.getMinIdle());
        config.setMaxWaitMillis(poolConfig.getIdleTimeout());
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        return config;
    }

    /**
     * 使用默认的集群连接池配置
     *
     * @return
     */
    private static JedisPoolConfig defaultJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(200);
        config.setMaxTotal(200);
        config.setMinIdle(200);
        config.setMaxWaitMillis(5000);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        return config;
    }

}
