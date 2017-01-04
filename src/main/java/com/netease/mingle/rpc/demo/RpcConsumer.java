package com.netease.mingle.rpc.demo;

import com.netease.mingle.rpc.client.RpcClient;

/**
 * Rpc Consumer
 * Created by Michael Jiang on 16-12-1.
 */
public class RpcConsumer {

    public static void main(String[] args) {
        HelloService helloService = RpcClient.proxy(HelloService.class, "localhost:8181");
        System.out.println(helloService.sayHi("michael"));
    }
}
