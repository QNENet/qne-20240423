package com.qnenet.qne.store.storesPrev.appendStore;

import org.jasypt.util.binary.AES256BinaryEncryptor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public interface QAppendStore {

    void init(Path appendStorePath, String storeName, int segmentCapacity, AES256BinaryEncryptor encryptor);

    long storeTotalObjCount();

    void closeSegment(QAppendStoreSegment segment) throws IOException;

    Long addObject(Object obj) ;

    Object getObject(long hugeObjId) throws IOException;

    //    public QBlockChainImpl(QNode node, String storeName, int segmentCapacity) throws IOException {
    //    }
    //

    //    public QBlockChainImpl(QNode node, String storeName, int segmentCapacity) throws IOException {
    //    }
    //

    void addNewSegment(long baseObjId) throws IOException;

//    QNode getNode();

    ArrayList<Object> getAllObjsWithClassId(int classId) throws IOException;

    long getStoreObjCount();

//    QSerialization getSerialization();

//    byte[] getEntryByHugeStoreIdx(long idx);

//    QStoreSegment openStoreSegment(int segmentId);
//
//    QStoreSegment createAppendStoreIdx(int segmentId);

//    Long appendBytes(byte[] bytes, int classId);
//
//    byte[] loadBytes(long appendStoreHugeIdx);


}
