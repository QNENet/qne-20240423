package com.qnenet.qne.objects.classes;

//import com.qnenet.qne.utils.QNetworkUtils;

import com.qnenet.qne.system.utils.QNetworkUtils;

public class QEPAddrPair {

    public QEPId epId;
    public byte[] wanIPAddrBytes;
    public byte[] lanIPAddrBytes;
    public int port;
    public short epIdx;


    public QEPAddrPair() {
        // for Kryo
    }

    public QEPAddrPair(QEPAddr wanEPAddr, QEPAddr lanEPAddr) {
        this.epId = wanEPAddr.epId;
        this.wanIPAddrBytes = wanEPAddr.getIPAddrBytes();
        if (lanEPAddr != null) this.lanIPAddrBytes = lanEPAddr.getIPAddrBytes();
        this.port = wanEPAddr.getPort();
        this.epIdx = wanEPAddr.epIdx;
    }

    public QEPAddrPair(String wanIPAddress, String lanIPAddress, int port) {
        this.epId = null;
        this.wanIPAddrBytes = QNetworkUtils.ipAddressToBytes(wanIPAddress);
        this.lanIPAddrBytes = QNetworkUtils.ipAddressToBytes(lanIPAddress);
        this.port = port;
//        this.epIdx = -1;
    }

    public QEPAddr getWanEPAddr() {
        return new QEPAddr(epId, wanIPAddrBytes, port, epIdx);
    }

    public QEPAddr getLanEPAddr() {
        return new QEPAddr(epId, lanIPAddrBytes, port, epIdx);
    }

    public void setWanEPAddr(QEPAddr wanEPAddr) {
        this.epId = wanEPAddr.epId;
        this.wanIPAddrBytes = wanEPAddr.getIPAddrBytes();
        this.port = wanEPAddr.getPort();
        this.epIdx = wanEPAddr.epIdx;
    }

    public void setLanEPAddr(QEPAddr lanEPAddr) {
        this.epId = lanEPAddr.epId;
        this.wanIPAddrBytes = lanEPAddr.getIPAddrBytes();
        this.port = lanEPAddr.getPort();
        this.epIdx = lanEPAddr.epIdx;
    }


    public String getWanIPAddress() {
       return QNetworkUtils.ipAddressFromBytes(wanIPAddrBytes);
    }
    
    public String getLanIPAddress() {
        if (lanIPAddrBytes == null) return null;
       return QNetworkUtils.ipAddressFromBytes(lanIPAddrBytes);
    }
    
    public QEPId getEpId() {
        return epId;
    }

    public void setEpId(QEPId epId) {
        this.epId = epId;
    }

    public void setEPIdx(short epIdx) {
        this.epIdx = epIdx;
    }
    
}