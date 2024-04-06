//package com.qnenet.qne.packet;
//
//import com.qnenet.qne.objects.classes.QNoiseKeypair;
//
//import java.net.DatagramPacket;
//import java.net.InetSocketAddress;
//
//public class QEP {
//
//    public final QEPAddr epAddr;
//    public transient final QTestPacketHandler packetHandler;
//    public transient final QNoiseKeypair noiseKeyPair;
//
//    public QEP(QTestPacketHandler packetHandler, InetSocketAddress socketAddr, int epIdx, QNoiseKeypair noiseKeyPair) {
//        this.epAddr = new QEPAddr();
//        epAddr.socketAddress = socketAddr;
//        epAddr.epIdx = (short) epIdx;
//        this.packetHandler = packetHandler;
//        this.noiseKeyPair = noiseKeyPair;
//    }
//
//    public QEPAddr getEPAddr() {
//        return epAddr;
//    }
//
//}
