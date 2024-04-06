package com.qnenet.qne.network.endpoint;

import com.qnenet.qne.network.known.QKnown;
import com.qnenet.qne.network.packet.QClientChannel;
import com.qnenet.qne.network.packet.QPacketHandler;
import com.qnenet.qne.objects.impl.QNEObjects;
// import com.qnenet.qne.system.constants.QSysConstants;

import com.qnenet.qne.network.packet.QPacket;
import com.qnenet.qne.objects.classes.*;
import com.qnenet.qne.system.impl.QSystem;
import com.qnenet.qne.system.utils.QRandomUtils;
import com.qnenet.qne.system.utils.QThreadUtils;
import com.southernstorm.noise.protocol.HandshakeState;
import jakarta.annotation.PostConstruct;
import org.jasypt.util.binary.AES256BinaryEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

@Service
@Scope("prototype")
public class QEndPoint { //implements QPacketOwner {

    @Autowired
    Environment env;

    @SuppressWarnings("rawtypes")
    @Autowired
    QNEObjects qobjs;


    //    private final QSystem system;
//    private final QNEObjects qobjs;
    private QEPAddr myEPAddr;
    // private QNoiseKeypair noiseKeyPair;
//    private QTestPacketHandler packetHandler;

    private LinkedBlockingQueue<QPacket> receivedPacketsQueue = new LinkedBlockingQueue<>();
    private ConcurrentHashMap<QEPAddr, ConcurrentLinkedQueue<QClientChannel>> clientChannels = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, QClientChannel> busyChannelsMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, DatagramPacket> packetsByPacketId = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, QPacketOwner> packetOwnerByPacketId = new ConcurrentHashMap<>();


    //    private ExecutorService executor;
//
//    @Autowired
//    ObjectFactory<QChannelClient> channelFactoryClient;
//
//    @Autowired
//    ObjectFactory<QChannelServer> channelFactoryServer;
//
//    @Autowired
//    QNEObjects qobjs;
//
////    @Autowired
////    QNEPaths qnePaths;
////
    @Autowired
    QPacketHandler packetHandler;
    //
//    @Autowired
    QSystem system;
//
//    @Autowired
//    QEndPointManager endPointManager;

    @Autowired
    QKnown known;

    public Path endPointInfoFilePath;

    private QEndPointProps endPointProps;
    //    private QDiscoveryServer discoveryServer;
//
////    private Map<QEPId, Queue<QChannel>> openClientChannelsMap = new ConcurrentHashMap<>();
////    private Map<QEPId, Queue<QChannel>> openServerChannelsMap = new ConcurrentHashMap<>();
////
////    private Map<QEPId, Queue<QChannel>> waitingInitiatorChannelsByQEPIdMap = new ConcurrentHashMap<>();
////    private Map<QEPId, Queue<QChannel>> busyInitiatorChannelsByQEPIdMap = new ConcurrentHashMap<>();
//
//    private Map<Integer, QChannelClient> initiatorChannels = new ConcurrentHashMap<>();
//    private Map<Integer, QChannelServer> responderChannels = new ConcurrentHashMap<>();
//    //    private Map<Integer, QPacketOwner> channelOwners = new ConcurrentHashMap<>();
////    Map<Integer, Thread> vThreads = new ConcurrentHashMap<>();
//
////    private Map<QEPId, QChannel> initiatorChannelsMap = new ConcurrentHashMap<>();
////    private Map<Integer, QChannel> responderChannelsMap = new ConcurrentHashMap<>();
//
//    private Map<String, Object> registeredObjectMap = new ConcurrentHashMap<>();
//
//    private Map<Integer, QNetMsg> netMsgsByRTId = new ConcurrentHashMap<>();
//    private boolean isRunning;
//    private ArrayList<String> localNodesPublicKnownInfoMap;
//
////    private BlockingQueue<ByteBuffer> blockingPacketBBQueue = new LinkedBlockingQueue<>();
//
    private Path endPointPropsFilePath;
    private QEndPointRestartInfo restartInfo;
////    private BlockingQueue<DatagramPacket> receivedPacketsQueue = new LinkedBlockingQueue<>();
////    private QChannel channel;

    public void destroy() {
        System.out.println("Shutting down EndPoint bean");
    }

//    public QEndPoint() {
//        System.out.println("EndPoint Construct");
//    }

///////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Post Construct ////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    @PostConstruct
    public void postConstruct() {
//        executor = system.getExecutor();
//        listenForPacket();
        startReceivedPacketListener();
        System.out.println("EndPoint Post Construct");

    }

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Init New //////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public void initNew(QSystem system, QEndPointProps endPointProps) {
        this.system = system;
        QThreadUtils.showThreadName("EndPointManager initNew");
        this.endPointProps = endPointProps;
        endPointPropsFilePath = Paths.get(endPointProps.endPointDirectory, "endpoint.props");
//        InetSocketAddress socketAddress = new InetSocketAddress(endPointProps.lanIPAddressBytes);
//        myEPAddr = new QEPAddr(socketAddress, epIdx);
        saveEndPointProps();

        restartInfo = new QEndPointRestartInfo();
        restartInfo.endPointIdx = endPointProps.endPointIdx;
        restartInfo.littlePassword = endPointProps.littlePassword;
        restartInfo.endPointInfoFilePathStr = endPointProps.endPointInfoFilePathStr;

        system.registerNewEndPoint(restartInfo);
        system.registerEndPointByIdx(this);
        known.addEPAddrPair(endPointProps.epAddrPair);

        getNetworkStructure();

    }

    private void getNetworkStructure() {
//        env.getProperty(QSysConstants.)
    }

    public void restart(QEndPointRestartInfo restartInfo) {
        AES256BinaryEncryptor littleEncryptor = new AES256BinaryEncryptor();
        littleEncryptor.setPasswordCharArray(restartInfo.littlePassword);
        endPointProps = (QEndPointProps) qobjs.loadObjFromEncFile(Paths.get(restartInfo.endPointInfoFilePathStr), littleEncryptor);
        endPointProps.littleEncryptor = littleEncryptor;
        endPointProps.bigEncryptor = new AES256BinaryEncryptor();
        endPointProps.bigEncryptor.setPasswordCharArray(endPointProps.bigPassword);

//        listenForPacket();

    }


////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Send /////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

    public void sendEchoMsgPlainBytes(QPacketOwner owner, QEPAddr destEPAddr, byte[] contentBytes, byte keepAlive) throws InterruptedException {
        // byte contentType = QSysConstants.CONTENT_TYPE_PLAIN_BYTES_ECHO;
        sendClientMsg(owner, destEPAddr, contentBytes, keepAlive);
    }

    public void sendNetMsgPlainBytes(QPacketOwner owner, QEPAddr destEPAddr, QNetMsg netMsg, byte keepAlive) throws InterruptedException {
//        byte contentType = QSysConstants.CONTENT_TYPE_PLAIN_BYTES_NET_MSG;
        byte[] contentBytes = qobjs.objToBytes(netMsg);
//        QNetMsg netMsg1 = (QNetMsg) qobjs.objFromBytes(contentBytes);
        sendClientMsg(owner, destEPAddr, contentBytes, keepAlive);
    }

    public void sendClientMsg(QPacketOwner owner, QEPAddr destEPAddr, byte[] contentBytes, byte keepAlive) throws InterruptedException {

        ConcurrentLinkedQueue<QClientChannel> clientChannelsQueue = clientChannels.get(destEPAddr);
        if (clientChannelsQueue == null) {
            clientChannelsQueue = new ConcurrentLinkedQueue<>();
            clientChannels.put(destEPAddr, clientChannelsQueue);
        }
        if (clientChannelsQueue.isEmpty()) {
            QClientChannel ch = new QClientChannel(this, destEPAddr, packetHandler.getUniqueChannelId());
            clientChannelsQueue.offer(ch);
            packetHandler.addClientChannel(ch);
        }
        QClientChannel clientChannel = clientChannelsQueue.poll();
        busyChannelsMap.put(clientChannel.getChannelId(), clientChannel);

        int packetId = getUniquePacketId();
        int channelId = clientChannel.getChannelId();
        byte destRole = (byte) HandshakeState.RESPONDER;

        packetOwnerByPacketId.put(packetId, owner);
        QPacket qPacket = new QPacket(destEPAddr, getMyEPAddr().epIdx, channelId, packetId,
                destRole, contentBytes, keepAlive);
        clientChannel.clientSendQPacket(qPacket);
    }


    public void receivePacket(QPacket qPacket) {
        try {
            receivedPacketsQueue.put(qPacket);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void receiveEchoPacket(QPacket qPacket) {
        try {
            receivedPacketsQueue.put(qPacket);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void receivePlainNetMsgPacket(QPacket qPacket) {
        qPacket.netMsg = (QNetMsg) qobjs.objFromBytes(qPacket.contentBytes);
        try {
            receivedPacketsQueue.put(qPacket);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void startReceivedPacketListener() {
        boolean isRunning = true;
        boolean finalIsRunning = isRunning;
        Thread listenThread = new Thread(() -> {
            try {
                while (finalIsRunning) {
                    QPacket qPacket = receivedPacketsQueue.take();
                    handleReceivedPacket(qPacket);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        listenThread.start();
    }

    private void handleReceivedPacket(QPacket qPacket) {

        if (qPacket.destRole == HandshakeState.INITIATOR) {
            QPacketOwner packetOwner = packetOwnerByPacketId.get(qPacket.packetId);
            packetOwner.processPacket(qPacket);
            System.out.println("EndPoint receive Initiator Message");
            return;
        }
//        if (qPacket.destRole == HandshakeState.RESPONDER) {
//            System.out.println("EndPoint receive Responder Message");
//        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Register Channels, Channels & Packets //////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


//    private void listenForPacket() {
//        executor.submit(() -> {
//            for (; ; ) {
//                DatagramPacket packet = receivedPacketsQueue.take();
//                executor.submit(() -> {
//                    try {
//                        handleReceivedPacket(packet);
//                    } catch (ShortBufferException e) {
//                        throw new RuntimeException(e);
//                    } catch (NoSuchAlgorithmException e) {
//                        throw new RuntimeException(e);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    } catch (BadPaddingException e) {
//                        throw new RuntimeException(e);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//            }
//
//        });
//    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
////////////// Open Channel /////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public QChannelClient openChannel(QEPId destEPId, QNetMsg netMsg, QPacketOwner channelOwner)
//            throws ExecutionException, InterruptedException {
////        int channelId = 1234;
//        int channelId = getUniqueChannelId();
//
//        QChannelClient clientChannel = channelFactoryClient.getObject();
//        QChannelInfo channelInfo = new QChannelInfo();
//        channelInfo.channelId = channelId;
//        channelInfo.role = HandshakeState.INITIATOR;
//        channelInfo.initiatorEndPointInfo = getEndPointInfo();
//        channelInfo.responderEndPointInfo = known.getEndPointInfo(destEPId);
//        clientChannel.setChannelInfo(channelInfo);
//        clientChannel.setEndPoint(this);
//        clientChannel.setFirstNetMsg(netMsg);
//        clientChannel.setOwner(channelOwner);
//        clientChannel.setDestSocketAddress();
//        clientChannel.setDisplayName("CLIENT");
//        initiatorChannels.put(channelInfo.channelId, clientChannel);
//        clientChannel.connect();
//        return clientChannel;
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
////////////// Received Packet Queue/////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void packetReceived(DatagramPacket packet, int channelId, int role) throws ShortBufferException,
//            NoSuchAlgorithmException, IOException, BadPaddingException, InterruptedException, ExecutionException {
////        QChannelClient channelClient = initiatorChannels.get(channelId);
////        channelClient.receivePacket(packet);
////        System.out.println("channelClient.receivePacket(packet)");
//
//        if (role == HandshakeState.INITIATOR) {
//            QChannelServer channelServer = responderChannels.get(channelId);
//            if (channelServer == null) { // needs new responder channel, should not happen for initiator channel
//
//                channelServer = channelFactoryServer.getObject();
//                QChannelInfo channelInfo = new QChannelInfo();
//                channelInfo.channelId = channelId;
//                channelInfo.role = HandshakeState.RESPONDER;
//                channelInfo.initiatorEndPointInfo = null;
//                channelInfo.responderEndPointInfo = getEndPointInfo();
//                channelServer.setChannelInfo(channelInfo);
//                channelServer.setEndPoint(this);
//                channelServer.setDisplayName("SERVER");
//                channelServer.setReturnSocketAddress((InetSocketAddress) packet.getSocketAddress());
//                responderChannels.put(channelInfo.channelId, channelServer);
//
//                showThreadName();
//                channelServer.accept(packet);
////                return;
//            } else {
//                QChannelServer responderChannelServer = responderChannels.get(channelId);
//                responderChannelServer.receivePacket(packet);
//                System.out.println("channelServer.receivePacket(packet)");
//            }
//            return;
//
//        } else {
//            QChannelClient channelClient = initiatorChannels.get(channelId);
//            channelClient.receivePacket(packet);
//        }
//
//    }
//
////        showThreadName();
////        channel.receivePacket(packet);
//
//
//    private void showThreadName() {
//        String name = Thread.currentThread().getName();
//        System.out.println("Thread Name -> " + name);
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
////////////// Messaging ////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////


    public QEPId getEPId() {
        return endPointProps.epAddrPair.epId;
    }

    public String getName() {
        return endPointProps.endPointName;
    }

    public QEPAddrPair getMyAddrPair() {
        return getMyAddrPair();
    }

//////////////////////////////////////////////////////////////////////////////////////////////////
/////// Round trips ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


//    public void doRoundTripRequest(String endPointName, QNetMsgStrings netMsg) throws ShortBufferException, NoSuchAlgorithmException, IOException, BadPaddingException, InterruptedException {
//        QEPId epId = known.getEPIdByName(endPointName);
//        QEndPointInfo endPointInfo = known.getEndPointInfo(epId);
//        doRoundTripRequest(endPointInfo, netMsg);
//    }


//    public void doRoundTripRequest(QEndPointInfo responderEndPointInfo, QNetMsg netMsg) throws ShortBufferException, NoSuchAlgorithmException, IOException, BadPaddingException, InterruptedException {
//
//
//        Queue<QChannel> queue = waitingInitiatorChannelsByQEPIdMap.get(responderEndPointInfo.qepId);
//        if (queue == null) {
//            queue = new LinkedList<>();
//        }
//        if (queue.isEmpty()) {
////            createNewInitiatorChannel(responderEndPointInfo, netMsg);
////            return;
//
//            QChannelInfo channelInfo = new QChannelInfo();
//            channelInfo.channelId = getUniqueChannelId();
//            channelInfo.role = HandshakeState.INITIATOR;
//            channelInfo.initiatorEndPointInfo = endPointProps.endPointInfo;
//            channelInfo.responderEndPointInfo = responderEndPointInfo;
//
//            QChannel channel = channelFactory.getObject();
//            channel.setChannelInfo(channelInfo);
//            channel.setDisplayName("CLIENT");
//            channel.setRole(HandshakeState.INITIATOR);
////            channel.setNew(true);
//            channel.setEndPoint(this);
//            channel.startHandshake(channelInfo.role);
//            queue.offer(channel);
//        }
//
//        QChannel channel = queue.poll();
//
////        QChannel channel = allChannelsByChannelIdMap.get(channelId);
////
////        QChannelInfo channelInfo = new QChannelInfo();
////        channelInfo.channelId =
////
////                getUniqueChannelId();
////
////        channelInfo.role = HandshakeState.INITIATOR;
////        channelInfo.initiatorEndPointInfo = endPointProps.endPointInfo;
////        channelInfo.responderEndPointInfo = responderEndPointInfo;
////
////        QChannel channel = createNewInitiatorChannel(channelInfo);
////        channel.setDestSocketAddress();
////        channel.startHandshake();
//    }


//    private QChannel createNewInitiatorChannel(QChannelInfo channelInfo) {
//        QChannel channel = channelFactory.getObject();
//        channel.setChannelInfo(channelInfo);
//        channel.setDisplayName("CLIENT");
//        channel.setRole(HandshakeState.INITIATOR);
//        channel.setNew(true);
//        channel.setEndPoint(this);
//        return channel;
//    }
//
//    public QChannel createNewServerChannel(InetSocketAddress senderSocketAddress, ByteBuf msgBB) {
//        QChannel channel = channelFactory.getObject();
//        QChannelInfo channelInfo = new QChannelInfo();
//        channel.setChannelInfo(channelInfo);
//        channel.setDisplayName("SERVER");
//        channel.setRole(HandshakeState.RESPONDER);
//        channel.setNew(true);
//        channel.setEndPoint(this);
//
//        channel.setSenderSocketAddress(senderSocketAddress);
//        channel.setMsgBB(msgBB);
//        return channel;
//    }


//    public QPacketOwner getNetMsgOrigin(QNetMsg netMsg) {
//        QNetMsg sentNetMsg = netMsgsByRTId.get(netMsg.roundTripId);
//        return sentNetMsg.origin;
//    }


//    public void removeClientChannelIfNecessary(QChannelClient channelClient) {
//        System.out.println();
////        if (!channelMsg.keepAlive) removeClientChannel(channelMsg.channel);
//    }


//
//    public QChannel getClientChannel(int channelId) {
//        return initiatorChannelsMap.get(channelId);
//    }
//
//
//    public void removeClientChannel(QChannel channel) {
//        int channelId = channel.getChannelId();
////        channelFactory.ungetService(channel);
//        initiatorChannelsMap.remove(channelId);
//    }


//
//    public void processChannelMsg(QHandshakeMsg channelMsg) {
//        getNetMsgOrigin(channelMsg).processRT(channelMsg);
//        removeClientChannelIfNecessary(channelMsg);
//    }


//    public void removeServerChannel(QChannel channel) {
//        QChannel channel1 = allChannelsByChannelIdMap.get(channel.getChannelId());
////        int channelId = channel.getChannelId();
////        responderChannelsMap.remove(channelId);
//    }


//    public void registerNetMsgRT(QNetMsg netMsg) {
//        netMsgsByRTId.put(netMsg.roundTripId, netMsg);
//    }


//    public void unregisterChannelMsgRT(int rtId) {
//        netMsgsByRTId.remove(rtId);
//    }


/////////////////////////////////////////////////////////////////////////////////////////////////////
////////////// Process //e///////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
//    public void processUnPackedPacket(QPacket uPacket) {
//
//    }
//
//    @Override
//    public void processFailedUnPackedPacket(QUnPackedPacket uPacket) {
//
//    }


//    public void processRT(QNetMsg netMsg) {
//        System.out.println("processRT");
//    }
//
//
//    public void roundTripFailed(QNetMsg netMsg) {
//        System.out.println("roundTripFailed");
//    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Register Channels, Channels & Packets //////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public int getUniqueChannelId() {
//        int channelId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
//        for (int checkChannelId : initiatorChannels.keySet()) {
//            if (checkChannelId == channelId) {
//                getUniqueChannelId();
//            }
//        }
//        return channelId;
//    }
//
//    public int getUniqueRTId() {
//        int rtId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
//        if (!netMsgsByRTId.containsKey(rtId)) return rtId;
//        return getUniqueRTId();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
////////////// Process //////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
//    public void processPacket(QPacket uPacket) {
//        System.out.println("EndPoint Process Received QPacket");
//    }
//
//    @Override
//    public void processFailedQPacket(QPacket uPacket) {
//        System.out.println("EndPoint Process FAILED Received QPacket");
//
//    }
///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Register Channels, Channels & Packets //////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public void setMyEPAddr(QEPAddr epAddr) {
        this.myEPAddr = epAddr;
    }

    // public void setNoiseKeyPair(QNoiseKeypair noiseKeyPair) {
    //     this.noiseKeyPair = noiseKeyPair;
    // }

    public QEPAddr getMyEPAddr() {
        return myEPAddr;
    }

//    public void setPacketHandler(QTestPacketHandler packetHandler) {
//        this.packetHandler = packetHandler;
//    }

    public QPacketHandler getPacketHandler() {
        return packetHandler;
    }

    public int getUniquePacketId() {
        int packetId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
        if (!packetsByPacketId.containsKey(packetId)) return packetId;
        return getUniquePacketId();
    }

    public ExecutorService getExecutor() {
        return system.getExecutor();
    }

    @SuppressWarnings("rawtypes")
    public QNEObjects getQNEObjects() {
        return qobjs;
    }


///////////////////////////////////////////////////////////////////////////////////////////////////
//////////// Persistence///////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    // private void saveEndPointInfo() {
    //     qobjs.saveObjToEncFile(endPointInfoFilePath, endPointProps, endPointProps.littleEncryptor);
    // }


    public void setEndPointType(int endPointType) {
        endPointProps.endPointType = endPointType;
    }


//    public QEPAddrPair getEPAddrPair() {
//        QEndPointInfo endPointInfo = new QEndPointInfo();
//        endPointInfo.qepId = endPointProps.qepId;
//        endPointInfo.wanIPAddressBytes = endPointProps.wanIPAddressBytes;
//        endPointInfo.lanIPAddressBytes = endPointProps.lanIPAddressBytes;
//        endPointInfo.port = endPointProps.port;
//        endPointInfo.endPointIdx = endPointProps.endPointIdx;
//        endPointInfo.publicKeyBytes = endPointProps.publicKeyBytes;
//        return endPointInfo;
//    }


    public Integer getEndPointIdx() {
        return endPointProps.endPointIdx;
    }


    public byte[] getNoisePrivateKeyClone() {
        // need clone because the clone will be cleared for security purposes
        return Arrays.copyOf(endPointProps.noiseKeypair.privateKeyBytes, 32);
    }


    public byte[] getNoisePublicKeyClone() {
        // need clone because the clone will be cleared for security purposes
        return Arrays.copyOf(endPointProps.noiseKeypair.publicKeyBytes, 32);
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
////////////// Received Packet Queue/////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void packetBBReceivedFromPacketHandler(ByteBuffer packetBB, InetSocketAddress destSocketAddress) throws ShortBufferException, NoSuchAlgorithmException, IOException, BadPaddingException, InterruptedException {
////        int responderIdx = packetBB.getInt(0);
//        int channelId = packetBB.getInt(4);
//        int role = packetBB.getInt(8);
//        QChannel channel;
//        if (role == HandshakeState.INITIATOR) {
//            channel = responderChannels.get(channelId);
//        } else {
//            channel = initiatorChannels.get(channelId);
//        }
////        QChannel channel = channels.get(channelId);
//        if (channel == null) { // needs new responder channel, should not happen for initiator channel
//
//            channel = channelFactory.getObject();
//            QChannelInfo channelInfo = new QChannelInfo();
//            channelInfo.channelId = channelId;
//            channelInfo.role = HandshakeState.RESPONDER;
//            channelInfo.initiatorEndPointInfo = null;
//            channelInfo.responderEndPointInfo = endPointProps.endPointInfo;
//            channel.setChannelInfo(channelInfo);
//            channel.setEndPoint(this);
//            channel.setInitiatorSocketAddress(destSocketAddress);
//            responderChannels.put(channelInfo.channelId, channel);
//
//            channel.startHandshake(packetBB);
//
//        }
//
////        int role = packetBB.getInt(8);
////        if (role == HandshakeState.INITIATOR) role = HandshakeState.RESPONDER;
////        else role = HandshakeState.INITIATOR;
////
////        else {
//////            channel.receivePacketBBFromEndPoint(packetBB);
//    }


//    private void listenForPacket() throws ShortBufferException, BadPaddingException, NoSuchAlgorithmException, IOException, InterruptedException {
//        for (; ; ) {
//            ByteBuffer packetBB = blockingPacketBBQueue.poll();
//            int channelId = packetBB.getInt(4);
//            QChannel channel = (QChannel) channels.get(channelId);
////            if (channel == null) {
////                channel = channelFactory.getObject();
////            }
////            channel.receivePacketBBFromEndPoint(packetBB);
//        }
//    }


//        int channelId = getUniqueChannelId();
//        Thread vThread = Thread.ofVirtual()
//                .name(String.valueOf(channelId))
//                .start(() -> {
//                    channel = channelFactory.getObject();
//                    QChannelInfo channelInfo = new QChannelInfo();
//                    channelInfo.channelId = channelId;
//                    channelInfo.role = HandshakeState.INITIATOR;
//                    channelInfo.initiatorEndPointInfo = endPointProps.endPointInfo;
//                    channelInfo.responderEndPointInfo = known.getEndPointInfo(destEPId);
//                    channel.setChannelInfo(channelInfo);
//                    channel.setEndPoint(this);
//                    channel.setFirstNetMsg(netMsg);
//                    channel.setOwner(channelOwner);
//                    channel.setDestSocketAddress();
//                    channels.put(channelInfo.channelId, channel);
//                    channel.setStatus(QSysConstants.CHANNEL_STATUS_OPEN_CHANNEL);
//                    channels.put(channel.getChannelId(), channel);
//
//                    try {
//                        channel.startHandshake(null);
//                    } catch (NoSuchAlgorithmException e) {
//                        throw new RuntimeException(e);
//                    } catch (ShortBufferException e) {
//                        throw new RuntimeException(e);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    } catch (BadPaddingException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//        vThreads.put(channelId, vThread);
//        return channelId;

//
//    public void returnedPacket(QNetMsg netMsg) {
//
//    }
//
//
//    public void returnedPacketFail(QNetMsg netMsg) {
//
//    }


///////////////////////////////////////////////////////////////////////////////////////////////////
//////////// Persistence///////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    private void saveEndPointProps() {
        qobjs.saveObjToEncFile(endPointPropsFilePath, endPointProps, endPointProps.littleEncryptor);
    }

    // private void loadEndPointInfo(AES256BinaryEncryptor littleEncryptor) {
    //     endPointProps = (QEndPointProps) qobjs.loadObjFromEncFile(endPointPropsFilePath, littleEncryptor);
    // }


    public QEndPointProps getEndPointProps() {
        return endPointProps;
    }

    public QEndPointRestartInfo getRestartInfo() {
        return restartInfo;
    }

    public char[] getLittlePassword() {
        return endPointProps.littlePassword;
    }


//    public void packetReceived(DatagramPacket packet) throws InterruptedException {
//        receivedPacketsQueue.put(packet);
//    }


//    public QChannel getChannel(int channelId) {
//        return channels.get(channelId);
//    }


//    public QEndPointRestartInfo getRestartInfo() {
//        return restartInfo;
//    }


///////////////////////////////////////////////////////////////////////////////////////////////////
} /////////// End Class ///////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


