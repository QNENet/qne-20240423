package com.qnenet.qne.objects.classes;

//import com.qnenet.qne.utils.QNetworkUtils;

import com.qnenet.qne.system.utils.QNetworkUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class QInetAddr {

    private byte[] ipAddressBytes;
//    private int port;

    public QInetAddr() {
        // for Kryo
    }

    public QInetAddr(InetAddress inetAddress) {

    }

    public QInetAddr(byte[] ipAddressBytes) {
        this.ipAddressBytes = ipAddressBytes;
    }

    public QInetAddr(String ipAddress) {
        this.ipAddressBytes = QNetworkUtils.ipAddressToBytes(ipAddress);
    }

    public InetAddress getInetAddr() {
        try {
            return InetAddress.getByAddress(ipAddressBytes);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    public byte[] getIPAddrBytes() {
        return ipAddressBytes;
    }


}
