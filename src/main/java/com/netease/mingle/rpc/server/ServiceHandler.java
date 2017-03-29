package com.netease.mingle.rpc.server;

import com.netease.mingle.rpc.shared.RpcRequest;
import com.netease.mingle.rpc.shared.RpcResponse;
import com.netease.mingle.rpc.shared.ServiceCheck;
import com.netease.mingle.rpc.shared.exception.SystemException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service Message Handler Created by Michael Jiang on 2016/11/27.
 */
public class ServiceHandler extends ChannelInboundHandlerAdapter {
    private static int factor = 2;
    private static ExecutorService executorService = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors() * factor);
    private static Logger logger = LoggerFactory.getLogger(ServiceHandler.class);
    private static ServiceRegister serviceRegister = ServiceRegister.getInstance();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcRequest) {
            logger.debug("recv rpc request:{}.", msg);
            RpcRequest rpcRequest = (RpcRequest) msg;
            ServiceRunner runner = new ServiceRunner(rpcRequest, ctx.channel());
            executorService.submit(runner);
        } else if (msg instanceof ServiceCheck) {
            ServiceCheck serviceCheck = (ServiceCheck) msg;
            boolean exist = serviceRegister.isServiceRegistered(serviceCheck);
            serviceCheck.setExist(exist);
            ctx.writeAndFlush(serviceCheck);
        } else {
            logger.warn("rpc call protocol not qualified");
            ctx.writeAndFlush(new SystemException());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    class ServiceRunner implements Runnable {
        private RpcRequest rpcRequest;
        private Channel channel;

        ServiceRunner(RpcRequest rpcRequest, Channel channel) {
            this.rpcRequest = rpcRequest;
            this.channel = channel;
        }

        @Override
        public void run() {
            ServiceInvoker invoker = ServiceInvoker.getServiceInvoker(rpcRequest);
            Object retObject = invoker.invoke();
            logger.debug("return value:{}.", retObject);
            RpcResponse rpcResponse = rpcRequest.fromReturn(retObject);
            channel.writeAndFlush(rpcResponse);
        }
    }
}
