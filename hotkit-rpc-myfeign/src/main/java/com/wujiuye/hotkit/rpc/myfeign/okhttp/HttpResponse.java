package com.wujiuye.hotkit.rpc.myfeign.okhttp;

/**
 * 同步http请求的响应
 *
 * @author wujiuye 2020/05/25
 */
public class HttpResponse {

    /**
     * http响应状态码
     */
    private int code;
    /**
     * 响应body
     */
    private String body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", body='" + body + '\'' +
                '}';
    }
}
