package com.qnenet.qne.objects.classes;

//import com.qnenet.qne.utils.QNetworkUtils;

import com.qnenet.qne.system.utils.QNetworkUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class QEPAddr {

    public QEPId epId; // 8
    public byte[] ipAddressBytes; // 16
    public int port; // 4
    public short epIdx; // 4

    public QEPAddr() {
        // for Kryo
    }

    public QEPAddr(InetSocketAddress socketAddress, int epIdx) {

    }

    public QEPAddr(QEPId epId, byte[] ipAddressBytes, int port, short epIdx) {
        this.epId = epId;
        this.ipAddressBytes = ipAddressBytes;
        this.port = port;
        this.epIdx = epIdx;
    }

    public QEPAddr(String ipAddress, int port) {
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

    public QEPId getEPId() {
        return epId;
    }

    public int getEPIdx() {
        return epIdx;
    }

    public byte[] getIPAddrBytes() {
        return ipAddressBytes;
    }

    public int getPort() {
        return port;
    }

    public void setEPIdx(short epIdx) {
        this.epIdx = epIdx;
    }
}
