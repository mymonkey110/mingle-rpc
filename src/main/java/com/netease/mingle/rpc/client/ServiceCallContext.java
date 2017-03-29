package com.netease.mingle.rpc.client;

import com.netease.mingle.rpc.shared.RpcRequest;
import com.netease.mingle.rpc.shared.RpcResponse;
import com.netease.mingle.rpc.shared.exception.RpcException;
import com.netease.mingle.rpc.shared.exception.WaitTimeoutException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Service Call Context Created by Michael Jiang on 2016/12/4.
 */
public class ServiceCallContext {
    private RpcRequest request;
    private RpcResponse response;
    private Lock locker = new ReentrantLock();
    private Condition done = locker.newCondition();

    public ServiceCallContext(RpcRequest request) {
        this.request = request;
    }

    public RpcResponse get() throws RpcException, InterruptedException {
        try {
            locker.lock();
            done.await(10, TimeUnit.SECONDS);
            if (response != null) {
                return response;
            } else {
                throw new WaitTimeoutException();
            }
        } finally {
            locker.unlock();
        }
    }

    public void setResponse(RpcResponse response) {
        try {
            locker.lock();
            this.response = response;
            done.signal();
        } finally {
            locker.unlock();
        }

    }

    public RpcRequest getRequest() {
        return request;
    }
}
