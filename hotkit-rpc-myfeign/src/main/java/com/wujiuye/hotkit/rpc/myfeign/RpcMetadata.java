package com.wujiuye.hotkit.rpc.myfeign;

import java.util.List;

/**
 * 请求元数据
 *
 * @author wujiuye 2020/07/01
 */
public class RpcMetadata {

    private String url;
    private String path;
    private HttpMethod method;
    private List<ParamMetadata> params;
    private RetryMetadata retryMetadata;

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return (url.endsWith("/") ? url.substring(0, url.length() - 1) : url)
                + (path.startsWith("/") ? path : "/" + path);
    }

    public RetryMetadata getRetryMetadata() {
        return retryMetadata;
    }

    public List<ParamMetadata> getParams() {
        return params;
    }

    static class Builder {

        private RpcMetadata rpcMetadata;

        private Builder() {
            rpcMetadata = new RpcMetadata();
        }

        public Builder url(String url) {
            rpcMetadata.url = url;
            return this;
        }

        public Builder path(String path) {
            rpcMetadata.path = path;
            return this;
        }

        public Builder method(HttpMethod method) {
            rpcMetadata.method = method;
            return this;
        }

        public Builder params(List<ParamMetadata> params) {
            rpcMetadata.params = params;
            return this;
        }

        public Builder retry(RetryMetadata retryMetadata) {
            rpcMetadata.retryMetadata = retryMetadata;
            return this;
        }

        public RpcMetadata build() {
            return rpcMetadata;
        }

    }

    public static Builder newBuilder() {
        return new Builder();
    }

}
