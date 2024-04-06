package com.qnenet.qne.network.packet;

import com.qnenet.qne.network.endpoint.QEndPoint;
import com.qnenet.qne.objects.classes.QEPAddr;
import com.qnenet.qne.system.utils.QDatagramUtils;

import java.net.DatagramPacket;
import java.util.concurrent.*;

public class QClientChannel extends QChannel {

    // private final ExecutorService executor;
    private LinkedBlockingQueue<QPacket> receivedPacketsQueue = new LinkedBlockingQueue<>();

    public QClientChannel(QEndPoint testEP, QEPAddr destEPAddr, int channelId) {
        super(testEP, destEPAddr, channelId);
        // executor = getEndPoint().getExecutor();
    }

    public void receivePacket(QPacket qPacket) throws InterruptedException {
        receivedPacketsQueue.put(qPacket);
    }

    public void clientSendQPacket(QPacket qPacket) {
        sendRequestPacket(qPacket);
//        CompletableFuture<QPacket> cf1 = CompletableFuture.supplyAsync(() -> sendRequestPacket(qPacket, executor));
    }

    private QPacket sendRequestPacket(QPacket qPacket) {
        try {
            DatagramPacket packet = QDatagramUtils.packPacket(qPacket);
            getPacketHandler().sendPacket(packet);
            return receivedPacketsQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void clientReceivePacket(QPacket qPacket) {
        getEndPoint().receivePlainNetMsgPacket(qPacket);

//        switch (receivedQPacket.contentType) {
//            case QSysConstants.CONTENT_TYPE_PLAIN_BYTES_ECHO ->
//                getEndPoint().receiveEchoPacket(receivedQPacket);
//
//            case QSysConstants.CONTENT_TYPE_PLAIN_BYTES_NET_MSG ->
//                getEndPoint().receivePlainNetMsgPacket(receivedQPacket);
//
//            default -> throw new IllegalStateException("Unexpected value: " + receivedQPacket.contentType);
//        };
    }
}
