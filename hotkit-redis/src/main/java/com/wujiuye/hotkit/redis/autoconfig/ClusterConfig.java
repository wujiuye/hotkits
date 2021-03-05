package com.wujiuye.hotkit.redis.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Redis集群配置
 *
 * @author wujiuye 2020/10/15
 */
@Component
@ConfigurationProperties(prefix = "redis")
public class ClusterConfig {

    /**
     * key为集群名称
     */
    private Map<String, Cluster> cluster;

    public Map<String, Cluster> getCluster() {
        return cluster;
    }

    public void setCluster(Map<String, Cluster> cluster) {
        this.cluster = cluster;
    }

    public static class Cluster {
        private ClusterConnection connection;
        /**
         * 使用lettuce不需要配置
         */
        private ClusterPool pool;

        public ClusterConnection getConnection() {
            return connection;
        }

        public void setConnection(ClusterConnection connection) {
            this.connection = connection;
        }

        public ClusterPool getPool() {
            return pool;
        }

        public void setPool(ClusterPool pool) {
            this.pool = pool;
        }
    }

    public static class ClusterConnection {
        private String host;
        private Integer port;
        private String pwd;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }

    public static class ClusterPool {
        /**
         * 连接池大小，最大连接总数
         */
        private int size;
        /**
         * 保持最大的空闲连接总数
         */
        private int maxIdle;
        /**
         * 保持最小的空闲连接总数
         */
        private int minIdle;
        /**
         * 空闲超时
         */
        private int idleTimeout;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getIdleTimeout() {
            return idleTimeout;
        }

        public void setIdleTimeout(int idleTimeout) {
            this.idleTimeout = idleTimeout;
        }
    }

}
