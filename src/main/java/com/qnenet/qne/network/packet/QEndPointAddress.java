package com.qnenet.qne.network.packet;

import java.net.InetSocketAddress;

public class QEndPointAddress {
    public InetSocketAddress socketAddress;
    public int epIdx;

    public QEndPointAddress() {
        // for kryo
    }

    public QEndPointAddress(String ipAddr, int port, int epIdx) {
        this.socketAddress = new InetSocketAddress(ipAddr, port);
        this.epIdx = epIdx;
    }


}
