package com.netease.mingle.rpc.shared.exception;

/**
 * Created by Michael Jiang on 2016/12/3.
 */
public class MethodNotFoundException extends RpcException {
    private static final long serialVersionUID = -7265060407885343361L;

    public MethodNotFoundException() {
        super(ErrorCode.METHOD_NOT_FOUND);
    }
}
