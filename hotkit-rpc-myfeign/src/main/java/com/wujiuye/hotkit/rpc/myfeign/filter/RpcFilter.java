package com.wujiuye.hotkit.rpc.myfeign.filter;

import com.wujiuye.hotkit.rpc.myfeign.MethodHandler;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 过滤器，提供扩展功能
 *
 * @author wujiuye 2020/07/21
 */
public interface RpcFilter {

    /**
     * 决定是否要拦截方法的执行
     *
     * @param handler 方法处理器
     * @param method  方法
     * @param args    参数
     */
    Object doFilter(MethodHandler handler, Method method, Object[] args) throws IOException;

    /**
     * 传递给下一个过滤器
     *
     * @param handler
     * @param method
     * @param args
     * @return
     * @throws IOException
     */
    Object fireFilter(MethodHandler handler, Method method, Object[] args) throws IOException;

}
