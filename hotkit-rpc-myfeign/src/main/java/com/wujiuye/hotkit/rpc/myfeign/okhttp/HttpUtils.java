package com.wujiuye.hotkit.rpc.myfeign.okhttp;

import com.wujiuye.hotkit.json.JsonUtils;
import com.wujiuye.hotkit.util.CollectionUtils;
import com.wujiuye.hotkit.util.concurrent.NameThreadFactory;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * HTTP工具类
 *
 * @author wujiuye
 * @version 1.0 on 2020/05/25
 */
public class HttpUtils {

    private final static MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json;charset=UTF-8");
    /**
     * 全局client
     */
    private final static OkHttpClient OK_HTTP_CLIENT
            = OkHttpClientConfig.getHttpClient(6000, true,
            new ThreadPoolExecutor(Integer.getInteger("okhttp3.threads", 200),
                    Integer.getInteger("okhttp3.threads", 200),
                    0, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(), new NameThreadFactory("okhttp3-", true)));

    /**
     * 发送get请求
     *
     * @param url
     * @return
     */
    public static HttpResponse sendGetRequest(String url) throws IOException {
        HttpResponse response = new HttpResponse();
        Request.Builder builder = new Request.Builder().url(url).get();
        okhttp3.Response okHttpResponse = OK_HTTP_CLIENT.newCall(builder.build()).execute();
        response.setCode(okHttpResponse.code());
        if (okHttpResponse.isSuccessful()) {
            response.setBody(Objects.requireNonNull(okHttpResponse.body()).string());
        }
        return response;
    }

    /**
     * 发送post请求 （body类型为json）
     *
     * @param url
     * @param header 请求头
     * @param body   请求body
     * @return
     */
    public static HttpResponse sendPostRequestByJson(String url, Map<String, String> header, Object body) throws IOException {
        RequestBody requestBody;
        if (body instanceof String) {
            requestBody = RequestBody.create(((String) body), JSON_MEDIA_TYPE);
        } else {
            requestBody = RequestBody.create(JsonUtils.toJsonString(body), JSON_MEDIA_TYPE);
        }
        return sendPostRequest(url, header, requestBody);
    }

    /**
     * 发送post请求 （表单提交）
     *
     * @param url
     * @param header 请求头
     * @param body   表单
     * @return
     */
    public static HttpResponse sendPostRequestByForm(String url, Map<String, String> header, Map<String, Object> body) throws IOException {
        FormBody.Builder requestBody = new FormBody.Builder();
        if (!CollectionUtils.isEmpty(body)) {
            body.forEach((key, value) -> requestBody.add(key, String.valueOf(value)));
        }
        return sendPostRequest(url, header, requestBody.build());
    }

    /**
     * 发送post请求
     *
     * @param url    请求url
     * @param header 请求头
     * @param body   请求body
     * @return
     */
    private static HttpResponse sendPostRequest(String url, Map<String, String> header, RequestBody body) throws IOException {
        HttpResponse response = new HttpResponse();
        Request.Builder builder = new Request.Builder().url(url).post(body);
        if (!CollectionUtils.isEmpty(header)) {
            header.forEach(builder::addHeader);
        }
        okhttp3.Response okHttpResponse = OK_HTTP_CLIENT.newCall(builder.build()).execute();
        response.setCode(okHttpResponse.code());
        if (okHttpResponse.isSuccessful()) {
            response.setBody(Objects.requireNonNull(okHttpResponse.body()).string());
        }
        return response;
    }

}
