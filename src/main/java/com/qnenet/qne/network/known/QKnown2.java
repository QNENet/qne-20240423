//package com.qnenet.qne.known;
//
//import com.qnenet.qne.classes.objectsQNE.QEndPointInfo;
//import com.qnenet.qne.objects.impl.QNEObjects;
//import com.qnenet.qne.system.impl.QNEPaths;
//import jakarta.annotation.PostConstruct;
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
//public class QKnown2 {
//
//    @Autowired
//    QNEPaths qnePaths;
//
//    @Autowired
//    QNEObjects qobjs;
//
//    private Path knownEndPointsMapFilePath;
//    private Map<Long, QEndPointInfo> knownEndPointsMap;
//
//    @PostConstruct
//    public void init() {
//
//        knownEndPointsMapFilePath = Paths.get(qnePaths.getSystemPath().toString(), "knownEndPointInfo.map");
//
//        if (Files.notExists(knownEndPointsMapFilePath)) {
//            knownEndPointsMap = new ConcurrentHashMap<>();
//            saveKnownEndPointsMap();
//        } else {
//            loadKnownEndPointsMap();
//        }
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Persistence ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void saveKnownEndPointsMap() {
//        qobjs.saveObjToFile(knownEndPointsMapFilePath, knownEndPointsMap);
//    }
//
//    private void loadKnownEndPointsMap() {
//        knownEndPointsMap = (ConcurrentHashMap<Long, QEndPointInfo>) qobjs.loadObjFromFile(knownEndPointsMapFilePath);
//    }
//
//    public QEndPointInfo getEndPointAddress(Long endPointId) {
//        return knownEndPointsMap.get(endPointId);
//    }
//
//    public void addEndPointAddress() {
//
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} /////// End Class ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
