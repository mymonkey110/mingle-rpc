package com.netease.mingle.rpc.shared;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 服务确认 Created by jiangwenkang on 2017/3/28.
 */
public class ServiceCheck implements Serializable {
    private static final long serialVersionUID = -1535117010242742608L;

    private String className;
    private Boolean exist;

    private ServiceCheck(String className) {
        this.className = className;
        this.exist = false;
    }

    public static ServiceCheck fromMethod(Method method) {
        if (method == null) {
            throw new IllegalArgumentException("method is null");
        }
        String className = method.getClass().getName();
        return new ServiceCheck(className);
    }

    public static ServiceCheck fromClass(Class clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null");
        }
        String className = clazz.getName();
        return new ServiceCheck(className);
    }

    public static ServiceCheck fromRpcRequest(RpcRequest rpcRequest) {
        if (rpcRequest == null) {
            throw new IllegalArgumentException("rpc request is null");
        }

        String className = rpcRequest.getClassName();
        return new ServiceCheck(className);
    }

    public String getClassName() {
        return className;
    }

    public Boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    @Override
    public String toString() {
        return "ServiceCheck{" + "className='" + className + '\'' + ", exist=" + exist + '}';
    }
}
