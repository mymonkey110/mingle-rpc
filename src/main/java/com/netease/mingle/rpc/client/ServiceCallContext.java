package com.netease.mingle.rpc.client;

import io.netty.channel.Channel;

/**
 * Service Call Context
 * Created by Michael Jiang on 2016/12/4.
 */
public class ServiceCallContext {
    private Class proxyClazz;
    private ServiceAddress serviceAddress;
    private Channel channel;

    public ServiceCallContext(Class proxyClazz, ServiceAddress serviceAddress) {
        this.proxyClazz = proxyClazz;
        this.serviceAddress = serviceAddress;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }
}
