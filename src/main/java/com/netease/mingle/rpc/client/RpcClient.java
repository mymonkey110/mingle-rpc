package com.netease.mingle.rpc.client;

import com.google.common.collect.Sets;
import com.netease.mingle.rpc.shared.InnerLoggerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Rpc Client
 * Created by Michael Jiang on 2016/12/2.
 */
public class RpcClient {
    private Bootstrap bootstrap = new Bootstrap().group(new NioEventLoopGroup());
    private Map<Class, ServiceAddress> serviceAddressMap = new ConcurrentHashMap<Class, ServiceAddress>(8);
    private Map<ServiceAddress, Channel> serviceAddressChannelMap = new ConcurrentHashMap<>(8);
    private static Logger logger = LoggerFactory.getLogger(RpcClient.class.toString());

    private static RpcClient instance = new RpcClient();

    private RpcClient() {
    }

    public static RpcClient getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T refer(Class<T> serviceInterface, String address) {
        if (!serviceAddressMap.containsKey(serviceInterface)) {
            ServiceAddress serviceAddress = ServiceAddress.parse(address);
            serviceAddressMap.put(serviceInterface, serviceAddress);
            return (T) new RpcProxy(serviceInterface, serviceAddress).getProxyObject();
        } else {
            throw new IllegalArgumentException("already refer service:" + serviceInterface.getName());
        }
    }

    public void init() {
        Set<ServiceAddress> serviceAddresses = Sets.newHashSet(serviceAddressMap.values());
        for (final ServiceAddress serviceAddress : serviceAddresses) {
            bootstrap.channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                            pipeline.addLast(new ClientHandler());
                            pipeline.addLast(new ObjectEncoder());
                        }
                    })
                    .connect(serviceAddress.toInetSocketAddress()).syncUninterruptibly().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        logger.info("mingle client connected to {}.", serviceAddress);
                        serviceAddressChannelMap.put(serviceAddress, future.channel());
                    } else {
                        logger.error("mingle client connected to {} failed.", serviceAddress);
                    }
                }
            });

        }
    }

    public Channel getServiceAddressBindChannel(ServiceAddress serviceAddress) {
        return serviceAddressChannelMap.get(serviceAddress);
    }

}
