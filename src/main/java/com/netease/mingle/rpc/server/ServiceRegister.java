package com.netease.mingle.rpc.server;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael Jiang on 2016/11/27.
 */
public class ServiceRegister {
    private final static Map<Class, Object> classInstanceMap = new ConcurrentHashMap<Class, Object>(8);

    public static <T> void register(Class<T> tClass, T instance) {
        if (classInstanceMap.containsKey(tClass)) {
            throw new IllegalStateException("already register class:" + tClass.getName());
        }
        classInstanceMap.put(tClass, instance);
    }

    public Set<Class> getServiceClass() {
        return classInstanceMap.keySet();
    }
}
