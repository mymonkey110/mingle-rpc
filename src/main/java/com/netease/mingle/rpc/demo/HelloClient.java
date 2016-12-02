package com.netease.mingle.rpc.demo;

import com.caucho.hessian.client.HessianProxyFactory;

import java.net.MalformedURLException;

/**
 * Created by Michael Jiang on 16-12-1.
 */
public class HelloClient {


    public static void main(String[] args) throws MalformedURLException {
        HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();
        hessianProxyFactory.create(HelloService.class,"http://localhost:")
    }
}
