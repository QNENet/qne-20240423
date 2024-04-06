package com.qnenet.qne.objects.classes;///*
// * Copyright (C) 2022 Paul F Fraser <paulf@qnenet.com>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *     http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.qnenet.qne.objectsQNE.classes;
//
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//
//import java.io.Serializable;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.UnknownHostException;
//
//public class QNetAddress implements Serializable {
//
//    public String ipAddress; // can be IPV4 or IPV6
//    public int port;
//
//
////    public String string;
//
//    public QNetAddress() { // for kryo
//    }
//
//    public QNetAddress(InetSocketAddress socketAddr) {
//        this.ipAddress = socketAddr.getHostString();
//        this.port = socketAddr.getPort();
//    }
//
//    public QNetAddress(String ipAddr, int port) {
//        this.ipAddress = ipAddr;
//        this.port = port;
//    }
//
//
//    public static byte[] netAddressToBytes(QNetAddress netAddress) {
//        byte[] bytes = QNetAddress.ipAddressToBytes(netAddress.ipAddress);
//        int port = netAddress.port;
//        if (bytes.length == 16) {
//            port = -port;
//        }
//        ByteBuf bb = Unpooled.buffer(20);
//        bb.writeInt(port);
//        bb.writeBytes(bytes);
//        return bb.array();
//    }
//
//
//    private static boolean isZeroArray(byte[] bytes) {
//        for (int i = 0; i < bytes.length; i++) {
//            if (bytes[i] != 0) return false;
//        }
//        return true;
//    }
//
//
//    public static QNetAddress netAddressFromBytes(byte[] bytes) {
//        if (isZeroArray(bytes)) return null;
//        QNetAddress result = null;
//        String ipAddress = null;
//        ByteBuf bb = Unpooled.wrappedBuffer(bytes);
//        int port = bb.readInt();
//        if (port > 0) {
//            byte[] ipAddrBytes = new byte[4];
//            bb.readBytes(ipAddrBytes);
//            ipAddress = QNetAddress.ipAddressFromBytes(ipAddrBytes);
//            result = new QNetAddress(ipAddress, port);
//        } else { // is ipv6
//            port = -port;
//            byte[] ipAddrBytes = new byte[16];
//            bb.readBytes(ipAddrBytes);
//            ipAddress = QNetAddress.ipAddressFromBytes(ipAddrBytes);
//            result = new QNetAddress(ipAddress, port);
//        }
//        return result;
//    }
//
//
//    public static String ipAddressFromBytes(byte[] ipAddrBytes) {
//
//        InetAddress inetAddr = null;
//        try {
//            inetAddr = InetAddress.getByAddress(ipAddrBytes);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return inetAddr.getHostAddress();
//    }
//
//    public static byte[] ipAddressToBytes(String ipAddr) {
//        InetAddress inetAddr = null;
//        try {
//            inetAddr = InetAddress.getByName(ipAddr);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return inetAddr.getAddress();
//    }
//
//    public static QNetAddress fromString(String netAddrStr) {
//        String[] split = netAddrStr.split(":");
//        return new QNetAddress(split[0], Integer.valueOf(split[1]));
//    }
//
//
//    public InetSocketAddress asSocketAddress() {
//        return new InetSocketAddress(ipAddress, port);
//    }
//
//    public InetAddress asInetAddress() {
//        try {
//            return InetAddress.getByName(ipAddress);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//
//        if (o == null || getClass() != o.getClass()) return false;
//
//        byte[] objBytes = QNetAddress.netAddressToBytes((QNetAddress) o);
//
//        byte[] thisBytes = QNetAddress.netAddressToBytes(this);
//
//        if (objBytes.length != thisBytes.length) return false;
//        for (int i = 0; i < objBytes.length; i++) {
//            if (objBytes[i] != thisBytes[i]) return false;
//        }
//        return true;
//    }
//
//
//
//
////    @Override
////    public int compareTo(QNetAddress o) {
////        String me = getString();
////        String other = o.getString();
////        return me.compareTo(other);
////    }
////}
//
//    public String asString() {
//        return ipAddress + ":" + String.valueOf(port);
////        if (string == null) {
////            string = ipAddress + ":" + String.valueOf(port);
////        }
////        return string;
//    }
//
//
//    public String getAsHTTP() {
//        return "http://" + ipAddress + ":" + String.valueOf(port);
//    }
//
//    public String getAsHTTPS() {
//        return "https://" + ipAddress + ":" + String.valueOf(port);
//    }
//
//
//    public static void main(String[] args) {
//
//        String ipv4_1 = "172.13.230.256";
//
//
//        String ipv6_1 = "2001:db8:3333:4444:5555:6666:7777:8888";
//        String ipv6_2 = "2001:db8:3333:4444:CCCC:DDDD:EEEE:FFFF";
//        String ipv6_3 = "::"; // (implies all 8 segments are zero)
//        String ipv6_4 = "2001:db8::"; // (implies that the last six segments are zero)
//        String ipv6_5 = "::1234:5678"; // (implies that the first six segments are zero)
//        String ipv6_6 = "2001:db8::1234:5678"; // (implies that the middle four segments are zero)
//        String ipv6_7 = "2001:0db8:0001:0000:0000:0ab9:C0A8:0102"; // (This can be compressed to eliminate leading zeros, as follows: 2001:db8:1::ab9:C0A8:102 )
//        String ipv6_8 = "2001:db8:1::ab9:C0A8:102";
//
//        QNetAddress netAddr1 = new QNetAddress(ipv4_1, 1234);
//
//        byte[] bytes1 = QNetAddress.netAddressToBytes(netAddr1);
//        QNetAddress qNetAddress = QNetAddress.netAddressFromBytes(bytes1);
//
//
//        System.out.println(qNetAddress.ipAddress + " " + qNetAddress.port);
//    }
//
//}
