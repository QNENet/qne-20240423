//package com.qnenet.qne.network.packet;
//
//import com.qnenet.qne.network.endpoint.QEndPoint;
//import com.qnenet.qne.objects.classes.QEPAddr;
//
//public class QSendPacketPack {
//
////    private int length;
////    private byte[] data;
////    private int receiverEPIdx;
////    private int senderEPIdx;
////    private int channelId;
////    private int role;
////    private InetSocketAddress socketAddress;
////    private QEPAddr remoteEPAddr;
////    private byte[] msgBytes;
//
//    //    private QEPAddr srcEPAddr;
//    private QEndPoint endPoint;
//    private int role;
//    private QEPAddr destEPAddr;
//    private int channelId;
//
//    private byte[] msgBytes;
//    private int contentType;
//
//    public QSendPacketPack(QEndPoint endPoint, int role, QEPAddr destEPAddr,
//                           int channelId, byte[] msgBytes, byte contentType) {
//        this.endPoint = endPoint;
//        this.role = role;
//        this.destEPAddr = destEPAddr;
//        this.channelId = channelId;
//        this.msgBytes = msgBytes;
//        this.contentType = contentType;
//    }
//
////    public DatagramPacket createSendPacket(QTestEP endPoint, byte role, QEPAddr destEPAddr,
////                                       int channelId, byte[] msgBytes, byte contentType) {
////        ByteBuffer bb = ByteBuffer.allocate(msgBytes.length + 10);
////        bb.putShort((short) destEPAddr.epIdx); // 2
////        bb.putShort((short) endPoint.getMyEPAddr().epIdx);   // 2
////        bb.putInt(channelId);  // 4
////        bb.put(role); // 1
////        bb.put(contentType); // 1
////        bb.put(msgBytes);
////        byte[] channelMsgBytes = bb.array();
////        DatagramPacket channelPacket = new DatagramPacket(channelMsgBytes, 0,
////                channelMsgBytes.length, destEPAddr.getSocketAddr());
////        return channelPacket;
////
////    }
//
////    public QSendPacketPack(DatagramPacket packet) {
////        this.length = packet.getLength();
////        this.data = packet.getData();
////        ByteBuffer bb = ByteBuffer.wrap(data);
////        this.receiverEPIdx = bb.getShort(0);
////        this.senderEPIdx = bb.getShort(2);
////        this.channelId = bb.getInt(4);
////        this.role = bb.get(8);
////        this.msgBytes = new byte[length - 9];
////        bb.get(9, msgBytes);
////        this.socketAddress = (InetSocketAddress) packet.getSocketAddress();
////        this.remoteEPAddr = new QEPAddr(socketAddress, (short) senderEPIdx);
////    }
//
//
////    public void setLength(int length) {
////        this.length = length;
////    }
////
////    public void setData(byte[] data) {
////        this.data = data;
////    }
////
////    public void setReceiverEPIdx(int receiverEPIdx) {
////        this.receiverEPIdx = receiverEPIdx;
////    }
////
////    public void setSenderEPIdx(int senderEPIdx) {
////        this.senderEPIdx = senderEPIdx;
////    }
////
////    public void setChannelId(int channelId) {
////        this.channelId = channelId;
////    }
////
////    public void setRole(int role) {
////        this.role = role;
////    }
////
////    public void setSocketAddress(InetSocketAddress socketAddress) {
////        this.socketAddress = socketAddress;
////    }
////
////    public void setRemoteEPAddr(QEPAddr remoteEPAddr) {
////        this.remoteEPAddr = remoteEPAddr;
////    }
////
////    public void setMsgBytes(byte[] msgBytes) {
////        this.msgBytes = msgBytes;
////    }
//}
