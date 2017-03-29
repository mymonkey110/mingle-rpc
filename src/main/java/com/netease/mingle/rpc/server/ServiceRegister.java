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

    /**
     * 服务接口导出
     *
     * @param serviceClass
     *            服务类
     * @param serviceInstance
     *            服务实例
     * @param <T>
     *            服务类型
     */
    public <T> void export(Class<T> serviceClass, T serviceInstance) {
        if (!serviceClass.isInterface()) {
            throw new IllegalArgumentException("mingle only support interface now");
        }
        if (serviceInstanceMap.containsKey(serviceClass)) {
            throw new IllegalStateException("already export class:" + serviceClass.getName());
        }
        serviceInstanceMap.put(serviceClass, serviceInstance);
    }

    Class getRegisteredClass(String className) {
        for (Class clazz : serviceInstanceMap.keySet()) {
            if (clazz.getName().equals(className)) {
                return clazz;
            }
        }
        throw new RuntimeException("not found registered class: " + className);
    }

    Object getServiceInstance(Class clazz) {
        return serviceInstanceMap.get(clazz);
    }

    @SuppressWarnings("unchecked")
    boolean isServiceRegistered(ServiceCheck serviceCheck) {
        String className = serviceCheck.getClassName();

        if (resolvedClazzMap.containsKey(className)) {
            return true;
        } else {
            try {
                Class clazz = Class.forName(className);
                if (serviceInstanceMap.containsKey(clazz)) {
                    resolvedClazzMap.put(className, clazz);
                }
                return true;
            } catch (ClassNotFoundException e) {
                logger.debug("not found class:{}.", className);
                return false;
            }
        }
    }

}
