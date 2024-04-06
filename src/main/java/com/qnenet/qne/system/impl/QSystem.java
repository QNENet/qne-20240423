package com.qnenet.qne.system.impl;

import com.qnenet.qne.cacertificates.QInterCertificate;
import com.qnenet.qne.cacertificates.QRootCertificate;
import com.qnenet.qne.discovery.QDiscoveryServer;
import com.qnenet.qne.network.endpoint.QEndPoint;
import com.qnenet.qne.network.known.QKnown;
import com.qnenet.qne.objects.classes.*;
import com.qnenet.qne.objects.impl.QNEObjects;
import com.qnenet.qne.segment.QSegmentServer;
import com.qnenet.qne.system.constants.QSysConstants;
import com.qnenet.qne.network.upnp.QPortManager;
// import com.qnenet.qne.security.users.QUsersManager;
import com.qnenet.qne.system.utils.*;
import jakarta.annotation.PostConstruct;

import jakarta.annotation.PreDestroy;
import org.apache.commons.io.FileUtils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.jasypt.util.binary.AES256BinaryEncryptor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
// import java.net.DatagramSocket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//////// Service //////////////////////////////////////////////////////////////////////////////////
//////// Service //////////////////////////////////////////////////////////////////////////////////
//////// Service //////////////////////////////////////////////////////////////////////////////////

@Service
public class QSystem {

    @Autowired
    private ObjectFactory<QEndPoint> endPointFactory;

    @Autowired
    private ObjectFactory<QRootCertificate> rootCertificateFactory;

    @Autowired
    private ObjectFactory<QInterCertificate> interCertificateFactory;

    @SuppressWarnings("unused")
    @Autowired
    private ObjectFactory<QDiscoveryServer> discoveryServerFactory;

    @SuppressWarnings("unused")
    @Autowired
    private ObjectFactory<QSegmentServer> segmentServerFactory;

    @Autowired
    private Environment env;

    @Autowired
    QNEPaths qnePaths;

    @Autowired
    QKnown known;

    @SuppressWarnings("rawtypes")
    @Autowired
    QNEObjects qobjs;

    @Autowired
    QPortManager portManager;

    // @Autowired
    // QUsersManager usersManager;

    private ExecutorService executor;

    private Map<String, Object> sysPropsMap;
    private AES256BinaryEncryptor bigEncryptor;
    private AES256BinaryEncryptor littleEncryptor;

    private ArrayList<Integer> statusList;
    private Path statusListFilePath;
    private KeyStore keyStore;
    // private Properties installerProps;

    // private boolean debugRestart;

    private static char[] littlePwd;
    private static char[] keyStorePwd;
    // private boolean uPnPListenerIsRunning;
    // private DatagramSocket uPnPSocket;

    private ArrayList<QEndPointRestartInfo> endPointsRestartInfo;
    private ConcurrentHashMap<Integer, QEndPoint> endPointsByIdxMap = new ConcurrentHashMap<>();
    private Path endPointRestartInfoListFilePath;

    @SuppressWarnings("unused")
    private boolean isNewSystem;

    @SuppressWarnings("unused")
    private QDiscoveryServer discoveryServer;
    @SuppressWarnings("unused")
    private QSegmentServer segmentServer;
    // private QCertificateServer certificateServer;

    //////// Constructor //////////////////////////////////////////////////////////////////////////////
    //////// Constructor //////////////////////////////////////////////////////////////////////////////
    //////// Constructor //////////////////////////////////////////////////////////////////////////////

    public QSystem() throws IOException {
        Security.addProvider(new BouncyCastleProvider());
        executor = getExecutor();
    }

    public ExecutorService getExecutor() {
        if (executor != null) return executor;
        return Executors.newVirtualThreadPerTaskExecutor();
    }

//////// Post Construct ///////////////////////////////////////////////////////////////////////////
//////// Post Construct ///////////////////////////////////////////////////////////////////////////
//////// Post Construct ///////////////////////////////////////////////////////////////////////////

    @PostConstruct
    public void postConstruct() {

//        debugRestart = Boolean.parseBoolean(env.getProperty("qne.is.debug.restart"));
//        if (debugRestart) {
//            if (Files.exists(qnePaths.getEndPointsPath())) {
//                FileUtils.deleteDirectory(qnePaths.getSystemPath().toFile());
//                FileUtils.deleteDirectory(qnePaths.getTmpPath().toFile());
//                FileUtils.deleteDirectory(qnePaths.getEndPointsPath().toFile());
//            }
//        }

        endPointRestartInfoListFilePath = Paths.get(qnePaths.getSystemPath().toString(), "endPointsRestartInfo.list");

        if (QFileUtils.checkDirectory(qnePaths.getEndPointsPath())) {
            isNewSystem = true;
            newSystem();
        } else {
            isNewSystem = false;
            restart();
        }
    }

//////// New System  //////////////////////////////////////////////////////////////////////////////
//////// New System  //////////////////////////////////////////////////////////////////////////////
//////// New System  //////////////////////////////////////////////////////////////////////////////

    private void newSystem() {
        executor.submit(() -> {

            portManager.init(this);
            newSystemStage2();
        });
    }


    private void newSystemStage2() {
        createIfNecessaryAndLoadSysPasswords();

//        Files.createDirectories(qnePaths.getTmpPath());
        QFileUtils.checkDirectory(qnePaths.getTmpPath());

        statusList = new ArrayList<>();
        statusList.add(QSysConstants.SYSTEM_STATUS_NEW_INSTALL);

        saveStatusList();

        installPropsToSysPropsMap(); // returns sysPropsMap

        sysPropsMap.put(QSysConstants.NODE_UUID, UUID.randomUUID());
        sysPropsMap.put(QSysConstants.NODE_NAME, "$" + QRandomUtils.generateRandomName(4));

        KeyPair ecdsaKeyPair = QSecurityUtils.createECDSAKeyPair("P-256");
        sysPropsMap.put(QSysConstants.SSL_PRIVATE_KEY_BYTES, ecdsaKeyPair.getPrivate().getEncoded());
        sysPropsMap.put(QSysConstants.SSL_PUBLIC_KEY_BYTES, ecdsaKeyPair.getPublic().getEncoded());

        // keypair for http server TLS
        sysPropsMap.put(QSysConstants.NOISE_KEY_PAIR, QSecurityUtils.createNoiseKeypair());

        littleEncryptor = new AES256BinaryEncryptor();
        littleEncryptor.setPasswordCharArray(littlePwd);

        char[] bigPwd = QSecurityUtils.generatePasswordChars(16);
        sysPropsMap.put(QSysConstants.BIG_PWD, bigPwd);
        bigEncryptor = new AES256BinaryEncryptor();
        bigEncryptor.setPasswordCharArray(bigPwd);

        sysPropsMap.put(QSysConstants.PASSWORDS_PIN, QRandomUtils.randomIntBetween(1000, 9999));

        ArrayList<char[]> endPointAccessKeyList = new ArrayList<>();
        sysPropsMap.put(QSysConstants.MEMBER_ACCESS_KEY_LIST, endPointAccessKeyList);


        sysPropsMap.put(QSysConstants.IS_HEADLESS, QGuiUtils.isReallyHeadless());

        if (Files.notExists(qnePaths.getKeystoreFilePath())) {
            keyStore = QSecurityUtils.createKeyStore();
            saveKeystore();
        }

        saveSysPropsMap();

        if (Boolean.parseBoolean(env.getProperty("create.master.certificates"))) {
            @SuppressWarnings("unused")
            QRootCertificate rootCertificate = rootCertificateFactory.getObject();
        }

        if (Boolean.parseBoolean(env.getProperty("create.inter.certificates"))) {
            int count = Integer.parseInt(env.getProperty("number.of.inter.certificates"));
            QInterCertificate interCertificate = interCertificateFactory.getObject();
            interCertificate.init(qnePaths, count);
        }

        String mainDiscoveryServerStr = env.getProperty(QSysConstants.MAIN_DISCOVERY_SERVER);
        String[] split = mainDiscoveryServerStr.split(":");
        QEPId mainDiscoveryServerQEPId = new QEPId(QSysConstants.QNE_GLOBAL_ID_BASE);
        byte[] ipAddressBytes = QNetworkUtils.ipAddressToBytes(split[0].trim());
        int port = Integer.valueOf(split[1].trim());
        short epIdx = Short.valueOf(split[2].trim());
        QEPAddr wanEPAddr = new QEPAddr(mainDiscoveryServerQEPId, ipAddressBytes, port, epIdx);
        QEPAddrPair epAddrPair = new QEPAddrPair(wanEPAddr,null);
        // find main discovery epAddrPair by QSysConstants.QNE_GLOBAL_ID_BASE
        known.addEPAddrPair(epAddrPair);

//        startServers();

        endPointsRestartInfo = new ArrayList<>();
        saveEndPointsRestartInfo();


        boolean deleteInstallerSystemProps = Boolean.parseBoolean(env.getProperty("qne.delete.installer.system.props"));
        if (deleteInstallerSystemProps) {
            try {
                Files.deleteIfExists(qnePaths.getInstallerPropsFilePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // usersManager.init(this);

        @SuppressWarnings("unused")
        QEndPoint endPoint = addEndPoint(QSysConstants.ENDPOINT_TYPE_FIRST);

    }

    // private void startServers() {
    //     if (Boolean.parseBoolean(env.getProperty("is.discovery.server"))) {
    //         discoveryServer = discoveryServerFactory.getObject();
    //     }

    //     if (Boolean.parseBoolean(env.getProperty("is.segment.server"))) {
    //         segmentServer = segmentServerFactory.getObject();
    //     }

    //     // if (Boolean.parseBoolean(env.getProperty("is.certificate.server"))) {
    //     //     certificateServer = segmentServerFactory.getObject();
    //     // }
    // }


//////// Restart //////////////////////////////////////////////////////////////////////////////////
//////// Restart //////////////////////////////////////////////////////////////////////////////////
//////// Restart //////////////////////////////////////////////////////////////////////////////////

    public void restart() {
        executor.submit(() -> {
            portManager.init(this);
            restartSystemStage2();
        });
    }

    private void restartSystemStage2() {


        createIfNecessaryAndLoadSysPasswords();
        littleEncryptor = new AES256BinaryEncryptor();
        littleEncryptor.setPasswordCharArray(littlePwd);
        loadSysPropsMap();

        bigEncryptor = new AES256BinaryEncryptor();
        bigEncryptor.setPasswordCharArray((char[]) sysPropsMap.get(QSysConstants.BIG_PWD));

        loadKeystore();

        // usersManager.init(this);

        loadEndPointsRestartInfo();
        openEndPoints();

//        startServers();


    }

//////// Pre Destroy //////////////////////////////////////////////////////////////////////////////
//////// Pre Destroy //////////////////////////////////////////////////////////////////////////////
//////// Pre Destroy //////////////////////////////////////////////////////////////////////////////

    @PreDestroy
    public void destroy() {
        System.out.println("QSystem Callback triggered - @PreDestroy.");
    }

/////////// Getters & Setters /////////////////////////////////////////////////////////////////////
/////////// Getters & Setters /////////////////////////////////////////////////////////////////////
/////////// Getters & Setters /////////////////////////////////////////////////////////////////////

    public Map<String, Object> getSysPropsMap() {
        return sysPropsMap;
    }

//    public ExecutorService getExecutor() {
//        return executor;
//    }

    public AES256BinaryEncryptor getBigEncryptor() {
        return bigEncryptor;
    }

    public UUID getUUID() {
        return (UUID) sysPropsMap.get(QSysConstants.NODE_UUID);
    }

    public String getName() {
        return (String) sysPropsMap.get(QSysConstants.NODE_NAME);
    }

    public ArrayList<Integer> getStatusList() {
        return statusList;
    }

    public byte[] getNoisePrivateKeyClone() {
        QNoiseKeypair noiseKeypair = (QNoiseKeypair) sysPropsMap.get(QSysConstants.NOISE_KEY_PAIR);
        return noiseKeypair.privateKeyBytes;
    }

    public byte[] getNoisePublicKeyClone() {
        QNoiseKeypair noiseKeypair = (QNoiseKeypair) sysPropsMap.get(QSysConstants.NOISE_KEY_PAIR);
        return noiseKeypair.publicKeyBytes;
    }

    public KeyStore getKeystore() {
        return keyStore;
    }

    @SuppressWarnings("rawtypes")
    public QNEObjects getQNEObjects() {
        return qobjs;
    }

    public String getLanIPAddress() {
        return (String) sysPropsMap.get(QSysConstants.LAN_IP_ADDRESS);
    }

    public int getPort() {
        return (int) sysPropsMap.get(QSysConstants.PORT);
    }

    public boolean getIsHeadless() {
        return (boolean) sysPropsMap.get(QSysConstants.IS_HEADLESS);
    }

/////////// End Points ////////////////////////////////////////////////////////////////////////////
/////////// End Points ////////////////////////////////////////////////////////////////////////////
/////////// End Points ////////////////////////////////////////////////////////////////////////////

    private QEndPoint addEndPoint(int endpointTypeFirst) {
        return addEndPoint(endpointTypeFirst, null);
    }


    public QEndPoint addEndPoint(int endPointType, String emailAddress) {

        QThreadUtils.showThreadName("EndPoint Manager Add EndPoint");

        QEndPoint endPoint = endPointFactory.getObject();

        Path endPointsPath = qnePaths.getEndPointsPath();

        QEndPointProps endPointProps = new QEndPointProps();

        endPointProps.endPointName = "0-" + QRandomUtils.generateRandomName(4);

        endPointProps.endPointType = endPointType;
        endPointProps.mainEmail = emailAddress;

        endPointProps.createdDateTime = LocalDateTime.now();

        endPointProps.bigPassword = QSecurityUtils.generatePasswordChars(16);
        endPointProps.littlePassword = QSecurityUtils.generateLittlePassword(4);

        endPointProps.noiseKeypair = QSecurityUtils.createNoiseKeypair();

        endPointProps.endPointDirectory = Paths.get(
                endPointsPath.toString(), QStringUtils.int4(endPointProps.endPointIdx)).toString();

        endPointProps.endPointStoreDirectory = Paths.get(
                endPointProps.endPointDirectory, "store").toString();

        Path endPointPropsFilePath = Paths.get(endPointProps.endPointDirectory, "endpoint.props");

        endPointProps.endPointInfoFilePathStr = endPointPropsFilePath.toString();

// transient
        endPointProps.endPointPath = Paths.get(endPointProps.endPointDirectory);
        endPointProps.endPointStorePath = Paths.get(endPointProps.endPointStoreDirectory);

        endPointProps.littleEncryptor = new AES256BinaryEncryptor();
        endPointProps.bigEncryptor = new AES256BinaryEncryptor();

        endPointProps.littleEncryptor.setPasswordCharArray(endPointProps.littlePassword);
        endPointProps.bigEncryptor.setPasswordCharArray(endPointProps.bigPassword);

        QEPAddrPair epAddrPair = new QEPAddrPair();
        epAddrPair.setEpId(new QEPId(QRandomUtils.randomLongBetween(100000L, 999999L)));
        epAddrPair.setEPIdx((short) QFileUtils.filesInDirCount(endPointsPath));
        endPointProps.epAddrPair = epAddrPair;

        endPointProps.publicKeyBytes = endPointProps.noiseKeypair.publicKeyBytes;

        endPoint.initNew(this, endPointProps);

        return endPoint;
    }


    public void registerNewEndPoint(QEndPointRestartInfo restartInfo) {
        endPointsRestartInfo.add(restartInfo);
        saveEndPointsRestartInfo();
    }

    private void saveEndPointsRestartInfo() {
        qobjs.saveObjToEncFile(endPointRestartInfoListFilePath, endPointsRestartInfo, bigEncryptor);
    }

    @SuppressWarnings("unchecked")
    private void loadEndPointsRestartInfo() {
        endPointsRestartInfo = (ArrayList<QEndPointRestartInfo>) qobjs.loadObjFromEncFile(endPointRestartInfoListFilePath, bigEncryptor);
    }

    public QEndPoint getEndPointByIdx(int endPointIdx) {
        return endPointsByIdxMap.get(endPointIdx);
    }

    public void registerEndPointByIdx(QEndPoint endPoint) {
        endPointsByIdxMap.put(endPoint.getEndPointIdx(), endPoint);
    }

    public void clearEndPointsList() {
        endPointsByIdxMap.clear();
    }

    public QEndPoint getEndPoint(int endPointIdx) {
        QEndPoint endPoint = endPointsByIdxMap.get(endPointIdx);
        return endPoint;
    }


    private void openEndPoints() {

        for (int i = 0; i < endPointsRestartInfo.size(); i++) {
            QEndPointRestartInfo endPointRestartInfo = endPointsRestartInfo.get(i);
            if (endPointRestartInfo == null) continue;

            QEndPoint endPoint = endPointFactory.getObject();
            endPoint.restart(endPointRestartInfo);
            registerEndPointByIdx(endPoint);
        }
    }

/////////// Persistence ///////////////////////////////////////////////////////////////////////////
/////////// Persistence ///////////////////////////////////////////////////////////////////////////
/////////// Persistence ///////////////////////////////////////////////////////////////////////////

    private void saveSysPropsMap() {
        qobjs.saveObjToEncFile(qnePaths.getSysPropsMapFilePath(), sysPropsMap, littleEncryptor);
    }

    @SuppressWarnings("unchecked")
    private void loadSysPropsMap() {
        sysPropsMap = (Map<String, Object>) qobjs.loadObjFromEncFile(qnePaths.getSysPropsMapFilePath(), littleEncryptor);
    }

    private Path getStatusListFilepath() {
        if (statusListFilePath == null)
            statusListFilePath = Paths.get(qnePaths.getSystemPath().toString(), "status.list");
        return statusListFilePath;
    }

    private void saveStatusList() {
        qobjs.saveObjToFile(getStatusListFilepath(), statusList);
    }

    // @SuppressWarnings("unchecked")
    // private void loadStatusList() {
    //     statusList = (ArrayList<Integer>) qobjs.loadObjFromFile(getStatusListFilepath());
    // }

    private void saveKeystore() {
        QSecurityUtils.saveKeyStore(qnePaths.getKeystoreFilePath(), keyStore, keyStorePwd);
    }

    private void loadKeystore() {
        keyStore = QSecurityUtils.loadKeyStore(qnePaths.getKeystoreFilePath(), keyStorePwd);
    }

/////////// Utilities /////////////////////////////////////////////////////////////////////////////
/////////// Utilities /////////////////////////////////////////////////////////////////////////////
/////////// Utilities /////////////////////////////////////////////////////////////////////////////

    private void createIfNecessaryAndLoadSysPasswords() {
        Path littlePwdFilePath = Paths.get(qnePaths.getRepositoryPath().toString(), "littlepwd.prop");
        Path keyStorePwdFilePath = Paths.get(qnePaths.getRepositoryPath().toString(), "keystorepwd.prop");
        try {
            if (Files.notExists(littlePwdFilePath)) {
                littlePwd = QSecurityUtils.generateLittlePassword(4);
                FileUtils.writeStringToFile(littlePwdFilePath.toFile(), new String(littlePwd), Charset.defaultCharset());
                keyStorePwd = QSecurityUtils.generatePasswordChars(8);
                FileUtils.writeStringToFile(keyStorePwdFilePath.toFile(), new String(keyStorePwd), Charset.defaultCharset());
            } else {
                littlePwd = FileUtils.readFileToString(littlePwdFilePath.toFile(), Charset.defaultCharset()).toCharArray();
                keyStorePwd = FileUtils.readFileToString(keyStorePwdFilePath.toFile(), Charset.defaultCharset()).toCharArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static Properties loadPropertiesFromFile(Path filePath) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(filePath.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public boolean savePropertiesToFile(Path filePath, Properties properties) {
        QFileUtils.checkDirectory(filePath.getParent());
        try {
            properties.store(new FileOutputStream(filePath.toString()), null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Map<String, Object> propsToMap(Path propsPath) {
        HashMap<String, Object> map = new HashMap<>();
        Properties properties = loadPropertiesFromFile(propsPath);
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String key = (String) propertyNames.nextElement();
            map.put(key, properties.getProperty(key));
        }
        return map;
    }

    public void addPropsToMap(Path propsPath, Map<String, String> map) {
        Properties properties = loadPropertiesFromFile(propsPath);
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String key = (String) propertyNames.nextElement();
            map.put(key, properties.getProperty(key));
        }
    }

    private void installPropsToSysPropsMap() {
        sysPropsMap = propsToMap(qnePaths.getInstallSystemPropsFilePath());
//
//        machine.cpu.count=16
        String cpuCountStr = (String) sysPropsMap.get("machine.cpu.count");
        sysPropsMap.put(QSysConstants.MACHINE_CPU_COUNT, Integer.parseInt(cpuCountStr));

//        machine.cpu.speed=2000
        String cpuSpeedStr = (String) sysPropsMap.get("machine.cpu.speed");
        sysPropsMap.put(QSysConstants.MACHINE_CPU_SPEED, Integer.parseInt(cpuSpeedStr));

//        machine.swap.memory=2047
        String swapMemoryStr = (String) sysPropsMap.get("machine.swap.memory");
        sysPropsMap.put(QSysConstants.MACHINE_SWAP_MEMORY, Integer.parseInt(swapMemoryStr));

//        machine.total.memory=15361
        String totalMemoryStr = (String) sysPropsMap.get("machine.total.memory");
        sysPropsMap.put(QSysConstants.MACHINE_TOTAL_MEMORY, Integer.parseInt(totalMemoryStr));

//
//        platform.has.multiple.processors=0
        String platformMultipleProcessorsStr = (String) sysPropsMap.get("platform.has.multiple.processors");
        sysPropsMap.put(QSysConstants.PLATFORM_HAS_MULTIPLE_PROCESSORS, Integer.parseInt(platformMultipleProcessorsStr));
    }

    public void updateCertificate(String wanIPAddress) {
        say("UpdateCertificate NOT IMPLEMENTED");
    }

    public void say(String line) {
        String timeStamp = DateFormat.getTimeInstance().format(new Date());
        String logline = timeStamp + ": " + line + "\n";
        System.out.print(logline);
    }


/////////// End Class /////////////////////////////////////////////////////////////////////////////
} ///////// End Class /////////////////////////////////////////////////////////////////////////////
/////////// End Class /////////////////////////////////////////////////////////////////////////////


