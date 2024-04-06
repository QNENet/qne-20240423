package com.qnenet.qne.objects.classes;

//import com.qnenet.qne.utils.QNetworkUtils;

import com.qnenet.qne.system.utils.QNetworkUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class QInetSocketAddr {

    private byte[] ipAddressBytes;
    private int port;

    public QInetSocketAddr() {
        // for Kryo
    }

    public QInetSocketAddr(InetSocketAddress socketAddress) {

    }

    public QInetSocketAddr(byte[] ipAddressBytes, int port) {
        this.ipAddressBytes = ipAddressBytes;
        this.port = port;
    }

    public QInetSocketAddr(String ipAddress, int port) {
        this.ipAddressBytes = QNetworkUtils.ipAddressToBytes(ipAddress);
        this.port = port;
    }

    public InetSocketAddress getSocketAddr() {
        try {
            return new InetSocketAddress(InetAddress.getByAddress(ipAddressBytes), port);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    public byte[] getIPAddrBytes() {
        return ipAddressBytes;
    }

    public int getPort() {
        return port;
    }

}
