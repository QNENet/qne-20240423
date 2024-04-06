//package com.qnenet.qne.data.blockchain.provider;
////
////public class QBlockChain {
////
////    public QBlockChain() {
////    }
////}
//
//import com.qnenet.qne.store.appendStore.blockchain.QBlockChain;
//import com.qnenet.qne.system.utils.QFileUtils;
//import org.jasypt.util.binary.AES256BinaryEncryptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.nio.file.Path;
//
////@Component(scope = ServiceScope.PROTOTYPE)
//@Service
//@Scope("prototype")
//public class QBlockChainImpl2 implements QBlockChain {
//
////    private static final int MAX_QUEUE_SIZE = 5;
////    static Logger log = LoggerFactory.getLogger(QBlockChainImpl.class);
//
//////////////  Custom  /////////////////////////////////////////////////////////////////////////////
//
//    @Autowired
//    ApplicationContext appCtx;
////    @Reference
////    private ComponentServiceObjects<QAppendStoreSegment> segmentFactory;
////
////    @Reference
////    QSerialization serialization;
//
//    private Path blockChainStorePath;
//
////    private QAppendStoreSegment currentAddSegment;
//
////    LinkedList<Long> openSegmentsQueue = new LinkedList<>();
////    Map<Long, QAppendStoreSegment> openSegmentsMap = new ConcurrentHashMap<>();
////
////    int maxQueueSize = 5;
////
////    private ArrayList<Long> segmentList = new ArrayList<>();
////
////    //    private QNode parentNode;
//////    private Path nodeStoresPath;
////    private String storeName;
////    private int segmentCapacity;
////    private AES256BinaryEncryptor encryptor;
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Activate //////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Init //////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    //    public QBlockChainImpl(QNode node, String storeName, int segmentCapacity) throws IOException {
////    }
////
//    @Override
//    public void init(Path blockChainStorePath, String storeName,
//                     int segmentCapacity, AES256BinaryEncryptor encryptor) {
////        this.parentNode = node;
//        this.blockChainStorePath = blockChainStorePath;
////        this.storeName = storeName;
////        this.segmentCapacity = segmentCapacity;
////        this.encryptor = encryptor;
////        nodeStoresPath = parentNode.getStoresPath();
//
//        try {
//
//            if (QFileUtils.checkDirectory(blockChainStorePath)) {
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
////        addNewSegment(0);
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Restart /////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void restart() throws IOException {
////        setsegmentList();
////        Long lastSegmentBaseObjId = segmentList.get(segmentList.size() - 1);
////        Path segmentDataPath = Paths.get(blockChainStorePath.toString(), QStringUtils.long19(lastSegmentBaseObjId));
//////        currentAddSegment = new QBlockChainImpl(this, segmentDataPath, "rwd", segmentCapacity, encryptor);
////        currentAddSegment = appCtx.getBean(QAppendStoreSegment.class);
////        currentAddSegment.init(this, segmentDataPath, "rwd", segmentCapacity, encryptor);
//    }
//
//
////    private void setsegmentList() {
////        segmentList.clear();
////        List<String> fileNames = Arrays.asList(blockChainStorePath.toFile().list());
////        Collections.sort(fileNames);
////        for (String fileName : fileNames) {
////            segmentList.add(Long.valueOf(fileName));
////        }
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Find Segment //////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    private boolean isObjectInAddSegment(long objId) {
////        if ((objId >= currentAddSegment.getBaseObjId()) && (objId <= currentAddSegment.getSegmentLastObjId()))
////            return true;
////        return false;
////    }
////
////    private int findSegmentIdContainingObject(long objId) {
////        Long thisId = -0L;
////        for (int i = 0; i < segmentList.size(); i++) {
////            thisId = segmentList.get(i);
////            if (objId >= thisId && objId < thisId + segmentCapacity) return i;
////        }
////        return -1;
////    }
////
////
////    private QAppendStoreSegment getSegmentContainingObject(long objId) throws IOException {
////        if (isObjectInAddSegment(objId)) return currentAddSegment;
////        int segmentId = findSegmentIdContainingObject(objId);
////        if (segmentId < 0) return null;
////        openSegmentsMap.get(segmentId);
////        if (openSegmentsQueue.contains(segmentId)) {
////            return openSegmentsMap.get(segmentId);
////        }
////        Long segmentBaseObjId = segmentList.get(segmentId);
////        return openSegment(segmentBaseObjId);
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Instances /////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    @Override
////    public void addNewSegment(long baseObjId) throws IOException {
//////        if (addSegment != null) {
//////            Path segmentFilePath = addSegment.getSegmentFilePath();
//////            int capacity = addSegment.getCapacity();
//////            closeSegment(addSegment);
//////
//////            QAppendStoreSegment prevSegment = segmentFactory.getService();
//////            prevSegment.init(this, segmentFilePath, "r", capacity);
//////            openSegmentsQueue.addLast((int) prevSegment.getSegmentId());
//////            openSegmentsMap.put((int) prevSegment.getSegmentId(), prevSegment);
//////            if (openSegmentsQueue.size() > maxQueueSize) {
//////                long segmentId = openSegmentsQueue.removeFirst();
//////                openSegmentsMap.remove(segmentId);
//////            }
//////        }
////
////        Path segmentDataPath = Paths.get(blockChainStorePath.toString(), QStringUtils.long19(baseObjId));
////        segmentList.add(baseObjId);
////        int segmentId = segmentList.size() - 1;
////
////        currentAddSegment = appCtx.getBean(QAppendStoreSegment.class);
//////        currentAddSegment = segmentFactory.getService();
////        currentAddSegment.initNewAppendStoreSegment(this, segmentDataPath, "rwd",
////                segmentCapacity, baseObjId, segmentId, encryptor);
////
////    }
//
////    private QAppendStoreSegment openSegment(long segmentBaseObjId) throws IOException {
////        QAppendStoreSegment segment = openSegmentsMap.get(segmentBaseObjId);
////        if (segment != null) return segment;
////
////        Path segmentDataPath = Paths.get(blockChainStorePath.toString(), QStringUtils.long19(segmentBaseObjId));
////
//////        QAppendStoreSegment openedSegment = segmentFactory.getService();
//////        openedSegment.init(this, segmentDataPath, "r", segmentCapacity);
////
//////        QAppendStoreSegment openedSegment = new QBlockChainImpl(this, segmentDataPath, "r", segmentCapacity);
////        QAppendStoreSegment openedSegment = appCtx.getBean(QAppendStoreSegment.class);
//////        QAppendStoreSegment openedSegment = segmentFactory.getService();
////        openedSegment.init(this, segmentDataPath, "r", segmentCapacity, encryptor);
////
////        //        long opendSegmentId = openedSegment.getSegmentId();
////        long baseObjId = openedSegment.getBaseObjId();
////        openSegmentsQueue.addLast(baseObjId);
////        openSegmentsMap.put(baseObjId, openedSegment);
////
////        if (openSegmentsQueue.size() > maxQueueSize) {
////            long removedSegmentBaseObjId = openSegmentsQueue.removeFirst();
////            QAppendStoreSegment removedSegment = openSegmentsMap.remove(removedSegmentBaseObjId);
////            closeSegment(removedSegment);
////        }
////        return openedSegment;
////    }
//
////    @Override
////    public void closeSegment(QAppendStoreSegment segment) throws IOException {
////        segment.closeRaf();
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Store Methods /////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    @Override
////    public Long addObject(Object obj) {
////        return currentAddSegment.addObject(obj);
////    }
////
////    @Override
////    public Object getObject(long objId) throws IOException {
////        QAppendStoreSegment segment = getSegmentContainingObject(objId);
////        return segment.getObject(objId);
////    }
////
////    @Override
////    public long storeTotalObjCount() {
////        return currentAddSegment.getSegmentLastObjId();
////    }
////
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// getters & setters /////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    @Override
////    public QNode getNode() {
////        return parentNode;
////    }
//
////    @Override
////    public ArrayList<Object> getAllObjsWithClassId(int classId) throws IOException {
////        long size = getStoreObjCount();
////        int segmentcount = segmentList.size();
////        ArrayList<Object> objList = new ArrayList<>();
////
////        for (int i = 0; i < segmentcount; i++) {
////            Long baseObjId = segmentList.get(i);
////            QAppendStoreSegment segment = openSegment(baseObjId);
////            segment.getObjectsForClassId(classId, objList);
////        }
////        return objList;
////    }
////
////    @Override
////    public long getStoreObjCount() {
////        return currentAddSegment.getSegmentLastObjId();
////    }
////
//////    @Override
//////    public QSerialization getSerialization() {
//////        return serialization;
//////    }
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//} /////// End Class ////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
