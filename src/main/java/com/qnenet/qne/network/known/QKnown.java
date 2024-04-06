package com.qnenet.qne.network.known;

import com.qnenet.qne.objects.classes.QEPAddr;
import com.qnenet.qne.objects.classes.QEPAddrPair;
import com.qnenet.qne.objects.classes.QEPId;
import com.qnenet.qne.objects.impl.QNEObjects;
import com.qnenet.qne.system.impl.QNEPaths;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QKnown {

    @Autowired
    QNEPaths qnePaths;

    @SuppressWarnings("rawtypes")
    @Autowired
    QNEObjects qobjs;

    private Path epAddrPairMapFilePath;
    private Path epAddrPairUpdatesMapFilePath;
    private Map<QEPId, QEPAddrPair> epAddrPairMap;
    private Map<QEPId, QEPAddrPair> epAddrPairUpdatesMap;

    private Path epNamesMapFilePath;
    private Path epNamesUpdatesMapFilePath;
    private Map<String, QEPId> epNamesMap;
    private Map<String, QEPId> epNamesUpdatesMap;



    @PostConstruct
    public void init() {

        epAddrPairMapFilePath = Paths.get(qnePaths.getKnownPath().toString(), "epAddrPair.map");
        epAddrPairUpdatesMapFilePath = Paths.get(qnePaths.getKnownPath().toString(), "epAddrPairUpdates.map");

        epNamesMapFilePath = Paths.get(qnePaths.getKnownPath().toString(), "epNames.map");
        epNamesUpdatesMapFilePath = Paths.get(qnePaths.getKnownPath().toString(), "epNamesUpdates.map");

        if (Files.notExists(epAddrPairMapFilePath)) {
            epAddrPairMap = new ConcurrentHashMap<>();
            epAddrPairUpdatesMap = new ConcurrentHashMap<>();
            saveEPAddrPairMap();
            saveEPAddrPairUpdatesMap();

            epNamesMap = new ConcurrentHashMap<>();
            epNamesUpdatesMap = new ConcurrentHashMap<>();
            saveEPNamesMap();
            saveEPNamesUpdatesMap();
        } else {
            loadEPAddrPairMap();
            loadEPAddrPairUpdatesMap();
            loadEPNamesMap();
            loadEPNamesUpdatesMap();
        }
    }



///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Getters & Setters /////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

//    public void addOrUpdateAddrPair(QEPAddrPair addrPair, boolean save) {
//        epAddrPairUpdatesMap.put(addrPair.epId, addrPair);
//        if (save) saveEPAddrPairUpdatesMap();
//    }

    public void addEPAddrPair(QEPAddrPair epAddrPair) {
        if (epAddrPairUpdatesMap.size() > 100) {
            processEPAddrPairUpdates();
        }
        epAddrPairUpdatesMap.put(epAddrPair.getEpId(), epAddrPair);
        saveEPAddrPairUpdatesMap();
    }

    public void removeEPAddrPair(QEPAddrPair epAddrPair) {
        if (epAddrPairUpdatesMap.size() > 100) {
            processEPAddrPairUpdates();
        }

        if (epAddrPairUpdatesMap.keySet().contains(epAddrPair.getEpId())){
            epAddrPairUpdatesMap.remove(epAddrPair.getEpId());
            saveEPAddrPairUpdatesMap();
            return;
        }

        epAddrPair.port = 0; // means delete epAddrPair
        epAddrPairUpdatesMap.put((QEPId) epAddrPair.epId, epAddrPair);
        saveEPAddrPairUpdatesMap();
    }






    public void addEndPointName(String endPointName, QEPId qepId) {
        epNamesUpdatesMap.put(endPointName, qepId);
        saveEPNamesUpdatesMap();
    }

    public QEPAddrPair getEPAddrPair(QEPId epId) {
        QEPAddrPair epAddrPair = epAddrPairUpdatesMap.get(epId);
        if (epAddrPair != null) return epAddrPair;

        epAddrPair = epAddrPairMap.get(epId);
        if (epAddrPair != null) return epAddrPair;

        return null;
    }


    private void processEPAddrPairUpdates() {

    }




    public void removeEndPointName(String endPointName, Long endPointId) {
//        QEndPointInfo endPointInfo = knownEndPointInfoMap.get(endPointId);
//        if (endPointInfo != null) {
//            knownEndPointInfoMap.remove(endPointId);
//        } else {
//            QEndPointInfo endPointInfo = knownEndPointInfoUpdatesMap.get(endPointId);
//            if (endPointInfo != null) {
//                knownEndPointInfoMap.remove(endPointId);
//
//        }
//        knownEndPointInfoUpdatesMap.get()
//
//        knownEndPointInfoMap.get(endPointId);
//
//
//
//        if (knownNamesUpdatesMap.size() >= 100) {
//            for (:
//                 ) {
//
//            }
//        } else {
//            knownNamesUpdatesMap.put(endPointName, id);
//        }
//        saveKnownNamesMap();
    }


///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Persistence ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    private void saveEPAddrPairMap() {
        qobjs.saveObjToFile(epAddrPairMapFilePath, epAddrPairMap);
    }

    @SuppressWarnings("unchecked")
    private void loadEPAddrPairMap() {
        epAddrPairMap = (ConcurrentHashMap<QEPId, QEPAddrPair>) qobjs.loadObjFromFile(epAddrPairMapFilePath);
    }

    private void saveEPAddrPairUpdatesMap() {
        qobjs.saveObjToFile(epAddrPairUpdatesMapFilePath, epAddrPairUpdatesMap);
    }

    @SuppressWarnings("unchecked")
    private void loadEPAddrPairUpdatesMap() {
        epAddrPairUpdatesMap = (ConcurrentHashMap<QEPId, QEPAddrPair>) qobjs.loadObjFromFile(epAddrPairUpdatesMapFilePath);
    }

    private void saveEPNamesMap() {
        qobjs.saveObjToFile(epNamesMapFilePath, epNamesMap);
    }

    @SuppressWarnings("unchecked")
    private void loadEPNamesMap() {
        epNamesMap = (Map<String, QEPId>) qobjs.loadObjFromFile(epNamesMapFilePath);
    }

    private void saveEPNamesUpdatesMap() {
        qobjs.saveObjToFile(epNamesUpdatesMapFilePath, epNamesUpdatesMap);
    }

    @SuppressWarnings("unchecked")
    private void loadEPNamesUpdatesMap() {
        epNamesUpdatesMap = (Map<String, QEPId>) qobjs.loadObjFromFile(epNamesUpdatesMapFilePath);
    }

    public QEPId getEPIdByName(String epName) {
        QEPId qepId = epNamesUpdatesMap.get(epName);
        if (qepId != null) return qepId;
        return epNamesMap.get(epName);
    }

    public QEPAddr getMyWanIPAddress() {
        return null;
    }

    public void setMainDiscoveryServer(QEPAddr epAddr) {

    }


///////////////////////////////////////////////////////////////////////////////////////////////////
} /////// End Class ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


