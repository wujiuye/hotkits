package com.wujiuye.hotkit.rpc.myfeign;

import com.wujiuye.hotkit.rpc.myfeign.filter.DefaultRpcFilterChainBuilder;
import com.wujiuye.hotkit.rpc.myfeign.filter.RpcFilterChain;
import com.wujiuye.hotkit.rpc.myfeign.filter.RpcFilterChainBuilder;
import com.wujiuye.hotkit.rpc.myfeign.annotation.RpcClient;
import com.wujiuye.hotkit.rpc.myfeign.filter.SpiLoaderUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Rpc代理工厂
 *
 * @author wujiuye 2020/07/01
 */
public class RpcProxyFactory {

    /**
     * 缓存代理对象
     */
    private static final ConcurrentMap<Class<?>, Object> PROXY_CACHE = new ConcurrentHashMap<>();
    private static final RpcFilterChain FILTER_CHAIN;

    private RpcProxyFactory() {
    }

    static {
        RpcFilterChainBuilder builder = SpiLoaderUtils.getFirstNotDefaultService(RpcFilterChainBuilder.class,
                DefaultRpcFilterChainBuilder.class);
        FILTER_CHAIN = builder.build();
    }

    private static class RpcClientInvocationHandler implements InvocationHandler {

        private Map<Method, MethodHandler> methodHandlerMap = new HashMap<>();

        public RpcClientInvocationHandler(Class<?> apiClass) {
            RpcClient rpcClient = apiClass.getAnnotation(RpcClient.class);
            if (rpcClient == null) {
                throw new NullPointerException("NOT FOUND @FeignClient BY " + apiClass.getName());
            }
            Method[] methods = apiClass.getMethods();
            for (Method method : methods) {
                methodHandlerMap.put(method, new MethodHandler(rpcClient, new DefaultClient(), method));
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodHandler handler = methodHandlerMap.get(method);
            return FILTER_CHAIN.doFilter(handler, method, args);
        }

    }

    public static <T> T createProxy(Class<T> apiClass, ClassLoader classLoader) {
        if (!apiClass.isInterface()) {
            throw new UnsupportedOperationException("不支持非接口！");
        }
        if (PROXY_CACHE.containsKey(apiClass)) {
            return (T) PROXY_CACHE.get(apiClass);
        }
        synchronized (apiClass) {
            if (classLoader == null) {
                classLoader = Thread.currentThread().getContextClassLoader();
            }
            Object proxy = Proxy.newProxyInstance(classLoader,
                    new Class<?>[]{apiClass},
                    new RpcClientInvocationHandler(apiClass));
            PROXY_CACHE.put(apiClass, proxy);
        }
        return (T) PROXY_CACHE.get(apiClass);
    }

}
