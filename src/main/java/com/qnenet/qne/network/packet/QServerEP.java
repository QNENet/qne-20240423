//package com.qnenet.qne.packet;
//
//import com.qnenet.qne.objects.classes.QNoiseKeypair;
//
//import java.net.DatagramPacket;
//
//
//public class QServerEP {
//
//    private final QEPAddr epAddr;
//    public transient QTestPacketHandler packetHandler;
//    public transient QNoiseKeypair noiseKeyPair;
//
//    public QServerEP(QEPAddr epAddr) {
//        this.epAddr = epAddr;
//    }
//
//    public void sendMsg(QEPAddr clientEPAddr, byte[] msg) {
//        DatagramPacket clientPacket = new DatagramPacket(msg, 0, msg.length, clientEPAddr.getSocketAddr());
////        packetHandler.sendServerPacket(clientPacket);
//    }
//
//    public void setPacketHandler(QTestPacketHandler packetHandler) {
//        this.packetHandler = packetHandler;
//    }
//
//    public void setNoiseKeyPair(QNoiseKeypair noiseKeypair) {
//        this.noiseKeyPair = noiseKeypair;
//    }
//}
//
