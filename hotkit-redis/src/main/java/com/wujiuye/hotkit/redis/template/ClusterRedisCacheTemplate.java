package com.wujiuye.hotkit.redis.template;

/**
 * 区分不同集群的RedisCacheTemplate
 *
 * @author wujiuye 2020/09/23
 */
public abstract class ClusterRedisCacheTemplate implements RedisCacheTemplate {

    private String cluster;

    public ClusterRedisCacheTemplate(String cluster) {
        this.cluster = cluster;
    }

    protected String getCluster() {
        return this.cluster;
    }

}
