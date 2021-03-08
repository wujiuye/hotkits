package com.wujiuye.hotkit.rpc.myfeign.filter;

import com.wujiuye.hotkit.rpc.myfeign.MethodHandler;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 过滤器链
 *
 * @author wujiuye 2020/07/21
 */
public class RpcFilterChain extends AbstractLinkRpcFilter {

    private AbstractLinkRpcFilter first = new AbstractLinkRpcFilter() {
        @Override
        public Object doFilter(MethodHandler handler, Method method, Object[] args) throws IOException {
            return fireFilter(handler, method, args);
        }
    };

    private AbstractLinkRpcFilter end = first;

    @Override
    public Object doFilter(MethodHandler handler, Method method, Object[] args) throws IOException {
        return first.doFilter(handler, method, args);
    }

    public void addLast(RpcFilter filter) {
        end.setNext(filter);
        end = (AbstractLinkRpcFilter) end.getNext();
    }

}
