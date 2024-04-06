package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objectsQNE.classes;
//
//import com.qnenet.qne.channel.QChannel;
//import com.qnenet.qne.endpoint.QEndPoint;
//import com.qnenet.qne.known.QKnown;
//import com.qnenet.qne.objectsQNE.impl.QNEObjects;
//import com.qnenet.qne.objectsQNE.intf.QNetMsg;
//import com.qnenet.qne.utils.QRandomUtils;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.ObjectFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.Map;
//import java.util.Queue;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//@Scope("prototype")
//public class QInitiatorChannels {
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
////    private QEPId remoteQEPId;
//
//    private ArrayList<QInitiatorChannelInfo> keysList;
//
////    private ConcurrentHashMap<Integer, QInitiatorChannelInfo> allResponderChannelsByChannelId;
//    private Queue<QInitiatorChannelInfo> initiatorChannelsNotAavailableQueue;
//    private Queue<QInitiatorChannelInfo> initiatorChannelsAvailableQueue;
////    private Queue<QInitiatorChannelInfo> responderChannelsBusyQueue;
//
//    private ArrayList<QInitiatorChannelInfo> underConstructionList;
//    private ArrayList<Integer> usedChannelIdsList;
//
//    private Map<Integer, QNetMsg> sentNetMsgsByRTId = new ConcurrentHashMap<>();
//    private QEndPoint localEndPoint;
//    private QEndPointInfo remoteEndPointInfo;
//
//
////    public void setEndPoint(QEndPoint endPoint) {
////        this.endPoint = endPoint;
////        this.qepId = (QEPId) endPoint.getEPId();
////    }
//
////    public void setRemoteEPId(QRemoteEPId remoteEPId) {
////        this.remoteEPId = remoteEPId;
////    }
////
//
//
////    public QResponderChannels(QEPId qepId) {
////        this.qepid = qepId;
////    }
//
//    @PostConstruct
//    private void init() {
//        this.keysList = new ArrayList<>();
////        this.allResponderChannelsByChannelId = new ConcurrentHashMap<>();
//        this.initiatorChannelsNotAavailableQueue = new LinkedList<>();
//        this.initiatorChannelsAvailableQueue = new LinkedList<>();
//
//        this.underConstructionList = new ArrayList<>();
//        this.usedChannelIdsList = new ArrayList<>();
//    }
//
//    public void destroy() {
//        System.out.println("Destroy QResponderChannels");
//    }
//
////    public void doRoundTripRequest(QNetMsg netMsg) {
////        QInitiatorChannelInfo availableInitiatorChannelInfo = getAvailableInitiatorChannel(netMsg);
////        availableInitiatorChannelInfo.channel.doRoundTripRequest(netMsg);
////    }
//
//
//    public void sendNetMsg(QNetMsg netMsg) {
////        netMsg.role = HandshakeState.INITIATOR;
//        QInitiatorChannelInfo availableInitiatorChannelInfo = initiatorChannelsAvailableQueue.poll();
//        if (availableInitiatorChannelInfo != null) {
//            initiatorChannelsNotAavailableQueue.offer(availableInitiatorChannelInfo);
//            availableInitiatorChannelInfo.channel.sendNetMsg(netMsg);
//            return;
//        }
//
//        availableInitiatorChannelInfo = new QInitiatorChannelInfo();
//        availableInitiatorChannelInfo.localEndPointInfo = localEndPoint.getMyEndPointInfo();
//        availableInitiatorChannelInfo.remoteEndPointInfo = remoteEndPointInfo;
//        availableInitiatorChannelInfo.channelId = getUniqueChannelId();
//        usedChannelIdsList.add(availableInitiatorChannelInfo.channelId);
//        availableInitiatorChannelInfo.isInitiator = true;
//        availableInitiatorChannelInfo.isBusy = true;
//        availableInitiatorChannelInfo.isUnderConstruction = true;
//        availableInitiatorChannelInfo.initiatorChannels = this;
//
//        QChannel initiatorChannel = channelFactory.getObject();
////        channel.setInitiatorEndPointInfo(channelInfo.localEndPointInfo);
//        initiatorChannel.setInitiatorChannelInfo(availableInitiatorChannelInfo);
//        initiatorChannel.setResponderSocketAddress();
//        availableInitiatorChannelInfo.channel = initiatorChannel;
//        initiatorChannelsNotAavailableQueue.offer(availableInitiatorChannelInfo);
//        initiatorChannel.startHandshake(netMsg);
//}
//
//    private int getUniqueChannelId() {
//        int channelId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
//        if (!usedChannelIdsList.contains(channelId)) return channelId;
//        return getUniqueChannelId();
//    }
//
//    //    @Override
//    public int registerNetMsgRT(QNetMsg netMsg) {
//        int rtId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
//        if (!sentNetMsgsByRTId.containsKey(rtId)) {
//            netMsg.roundTripId = rtId;
//            sentNetMsgsByRTId.put(rtId, netMsg);
//            return rtId;
//        }
//        return registerNetMsgRT(netMsg);
//    }
//
//
////    public void setRemoteQEPId(QEPId remoteEPId) {
////        this.remoteQEPId = remoteEPId;
////    }
//
//    public void setResponderEndPointInfo(QEndPointInfo remoteEndPointInfo) {
//        this.remoteEndPointInfo = remoteEndPointInfo;
//    }
//
//    public void setLocalEndPoint(QEndPoint localEndPoint) {
//        this.localEndPoint = localEndPoint;
//    }
//
//    public void getChannelByChannelId() {
//
//    }
//}
//
