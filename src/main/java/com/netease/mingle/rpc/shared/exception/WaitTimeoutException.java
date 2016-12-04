package com.netease.mingle.rpc.shared.exception;

/**
 * Created by Michael Jiang on 2016/12/4.
 */
public class WaitTimeoutException extends RpcException {
    private static final long serialVersionUID = 3789309302864328384L;

    public WaitTimeoutException() {
        super(ErrorCode.WAIT_TIMEOUT);
    }
}
