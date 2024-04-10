// package com.qnenet.qne.network.packet;

// import com.qnenet.qne.objects.classes.QEPAddr;
// import com.qnenet.qne.objects.classes.QNetMsg;
// import com.qnenet.qne.system.utils.QDatagramUtils;

// import java.nio.ByteBuffer;

// public class QPacket {
//     public QEPAddr destEPAddr;
//     public short srcEPIdx;
//     public int channelId;
//     public int packetId;
//     public byte destRole;
// //    public byte contentType;
//     public byte keepAlive;
//     public byte[] contentBytes;
//     // private final byte[] msgBytes;

//     public transient QNetMsg netMsg;

//     public QPacket(QEPAddr destEPAddr, short srcEPIdx, int channelId, int packetId,
//                    byte destRole, byte[] contentBytes, byte keepAlive) {
//         this.destEPAddr = destEPAddr;
//         this.srcEPIdx = srcEPIdx;
//         this.channelId = channelId;
//         this.packetId = packetId;
//         this.destRole = destRole;
// //        this.contentType = contentType;
//         this.keepAlive = keepAlive;
//         this.contentBytes = contentBytes;
//         ByteBuffer bb = ByteBuffer.allocate(contentBytes.length + 14);
//         bb.putShort(destEPAddr.epIdx);
//         bb.putShort(srcEPIdx);
//         bb.putInt(channelId);
//         bb.putInt(packetId);
//         bb.put(destRole);
// //        bb.put(contentType);
//         bb.put(keepAlive);
//         bb.put(contentBytes);
//         // this.msgBytes = bb.array();
//     }

//     public void changeDirection() {
//         short tmpSrcEPIdx = srcEPIdx;
//         short tmpDestEPIdx = destEPAddr.epIdx;
// //        QEPAddr tmpDestEPAddress = destEPAddr;
//         destRole = QDatagramUtils.reverseRole(destRole);
//         srcEPIdx = tmpDestEPIdx;
//         // short destEPIdx = tmpSrcEPIdx;
//         destEPAddr.setEPIdx(tmpSrcEPIdx);
//     }

// }
