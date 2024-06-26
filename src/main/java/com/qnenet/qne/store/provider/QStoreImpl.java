package com.qnenet.qne.store.provider;


import com.qnenet.qne.objects.classes.QStoreObject;
import com.qnenet.qne.system.utils.QFileUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.*;

// @Service
// @Scope("prototype")
public class QStoreImpl {

    // @Autowired
    // QNEObjects qobjs;

    private Path storePath;
    private long creationTimestamp;
    private String storeName;
    private Path filePath;
    private MemorySegment segment;
    private long addPtr;
    private long readPtr;
    private long addPtrLocation;
    private long readPtrLocation;

    private UUID uuid;
    private long storeOwner;
    private RandomAccessFile raf;
    private long position;
    private long writePtr;
    private long writePtrLocation;


    protected QStoreImpl(String storeName, long storeOwner, Path storePath) throws IOException {
        this.storeName = storeName;
        this.storeOwner = storeOwner;
        this.storePath = storePath;
        if (QFileUtils.checkDirectory(storePath)) {
            newSystem();
        } else {
            restart();
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////
/////// Init //////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    // @Override
    // public void init(String storeName, long storeOwner, Path storePath) throws IOException {
    //     this.storeName = storeName;
    //     this.storeOwner = storeOwner;
    //     this.storePath = storePath;
    //     if (QFileUtils.checkDirectory(storePath)) {
    //         newSystem();
    //     } else {
    //         restart();
    //     }
    // }

//////////////////////////////////////////////////////////////////////////////////////////////////
/////// New System ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    private void newSystem() throws IOException {

        this.uuid = UUID.randomUUID();
        this.creationTimestamp = System.currentTimeMillis();
        this.filePath = Paths.get(storePath.toString(), storeName);

        // QStoreInfo info = new QStoreInfo(filePath.toString());

        open();

//        segment.set(ValueLayout.JAVA_LONG, 0L, creationTimestamp);
//
//        long atIndex = segment.getAtIndex(ValueLayout.JAVA_LONG, 0L);

//        raf.writeLong(creationTimestamp);
//        raf.writeLong(uuid.getMostSignificantBits());
//        raf.writeLong(uuid.getLeastSignificantBits());
    }

    // private void setPosition(long l) {
    //     position = 0L;
    // }

//////////////////////////////////////////////////////////////////////////////////////////////////
/////// Restart //////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////

    private void restart() throws IOException {
        open();
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Store Methods /////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////


    public void open() throws IOException {

        Set<OpenOption> openOptions = Set.of(CREATE_NEW, SPARSE, READ, WRITE);
        try (var fc = FileChannel.open(filePath, openOptions);

             var arena = Arena.ofConfined()) {
            long size = 1L << 32;
            System.out.println(size);
            segment = fc.map(FileChannel.MapMode.READ_WRITE, 0, 1L << 32, arena);

            segment.set(ValueLayout.JAVA_LONG, 0L, creationTimestamp);

            long ts = segment.getAtIndex(ValueLayout.JAVA_LONG, 0L);

            System.out.println(ts);
        } // Resources allocated by "mapped" is released here via TwR


//        if (raf == null) {
//            try {
//                raf = new RandomAccessFile(filePath.toFile(), "rws");
//            } catch (FileNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }

    public boolean addObject(QStoreObject obj) {
        return false;
    }

    public QStoreObject getObject(Object objId) {
        return null;
    }

    public Long getTotalObjCount() {
        return null;
    }

    public ArrayList<Object> getAllObjsWithClassId(int classId) {
        return null;
    }

    public long addByte(byte byteVal) {
        segment.set(ValueLayout.JAVA_BYTE, addPtr, byteVal);
        return bumpAddPtr(Byte.SIZE);
    }

    public long addInt(int intVal) {
        segment.set(ValueLayout.JAVA_INT, addPtr, intVal);
        return bumpAddPtr(Integer.SIZE);
    }

    public long addLong(long longVal) {
        segment.set(ValueLayout.JAVA_LONG, addPtr, longVal);
        return bumpAddPtr(Long.SIZE);
    }

    public long addBytes(byte[] bytesVal) {
        MemorySegment slice = segment.asSlice(addPtr, bytesVal.length);
        slice.copyFrom(MemorySegment.ofArray(bytesVal));
        return bumpAddPtr(bytesVal.length);
    }

    public long addString(String stringVal) {
        segment.setString(addPtr, stringVal);
        return bumpAddPtr(stringVal.length());
    }

    public long setByte(long ptr, byte byteVal) {
        segment.set(ValueLayout.JAVA_BYTE, ptr, byteVal);
        return bumpAddPtr(Byte.SIZE);
    }

    public long setInt(long ptr, int intVal) {
        segment.set(ValueLayout.JAVA_INT, ptr, intVal);
        return bumpAddPtr(Integer.SIZE);
    }

    public long setLong(long ptr, long longVal) {
        segment.set(ValueLayout.JAVA_LONG, ptr, longVal);
        return bumpAddPtr(Long.SIZE);
    }

    public long setBytes(long ptr, byte[] bytesVal) {
        MemorySegment slice = segment.asSlice(ptr, bytesVal.length);
        slice.copyFrom(MemorySegment.ofArray(bytesVal));
        return bumpAddPtr(bytesVal.length);
    }

    public long setString(long ptr, String stringVal) {
        segment.setString(ptr, stringVal);
        return bumpAddPtr(stringVal.length());
    }

    public byte getByte(long ptr) {
        return segment.get(ValueLayout.JAVA_BYTE, ptr);
    }

    public int getInt(long ptr) {
        return segment.get(ValueLayout.JAVA_INT, ptr);
    }

    public long getLong(long ptr) {
        return segment.get(ValueLayout.JAVA_LONG, ptr);
    }

    public byte[] getBytes(long ptr, int size) {
        return null;
    }

    public String getString(long ptr) {
        String str = segment.getString(ptr);
        bumpReadPtr(str.length());
        return str;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// getters & setters /////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

//    public void doit() {
//        MemoryLayout POINT_2D = MemoryLayout.structLayout(
//                ValueLayout.JAVA_DOUBLE.withName("x"),
//                ValueLayout.JAVA_DOUBLE.withName("y")
//        );
//
//        VarHandle handle_x = POINT_2D.varHandle(MemoryLayout.PathElement.groupElement("x"));
//        VarHandle handle_y = POINT_2D.varHandle(MemoryLayout.PathElement.groupElement("y"));
//        try (ResourceScope scope = ResourceScope.newConfinedScope()) {
//            MemorySegment point = MemorySegment.allocateNative(POINT_2D);
//            handle_x.set(point, 3d);
//            handle_y.set(point, 4d);
//        } // free
//    }

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Persistence ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    private long bumpAddPtr(int size) {
        addPtr += size;
        saveAddPtr();
        return addPtr;
    }

    private long bumpReadPtr(int size) {
        readPtr += size;
        saveReadPtr();
        return readPtr;
    }

    private void saveAddPtr() {
        segment.set(ValueLayout.JAVA_LONG, addPtrLocation, addPtr);
    }

    private long loadAddPtr() {
        addPtr = segment.get(ValueLayout.JAVA_LONG, addPtrLocation);
        return addPtr;
    }

    private void saveReadPtr() {
        segment.set(ValueLayout.JAVA_LONG, readPtrLocation, readPtr);
    }

    private long loadReadPtr() {
        readPtr = segment.get(ValueLayout.JAVA_LONG, readPtrLocation);
        return readPtr;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
} /////// End Class ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
