package com.netease.mingle.rpc.shared;

import com.netease.mingle.rpc.shared.exception.RpcException;

import java.io.Serializable;

/**
 * Rpc Response
 * Created by Michael Jiang on 2016/12/4.
 */
public class RpcResponse implements Serializable{
    private static final long serialVersionUID = 6992447826073365028L;

    private String requestId;
    private Object normalResult;
    private Throwable exceptionResult;
    private RpcException rpcException;

    private RpcResponse(String requestId, Object normalResult) {
        this.requestId = requestId;
        this.normalResult = normalResult;
    }

    private RpcResponse(String requestId, Throwable userException) {
        this.requestId = requestId;
        this.exceptionResult = userException;
    }

    private RpcResponse(String requestId, RpcException rpcException) {
        this.requestId = requestId;
        this.rpcException = rpcException;
    }

    public static RpcResponse normalReturn(String requestId, Object normalResult) {
        return new RpcResponse(requestId, normalResult);
    }

    public static RpcResponse userThrowException(String requestId, Throwable userException) {
        return new RpcResponse(requestId, userException);
    }

    public static RpcResponse innerException(String requestId, RpcException rpcException) {
        return new RpcResponse(requestId, rpcException);
    }

    public boolean isNormalReturn() {
        return normalResult != null;
    }

    public String getRequestId() {
        return requestId;
    }

    public Object getNormalResult() {
        return normalResult;
    }

    public Throwable getExceptionResult() {
        return exceptionResult;
    }

    public RpcException getRpcException() {
        return rpcException;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", normalResult=" + normalResult +
                ", exceptionResult=" + exceptionResult +
                ", rpcException=" + rpcException +
                '}';
    }
}
