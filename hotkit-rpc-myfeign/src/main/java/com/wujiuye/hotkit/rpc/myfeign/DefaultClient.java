package com.wujiuye.hotkit.rpc.myfeign;

import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpRequest;
import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpResponse;
import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpUtils;

import java.io.IOException;
import java.util.function.Function;

/**
 * 默认使用的Client
 *
 * @author wujiuye 2020/07/01
 */
public class DefaultClient extends BaseClient {

    @Override
    protected Function<HttpRequest, HttpResponse> toSyncTask() {
        return request -> {
            try {
                switch (request.getMethod()) {
                    case "GET":
                        return HttpUtils.sendGetRequest(request.getUrl());
                    case "POST":
                        return HttpUtils.sendPostRequestByJson(request.getUrl(), null, request.getBody());
                    default:
                }
            } catch (IOException e) {
                throw toRuntimeException(e);
            }
            throw new RuntimeException("NOT SUPPOR HTTP REQUEST METHOD!");
        };
    }

    private RuntimeException toRuntimeException(IOException e) {
        return new RuntimeException(e.getLocalizedMessage(), e.getCause());
    }

}
