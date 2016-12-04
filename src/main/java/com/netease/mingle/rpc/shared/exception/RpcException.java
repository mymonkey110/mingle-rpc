package com.netease.mingle.rpc.shared.exception;

/**
 * Rpc Call Exception
 * Created by Michael Jiang on 2016/12/3.
 */
public class RpcException extends RuntimeException {

    private static final long serialVersionUID = -7133274500160548484L;

    private final ErrorCode errorCode;

    protected RpcException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public int getCode() {
        return errorCode.getCode();
    }

    public String getMsg() {
        return errorCode.getMsg();
    }

    enum ErrorCode {
        SYS_ERROR(1001,"system error"),

        NETWORK_ERROR(1002,"network error"),

        METHOD_NOT_FOUND(1003,"method not found"),

        METHOD_INVOKE_ERROR(1004,"method invoke error"),

        WAIT_TIMEOUT(1005,"method call wait timeout");

        private int code;
        private String msg;

        ErrorCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
