package com.netease.mingle.rpc.client;

import java.net.InetSocketAddress;

/**
 * Mingle Service Address, like InetAddress
 * Created by Michael Jiang on 2016/12/3.
 */
public class ServiceAddress {
    private String ip;
    private int port = 80;

    private ServiceAddress(String ip, int port) {
        if (!isValidPort(port)) {
            throw new IllegalArgumentException("port:"+port+" is illegal");
        }
        this.ip=ip;
        this.port = port;
    }

    private static boolean isValidPort(int port) {
        return port > 0 && port <= 65535;
    }

    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(ip,port);
    }
}
