package com.wujiuye.hotkit.redis.multidb;

/**
 * db持有者
 * 站在不应该使用多db的角度，让多db支持使用动态切换方式，不污染模版类
 *
 * @author wujiuye 2020/10/19
 */
public final class RedisDataSourceHodler {

    private final static ThreadLocal<Integer> CONTEXT = new ThreadLocal<>();

    private RedisDataSourceHodler() {

    }

    public static void switchDataSouce(int db) {
        CONTEXT.set(db);
    }

    public static int getDataSource() {
        Integer db = CONTEXT.get();
        return db == null ? 0 : db;
    }

    public static void clearDataSource() {
        CONTEXT.remove();
    }

}
