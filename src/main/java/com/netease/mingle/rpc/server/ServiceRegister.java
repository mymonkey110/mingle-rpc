package com.netease.mingle.rpc.server;

import com.netease.mingle.rpc.shared.MethodInvocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rpc Service Register
 * Created by Michael Jiang on 2016/11/27.
 */
public class ServiceRegister {
    private final static Map<Class, List<MethodInvocation>> classInstanceMap = new ConcurrentHashMap<>(8);
    private final static Map<MethodInvocation, Object> serviceHostMap = new ConcurrentHashMap<>();

    public <T> void register(Class<T> serviceClass, T serviceInstance) {
        if (classInstanceMap.containsKey(serviceClass)) {
            throw new IllegalStateException("already register class:" + serviceClass.getName());
        }
        List<MethodInvocation> methodInvocations = resolveServiceInterface(serviceClass);
        classInstanceMap.put(serviceClass, methodInvocations);
        for (MethodInvocation methodInvocation : methodInvocations) {
            serviceHostMap.put(methodInvocation, serviceInstance);
        }
    }

    public Set<Class> getServiceClass() {
        return classInstanceMap.keySet();
    }

    private List<MethodInvocation> resolveServiceInterface(Class<?> serviceInterface) {
        if (!serviceInterface.isInterface()) {
            throw new IllegalArgumentException("register service " + serviceInterface.getName() + " is not interface");
        }
        Method[] methods = serviceInterface.getDeclaredMethods();
        List<MethodInvocation> methodInvocations = new ArrayList<>();
        for (Method method : methods) {
            MethodInvocation methodInvocation = MethodInvocation.from(method);
            methodInvocations.add(methodInvocation);
        }
        return methodInvocations;
    }
}
