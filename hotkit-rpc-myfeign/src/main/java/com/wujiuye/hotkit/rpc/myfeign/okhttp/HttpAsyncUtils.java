package com.wujiuye.hotkit.rpc.myfeign.okhttp;

import com.wujiuye.hotkit.json.JsonUtils;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * 用于异步http请求
 *
 * @author wujiuye 2020/05/25
 */
public class HttpAsyncUtils {

    private volatile static OkHttpClient okHttpClientAsync;
    private static LongAdder threadId = new LongAdder();
    private final static MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json;charset=UTF-8");

    private static OkHttpClient getOkHttpClientAsync() {
        if (okHttpClientAsync == null) {
            synchronized (HttpUtils.class) {
                if (okHttpClientAsync == null) {
                    int threads = Integer.getInteger("okhttp.threads", Runtime.getRuntime().availableProcessors());
                    ExecutorService executorService = new ThreadPoolExecutor(threads, threads, 60, TimeUnit.SECONDS,
                            new ArrayBlockingQueue<>(10),
                            run -> {
                                Thread thread = new Thread(run, "okhttp.thread-" + threadId.intValue());
                                threadId.increment();
                                return thread;
                            });
                    okHttpClientAsync = OkHttpClientConfig.getHttpClient(1000, false, executorService);
                }
            }
        }
        return okHttpClientAsync;
    }

    /**
     * 异步get
     *
     * @param url    请求的url
     * @param tClass 响应body的反序列化Java类型
     * @return Future:在需要获取结果时，调用getResponse获取返回结果，
     * 如果request过程中出错则会在该方法抛出异常
     */
    public static <T> AsyncHttpFuture<T> asyncGetRequest(String url, Class<T> tClass) {
        Request.Builder builder = new Request.Builder().url(url).get();
        return asyncSendRequest(builder, tClass);
    }

    /**
     * 异步post
     *
     * @param url    请求的url
     * @param header 请求头
     * @param body   请求body
     * @param tClass 响应body的反序列化Java类型
     * @return Future:在需要获取结果时，调用getResponse获取返回结果，
     * 如果request过程中出错则会在该方法抛出异常
     */
    public static <T> AsyncHttpFuture<T> asyncPostRequestByJson(String url, Map<String, String> header, Object body, Class<T> tClass) {
        RequestBody requestBody;
        if (body instanceof String) {
            requestBody = RequestBody.create(((String) body), JSON_MEDIA_TYPE);
        } else {
            requestBody = RequestBody.create(JsonUtils.toJsonString(body), JSON_MEDIA_TYPE);
        }
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        return asyncSendRequest(builder, tClass);
    }

    private static <T> AsyncHttpFuture<T> asyncSendRequest(Request.Builder builder, Class<T> tClass) {
        final AsyncHttpFuture<T> asyncHttpFuture = new AsyncHttpFuture<>(tClass);
        getOkHttpClientAsync().newCall(builder.build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        asyncHttpFuture.setException(e);
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response okHttpResponse) {
                        try {
                            HttpResponse response = new HttpResponse();
                            response.setCode(okHttpResponse.code());
                            if (okHttpResponse.isSuccessful()) {
                                response.setBody(Objects.requireNonNull(okHttpResponse.body()).string());
                            }
                            asyncHttpFuture.setResponse(response);
                        } catch (Exception e) {
                            asyncHttpFuture.setException(e);
                        }
                    }
                });
        return asyncHttpFuture;
    }

}
