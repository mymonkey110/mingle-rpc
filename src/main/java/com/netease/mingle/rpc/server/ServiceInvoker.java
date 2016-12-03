package com.netease.mingle.rpc.server;

import com.netease.mingle.rpc.shared.MethodInvocation;

/**
 * Created by Michael Jiang on 2016/12/3.
 */
public class ServiceInvoker {
    private Class aClass;
    private String methodName;
    private Object instance;
    private Object[] parameters;
    private ServiceRegister serviceRegister;

    private ServiceInvoker(MethodInvocation methodInvocation) {
        serviceRegister = ServiceRegisterFactory.getRegister(this.getClass().getClassLoader());
        if (serviceRegister.isServiceRegistered(methodInvocation)) {
            this.aClass = serviceRegister.getRegisteredClass(methodInvocation.getClassName());
            this.methodName = methodInvocation.getMethodName();

        }
    }

    public static ServiceInvoker getServiceInvoker(MethodInvocation methodInvocation) {
        return new ServiceInvoker(methodInvocation);
    }


}
