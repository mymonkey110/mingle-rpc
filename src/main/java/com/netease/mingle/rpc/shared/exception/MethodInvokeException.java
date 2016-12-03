package com.netease.mingle.rpc.shared.exception;

/**
 * Created by Michael Jiang on 2016/12/3.
 */
public class MethodInvokeException extends RpcException {
    public MethodInvokeException() {
        super(ErrorCode.METHOD_INVOKE_ERROR);
    }
}
