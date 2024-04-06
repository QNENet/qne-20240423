package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objects.classes;
//
//import com.qnenet.qne.system.impl.QSystem;
//
//import java.util.ArrayList;
//import java.util.UUID;
//
//public class QNetEndPointInfo {
//
//    private QSystem system;
//
//    public UUID uuid;
//    public int segmentId;
//    public String name;
//    public QNetAddressPair netAddressPair;
//    public QNetAddress useNetAddress; // if use net address == null then same node
//    public int installIdx; // the idx used to find endpoint installation
//    public ArrayList<Integer> statusList;
//
//    public QNetEndPointInfo() {
//    }
//
//    public QNetEndPointInfo(QSystem system) {
//        this.system = system;
//        uuid = system.getUUID();
//        name = system.getName();
//        netAddressPair = system.getEndPointPair();
//        installIdx = -1; // the idx used to find endpoint installation
//        statusList = system.getStatusList();
//    }
//
//
////    public QNetEndPointInfo(QEndPointProps memberInfo) {
////        uuid = memberInfo.uuid;
////        name = memberInfo.name;
////        netAddressPair = memberInfo.netAddressPair;
////        installIdx = memberInfo.installIdx; // the idx used to find endpoint installation
////        statusList = memberInfo.statusList;
////    }
//
//    public void useNetAddress() {
//        String nodeWanIpAddress = system.getEndPointPair().wanNetAddress.ipAddress;
//        String nodeLanIpAddress = system.getEndPointPair().lanNetAddress.ipAddress;
//
//        String thisWanIPAddress = netAddressPair.wanNetAddress.ipAddress;
//        String thisLanIPAddress = netAddressPair.lanNetAddress.ipAddress;
//
//        if (nodeWanIpAddress.equals(thisWanIPAddress)) { // on same lan
//            if (nodeLanIpAddress.equals(thisLanIPAddress)) { // same node
//                this.useNetAddress = null;
////                this.sameNode = true;
//            } else { //diff node
//                this.useNetAddress = this.netAddressPair.lanNetAddress;
////                this.sameNode = false;
//            }
//        } else {
//            this.useNetAddress = this.netAddressPair.wanNetAddress;
//        }
//    }
//
//    public UUID getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(UUID uuid) {
//        this.uuid = uuid;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public QNetAddress getWanNetAddress() {
//        return netAddressPair.wanNetAddress;
//    }
//
//    public void setWanNetAddress(QNetAddress wanNetAddress) {
//        this.netAddressPair.wanNetAddress = wanNetAddress;
//    }
//
//    public int getInstallIdx() {
//        return installIdx;
//    }
//
//    public void setInstallIdx(int installIdx) {
//        this.installIdx = installIdx;
//    }
//
//    public ArrayList<Integer> getStatusList() {
//        return statusList;
//    }
//
//    public void setStatusList(ArrayList<Integer> statusList) {
//        this.statusList = statusList;
//    }
//}
