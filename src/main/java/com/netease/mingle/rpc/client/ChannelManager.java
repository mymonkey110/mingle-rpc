package com.netease.mingle.rpc.client;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Channel Manager
 * Created by Michael Jiang on 2016/12/4.
 */
public class ChannelManager {
    private static Map<Class, Channel> proxyClassChannelMap = new ConcurrentHashMap<>(8);

    public static void registerClassChannel(Class clazz, Channel channel) {
        if (!proxyClassChannelMap.containsKey(clazz)) {
            proxyClassChannelMap.put(clazz, channel);
        } else {
            throw new IllegalStateException("already register class:" + clazz.getName());
        }
    }

    public static Channel getClassChannel(Class clazz) {
        return proxyClassChannelMap.get(clazz);
    }

    public static boolean isClassChannelRegistered(Class clazz) {
        return proxyClassChannelMap.containsKey(clazz);
    }
}
