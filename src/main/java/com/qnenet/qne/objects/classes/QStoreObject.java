package com.qnenet.qne.objects.classes;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class QStoreObject {
    public int classId;
    public byte[] objBytes;
    public byte bytesFormat;

    public ByteBuf toByteBuf() {
        int size = 4 + 4 + 4 + 1 + objBytes.length;
        ByteBuf bb = Unpooled.buffer(size);
        bb.writeInt(size);
        bb.writeInt(-1);
        bb.writeInt(classId);
        bb.writeByte(bytesFormat);
        bb.writeBytes(objBytes);
        return bb;
    }

    public QStoreObject fromBytes(byte[] storeObjectBytes) {
        QStoreObject storeObj = new QStoreObject();
        ByteBuf bb = Unpooled.wrappedBuffer(storeObjectBytes);
        storeObj.classId = bb.getInt(8);
        storeObj.bytesFormat = bb.getByte(12);
        storeObj.objBytes = new byte[storeObjectBytes.length - 4 - 1];
        bb.readBytes(objBytes);
        return storeObj;
    }

}
