package com.wujiuye.hotkit.rpc.myfeign.filter;

import com.wujiuye.hotkit.rpc.myfeign.MethodHandler;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 抽象过滤器链RpcFilter
 *
 * @author wujiuye 2020/07/21
 */
public abstract class AbstractLinkRpcFilter implements RpcFilter {

    private RpcFilter next;

    public AbstractLinkRpcFilter() {
    }

    public void setNext(RpcFilter filter) {
        this.next = filter;
    }

    public RpcFilter getNext() {
        return next;
    }

    @Override
    public Object fireFilter(MethodHandler handler, Method method, Object[] args) throws IOException {
        return next == null ? (handler.invoke(args)) : next.doFilter(handler, method, args);
    }

}
