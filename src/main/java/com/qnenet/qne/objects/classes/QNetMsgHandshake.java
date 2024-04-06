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
//import com.qnenet.qne.objectsQNE.constants.QObjectsConstants;
//import com.qnenet.qne.objectsQNE.intf.QNetMsg;
//
//public class QNetMsgHandshake extends QNetMsg {
//
//    public QEndPointInfo localEndPointInfo;
//    public QEndPointInfo remoteEndPointInfo;
//    public QNetMsg firstNetMsg;
//
//
//    @Override
//    public void handleRequest(QNetMsg netMsg) {
//        if (netMsg instanceof QNetMsgHandshake) {
//
//
//            setStatus(QObjectsConstants.NET_MSG_RESPONSE_STATUS_OK);
//        } else {
//            setStatus(QObjectsConstants.NET_MSG_RESPONSE_STATUS_WRONG_NET_MSG_TYPE);
//        }
//        netMsg.channel.sendNetMsg(netMsg);
//    }
//
//    @Override
//    public void handleResponse(QNetMsg netMsg) {
////        channelMsg.channel.getNode().processChannelMsg(channelMsg);
//    }
//
//
////    @Override
////    public void handleRequest(QRoundTrip rt) {
////        QPacketHandler handler = rt.node.getPacketHandler();
////        if (rt.netMsg instanceof QNetMsgAddSegmentServer) {
////            QDiscoveryServer discoveryServer = (QDiscoveryServer) rt.node.getRegisteredObject(QSystemConstants.DISCOVERY_SERVER);
////            segmentId = discoveryServer.addSegmentServer(publicKnownInfo, null);
////            updatedNetworkStructure = discoveryServer.getNetworkStructure();
////            setStatus(QNetMsgConstants.NET_MSG_RESPONSE_STATUS_OK);
////        } else {
////            setStatus(QNetMsgConstants.NET_MSG_RESPONSE_STATUS_WRONG_NODE_TYPE);
////        }
////        rt.isResponse = true;
////        rt.isTransport = true;
////        rt.channel.doRoundTripRequest(rt);
////    }
////
////
////    @Override
////    public void handleResponse(QRoundTrip rt) {
////        rt.node.getSentNetMsg(rt).origin.processRT(rt);
////    }
//
//
//}
