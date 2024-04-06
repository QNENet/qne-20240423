//package com.qnenet.qne.packet;
//
//import com.qnenet.qne.objects.classes.QNoiseKeypair;
//import com.southernstorm.noise.protocol.HandshakeState;
//import io.netty.buffer.ByteBuf;
//
//import java.net.DatagramPacket;
//import java.nio.ByteBuffer;
//
//
//public class QClientEP {
//
//    private final QEPAddr epAddr;
//    public transient QTestPacketHandler packetHandler;
//    public transient QNoiseKeypair noiseKeyPair;
//
//    public QClientEP(QEPAddr epAddr) {
//        this.epAddr = epAddr;
//    }
//
//    public void sendMsg(QEPAddr serverEPAddr, byte[] msg) {
//        ByteBuffer bb = ByteBuffer.allocate(msg.length + 4);
//        bb.putInt(HandshakeState.INITIATOR);
//        bb.put(msg);
//        byte[] channelMsgBytes = bb.array();
//        DatagramPacket clientPacket = new DatagramPacket(channelMsgBytes, 0, channelMsgBytes.length, serverEPAddr.getSocketAddr());
//        packetHandler.sendPacket(clientPacket);
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
