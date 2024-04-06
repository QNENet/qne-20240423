package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objectsQNE.classes;
//
//import com.qnenet.qne.channel.QChannel;
//import com.qnenet.qne.known.QKnown;
//import com.qnenet.qne.objectsQNE.impl.QNEObjects;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.ObjectFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//@Scope("prototype")
//public class QResponderChannels {
//
//    @Autowired
//    private ObjectFactory<QChannel> channelFactory;
//
//    @Autowired
//    private QNEObjects qobjs;
//
//    @Autowired
//    private QKnown known;
//
//    private Map<Integer, QChannel> responderChannelsByChannelId;
//
//    @PostConstruct
//    private void init() {
////        this.keysList = new ArrayList<>();
////        this.initiatorChannelsMap = new ConcurrentHashMap<>();
////        this.initiatorChannelsBusyQueue = new LinkedList<>();
////        this.responderChannelsReadyQueue = new LinkedList<>();
////        this.responderChannelsBusyQueue = new LinkedList<>();
////
////        this.underConstructionList = new ArrayList<>();
////        this.usedChannelIdsList = new ArrayList<>();
//    }
//
//    public void destroy() {
//        System.out.println("Destroy QResponderChannels");
//    }
//
//    public QChannel getChannelByChannelId(int channelId) {
//        QChannel channel = responderChannelsByChannelId.get(channelId);
//        if (channel == null) {
//            channel = channelFactory.getObject();
//
//            QResponderChannelInfo responderChannelInfo = new QResponderChannelInfo();
////            channelInfo.localEndPointInfo;
////            channelInfo.remoteEndPointInfo;
//            responderChannelInfo.channelId = channelId;
//            responderChannelInfo.isInitiator = false;
//            responderChannelInfo.channel = channel;
//            responderChannelInfo.responderChannels = this;
//
//            channel.setResponderChannelInfo(responderChannelInfo);
//        }
//        return channel;
//    }
//
////    public void doRoundTripRequest(QNetMsg netMsg) {
////        QInitiatorChannelInfo availableInitiatorChannelInfo = getAvailableInitiatorChannel(netMsg);
////        availableInitiatorChannelInfo.channel.doRoundTripRequest(netMsg);
////    }
//
//
////    public void doRoundTripRequest(QNetMsg netMsg) {
////        QResponderChannelInfo initiatorChannelInfo = initiatorChannelsMap.get(channelId);
////        if (initiatorChannelInfo != null) {
////            initiatorChannelInfo.channel.doRoundTripRequest(netMsg);
////            return;
////        }
////
////        initiatorChannelInfo = new QInitiatorChannelInfo();
////        initiatorChannelInfo.localEndPointInfo = localEndPoint.getMyEndPointInfo();
////        initiatorChannelInfo.remoteEndPointInfo = remoteEndPointInfo;
////        initiatorChannelInfo.channelId = getUniqueChannelId();
////        usedChannelIdsList.add(initiatorChannelInfo.channelId);
////        initiatorChannelInfo.isInitiator = true;
////        initiatorChannelInfo.isBusy = true;
////        initiatorChannelInfo.isUnderConstruction = true;
////        initiatorChannelInfo.initiatorChannels = this;
////
////        QChannel channel = channelFactory.getObject();
//////        channel.setInitiatorEndPointInfo(channelInfo.localEndPointInfo);
////        channel.setChannelInfo(initiatorChannelInfo);
////        channel.setResponderSocketAddress();
////        initiatorChannelInfo.channel = channel;
////        initiatorChannelsQueue.offer(initiatorChannelInfo);
////
////        channel.startHandshake(netMsg);
////
////}
//
////    private int getUniqueChannelId() {
////        int channelId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
////        if (!usedChannelIdsList.contains(channelId)) return channelId;
////        return getUniqueChannelId();
////    }
////
////    //    @Override
////    public int registerNetMsgRT(QNetMsg netMsg) {
////        int rtId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
////        if (!sentNetMsgsByRTId.containsKey(rtId)) {
////            netMsg.roundTripId = rtId;
////            sentNetMsgsByRTId.put(rtId, netMsg);
////            return rtId;
////        }
////        return registerNetMsgRT(netMsg);
////    }
//
//
////    public void setRemoteQEPId(QEPId remoteEPId) {
////        this.remoteQEPId = remoteEPId;
////    }
//
////    public void setInitiatorEndPointInfo(QEndPointInfo remoteEndPointInfo) {
////        this.remoteEndPointInfo = remoteEndPointInfo;
////    }
//
////    public void setLocalEndPoint(QEndPoint localEndPoint) {
////        this.localEndPoint = localEndPoint;
////    }
//
////    public void getChannelByChannelId() {
////ini
////    }
//
//
//}
//
