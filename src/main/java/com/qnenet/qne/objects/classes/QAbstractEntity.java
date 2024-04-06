package com.qnenet.qne.objects.classes;

import java.util.Map;
import java.util.UUID;

public abstract class QAbstractEntity {

    /*
    when serialized if long value = 0 then only one byte in serialized result
    same if we use Long and is null one byte is in serialized result
    early entries in the append store when serialized have the long values
    effectively compressed into bytes the ints the longs
    map when not used (null) also only one byte
     */
    private UUID uuid;
    private int segmentId;
    private int classId;
    private long thisPtr;
    private long prevPtr;
    private long nextPtr;
    private long timestamp;

    private Map<Object, Object> map;


    public Map<Object, Object> getMap() {
        return map;
    }

    public void setMap(Map<Object, Object> map) {
        this.map = map;
    }

    public UUID getUUID() {
//        if (uuid == null) uuid = UUID.randomUUID();
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

//    public void setUUIDFromStr(String uuidStr) {
//        this.uuid = UUID.fromString(uuidStr);
//    }
//
//    public String getUUIDStr() {
//        return uuid.toString();
//    }


//    public Long getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Long timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public ArrayList<String> getTags() {
//        return tags;
//    }
//
//    public void setTags(ArrayList<String> tags) {
//        this.tags = tags;
//    }

    @Override
    public int hashCode() {
        if (uuid != null) {
            return uuid.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof QAbstractEntity)) {
            return false; // null or other class
        }
        QAbstractEntity other = (QAbstractEntity) obj;

        if (uuid != null) {
            return uuid.equals(other.uuid);
        }
        return super.equals(other);
    }


}
