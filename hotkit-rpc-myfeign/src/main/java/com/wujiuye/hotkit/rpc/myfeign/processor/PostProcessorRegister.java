package com.wujiuye.hotkit.rpc.myfeign.processor;

import com.wujiuye.hotkit.rpc.myfeign.RpcMetadata;
import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理器注册器
 *
 * @author wujiuye 2020/07/02
 */
public class PostProcessorRegister implements RequestPostProcessor {

    private static List<RequestPostProcessor> postProcessors;

    static {
        postProcessors = new ArrayList<>();
        postProcessors.add(0, new ParamPostProcessor());
    }

    private PostProcessorRegister() {
    }

    public final static PostProcessorRegister REGISTER = new PostProcessorRegister();

    public synchronized void registPostProcessor(RequestPostProcessor postProcessor) {
        postProcessors.add(postProcessor);
        postProcessors = postProcessors.parallelStream()
                .sorted(Comparator.comparingInt(RequestPostProcessor::order))
                .collect(Collectors.toList());
    }

    @Override
    public HttpRequest postProcessor(HttpRequest request, RpcMetadata rpcMetadata, Object[] args) {
        HttpRequest requestResullt = request;
        for (RequestPostProcessor postProcessor : postProcessors) {
            requestResullt = postProcessor.postProcessor(request, rpcMetadata, args);
        }
        return requestResullt;
    }

}
