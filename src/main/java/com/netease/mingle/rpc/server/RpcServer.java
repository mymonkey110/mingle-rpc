package com.netease.mingle.rpc.server;

import com.netease.mingle.rpc.shared.HttpChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Michael Jiang on 2016/11/27.
 */
public class RpcServer {
    private ServiceRegister serviceRegister;
    private int servicePort;

    public RpcServer(ServiceRegister serviceRegister, int servicePort) {
        this.serviceRegister = serviceRegister;
        this.servicePort = servicePort;
    }

    public void start() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group).channel(NioServerSocketChannel.class)
                .localAddress(servicePort)
                .childHandler(new HttpChannelInitializer(false))
                .childHandler(new ServiceHandler())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println("Rpc Server is started and listening on port:" + servicePort);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
}
