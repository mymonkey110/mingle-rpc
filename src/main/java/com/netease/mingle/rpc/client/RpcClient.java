package com.netease.mingle.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.oio.OioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rpc Client
 * Created by Michael Jiang on 2016/12/2.
 */
public class RpcClient {
    private Map<Class,ServiceAddress> serviceAddressMap = new ConcurrentHashMap<>(8);
    private Map<ServiceAddress,Boolean> serviceAddressInitializeMap = new ConcurrentHashMap<>(8);

    public void proxy(Class serviceInterface,String serviceAddress) {

    }

    private static Bootstrap bootstrap=new Bootstrap();

    static {
        bootstrap.group(new OioEventLoopGroup())
                .channel(OioSocketChannel.class)
                .handler(new ClientHandler());
    }

    private void initializeServiceAddress(ServiceAddress serviceAddress) {
        synchronized (serviceAddress) {

        }
    }

}
