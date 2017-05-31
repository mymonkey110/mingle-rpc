package com.netease.mingle.rpc.client;

import com.google.common.collect.Sets;
import com.netease.mingle.rpc.shared.ServiceCheck;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rpc Client Created by Michael Jiang on 2016/12/2.
 */
public class RpcClient {
    private static Logger logger = LoggerFactory.getLogger(RpcClient.class);
    private static RpcClient instance = new RpcClient();
    private Bootstrap bootstrap = new Bootstrap().group(new NioEventLoopGroup());
    private Map<Class, ServiceAddress> serviceAddressMap = new ConcurrentHashMap<>(8);
    private Map<ServiceAddress, Channel> serviceAddressChannelMap = new ConcurrentHashMap<>(8);
    private List<Class> needStartUpCheckService = new ArrayList<>();

    private RpcClient() {
    }

    public static RpcClient getInstance() {
        return instance;
    }

    public synchronized <T> T refer(Class<T> serviceInterface, String address) {
        return this.refer(serviceInterface, address, false);
    }

    /**
     * 引用远程接口申明
     *
     * @param serviceInterface
     *            服务接口
     * @param address
     *            远程服务地址
     * @param startUpCheck
     *            启动检查，默认false
     * @param <T>
     *            接口类型
     * @return 代理过的接口
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> T refer(Class<T> serviceInterface, String address, boolean startUpCheck) {
        if (!serviceAddressMap.containsKey(serviceInterface)) {
            ServiceAddress serviceAddress = ServiceAddress.parse(address);
            serviceAddressMap.put(serviceInterface, serviceAddress);
            if (startUpCheck) {
                needStartUpCheckService.add(serviceInterface);
            }
            return (T) new RpcProxy(serviceInterface, serviceAddress).getProxyObject();
        } else {
            throw new IllegalArgumentException("already refer service:" + serviceInterface.getName());
        }
    }

    public void init() {
        Set<ServiceAddress> serviceAddresses = Sets.newHashSet(serviceAddressMap.values());
        for (final ServiceAddress serviceAddress : serviceAddresses) {
            bootstrap.channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                    pipeline.addLast(new ClientHandler());
                    pipeline.addLast(new ObjectEncoder());
                }
            }).connect(serviceAddress.toInetSocketAddress()).syncUninterruptibly()
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            logger.info("mingle client connected to {}.", serviceAddress);
                            serviceAddressChannelMap.put(serviceAddress, future.channel());

                            if (!needStartUpCheckService.isEmpty()) {
                                for (Class needCheckService : needStartUpCheckService) {
                                    ServiceAddress serviceAddress1 = serviceAddressMap.get(needCheckService);
                                    Channel channel = serviceAddressChannelMap.get(serviceAddress1);
                                    ServiceCheck serviceCheck = ServiceCheck.fromClass(needCheckService);
                                    channel.writeAndFlush(serviceCheck);
                                }
                            }
                        } else {
                            logger.error("mingle client connected to {} failed.", serviceAddress);
                        }
                    });

        }
    }

    Channel getServiceAddressBindChannel(ServiceAddress serviceAddress) {
        return serviceAddressChannelMap.get(serviceAddress);
    }

}
