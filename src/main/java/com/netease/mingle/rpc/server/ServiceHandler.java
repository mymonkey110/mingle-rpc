package com.netease.mingle.rpc.server;

import com.netease.mingle.rpc.shared.RpcRequest;
import com.netease.mingle.rpc.shared.exception.SystemException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Service Message Handler
 * Created by Michael Jiang on 2016/11/27.
 */
public class ServiceHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ServiceHandler.class.toString());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcRequest) {
            logger.info("recv rpc request:{}.", msg);
            RpcRequest methodInvocation = (RpcRequest) msg;
            ServiceInvoker serviceInvoker = ServiceInvoker.getServiceInvoker(methodInvocation);
            Object retObject = serviceInvoker.invoke();
            logger.info("return object:{}.", retObject);
            //FIXME: wrap into RpcResponse
            ctx.channel().writeAndFlush("hi,michael");
        } else {
            logger.warn("rpc call protocol not qualified");
            ctx.writeAndFlush(new SystemException());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
