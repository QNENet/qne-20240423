// package com.qnenet.qne.network.packet;

// import com.qnenet.qne.network.node.QNode;
// import com.qnenet.qne.objects.classes.QNEPacket;

// public class QServerChannel extends QChannel {

// //    private final ExecutorService executor;
//     // @SuppressWarnings("rawtypes")
//     // private final QNEObjects qobjs;

//     public QServerChannel(QNode node, QNEPacket packetData) {
//         super(node, packetData);
// //        executor = getEndPoint().getExecutor();
//         // qobjs = getEndPoint().getQNEObjects();
//     }

// //     public void serverSendNetMsg(QPacket qPacket) throws UnknownHostException {
// //         qPacket.contentBytes = qobjs.objToBytes(qPacket.netMsg);
// //         DatagramPacket packet = QDatagramUtils.packPacket(qPacket);
// //         getPacketHandler().sendPacket(packet);
// //     }

// //     public void serverSendPacket(QPacket qPacket) throws UnknownHostException {
// //         DatagramPacket packet = QDatagramUtils.packPacket(qPacket);
// //         getPacketHandler().sendPacket(packet);
// //     }

// //     public void serverReceivePacket(QPacket qPacket) {
// //         qPacket.changeDirection();

// //         QNetMsg netMsg = (QNetMsg) qobjs.objFromBytes(qPacket.contentBytes);
// //         qPacket.netMsg = netMsg;
// //         netMsg.handleRequest(this, qPacket);


// // //        switch (qPacket.contentType) {
// // //            case QSysConstants.CONTENT_TYPE_PLAIN_BYTES_ECHO -> {
// // //                byte[] contentBytes = qPacket.contentBytes;
// // ////                createEchoResponsePacket(qPacket);
// // //                serverSendPacket(qPacket);
// // //            }
// // //            case QSysConstants.CONTENT_TYPE_PLAIN_BYTES_NET_MSG -> {
// // //                QNetMsg netMsg = (QNetMsg) qobjs.objFromBytes(qPacket.contentBytes);
// // //                qPacket.netMsg = netMsg;
// // //                netMsg.handleRequest(this, qPacket);
// // ////                qPacket.createNetMsgResponse(this, qPacket);
// // ////                qPacket.netMsg.handleRequest(this, qPacket);
// // ////                QPacket netMsgResponsePacket = createEchoResponsePacket(qReqestPacket);
// // ////                serverSendPacket(netMsgResponsePacket);
// // //            }
// // //            default -> throw new IllegalStateException("Unexpected value: " + qPacket.contentType);
// // //        }

// //         System.out.println("Server Packet Channel Receive Packet");

// //     }

// // //    private void createEchoResponsePacket(QPacket qPacket) {
// // //        qPacket.changeDirection();
// // //        byte[] contentBytes = qPacket.contentBytes;
// // //    }


// }
