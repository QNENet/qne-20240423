package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objects.classes;
//
//import java.util.ArrayList;
//import java.util.Map;
//
//public abstract class QAbstractEntity2 {
//
//    /*
//    when serialized if long value = 0 then only one byte in serialized result
//    same if we use Long and is null one byte is in serialized result
//    early entries in the append store when serialized have the long values
//    effectively compressed into bytes the ints the longs
//    map when not used (null) also only one byte
//     */
//
//    // block chain
//    private long timestamps;       // 8 bytes idx -> 0
//    private long prevHash;       // 8 bytes idx -> 0
//    private ArrayList<byte[]> data;       // 8 bytes idx -> 0
//    private long hash;       // 8 bytes idx -> 0
//
//
//
//
//    // memberId and entry pointer make up the uuid
//    private long memberId;       // 8 bytes idx -> 0
//    private long entryPtr;       // 8 bytes idx -> 7
//    private long prevPtr;        // 8 bytes idx -> 15
//    private long nextPtr;        // 8 bytes idx -> 23
//    private long sysMilliSecs;   // 8 bytes idx -> 31
//
//    private int classId;         // 4 bytes idx -> 39
//
//    private Map<Object, Object> map; // idx -> 35 if null -> 1byte
//
//    private byte[] data;
//
//    private int hash32; // lower 32 bits of sha1
//
//    // 37 bytes header size
//
//    public Map<Object, Object> getMap() {
//        return map;
//    }
//
//    public void setMap(Map<Object, Object> map) {
//        this.map = map;
//    }
//
//    public long getMemberId() {
//        return memberId;
//    }
//
//    public void setMemberId(long memberId) {
//        this.memberId = memberId;
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
//    public int getSegmentId() {
//        return segmentId;
//    }
//
//    public void setSegmentId(int segmentId) {
//        this.segmentId = segmentId;
//    }
//
//    public int getClassId() {
//        return classId;
//    }
//
//    public void setClassId(int classId) {
//        this.classId = classId;
//    }
//
//    public int getThisSegmentPtr() {
//        return thisSegmentPtr;
//    }
//
//    public void setThisSegmentPtr(int thisSegmentPtr) {
//        this.thisSegmentPtr = thisSegmentPtr;
//    }
//
//    public int getPrevSegmentPtr() {
//        return prevSegmentPtr;
//    }
//
//    public void setPrevSegmentPtr(int prevSegmentPtr) {
//        this.prevSegmentPtr = prevSegmentPtr;
//    }
//
//    public int getNextSegmentPtr() {
//        return nextSegmentPtr;
//    }
//
//    public void setNextSegmentPtr(int nextSegmentPtr) {
//        this.nextSegmentPtr = nextSegmentPtr;
//    }
//
//    @Override
//    public int hashCode() {
//        if (uuid != null) {
//            return uuid.hashCode();
//        }
//        return super.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (!(obj instanceof QAbstractEntity2)) {
//            return false; // null or other class
//        }
//        QAbstractEntity2 other = (QAbstractEntity2) obj;
//
//        if (memberId != 0 && timestamp != 0) {
//            return uuid.equals(other.uuid);
//        }
//        return super.equals(other);
//    }
//
//
//}
