//package com.qnenet.qne.lannodes;
//
//import com.qnenet.qne.objects.classes.*;
//import com.qnenet.qne.objects.impl.QNEObjects;
//import com.qnenet.qne.system.impl.QNEPaths;
//import com.qnenet.qne.system.impl.QSystem;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import org.apache.commons.io.FileUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.awt.*;
//import java.io.*;
//import java.net.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.zip.CRC32;
//
//@Service
//public class QLanNodes {
//
//    @Autowired
//    QNEPaths qnePaths;
//
//    @Autowired
//    QSystem system;
//
//    @Autowired
//    QNEObjects qobjs;
//
//    private DatagramSocket announceSocket;
//    private boolean isRunning;
//
//    private ArrayList<String> knownLanNodesList;
//    private Thread announceListenThread;
//
//
////          localNodesPublicNetEndPointInfoMap = known.getKnownLanNodesList();
//
//
////    localNodesPublicNetEndPointInfoMap = known.getKnownLanNodesList();
//
//    //        localNodesPublicNetEndPointInfoMap = known.getKnownLanNodesList();
//
//    public QLanNodes() {
//
//        Path knownLanNodesFilePath = Paths.get(qnePaths.getNodePath().toString(), "knownLanNodes.list");
//
//        if (Files.notExists(knownLanNodesFilePath)) {
//            knownLanNodesList = new ArrayList<>();
//            saveKnownLanNodesList();
//        } else {
//            loadKnownLanNodesList();
//        }
//
//    }
//
//    //    @Override
////    public ArrayList<String> getKnownLanNodesList() {
////        return known.getKnownLanNodesList();
////    }
//
////    @Override
//    public ArrayList<String> updateLanAddessList(ArrayList<String> sendLanAddressList) {
//        boolean dirty = false;
//        for (String addrStr : sendLanAddressList) {
//            addrStr = addrStr.trim();
//            if (knownLanNodesList.contains(addrStr)) continue;
//            knownLanNodesList.add(addrStr);
//            dirty = true;
//        }
//        if (dirty) saveKnownLanNodesList();
//        dirty = false;
//        System.out.println("---Updated List---------------------");
//        for (String lanNode : knownLanNodesList) {
//            System.out.println(lanNode);
//        }
//        System.out.println("---Updated List---------------------");
//        return knownLanNodesList;
//    }
//
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Announce ////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void startAnnounceListener() throws SocketException {
//        announceSocket = new DatagramSocket(49532);
//        announceListenThread = new Thread(() -> {
//            try {
//                while (isRunning) {
//                    // Wait for a packet
//                    byte[] buffer = new byte[2048];
//                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//                    announceSocket.receive(packet); // blocked until packet received
//                    QNetAddress netAddress = new QNetAddress((InetSocketAddress) packet.getSocketAddress());
//                    byte[] packetData = packet.getData();
//                    int length = packet.getLength();
//                    ByteBuf bb = Unpooled.wrappedBuffer(packetData);
//                    byte[] msgBytes = new byte[length];
//                    bb.readBytes(msgBytes);
//
//                    String dataStr = new String(msgBytes, StandardCharsets.UTF_8).trim();
//                    if (!knownLanNodesList.contains(dataStr)) {
//                        knownLanNodesList.add(dataStr);
//                        saveKnownLanNodesList();
//                    }
//                    System.out.println("-------------------------------------");
//                    for (String lanNode : knownLanNodesList) {
//                        System.out.println(lanNode);
//                    }
//                    System.out.println("-------------------------------------");
//
//                    if (knownLanNodesList.size() > 1) {
//                        for (int i = 1; i < knownLanNodesList.size(); i++) {
//
//                            String s = knownLanNodesList.get(i);
//                            String[] split = s.split(":");
//                            QNetAddress lanNetAddress = new QNetAddress(split[0], Integer.valueOf(split[1]));
//
//                            QNetMsgSwapLanAddressListAndNetEndPointInfo swapMsg = new QNetMsgSwapLanAddressListAndNetEndPointInfo();
//
//                            swapMsg.sendLanAddressList = knownLanNodesList;
//                            swapMsg.sendNetEndPointInfo = getSelfNetEndPointInfo();
//
//                            swapMsg.origin = this;
//                            swapMsg.destQNetAddress = lanNetAddress;
//                            swapMsg.srcIdx = -1;
//                            swapMsg.destIdx = -1;
//
//                            doRoundTripRequest(swapMsg);
//
//
////                            QNetMsgSwapNetEndPointInfo swapMsg = new QNetMsgSwapNetEndPointInfo();
////                            swapMsg.sendNetEndPointInfo = getMyNetEndPointInfo();
////                            swapMsg.origin = this;
////                            node.doRoundTripRequest(lanNetAddress, swapMsg);
//                        }
//                    }
//
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } finally {
////                stopAnnounceListener();
//            }
//        });
//        announceListenThread.start();
//        isRunning = true;
//        System.out.println("KNOWN Socket listening on port -> " + "49532");
//    }
//
////    private QNetEndPointInfo getMyNetEndPointInfo() {
////        UUID uuid = nicknamesMap.get(QSystemConstants.MY_NICKNAME);
////        return knownMap.get(uuid);
////    }
////
////    private boolean lanAddressIsSelf(QNetAddress lanNetAddress) {
////        QNetEndPointInfo selfNetEndPointInfo = getSelfNetEndPointInfo();
////        if (selfNetEndPointInfo.netAddressPair.lanNetAddress.ipAddress.equals(lanNetAddress.ipAddress)) return true;
////        return false;
////    }
//
////    public void stopAnnounceListener() {
////        if (announceListenThread != null) {
////            announceListenThread.interrupt();
////            announceListenThread = null;
////        }
////        if (announceSocket != null && !announceSocket.isClosed()) {
////            announceSocket.close();
////            announceSocket = null;
////        }
////        isRunning = false;
////        System.out.println("KNOWN Socket Closed");
////    }
//
//
////    private void announce() throws IOException {
////        String ipAddress = nodeInfo.netAddressPair.lanNetAddress.ipAddress;
////        String[] split = ipAddress.split("\\.");
////        String s = split[0] + "." + split[1] + "." + split[2] + ".255";
////        InetAddress address = InetAddress.getByName(s);
////        DatagramSocket socket = new DatagramSocket();
////        socket.setBroadcast(true);
////        QNetAddress lanNetAddress = nodeInfo.netAddressPair.lanNetAddress;
////        byte[] buffer = lanNetAddress.asString().getBytes();
////        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 49532);
////        socket.send(packet);
////        socket.close();
////    }
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////////
/////////// NetEndPoints ////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    public void addOrUpdateNetEndPointInfo(QNetEndPointInfo netEndPointInfo, boolean doSave) {
//        knownNetEndPointInfoMap.put(netEndPointInfo.uuid, netEndPointInfo);
//        if (doSave) saveKnownNetEndPointInfoMap();
//    }
//
////    @Override
////    public void addOrUpdateLanNetEndPointInfo(QNetEndPointInfo netEndPointInfo) {
////
////    }
//
//    @Override
//    public QNetEndPointInfo getSelfNetEndPointInfo() {
//        if (selfNetEndPointInfo == null) {
//            UUID uuid = nicknamesMap.get(QSystemConstants.MY_NICKNAME);
//            selfNetEndPointInfo = knownNetEndPointInfoMap.get(uuid);
//        }
//        return selfNetEndPointInfo;
//    }
//
//
//    @Override
//    public QNetEndPointInfo getNetEndPointInfoByUUID(UUID uuid) {
//        QNetEndPointInfo netEndPointInfo = knownNetEndPointInfoMap.get(uuid);
//        setUseAddress(netEndPointInfo);
//        return netEndPointInfo;
//    }
//
//    @Override
//    public void removeNetEndPointInfo(UUID uuid) {
//        knownNetEndPointInfoMap.remove(uuid);
//        saveKnownNetEndPointInfoMap();
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Persistence ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void saveKnownLanNodesList () {
//        qobjs.saveObjToFile(knownLanNodeAddressesFilePath, knownLanNodesList);
//    }
//
//    private void loadKnownLanNodesList () {
//        qobjs = (ArrayList<String>) serialization.loadObjFromFile(knownLanNodeAddressesFilePath);
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} /////// End Class ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
