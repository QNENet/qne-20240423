//package com.qnenet.qne.data.blockchain.provider;
//
//import com.qnenet.qne.objects.impl.QNEObjects;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import org.jasypt.util.binary.AES256BinaryEncryptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.nio.channels.FileChannel;
//import java.util.ArrayList;
//
//@Component
//@Scope("prototype")
//public class QBlockFile {
//
//    @Autowired
//    QNEObjects qobjs;
//
//
//    long blockId;            //  8
//    long timestamp;          //  8
//    byte[] prevHash;         // 32
//    //    ArrayList<byte[]> dataArray;
//    ByteBuf nbb = Unpooled.buffer();
//
//    byte[] hash;             // 32
//
//    private FileChannel channel;
//    //    private ArrayList<byte[]> objBytesList = new ArrayList<>();
//    private ArrayList<Integer> itemPtrIdx = new ArrayList<>();
////    private ByteBuf workBB = Unpooled.buffer();
////    private ByteBuf finalBB = Unpooled.buffer();
////    private ByteBuf sizedBB = Unpooled.buffer();
//
//
////    public QBlockFile() {
////        this.dataArray = new ArrayList<>();
////    }
////
////    public QBlockFile(ArrayList<byte[]> dataArray) {
////    }
//
//    public byte[] createSizedBytes(byte[] bytes) {
//        ByteBuf sizedBB = Unpooled.buffer(bytes.length + 4);
//        sizedBB.writeInt(bytes.length);
//        sizedBB.writeBytes(bytes);
//        return sizedBB.array();
//    }
//
////    public ArrayList<byte[]> getObjBytesList() {
////        return objBytesList;
////    }
//
//    public void addEncBytes(byte[] encBytes) {
//        int ptr = nbb.writerIndex();
//        nbb.writeInt(encBytes.length);
//        nbb.writeBytes(encBytes);
//        itemPtrIdx.add(ptr);
//    }
//
//
//
//
////    public ByteBuf getWorkBB() {
////        return workBB;
////    }
////
////    public ByteBuf getFinalBB() {
////        return finalBB;
////    }
//
//    public ArrayList<Integer> createDataIdx(byte[] dataArray) {
//        ArrayList<Integer> idx = new ArrayList<>();
//        ByteBuf tmpBB = Unpooled.wrappedBuffer(dataArray);
//        int ptr = 0;
//        idx.add(ptr);
//        while (true) {
//            int bytesSize = tmpBB.getInt(ptr);
//            int entrySize = bytesSize + 4;
//            ptr += entrySize;
//            if (ptr >= dataArray.length) break;
//            idx.add(ptr);
//        }
//        return idx;
//    }
//
//    public Object loadObjByIdx(byte[] dataArray, int sizePtr, AES256BinaryEncryptor encryptor) {
//        ByteBuf nbb = Unpooled.wrappedBuffer(dataArray);
//        int objBytesSize = nbb.getInt(sizePtr);
//        byte[] objBytes = new byte[objBytesSize];
//        nbb.getBytes(sizePtr + 4, objBytes);
//        return qobjs.objFromEncBytes(objBytes, encryptor);
//    }
//
//    public ArrayList<Integer> getItemPtrIdx() {
//        return itemPtrIdx;
//    }
//
//    public Object getObj(int itemIdx, AES256BinaryEncryptor encryptor) {
//        int ptr = itemPtrIdx.get(itemIdx);
//        int objEncBytesSize = nbb.getInt(ptr);
//        byte[] objEncBytes = new byte[objEncBytesSize];
//        nbb.getBytes(ptr + 4, objEncBytes);
//        return qobjs.objFromEncBytes(objEncBytes, encryptor);
//    }
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
