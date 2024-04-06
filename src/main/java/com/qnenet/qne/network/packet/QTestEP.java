//package com.qnenet.qne.packet;
//
//import com.qnenet.qne.system.constants.QSysConstants;
//import com.qnenet.qne.objects.classes.QNoiseKeypair;
//import com.qnenet.qne.objects.impl.QNEObjects;
//import com.qnenet.qne.objects.classes.QNetMsg;
//import com.qnenet.qne.objects.classes.QPacketOwner;
//import com.qnenet.qne.system.impl.QSystem;
//import com.qnenet.qne.system.utils.QRandomUtils;
//import com.southernstorm.noise.protocol.HandshakeState;
//
//
//import java.net.DatagramPacket;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.LinkedBlockingQueue;
//
//public class QTestEP {
//    private final QSystem system;
//    private final QNEObjects qobjs;
//    private QEPAddr myEPAddr;
//    private QNoiseKeypair noiseKeyPair;
//    private QTestPacketHandler packetHandler;
//
//    private LinkedBlockingQueue<QPacket> receivedPacketsQueue = new LinkedBlockingQueue<>();
//    private ConcurrentHashMap<QEPAddr, ConcurrentLinkedQueue<QClientChannel>> clientChannels = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<Integer, QClientChannel> busyChannelsMap = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<Integer, DatagramPacket> packetsByPacketId = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<Integer, QPacketOwner> packetOwnerByPacketId = new ConcurrentHashMap<>();
//
//////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Constructor //////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public QTestEP(QSystem system) {
//        this.system = system;
//        this.qobjs = system.getQNEObjects();
//        startReceivedPacketListener();
//    }
//
//////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Send /////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void sendEchoMsgPlainBytes(QPacketOwner owner, QEPAddr destEPAddr, byte[] contentBytes, byte keepAlive) throws InterruptedException {
//        byte contentType = QSysConstants.CONTENT_TYPE_PLAIN_BYTES_ECHO;
//        sendClientMsg(owner, destEPAddr, contentBytes, keepAlive);
//    }
//
//    public void sendNetMsgPlainBytes(QPacketOwner owner, QEPAddr destEPAddr, QNetMsg netMsg, byte keepAlive) throws InterruptedException {
////        byte contentType = QSysConstants.CONTENT_TYPE_PLAIN_BYTES_NET_MSG;
//        byte[] contentBytes = qobjs.objToBytes(netMsg);
////        QNetMsg netMsg1 = (QNetMsg) qobjs.objFromBytes(contentBytes);
//        sendClientMsg(owner, destEPAddr, contentBytes, keepAlive);
//    }
//
//    public void sendClientMsg(QPacketOwner owner, QEPAddr destEPAddr, byte[] contentBytes, byte keepAlive) throws InterruptedException {
//
//        ConcurrentLinkedQueue<QClientChannel> clientChannelsQueue = clientChannels.get(destEPAddr);
//        if (clientChannelsQueue == null) {
//            clientChannelsQueue = new ConcurrentLinkedQueue<>();
//            clientChannels.put(destEPAddr, clientChannelsQueue);
//        }
//        if (clientChannelsQueue.isEmpty()) {
//            QClientChannel ch = new QClientChannel(this, destEPAddr, packetHandler.getUniqueChannelId());
//            clientChannelsQueue.offer(ch);
//            packetHandler.addClientChannel(ch);
//        }
//        QClientChannel clientChannel = clientChannelsQueue.poll();
//        busyChannelsMap.put(clientChannel.getChannelId(), clientChannel);
//
//        int packetId = getUniquePacketId();
//        int channelId = clientChannel.getChannelId();
//        byte destRole = (byte) HandshakeState.RESPONDER;
//
//        packetOwnerByPacketId.put(packetId, owner);
//        QPacket qPacket = new QPacket(destEPAddr, getMyEPAddr().epIdx, channelId, packetId,
//                destRole, contentBytes, keepAlive);
//        clientChannel.clientSendQPacket(qPacket);
//    }
//
//
//    public void receivePacket(QPacket qPacket) {
//        try {
//            receivedPacketsQueue.put(qPacket);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void receiveEchoPacket(QPacket qPacket) {
//        try {
//            receivedPacketsQueue.put(qPacket);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void receivePlainNetMsgPacket(QPacket qPacket) {
//        qPacket.netMsg = (QNetMsg) qobjs.objFromBytes(qPacket.contentBytes);
//        try {
//            receivedPacketsQueue.put(qPacket);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public void startReceivedPacketListener() {
//        boolean isRunning = true;
//        boolean finalIsRunning = isRunning;
//        Thread listenThread = new Thread(() -> {
//            try {
//                while (finalIsRunning) {
//                    QPacket qPacket = receivedPacketsQueue.take();
//                    handleReceivedPacket(qPacket);
//                }
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        listenThread.start();
//    }
//
//    private void handleReceivedPacket(QPacket qPacket) {
//
//        if (qPacket.destRole == HandshakeState.INITIATOR) {
//            QPacketOwner packetOwner = packetOwnerByPacketId.get(qPacket.packetId);
//            packetOwner.processPacket(qPacket);
//            System.out.println("EndPoint receive Initiator Message");
//            return;
//        }
////        if (qPacket.destRole == HandshakeState.RESPONDER) {
////            System.out.println("EndPoint receive Responder Message");
////        }
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Register Channels, Channels & Packets //////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void setMyEPAddr(QEPAddr epAddr) {
//        this.myEPAddr = epAddr;
//    }
//
//    public void setNoiseKeyPair(QNoiseKeypair noiseKeyPair) {
//        this.noiseKeyPair = noiseKeyPair;
//    }
//
//    public QEPAddr getMyEPAddr() {
//        return myEPAddr;
//    }
//
//    public void setPacketHandler(QTestPacketHandler packetHandler) {
//        this.packetHandler = packetHandler;
//    }
//
//    public QTestPacketHandler getPacketHandler() {
//        return packetHandler;
//    }
//
//    public int getUniquePacketId() {
//        int packetId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
//        if (!packetsByPacketId.containsKey(packetId)) return packetId;
//        return getUniquePacketId();
//    }
//
//    public ExecutorService getExecutor() {
//        return system.getExecutor();
//    }
//
//    public QNEObjects getQNEObjects() {
//        return qobjs;
//    }
//
//}
