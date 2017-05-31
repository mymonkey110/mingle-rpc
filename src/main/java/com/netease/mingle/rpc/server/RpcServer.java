package com.netease.mingle.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RPC Server
 *
 * Created by Michael Jiang on 2016/11/27.
 */
public class RpcServer {
    private static final int BACKLOG_NUM = 128;
    private static Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private int servicePort;

    public RpcServer(int servicePort) {
        this.servicePort = servicePort;
    }

    public void start() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(servicePort)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(
                                new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                        pipeline.addLast(new ServiceHandler());
                        pipeline.addLast(new ObjectEncoder());
                    }
                }).option(ChannelOption.SO_BACKLOG, BACKLOG_NUM).childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            ChannelFuture future = bootstrap.bind().sync();
            logger.info("Rpc Server is started and listening on port:{}.", servicePort);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
