package com.qnenet.qne.discovery;

import com.qnenet.qne.network.known.QKnown;
import com.qnenet.qne.objects.classes.QEPAddr;
import com.qnenet.qne.objects.classes.QNetworkStructure;
import com.qnenet.qne.objects.impl.QNEObjects;
import com.qnenet.qne.system.impl.QNEPaths;
import com.qnenet.qne.system.utils.QFileUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("prototype")
public class QDiscoveryServer {

    private Path discoveryServerPath;

    @Autowired
    QKnown known;

    @Autowired
    QNEPaths qnePaths;

    @SuppressWarnings("rawtypes")
    @Autowired
    QNEObjects qobjs;

    private QNetworkStructure networkStructure;

    @PostConstruct
    public void postConstruct() {

//    public QDiscoveryServer() throws IOException {
        discoveryServerPath = Paths.get(qnePaths.getSystemPath().toString(), "discoveryServer");

        if (QFileUtils.checkDirectory(discoveryServerPath)) {
            newSystem();
        } else {
            restartSystem();
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////
///////// New System //////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    private void newSystem() {
        networkStructure = new QNetworkStructure();
        networkStructure.discoveryServersList = new ArrayList<>();
        networkStructure.certificateServersList = new ArrayList<>();
        networkStructure.segmentServersMap = new ConcurrentHashMap<>();

        QEPAddr myEPAddr = known.getMyWanIPAddress();
        networkStructure.discoveryServersList.add(myEPAddr);
        saveLatestNetworkStructure();
    }


///////////////////////////////////////////////////////////////////////////////////////////////////
///////// Restart /////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    private void restartSystem() {
        loadLatestNetworkStructure();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////// Discovery Server Methods ////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public void addNewDiscoveryServer(QEPAddr wanEPAddr) {
        networkStructure.discoveryServersList.add(wanEPAddr);
        saveLatestNetworkStructure();
    }

    public void removeDiscoveryServer(QEPAddr wanEPAddr) {
        networkStructure.discoveryServersList.remove(wanEPAddr);
        saveLatestNetworkStructure();
    }

    public void addNewCertificateServer(QEPAddr wanEPAddr) {
        networkStructure.certificateServersList.add(wanEPAddr);
        saveLatestNetworkStructure();
    }


    public void removeCertificateServer(QEPAddr wanEPAddr) {
        networkStructure.certificateServersList.remove(wanEPAddr);
        saveLatestNetworkStructure();
    }

    public void addNewSegmentServer(int segmentId, QEPAddr wanEPAddr) {
        ArrayList<QEPAddr> segServersBySegmentId = networkStructure.segmentServersMap.get(segmentId);
        if (segServersBySegmentId == null) {
            segServersBySegmentId = new ArrayList<>();
        }
        segServersBySegmentId.add(wanEPAddr);
        networkStructure.segmentServersMap.put(segmentId, segServersBySegmentId);
        saveLatestNetworkStructure();
    }

    public void removeSegmentServer(@SuppressWarnings("rawtypes") QNEObjects qobjs, int segmentId, QEPAddr epAddr) {
        ArrayList<QEPAddr> segServersBySegmentId = networkStructure.segmentServersMap.get(segmentId);
        if (segServersBySegmentId == null) return;
        segServersBySegmentId.remove(epAddr);
        if (segServersBySegmentId.isEmpty()) {
            networkStructure.segmentServersMap.remove(segmentId);
        } else {
            networkStructure.segmentServersMap.put(segmentId, segServersBySegmentId);
        }
        saveLatestNetworkStructure();
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////// Persistence ////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void saveLatestNetworkStructure() {
        networkStructure.timestamp = System.currentTimeMillis();
        String fileName = String.valueOf(networkStructure.timestamp) + ".networkSturcture";
        Path networkStructureFilePath = Paths.get(discoveryServerPath.toString(), fileName);
        qobjs.saveObjToFile(networkStructureFilePath, networkStructure);
    }

    private void loadLatestNetworkStructure() {
        Path latestFileInDir = QFileUtils.getLatestFileInDir(discoveryServerPath);
        networkStructure = (QNetworkStructure) qobjs.loadObjFromFile(latestFileInDir);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
} ///////// End Class /////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


