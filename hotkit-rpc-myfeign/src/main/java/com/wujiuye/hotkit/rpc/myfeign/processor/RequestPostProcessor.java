package com.wujiuye.hotkit.rpc.myfeign.processor;

import com.wujiuye.hotkit.rpc.myfeign.RpcMetadata;
import com.wujiuye.hotkit.rpc.myfeign.okhttp.HttpRequest;

/**
 * 请求前置处理器
 *
 * @author wujiuye 2020/07/02
 */
public interface RequestPostProcessor {

    /**
     * 处理器排序
     *
     * @return
     */
    default int order() {
        return Integer.MAX_VALUE;
    }

    /**
     * 在请求之前实现参数映射、添加请求头等
     *
     * @param request     请求构造器
     * @param rpcMetadata 请求元数据
     * @param args        请求参数
     * @return
     */
    HttpRequest postProcessor(HttpRequest request, RpcMetadata rpcMetadata, Object[] args);

}
