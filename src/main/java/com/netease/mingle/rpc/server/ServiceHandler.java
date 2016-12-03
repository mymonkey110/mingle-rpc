package com.netease.mingle.rpc.server;

import com.netease.mingle.rpc.shared.InnerLoggerFactory;
import com.netease.mingle.rpc.shared.MethodInvocation;
import com.netease.mingle.rpc.shared.exception.SystemException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;


/**
 * Service Message Handler
 * Created by Michael Jiang on 2016/11/27.
 */
public class ServiceHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = InnerLoggerFactory.getLogger(ServiceHandler.class.toString());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MethodInvocation) {
            MethodInvocation methodInvocation = (MethodInvocation) msg;
            ServiceInvoker serviceInvoker = ServiceInvoker.getServiceInvoker(methodInvocation);
            Object retObject = serviceInvoker.invoke();
            ctx.writeAndFlush(retObject);
        }
        logger.warning("rpc call protocol not qualified");
        ctx.writeAndFlush(new SystemException());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
