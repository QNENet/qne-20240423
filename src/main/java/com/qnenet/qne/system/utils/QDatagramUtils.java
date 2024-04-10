// /*
//  * Copyright (C) 2022 Paul F Fraser <paulf@qnenet.com>
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *     http://www.apache.org/licenses/LICENSE-2.0
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */

// package com.qnenet.qne.system.utils;

// import com.qnenet.qne.network.packet.QPcket;

// import java.net.DatagramPacket;
// import java.nio.ByteBuffer;

// public class QDatagramUtils {

//     public static DatagramPacket packPacket(QPacket qPacket) {
//         ByteBuffer bb = ByteBuffer.allocate(qPacket.contentBytes.length + 15);
//         bb.putShort(qPacket.destEPAddr.epIdx);
//         bb.putShort(qPacket.srcEPIdx);
//         bb.putInt(qPacket.channelId);
//         bb.putInt(qPacket.packetId);
//         bb.put(qPacket.destRole);
// //        bb.put(qPacket.contentType);
//         bb.put(qPacket.keepAlive);
//         bb.put(qPacket.contentBytes);
//         return new DatagramPacket(bb.array(), 0, bb.array().length, qPacket.destEPAddr.getSocketAddr());
//     }

//     public static QPacket unPackPacket(DatagramPacket packet) {
// //         int length = packet.getLength();
// //         byte[] packetData = packet.getData();
// //         ByteBuffer bb = ByteBuffer.wrap(packetData);
// //         short destEPIdx = bb.getShort(0);
// //         short srcEPIdx = bb.getShort(2);
// //         int channelId = bb.getInt(4);
// //         int packetId = bb.getInt(8);
// //         byte role = bb.get(12);
// // //        byte contentType = bb.get(13);
// //         byte keepAlive = bb.get(13);
// //         byte[] contentBytes = new byte[length - 14];
// //         bb.get(14, contentBytes);

// ////            public QEPAddr(QEPId epId, byte[] ipAddressBytes, short port, short epIdx) {
// //
// //            QEPAddr destEPAddr = new QEPAddr(epId, ipAddressBytes, packet.getSocketAddress(), destEPIdx);
// //
// //            //            QEPAddr destEPAddr = new QEPAddr((InetSocketAddress) packet.getSocketAddress(), destEPIdx);
// //
// //        QPacket qPacket = new QPacket(destEPAddr, srcEPIdx, channelId, packetId, role, contentBytes, keepAlive);
//         return null;
// //        return qPacket;
//     }

//     public static byte reverseRole(byte role) {
//         return (role == (byte) 1) ? (byte) 2 : (byte) 1;
//     }

// }

