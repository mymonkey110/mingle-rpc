package com.netease.mingle.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Client Handler
 * Created by Michael Jiang on 2016/12/3.
 */
public class ClientHandler extends SimpleChannelInboundHandler {

    private Map<String,ServiceCallContext> callContextMap = new ConcurrentHashMap<>(8);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
