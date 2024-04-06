//package com.qnenet.qne.network.endpoint;
//
//import com.qnenet.qne.network.known.QKnown;
//import com.qnenet.qne.objects.classes.QEPAddrPair;
//import com.qnenet.qne.objects.classes.QEPId;
//import com.qnenet.qne.objects.classes.QEndPointProps;
//import com.qnenet.qne.objects.classes.QEndPointRestartInfo;
//import com.qnenet.qne.objects.impl.QNEObjects;
//import com.qnenet.qne.system.impl.QNEPaths;
//import com.qnenet.qne.system.impl.QSystem;
//import com.qnenet.qne.system.utils.*;
//import jakarta.annotation.PostConstruct;
//import org.jasypt.util.binary.AES256BinaryEncryptor;
//import org.springframework.beans.factory.ObjectFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.ShortBufferException;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.NoSuchAlgorithmException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//@Lazy
////@Scope("prototype")
//public class QEndPointManager {
//
//    @Autowired
//    private ObjectFactory<QEndPoint> endPointFactory;
//
////    @Autowired
////    private QSystem system;
//
//    @Autowired
//    private QNEObjects qobjs;
//
//    @Autowired
//    private QNEPaths qnePaths;
//
//    @Autowired
//    private QKnown known;
//
//
//    private ArrayList<QEndPointRestartInfo> endPointsRestartInfo;
//
//    private ConcurrentHashMap<Integer, QEndPoint> endPointsByIdxMap = new ConcurrentHashMap<>();
//    private Path endPointRestartInfoListFilePath;
//
//    public QEndPointManager() {
//    }
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////////
///////////// Post Construct ////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @PostConstruct
//    public void init() throws ShortBufferException, BadPaddingException, NoSuchAlgorithmException, IOException, InterruptedException {
//        Path endPointManagerPath = Paths.get(qnePaths.getSystemPath().toString(), "endPointManager");
//        endPointRestartInfoListFilePath = Paths.get(endPointManagerPath.toString(), "endPointsRestartInfo.list");
//
//        if (QFileUtils.checkDirectory(endPointManagerPath)) {
//            newSystem();
//        } else {
//            restart();
//        }
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// New System ////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void newSystem() {
//        endPointsRestartInfo = new ArrayList<>();
//        saveEndPointsRestartInfo();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// Restart ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void restart() throws ShortBufferException, BadPaddingException, NoSuchAlgorithmException, IOException, InterruptedException {
//        loadEndPointsRestartInfo();
//        openEndPoints();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// Getters & Setters /////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public QEndPoint getEndPointByIdx(int endPointIdx) {
//        return endPointsByIdxMap.get(endPointIdx);
//    }
//
//    public void registerEndPointByIdx(QEndPoint endPoint) {
//        endPointsByIdxMap.put(endPoint.getEndPointIdx(), endPoint);
//    }
//
//    public void clearEndPointsList() {
//        endPointsByIdxMap.clear();
//    }
//
//    public QEndPoint getEndPoint(int endPointIdx) {
//        QEndPoint endPoint = endPointsByIdxMap.get(endPointIdx);
//        return endPoint;
//    }
//
//
//    private void openEndPoints() throws ShortBufferException, BadPaddingException, NoSuchAlgorithmException, IOException, InterruptedException {
//
//        for (int i = 0; i < endPointsRestartInfo.size(); i++) {
//            QEndPointRestartInfo endPointRestartInfo = endPointsRestartInfo.get(i);
//            if (endPointRestartInfo == null) continue;
//
//            QEndPoint endPoint = endPointFactory.getObject();
//            endPoint.restart(endPointRestartInfo);
////            endPointsByIdxMap.put(endPoint.getEndPointIdx(), endPoint);
//            registerEndPointByIdx(endPoint);
//
////            Path endPointPropsFilePath = Paths.get(qnePaths.getEndPointsPath().toString(),
////                    QStringUtils.int4(i), "endpoint.info");
////            AES256BinaryEncryptor endPointLittleEncryptor = new AES256BinaryEncryptor();
////            qobjs.loadObjFromEncFile(endPointPropsFilePath, );
//
//        }
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// EndPoints ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public QEndPoint addEndPoint(int endPointType, String emailAddress) throws ShortBufferException, BadPaddingException, NoSuchAlgorithmException, IOException, InterruptedException {
//
//        QThreadUtils.showThreadName("EndPoint Manager Add EndPoint");
//
//        QEndPoint endPoint = endPointFactory.getObject();
//
//        Path endPointsPath = qnePaths.getEndPointsPath();
//
//        QEndPointProps endPointProps = new QEndPointProps();
//
////        endPointProps = new QEndPointProps();
//
//        endPointProps.endPointName = "0-" + QRandomUtils.generateRandomName(4);
//
//
//        endPointProps.endPointType = endPointType;
//        endPointProps.mainEmail = emailAddress;
//
//        endPointProps.createdDateTime = LocalDateTime.now();
//
//        endPointProps.bigPassword = QSecurityUtils.generatePasswordChars(16);
//        endPointProps.littlePassword = QSecurityUtils.generateLittlePassword(4);
//
//        endPointProps.noiseKeypair = QSecurityUtils.createNoiseKeypair();
//
//        endPointProps.endPointDirectory = Paths.get(
//                endPointsPath.toString(), QStringUtils.int4(endPointProps.endPointIdx)).toString();
//
//        endPointProps.endPointStoreDirectory = Paths.get(
//                endPointProps.endPointDirectory, "store").toString();
//
//        Path endPointPropsFilePath = Paths.get(endPointProps.endPointDirectory, "endpoint.props");
//
//        endPointProps.endPointInfoFilePathStr = endPointPropsFilePath.toString();
//
//// transient
//        endPointProps.endPointPath = Paths.get(endPointProps.endPointDirectory);
////        QFileUtils.checkDirectory(endPointProps.endPointPath.getParent());
//        endPointProps.endPointStorePath = Paths.get(endPointProps.endPointStoreDirectory);
//
//        endPointProps.littleEncryptor = new AES256BinaryEncryptor();
//        endPointProps.bigEncryptor = new AES256BinaryEncryptor();
//
//        endPointProps.littleEncryptor.setPasswordCharArray(endPointProps.littlePassword);
//        endPointProps.bigEncryptor.setPasswordCharArray(endPointProps.bigPassword);
//
//        QEPAddrPair epAddrPair = new QEPAddrPair();
//        epAddrPair.setEpId(new QEPId(QRandomUtils.randomLongBetween(100000L, 999999L)));
//        epAddrPair.setEPIdx((short) QFileUtils.filesInDirCount(endPointsPath));
//        endPointProps.epAddrPair = epAddrPair;
//
////        endPointProps.qepId = new QEPId(QRandomUtils.randomLongBetween(100000L, 999999L));
////        endPointProps.wanIPAddressBytes = systemBaseEndPointInfo.wanIPAddressBytes;
////        endPointProps.lanIPAddressBytes = systemBaseEndPointInfo.lanIPAddressBytes;
////        endPointProps.port = systemBaseEndPointInfo.port;
////        endPointProps.endPointIdx;               //   4
//        endPointProps.publicKeyBytes = endPointProps.noiseKeypair.publicKeyBytes;
//
//        endPoint.initNew(endPointProps);
//
//        return endPoint;
//    }
////    public QEndPoint addInvitedEndPoint(int inviteId, int endPointType) {
//////        QEndPoint endPoint = endPointFactory.getObject();
//////        int idx = QFileUtils.filesInDirCount(qnePaths.getEndPointsPath()) + 1;
//////
//////        endpoint.initEndPoint(inviteId, idx, endPointType);
//////        endPoints.add(endpoint);
////        return endPoint;
////    }
//
//    public void registerNewEndPoint(QEndPointRestartInfo restartInfo) {
//        endPointsRestartInfo.add(restartInfo);
//        saveEndPointsRestartInfo();
//    }
//
//
//    private void saveEndPointsRestartInfo() {
//        qobjs.saveObjToEncFile(endPointRestartInfoListFilePath, endPointsRestartInfo, system.getBigEncryptor());
//    }
//
//    private void loadEndPointsRestartInfo() {
//        endPointsRestartInfo = (ArrayList<QEndPointRestartInfo>) qobjs.loadObjFromEncFile(endPointRestartInfoListFilePath, system.getBigEncryptor());
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} /////////// End Class ///////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
