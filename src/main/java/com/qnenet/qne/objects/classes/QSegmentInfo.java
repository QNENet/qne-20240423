package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objects.classes;
//
//import com.qnenet.qne.utils.QBase36;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//public class QSegmentInfo {
//    public UUID segmentUUID;
//    public long timestamp;
//    public long segmentBaseStorePtr;
//
//    public QStoreInfo storeInfo;
//
//    public transient Long segmentLimitStorePtr;
////    public transient QStoreSegment segment;
//    public transient long lastUsed;
//    public transient long usedCount;
//
//    public boolean contains(long storePtr) {
//        if (segmentLimitStorePtr == -1L) { // is current Segmant
//            return true;
//        } else {
//            if (storePtr >= segmentBaseStorePtr && storePtr < segmentLimitStorePtr) return true; // is current add segment
//        }
//        return false;
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
