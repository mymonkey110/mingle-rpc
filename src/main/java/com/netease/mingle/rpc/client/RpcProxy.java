package com.netease.mingle.rpc.client;

import com.netease.mingle.rpc.shared.RpcRequest;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Rpc Proxy
 * Created by jiangwenkang on 2017/1/4.
 */
class RpcProxy<T> implements InvocationHandler {
    private Class<T> service;
    private ServiceAddress address;

    RpcProxy(Class<T> service, ServiceAddress address) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("mingle can only refer interface now");
        }
        if (address == null) {
            throw new NullPointerException("service remote address can't be null");
        }
        this.service = service;
        this.address = address;
    }

    @SuppressWarnings("unchecked")
    T getProxyObject() {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{service}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.from(method).addParameters(args);

        Channel channel = RpcClient.getInstance().getServiceAddressBindedChannel(address);
        ClientHandler clientHandler = channel.pipeline().get(ClientHandler.class);
        ServiceCallContext serviceCallContext = clientHandler.sendRequest(rpcRequest);
        return serviceCallContext.get();
    }
}