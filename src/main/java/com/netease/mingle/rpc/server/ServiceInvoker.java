package com.netease.mingle.rpc.server;

import com.netease.mingle.rpc.shared.MethodInvocation;
import com.netease.mingle.rpc.shared.exception.MethodNotFoundException;
import com.netease.mingle.rpc.shared.exception.SystemException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Service Invoker
 * Created by Michael Jiang on 2016/12/3.
 */
public class ServiceInvoker {
    private Class clazz;
    private String methodName;
    private Object instance;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    private ServiceInvoker(MethodInvocation methodInvocation) {
        ServiceRegister serviceRegister = ServiceRegisterFactory.getRegister(this.getClass().getClassLoader());
        if (serviceRegister.isServiceRegistered(methodInvocation)) {
            this.clazz = serviceRegister.getRegisteredClass(methodInvocation.getClassName());
            this.methodName = methodInvocation.getMethodName();
            this.instance = serviceRegister.getServiceInstance(clazz);
            this.parameterTypes = methodInvocation.getParameterTypes();
            this.parameters = methodInvocation.getParameters();
        } else {
            throw new RuntimeException("method is not registered!");
        }
    }

    public static ServiceInvoker getServiceInvoker(MethodInvocation methodInvocation) {
        return new ServiceInvoker(methodInvocation);
    }

    @SuppressWarnings("unchecked")
    public Object invoke() {
        Method[] methods = clazz.getDeclaredMethods();
        try {
            Method method = clazz.getMethod(methodName, parameterTypes);
            return method.invoke(instance, parameters);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return new MethodNotFoundException();
        } catch (IllegalAccessException | InvocationTargetException e) {
            return new SystemException();
        }
    }
}
