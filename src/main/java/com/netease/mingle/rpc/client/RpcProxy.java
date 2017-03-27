package com.netease.mingle.rpc.client;

import com.netease.mingle.rpc.shared.RpcRequest;
import com.netease.mingle.rpc.shared.RpcResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Rpc Proxy Created by jiangwenkang on 2017/1/4.
 */
class RpcProxy<T> implements InvocationHandler {
    private static Logger logger = LoggerFactory.getLogger(RpcProxy.class);
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
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { service }, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isNotProxyableMethod(method)) {
            logger.debug("method:{} is not proxyable, invoke local method.");
            return method.invoke(proxy, args);
        }

        RpcRequest rpcRequest = RpcRequest.from(method).addParameters(args);
        logger.info("sending rpc request:{}.", rpcRequest);

        Channel channel = RpcClient.getInstance().getServiceAddressBindChannel(address);
        ClientHandler clientHandler = channel.pipeline().get(ClientHandler.class);
        ServiceCallContext serviceCallContext = clientHandler.sendRequest(rpcRequest);
        channel.writeAndFlush(rpcRequest);
        RpcResponse rpcResponse = serviceCallContext.get();
        if (rpcResponse.isNormalReturn()) {
            return rpcResponse.getNormalResult();
        } else if (rpcResponse.isUserThrowable()) {
            throw rpcResponse.getExceptionResult();
        } else {
            throw rpcResponse.getRpcException();
        }
    }

    private boolean isNotProxyableMethod(Method method) {
        if (method.getDeclaringClass().equals(Object.class)) {
            return true;
        }
        String methodName = method.getName();
        if (methodName.equals("toString") && method.getParameterTypes().length == 0) {
            return true;
        }
        if (methodName.equals("hashCode") && method.getParameterTypes().length == 0) {
            return true;
        }
        if (methodName.equals("equals") && method.getParameterTypes().length == 1) {
            return true;
        }
        return false;
    }
}