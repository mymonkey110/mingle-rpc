package com.netease.mingle.rpc.client;

import com.netease.mingle.rpc.shared.MethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Rpc Client Proxy
 * Created by Michael Jiang on 2016/12/3.
 */
public class RpcProxy implements InvocationHandler {
    private Class rawObject;

    public RpcProxy(Class service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("mingle can only proxy interface now");
        }
        this.rawObject = service;
    }

    public Object getProxyObject() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{rawObject}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodInvocation methodInvocation = MethodInvocation.from(method);


    }
}
