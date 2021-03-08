package com.wujiuye.hotkit.rpc.myfeign.okhttp;

/**
 * @author wujiuye 2020/07/01
 */
public class HttpRequest {

    private String url;
    private String method;
    private Object body;

    public HttpRequest() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", body=" + body +
                '}';
    }

}
