//package com.qnenet.qne.store.storesPrev.mapStore.provider;
//
//import com.qnenet.qne.classes.objectsQNE.QStoreObject;
//import com.qnenet.qne.classes.objectsQNE.QStoreObjectKey;
//import com.qnenet.qne.objects.impl.QNEObjects;
//import com.qnenet.qne.store.storesPrev.mapStore.api.QMapStore;
//import com.qnenet.qne.system.utils.QFileUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//@Scope("prototype")
//public class QMapStoreImpl implements QMapStore {
//
//    @Autowired
//    QNEObjects qobjs;
//
//    private Path mapStorePath;
//    private int processUpdatesCount;
//    private Path mainMapFilePath;
//    private Path updatesMapFilePath;
//    private ConcurrentHashMap<QStoreObjectKey, QStoreObject> mainMap;
//    private ConcurrentHashMap<QStoreObjectKey, QStoreObject> updatesMap;
//
////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Init //////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
//    public void init(Path storePath, int processUpdatesCount) {
//
//        this.mapStorePath = storePath;
//        this.processUpdatesCount = processUpdatesCount;
//
//        mainMapFilePath = Paths.get(mapStorePath.toString(), "main.map");
//        updatesMapFilePath = Paths.get(mapStorePath.toString(), "updates.map");
//
//        try {
//            if (QFileUtils.checkDirectory(mapStorePath)) {
//                newSystem();
//            } else {
//                restart();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// New System ////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void newSystem() throws IOException {
//        mainMap = new ConcurrentHashMap<>();
//        updatesMap = new ConcurrentHashMap<>();
//        saveMainMap();
//        saveUpdatesMap();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Restart /////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void restart() throws IOException {
//        processUpdates();
//        loadMainMap();
//        loadUpdatesMap();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Store Methods /////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void processUpdates() {
//        for (QStoreObjectKey key : updatesMap.keySet()) {
//            QStoreObject storeObject = updatesMap.get(key);
//            if (storeObject != null) {
//                mainMap.put(key, storeObject);
//            }
//        }
//        saveMainMap();
//        updatesMap.clear();
//        saveUpdatesMap();
//    }
//
//    @Override
//    public boolean saveStoreObject(QStoreObject storeObject) {
////        if (updatesMap.size() > processUpdatesCount) processUpdates();
////        QStoreObjectKey storeKey = new QStoreObjectKey(storeObject.classId, storeObject.id);
////        updatesMap.put(storeKey, storeObject);
////        saveUpdatesMap();
//        return true;
//    }
//
//    @Override
//    public QStoreObject loadStoreObject(int classId, Object id) {
//        QStoreObjectKey storeKey = new QStoreObjectKey(classId, id);
//        QStoreObject storeObject = updatesMap.get(storeKey);
//        if (storeObject != null) return storeObject;
//        storeObject = mainMap.get(storeKey);
//        if (storeObject != null) return storeObject;
//        return null;
//    }
//
////    @Override
////    public Long getTotalObjCount() {
////        return null;
////    }
////
////    @Override
////    public ArrayList<Object> getAllObjsWithClassId(int classId) {
////        return null;
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// getters & setters /////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Persistence ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void saveMainMap() {
//        qobjs.saveObjToFile(mainMapFilePath, mainMap);
//    }
//
//    private void loadMainMap() {
//        mainMap = (ConcurrentHashMap<QStoreObjectKey, QStoreObject>) qobjs.loadObjFromFile(mainMapFilePath);
//    }
//
//    private void saveUpdatesMap() {
//        qobjs.saveObjToFile(updatesMapFilePath, updatesMap);
//    }
//
//    private void loadUpdatesMap() {
//        updatesMap = (ConcurrentHashMap<QStoreObjectKey, QStoreObject>) qobjs.loadObjFromFile(updatesMapFilePath);
//    }
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//} /////// End Class ////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
