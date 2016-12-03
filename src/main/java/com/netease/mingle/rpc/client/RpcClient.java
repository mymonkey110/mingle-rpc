package com.netease.mingle.rpc.client;

import com.netease.mingle.rpc.shared.InnerLoggerFactory;
import com.netease.mingle.rpc.shared.MethodInvocation;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Rpc Client
 * Created by Michael Jiang on 2016/12/2.
 */
public class RpcClient {
    private Map<Class, ServiceAddress> serviceAddressMap = new ConcurrentHashMap<>(8);
    private Map<ServiceAddress, Boolean> serviceAddressInitializeMap = new ConcurrentHashMap<>(8);
    private static Logger logger = InnerLoggerFactory.getLogger(RpcClient.class.toString());

    @SuppressWarnings("unchecked")
    public synchronized <T> T proxy(Class<T> serviceInterface, String address) {
        if (!serviceAddressMap.containsKey(serviceInterface)) {
            ServiceAddress serviceAddress = ServiceAddress.parse(address);
            serviceAddressMap.put(serviceInterface, serviceAddress);
            initializeServiceAddress(serviceAddress);
            return (T) new RpcProxy(serviceInterface).getProxyObject();
        } else {
            throw new IllegalArgumentException("already proxy service:" + serviceInterface.getName());
        }
    }

    private static Bootstrap bootstrap = new Bootstrap();

    static {
        bootstrap.group(new OioEventLoopGroup())
                .channel(OioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                        pipeline.addLast(new ClientHandler());
                        pipeline.addLast(new ObjectEncoder());
                    }
                });
    }

    private synchronized void initializeServiceAddress(final ServiceAddress serviceAddress) {
        if (!serviceAddressInitializeMap.get(serviceAddress)) {
            ChannelFuture channelFuture = bootstrap.remoteAddress(serviceAddress.toInetSocketAddress()).connect();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        logger.info("connected to service host:" + serviceAddress);
                        serviceAddressInitializeMap.put(serviceAddress, true);
                    } else {
                        logger.warning("can't connected to service host:" + serviceAddress);
                    }
                }
            });
        }
    }

    class RpcProxy<T> implements InvocationHandler {
        private Class<T> service;

        RpcProxy(Class<T> service) {
            if (!service.isInterface()) {
                throw new IllegalArgumentException("mingle can only proxy interface now");
            }
            this.service = service;
        }

        @SuppressWarnings("unchecked")
        T getProxyObject() {
            return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{service}, this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodInvocation methodInvocation = MethodInvocation.from(method);

            return null;
        }
    }

}
