// package com.qnenet.qne.network.packet;

// import com.qnenet.qne.network.node.QNode;
// import com.qnenet.qne.objects.classes.QNEPacket;
// import com.qnenet.qne.system.utils.QDatagramUtils;

// import java.net.DatagramPacket;
// import java.util.concurrent.*;

// public class QClientChannel extends QChannel {

//     // private final ExecutorService executor;
//     private LinkedBlockingQueue<QNEPacket> receivedPacketsQueue = new LinkedBlockingQueue<>();

//     public QClientChannel(QNode node, QNEPacket packetData) {
//         super(node, packetData);
//         // executor = getEndPoint().getExecutor();
//     }

//     public void receivePacket(QNEPacket qPacket) throws InterruptedException {
//         receivedPacketsQueue.put(qPacket);
//     }

//     public void clientSendQPacket(QNEPacket qPacket) {
//         sendRequestPacket(qPacket);
// //        CompletableFuture<QPacket> cf1 = CompletableFuture.supplyAsync(() -> sendRequestPacket(qPacket, executor));
//     }

//     private QNEPacket sendRequestPacket(QNEPacket qPacket) {
//         try {
//             DatagramPacket packet = QDatagramUtils.packPacket(qPacket);
//             getPacketHandler().sendPacket(packet);
//             return receivedPacketsQueue.take();
//         } catch (InterruptedException e) {
//             throw new RuntimeException(e);
//         }
//     }


//     public void clientReceivePacket(QNEPacket qPacket) {
//         getEndPoint().receivePlainNetMsgPacket(qPacket);

// //        switch (receivedQPacket.contentType) {
// //            case QSysConstants.CONTENT_TYPE_PLAIN_BYTES_ECHO ->
// //                getEndPoint().receiveEchoPacket(receivedQPacket);
// //
// //            case QSysConstants.CONTENT_TYPE_PLAIN_BYTES_NET_MSG ->
// //                getEndPoint().receivePlainNetMsgPacket(receivedQPacket);
// //
// //            default -> throw new IllegalStateException("Unexpected value: " + receivedQPacket.contentType);
// //        };
//     }
// }
