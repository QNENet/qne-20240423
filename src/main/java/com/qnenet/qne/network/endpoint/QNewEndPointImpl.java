//package com.qnenet.qne.endpoint;
//
//import com.qnenet.qne.system.constants.QSysConstants;
//import com.qnenet.qne.objects.classes.QEPId;
//import com.qnenet.qne.objects.classes.QEndPointProps;
//import com.qnenet.qne.objects.classes.QEndPointRestartInfo;
//import com.qnenet.qne.objects.impl.QNEObjects;
//import com.qnenet.qne.system.impl.QNEPaths;
//import com.qnenet.qne.system.impl.QSystem;
//import com.qnenet.qne.system.utils.QFileUtils;
//import com.qnenet.qne.system.utils.QRandomUtils;
//import com.qnenet.qne.system.utils.QSecurityUtils;
//import com.qnenet.qne.system.utils.QStringUtils;
//import jakarta.annotation.PostConstruct;
//import org.jasypt.util.binary.AES256BinaryEncryptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Service;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.NoSuchAlgorithmException;
//import java.time.LocalDateTime;
//
//@Service
//@Scope("prototype")
//public class QNewEndPointImpl implements QNewEndPoint {
//
//    @Autowired
//    QSystem system;
//
//    @Autowired
//    QNEPaths qnePaths;
//
//    @Autowired
//    QNEObjects qobjs;
//
//    public Path endPointPropsFilePath;
//
//    private QEndPointProps endPointProps;
//    //    private QDiscoveryServer discoveryServer;
//    private QEndPointRestartInfo restartInfo;
//
//    @PostConstruct
//    public void init() throws NoSuchAlgorithmException {
//
////        qobjs = new QObjects();
//
//        Path endPointsPath = qnePaths.getEndPointsPath();
//        endPointProps = new QEndPointProps();
//
////        endPointProps.qEPId = "0-" + QRandomUtils.generateRandomName(4);
//
//        endPointProps.endPointName = "0-" + QRandomUtils.generateRandomName(4);
//
//        endPointProps.endPointIdx = QFileUtils.filesInDirCount(endPointsPath);
//
//        endPointProps.endPointType = QSysConstants.MEMBER_TYPE_NORMAL; //default
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
//        endPointPropsFilePath = Paths.get(endPointProps.endPointDirectory, "endpoint.props");
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
//        endPointProps.endPointInfo = system.getBaseEndPoint();
//        // ids below NODE_ID_BASE = 100000000000L; // 100,000,000 1NJCHS 1n.jc.hs are local only
//        // this is set to global Id later
//        endPointProps.endPointInfo.setQepId(new QEPId(QRandomUtils.randomLongBetween(100000L, 999999L)));
//        endPointProps.endPointInfo.setEndPointIdx(endPointProps.endPointIdx);
//        endPointProps.endPointInfo.setPublicKeyBytes(endPointProps.noiseKeypair.publicKeyBytes);
//
////        known.addEndPointAddress(endPointProps.endPointAddress);
//
//        saveEndPointProps();
//
//
////        system.registerNewEndPoint(endPointInfo, restartInfo);
//
//
////        nickNames.addNickname(nickName, endPointInfo.endPoint);
//
////        if (endPointInfo.endPointType == QSysConstants.MEMBER_TYPE_DISCOVERY_SERVER) {
////            discoveryServer = discoveryServerFactory.getObject();
////        }
//
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
////////////// Messaging ////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
////    @Override
////    public void sendMessage(QNetMsgStrings netMsg) {
////        node.send(netMsg);
////    }
//
//
////    public void sendMessage(QNetMsgStrings netMsg) {
////
////
//////        node.sendMessage(QNetAddressPair this.endPoint, destEndPoint.getEndPoint, netMsg);
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
////////////// Persistence///////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void saveEndPointProps() {
//        qobjs.saveObjToEncFile(endPointPropsFilePath, endPointProps, endPointProps.littleEncryptor);
//    }
//
//    private void loadEndPointInfo(AES256BinaryEncryptor littleEncryptor) {
//        endPointProps = (QEndPointProps) qobjs.loadObjFromEncFile(endPointPropsFilePath, littleEncryptor);
//    }
//
//    @Override
//    public QEndPointProps getEndPointProps() {
//        return endPointProps;
//    }
//
//    @Override
//    public QEndPointRestartInfo getRestartInfo() {
//        return restartInfo;
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} /////////// End Class ///////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
