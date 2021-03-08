package com.wujiuye.hotkit.rpc.myfeign;

import com.wujiuye.hotkit.json.JsonUtils;
import com.wujiuye.hotkit.rpc.myfeign.annotation.ShowLog;
import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpRequest;
import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpResponse;
import com.wujiuye.hotkit.rpc.myfeign.processor.PostProcessorRegister;
import com.wujiuye.hotkit.rpc.myfeign.annotation.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 方法拦截处理器
 *
 * @author wujiuye 2020/07/01
 */
public class MethodHandler {

    private final static Logger logger = LoggerFactory.getLogger("rpc");

    private Class<?> returnType;
    private RpcMetadata rpcMetadata;
    private Client client;
    private boolean showRequestLog;
    private boolean showLogResponse;

    public MethodHandler(RpcClient rpcClient, Client client, Method method) {
        this.client = client;
        // 解析请求映射
        rpcMetadata = AnnotationUtils.analysisMappingAnnotation(rpcClient, method);
        if (rpcMetadata == null) {
            throw new NullPointerException("METHOD NOT FOUND MAPPING ANNOTATION!");
        }
        // 获取方法返回值类
        returnType = method.getReturnType();
        ShowLog showLog = method.getAnnotation(ShowLog.class);
        if (showLog != null) {
            showRequestLog = showLog.request();
            showLogResponse = showLog.response();
        }
    }

    public Object invoke(Object[] args) throws IOException {
        String requestId = UUID.randomUUID().toString();
        HttpRequest request = new HttpRequest();
        request.setUrl(rpcMetadata.getUrl());
        request = PostProcessorRegister.REGISTER.postProcessor(request, rpcMetadata, args);
        if (showRequestLog) {
            logger.info("rpc request {} ==> url:{}, method:{}, request body:{}", requestId,
                    rpcMetadata.getUrl(), rpcMetadata.getMethod(),
                    request.getBody() == null ? null : JsonUtils.toJsonString(request.getBody()));
        }
        HttpResponse response = client.execute(request, rpcMetadata.getRetryMetadata());
        if (showLogResponse) {
            logger.info("rpc response {} ==> url:{}, method:{}, response body:{}", requestId,
                    rpcMetadata.getUrl(), rpcMetadata.getMethod(),
                    response == null ? null : JsonUtils.toJsonString(response));
        }
        if (response != null && response.getCode() == 200 && response.getBody() != null) {
            return JsonUtils.fromJson(response.getBody(), returnType);
        }
        throw new IOException("请求失败！");
    }

}
