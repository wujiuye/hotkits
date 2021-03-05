package com.wujiuye.hotkit.redis.impl.lettuce;

import com.wujiuye.hotkit.redis.autoconfig.BeanFactoryHolder;
import com.wujiuye.hotkit.redis.autoconfig.ClusterConfig;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Lettuce集群配置委托
 *
 * @author wujiuye 2020/11/02
 */
class LettuceClusterDelegate {

    private static final Supplier<ClusterConfig> CLUSTER_CONFIG_SUPPLIER = () -> BeanFactoryHolder.getBean(ClusterConfig.class);

    private static final ConcurrentHashMap<String, RedisClient> CLIENT_MAP = new ConcurrentHashMap<>();
    private static Map<String, StatefulRedisPubSubConnection<String, String>> CONNECTION_MAP = new HashMap<>();

    private final static ClientResources SHARE_CLIENT_RESOURCE = DefaultClientResources.builder()
            .ioThreadPoolSize(Math.max(4, Runtime.getRuntime().availableProcessors()))
            .build();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!CONNECTION_MAP.isEmpty()) {
                CONNECTION_MAP.values().forEach(StatefulConnection::close);
            }
            if (!CLIENT_MAP.isEmpty()) {
                CLIENT_MAP.values().forEach(AbstractRedisClient::shutdown);
            }
        }));
    }

    private static ClusterConfig.ClusterConnection getClusterConfig(String cluster) {
        ClusterConfig clusterConfig = CLUSTER_CONFIG_SUPPLIER.get();
        if (clusterConfig == null) {
            throw new NullPointerException("Spring环境未初始化完成！");
        }
        ClusterConfig.Cluster chooseCluster = clusterConfig.getCluster().get(cluster);
        if (chooseCluster == null) {
            throw new NullPointerException("集群" + cluster + "未配置！");
        }
        return chooseCluster.getConnection();
    }

    private static RedisURI createRedisUri(String cluster, int database) {
        ClusterConfig.ClusterConnection clusterConnection = getClusterConfig(cluster);
        return RedisURI.builder()
                .withHost(clusterConnection.getHost())
                .withPort(clusterConnection.getPort())
                .withPassword(clusterConnection.getPwd())
                .withDatabase(database)
                .build();
    }

    private static RedisClient getRedisClient(String cluster, int database) {
        return CLIENT_MAP.computeIfAbsent(cluster + "::" + database,
                key -> RedisClient.create(SHARE_CLIENT_RESOURCE, createRedisUri(cluster, database)));
    }

    /**
     * 获取某个集群的某个db的连接
     * 当然，是不推荐使用多db的
     *
     * @param cluster  集群名称
     * @param database 集群db
     * @return
     */
    public static StatefulRedisPubSubConnection<String, String> getStatefulRedisPubSubConnection(String cluster, int database) {
        String key = cluster + "::" + database;
        StatefulRedisPubSubConnection<String, String> connection = CONNECTION_MAP.get(key);
        if (connection == null) {
            synchronized (LettuceClusterDelegate.class) {
                if (CONNECTION_MAP.containsKey(key)) {
                    return CONNECTION_MAP.get(key);
                }
                connection = getRedisClient(cluster, database).connectPubSub();
                Map<String, StatefulRedisPubSubConnection<String, String>> newMap = new HashMap<>(CONNECTION_MAP);
                newMap.put(key, connection);
                CONNECTION_MAP = newMap;
            }
        }
        return connection;
    }

    /**
     * 随机选择一个集群
     *
     * @return
     */
    public static StatefulRedisPubSubConnection<String, String> chooseDefaultClusterConnection() {
        Optional<StatefulRedisPubSubConnection<String, String>> connection = CONNECTION_MAP.values().stream().findFirst();
        if (connection.isPresent()) {
            return connection.get();
        } else {
            ClusterConfig clusterConfig = CLUSTER_CONFIG_SUPPLIER.get();
            if (clusterConfig == null) {
                throw new NullPointerException("未配置任何Redis集群");
            }
            // 只初始化集群的db 0 链接
            clusterConfig.getCluster().forEach((key, value) -> getStatefulRedisPubSubConnection(key, 0));
            connection = CONNECTION_MAP.values().stream().findFirst();
            if (connection.isPresent()) {
                return connection.get();
            } else {
                throw new NullPointerException("未配置任何Redis集群");
            }
        }
    }

}
