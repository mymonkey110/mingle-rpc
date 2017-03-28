package com.netease.mingle.rpc.server;

import com.netease.mingle.rpc.shared.ServiceCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rpc Service Register Created by Michael Jiang on 2016/11/27.
 */
public class ServiceRegister {
    private static Map<Class, Object> serviceInstanceMap = new ConcurrentHashMap<>(8);
    private static Map<String, Class> resolvedClazzMap = new ConcurrentHashMap<>(8);
    private static ServiceRegister instance = new ServiceRegister();
    private static Logger logger = LoggerFactory.getLogger(ServiceRegister.class);

    public static ServiceRegister getInstance() {
        return instance;
    }

    public <T> void register(Class<T> serviceClass, T serviceInstance) {
        if (!serviceClass.isInterface()) {
            throw new IllegalArgumentException("mingle only support interface now");
        }
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

    @SuppressWarnings("unchecked")
    public boolean isServiceRegistered(ServiceCheck serviceCheck) {
        String className = serviceCheck.getClassName();
        String methodName = serviceCheck.getMethodName();
        Class<?>[] parameterTypes = serviceCheck.getParameterTypes();

        if (resolvedClazzMap.containsKey(className)) {
            Class clazz = resolvedClazzMap.get(className);
            try {
                clazz.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                logger.debug("class:{} has no method:{} with parameter types:{}.", className, methodName,
                        parameterTypes);
                return false;
            }
        } else {
            try {
                Class clazz = Class.forName(className);
                if (serviceInstanceMap.containsKey(clazz)) {
                    resolvedClazzMap.put(className, clazz);
                }
                clazz.getDeclaredMethod(methodName, parameterTypes);
            } catch (ClassNotFoundException e) {
                logger.debug("not found class:{}.", className);
                return false;
            } catch (NoSuchMethodException e) {
                logger.debug("class:{} has no method:{} with parameter types:{}.", className, methodName,
                        parameterTypes);
                return false;
            }
        }

        return true;
    }

}
