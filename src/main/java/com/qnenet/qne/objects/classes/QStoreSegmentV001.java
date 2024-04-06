package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objects.classes;
//
//import com.qnenet.qne.utils.QBase36;
//
//import java.io.RandomAccessFile;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//public class QStoreSegmentV001 {
//    public int classId;
//    public long timestamp;
//    public UUID thisUUID;
//    public UUID prevUUID;
//    public UUID nextUUID;
//    public int storeInfoPtr;   // ptr to storeInfo
//    public long baseStoreId;
//    public int firstItemPtr;   // ptr to firstItem
//    public int lastItemPtr;   // ptr to lastItem
//    public int updatesPtr;   // ptr to updates which are used after the segment is full
//
//    public transient RandomAccessFile raf; //null if not loaded
//    public transient long lastUsed;
//    public transient long usedCount;
//    public transient QStoreInfo storeInfo;
//    public transient String fileName;
//
//    public QStoreSegmentV001(QStoreInfo storeInfo, long baseStoreId) {
//        this.storeInfo = storeInfo;
//        this.baseStoreId = baseStoreId;
//        this.fileName = Paths.get(storeInfo.storePathStr, "segments", QBase36.);
//    }
//
//    public boolean contains(long storePtr) {
//        int segmentPtr = segmentPtrFromStorePtr(storePtr);
//        if (segmentPtr >= firstItem && segmentPtr <= lastItem ) return true;
//        return false;
//    }
//
//    private int segmentPtrFromStorePtr(long storePtr) {
//       return (int) (storePtr -  baseStoreId);
//    }
//
//    public void bumpUsage() {
//        lastUsed = System.currentTimeMillis();
//        usedCount += 1;
//    }
//
//    public Path segmentFilePath() {
//        return Paths.get(storeInfo.storePathStr, "segments", QBase36.uuidToBase36Str(segmentUUID));
//    }
//
//}
