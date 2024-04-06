package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objectsQNE.classes;
//
//import com.qnenet.qne.utils.QNetworkUtils;
//
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//import java.net.UnknownHostException;
//import java.util.Arrays;
//
//public class QEndPointInfo {
//
//    //    if port is negative endPointAddress is to be removed (used in update map)
//    public QEPId qepId;               //   8
//    public byte[] wanIPAddressBytes;      //  16 or 4
//    public byte[] lanIPAddressBytes;      //   4
//    public int port;                      //   4
//    public short endPointIdx;               //   4
//    public byte[] publicKeyBytes;         //  32
//    //                                        86 or 59 bytes
////    public String endPointName;           //  18 max
//
//    public transient QEndPointInfo endPoint;
//    public transient boolean forceDestWanIPAddress;
//
//    public QEndPointInfo() {
//    }
//
//
//    public QEPId getQepId() {
//        return qepId;
//    }
//
//    public void setQepId(QEPId qepId) {
//        this.qepId = qepId;
//    }
//
///////////// Wan ///////////////////////////////////////////////////////////////////////////////////
//
//    public void setWanIPAddress(String wanIpAddressStr) {
//        wanIPAddressBytes = QNetworkUtils.ipAddressToBytes(wanIpAddressStr);
//    }
//
//    public String getWanIPAddress() {
//        return QNetworkUtils.ipAddressFromBytes(wanIPAddressBytes);
//    }
//
//    public void setWanIPAddressBytes(byte[] wanIPAddressBytes) {
//        this.wanIPAddressBytes = wanIPAddressBytes;
//    }
//
//    public byte[] getWanIPAddressBytes() {
//        return wanIPAddressBytes;
//    }
//
///////////// Lan ///////////////////////////////////////////////////////////////////////////////////
//
//    public void setLanIPAddress(String lanIPAddress) {
//        this.lanIPAddressBytes = QNetworkUtils.ipAddressToBytes(lanIPAddress);
//    }
//
//    public String getLanIPAddress() {
//        return QNetworkUtils.ipAddressFromBytes(lanIPAddressBytes);
//    }
//
//    public void setLanIPAddressBytes(byte[] lanIPAddressBytes) {
//        this.lanIPAddressBytes = lanIPAddressBytes;
//    }
//
//    public byte[] getLanIPAddressBytes() {
//        return lanIPAddressBytes;
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public int getPort() {
//        return port;
//    }
//
//    public void setPort(int port) {
//        this.port = port;
//    }
//
//    public int getEndPointIdx() {
//        return endPointIdx;
//    }
//
//    public void setEndPointIdx(short endPointIdx) {
//        this.endPointIdx = endPointIdx;
//    }
//
//    public void setPublicKeyBytes(byte[] bytes) {
//        this.publicKeyBytes = bytes;
//    }
//
//    public SocketAddress getSocketAddress() {
//        if (forceDestWanIPAddress) {
//            return new InetSocketAddress(getWanIPAddress(), port);
//        }
//        return new InetSocketAddress(getLanIPAddress(), port);
//    }
//
//    public QEPAddr getWanEPAddr() throws UnknownHostException {
//        InetSocketAddress wanSocketAddress = new InetSocketAddress(InetAddress.getByAddress(wanIPAddressBytes), port);
//        return new QEPAddr(wanSocketAddress, endPointIdx);
//    }
//
//    public QEPAddr getLanEPAddr() throws UnknownHostException {
//        InetSocketAddress lanSocketAddress = new InetSocketAddress(InetAddress.getByAddress(lanIPAddressBytes), port);
//        return new QEPAddr(lanSocketAddress, endPointIdx);
//    }
//
//
//    @Override
//    public boolean equals(Object obj) {
//        QEndPointInfo epa = (QEndPointInfo) obj;
//        if (this == epa) return true;
//        if (epa == null || getClass() != epa.getClass()) return false;
//        if (qepId == epa.qepId &&
//                port == epa.port &&
//                endPointIdx == epa.endPointIdx &&
//                Arrays.equals(wanIPAddressBytes, epa.wanIPAddressBytes) &&
//                Arrays.equals(lanIPAddressBytes, epa.lanIPAddressBytes) &&
//                Arrays.equals(publicKeyBytes, epa.publicKeyBytes)) return true;
//        return false;
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} ////////////// End Class ////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
