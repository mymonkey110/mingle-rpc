package com.netease.mingle.rpc.client;

import com.netease.mingle.rpc.shared.InnerLoggerFactory;
import com.netease.mingle.rpc.shared.RpcRequest;
import com.netease.mingle.rpc.shared.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Client Handler
 * Created by Michael Jiang on 2016/12/3.
 */
public class ClientHandler extends SimpleChannelInboundHandler {

    private Class serviceInterface;
    private ServiceAddress serviceAddress;
    private Map<String, ServiceCallContext> callContextMap = new ConcurrentHashMap<>(8);

    private static Logger logger = InnerLoggerFactory.getLogger(ClientHandler.class.toString());

    public ClientHandler(Class serviceInterface, ServiceAddress serviceAddress) {
        super();
        this.serviceInterface = serviceInterface;
        this.serviceAddress = serviceAddress;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcResponse) {
            RpcResponse response = (RpcResponse) msg;
            ServiceCallContext callContext = callContextMap.get(response.getRequestId());
            if (callContext != null) {
                callContext.setResponse(response);
                callContextMap.remove(response.getRequestId());
            } else {
                logger.warning("request id:" + response.getRequestId() + " is not recorded.");
            }
        }
        logger.warning("recv msg is not protocol of rpc response");
    }

    public ServiceCallContext sendRequest(RpcRequest request) {
        ServiceCallContext serviceCallContext = new ServiceCallContext(request);
        if (!callContextMap.containsKey(request.getRequestId())) {
            callContextMap.put(request.getRequestId(), serviceCallContext);
        } else {
            logger.warning("request id already recorded");
        }
        return serviceCallContext;
    }

    public Class getServiceInterface() {
        return serviceInterface;
    }

    public ServiceAddress getServiceAddress() {
        return serviceAddress;
    }
}
