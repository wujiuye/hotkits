package com.wujiuye.hotkit.rpc.myfeign.filter;

/**
 * 默认过滤器链构造器
 *
 * @author wujiuye 2020/07/21
 */
public class DefaultRpcFilterChainBuilder implements RpcFilterChainBuilder {

    @Override
    public RpcFilterChain build() {
        RpcFilterChain chain = new RpcFilterChain();
        return chain;
    }

}
