// package com.qnenet.qne.network.packet;

// import com.qnenet.qne.network.endpoint.QEndPoint;
// import com.qnenet.qne.network.node.QNode;
// import com.qnenet.qne.objects.classes.QEPAddr;
// import com.qnenet.qne.objects.classes.QNEPacket;

// public abstract class QChannel {

// //     private final QEPAddr srcEPAddr;
// //     private final QEPAddr destEPAddr;
// //     private final int channelId;
// //     private final QPacketHandler packetHandler;
// //     private final QEndPoint endPoint;
// // //    private int busyId;

//     QNEPacket packetData;
//     QNode node;

//     public QChannel(QNode node, QNEPacket packetData) {
//         this.node = node;
//         this.packetData = packetData;

//            //     QEPAddr sourceEPAddr = new QEPAddr(packet.getAddress(), packet.getPort(), packetData.sourceEPIdx);
//             //     QEPAddr destinationEPAddr = new QEPAddr(packet.getAddress(), packet.getPort(),
//             //             packetData.destinationEPIdx);
//             //     channel = new QChannel(this, sourceEPAddr, destinationEPAddr);
//             //     channelsByIdMap.put(packetData.channelId, channel);


//         // this.endPoint = testEP;
//         // this.packetHandler = testEP.getPacketHandler();
//         // this.srcEPAddr = testEP.getMyEPAddr();
//         // this.destEPAddr = destEPAddr;
//         // this.channelId = channelId;
// //        busyId = QSysConstants.CHANNEL_NOT_BUSY_ID;
//     }

// //    public void receivePacket(QUnPackedPacket uPacket) {
// //
// //    }
// //
// //    public String sendPacket(DatagramPacket packet) {
// //        return packetHandler.sendPacket(packet);
// //    }

//     public QEPAddr getSrcEPAddr() {
//         return srcEPAddr;
//     }

//     public QEPAddr getDestEPAddr() {
//         return destEPAddr;
//     }

//     public int getChannelId() {
//         return channelId;
//     }

//     public QPacketHandler getPacketHandler() {
//         return packetHandler;
//     }

//     public QEndPoint getEndPoint() {
//         return endPoint;
//     }

// //    public int getBusyId() {
// //        return busyId;
// //    }
// //
// //    public void setBusyId(int busyId) {
// //        this.busyId = busyId;
// //    }

// }
