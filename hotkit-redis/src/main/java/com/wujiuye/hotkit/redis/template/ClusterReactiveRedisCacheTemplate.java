package com.wujiuye.hotkit.redis.template;

/**
 * 区分不同集群的RedisCacheTemplate
 *
 * @author wujiuye 2020/09/23
 */
public abstract class ClusterReactiveRedisCacheTemplate implements ReactiveRedisCacheTemplate {

    private String cluster;

    public ClusterReactiveRedisCacheTemplate(String cluster) {
        this.cluster = cluster;
    }

    protected String getCluster() {
        return this.cluster;
    }

}
