package com.netease.mingle.rpc.demo;

import com.netease.mingle.rpc.server.RpcServer;
import com.netease.mingle.rpc.server.ServiceRegister;

/**
 * Rpc Provider
 * Created by jiangwenkang on 2017/1/4.
 */
public class RpcProvider {
    public static void main(String[] args) {
        ServiceRegister serviceRegister = ServiceRegister.getInstance();
        HelloService helloService = new HelloServiceImpl();
        serviceRegister.register(HelloService.class,helloService);
        RpcServer rpcServer = new RpcServer(9999);
        rpcServer.start();
    }
}
