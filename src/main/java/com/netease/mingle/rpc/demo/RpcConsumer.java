package com.netease.mingle.rpc.demo;

import com.netease.mingle.rpc.client.RpcClient;

/**
 * Rpc Consumer Created by Michael Jiang on 16-12-1.
 */
public class RpcConsumer {

    public static void main(String[] args) {
        RpcClient rpcClient = RpcClient.getInstance();
        HelloService helloService = rpcClient.refer(HelloService.class, "localhost:9999", true);
        rpcClient.init();
        String replay = helloService.sayHi("michael");
        System.out.println(replay);
/*
        try {
            helloService.exceptionTest();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }*/

    }
}
