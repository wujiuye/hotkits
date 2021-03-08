package com.wujiuye.hotkit.rpc.myfeign.okhttp;

import com.wujiuye.hotkit.json.JsonUtils;

/**
 * 异步获取结果，在业务之前发送请求，
 * 在业务需要获取请求结果时再调用get
 *
 * @author wujiuye 2020/05/27
 */
public class AsyncHttpFuture<T> {

    private Class<T> tClass;
    private volatile T response;
    private volatile Exception exception;

    public AsyncHttpFuture(Class<T> tClass) {
        this.tClass = tClass;
    }

    synchronized void setResponse(HttpResponse response) {
        if (response.getCode() == 200) {
            try {
                this.response = JsonUtils.fromJson(response.getBody(), tClass);
                // 当字符串为""时，parseObject不抛出异常，而是返回null
                if (this.response == null) {
                    this.exception = new NullPointerException("response is null.");
                }
            } catch (Exception e) {
                this.exception = new Exception("parse response json msg error.");
            }
        } else {
            this.exception = new Exception("response code is " + response.getCode());
        }
        this.notifyAll();
    }

    synchronized void setException(Exception exception) {
        this.exception = exception;
        this.notifyAll();
    }

    public synchronized T get() throws Exception {
        if (response == null && exception == null) {
            this.wait();
        }
        if (this.exception != null) {
            throw this.exception;
        }
        return this.response;
    }

}
