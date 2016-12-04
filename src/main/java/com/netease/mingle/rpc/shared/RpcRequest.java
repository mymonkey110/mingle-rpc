package com.netease.mingle.rpc.shared;

import com.netease.mingle.rpc.shared.util.UUIDUtil;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Rpc Request
 * Created by Michael Jiang on 2016/12/4.
 */
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 2055789247919025856L;

    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    public RpcRequest(String className, String methodName, Class<?>[] parameterTypes) {
        this(className, methodName, parameterTypes, null);
    }

    public RpcRequest(String className, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        this.requestId = UUIDUtil.generateUUID();
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    public static RpcRequest from(Method method) {
        if (method == null) {
            throw new NullPointerException("method is null");
        }
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        return new RpcRequest(className, methodName, parameterTypes);
    }

    public void addParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getRequestId() {
        return requestId;
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

    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + requestId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}