//package com.qnenet.qne.store.storesPrev.blockchain.api;
//
//import com.qnenet.qne.objects.impl.QNEObjects;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.nio.ByteBuffer;
//import java.util.ArrayList;
//
//@Component
//@Scope("prototype")
//public class QBlock {
//
//    @Autowired
//    QNEObjects qobjs;
//
//    long blockId;            //  8
//    byte[] prevHash;         // 32
//    long timestamp;          //  8
//    ArrayList<byte[]> data;
//
//    byte[] hash;             // 32
//                             // 80
//    public QBlock() {
//    }
//
//    public void initBlock(byte[] prevHash) {
//        this.prevHash = prevHash;
//    }
//
//    public byte[] asBytes() {
//        byte[] dataBytes = qobjs.objToBytes(data);
//
//        ByteBuffer bb = ByteBuffer.allocateDirect(dataBytes.length + 48);
//        bb.putLong(blockId);
//        bb.putLong(timestamp);
//        bb.put(prevHash);
//        bb.put(dataBytes);
//
////        Blake3 hasher = Blake3.newInstance();
////        hasher.update(bb.array());
////
////        hash = hasher.digest();
////        bb.put(hash);
//        return bb.array();
//    }
//
//
//}
