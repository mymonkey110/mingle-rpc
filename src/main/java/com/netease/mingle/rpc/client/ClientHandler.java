package com.netease.mingle.rpc.client;

import com.netease.mingle.rpc.shared.RpcRequest;
import com.netease.mingle.rpc.shared.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Client Handler
 * Created by Michael Jiang on 2016/12/3.
 */
public class ClientHandler extends SimpleChannelInboundHandler {

    private static Map<String, ServiceCallContext> callContextMap = new ConcurrentHashMap<String, ServiceCallContext>(8);

    private static Logger logger = LoggerFactory.getLogger(ClientHandler.class.toString());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcResponse) {
            RpcResponse response = (RpcResponse) msg;
            ServiceCallContext callContext = callContextMap.get(response.getRequestId());
            if (callContext != null) {
                callContext.setResponse(response);
                callContextMap.remove(response.getRequestId());
            } else {
                logger.warn("request id:" + response.getRequestId() + " is not recorded.");
            }
        }else {
            logger.warn("recv msg is not protocol of rpc response");
        }
    }

    public ServiceCallContext sendRequest(RpcRequest request) {
        ServiceCallContext serviceCallContext = new ServiceCallContext(request);
        if (!callContextMap.containsKey(request.getRequestId())) {
            logger.info("register request:{}.",request);
            callContextMap.put(request.getRequestId(), serviceCallContext);
        } else {
            logger.warn("request id already recorded");
        }
        return serviceCallContext;
    }
}
