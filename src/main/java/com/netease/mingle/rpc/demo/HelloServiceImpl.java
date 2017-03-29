package com.netease.mingle.rpc.demo;

/**
 * hello service demo implementation Created by Michael Jiang on 16-12-1.
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHi(String name) {
        return "hi," + name;
    }

    @Override
    public void exceptionTest() {
        throw new RuntimeException("user customer throw");
    }
}
