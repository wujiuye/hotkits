package com.wujiuye.hotkit.rpc.myfeign.spring;

import com.wujiuye.hotkit.rpc.myfeign.RpcProxyFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * RpcClient的FactoryBean
 *
 * @author wujiuye 2020/07/01
 */
public class RpcClientFactoryBean implements FactoryBean<Object> {

    private String className;
    private Class<?> apiClass;

    public RpcClientFactoryBean(String className) {
        this.className = className;
    }

    @Override
    public Object getObject() throws Exception {
        return RpcProxyFactory.createProxy(apiClass, this.getClass().getClassLoader());
    }

    @Override
    public Class<?> getObjectType() {
        try {
            apiClass = this.getClass().getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return apiClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
