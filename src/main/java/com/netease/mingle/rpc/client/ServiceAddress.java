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
            throw new IllegalArgumentException("port:" + port + " is illegal");
        }
        this.ip = ip;
        this.port = port;
    }

    public static ServiceAddress parse(String address) {
        String[] ipAndPort = address.split(":");
        if (ipAndPort.length == 2) {
            return new ServiceAddress(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
        } else if (ipAndPort.length == 1) {
            return new ServiceAddress(ipAndPort[0], 80);
        } else {
            throw new IllegalArgumentException("address:" + address + " is illegal");
        }
    }

    private static boolean isValidPort(int port) {
        return port > 0 && port <= 65535;
    }

    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(ip, port);
    }

    @Override
    public String toString() {
        return "ServiceAddress{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
