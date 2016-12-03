package com.netease.mingle.rpc.shared.exception;

/**
 * System inner error
 * Created by Michael Jiang on 2016/12/3.
 */
public class SystemException extends RpcException {
    private static final long serialVersionUID = 7846083151830519118L;

    public SystemException() {
        super(ErrorCode.SYS_ERROR);
    }
}
