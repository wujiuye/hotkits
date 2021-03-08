package com.wujiuye.hotkit.rpc.myfeign.okhttp;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author wujiuye 2020/05/25
 */
class OkHttpClientConfig {

    /**
     * 获取OkHttpClient
     *
     * @param timeout                  超时时间
     * @param retryOnConnectionFailure 重试次数
     * @param executorService          线程池
     * @return
     */
    static OkHttpClient getHttpClient(long timeout, boolean retryOnConnectionFailure, ExecutorService executorService) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 链接超时6秒
        builder.connectTimeout(timeout, TimeUnit.MILLISECONDS)
                // 读超时
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                // 写超时
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                // 连接失败是否重试
                .retryOnConnectionFailure(retryOnConnectionFailure);
        // 配置线程池
        if (executorService != null) {
            builder.dispatcher(new Dispatcher(executorService));
        }
        return builder.build();
    }

}
