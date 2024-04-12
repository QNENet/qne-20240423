// package com.qnenet.qne.store.api;


// import com.qnenet.qne.objects.classes.QStoreObject;

// import java.io.IOException;
// import java.nio.file.Path;
// import java.util.ArrayList;

// public interface QStore {

//     void init(String storeName, long storeOwner, Path storePath) throws IOException;

//     public boolean addObject(QStoreObject obj);

//     public QStoreObject getObject(Object objId);

//     public Long getTotalObjCount();

//     public ArrayList<Object> getAllObjsWithClassId(int classId);

//     public long addByte(byte byteVal);
//     public long addInt(int intVal);
//     public long addLong(long longVal);
//     public long addBytes(byte[] bytesVal);
//     public long addString(String stringVal);

//     public long setByte(long ptr, byte byteVal);
//     public long setInt(long ptr, int intVal);
//     public long setLong(long ptr, long longVal);
//     public long setBytes(long ptr, byte[] bytesVal);
//     public long setString(long ptr, String stringVal);

//     public byte getByte(long ptr);
//     public int getInt(long ptr);
//     public long getLong(long ptr);
//     public byte[] getBytes(long ptr, int size);
//     public String getString(long ptr);
// }