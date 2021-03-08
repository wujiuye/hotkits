package com.wujiuye.hotkit.rpc.myfeign.processor;

import com.wujiuye.hotkit.rpc.myfeign.ParamMetadata;
import com.wujiuye.hotkit.rpc.myfeign.HttpMethod;
import com.wujiuye.hotkit.rpc.myfeign.RpcMetadata;
import com.wujiuye.hotkit.rpc.myfeign.annotation.RequestBody;
import com.wujiuye.hotkit.rpc.myfeign.annotation.RequestParam;
import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpRequest;
import com.wujiuye.hotkit.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 请求参数前置处理器
 *
 * @author wujiuye 2020/07/02
 */
public class ParamPostProcessor implements RequestPostProcessor {

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }

    @Override
    public HttpRequest postProcessor(HttpRequest request, RpcMetadata rpcMetadata, Object[] args) {
        List<ParamMetadata> paramMetadataList = rpcMetadata.getParams();
        if (!CollectionUtils.isEmpty(paramMetadataList)) {
            request.setUrl(urlJoinParams(request.getUrl(), paramMetadataList, args));
        }
        request.setMethod(rpcMetadata.getMethod().name());
        if (rpcMetadata.getMethod() == HttpMethod.POST) {
            request.setBody(getRequestBody(paramMetadataList, args));
        }
        return request;
    }

    private String urlJoinParams(String url, List<ParamMetadata> paramMetadataList, Object[] args) {
        boolean isFistParam = !url.contains("=");
        for (ParamMetadata paramMetadata : paramMetadataList) {
            Annotation[] paramAnnotations = paramMetadata.getParamAnnotations();
            if (!CollectionUtils.isEmpty(paramAnnotations)) {
                for (Annotation anno : paramAnnotations) {
                    if (anno instanceof RequestParam) {
                        if (!url.startsWith("?")) {
                            url = url.concat("?");
                        }
                        if (!isFistParam) {
                            url = url.concat("&");
                        }
                        isFistParam = false;
                        url = url.concat(((RequestParam) anno).value());
                        url = url.concat("=");
                        url = url.concat(String.valueOf(args[paramMetadata.getParamIndexOnMethod()]));
                    }
                }
            }
        }
        return url;
    }

    private Object getRequestBody(List<ParamMetadata> paramMetadataList, Object[] args) {
        for (ParamMetadata paramMetadata : paramMetadataList) {
            Annotation[] paramAnnotations = paramMetadata.getParamAnnotations();
            if (!CollectionUtils.isEmpty(paramAnnotations)) {
                for (Annotation anno : paramAnnotations) {
                    if (anno instanceof RequestBody) {
                        return args[paramMetadata.getParamIndexOnMethod()];
                    }
                }
            }
        }
        return null;
    }

}
