package com.wujiuye.hotkit.rpc.myfeign;

import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpRequest;
import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpResponse;
import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpSupperRetryUtils;

import java.util.function.Function;

/**
 * 基类Client，封装execute通用逻辑
 *
 * @author wujiuye 2020/07/01
 */
public abstract class BaseClient implements Client {

    @Override
    public HttpResponse execute(HttpRequest request, RetryMetadata retryMetadata) {
        if (retryMetadata.isEnableRetry()) {
            return HttpSupperRetryUtils.retryByRetryRule(() -> toSyncTask().apply(request),
                    retryMetadata.getRetryMaxNumber(),
                    retryMetadata.getRetryStrategys());
        } else {
            return toSyncTask().apply(request);
        }
    }

    protected abstract Function<HttpRequest, HttpResponse> toSyncTask();

}
