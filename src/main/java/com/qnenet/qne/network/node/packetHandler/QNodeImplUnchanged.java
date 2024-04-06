//package com.qnenet.qne.node.packetHandler;//package com.qnenet.qne.qnode.provider.packetHandler;
//
//import com.qnenet.qne.qneapi.classes.*;
//import com.qnenet.qne.qneapi.constants.QSystemConstants;
//import com.qnenet.qne.qneapi.intf.*;
//import com.qnenet.qne.qnetmsgs.QNetMsgAddNewNodeLookup;
//import com.qnenet.qne.qnetmsgs.QNetMsgSwapNetEndPointInfo;
//import com.qnenet.qne.qneutils.*;
//import com.qnenet.qne.qnoiseprotocol.appendStore.QNoiseProtocol;
//import com.qnenet.qne.qnoiseprotocol.appendStore.p1.HandshakeState;
//import com.qnenet.qne.qserialization.appendStore.QSerialization;
//import com.qnenet.qne.qsystem.appendStore.QSystem;
//import com.qnenet.qne.qsystemmanager.appendStore.QSystemManager;
//import org.apache.commons.io.FileUtils;
//import org.jasypt.util.binary.AES256BinaryEncryptor;
//import org.osgi.service.component.ComponentServiceObjects;
//import org.osgi.service.component.annotations.Activate;
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Deactivate;
//import org.osgi.service.component.annotations.Reference;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.net.DatagramSocket;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.*;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Class /////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//@Component(immediate = true, service = QNode.class)
//public class QNodeImplUnchanged implements QNode, QNetMsgOrigin {
//
//    private static QNodeImplUnchanged INSTANCE;
//    Logger log = LoggerFactory.getLogger(QNodeImplUnchanged.class);
//
//    @Reference
//    ComponentServiceObjects<QChannel> channelFactory;
//
//    @Reference
//    ComponentServiceObjects<QEndPointInfo> memberFactory;
//
//    @Reference
//    QSystem system;
//
//    @Reference
//    QSystemManager systemManager;
//
//    @Reference
//    QSerialization serialization;
//
//
//    @Reference
//    QPacketHandler packetHandler;
//
//    @Reference
//    QNoiseProtocol noise;
//
//    private Path nodeInfoFilePath;
//    private Path nodePath;
//    private Path knownLanNodeAddressesFilePath;
//    private Path objEntryIdsFilePath;
//    private Path knownNetEndPointInfoMapFilePath;
//    private Path networkStructureFilePath;
//    private Path nicknamesMapFilePath;
//    private Path membersPath;
////    private Path pinsFilePath;
//
//    private QNodeInfo nodeInfo;
//    private AES256BinaryEncryptor encryptor;
////    private AES256BinaryEncryptor nodeInfoEncryptor;
//
//    private Map<String, Object> registeredObjectMap = new ConcurrentHashMap<>();
//    private Map<Integer, QChannel> clientChannelsMap = new ConcurrentHashMap<>();
//    private Map<Integer, QChannel> serverChannelsMap = new ConcurrentHashMap<>();
//    private Map<Integer, QChannelMsg> channelMsgsByRTId = new ConcurrentHashMap<>();
//
//    private Map<UUID, QNetEndPointInfo> knownNetEndPointInfoMap;
//    private Map<String, UUID> nicknamesMap;
////    private Map<Integer, Object> pins;
//
//
//    private boolean isRunning;
////    private ArrayList<String> localNodesPublicNetEndPointInfoMap;
//
//    //    private ArrayList<String> knownLanNodesList;
//    private DatagramSocket announceSocket;
//    private Thread announceListenThread;
//
//    private ConcurrentHashMap<Integer, QEndPointInfo> members = new ConcurrentHashMap<>();
//
//
//    //    private Path knownPath;
//    private QNetworkStructure networkStructure;
//    private QNetEndPointInfo selfNetEndPointInfo;
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Activate //////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Activate
//    public void activate() throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
//        setupPaths();
////        packetHandler.setNode(this);
//
//        if (Files.notExists(nodeInfoFilePath)) {
//            newSystem();
//        } else {
//            restart();
//        }
//
////        if (!nodeInfo.isHeadless) {
////            startAnnounceListener();
////            announce();
////        }
//
////        packetHandler.startListener(this);
//
////        installUPnPIfNecessary();
//
////        known.setNode(this);
//
////        localNodesPublicNetEndPointInfoMap = known.getKnownLanNodesList();
//
//        log.info("Hello from -> " + getClass().getSimpleName());
//    }
//
//
//    @Deactivate
//    public void deactivate() throws InterruptedException {
////        packetHandler.stopListener();
//        log.info("Goodbye from -> " + getClass().getSimpleName());
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void printMsg(String msg) {
//        System.out.println(msg);
//        log.info(msg);
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// New System ////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
//    public void newSystem() {
//        Properties sysProps = system.getSysProps();
//        QFileUtils.checkDirectory(nodePath);
//        nodeInfo = new QNodeInfo();
//
//        nodeInfo.uuid = UUID.randomUUID();
//        nodeInfo.name = "$" + QRandomUtils.generateRandomName(4);
//
//        nodeInfo.statusList = new ArrayList<>();
//        nodeInfo.statusList.add(QSystemConstants.NODE_STATUS_NEW_INSTALL);
//
////        nodeInfo.port = Integer.valueOf(sysProps.getProperty(QSystemConstants.PORT));
////
//////        nodeInfo.port = QNetworkUtils.getFreePort();
////////        nodeInfo.port = 54081;
//
//        convertSysProps(sysProps);
//
////        nodeInfo.segmentItem = new QNetEndPointInfo();
////        nodeInfo.segmentItem.uuid = nodeInfo.uuid;
////        nodeInfo.segmentItem.name = nodeInfo.name;
////        if (nodeInfo.testUseLanAdddress) nodeInfo.segmentItem.netAddress = nodeInfo.netAddressPair.lanNetAddress;
////        else nodeInfo.segmentItem.netAddress = nodeInfo.netAddressPair.wanNetAddress;
////        nodeInfo.segmentItem.netAddress = nodeInfo.netAddressPair.wanNetAddress;
////        nodeInfo.segmentItem.installIdx = 0;
////        nodeInfo.segmentItem.statusList = new ArrayList<>();
//
//        KeyPair ecdsaKeyPair = QSecurityUtils.createECDSAKeyPair("P-256");
//
////        nodeInfo.sslPrivateKey = ecdsaKeyPair.getPrivate();
//        nodeInfo.sslPrivateKeyEncoded = ecdsaKeyPair.getPrivate().getEncoded();
//        nodeInfo.sslPublicKeyEncoded = ecdsaKeyPair.getPublic().getEncoded();
//
////        PrivateKey ecdsa = QSecurityUtils.ecdsaPrivateKeyFromBytes(nodeInfo.sslPrivateKeyEncoded);
//
////        nodeInfo.sslPublicKeyPEM = QSecurityUtils.ecdsaPublicKeyToPEM(ecdsaKeyPair.getPublic());
//
//        nodeInfo.nodePathStr = nodePath.toString();
//        nodeInfo.noiseKeypair = noise.createNoiseKeypair();
////
////
////        nodeInfo.masterPassword = QSecurityUtils.generatePasswordChars(16);
////        nodeInfo.masterEncryptor = new AES256BinaryEncryptor();
////        nodeInfo.masterEncryptor.setPasswordCharArray(nodeInfo.masterPassword);
////
////        nodeInfo.passwordPin = QRandomUtils.randomIntBetween(1000, 9999);
//
//        nodeInfo.littlePasswordList = new ArrayList<>();
//        nodeInfo.memberPinsList = new ArrayList<>();
//
//
////        Path nodeStorePath = Paths.get(nodeInfo.nodePathStr, "nodeStore");
////        nodeStore.init(nodeStorePath, QSystemConstants.NODE_STORE_NAME, QSystemConstants.NODE_STORE_SEGMENT_CAPACITY, encryptor);
//
//        char[] littlePassStr = QSecurityUtils.generateLittlePassword(4);
//
//        hideLittlePassChars(littlePassStr);
//
//        saveNodeInfo();
//
////        knownLanNodesList = new ArrayList<>();
//        knownNetEndPointInfoMap = new ConcurrentHashMap<>();
//        nicknamesMap = new ConcurrentHashMap<>();
//
////        saveKnownLanNodesList();
//
//        QNetEndPointInfo selfNetEndPointInfo = new QNetEndPointInfo(nodeInfo);
//        addOrUpdateNetEndPointInfo(selfNetEndPointInfo, true);
//        addNickname(QSystemConstants.MY_NICKNAME, selfNetEndPointInfo.uuid);
//
//        saveKnownNetEndPointInfoMap();
//        saveNicknamesMap();
//
//        packetHandler.startListener(this);
//
////        nodeInfo.nodelookupIdx = 0;
//
//        if (nodeInfo.port != 26055) {
//            addNodeToDiscoveryServer();
//        }
//
////        for (int i = 0; i < 3; i++) {
////        QEndPointInfo qMember = addMember(); // add first endpoint
//
////        pins = new ConcurrentHashMap<>();
////        }
//
//
//    }
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
//    private void addNodeToDiscoveryServer() {
//        QNetMsgAddNewNodeLookup netMsg = new QNetMsgAddNewNodeLookup();
//        netMsg.uuid = nodeInfo.uuid;
//        netMsg.netAddressPair = nodeInfo.netAddressPair;
//        netMsg.sslPublicKeyEncoded = nodeInfo.sslPublicKeyEncoded;
//        netMsg.origin = this;
//        netMsg.keepAlive = false;
//        netMsg.destQNetAddress = getRandomDiscoveryServerNetAddress();
//        netMsg.srcIdx = -1;
//        netMsg.destIdx = -1;
//        doRoundTripRequest(netMsg);
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Restart ///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void restart() {
//        loadNodeInfo();
//        loadKnownNetEndPointInfoMap();
////        loadKnownLanNodesList();
//        loadNicknamesMap();
//        loadNetworkStructure();
////        nodeInfo.masterEncryptor = new AES256BinaryEncryptor();
////        nodeInfo.masterEncryptor.setPasswordCharArray(nodeInfo.masterPassword);
//        nodeInfo.sslPrivateKey = QSecurityUtils.ecdsaPrivateKeyFromBytes(nodeInfo.sslPrivateKeyEncoded);
//
//        for (int i = 0; i < nodeInfo.littlePasswordList.size(); i++) {
//            char[] littlePassword = nodeInfo.littlePasswordList.get(i);
//            if (littlePassword == null) {
//                members.put(i, null);
//                continue;
//            }
//
//            Path memberPath = Paths.get(membersPath.toString(), QStringUtils.int4(i));
//            QEndPointInfo endpoint = memberFactory.getService();
//            endpoint.restartInit(this, littlePassword, memberPath);
//            members.put(i, endpoint);
//        }
//
//        packetHandler.startListener(this);
//
////        installUPnPIfNecessary();
//    }
//
//    private void installUPnPIfNecessary() {
//        if (!nodeInfo.isHeadless) {
//            systemManager.installArtifacts("addonupnp-0.0.1.kar");
//        }
//    }
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
//    private void convertSysProps(Properties sysProps) {
//        nodeInfo.cpuCount = Integer.valueOf(sysProps.getProperty("machine.cpu.count"));
//        nodeInfo.cpuSpeed = Integer.valueOf(sysProps.getProperty("machine.cpu.speed"));
//        nodeInfo.swapMemory = Integer.valueOf(sysProps.getProperty("machine.swap.memory"));
//        nodeInfo.totalMemory = Integer.valueOf(sysProps.getProperty("machine.total.memory"));
//        nodeInfo.multipleProcessors = Integer.valueOf(sysProps.getProperty("platform.has.multiple.processors"));
//
//        nodeInfo.machineIpAddress = sysProps.getProperty("machine.ip.address");
//
//        String wanAddress = sysProps.getProperty("wan.ip.address");
//        String lanAddress = sysProps.getProperty("lan.ip.address");
//
//        nodeInfo.port = Integer.valueOf(sysProps.getProperty(QSystemConstants.PORT));
//
//        nodeInfo.netAddressPair = new QNetAddressPair();
//        nodeInfo.netAddressPair.wanNetAddress = new QNetAddress(wanAddress, nodeInfo.port);
//        if (lanAddress != null)
//            nodeInfo.netAddressPair.lanNetAddress = new QNetAddress(lanAddress, nodeInfo.port);
//
//        nodeInfo.platformName = sysProps.getProperty("platform.name");
//        nodeInfo.systemLocale = sysProps.getProperty("system.locale");
//        nodeInfo.isHeadless = QConvertUtils.getBooleanFromString(sysProps.getProperty(QSystemConstants.IS_HEADLESS));
//
//        nodeInfo.testUseLanAdddress = QConvertUtils.getBooleanFromString(sysProps.getProperty(QSystemConstants.TEST_USE_LAN_ADDRESS));
//
//        String osxMV = sysProps.getProperty("osx.major.version");
//        if (!osxMV.contains("***")) {
//            nodeInfo.osxMajorVersion = osxMV;
//        }
//
//        String osxV = sysProps.getProperty("osx.version");
//        if (!osxV.contains("***")) {
//            nodeInfo.osxVersion = osxV;
//        }
//
//        String winOSName = sysProps.getProperty("windows.os.name");
//        if (!winOSName.contains("***")) {
//            nodeInfo.windowsOSName = winOSName;
//        }
//
//        nodeInfo.isProduction = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.IS_PRODUCTION)); //"true");
//        nodeInfo.isNewSystem = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.DO_NEW_SYSTEM)); //"n");
//        nodeInfo.useStartupArtifacts = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.USE_STARTUP_ARTIFACTS)); //"false");
//        nodeInfo.startupArtifacts = (String) sysProps.get(QSystemConstants.STARTUP_ARTIFACTS);
//        nodeInfo.bundleWatch = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.BUNDLE_WATCH)); //"true");
//        nodeInfo.isQNEWebsite = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.IS_QNE_WEBSITE)); //"false");
//        nodeInfo.doSocketTimeOut = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.DO_SOCKET_TIMEOUT)); //"n");
//        nodeInfo.sayConsolePrint = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.SAY_CONSOLE_PRINT)); //"true");
//        nodeInfo.sayLog = QConvertUtils.getBooleanFromString((String) sysProps.get(QSystemConstants.SAY_LOG)); //"true");
//        nodeInfo.debugLevel = (String) sysProps.get(QSystemConstants.DEBUG_LEVEL); //"warn");
//        nodeInfo.karafDir = (String) sysProps.get(QSystemConstants.KARAF_DIR); //"/home/paulf/QNE-DEV-4.3.6/karaf");
//        nodeInfo.installerPathName = (String) sysProps.get(QSystemConstants.INSTALLER_PATH_NAME);
//        nodeInfo.karafFeaturesConfigKey = (String) sysProps.get(QSystemConstants.KARAF_FEATURES_CONFIG_KEY); //"com.qnenet.qne.runprops");
//        nodeInfo.systemUserName = (String) sysProps.get(QSystemConstants.SYSTEM_USER_NAME); //"root");
//        nodeInfo.systemTmpDir = (String) sysProps.get(QSystemConstants.SYSTEM_TMP_DIRECTORY); //"/tmp");
//        nodeInfo.javaDir = (String) sysProps.get(QSystemConstants.JAVA_DIR); //"/home/paulf/QNE-DEV-4.3.6/java");
//        nodeInfo.installerDir = (String) sysProps.get(QSystemConstants.INSTALLER_DIR); //"/home/paulf/Dropbox/software/installbuilder/installbuilder-23.1.0-linux/output");
//
//        nodeInfo.discoveryServerListString = (String) sysProps.get(QSystemConstants.DISCOVERY_SERVERS); //"6b7b5c5c-a868-4371-b438-945bed3f19b9;5.78.44.229:49533");
//
//
//        nodeInfo.installDir = (String) sysProps.get(QSystemConstants.INSTALL_DIR); //"/home/paulf/QNE-DEV-4.3.6");
//        nodeInfo.platformInstallerPrefix = (String) sysProps.get(QSystemConstants.PLATFORM_INSTALLER_PREFIX); //"/opt");
//        nodeInfo.machineHostName = (String) sysProps.get(QSystemConstants.MACHINE_HOST_NAME); //"paulf-S550CB");
//        nodeInfo.sslKeyFactoryAlgorithm = (String) sysProps.get(QSystemConstants.SSL_KEY_INFO); //"rsa:2048");
//        nodeInfo.machineFQDN = (String) sysProps.get(QSystemConstants.MACHINE_FQDN); //"paulf-S550CB");
//        nodeInfo.platformExecSuffix = (String) sysProps.get(QSystemConstants.PLATFORM_EXEC_SUFFIX); //"run");
//        nodeInfo.userHomeDir = (String) sysProps.get(QSystemConstants.USER_HOME_DIR); //"/home/paulf");
//        nodeInfo.user = (String) sysProps.get(QSystemConstants.USER); //"paulf");
//        nodeInfo.platformPathSeparator = (String) sysProps.get(QSystemConstants.PLATFORM_PATH_SEPARATOR); //"/");
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Members ///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
//    public QEndPointInfo addMember() {
//        QEndPointInfo endpoint = memberFactory.getService();
//        endpoint.initNew(this);
//        return endpoint;
//    }
//
//
///////////////////////////////////////////////////////////////////////////////////////////////////
///////// Round trips ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
//    public void doRoundTripRequest(QNetMsg netMsg) {
//        if (netMsg.destUUID != null) {
//            QNetEndPointInfo qNetEndPointInfo = knownNetEndPointInfoMap.get(netMsg.destUUID);
//            netMsg.destQNetAddress = qNetEndPointInfo.useNetAddress;
//            doRoundTripRequestInternal(netMsg);
//        } else if (netMsg.destNickName != null) {
//            UUID destUUID = nicknamesMap.get(netMsg.destNickName);
//            QNetEndPointInfo qNetEndPointInfo = knownNetEndPointInfoMap.get(destUUID);
//            netMsg.destQNetAddress = qNetEndPointInfo.useNetAddress;
//            doRoundTripRequestInternal(netMsg);
//        } else if (netMsg.destQNetAddress != null) {
//            doRoundTripRequestInternal(netMsg);
//        }
//    }
//
//
////    private void doRoundTripRequest(QNetEndPointInfo endPointInfo, QNetMsg netMsg) {
////        netMsg.destQNetAddress = endPointInfo.useNetAddress;
////        doRoundTripRequest(netMsg);
////    }
//
//    private void doRoundTripRequestInternal(QNetMsg netMsg) {
//        QChannelMsg channelMsg = new QChannelMsg();
//        channelMsg.netAddress = netMsg.destQNetAddress;
//        channelMsg.channelId = getUniqueChannelId();
//        channelMsg.role = HandshakeState.INITIATOR;
//        channelMsg.keepAlive = netMsg.keepAlive;
//        channelMsg.rtId = getUniqueRTId();
//        channelMsg.srcIdx = netMsg.srcIdx;
//        channelMsg.destIdx = netMsg.destIdx;
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
//            clientChannel.sendChannelMsg(channelMsg);
//        }
//    }
//
//
////    @Override
////    public QNetworkStructure getNetworkStructure() {
////        return known.getNetworkStructure();
////    }
//
////    @Override
////    public void saveObject(Object obj) {
////        appendStore.addObject(obj);
////    }
//
////    @Override
////    public UUID getUUID() {
////        return nodeInfo.netEndPointInfo.publicNetEndPointInfo.uuid;
////    }
////
////    @Override
////    public QPublicNetEndPointInfo getPublicNetEndPointInfo() {
////        return nodeInfo.netEndPointInfo.publicNetEndPointInfo;
////    }
////
////    @Override
////    public QNetEndPointInfo getMyNetEndPointInfo() {
////        return nodeInfo.netEndPointInfo;
////    }
////
////    @Override
////    public QPublicNetEndPointInfo getMyPublicNetEndPointInfo() {
////        return nodeInfo.netEndPointInfo.publicNetEndPointInfo;
////    }
//
//
////    @Override
////    public void setMyCertificate(byte[][] endCertificateBytes) {
////        X509Certificate[] certificateArray = QSecurityUtils.certificateArrayFromByteArray(endCertificateBytes);
////        PrivateKey privateKey = QSecurityUtils.privateKeyFromBytes(nodeInfo.sslPrivateKeyBytes, nodeInfo.sslKeyType);
////        KeyStore keyStore = system.getKeystore();
////        keyStore.setKeyEntry("myCertificate", privateKey, system.getKeyPwd(), certificateArray);
////        system.saveKeystore(); //QSecurityUtils.saveKeyStore(keystoreFilePath, keyStore, getKeyStorePwd());
////
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Get Channel ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    private QChannel getOrCreateClientChannel(QChannelMsg channelMsg) {
//        QChannel clientChannel = clientChannelsMap.get(channelMsg.channelId);
//        if (clientChannel != null) return clientChannel;
////        return getNewClientChannel(channelMsg);
////    }
////
////    @Override
////    public QChannel getNewClientChannel(QChannelMsg channelMsg) {
//        QChannel channel = channelFactory.getService();
//        channel.setChannelId(channelMsg.channelId);
//        channel.setFirstMessage(channelMsg.netMsg);
//        channel.setNode(this);
//        channel.setIsNew(true);
//        channel.setDestinationNetAddress(channelMsg.netAddress);
//        channel.setRole(HandshakeState.INITIATOR);
//        channel.setDisplayName("CLIENT");
//        channel.setSrcIdx(channelMsg.srcIdx);
//        channel.setDestIdx(channelMsg.destIdx);
//        clientChannelsMap.put(channelMsg.channelId, channel);
//        return channel;
//    }
//
//    @Override
//    public QNetMsgOrigin getChannelMsgOrigin(QChannelMsg channelMsg) {
//        QChannelMsg sentChannelMsg = channelMsgsByRTId.get(channelMsg.rtId);
//        return sentChannelMsg.netMsg.origin;
//    }
//
//
//    @Override
//    public QChannel getClientChannel(int channelId) {
//        return clientChannelsMap.get(channelId);
//    }
//
//    @Override
//    public void removeClientChannel(QChannel channel) {
//        int channelId = channel.getChannelId();
//        channelFactory.ungetService(channel);
//        clientChannelsMap.remove(channelId);
//    }
//
//    @Override
//    public void removeClientChannelIfNecessary(QChannelMsg channelMsg) {
//        if (!channelMsg.keepAlive) removeClientChannel(channelMsg.channel);
//    }
//
//    @Override
//    public void processChannelMsg(QChannelMsg channelMsg) {
//        getChannelMsgOrigin(channelMsg).processRT(channelMsg);
//        removeClientChannelIfNecessary(channelMsg);
//    }
//
//
//    @Override
//    public void removeServerChannel(QChannel channel) {
//        int channelId = channel.getChannelId();
//        channelFactory.ungetService(channel);
//        serverChannelsMap.remove(channelId);
//    }
//
//    @Override
//    public void registerChannelMsgRT(QChannelMsg channelMsg) {
//        channelMsgsByRTId.put(channelMsg.rtId, channelMsg);
//    }
//
//    @Override
//    public void unregisterChannelMsgRT(int rtId) {
//        channelMsgsByRTId.remove(rtId);
//    }
//
//
//    @Override
//    public QChannel getOrCreateServerChannel(QChannelMsg channelMsg) {
//        int channelId = channelMsg.channelId;
//        QChannel channel = serverChannelsMap.get(channelId);
//        if (channel != null) return channel;
//
//        channel = channelFactory.getService();
//        channel.setChannelId(channelId);
////        channel.setSrcUUID(channelMsg.responderUUID);
////        channel.setDestUUID(channelMsg.initiatorUUID);
//        channel.setNode(this);
//        channel.setIsNew(true);
//        channel.setDestinationNetAddress(channelMsg.netAddress);
//
//        channel.setRole(HandshakeState.RESPONDER);
//        channel.setDisplayName("SERVER");
//        channel.setSrcIdx(channelMsg.srcIdx);
//        channel.setDestIdx(channelMsg.destIdx);
//        serverChannelsMap.put(channelId, channel);
//        return channel;
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Register Object ///////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
//    public void registerObject(String key, Object object) {
//        registeredObjectMap.put(key, object);
//    }
//
//    @Override
//    public void unregisterObject(String key) {
//        registeredObjectMap.remove(key);
//    }
//
//    @Override
//    public Object getRegisteredObject(String key) {
//        return registeredObjectMap.get(key);
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Ports /////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void openPorts() {
////        QPortMapping portMapping = new QPortMapping();
////
////        portMapping.externalPort = nodeInfo.wanNetAddress.port;
////        portMapping.internalPort = nodeInfo.lanNetAddress.port;
////        portMapping.internalClient = (String) system.getsystemInfoMap().get(QSystemConstants.LAN_IP_ADDRESS);
////        portMapping.protocol = "UDP";
////        portMapping.description = "QNE-" + nodeInfo.loginName;
////        portMapping.enabled = true;
////        portMapping.leaseDurationSeconds = 0;
////        portMapping.remoteHost = null;
////
////        uPnP.openPort(portMapping, nodeInfo.loginName);
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Getters & Setters /////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
////    @Override
////    public Timer getChannelMsgTimer() {
////        return channelMsgTimer;
////    }
//
////    @Override
////    public void setSegmentIdAndSave(int segmentId) {
////        nodeInfo.segmentId = segmentId;
////        system.getExecutor().submit(new Runnable() {
////            @Override
////            public void run() {
////                saveNodeInfo();
////            }
////        });
////    }
////
////    @Override
////    public int getSegmentId() {
////        return nodeInfo.segmentId;
////    }
//
//    @Override
//    public QPacketHandler getPacketHandler() {
//        return packetHandler;
//    }
//
//
//    // sets networkStructure in repository/nodeKeepData
//    @Override
//    public void setNetworkStructure(QNetworkStructure networkStructure) {
//        this.networkStructure = networkStructure;
//        saveNetworkStructure();
//    }
//
//    @Override
//    public AES256BinaryEncryptor getEncryptor() {
//        return encryptor;
//    }
//
////    @Override
////    public void setUUID(UUID uuid) {
////        nodeInfo.publicNetEndPointInfo.uuid = uuid;
////        saveNodeInfo();
////    }
//
////    @Override
////    public QNetEndPointInfo getNetEndPointInfoByUUID(UUID uuid) {
////        return known.getNetEndPointInfoByUUID(uuid);
////    }
//
////    @Override
////    public void forcePort(int port) {
////        packetHandler.stopListener();
////        nodeInfo.port = port;
////        system.getExecutor().submit(new Runnable() {
////            @Override
////            public void run() {
////                saveNodeInfo();
////            }
////        });
////        packetHandler.startListener(this);
////    }
//
//
//    private void printPrettyMsg(String msg, byte[] bytes) {
//        printMsg("NODE " + msg + " -> " + QStringUtils.prettyArrayBytes(bytes));
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Paths /////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    void setupPaths() {
//        nodePath = system.getNodePath();
//        nodeInfoFilePath = Paths.get(nodePath.toString(), "node.info");
//
//        knownLanNodeAddressesFilePath = Paths.get(nodePath.toString(), "knownLanNodeAddresses.list");
//
//        objEntryIdsFilePath = Paths.get(nodePath.toString(), "objEntryIds.props");
//
//        knownNetEndPointInfoMapFilePath = Paths.get(nodePath.toString(), "knownNetEndPointInfo.map");
//        networkStructureFilePath = Paths.get(nodePath.toString(), "networkStructure");
//        nicknamesMapFilePath = Paths.get(nodePath.toString(), "nicknames.map");
////        pinsFilePath = Paths.get(nodePath.toString(), "pins.map");
//
//        membersPath = Paths.get(system.getWorkPath().toString(), "members");
//
//
//    }
//
//
//    @Override
//    public byte[] getNoisePrivateKeyClone() {
//        return nodeInfo.noiseKeypair.privateKeyBytes.clone();
//    }
//
//    @Override
//    public byte[] getNoisePublicKeyClone() {
//        return nodeInfo.noiseKeypair.publicKeyBytes.clone();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Register Channels, Channels & Packets /////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
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
//    @Override
//    public int getUniqueRTId() {
//        int rtId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
//        if (!channelMsgsByRTId.containsKey(rtId)) return rtId;
//        return getUniqueRTId();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//////////// serialization //////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    @Override
////    public byte[] objToBytes(Object obj) {
////        return serialization.objToBytes(obj);
////    }
////
////    @Override
////    public Object objFromBytes(byte[] bytes) {
////        return serialization.objFromBytes(bytes);
////    }
////
////    @Override
////    public void saveObjToFile(Path filePath, Object object) {
////        serialization.saveObjToFile(filePath, object);
////    }
////
////    @Override
////    public Object loadObjFromFile(Path filePath) {
////        return serialization.loadObjFromFile(filePath);
////    }
////
////    @Override
////    public byte[] objToEncBytes(Object obj) {
////        return serialization.objToEncBytes(obj, encryptor);
////    }
////
////    @Override
////    public Object objFromEncBytes(byte[] bytes) {
////        return serialization.objFromEncBytes(bytes, encryptor);
////    }
////
////    @Override
////    public boolean saveObjToEncFile(Path filePath, Object obj) {
////        return serialization.saveObjToEncFile(filePath, obj, encryptor);
////    }
////
////    @Override
////    public Object loadObjFromEncFile(Path filePath) {
////        return serialization.loadObjFromEncFile(filePath, encryptor);
////    }
////
////    @Override
////    public int getClassIdForObject(Object obj) {
////        return serialization.getClassIdForObject(obj);
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Getters & Setters /////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
//    public QNodeInfo getNodeInfo() {
//        return nodeInfo;
//    }
//
//    //    @Override
//
////    @Override
////    public ArrayList<String> getKnownLanNodesList() {
////        return known.getKnownLanNodesList();
////    }
//
////    @Override
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
//
//    @Override
//    public QNetAddress getRandomDiscoveryServerNetAddress() {
//        String[] split = nodeInfo.discoveryServerListString.split(";");
//        String discAddressStr = split[QRandomUtils.randomIntBetween(0, split.length - 1)];
//        String[] split1 = discAddressStr.split(":");
//        return new QNetAddress(split1[0], Integer.valueOf(split1[1]));
//    }
//
//    @Override
//    public String getWanAddressStr() {
//        return nodeInfo.netAddressPair.wanNetAddress.ipAddress + ":" +
//                String.valueOf(nodeInfo.netAddressPair.wanNetAddress.port);
//    }
//
//    @Override
//    public String getLanAddressStr() {
//        return nodeInfo.netAddressPair.lanNetAddress.ipAddress + ":" +
//                String.valueOf(nodeInfo.netAddressPair.lanNetAddress.port);
//    }
//
//    @Override
//    public PrivateKey getSSLPrivateKey() {
//        if (nodeInfo.sslPrivateKey == null) {
//            nodeInfo.sslPrivateKey = QSecurityUtils.ecdsaPrivateKeyFromBytes(nodeInfo.sslPrivateKeyEncoded);
//        }
//        return nodeInfo.sslPrivateKey;
//    }
//
//    private void setUseAddress(QNetEndPointInfo netEndPointInfo) {
////        String systemWanIpAddress = netEndPointInfo.netAddressPair.wanNetAddress.ipAddress;
////        if (systemWanIpAddress.equals(netEndPointInfo.netAddressPair.wanNetAddress.ipAddress)) { // same lan
////            if (netEndPointInfo.netAddressPair.lanNetAddress != null) {
////                netEndPointInfo.useNetAddress = netEndPointInfo.netAddressPair.lanNetAddress;
////            } else {
////                netEndPointInfo.useNetAddress = netEndPointInfo.netAddressPair.wanNetAddress;
////            }
////        } else {
////            netEndPointInfo.useNetAddress = netEndPointInfo.netAddressPair.wanNetAddress;
////        }
//    }
//
//    @Override
//    public Path getNodePath() {
//        return nodePath;
//    }
//
//    @Override
//    public Path getMembersPath() {
//        return membersPath;
//    }
//
//
////    @Override
////    public void addNetEndPointInfo(QNetEndPointInfo knownInfo) {
////        known.addOrUpdateNetEndPointInfo(knownInfo, true);
////    }
////
////    @Override
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
////    @Override
////    public void sendEmail(ArrayList<String> toList, ArrayList<String> ccList, ArrayList<String> bccList, String subject, String body) {
////        try {
////            emailSend.sendQNEEmail(this, toList, ccList, bccList, subject, body);
////        } catch (AddressException e) {
////            throw new RuntimeException(e);
////        }
////    }
//
////    @Override
////    public void saveUserAnswerSet(QAnswerSet questionaaireAnswerSet) {
////        questionAnswerSets.put(questionaaireAnswerSet.getQuestionsetName(), questionaaireAnswerSet);
////        saveQuestionaireAnswerSets();
////    }
//
////    @Override
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
//    @Override
//    public void addOrUpdateNetEndPointInfo(QNetEndPointInfo netEndPointInfo, boolean doSave) {
//        knownNetEndPointInfoMap.put(netEndPointInfo.uuid, netEndPointInfo);
//        if (doSave) saveKnownNetEndPointInfoMap();
//    }
//
////    @Override
////    public void addOrUpdateLanNetEndPointInfo(QNetEndPointInfo netEndPointInfo) {
////
////    }
//
//    @Override
//    public QNetEndPointInfo getSelfNetEndPointInfo() {
//        if (selfNetEndPointInfo == null) {
//            UUID uuid = nicknamesMap.get(QSystemConstants.MY_NICKNAME);
//            selfNetEndPointInfo = knownNetEndPointInfoMap.get(uuid);
//        }
//        return selfNetEndPointInfo;
//    }
//
//
//    @Override
//    public QNetEndPointInfo getNetEndPointInfoByUUID(UUID uuid) {
//        QNetEndPointInfo netEndPointInfo = knownNetEndPointInfoMap.get(uuid);
//        setUseAddress(netEndPointInfo);
//        return netEndPointInfo;
//    }
//
//    @Override
//    public void removeNetEndPointInfo(UUID uuid) {
//        knownNetEndPointInfoMap.remove(uuid);
//        saveKnownNetEndPointInfoMap();
//    }
//
//    @Override
//    public QEndPointInfo getMember(int installIdx) {
//        return members.get(installIdx);
//    }
//
//    @Override
//    public void registerMember(int installIdx, QEndPointInfo endpoint) {
//        members.put(installIdx, endpoint);
//    }
//
//    @Override
//    public boolean checkIfEmailAlreadyInvited(String email) {
//        for (QEndPointInfo endpoint : members.values()) {
//            if (endpoint.getMemberInfo().email.equals(email)) return true;
//        }
//        return false;
//    }
//
//    @Override
//    public QEndPointInfo findMemberByPin(int pin) {
//        for (int i = 0; i < nodeInfo.memberPinsList.size(); i++) {
//            if (nodeInfo.memberPinsList.get(i) != pin) continue;
//            return members.get(i);
//        }
//        return null;
//    }
//
//    @Override
//    public boolean setMemberPin(int installIdx) {
//        int pin = getFreeMemberPin();
//        if (installIdx != nodeInfo.memberPinsList.size()) return false;
//        nodeInfo.memberPinsList.add(pin);
//        saveNodeInfo();
//        return true;
//    }
//
//    private int getFreeMemberPin() {
//        return 123456;
////        for (int i = 0; i < 10000; i++) {
////            int pin = QRandomUtils.randomIntBetween(100000, 999999);
////            if (nodeInfo.memberPinsList.contains(pin)) getFreeMemberPin();
////            return pin;
////        }
////        return 0;
//    }
//
////    @Override
////    public QEndPointInfo findByPin(int pin) {
////        return members.;
////    }
//
////    @Override
////    public void addNetEndPointInfoComplete(QNetEndPointInfo netEndPointInfo) {
////
////    }
////
////    @Override
////    public QNetEndPointInfo getSelfNetEndPointInfoComplete() {
////
////        return null;
////    }
//
//    @Override
//    public void swapNetEndPointInfo(QNetAddress netAddr) {
//        QNetMsgSwapNetEndPointInfo swapMsg = new QNetMsgSwapNetEndPointInfo();
//        swapMsg.sendNetEndPointInfo = getSelfNetEndPointInfo();
//        swapMsg.origin = this;
//        swapMsg.destQNetAddress = netAddr;
//        doRoundTripRequest(swapMsg);
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// NickNames ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
//    public void addNickname(String nickName, UUID uuid) {
//        nicknamesMap.put(nickName, uuid);
//        saveNicknamesMap();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Process ///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
//    public void processRT(QChannelMsg channelMsg) {
//        if (channelMsg.netMsg instanceof QNetMsgAddNewNodeLookup) {
//            QNetMsgAddNewNodeLookup netMsg = (QNetMsgAddNewNodeLookup) channelMsg.netMsg;
//            nodeInfo.nodelookupIdx = netMsg.nodeIdx;
//            nodeInfo.certificateChainBytes = netMsg.endCertificateBytes;
//            saveNodeInfo();
//            System.out.println("AddNewNodeLookup -> Success");
////            return;
//        }
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
//    }
//
//    @Override
//    public void roundTripFailed(QChannelMsg channelMsg) {
//        if (channelMsg.netMsg instanceof QNetMsgAddNewNodeLookup) {
//            System.out.println("AddNewNodeLookup -> Failed");
//            //            return;
//        }
//
////            if (channelMsg.netMsg instanceof QNetMsgSwapNetEndPointInfo) {
////                System.out.println("SwapNetEndPointInfo -> Failed");
////                return;
////            }
////            if (channelMsg.netMsg instanceof QNetMsgSwapLanAddressListAndNetEndPointInfo) {
////                System.out.println("SwapLanAddressList -> Failed");
////                return;
////            }
////            if (channelMsg.netMsg instanceof QNetMsgGetNetworkStructure) {
////                System.out.println("GetDiscoveryInfo -> Failed");
////            return;
////            }
//
//
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Persistence ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void saveNodeInfo() {
//        AES256BinaryEncryptor littleEncryptor = new AES256BinaryEncryptor();
//        char[] littlePassChars = getLittlePassChars();
//        littleEncryptor.setPasswordCharArray(littlePassChars);
//        Arrays.fill(littlePassChars, '0');
//        serialization.saveObjToEncFile(nodeInfoFilePath, nodeInfo, littleEncryptor);
//    }
//
//    private void loadNodeInfo() {
//        AES256BinaryEncryptor littleEncryptor = new AES256BinaryEncryptor();
//        char[] littlePassChars = getLittlePassChars();
//        littleEncryptor.setPasswordCharArray(littlePassChars);
//        Arrays.fill(littlePassChars, '0');
//        nodeInfo = (QNodeInfo) serialization.loadObjFromEncFile(nodeInfoFilePath, littleEncryptor);
//    }
//
//
////        private void saveKnownLanNodesList () {
////            serialization.saveObjToFile(knownLanNodeAddressesFilePath, knownLanNodesList);
////        }
////
////        private void loadKnownLanNodesList () {
////            knownLanNodesList = (ArrayList<String>) serialization.loadObjFromFile(knownLanNodeAddressesFilePath);
////        }
//
//
//    private void hideLittlePassChars(char[] littlePassChars) {
//        Path path = Paths.get(System.getProperty("java.io.tmpdir"), "lp");
//        try {
//            byte[] bytes = QConvertUtils.charArrayToByteArray(littlePassChars);
//            FileUtils.writeByteArrayToFile(path.toFile(), bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private char[] getLittlePassChars() {
//        Path path = Paths.get(System.getProperty("java.io.tmpdir"), "lp");
//        try {
//            byte[] bytes = FileUtils.readFileToByteArray(path.toFile());
//            return QConvertUtils.byteArrayToCharArray(bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    private void saveKnownNetEndPointInfoMap() {
//        serialization.saveObjToFile(knownNetEndPointInfoMapFilePath, knownNetEndPointInfoMap);
//    }
//
//    private void loadKnownNetEndPointInfoMap() {
//        knownNetEndPointInfoMap = (ConcurrentHashMap<UUID, QNetEndPointInfo>) serialization.loadObjFromFile(knownNetEndPointInfoMapFilePath);
//    }
//
//    private void saveNicknamesMap() {
//        serialization.saveObjToFile(nicknamesMapFilePath, nicknamesMap);
//    }
//
//    private void loadNicknamesMap() {
//        nicknamesMap = (Map<String, UUID>) serialization.loadObjFromFile(nicknamesMapFilePath);
//    }
//
//    private void saveNetworkStructure() {
//        serialization.saveObjToFile(networkStructureFilePath, networkStructure);
//    }
//
//    private void loadNetworkStructure() {
//        networkStructure = (QNetworkStructure) serialization.loadObjFromFile(networkStructureFilePath);
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} /////// End Class ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
