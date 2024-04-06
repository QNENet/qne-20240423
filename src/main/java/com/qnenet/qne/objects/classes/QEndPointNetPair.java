package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objects.classes;
//
//import com.qnenet.qne.endpoint.QEndPointInfo;
//import com.qnenet.qne.intf.objectsQNE.QNetMsg;
//
//import java.util.Arrays;
//
//public class QEndPointNetPair {
//
//    public QEndPointInfo srcEndPoint;
//    public QEndPointInfo destEndPoint;
//
//    public QEndPointNetPair() {
//    }
//
//    public QEndPointNetPair(QEndPointInfo srcEP, QEndPointInfo destEP, QNetMsg netMsgs) {
//        this.srcEndPoint = srcEP.getEndPointNet();
//        this.destEndPoint = destEP.getEndPointNet();
//
//        if (!Arrays.equals(srcEndPoint.wanIPAddressBytes, destEndPoint.wanIPAddressBytes)) { // same lan
//            srcEndPoint.useDestWanIPAddress = true;
//        }
//    }
//
//    public QEndPointNetPair(QEndPointInfo srcEndPoint, QEndPointInfo destEndPoint) {
//        this.srcEndPoint = srcEndPoint;
//        this.destEndPoint = destEndPoint;
//    }
//
//
//    public QEndPointInfo getSrcEndPoint() {
//        return srcEndPoint;
//    }
//
//    public void setSrcEndPoint(QEndPointInfo srcEndPoint) {
//        this.srcEndPoint = srcEndPoint;
//    }
//
//    public QEndPointInfo getDestEndPoint() {
//        return destEndPoint;
//    }
//
//    public void setDestEndPoint(QEndPointInfo destEndPoint) {
//        this.destEndPoint = destEndPoint;
//    }
//
////    boolean isOnLan() {
////        return srcEndPoint.wanIPAddress.equals(destEndPoint.wanIPAddress);
////    }
////
////
////    boolean isOnDevice() {
////        return srcEndPoint.lanIPAddress.equals(destEndPoint.lanIPAddress);
////    }
//
//
//}
