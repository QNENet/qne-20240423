package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objectsQNE.classes;
//
//
//import com.qnenet.qne.objectsQNE.intf.QChannel;
//import com.qnenet.qne.objectsQNE.intf.QNetMsg;
//
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//
//public class QChannelMsg {
//
//    public int roundTripId;
//    public int initiatorEndpointIdx;
//    public int responderEndpointIdx;
//    public int role;
//    public byte[] bytes;
//
////    public QEndPointInfo initiatorEndPointAddress;
////    public QEndPointInfo responderEndPointAddress;
//
//
////    public boolean keepAlive;
//
//    public transient QNetMsg netMsg;
//    public transient QChannel channel;
////    public transient boolean isSend;
//
//    public SocketAddress getSocketAddress(QEndPointInfo endPointAddress) {
//        if (endPointAddress.useDestWanIPAddress) {
//            return new InetSocketAddress(endPointAddress.getWanIPAddress(), endPointAddress.getPort());
//        }
//        return new InetSocketAddress(endPointAddress.getLanIPAddress(), endPointAddress.getPort());
//    }
//}
