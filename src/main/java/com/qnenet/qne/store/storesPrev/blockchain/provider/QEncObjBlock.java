//package com.qnenet.qne.store.storesPrev.blockchain.provider;
//
//import com.qnenet.qne.objects.impl.QNEObjects;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import org.jasypt.util.binary.AES256BinaryEncryptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//
//@Component
//@Scope("prototype")
//public class QEncObjBlock {
//
//    @Autowired
//    QNEObjects qobjs;
//
//    ByteBuf nbb = Unpooled.buffer();
//
//    private ArrayList<Integer> itemPtrIdx = new ArrayList<>();
//
////    public void addEncBytes(byte[] encBytes) {
////        int ptr = nbb.writerIndex();
////        nbb.writeInt(encBytes.length);
////        nbb.writeBytes(encBytes);
////        itemPtrIdx.add(ptr);
////    }
//
//    public int addObj(Object obj, AES256BinaryEncryptor encryptor) {
//        int ptr = nbb.writerIndex();
//        byte[] encBytes = qobjs.objToEncBytes(obj, encryptor);
//        nbb.writeInt(encBytes.length);
//        nbb.writeBytes(encBytes);
//        itemPtrIdx.add(ptr);
//        return ptr;
//    }
//
//    public Object getObjByIdx(int itemIdx, AES256BinaryEncryptor encryptor) {
//        int ptr = itemPtrIdx.get(itemIdx);
//        int objBytesSize = nbb.getInt(ptr);
//        byte[] objBytes = new byte[objBytesSize];
//        nbb.getBytes(ptr + 4, objBytes);
//        return qobjs.objFromEncBytes(objBytes, encryptor);
//    }
//
//    public ArrayList<Integer> getItemPtrIdx() {
//        return itemPtrIdx;
//    }
//
//    public byte[] getAllBytes() {
//        nbb.capacity(nbb.writerIndex());
//        return nbb.array();
//    }
//
//    public int getNbbWriterIndex() {
//        return nbb.writerIndex();
//    }
//
////    public ArrayList<Integer> getItemPtrIdx() {
////        return itemPtrIdx;
////    }
////
////    public Object getObj(int itemIdx, AES256BinaryEncryptor encryptor) {
////        int ptr = itemPtrIdx.get(itemIdx);
////        int objEncBytesSize = nbb.getInt(ptr);
////        byte[] objEncBytes = new byte[objEncBytesSize];
////        nbb.getBytes(ptr + 4, objEncBytes);
////        return qobjs.objFromEncBytes(objEncBytes, encryptor);
////    }
//
//
////    public MappedByteBuffer init(Path path) throws IOException {
////        RandomAccessFile raf = new RandomAccessFile(path.toFile(), "rw");
////        channel = raf.getChannel();
////        MappedByteBuffer mbb = channel.map(FileChannel.MapMode.READ_WRITE, 0, Integer.MAX_VALUE);
////        return mbb;
////    }
////
////
////    public ByteBuffer init(Path path, byte[] prevHash) throws IOException {
////        MappedByteBuffer mbb = init(path);
////        mbb.put(prevHash);
////        mbb.putLong(System.currentTimeMillis());
////        return mbb;
////    }
////
////
////    public void close() throws IOException {
////       channel.close();
////    }
//
//}
