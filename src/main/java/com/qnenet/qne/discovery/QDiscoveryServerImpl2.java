//package com.qnenet.qne.discovery;
//
//////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Class ///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//import com.qnenet.qne.objects.classes.QDiscoveryServer;
//import com.qnenet.qne.objects.classes.QNetMsg;
//import com.qnenet.qne.objects.impl.QNEObjects;
//import com.qnenet.qne.system.constants.QSysConstants;
//import com.qnenet.qne.system.impl.QNEPaths;
//import com.qnenet.qne.system.impl.QSystem;
//import com.qnenet.qne.system.utils.*;
//import jakarta.annotation.PostConstruct;
//import org.bouncycastle.asn1.x500.X500Name;
//import org.bouncycastle.cert.X509v3CertificateBuilder;
//import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
//import org.bouncycastle.operator.ContentSigner;
//import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import javax.swing.*;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.math.BigInteger;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.*;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.time.LocalDate;
//import java.time.ZoneOffset;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//@Component
//@Scope("prototype")
//public class QDiscoveryServerImpl implements QDiscoveryServer {
//
////    Logger log = LoggerFactory.getLogger(QDiscoveryServerImpl.class);
//
//    private static final int ENTRY_SIZE = 20;
//    private static final int VALIDITY_YEARS = 1;
////    private final QObjects qobjs;
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
//    private Path discoveryServerPath;
//    //    private Path certificateServerPath;
//    private Path nodeLookupRafFilePath;
//    private Path spareSlotsListFilePath;
//    private CopyOnWriteArrayList<Long> spareSlotsList;
//
//    private RandomAccessFile nodeLookupRaf;
//    private PrivateKey interPrivateKey;
//    private X509Certificate interCertificate;
//    private PublicKey interPublicKey;
//    private X509Certificate[] interCertificateChain;
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Activator ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//@PostConstruct
//    public void postConstruct() throws IOException {
//
//        discoveryServerPath = Paths.get(qnePaths.getSystemPath().toString(),"discoveryServer");
//        nodeLookupRafFilePath = Paths.get(discoveryServerPath.toString(), "nodeLookup.raf");
//        spareSlotsListFilePath = Paths.get(discoveryServerPath.toString(), "spareSlots.list");
//
//
//        JFrame jFrame = new JFrame();
//        char[] interKeyStorePassPhrase = JOptionPane.showInputDialog(jFrame, "Enter Inter Keystore Pass Phrase").toCharArray();
//
////        Path interKeyStorePath =
////                Paths.get(qnePaths.getRepositoryPath().toString(), QSysConstants.INTER_KEYSTORE_DIR_NAME);
////        Path interKeyStoreFilePath = Paths.get(interKeyStorePath.toString(), "interKeyStore.p12");
//
//    Path interKeyStorePath = Paths.get(qnePaths.getRepositoryPath().toString(), QSysConstants.INTER_KEYSTORE_DIR_NAME);
//    Path interKeyStoreFilePath = Paths.get(interKeyStorePath.toString(), "interKeyStore.p12");
//
//    KeyStore interKeyStore = null;
//        try {
//            interKeyStore = QSecurityUtils.loadKeyStore(interKeyStoreFilePath, interKeyStorePassPhrase);
//
//            ArrayList<String> allAliases = Collections.list(interKeyStore.aliases());
//            ArrayList<String> interAliasses = new ArrayList<>();
//            for (String alias : allAliases) {
//                if (alias.startsWith(QSysConstants.INTERCERT_ALIAS_PREFIX)) {
//                    interAliasses.add(alias);
//                }
//            }
//            Collections.sort(interAliasses);
//
//            int idx = QRandomUtils.randomIntBetween(0, interAliasses.size() - 1);
//
//            String useInterAlias = interAliasses.get(idx);
//            KeyStore.PrivateKeyEntry interEntry = (KeyStore.PrivateKeyEntry) interKeyStore.getEntry(useInterAlias,
//                    new KeyStore.PasswordProtection(interKeyStorePassPhrase));
//
//
////            KeyStore keyStore = system.getKeystore();
//////            KeyStore trustStore = system.getTrustStore();
//
//            interCertificate = (X509Certificate) interEntry.getCertificate();
//            interCertificateChain = (X509Certificate[]) interEntry.getCertificateChain();
//            interPrivateKey = interEntry.getPrivateKey();
//            interPublicKey = interCertificate.getPublicKey();
//
////        networkStructureFilePath = Paths.get(discoveryServerPath.toString(), "networkStructure");
//        } catch (KeyStoreException e) {
//            throw new RuntimeException(e);
//        } catch (CertificateException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (UnrecoverableEntryException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        if (Files.notExists(discoveryServerPath)) {
//            newSystem();
//        } else {
//            restartSystem();
//        }
//
////        system.registerObject(QSysConstants.DISCOVERY_SERVER, this);
//
////        log.info("Hello from -> " + getClass().getSimpleName());
//    }
//
//
//
////    @Deactivate
////    public void deactivate() {
////        node.unregisterObject(QSysConstants.DISCOVERY_SERVER);
////        log.info("Goodbye from -> " + getClass().getSimpleName());
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// New System //////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void newSystem() throws IOException {
//        QFileUtils.checkDirectory(discoveryServerPath);
//        spareSlotsList = new CopyOnWriteArrayList<>();
//        saveSpareSlotsList();
//
//        loadsegmentRaf();
//
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Restart /////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void restartSystem() {
//        loadSpareSlotsList();
//        loadsegmentRaf();
////        loadNetworkStructure();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Add CertificateServer ///////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    @Override
////    public void addCertificateServer(String certServerAddrStr) {
////        if (networkStructure.certificateServers.isEmpty()) {
////            networkStructure.certificateServers = certServerAddrStr;
////            saveNetworkStructure();
////
////            system.getExecutor().submit(new Runnable() {
////                @Override
////                public void run() {
////                    QNetMsgGetCertificate netMsg = new QNetMsgGetCertificate();
//////                    netMsg.segmentItem = new QSegmentItem(node.getNodeInfo());
////                    netMsg.sslPublicKeyEncoded = node.getNodeInfo().sslPublicKeyEncoded;
////
//////                    System.out.println("Sent public key pem -> " + netMsg.sslPublicKeyPEM);
////                    netMsg.origin = origin;
////                    netMsg.destQNetAddress = QNetAddress.fromString(certServerAddrStr);
////                    node.doRoundTripRequest(netMsg);
////                }
////            });
////
////        } else {
////            networkStructure.certificateServers += ";" + certServerAddrStr;
//////            saveNetworkStructure();
////        }
////    }
//
////    private Integer getNextSegmentId() {
////        if (networkStructure.segmentServers.isEmpty()) return 1;
////        int max = 0;
////        for (int id : networkStructure.segmentServers.keySet()) {
////            if (id > max) max = id;
////        }
////        return max + 1;
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Add SegmentServer ///////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//// set segmentId to null for new segment
//// set segmentId for copy segment
//
////    @Override
////    public int addSegmentServer(String segmentServerAddressStr) {
////        return addSegmentServer(segmentServerAddressStr, null);
////    }
//
////    @Override
////    public int addSegmentServer(String segmentServerAddressStr, Integer segmentId) {
////        if (segmentId == null) {
////            segmentId = getNextSegmentId();
////            networkStructure.segmentServers.put(segmentId, segmentServerAddressStr);
////        } else {
////            String s = networkStructure.segmentServers.get(segmentId);
////            s += ";" + segmentServerAddressStr;
////            networkStructure.segmentServers.put(segmentId, s);
////        }
////        saveNetworkStructure();
////        return segmentId;
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Getters & Setters ///////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    @Override
////    public QNode getNode() {
////        return node;
////    }
//
//    @Override
//    public Path getPath() {
//        return discoveryServerPath;
//    }
//
////    @Override
////    public QNetworkStructure getNetworkStructure() {
////        return networkStructure;
////    }
//
////    @Override
////    public synchronized void addNewNode(QNetMsg netMsg) {
////        QNetMsgAddNewNodeLookup nm = (QNetMsgAddNewNodeLookup) netMsg;
////        this.addNewNode(nm);
////    }
//
//    @Override
//    public void getCertificate(QNetMsg netMsg) {
//        QNetMsgAddNewNodeLookup nm = (QNetMsgAddNewNodeLookup) netMsg;
//
//        String subject = "CN=QNE-NODE-ID : " + QBase36.qneIdToQNEIdString(nm.nodeId);
////        String subject = "CN=" + publicKnownInfo.netAddressPair.wanNetAddress.ipAddress;
//        X500Name endCertSubject = new X500Name(subject);
//
//        LocalDate localNow = LocalDate.now();
//        LocalDate localNowMinus = localNow.minus((long) 7, ChronoUnit.DAYS);
//
//        Date dateFrom = Date.from(localNowMinus.atStartOfDay().toInstant(ZoneOffset.UTC));
//        Date dateTo = QDateTimeUtils.nowUTCPlusYears(VALIDITY_YEARS);
//
//        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
//
////        PublicKey endPublicKey = QSecurityUtils.ecPublicKeyFromBytes(QSysConstants.END_CERT_EC_NAME_CURVE, sslPublicKeyPEM);
////        PublicKey endPublicKey = QSecurityUtils.ecPublicKeyFromPEM(sslPublicKeyPEM);
//
//        try {
//
////            Reader rdr = new StringReader(publicKeyPEM); // or from file etc.
////            org.bouncycastle.util.io.pem.PemObject spki = new org.bouncycastle.util.io.pem.PemReader(rdr).readPemObject();
////            PublicKey endPublicKey = null;
////            endPublicKey = KeyFactory.getInstance("EC", "BC").generatePublic(new X509EncodedKeySpec(spki.getContent()));
//
//            PublicKey endPublicKey = QSecurityUtils.ecdsaPublicKeyFromBytes(nm.sslPublicKeyEncoded);
//            ContentSigner endCertContentSigner = new JcaContentSignerBuilder(
//                    QSysConstants.END_CERT_SIGNATURE_ALGORITHM)
//                    .setProvider(QSysConstants.END_CERT_BC_PROVIDER)
//                    .build(interPrivateKey);
//
//            X509v3CertificateBuilder endCertBuilder = new JcaX509v3CertificateBuilder(
//                    interCertificate,
//                    serialNumber,
//                    dateFrom,
//                    dateTo,
//                    endCertSubject,
//                    endPublicKey);
//
////        X509v3CertificateBuilder certBuilderV3 = new JcaX509v3CertificateBuilder(
////                interCertificate,
////                BigInteger.valueOf(System.currentTimeMillis()),
////                dateFrom,
////                dateTo,
////                new X500Name(subject),
////                endPublicKey);
//
//
//////        String issuer = interCertificate.getSubjectX500Principal().toString();
////        String subject = "CN=QNE-Id -> " + QBase36.qneIdToFormattedQNEIdString(knownInfo.qneId);
////
////        LocalDate localNow = LocalDate.now();
////        LocalDate localNowMinus = localNow.minus((long) 7, ChronoUnit.DAYS);
////
////        Date dateFrom = Date.from(localNowMinus.atStartOfDay().toInstant(ZoneOffset.UTC));
////        Date dateTo = QDateTime.nowUTCPlusYears(VALIDITY_YEARS);
////
////        PublicKey endPublicKey = QSecurityUtils.publicKeyFromBytes(knownInfo.publicKeyBytes, null);
////
////        X509v3CertificateBuilder certBuilderV3 = new JcaX509v3CertificateBuilder(
////                interCertificate,
////                BigInteger.valueOf(System.currentTimeMillis()),
////                dateFrom,
////                dateTo,
////                new X500Name(subject),
////                endPublicKey);
////
///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////// Extensions ////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
////
////            JcaX509ExtensionUtils extUtils = null;
////            extUtils = new JcaX509ExtensionUtils();
////
////            // is CA certificate
////            // certBuilderV3.addExtension(Extension.basicConstraints, true, new BasicConstraints(0));
////
////            endCertBuilder.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(interPublicKey));
////
////            endCertBuilder.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(endPublicKey));
////
///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////// Extensions Alt Names //////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
////
//////        certBuilderV3.addExtension(Extension.subjectAlternativeName,
//////                false,
//////                extUtils.createAuthorityKeyIdentifier(interPublicKey));
////
////
////            ArrayList<GeneralName> altNames = new ArrayList<>();
////            altNames.add(new GeneralName(GeneralName.iPAddress, nm.newNodeNetAddressPair.wanNetAddress.ipAddress));
////            if (nm.newNodeNetAddressPair.lanNetAddress != null) {
////                altNames.add(new GeneralName(GeneralName.iPAddress, nm.newNodeNetAddressPair.lanNetAddress.ipAddress));
////            }
////
////            GeneralNames subjectAltNames = GeneralNames.getInstance(new DERSequence((GeneralName[]) altNames.toArray(new GeneralName[]{})));
////            endCertBuilder.addExtension(Extension.subjectAlternativeName, false, subjectAltNames);
////
////
///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////// Certificate ///////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
////
////            X509CertificateHolder endCertHolder = endCertBuilder.build(endCertContentSigner);
////            X509Certificate endCert = new JcaX509CertificateConverter().setProvider(
////                    QSysConstants.END_CERT_BC_PROVIDER).getCertificate(endCertHolder);
////
//////            X509CertificateHolder certHldr = endCertBuilder.build(
//////                    new JcaContentSignerBuilder("sha256WithRSAEncryption")
//////                            .setProvider("BC")
//////                            .build(interPrivateKey));
//////
//////            X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHldr);
////
//////        X509CertificateHolder certHldr = certBuilderV3.build(new JcaContentSignerBuilder("SHA1WithRSA").setProvider("BC").build(interPrivateKey));
//////
//////        X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHldr);
////
///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////// Verify  ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
////
////            endCert.checkValidity(new Date());
////
////            endCert.verify(interPublicKey);
////
///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////// Bag Attribute  ////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
////
////            PKCS12BagAttributeCarrier bagAttr = (PKCS12BagAttributeCarrier) endCert;
////
////            // this is actually optional - but if you want to have control over setting the friendly name this is the way to do it...
////            bagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, new DERBMPString("QNE End Certificate"));
////
////            // this is also optional - in the sense that if you leave this out the keystore will add it automatically,
////            // note though that for the browser to recognise the associated private key this you should at least use the
////            // pkcs_9_localKeyId OID and set it to the same as you do for the private key's localKeyId.
////
////            bagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, extUtils.createSubjectKeyIdentifier(endPublicKey));
////
///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////// Finish  ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
////            X509Certificate[] newChain = new X509Certificate[]{endCert, interCertificateChain[0], interCertificateChain[1]};
////
////            nm.endCertificateBytes = QSecurityUtils.certificateArrayToByteArray(newChain);
////        } catch (NoSuchAlgorithmException e) {
////            throw new RuntimeException(e);
////        } catch (CertificateNotYetValidException e) {
////            throw new RuntimeException(e);
////        } catch (CertificateExpiredException e) {
////            throw new RuntimeException(e);
////        } catch (CertificateEncodingException e) {
////            throw new RuntimeException(e);
////        } catch (CertificateException e) {
////            throw new RuntimeException(e);
////        } catch (SignatureException e) {
////            throw new RuntimeException(e);
////        } catch (InvalidKeyException e) {
////            throw new RuntimeException(e);
////        } catch (NoSuchProviderException e) {
////            throw new RuntimeException(e);
////        } catch (CertIOException e) {
////            throw new RuntimeException(e);
////        } catch (OperatorCreationException e) {
////            throw new RuntimeException(e);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
//
//
//    }
//
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////// RAF ////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void loadsegmentRaf() {
//        nodeLookupRaf = null;
//        try {
//            /*
//             * rws forces every write to the storage device. Both content and metadata
//             * written, slower, but safer.
//             */
//            nodeLookupRaf = new RandomAccessFile(nodeLookupRafFilePath.toFile(), "rws");
//
//        } catch (FileNotFoundException e) {
//        }
//    }
//
//    private long getNextIdx() {
//        try {
//            return nodeLookupRaf.length() / ENTRY_SIZE;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//    private void close() {
//        try {
//            nodeLookupRaf.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
////    @Override
////    public QNetEndPointInfo getNetAddress(int idx) {
////        try {
////            if (positionAtIdx(idx)) return null;
////            byte[] objBytes = new byte[segmentRaf.readByte()];
////            segmentRaf.read(objBytes);
////            return (QNetEndPointInfo) serialization.objFromBytes(objBytes);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
//
//    private boolean positionAtIdx(long idx) {
//        long ptr = idx * ENTRY_SIZE;
//        try {
//            if (ptr >= nodeLookupRaf.length()) return true;
////            byte[] readArray = new byte[ENTRY_SIZE];
//            nodeLookupRaf.seek(ptr);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return false;
//    }
//
//
////    private boolean setNodeNetAddressAtIdx(long nodeIdx, QNetAddress netAddress) {
////        try {
////            nodeLookupRaf.seek(nodeIdx * ENTRY_SIZE);
////
//////            byte[] objBytes = serialization.objToBytes(netAddress);
////            byte[] netAddressBytes = QNetAddress.netAddressToBytes(netAddress);
////
//////            nodeLookupRaf.writeByte(netAddressBytes.length);
////            nodeLookupRaf.write(netAddressBytes);
////            return true;
////        } catch (Exception e) {
////            return false;
//////            e.printStackTrace();
////        }
////    }
//
////    private void addNewNode(QNetMsgAddNewNodeLookup nm) {
////        long idx = -1;
////        if (spareSlotsList.isEmpty()) {
////            idx = getNextIdx();
////        } else {
////            idx = spareSlotsList.remove(0);
////        }
////        setNodeNetAddressAtIdx(idx, nm.newNodeNetAddressPair.wanNetAddress);
////        nm.nodeId = idx + QSysConstants.NODE_ID_BASE;
////    }
//
//    private long removeNode(Long nodeId) {
//        long nodeIdx = nodeId - QSysConstants.QNE_GLOBAL_ID_BASE;
//        byte[] bytes = new byte[ENTRY_SIZE];
////        setNodeNetAddressAtIdx(nodeIdx, item);
//        positionAtIdx(nodeIdx);
//        spareSlotsList.add(nodeIdx);
//        saveSpareSlotsList();
//        return nodeIdx;
//    }
//
//
//    private long addNext(byte[] bytes) {
//        long idx = 0;
//        try {
//            long ptr = nodeLookupRaf.length();
//            idx = ptr / ENTRY_SIZE;
////            if (idx > QSysConstants.NODE_LOOKUP_MAX_NUMBER_OF_ENTRIES - 1) return -1;
//            nodeLookupRaf.seek(ptr);
//            nodeLookupRaf.write(bytes);
//            return idx + QSysConstants.QNE_GLOBAL_ID_BASE;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }
//
//    private long getSize() {
//        try {
//            return nodeLookupRaf.length();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return 0L;
//    }
//
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Persistence ////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
////    private void saveNameLookupMap() {
////        serialization.saveObjToFile(nameLookupMapFilePath, nameLookupMap);
////    }
////
////    private void loadNameLookupMap() {
////        nameLookupMap = (ConcurrentHashMap<String, Integer>) serialization.loadObjFromFile(nameLookupMapFilePath);
////    }
//
//    private void saveSpareSlotsList() {
//        qobjs.saveObjToFile(spareSlotsListFilePath, spareSlotsList);
//    }
//
//    private void loadSpareSlotsList() {
//        spareSlotsList = (CopyOnWriteArrayList<Long>) qobjs.loadObjFromFile(spareSlotsListFilePath);
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// ProcessRT ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    @Override
////    public void processRT(QChannelMsg channelMsg) {
////        if (channelMsg.netMsg instanceof QNetMsgGetCertificate) {
////            QNode node = channelMsg.channel.getNode();
////            QNetMsgGetCertificate netMsg = (QNetMsgGetCertificate) channelMsg.netMsg;
////            byte[][] endCertificateBytes = netMsg.endCertificateBytes;
////            PrivateKey privateKey = node.getSSLPrivateKey();
////            system.addEntryToKeystore("endCert", endCertificateBytes, privateKey);
////            System.out.println("GetCertificate -> Success");
////            return;
////        }
////    }
////
////    @Override
////    public void roundTripFailed(QChannelMsg channelMsg) {
////        if (channelMsg.netMsg instanceof QNetMsgGetCertificate) {
////            QNetMsgGetCertificate netMsg = (QNetMsgGetCertificate) channelMsg.netMsg;
////            System.out.println("GetCertificate -> Failed");
////        }
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} ///////// End Class /////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
