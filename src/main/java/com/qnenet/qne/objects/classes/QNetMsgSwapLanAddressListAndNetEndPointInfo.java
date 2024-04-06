package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objects.classes;///*
//
///*
//Copyright(C)2022Paul F Fraser<paulf@qnenet.com>
//        *Licensed under the Apache License,Version2.0(the"License");
//        *you may not use this file except in compliance with the License.
//        *You may obtain a copy of the License at
//        *http://www.apache.org/licenses/LICENSE-2.0
//        *Unless required by applicable law or agreed to in writing,software
//        *distributed under the License is distributed on an"AS IS"BASIS,
//        *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
//        *See the License for the specific language governing permissions and
//        *limitations under the License.
//*/
//
//import com.qnenet.qne.constants.QNetMsgConstants;
//import com.qnenet.qne.lannodes.QLanNodes;
//import com.qnenet.qne.node.QNode;
//import com.qnenet.qne.intf.objectsQNE.QNetMsg;
//import org.springframework.beans.factory.annotation.Autowired;
//import com.qnenet.qne.constants.QSysConstants;
//import java.util.ArrayList;
//
//
//public class QNetMsgSwapLanAddressListAndNetEndPointInfo extends QNetMsg {
//
//    public ArrayList<String> sendLanAddressList;
//    public ArrayList<String> replyLanAddressList;
//
//    public QNetEndPointInfo sendNetEndPointInfo;
//    public QNetEndPointInfo replyNetEndPointInfo;
//
//    @Autowired
//    QLanNodes lanNodes;
//
//    @Autowired
//    QNode node;
//
//    @Override
//    public void handleRequest(QChannelMsg channelMsg) {
////        QNode node = channelMsg.channel.getNode();
//        if (channelMsg.netMsg instanceof QNetMsgSwapLanAddressListAndNetEndPointInfo) {
//
//            replyLanAddressList = lanNodes.updateLanAddessList(sendLanAddressList);
//            sendLanAddressList = null;
//
//            lanNodes.addOrUpdateNetEndPointInfo(sendNetEndPointInfo, true);
//            replyNetEndPointInfo = lanNodes.getSelfNetEndPointInfo();
//            sendNetEndPointInfo = null;
//
//            setStatus(QNetMsgConstants.NET_MSG_RESPONSE_STATUS_OK);
//        } else {
//            setStatus(QNetMsgConstants.NET_MSG_RESPONSE_STATUS_WRONG_NET_MSG_TYPE);
//        }
//        channelMsg.channel.sendChannelMsgWithNetMsg(channelMsg);
//    }
//
//    @Override
//    public void handleResponse(QChannelMsg channelMsg) {
//        node.processChannelMsg(channelMsg);
//    }
//
//
//}
