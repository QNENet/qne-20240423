//package com.qnenet.qne.packet;
//
//import com.qnenet.qne.system.utils.QDatagramUtils;
//import com.qnenet.qne.system.utils.QRandomUtils;
//import com.southernstorm.noise.protocol.HandshakeState;
//
//import java.io.IOException;
//import java.net.*;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.LinkedBlockingQueue;
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Class /////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//public class QTestPacketHandler {
//
//    private static final int DATAGRAM_BUFFER_SIZE = 64 * 1024;      // 64KB
//    private static final long RESPONSE_TIMEOUT = 2000L;
//    //    private static final long RESPONSE_TIMEOUT = 60 * 60 * 1000L; // 60 mins
//    private static final Integer MAX_TIMEOUTS = 3;
//
//    private DatagramSocket socket;
//    private boolean isRunning;
//
//    private Thread listenThread;
//    private int port = 12345;
//
//    ArrayList<LinkedBlockingQueue<DatagramPacket>> receivedPacketsQueueList = new ArrayList<>();
//    ConcurrentHashMap<Integer, QTestEP> epsByIdxMap = new ConcurrentHashMap<>();
//
//    private ConcurrentHashMap<Integer, QChannel> allChannelsMap = new ConcurrentHashMap<>();
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Constructor ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public QTestPacketHandler() throws IOException {
//        startListener();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Send Packet ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void sendPacket(DatagramPacket packet) {
//        try {
//            socket.send(packet);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Receive Packet ////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void receivePacket(DatagramPacket packet) {
//        QPacket qPacket = QDatagramUtils.unPackPacket(packet);
//        if (qPacket.destRole == HandshakeState.INITIATOR) {
//            QClientChannel clientChannel = (QClientChannel) allChannelsMap.get(qPacket.channelId);
//            clientChannel.clientReceivePacket(qPacket);
//            return;
//        }
//        QServerChannel serverChannel = (QServerChannel) allChannelsMap.get(-qPacket.channelId);
//        if (serverChannel != null) {
//            serverChannel.serverReceivePacket(qPacket);
//            return;
//        }
//
//        QTestEP endPoint = epsByIdxMap.get(qPacket.destEPAddr.getEPIdx());
//
//        serverChannel = new QServerChannel(endPoint, qPacket.destEPAddr, qPacket.channelId);
//        allChannelsMap.put(-serverChannel.getChannelId(), serverChannel);
//
//        serverChannel.serverReceivePacket(qPacket);
//
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Listener //////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void startListener() {
//        try {
//            socket = new DatagramSocket(port);
//        } catch (SocketException e) {
//            throw new RuntimeException(e);
//        }
//        listenThread = new Thread(() -> {
//            try {
//                while (isRunning) {
//                    // Wait for a packet
//                    byte[] buffer = new byte[DATAGRAM_BUFFER_SIZE];
//                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//                    socket.receive(packet); // blocked until packet received
//                    receivePacket(packet);
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        listenThread.start();
//        isRunning = true;
//        System.out.println("LLL LLL LLL Socket listening on port -> " + port);
//    }
//
//
//    public void stopListener() {
//        if (listenThread != null) {
//            listenThread.interrupt();
//            listenThread = null;
//        }
//        if (socket != null && !socket.isClosed()) {
//            socket.close();
//            socket = null;
//        }
//        isRunning = false;
//        System.out.println("CCC CCC CCC Socket Closed");
//    }
//
//    public void registerEndPoint(int epIdx, QTestEP testEP) {
//        epsByIdxMap.put(epIdx, testEP);
//    }
//
//    public void addClientChannel(QChannel packetChannel) {
//        allChannelsMap.put(packetChannel.getChannelId(), packetChannel);
//    }
//
//    public void addServerChannel(QChannel packetChannel) {
//        allChannelsMap.put(-packetChannel.getChannelId(), packetChannel);
//    }
//
//    int getUniqueChannelId() {
//        int channelId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
//        if (allChannelsMap.keySet().contains(channelId)) {
//            getUniqueChannelId();
//        }
//        return channelId;
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} /////// End Class ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
