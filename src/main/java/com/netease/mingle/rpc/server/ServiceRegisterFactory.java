package com.netease.mingle.rpc.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service Register Factory
 * Created by Michael Jiang on 16-12-2.
 */
public class ServiceRegisterFactory {
    private static Map<ClassLoader,ServiceRegister> serviceRegisterMap = new ConcurrentHashMap<ClassLoader, ServiceRegister>();

    public static ServiceRegister getRegister(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new NullPointerException("classloader is null");
        }
        if (serviceRegisterMap.containsKey(classLoader)) {
            return serviceRegisterMap.get(classLoader);
        }else {
            ServiceRegister serviceRegister = new ServiceRegister();
            serviceRegisterMap.put(classLoader,serviceRegister);
            return serviceRegister;
        }

    }
}
