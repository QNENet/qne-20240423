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
//package com.qnenet.qne.objects.classes;
//
//
////import com.qnenet.qneobjects.constants.QObjectsConstants;
////import com.qnenet.qneobjects.intf.QDiscoveryServer;
////import com.qnenet.qneobjects.intf.QNetMsg;
////import com.qnenet.qneobjects.intf.QNode;
//
//
//import com.qnenet.qne.node.QNode;
//import com.qnenet.qne.constants.objectsQNE.QObjectsConstants;
//import com.qnenet.qne.intf.objectsQNE.QDiscoveryServer;
//import com.qnenet.qne.intf.objectsQNE.QNetMsg;
//
//public class QNetMsgAddNewNodeLookup extends QNetMsg {
//
//// rewuest
////    public UUID uuid;
//    public QNetAddressPair newNodeNetAddressPair;
//    public byte[] sslPublicKeyEncoded;
//
//
//    // response
//    public long nodeId;
//    public byte[][] endCertificateBytes;
//
//    @Override
//    public void handleRequest(QChannelMsg channelMsg) {
//////        QNode node = channelMsg.channel.getNode();
////        if (channelMsg.netMsg instanceof QNetMsgAddNewNodeLookup) {
////            QNetMsgAddNewNodeLookup netMsg = (QNetMsgAddNewNodeLookup) channelMsg.netMsg;
////            QDiscoveryServer discoveryServer = (QDiscoveryServer) node.getRegisteredObject(QObjectsConstants.DISCOVERY_SERVER);
////            discoveryServer.addNewNode(netMsg);
////            discoveryServer.getCertificate(netMsg);
////            setStatus(QObjectsConstants.NET_MSG_RESPONSE_STATUS_OK);
////        } else {
////            setStatus(QObjectsConstants.NET_MSG_RESPONSE_STATUS_WRONG_NODE_TYPE);
////        }
////        channelMsg.channel.sendChannelMsgWithNetMsg(channelMsg);
//    }
//
//    @Override
//    public void handleResponse(QChannelMsg channelMsg) {
////        channelMsg.channel.getNode().processChannelMsg(channelMsg);
//    }
//}
