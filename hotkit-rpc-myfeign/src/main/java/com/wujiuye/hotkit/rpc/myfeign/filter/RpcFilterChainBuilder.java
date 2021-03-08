package com.wujiuye.hotkit.rpc.myfeign.filter;

/**
 * 过滤器链构造器
 *
 * @author wujiuye 2020/07/21
 */
public interface RpcFilterChainBuilder {

    /**
     * 构造过滤器链
     *
     * @return
     */
    RpcFilterChain build();

}
