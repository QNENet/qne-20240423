package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objectsQNE.classes;
//
//import com.qnenet.qne.objectsQNE.impl.QObjects;
//import com.qnenet.qne.system.impl.QNEPaths;
//import com.qnenet.qne.utils.QFileUtils;
//import org.springframework.beans.factory.ObjectFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Service;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.UUID;
//
//@Service
//@Scope("prototype")
//public class QStore {
//    public UUID uuid;
//    private long storeOwnerQNEId;
//    public String savedStoreFilePathStr;
//    public long timestamp;
//    private int segmentSize;
//
//    public ArrayList<QStoreSegment> segmentList;
//
//    public transient Path storePath;
//    public transient Path segmentsPath;
//    //    public transient Path segmentListFilePath;
//    public transient Path storeSaveFilePath;
//
//    private transient QObjects qobjs;
//
//    @Autowired
//    transient ObjectFactory<QStoreSegment> segmentFactory;
//
//    @Autowired
//    transient QNEPaths qnePaths;
////    private QStoreSegment currentSegment;
//
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//    //////// Constructor //////////////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void initNewStore(long storeOwnerId, Path storePath, int segmentSize) throws IOException {
//        this.storePath = storePath;
//        this.storeOwnerQNEId = storeOwnerId;
//        this.segmentSize = segmentSize;
//        this.qobjs = new QObjects();
//        this.storeSaveFilePath = Paths.get(storePath.toString(), "saved.store");
//        this.savedStoreFilePathStr = storeSaveFilePath.toString();
//
////    // create new store first segment
//        this.segmentsPath = Paths.get(storePath.toString(), "segments");
//        QFileUtils.checkDirectory(segmentsPath);
//        addSegment();
//    }
//
//    public QStoreSegment addSegment() throws IOException {
//        this.uuid = UUID.randomUUID();
//        timestamp = System.currentTimeMillis();
//        if (segmentList == null) segmentList = new ArrayList<>();
//
//        QStoreSegment segment = segmentFactory.getObject();
//        segment.initNewSegment(this);
//        segmentList.add(segment);
//        saveStore();
//        return segment;
//    }
//
//    public void open(Path storePath) {
//        this.storePath = storePath;
//        this.qobjs = new QObjects();
//        this.segmentsPath = Paths.get(storePath.toString(), "segments");
//        this.storeSaveFilePath = Paths.get(storePath.toString(), "saved.store");
//        loadStore();
//    }
//
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//    //////// New System ///////////////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////////////////////////
////
////    private void newStore() throws IOException {
////        this.uuid = UUID.randomUUID();
////        storeOwnerQNEId = QSysConstants.QNE_GLOBAL_ID_BASE - 1L;
//////        Path fileName = storePath.getFileName();
//////        setStoreName(fileName.toString());
////        timestamp = System.currentTimeMillis();
////
////        QStoreSegment segment = segmentFactory.getObject();
////        segment.init(this, true);
////        segmentList = new ArrayList<>();
////        segmentList.add(segment);
////
////        saveStoreFields();
////
////    }
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//    //////// Restart //////////////////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void restartStore() {
//        loadStore();
//    }
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//    ///////// Store Methods ///////////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//
//    public long saveStoreObjToCurrentSegment(QStoreObject storeObjToSave) throws IOException {
//        QStoreSegment currentSegment = getCurrentSegment();
//        int newStoreObjSegmentPtr = currentSegment.addStoreObjToCurrentSegment(storeObjToSave);
//        long newStoreObjStorePtr = newStoreObjSegmentPtr + currentSegment.segmentFirstStoreItem;
//
//        long afterLength = currentSegment.addPtr;
//        if (afterLength >= segmentSize) {
//            addSegment();
//        }
//        return newStoreObjStorePtr;
//
//
//    }
//
//    private QStoreSegment getCurrentSegment() throws FileNotFoundException {
//        if (segmentList.isEmpty()) return null;
//        QStoreSegment currentSegment = segmentList.get(segmentList.size() - 1);
//        if (currentSegment == null) return null;
//        currentSegment.open();
//        return currentSegment;
//    }
//
//
//    private int getStoreObjSavedSize(QStoreObject storeObj) {
//        return 4 + 1 + 4 + storeObj.objBytes.length;
//    }
//
//
//    public QStoreObject loadObj(long storeObjPtr) throws IOException {
//        QStoreSegment segment = getSegmentForStoreIdx(storeObjPtr);
//        return segment.loadStoreObj(storeObjPtr);
//    }
//
//
////    public QStoreInfo getStoreInfo() {
////        return storeInfo;
////    }
//
//    private QStoreSegment getSegmentForStoreIdx(long storeObjPtr) throws IOException {
////        if (currentSegment.getSegmentInfo().contains(storeObjPtr)) return currentSegment;
//
////        for (int segmentId = 0; segmentId < segmentInfoList.size(); segmentId++) {
////            QSegmentInfo segmentInfo = segmentInfoList.get(segmentId);
////            if (segmentInfo.contains(storeObjPtr)) {
////                return loadSegment(segmentInfo);
////            }
////        }
//        return null;
//    }
//
////    private QStoreSegment loadSegment(QSegmentInfo segmentInfo) throws IOException {
////        QStoreSegment segment = segmentFactory.getObject();
////        segment.init(segmentInfo);
////        return segment;
////    }
//
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//    //////// Segments /////////////////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//
//
////    private QStoreSegment addNewSegment(long baseStoreId) {
////        QStoreSegment segment = new QStoreSegment(this, baseStoreId);
////
////        return segment;
////    }
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//    //////// Getters and Setters //////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//
//    public UUID getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(UUID uuid) {
//        this.uuid = uuid;
//    }
//
//    public long getStoreOwnerQNEId() {
//        return storeOwnerQNEId;
//    }
//
//    public void setStoreOwnerQNEId(long storeOwnerEId) {
//        this.storeOwnerQNEId = storeOwnerQNEId;
//    }
//
//    public String getSavedStoreFilePathStr() {
//        return savedStoreFilePathStr;
//    }
//
//    public void setSavedStoreFilePathStr(String savedStoreFilePathStr) {
//        this.savedStoreFilePathStr = savedStoreFilePathStr;
//    }
//
//    public long getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(long timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public int getSegmentSize() {
//        return segmentSize;
//    }
//
//    public void setSegmentSize(int segmentSize) {
//        this.segmentSize = segmentSize;
//    }
//
//    public ArrayList<QStoreSegment> getSegmentList() {
//        return segmentList;
//    }
//
//    public void setSegmentList(ArrayList<QStoreSegment> segmentList) {
//        this.segmentList = segmentList;
//    }
//
//    public Path getStorePath() {
//        return storePath;
//    }
//
//    public void setStorePath(Path storePath) {
//        this.storePath = storePath;
//    }
//
//    public Path getSegmentsPath() {
//        return segmentsPath;
//    }
//
//    public void setSegmentsPath(Path segmentsPath) {
//        this.segmentsPath = segmentsPath;
//    }
//
//    public Path getStoreSaveFilePath() {
//        return storeSaveFilePath;
//    }
//
//    public void setStoreSaveFilePath(Path storeSaveFilePath) {
//        this.storeSaveFilePath = storeSaveFilePath;
//    }
//
//    public QObjects getQobjs() {
//        return qobjs;
//    }
//
//    public void setQobjs(QObjects qobjs) {
//        this.qobjs = qobjs;
//    }
//
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//    //////// Persistence //////////////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void save() {
//        saveStore();
//    }
//
//    private void saveStore() {
//        qobjs.saveObjToFile(storeSaveFilePath, this);
//    }
//
//    private QStore loadStore() {
//        QStore store = (QStore) qobjs.loadObjFromFile(storeSaveFilePath);
//        this.uuid = store.uuid;
//        this.storeOwnerQNEId = store.storeOwnerQNEId;
//        this.savedStoreFilePathStr = store.savedStoreFilePathStr;
//        this.timestamp = store.timestamp;
//        this.segmentList = store.segmentList;
//        return store;
//    }
//
//    public long getNextSegmentBase() throws IOException {
//        if (getCurrentSegment() == null) return 0L;
//        return getCurrentSegment().getLastStoreEntry();
//    }
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//    /////// Persistence ///////////////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//
////    private void saveSegmentList() {
////        qobjs.saveObjToFile(segmentListFilePath, segmentList);
////    }
////
////    private void loadSegmentList() {
////        segmentInfoList = (ArrayList<QSegmentInfo>) qobjs.loadObjFromFile(segmentInfoListFilePath);
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////
//} //////// End Class //////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
//
