package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objectsQNE.classes;
//
//import com.qnenet.qne.constants.QSysConstants;
//import com.qnenet.qne.store.api.QStore;
//import com.qnenet.qne.utils.QBase36;
//import io.netty.buffer.ByteBuf;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.UUID;
//
//@Service
//@Scope("prototype")
//public class QStoreSegment {
//    public int classId;
//    public long timestamp;
//    public UUID uuid;
//    public UUID prevUUID;
//    public UUID nextUUID;
//
//    public UUID storeUUID;
//    public long storeOwnerQNEId;
//    public long storeTimestamp;
//
//    public int addPtr;
//    public int lastSegmentItem;
//
//    public long segmentFirstStoreItem; // ptr to first Store Item
//
//    public transient RandomAccessFile raf; //null if not loaded
//    public transient long lastUsed;
//    public transient long usedCount;
//    public transient QStore store;
//    public transient String filePathStr;
//    public transient QStoreSegment prevStoreSegment;
//
//    public void initNewSegment(QStore store) throws IOException {
//        this.store = store;
//        this.storeUUID = store.uuid;
//        this.storeOwnerQNEId = store.getStoreOwnerQNEId();
//        this.storeTimestamp = store.timestamp;
//
//        uuid = UUID.randomUUID();
//        this.filePathStr = Paths.get(store.getSegmentsPath().toString(), QBase36.uuidToBase36Str(uuid)).toString();
//
//        this.timestamp = System.currentTimeMillis();
//        classId = store.getQobjs().getClassId(QStoreSegment.class);
//
//        open();
//
//        raf.writeLong(timestamp);                                       // 0  timestamp
//        raf.writeInt(classId); // 8 classId
//        raf.writeLong(uuid.getMostSignificantBits());                   // 12 uuid
//        raf.writeLong(uuid.getLeastSignificantBits());                  // 20
//
//        ArrayList<QStoreSegment> segmentList = store.getSegmentList();
//        if (segmentList.isEmpty()) {
//            raf.writeLong(0);                                            // 28 prev uuid
//            raf.writeLong(0);                                            // 36
//            raf.writeLong(0);                                            // 44 next uuid
//            raf.writeLong(0);                                            // 53
//            segmentFirstStoreItem = 0L;
//        } else {
//            prevStoreSegment = segmentList.get(segmentList.size() - 1);
//            setAndSaveNextUUID(uuid);
//
//            prevUUID = prevStoreSegment.uuid;
//
//            segmentFirstStoreItem = prevStoreSegment.getSegmentLastStoreItem();
//
//            raf.writeLong(prevUUID.getMostSignificantBits());                                            // 28 prev uuid
//            raf.writeLong(prevUUID.getLeastSignificantBits());                                            // 36
//            raf.writeLong(0);                                            // 44 next uuid
//            raf.writeLong(0);                                            // 53
//        }
//
//        raf.writeLong(storeUUID.getMostSignificantBits());              // 60 store uuid
//        raf.writeLong(storeUUID.getLeastSignificantBits());             // 68
//        raf.writeLong(storeOwnerQNEId);                                 // 76 store owner
//        raf.writeLong(storeTimestamp);                                  // 84 store timestamp
//
//        // entries ptr leaves spare room
//        raf.writeInt(addPtr);                                           // 92 ssegment add Ptr
//        raf.writeInt(lastSegmentItem);                                              // 96 ssegment last entry
//
//        raf.writeLong(store.getNextSegmentBase());                       // 100 ssegment base
//
//
//    }
//
//    private long getSegmentLastStoreItem() {
//        return segmentFirstStoreItem + lastSegmentItem;
//    }
//
//    private void setAndSaveNextUUID(UUID uuid) throws IOException {
//        open();
//        raf.seek(QSysConstants.SEGMENT_PTR_NEXT_UUID);
//        raf.writeLong(uuid.getMostSignificantBits());
//        raf.writeLong(uuid.getLeastSignificantBits());
//        nextUUID = uuid;
////        store.save();
//    }
//
//    private long getLastStoreItemPtr() {
//        return lastSegmentItem; // + lastSegmentPtr;
//    }
//
//    public void open() throws FileNotFoundException {
//        if (raf == null) {
//            raf = new RandomAccessFile(new File(filePathStr), "rws");
//        }
//    }
//
//
//    public int contains(long storeEntryPtr) throws IOException {
//        int segmentPtr = (int) (storeEntryPtr - segmentFirstStoreItem);
//        if (segmentPtr < 0) return -1;
//        if (segmentPtr > raf.length()) return 1;
//        return 0;
//    }
//
//    private int segmentPtrFromStorePtr(long storePtr) {
//        return (int) (storePtr - segmentFirstStoreItem);
//    }
//
//    public void bumpUsage() {
//        lastUsed = System.currentTimeMillis();
//        usedCount += 1;
//    }
//
//    public Path segmentFilePath() {
//        return Paths.get(store.getSegmentsPath().toString(), QBase36.uuidToBase36Str(uuid));
//    }
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//    //////// StoreObjs ////////////////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//
//    public int addStoreObjToCurrentSegment(QStoreObject storeObj) throws IOException {
//        ByteBuf bb = storeObj.toByteBuf();
//
//        raf.seek(QSysConstants.SEGMENT_PTR_ADD);
//        addPtr = raf.readInt();
//        lastSegmentItem = raf.readInt();
//        bb.setInt(4, lastSegmentItem);
//
//        raf.seek(lastSegmentItem);
//        raf.write(bb.array());
//
//        lastSegmentItem = addPtr;
//        addPtr += bb.array().length;
//
//        raf.seek(QSysConstants.SEGMENT_PTR_ADD);
//        raf.writeInt(addPtr);
//        raf.writeInt(lastSegmentItem);
//
//        store.save();
//
//        return lastSegmentItem;
//    }
//
//    public QStoreObject loadStoreObj(long storePtr) throws IOException {
//        int segmentPtr = (int) (storePtr - segmentFirstStoreItem);
//        raf.seek(segmentPtr);
//        QStoreObject storeObj = new QStoreObject();
//        storeObj.classId = raf.readInt();
//        storeObj.bytesFormat = raf.readByte();
//        int objBytesLength = raf.readInt();
//        storeObj.objBytes = new byte[objBytesLength];
//        raf.read(storeObj.objBytes);
//        return storeObj;
//    }
//
//    public long getLastStoreEntry() throws IOException {
//        open();
//        raf.seek(QSysConstants.SEGMENT_PTR_LAST_ENTRY);
//        int segmentLastEntry = raf.readInt();
//        long segmentBase = raf.readLong();
//        return segmentLastEntry + segmentBase;
//    }
//
//
////    ///////////////////////////////////////////////////////////////////////////////////////////////
////    //////// Persistence //////////////////////////////////////////////////////////////////////////
////    ///////////////////////////////////////////////////////////////////////////////////////////////
////
////    private void saveSegment() {
////        store.getQobjs().saveObjToFile(segmentFilePath, this);
////    }
////
////    private void loadSegment() {
////        store.getQobjs().loadObjFromFile(segmentFilePath);
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////
//} //////// End Class //////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
