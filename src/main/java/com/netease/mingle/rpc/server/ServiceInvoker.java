package com.netease.mingle.rpc.server;

import com.netease.mingle.rpc.shared.RpcRequest;
import com.netease.mingle.rpc.shared.ServiceCheck;
import com.netease.mingle.rpc.shared.exception.MethodNotFoundException;
import com.netease.mingle.rpc.shared.exception.SystemException;

import java.lang.reflect.Method;

/**
 * Service Invoker Created by Michael Jiang on 2016/12/3.
 */
public class ServiceInvoker {
    private static ServiceRegister serviceRegister = ServiceRegister.getInstance();

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

    public static ServiceInvoker getServiceInvoker(RpcRequest rpcRequest) {
        return new ServiceInvoker(rpcRequest);
    }

    @SuppressWarnings("unchecked")
    public Object invoke() {
        try {
            Method method = clazz.getMethod(methodName, parameterTypes);
            return method.invoke(instance, parameters);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return new MethodNotFoundException();
        } catch (Exception e) {
            return new SystemException();
        }
    }
}
