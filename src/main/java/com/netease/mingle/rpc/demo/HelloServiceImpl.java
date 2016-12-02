package com.netease.mingle.rpc.demo;

/**
 * Created by Michael Jiang on 16-12-1.
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHi(String name) {
        return "hi," + name;
    }
}
