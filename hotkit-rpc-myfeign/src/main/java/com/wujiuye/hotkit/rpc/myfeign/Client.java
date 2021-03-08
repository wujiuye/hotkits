package com.wujiuye.hotkit.rpc.myfeign;

import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpRequest;
import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpResponse;

import java.io.IOException;

/**
 * @author wujiuye 2020/07/01
 */
public interface Client {

    /**
     * 发起RPC请求
     *
     * @param request
     * @return
     */
    HttpResponse execute(HttpRequest request, RetryMetadata retryMetadata) throws IOException;

}
