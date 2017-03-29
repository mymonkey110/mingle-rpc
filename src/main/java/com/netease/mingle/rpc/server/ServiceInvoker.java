package com.netease.mingle.rpc.server;

import com.netease.mingle.rpc.shared.RpcRequest;
import com.netease.mingle.rpc.shared.ServiceCheck;
import com.netease.mingle.rpc.shared.exception.MethodNotFoundException;
import com.netease.mingle.rpc.shared.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Service Invoker Created by Michael Jiang on 2016/12/3.
 */
public class ServiceInvoker {
    private static ServiceRegister serviceRegister = ServiceRegister.getInstance();
    private static Logger logger = LoggerFactory.getLogger(ServiceInvoker.class);

    private Class clazz;
    private String methodName;
    private Object instance;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    private ServiceInvoker(RpcRequest rpcRequest) {
        ServiceCheck serviceCheck = ServiceCheck.fromRpcRequest(rpcRequest);
        // TODO: Optimize Point, Accelerate Check
        if (serviceRegister.isServiceRegistered(serviceCheck)) {
            this.clazz = serviceRegister.getRegisteredClass(rpcRequest.getClassName());
            this.methodName = rpcRequest.getMethodName();
            this.instance = serviceRegister.getServiceInstance(clazz);
            this.parameterTypes = rpcRequest.getParameterTypes();
            this.parameters = rpcRequest.getParameters();
        } else {
            throw new RuntimeException("method is not registered!");
        }
    }

    static ServiceInvoker getServiceInvoker(RpcRequest rpcRequest) {
        return new ServiceInvoker(rpcRequest);
    }

    @SuppressWarnings("unchecked")
    Object invoke() {
        try {
            Method method = clazz.getMethod(methodName, parameterTypes);
            return method.invoke(instance, parameters);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("invoke method exception:{}.", e.getMessage(), e);
            return e.getCause();
        }
    }
}
