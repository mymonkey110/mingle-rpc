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
    private Map<Class,Object> serviceInstanceMap;
    private Map<String, Class> resolvedClazzMap;

    public ServiceRegister() {
        this.serviceInstanceMap = new ConcurrentHashMap<>(8);
        this.resolvedClazzMap = new ConcurrentHashMap<>(8);
    }

    public <T> void register(Class<T> serviceClass, T serviceInstance) {
        if (serviceInstanceMap.containsKey(serviceClass)) {
            throw new IllegalStateException("already register class:" + serviceClass.getName());
        }
        serviceInstanceMap.put(serviceClass, serviceInstance);
    }

    public Class getRegisteredClass(String className) {
        for (Class clazz : serviceInstanceMap.keySet()) {
            if (clazz.getName().equals(className)) {
                return clazz;
            }
        }
        throw new RuntimeException("not found registered class: " + className);
    }

    public Object getServiceInstance(Class clazz) {
        return serviceInstanceMap.get(clazz);
    }

    public Set<Class> getServiceClass() {
        return serviceInstanceMap.keySet();
    }

    public boolean isServiceRegistered(MethodInvocation methodInvocation) {
        String className = methodInvocation.getClassName();
        if (resolvedClazzMap.containsKey(className)) {
            return true;
        }
        try {
            Class clazz = Class.forName(className);
            resolvedClazzMap.put(className, clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
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
