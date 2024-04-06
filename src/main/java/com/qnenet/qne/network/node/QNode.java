//package com.qnenet.qne.node;
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Class /////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//import com.qnenet.qne.noise.appendStore.p1.HandshakeState;
//import com.qnenet.qne.objects.classes.*;
//import com.qnenet.qne.objects.impl.QNEObjects;
//import com.qnenet.qne.intf.objectsQNE.QChannel;
////import com.qnenet.qne.objects.intf.QEndPointInfo;
//import com.qnenet.qne.intf.objectsQNE.QNetMsgOrigin;
//import com.qnenet.qne.intf.objectsQNE.QNetMsg;
//import com.qnenet.qne.objectsQNE.classes.QChannelMsg;
//import com.qnenet.qne.system.impl.QNEPaths;
//import com.qnenet.qne.system.utils.QRandomUtils;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.ObjectFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//public class QNode {
//
//    @Autowired
//    ObjectFactory<QChannel> channelFactory;
//
////    @Autowired
////    QSystem system;
//
//    @Autowired
//    QNEPaths qnePaths;
//
//    @Autowired
//    QNEObjects qobjs;
//
//
//    private Map<String, Object> registeredObjectMap = new ConcurrentHashMap<>();
//    private Map<Integer, QChannel> clientChannelsMap = new ConcurrentHashMap<>();
//    private Map<Integer, QChannel> serverChannelsMap = new ConcurrentHashMap<>();
//    private Map<Integer, QChannelMsg> channelMsgsByRTId = new ConcurrentHashMap<>();
//
//    private Path nodePropsFilePath;
//    private Map<Object, Object> nodeProps;
//
//
////    private Map<Long, QNetAddressPair> knownNodesMap;
////    private Map<String, QNetAddressPair> nicknamesMap;
//
////    private ConcurrentHashMap<Integer, QEndPointInfo> members = new ConcurrentHashMap<>();
////    private ConcurrentHashMap<Integer, QInviteInfo> invitesMap;
////
////    private ArrayList<String> routesList;
////
////    //    private AES256BinaryEncryptor littleEncryptor;
////    private ConcurrentHashMap<String, RandomAccessFile> transferRafsByTransferNameMap = new ConcurrentHashMap<>();
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Constructor ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    public QNode() {
//    }
//
//    @PostConstruct
//    public void init() {
//
//        nodePropsFilePath = Paths.get(qnePaths.getNodePath().toString(), "node.props");
//
//        if (Files.notExists(nodePropsFilePath)) {
//            nodeProps = new ConcurrentHashMap<>();
//            saveNodeProps();
//        } else {
//            loadNodeProps();
//        }
//    }
//
//
//
///////////////////////////////////////////////////////////////////////////////////////////////////
///////// Round trips ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    public void send(QNetMsg netMsg) {
////
////
////
////        if (netMsg.destNodeId != null) {
////            QEndPointInfo netAddressPair = known.getEndPoint(netMsg.destNodeId);
////            netMsg.useNetAddress = getUseNetAddress(netMsg);
////            doRoundTripRequestInternal(netMsg);
////        } else if (netMsg.destNickName != null) {
////            netMsg.destQNetAddressPair = nickNames.getNetAddressPair(netMsg.destNickName);
////            doRoundTripRequestInternal(netMsg);
////        } else if (netMsg.destQNetAddressPair != null) {
////            doRoundTripRequestInternal(netMsg);
////        }
////    }
////
////    private QNetAddress getUseNetAddress(QNetMsg netMsg) {
////        QNetAddressPair destQNetAddressPair = netMsg.destQNetAddressPair;
////        if (destQNetAddressPair.wanNetAddress.ipAddress.equals(system.getWanIPAddress())) {
////            return destQNetAddressPair.lanNetAddress;
////        } else return destQNetAddressPair.wanNetAddress;
////    }
//
//
//    public void send(QNetMsg netMsg) {
//        QChannelMsg channelMsg = new QChannelMsg();
//        channelMsg.endPointPair = netMsg.endPointNetPair;
//        channelMsg.channelId = getUniqueChannelId();
//        channelMsg.role = HandshakeState.INITIATOR;
//        channelMsg.keepAlive = netMsg.keepAlive;
//        channelMsg.channelPacketId = getUniqueRTId();
////        channelMsg.srcIdx = netMsg.srcMemberIdx;
////        channelMsg.destIdx = netMsg.destMemberIdx;
//
//        channelMsg.netMsg = netMsg;
//
//        QChannel clientChannel = getOrCreateClientChannel(channelMsg);
//        channelMsg.channel = clientChannel; // transient
////        channelMsg.origin = this; // transient
//        registerChannelMsgRT(channelMsg);
//
//        if (clientChannel.isNew()) {
//            clientChannel.startHandshake(channelMsg);
//        } else {
//            clientChannel.sendChannelPacket(channelMsg);
//        }
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Get Channel ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    private QChannel getOrCreateClientChannel(QChannelMsg channelMsg) {
//        QChannel clientChannel = clientChannelsMap.get(channelMsg.channelId);
//        if (clientChannel != null) return clientChannel;
//
//        QChannel channel = channelFactory.getObject();
//        channel.setChannelId(channelMsg.channelId);
//        channel.setFirstMessage(channelMsg.netMsg);
//        channel.setClientEndPoint(this);
//        channel.setIsNew(true);
//        channel.setEndPointPair(channelMsg.endPointPair);
//        channel.setRole(HandshakeState.INITIATOR);
//        channel.setDisplayName("CLIENT");
////        channel.setSrcIdx(channelMsg.srcIdx);
////        channel.setDestIdx(channelMsg.destIdx);
//        clientChannelsMap.put(channelMsg.channelId, channel);
//        return channel;
//    }
//
//
//    public QNetMsgOrigin getChannelMsgOrigin(QChannelMsg channelMsg) {
//        QChannelMsg sentChannelMsg = channelMsgsByRTId.get(channelMsg.channelPacketId);
//        return sentChannelMsg.netMsg.origin;
//    }
//
//
//
//    public QChannel getClientChannel(int channelId) {
//        return clientChannelsMap.get(channelId);
//    }
//
//
//    public void removeClientChannel(QChannel channel) {
//        int channelId = channel.getChannelId();
////        channelFactory.ungetService(channel);
//        clientChannelsMap.remove(channelId);
//    }
//
//
//    public void removeClientChannelIfNecessary(QChannelMsg channelMsg) {
//        if (!channelMsg.keepAlive) removeClientChannel(channelMsg.channel);
//    }
//
//
//    public void processChannelMsg(QChannelMsg channelMsg) {
//        getChannelMsgOrigin(channelMsg).returnedPacket(channelMsg);
//        removeClientChannelIfNecessary(channelMsg);
//    }
//
//
//
//    public void removeServerChannel(QChannel channel) {
//        int channelId = channel.getChannelId();
////        channelFactory.ungetService(channel);
//        serverChannelsMap.remove(channelId);
//    }
//
//
//    public void registerChannelMsgRT(QChannelMsg channelMsg) {
//        channelMsgsByRTId.put(channelMsg.channelPacketId, channelMsg);
//    }
//
//
//    public void unregisterChannelMsgRT(int rtId) {
//        channelMsgsByRTId.remove(rtId);
//    }
//
//
//
////    public QChannel getOrCreateServerChannel(QChannelMsg channelMsg) {
////        int channelId = channelMsg.channelId;
////        QChannel channel = serverChannelsMap.get(channelId);
////        if (channel != null) return channel;
////
////        channel = (QChannel) appCtx.getBean("qchannel");
//////        channel = channelFactory.getService();
////        channel.setChannelId(channelId);
//////        channel.setSrcUUID(channelMsg.responderUUID);
//////        channel.setDestUUID(channelMsg.initiatorUUID);
////        channel.setNode(this);
////        channel.setIsNew(true);
////        channel.setDestinationNetAddress(channelMsg.netAddress);
////
////        channel.setRole(HandshakeState.RESPONDER);
////        channel.setDisplayName("SERVER");
////        channel.setSrcIdx(channelMsg.srcIdx);
////        channel.setDestIdx(channelMsg.destIdx);
////        serverChannelsMap.put(channelId, channel);
////        return channel;
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    public String getWanAddressStr() {
////        QNetAddress wanNetAddress = system.constructNetAddressPair().wanNetAddress;
////        return wanNetAddress.ipAddress + ":" + wanNetAddress.port;
////    }
////
////    public String getLanAddressStr() {
////        QNetAddress lanNetAddress = system.constructNetAddressPair().lanNetAddress;
////        if (lanNetAddress == null) return null;
////        return lanNetAddress.ipAddress + ":" + lanNetAddress.port;
////    }
////
////    public PrivateKey getSSLPrivateKey() {
////        if (nodeInfo.sslPrivateKey == null) {
////            nodeInfo.sslPrivateKey = QSecurityUtils.ecdsaPrivateKeyFromBytes(nodeInfo.sslPrivateKeyEncoded);
////        }
////        return nodeInfo.sslPrivateKey;
////    }
////
//////
//////    public Path getNodePath() {
//////        return nodePath;
//////    }
////
////    public String getNodeDirStr() {
////        return nodeDirStr;
////    }
////
////    public Path getMembersPath() {
////        return system.getPathSysProp(QSystemConstants.MEMBERS_DIR);
////    }
////
//
//
//    private void printMsg(String msg) {
//        System.out.println(msg);
////        log.info(msg);
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// New System ////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
////    public void newSystem() {
//////        Properties sysProps = system.getSysProps();
////        qpaths.checkDirectory(system.getPathSysProp(QSystemConstants.NODE_DIR));
////        nodeInfo = new QNodeInfo();
////
////        nodeInfo.uuid = UUID.randomUUID();
////        nodeInfo.name = "$" + QRandomUtils.generateRandomName(4);
////
////        statusList = new ArrayList<>();
////        statusList.add(QSystemConstants.NODE_STATUS_NEW_INSTALL);
////        saveStatusList();
////
//////        convertSysProps(sysProps);
////
////        KeyPair ecdsaKeyPair = QSecurityUtils.createECDSAKeyPair("P-256");
////        nodeInfo.sslPrivateKey = ecdsaKeyPair.getPrivate();
////        nodeInfo.sslPrivateKeyEncoded = ecdsaKeyPair.getPrivate().getEncoded();
////        nodeInfo.sslPublicKeyEncoded = ecdsaKeyPair.getPublic().getEncoded();
////
//////        PrivateKey sslPrivateKey = getSSLPrivateKey();
//////        byte[] bytes = sslPrivateKey.getEncoded();
////
////        nodeInfo.noiseKeypair = noise.createNoiseKeypair();
////
////
////        nodeInfo.masterPassword = QSecurityUtils.generatePasswordChars(16);
////        nodeInfo.masterEncryptor = new AES256BinaryEncryptor();
////        nodeInfo.masterEncryptor.setPasswordCharArray(nodeInfo.masterPassword);
////
////        nodeInfo.passwordPin = QRandomUtils.randomIntBetween(1000, 9999);
////
////        nodeInfo.memberAccessKeyList = new ArrayList<>();
////        nodeInfo.memberPinsList = new ArrayList<>();
////
////        saveNodeInfo();
////
////        knownNodesMap = new ConcurrentHashMap<>();
////        nicknamesMap = new ConcurrentHashMap<>();
////        invitesMap = new ConcurrentHashMap<>();
////
////
//////        saveKnownLanNodesList();
////
//////        QNetEndPointInfo selfNetEndPointInfo = new QNetEndPointInfo(nodeInfo);
//////        addOrUpdateNetEndPointInfo(selfNetEndPointInfo, true);
//////        addNickname(QSystemConstants.MY_NICKNAME, selfNetEndPointInfo.uuid);
////
////        saveKnownNodesMap();
////        saveNicknamesMap();
////        saveInvitesMap();
////
////
////        routesList = new ArrayList<>();
////        routesList.add(QSystemConstants.ROUTE_PUBLIC_HOME);
////
//////        routeManager.registerRoutes(QSystemConstants.NODE_INSTALL_IDX, routesList);
////
////        saveRoutesList();
////
////        packetHandler.startListener();
////
////        int port = system.getIntSysProp(QSystemConstants.PORT);
////        if (port != 49334) {
////            addNodeToDiscoveryServer();
////        }
////
////
//////        pins = new ConcurrentHashMap<>();
//////        }
////
////
////    }
//
//
////    private PrivateKey privateKeyFromPEM(String sslPrivateKeyPEM) {
////        try {
////
////            Reader rdr = new StringReader(sslPrivateKeyPEM); // or from file etc.
////            org.bouncycastle.util.io.pem.PemObject spki = new org.bouncycastle.util.io.pem.PemReader(rdr).readPemObject();
//////        PublicKey endPublicKey = null;
////            return KeyFactory.getInstance("EC", "BC").generatePrivate(new X509EncodedKeySpec(spki.getContent()));
////        } catch (InvalidKeySpecException e) {
////            throw new RuntimeException(e);
////        } catch (NoSuchAlgorithmException e) {
////            throw new RuntimeException(e);
////        } catch (NoSuchProviderException e) {
////            throw new RuntimeException(e);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
////    }
//
//
////    private void addNodeToDiscoveryServer() {
////        QNetMsgAddNewNodeLookup netMsg = new QNetMsgAddNewNodeLookup();
//////        netMsg.uuid = nodeInfo.uuid;
////        netMsg.newNodeNetAddressPair = system.constructNetAddressPair();
////        netMsg.sslPublicKeyEncoded = nodeInfo.sslPublicKeyEncoded;
////        netMsg.origin = this;
////        netMsg.keepAlive = false;
////        netMsg.destQNetAddressPair = new QNetAddressPair();
////        netMsg.destQNetAddressPair = system.getRandomDiscoveryServerNetAddress();
////        netMsg.useNetAddress = getUseNetAddress(netMsg);
////        netMsg.srcMemberIdx = 0;
////        netMsg.destMemberIdx = 0;
////        doRoundTripRequest(netMsg);
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Restart ///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    public void restart() {
////        loadNodeInfo();
////        loadStatusList();
////        nodeInfo.masterEncryptor = new AES256BinaryEncryptor();
////        nodeInfo.masterEncryptor.setPasswordCharArray(nodeInfo.masterPassword);
////        nodeInfo.sslPrivateKey = QSecurityUtils.ecdsaPrivateKeyFromBytes(nodeInfo.sslPrivateKeyEncoded);
////        loadKnownNodesMap();
////        loadNicknamesMap();
////        loadInvitesMap();
////
////
//////        for (int i = 0; i < nodeInfo.littlePasswordList.size(); i++) {
//////            char[] littlePassword = nodeInfo.littlePasswordList.get(i);
//////            if (littlePassword == null) {
//////                members.put(i, null);
//////                continue;
//////            }
//////
//////            Path memberPath = Paths.get(membersPath.toString(), QStringUtils.int4(i));
//////            QEndPointInfo endpoint = memberFactory.getService();
//////            endpoint.restartInit(this, littlePassword, memberPath);
//////            members.put(i, endpoint);
//////        }
////
////        packetHandler.startListener();
////
//////        installUPnPIfNecessary();
////    }
//
////    private void installUPnPIfNecessary() {
////        if (!QGuiUtils.isReallyHeadless()) {
////            systemManager.installArtifacts("addonupnp-0.0.1.kar");
////        }
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Announce ////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    private void startAnnounceListener() throws SocketException {
////        announceSocket = new DatagramSocket(49532);
////        announceListenThread = new Thread(() -> {
////            try {
////                while (isRunning) {
////                    // Wait for a packet
////                    byte[] buffer = new byte[2048];
////                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
////                    announceSocket.receive(packet); // blocked until packet received
////                    QNetAddress netAddress = new QNetAddress((InetSocketAddress) packet.getSocketAddress());
////                    byte[] packetData = packet.getData();
////                    int length = packet.getLength();
////                    ByteBuf bb = Unpooled.wrappedBuffer(packetData);
////                    byte[] msgBytes = new byte[length];
////                    bb.readBytes(msgBytes);
////
////                    String dataStr = new String(msgBytes, StandardCharsets.UTF_8).trim();
////                    if (!knownLanNodesList.contains(dataStr)) {
////                        knownLanNodesList.add(dataStr);
////                        saveKnownLanNodesList();
////                    }
////                    System.out.println("-------------------------------------");
////                    for (String lanNode : knownLanNodesList) {
////                        System.out.println(lanNode);
////                    }
////                    System.out.println("-------------------------------------");
////
////                    if (knownLanNodesList.size() > 1) {
////                        for (int i = 1; i < knownLanNodesList.size(); i++) {
////
////                            String s = knownLanNodesList.get(i);
////                            String[] split = s.split(":");
////                            QNetAddress lanNetAddress = new QNetAddress(split[0], Integer.valueOf(split[1]));
////
////                            QNetMsgSwapLanAddressListAndNetEndPointInfo swapMsg = new QNetMsgSwapLanAddressListAndNetEndPointInfo();
////
////                            swapMsg.sendLanAddressList = knownLanNodesList;
////                            swapMsg.sendNetEndPointInfo = getSelfNetEndPointInfo();
////
////                            swapMsg.origin = this;
////                            swapMsg.destQNetAddress = lanNetAddress;
////                            swapMsg.srcIdx = -1;
////                            swapMsg.destIdx = -1;
////
////                            doRoundTripRequest(swapMsg);
////
////
//////                            QNetMsgSwapNetEndPointInfo swapMsg = new QNetMsgSwapNetEndPointInfo();
//////                            swapMsg.sendNetEndPointInfo = getMyNetEndPointInfo();
//////                            swapMsg.origin = this;
//////                            node.doRoundTripRequest(lanNetAddress, swapMsg);
////                        }
////                    }
////
////                }
////            } catch (IOException e) {
////                throw new RuntimeException(e);
////            } finally {
//////                stopAnnounceListener();
////            }
////        });
////        announceListenThread.start();
////        isRunning = true;
////        System.out.println("KNOWN Socket listening on port -> " + "49532");
////    }
//
////    private QNetEndPointInfo getMyNetEndPointInfo() {
////        UUID uuid = nicknamesMap.get(QSystemConstants.MY_NICKNAME);
////        return knownMap.get(uuid);
////    }
////
////    private boolean lanAddressIsSelf(QNetAddress lanNetAddress) {
////        QNetEndPointInfo selfNetEndPointInfo = getSelfNetEndPointInfo();
////        if (selfNetEndPointInfo.netAddressPair.lanNetAddress.ipAddress.equals(lanNetAddress.ipAddress)) return true;
////        return false;
////    }
//
////    public void stopAnnounceListener() {
////        if (announceListenThread != null) {
////            announceListenThread.interrupt();
////            announceListenThread = null;
////        }
////        if (announceSocket != null && !announceSocket.isClosed()) {
////            announceSocket.close();
////            announceSocket = null;
////        }
////        isRunning = false;
////        System.out.println("KNOWN Socket Closed");
////    }
//
//
////    private void announce() throws IOException {
////        String ipAddress = nodeInfo.netAddressPair.lanNetAddress.ipAddress;
////        String[] split = ipAddress.split("\\.");
////        String s = split[0] + "." + split[1] + "." + split[2] + ".255";
////        InetAddress address = InetAddress.getByName(s);
////        DatagramSocket socket = new DatagramSocket();
////        socket.setBroadcast(true);
////        QNetAddress lanNetAddress = nodeInfo.netAddressPair.lanNetAddress;
////        byte[] buffer = lanNetAddress.asString().getBytes();
////        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 49532);
////        socket.send(packet);
////        socket.close();
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Convert SysProps //////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    private void convertSysProps(Properties sysProps) {
//////        nodeInfo.cpuCount = Integer.valueOf(sysProps.getProperty("machine.cpu.count"));
//////        nodeInfo.cpuSpeed = Integer.valueOf(sysProps.getProperty("machine.cpu.speed"));
//////        nodeInfo.swapMemory = Integer.valueOf(sysProps.getProperty("machine.swap.memory"));
//////        nodeInfo.totalMemory = Integer.valueOf(sysProps.getProperty("machine.total.memory"));
//////        nodeInfo.multipleProcessors = Integer.valueOf(sysProps.getProperty("platform.has.multiple.processors"));
//////
//////        nodeInfo.machineIpAddress = sysProps.getProperty("machine.ip.address");
////
////
//////        nodeInfo.platformName = sysProps.getProperty("platform.name");
//////        nodeInfo.systemLocale = sysProps.getProperty("system.locale");
//////        nodeInfo.isHeadless = QConvertUtils.getBooleanFromString(sysProps.getProperty(QSystemConstants.IS_HEADLESS));
////
//////        nodeInfo.testUseLanAdddress = QConvertUtils.getBooleanFromString(sysProps.getProperty(QSystemConstants.TEST_USE_LAN_ADDRESS));
////
//////        String osxMV = sysProps.getProperty("osx.major.version");
//////        if (!osxMV.contains("***")) {
//////            nodeInfo.osxMajorVersion = osxMV;
//////        }
////
//////        String osxV = sysProps.getProperty("osx.version");
//////        if (!osxV.contains("***")) {
//////            nodeInfo.osxVersion = osxV;
//////        }
////
//////        String winOSName = sysProps.getProperty("windows.os.name");
//////        if (!winOSName.contains("***")) {
//////            nodeInfo.windowsOSName = winOSName;
//////        }
////
//////        nodeInfo.isGenesis = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.IS_GENESIS));
//////
//////        nodeInfo.isProduction = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.IS_PRODUCTION)); //"true");
//////        nodeInfo.isNewSystem = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.DO_NEW_SYSTEM)); //"n");
//////        nodeInfo.useStartupArtifacts = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.USE_STARTUP_ARTIFACTS)); //"false");
//////        nodeInfo.startupArtifacts = (String) sysProps.get(QSystemConstants.STARTUP_ARTIFACTS);
//////        nodeInfo.bundleWatch = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.BUNDLE_WATCH)); //"true");
//////        nodeInfo.isQNEWebsite = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.IS_QNE_WEBSITE)); //"false");
//////        nodeInfo.doSocketTimeOut = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.DO_SOCKET_TIMEOUT)); //"n");
//////        nodeInfo.sayConsolePrint = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.SAY_CONSOLE_PRINT)); //"true");
//////        nodeInfo.sayLog = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.SAY_LOG)); //"true");
//////        nodeInfo.debugLevel = (String) sysProps.get(QSystemConstants.DEBUG_LEVEL); //"warn");
//////        nodeInfo.karafDir = (String) sysProps.get(QSystemConstants.KARAF_DIR); //"/home/paulf/QNE-DEV-4.3.6/karaf");
//////        nodeInfo.installerPathName = (String) sysProps.get(QSystemConstants.INSTALLER_PATH_NAME);
//////        nodeInfo.karafFeaturesConfigKey = (String) sysProps.get(QSystemConstants.KARAF_FEATURES_CONFIG_KEY); //"com.qnenet.qne.runprops");
//////        nodeInfo.systemUserName = (String) sysProps.get(QSystemConstants.SYSTEM_USER_NAME); //"root");
//////        nodeInfo.systemTmpDir = (String) sysProps.get(QSystemConstants.SYSTEM_TMP_DIRECTORY); //"/tmp");
//////        nodeInfo.javaDir = (String) sysProps.get(QSystemConstants.JAVA_DIR); //"/home/paulf/QNE-DEV-4.3.6/java");
//////        nodeInfo.installerDir = (String) sysProps.get(QSystemConstants.INSTALLER_DIR); //"/home/paulf/Dropbox/software/installbuilder/installbuilder-23.1.0-linux/output");
//////
//////        nodeInfo.discoveryServerListString = (String) sysProps.get(QSystemConstants.DISCOVERY_SERVERS); //"6b7b5c5c-a868-4371-b438-945bed3f19b9;5.78.44.229:49533");
////
////
//////        nodeInfo.installDir = (String) sysProps.get(QSystemConstants.INSTALL_DIR); //"/home/paulf/QNE-DEV-4.3.6");
//////        nodeInfo.platformInstallerPrefix = (String) sysProps.get(QSystemConstants.PLATFORM_INSTALLER_PREFIX); //"/opt");
//////        nodeInfo.machineHostName = (String) sysProps.get(QSystemConstants.MACHINE_HOST_NAME); //"paulf-S550CB");
//////        nodeInfo.sslKeyFactoryAlgorithm = (String) sysProps.get(QSystemConstants.SSL_KEY_INFO); //"rsa:2048");
//////        nodeInfo.machineFQDN = (String) sysProps.get(QSystemConstants.MACHINE_FQDN); //"paulf-S550CB");
//////        nodeInfo.platformExecSuffix = (String) sysProps.get(QSystemConstants.PLATFORM_EXEC_SUFFIX); //"run");
//////        nodeInfo.userHomeDir = (String) sysProps.get(QSystemConstants.USER_HOME_DIR); //"/home/paulf");
//////        nodeInfo.user = (String) sysProps.get(QSystemConstants.USER); //"paulf");
//////        nodeInfo.platformPathSeparator = (String) sysProps.get(QSystemConstants.PLATFORM_PATH_SEPARATOR); //"/");
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Members ///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////
////    public QEndPointInfo addMember(QInviteInfo inviteInfo) {
////        QEndPointInfo endpoint = memberFactory.getService();
////        endpoint.initNew(this, inviteInfo);
////        return endpoint;
////    }
//
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Register Object ///////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    public void registerObject(String key, Object object) {
//        registeredObjectMap.put(key, object);
//    }
//
//
//    public void unregisterObject(String key) {
//        registeredObjectMap.remove(key);
//    }
//
//
//    public Object getRegisteredObject(String key) {
//        return registeredObjectMap.get(key);
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Ports /////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    private void openPorts() {
//////        QPortMapping portMapping = new QPortMapping();
//////
//////        portMapping.externalPort = nodeInfo.wanNetAddress.port;
//////        portMapping.internalPort = nodeInfo.lanNetAddress.port;
//////        portMapping.internalClient = (String) system.getsystemInfoMap().get(QSystemConstants.LAN_IP_ADDRESS);
//////        portMapping.protocol = "UDP";
//////        portMapping.description = "QNE-" + nodeInfo.loginName;
//////        portMapping.enabled = true;
//////        portMapping.leaseDurationSeconds = 0;
//////        portMapping.remoteHost = null;
//////
//////        uPnP.openPort(portMapping, nodeInfo.loginName);
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Getters & Setters /////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
////
////    public Timer getChannelMsgTimer() {
////        return channelMsgTimer;
////    }
//
////
////    public void setSegmentIdAndSave(int segmentId) {
////        nodeInfo.segmentId = segmentId;
////        system.getExecutor().submit(new Runnable() {
////
////            public void run() {
////                saveNodeInfo();
////            }
////        });
////    }
////
////
////    public int getSegmentId() {
////        return nodeInfo.segmentId;
////    }
//
////
////    public QPacketHandler getPacketHandler() {
////        return packetHandler;
////    }
////
////
////    public AES256BinaryEncryptor getEncryptor() {
////        return nodeInfo.masterEncryptor;
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Paths /////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    void setupPaths() {
////        nodeDirStr = system.getStrSysProp(QSystemConstants.NODE_DIR);
//////        nodePath = Paths.g;
////        nodeInfoFilePath = Paths.get(nodeDirStr, "node.info");
////
//////        knownLanNodeAddressesFilePath = Paths.get(nodePath.toString(), "knownLanNodeAddresses.list");
//////
//////        objEntryIdsFilePath = Paths.get(nodePath.toString(), "objEntryIds.props");
////
////        knownNodesMapFilePath = Paths.get(nodeDirStr, "knownNetEndPointInfo.map");
//////        networkStructureFilePath = Paths.get(nodePath.toString(), "networkStructure");
////        nicknamesMapFilePath = Paths.get(nodeDirStr, "nicknames.map");
////        invitesMapFilePath = Paths.get(nodeDirStr, "invites.map");
//////        pinsFilePath = Paths.get(nodePath.toString(), "pins.map");
////        routesListFilePath = Paths.get(nodeDirStr, "routes.list");
////
//////        membersPath = Paths.get(system.getWorkPath().toString(), "members");
////
////
////    }
//
//
////
////    public byte[] getNoisePrivateKeyClone() {
////        return nodeInfo.noiseKeypair.privateKeyBytes.clone();
////    }
////
////
////    public byte[] getNoisePublicKeyClone() {
////        return nodeInfo.noiseKeypair.publicKeyBytes.clone();
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Register Channels, Channels & Packets /////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    public int getUniqueChannelId() {
//        int channelId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
//        for (int checkChannelId : clientChannelsMap.keySet()) {
//            if (checkChannelId == channelId) {
//                getUniqueChannelId();
//            }
//        }
//        return channelId;
//    }
//
//
//    public int getUniqueRTId() {
//        int rtId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
//        if (!channelMsgsByRTId.containsKey(rtId)) return rtId;
//        return getUniqueRTId();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Getters & Setters /////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////
////    public QNodeInfo getNodeInfo() {
////        return nodeInfo;
////    }
//
//    //
//
////
////    public ArrayList<String> getKnownLanNodesList() {
////        return known.getKnownLanNodesList();
////    }
//
////
////    public ArrayList<String> updateLanAddessList(ArrayList<String> sendLanAddressList) {
////        boolean dirty = false;
////        for (String addrStr : sendLanAddressList) {
////            addrStr = addrStr.trim();
////            if (knownLanNodesList.contains(addrStr)) continue;
////            knownLanNodesList.add(addrStr);
////            dirty = true;
////        }
////        if (dirty) saveKnownLanNodesList();
////        dirty = false;
////        System.out.println("---Updated List---------------------");
////        for (String lanNode : knownLanNodesList) {
////            System.out.println(lanNode);
////        }
////        System.out.println("---Updated List---------------------");
////        return knownLanNodesList;
////    }
//
//    //    58.178.192.211:26055;192.168.1.5:26055,58.178.192.211:26055;192.168.1.5:26055
////
////    public QNetAddressPair getRandomDiscoveryServerNetAddress() {
////        QNetAddressPair netAddressPair = new QNetAddressPair();
////        sysProps.discoveryServerListString
////        String[] discoveryServerStrs = sysProps.discoveryServerListString.split(",");
////        String discServerStr = discoveryServerStrs[QRandomUtils.randomIntBetween(0, discoveryServerStrs.length - 1)];
////        String[] serverStrSplit = discServerStr.split(";");
////        String wanAddressStr = serverStrSplit[0];
////        String[] wanSplit = wanAddressStr.split(":");
////        netAddressPair.wanNetAddress = new QNetAddress(wanSplit[0], Integer.valueOf(wanSplit[1]));
////
////        String lanAddressStr;
////        if (serverStrSplit.length > 1) {
////            lanAddressStr = serverStrSplit[1];
////            String[] lanSplit = lanAddressStr.split(":");
////            netAddressPair.lanNetAddress = new QNetAddress(lanSplit[0], Integer.valueOf(lanSplit[1]));
////        }
////
////        return netAddressPair;
////
////    }
//
//
//
////
////    public void addNetEndPointInfo(QNetEndPointInfo knownInfo) {
////        known.addOrUpdateNetEndPointInfo(knownInfo, true);
////    }
////
////
////    public QNetEndPointInfo getSelfNetEndPointInfo() {
////        return known.getSelfNetEndPointInfo();
////    }
//
//
////    private void saveMemberInfo(QEndPointProps memberInfo) {
////        serialization.saveObjToEncFile(Paths.get(memberInfo.memberDirStr, "endpoint.info"), memberInfo, memberInfo.littleEncryptor);
////    }
////
////    private void loadMemberInfo(char[] littlePassword) {
//////        memberInfo = serialization.loadObjFromEncFile(Paths.get(memberInfo.memberDirStr), memberInfo, memberInfo.littleEncryptor);
////    }
//
////
////    public void sendEmail(ArrayList<String> toList, ArrayList<String> ccList, ArrayList<String> bccList, String subject, String body) {
////        try {
////            emailSend.sendQNEEmail(this, toList, ccList, bccList, subject, body);
////        } catch (AddressException e) {
////            throw new RuntimeException(e);
////        }
////    }
//
////
////    public void saveUserAnswerSet(QAnswerSet questionaaireAnswerSet) {
////        questionAnswerSets.put(questionaaireAnswerSet.getQuestionsetName(), questionaaireAnswerSet);
////        saveQuestionaireAnswerSets();
////    }
//
////
////    public byte[] getCurrentPhotoBytes() {
////        return null;
////    }
//
////    public void saveQuestionaireAnswerSets() {
//////        serialization.saveObjToEncFile(answerSetsFilePath, questionAnswerSets, nodeInfoEncryptor);
////        serialization.saveObjToEncFile(answerSetsFilePath, questionAnswerSets, encryptor);
////    }
//
////    private void loadQuestionaireAnswerSets() {
//////        questionAnswerSets = (Map<String, QAnswerSet>) serialization.loadObjFromEncFile(answerSetsFilePath, nodeInfoEncryptor);
////        questionAnswerSets = (Map<String, QAnswerSet>) serialization.loadObjFromEncFile(answerSetsFilePath, encryptor);
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// NetEndPoints ////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////
////    public void addOrUpdateNetEndPointInfo(QNetEndPointInfo netEndPointInfo, boolean doSave) {
////        knownNetEndPointInfoMap.put(netEndPointInfo.uuid, netEndPointInfo);
////        if (doSave) saveKnownNetEndPointInfoMap();
////    }
////
//////
//////    public void addOrUpdateLanNetEndPointInfo(QNetEndPointInfo netEndPointInfo) {
//////
//////    }
////
////
////    public QNetEndPointInfo getSelfNetEndPointInfo() {
////        if (selfNetEndPointInfo == null) {
////            UUID uuid = nicknamesMap.get(QSystemConstants.MY_NICKNAME);
////            selfNetEndPointInfo = knownNetEndPointInfoMap.get(uuid);
////        }
////        return selfNetEndPointInfo;
////    }
////
////
////
////    public QNetEndPointInfo getNetEndPointInfoByUUID(UUID uuid) {
////        QNetEndPointInfo netEndPointInfo = knownNetEndPointInfoMap.get(uuid);
////        setUseAddress(netEndPointInfo);
////        return netEndPointInfo;
////    }
////
////
////    public void removeNetEndPointInfo(UUID uuid) {
////        knownNetEndPointInfoMap.remove(uuid);
////        saveKnownNetEndPointInfoMap();
////    }
//
////
////    public QEndPointInfo getMember(int installIdx) {
////        return members.get(installIdx);
////    }
////
////
////    public void registerMember(int installIdx, QEndPointInfo endpoint) {
////        members.put(installIdx, endpoint);
////    }
////
////
////    public boolean checkIfEmailAlreadyInvited(String email) {
////        for (QEndPointInfo endpoint : members.values()) {
////            if (endpoint.getMemberInfo().email.equals(email)) return true;
////        }
////        return false;
////    }
////
////
////    public QEndPointInfo findMemberByPin(int pin) {
////        for (int i = 0; i < nodeInfo.memberPinsList.size(); i++) {
////            if (nodeInfo.memberPinsList.get(i) != pin) continue;
////            return members.get(i);
////        }
////        return null;
////    }
////
////
////    public boolean setMemberPin(int installIdx) {
////        int pin = getFreeMemberPin();
////        if (installIdx != nodeInfo.memberPinsList.size()) return false;
////        nodeInfo.memberPinsList.add(pin);
////        saveNodeInfo();
////        return true;
////    }
////
////    private int getFreeMemberPin() {
////        return 123456;
//////        for (int i = 0; i < 10000; i++) {
//////            int pin = QRandomUtils.randomIntBetween(100000, 999999);
//////            if (nodeInfo.memberPinsList.contains(pin)) getFreeMemberPin();
//////            return pin;
//////        }
//////        return 0;
////    }
//
////
////    public QEndPointInfo findByPin(int pin) {
////        return members.;
////    }
//
////
////    public void addNetEndPointInfoComplete(QNetEndPointInfo netEndPointInfo) {
////
////    }
////
////
////    public QNetEndPointInfo getSelfNetEndPointInfoComplete() {
////
////        return null;
////    }
//
////
////    public void swapNetEndPointInfo(QNetAddress netAddr) {
////        QNetMsgSwapNetEndPointInfo swapMsg = new QNetMsgSwapNetEndPointInfo();
////        swapMsg.sendNetEndPointInfo = getSelfNetEndPointInfo();
////        swapMsg.origin = this;
////        swapMsg.destQNetAddress = netAddr;
////        doRoundTripRequest(swapMsg);
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// NickNames ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////
////    public void addNickname(String nickName, QNetAddressPair netAddressPair) {
////        nicknamesMap.put(nickName, netAddressPair);
////        saveNicknamesMap();
////    }
////
////
////    public void createEmail(QInviteInfo inviteInfo, Desktop desktop) {
////        ExecutorService executor = system.getExecutor();
////        executor.submit(new Runnable() {
////
////            public void run() {
//////                        createEmail(inviteInfo);
////                StringBuilder sb = new StringBuilder();
////                sb.append("mailto:");
////                sb.append(inviteInfo.mainEmailAddress);
////                sb.append("?subject=");
//////                        sb.append("Quick%20N%20Easy%20Invite");
//////                        sb.append("&body=");
//////                        sb.append("Invite%20to%20try%20Quick%20N%20Easy%0D%0A");
//////                        sb.append("Quick%20N%20Easy%20is%20great");
////
////                sb.append("Quick N Easy Invite".replace(" ", "%20"));
////                sb.append("&body=");
////                sb.append("Invite to try Quick N Easy".replace(" ", "%20"));
////                sb.append("Quick N Easy is great".replace(" ", "%20"));
////
////
//////                        String message = "mailto:" + emailValue + "?subject=QuickNEasyInvite&body=Invite";
////                String message = sb.toString();
////                System.out.println(message);
////                URI uri = URI.create(message);
////                try {
////                    desktop.mail(uri);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////            }
////        });
////
////    }
////
////
////    public void registerInvite(QInviteInfo inviteInfo) {
////        invitesMap.put(inviteInfo.inviteId, inviteInfo);
////        saveInvitesMap();
////    }
////
////
////    public ArrayList<String> getRoutesList() {
////        return routesList;
////    }
////
////
////    public void putArtifactBytesInRepository(String artifactName, byte[] artifactBytes) {
////        File file = Paths.get(system.getStrSysProp(QSystemConstants.ARTIFACTS_DIR), artifactName).toFile();
////        try {
////            FileUtils.writeByteArrayToFile(file, artifactBytes);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
////    }
////
////
////    public RandomAccessFile getFileTransferRaf(String transferName) {
////        return transferRafsByTransferNameMap.get(transferName);
////    }
////
////
////    public Path getArtifactsPath() {
////        return system.getPathSysProp(QSystemConstants.ARTIFACTS_DIR);
////    }
////
////
////    public long getFileCheckSum(Path rafPath) {
////        InputStream in = null;
////        try {
////            in = new FileInputStream(rafPath.toFile());
////            CRC32 crc = new CRC32();
////            int c;
////            while (true) {
////                c = in.read();
////                if (c == -1) break;
//////                if (!((c = in.read()) != -1)) break;
////                crc.update(c);
////            }
////            return crc.getValue();
////        } catch (FileNotFoundException e) {
////            throw new RuntimeException(e);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
////    }
////
////
////    public boolean copyArtifactFromAnotherNodeIntoRpository(String artifactName) {
////        return false;
////    }
////
////
////
////    public QInviteInfo getInviteInfo(int inviteId) {
////        return invitesMap.get(inviteId);
////    }
//
///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Process ///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
////
////
////    public void processRT(QChannelMsg channelMsg) {
////        if (channelMsg.netMsg instanceof QNetMsgAddNewNodeLookup) {
////            QNetMsgAddNewNodeLookup netMsg = (QNetMsgAddNewNodeLookup) channelMsg.netMsg;
////            nodeInfo.nodeGlobalId = netMsg.nodeId;
////            nodeInfo.certificateChainBytes = netMsg.endCertificateBytes;
////            X509Certificate[] certificates = QSecurityUtils.certificateArrayFromByteArray(nodeInfo.certificateChainBytes);
////
////            system.setMyCertificate(getSSLPrivateKey(), certificates);
////
//////            Path rootPemFilepath = Paths.get(system.getQnePath().toString(), "root.cer");
////            Path rootPemFilepath = Paths.get(system.getStrSysProp(QSystemConstants.QNE_DIR), "root.cer");
////            String rootPem = QSecurityUtils.certificateToPem(certificates[2]);
////
////            try {
////                FileUtils.writeStringToFile(rootPemFilepath.toFile(), rootPem, Charset.defaultCharset());
////            } catch (IOException e) {
////                throw new RuntimeException(e);
////            }
////
//////            if (nodeInfo.isGenesis) {
//////                QInviteInfo inviteInfo = new QInviteInfo();
//////                inviteInfo.inviteId = QRandomUtils.randomIntBetween(100000, 999999);
//////                inviteInfo.mainEmailAddress = "paulf@qnenet.com";
//////                inviteInfo.knownAs = "Paul";
//////                inviteInfo.firstName = "Paul";
//////                inviteInfo.lastName = "Fraser";
//////                inviteInfo.timestamp = System.currentTimeMillis();
//////                invitesMap.put(inviteInfo.inviteId, inviteInfo);
//////                saveInvitesMap();
//////                QEndPointInfo qMember = addMember(inviteInfo); // add first endpoint
//////            }
////
////            saveNodeInfo();
////
////            System.out.println("AddNewNodeLookup -> Success");
//////            return;
////        }
//
//
////        if (channelMsg.netMsg instanceof QNetMsgSwapNetEndPointInfo) {
////            QNetMsgSwapNetEndPointInfo netMsg = (QNetMsgSwapNetEndPointInfo) channelMsg.netMsg;
////            addOrUpdateNetEndPointInfo(netMsg.replyNetEndPointInfo, true);
////            System.out.println("SwapNetEndPointInfo -> Success");
////            return;
////        }
////        if (channelMsg.netMsg instanceof QNetMsgSwapLanAddressListAndNetEndPointInfo) {
////
////            QNetMsgSwapLanAddressListAndNetEndPointInfo netMsg = (QNetMsgSwapLanAddressListAndNetEndPointInfo) channelMsg.netMsg;
////            updateLanAddessList(netMsg.replyLanAddressList);
////
////            addOrUpdateNetEndPointInfo(netMsg.replyNetEndPointInfo, true);
////
////            System.out.println("SwapLanAddressList -> Success");
////            return;
////        }
////
////        if (channelMsg.netMsg instanceof QNetMsgGetNetworkStructure) {
////            QNetMsgGetNetworkStructure netMsg = (QNetMsgGetNetworkStructure) channelMsg.netMsg;
////            networkStructure = netMsg.networkStructure;
////            saveNetworkStructure();
////
////            System.out.println("GetDiscoveryInfo -> Success");
////            return;
////        }
////    }
//
//    public void roundTripFailed(QChannelMsg channelMsg) {
////        if (channelMsg.netMsg instanceof QNetMsgAddNewNodeLookup) {
////            System.out.println("AddNewNodeLookup -> Failed");
////            //            return;
////        }
////
//////            if (channelMsg.netMsg instanceof QNetMsgSwapNetEndPointInfo) {
//////                System.out.println("SwapNetEndPointInfo -> Failed");
//////                return;
//////            }
//////            if (channelMsg.netMsg instanceof QNetMsgSwapLanAddressListAndNetEndPointInfo) {
//////                System.out.println("SwapLanAddressList -> Failed");
//////                return;
//////            }
//////            if (channelMsg.netMsg instanceof QNetMsgGetNetworkStructure) {
//////                System.out.println("GetDiscoveryInfo -> Failed");
//////            return;
//////            }
//
//
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Persistence ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void saveNodeProps() {
//        qobjs.saveObjToFile(nodePropsFilePath, nodeProps);
//    }
//
//    private void loadNodeProps() {
//        nodeProps = (Map<Object, Object>) qobjs.loadObjFromFile(nodePropsFilePath);
//    }
//
////    private Path getStatusListFilepath() {
////        if (statusListFilePath == null)
////            statusListFilePath = Paths.get(system.getStrSysProp(QSystemConstants.NODE_DIR), "status.list");
////        return statusListFilePath;
////    }
////
////    private void saveStatusList() {
////        qobjs.saveObjToFile(getStatusListFilepath(), statusList);
////    }
////
////    private void loadStatusList() {
////        statusList = (ArrayList<Integer>) qobjs.loadObjFromFile(getStatusListFilepath());
////    }
////
////
//////        private void saveKnownLanNodesList () {
//////            serialization.saveObjToFile(knownLanNodeAddressesFilePath, knownLanNodesList);
//////        }
//////
//////        private void loadKnownLanNodesList () {
//////            knownLanNodesList = (ArrayList<String>) serialization.loadObjFromFile(knownLanNodeAddressesFilePath);
//////        }
////
////
////    private void saveKnownNodesMap() {
////        qobjs.saveObjToFile(knownNodesMapFilePath, knownNodesMap);
////    }
////
////    private void loadKnownNodesMap() {
////        knownNodesMap = (ConcurrentHashMap<Long, QNetAddressPair>) qobjs.loadObjFromFile(knownNodesMapFilePath);
////    }
////
////    private void saveNicknamesMap() {
////        qobjs.saveObjToFile(nicknamesMapFilePath, nicknamesMap);
////    }
////
////    private void loadNicknamesMap() {
////        nicknamesMap = (Map<String, QNetAddressPair>) qobjs.loadObjFromFile(nicknamesMapFilePath);
////    }
////
////    private void saveInvitesMap() {
////        qobjs.saveObjToEncFile(invitesMapFilePath, invitesMap, nodeInfo.masterEncryptor);
////    }
////
////    private void loadInvitesMap() {
////        invitesMap = (ConcurrentHashMap<Integer, QInviteInfo>) qobjs.loadObjFromEncFile(invitesMapFilePath, nodeInfo.masterEncryptor);
////    }
////
////    private void saveRoutesList() {
////        qobjs.saveObjToFile(routesListFilePath, routesList);
////    }
////
////    private void loadRoutesList() {
////        routesList = (ArrayList<String>) qobjs.loadObjFromFile(routesListFilePath);
////    }
////
////    public Properties getSysProps() {
////        return sysProps;
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} /////// End Class ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
