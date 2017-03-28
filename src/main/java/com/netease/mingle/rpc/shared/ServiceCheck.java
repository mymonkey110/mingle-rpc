package com.netease.mingle.rpc.shared;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 服务确认 Created by jiangwenkang on 2017/3/28.
 */
public class ServiceCheck implements Serializable {
    private static final long serialVersionUID = -1535117010242742608L;

    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Boolean exist;

    private ServiceCheck(String className, String methodName, Class<?>[] parameterTypes) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.exist = false;
    }

    public static ServiceCheck fromMethod(Method method) {
        if (method == null) {
            throw new IllegalArgumentException("method is null");
        }
        String className = method.getClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        return new ServiceCheck(className, methodName, parameterTypes);
    }

    public static ServiceCheck fromRpcRequest(RpcRequest rpcRequest) {
        if (rpcRequest == null) {
            throw new IllegalArgumentException("rpc request is null");
        }

        String className = rpcRequest.getClassName();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        return new ServiceCheck(className, methodName, parameterTypes);
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    @Override
    public String toString() {
        return "ServiceCheck{" + "className='" + className + '\'' + ", methodName='" + methodName + '\''
                + ", parameterTypes=" + Arrays.toString(parameterTypes) + ", exist=" + exist + '}';
    }
}
