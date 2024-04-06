///*
// * Copyright (C) 2022 Paul F Fraser <paulf@qnenet.com>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *     http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.qnenet.qne.data.blockchain.provider;
//
///*
// * Copyright (C) 2022 Paul F Fraser <paulf@qnenet.com>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *     http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Class /////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//import com.qnenet.qne.store.appendStore.blockchain.QBlock;
//import com.qnenet.qne.store.appendStore.blockchain.QBlockChain;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.file.Path;
//
////@Component(scope = ServiceScope.PROTOTYPE)
//@Component
//@Scope("prototype")
//public class QBlockChainImpl implements QBlockChain {
//
////    static Logger log = LoggerFactory.getLogger(QBlockChainImpl.class);
//
////    private static final int SEGMENT_HEADER_SIZE = 24;
//
////    @Autowired
////    QNEObjects qobjs;
//
//    private RandomAccessFile raf;
//    private String rafMode;
////    private ByteBuf headerBB;
////    private QAppendStore parentStore;
//
////    ArrayList<Integer> idxArrayList;
//    private Path rafFilePath;
////    private int capacity;
////    private long baseObjId;
////    private long rafId;
////    private int rafObjCount;
////    private int addDataPtr;
////    private AES256BinaryEncryptor encryptor;
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Activate //////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    @Activate
////    public void activate() {
////        log.info("Hello from -> " + getClass().getSimpleName());
////    }
////
////    @Deactivate
////    public void deactivate() {
////        try {
////            raf.close();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        log.info("Goodbye from -> " + getClass().getSimpleName());
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Init //////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Override
//    public void initRaf(Path rafFilePath, String rafMode) throws IOException {
//        this.rafFilePath = rafFilePath;
//        this.rafMode = rafMode;
//
//        openRaf();
//   }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Blcck Access //////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    @Override
//    public Long addBlock(QBlock block) {
//        long blockStartPtr = getRafSize();
//
//    }
//
////    @Override
////    public QAbstractEntity getObject(long objId) throws IOException {
////        int id = (int) (objId - baseObjId);
////        int ptr = idxArrayList.get(id);
////        raf.seek(ptr);
////        int allDataSize = raf.readInt();
////        byte[] data = new byte[allDataSize - 4];
////        raf.read(data);
////        ByteBuf bb = Unpooled.wrappedBuffer(data);
////        int classId = bb.readInt();
////        byte[] objData = new byte[bb.readableBytes()];
////        bb.readBytes(objData);
////        return (QAbstractEntity) qobjs.objFromEncBytes(objData, encryptor);
////    }
////
////    public Long addData(int classId, byte[] bytes) throws IOException {
////        int nextDataPtr = getAddDataPtr();
////        raf.seek(nextDataPtr);
////        int size = bytes.length + 4 + 4;
////        raf.writeInt(size);
////        raf.writeInt(classId);
////        raf.write(bytes);
////        bumpAddDataPtr(size); // when raf full this points to idx
////        int newCount = getSegmentObjCount() + 1;
////        setSegmentObjCount(newCount);
////        idxArrayList.add(nextDataPtr);
////        setHeader(); // entry not valid until header written (might help with power fail)
////
////        if (newCount >= capacity) {
////            // write idx to end of raf
////            int ptr = getAddDataPtr();
////            raf.seek(ptr);
////            for (int i = 0; i < idxArrayList.size(); i++) {
////                raf.writeInt(idxArrayList.get(i));
//////                ptr += 4;
//////                raf.seek(ptr);
////            }
////            parentStore.addNewSegment(getBaseObjId() + newCount);
////        }
////
//////        rafObjCount = newCount;
////
////        return baseObjId + newCount - 1;
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Raf ///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private void openRaf() {
//        raf = null;
//        try {
//            raf = new RandomAccessFile(rafFilePath.toFile(), rafMode);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void closeRaf() {
//        try {
//            raf.close();
//            raf = null;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public long getRafSize() {
//        try {
//            return raf.length();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Support Methods ///////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    private void setupIdxArrayFromData() throws IOException {
////        idxArrayList = new ArrayList<>();
////        int ptr = SEGMENT_HEADER_SIZE;
////        for (int i = 0; i < getSegmentObjCount(); i++) {
////            idxArrayList.add(ptr);
////            raf.seek(ptr);
////            int entrySize = raf.readInt();
////            ptr += entrySize;
////        }
////    }
////
////    @Override
////    public long getSegmentLastObjId() {
////        return baseObjId + rafObjCount;
////    }
////
////    @Override
////    public Long getNextObjId() {
////        return getSegmentLastObjId() + 1;
////    }
////
//
////    @Override
////    public void reopenReadOnly() {
////        try {
////            raf.close();
////            raf = new RandomAccessFile(rafPath.toFile(), "r");
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Getters & Setters /////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    public long getBaseObjId() {
////        baseObjId = headerBB.getLong(0);
////        return baseObjId;
////    }
////
////    public void setBaseObjId(long baseObjId) {
////        headerBB.setLong(0, baseObjId);
////        this.baseObjId = baseObjId;
////    }
////
////
////    public long getSegmentId() {
////        rafId = headerBB.getLong(8);
////        return rafId;
////    }
////
////    public void setSegmentId(long rafId) {
////        headerBB.setLong(8, rafId);
////        this.rafId = rafId;
////    }
////
////    public int getSegmentObjCount() {
////        int rafObjCount = headerBB.getInt(16);
////        return rafObjCount;
////    }
////
////    public void setSegmentObjCount(int rafObjCount) {
////        headerBB.setInt(16, rafObjCount);
////        this.rafObjCount = rafObjCount;
////    }
////
////    public boolean bumpIdCount() {
////        int newCount = getSegmentObjCount() + 1;
////        if (newCount > capacity) return false;
////        setSegmentObjCount(newCount);
////        return true;
////    }
////
////    public int getAddDataPtr() {
////        int addDataPtr = headerBB.getInt(20);
////        return addDataPtr;
////    }
////
////    public int setAddDataPtr(int addDataPtr) {
////        headerBB.setInt(20, addDataPtr);
////        return addDataPtr;
////    }
////
////    public int bumpAddDataPtr(int bumpSize) {
////        int addDataPtr1 = getAddDataPtr();
////        int addDataPtr = addDataPtr1 + bumpSize;
////        setAddDataPtr(addDataPtr);
////        return addDataPtr;
////    }
//
//
////    @Override
////    public long getLastObjId() {
////        return baseObjId + getSegmentIdCount();
////    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} /////// End Class ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
