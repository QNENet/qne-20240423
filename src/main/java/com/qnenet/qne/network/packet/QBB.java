//package com.qnenet.qne.packet;
//
//import java.nio.ByteBuffer;
//
//public class QBB {
//
//    private final ByteBuffer bb;
//
//    public QBB(int size) {
//        this.bb = ByteBuffer.allocate(size);
//    }
//
//    public void putShort(short shortValue) {
//        bb.putShort(shortValue);
//    }
//
//    public void putShort(int idx, short shortValue) {
//        bb.putShort(idx, shortValue);
//    }
//
//    public void putInt(int intValue) {
//        bb.putInt(intValue);
//    }
//
//    public void putInt(int idx, int intValue) {
//        bb.putInt(intValue);
//    }
//
//    public void putByte(byte byteValue) {
//        bb.put(byteValue);
//    }
//
//    public void putByte(int idx, byte byteValue) {
//        bb.put(byteValue);
//    }
//
//    public void putBytes(byte[] bytes) {
//        bb.put(bytes);
//    }
//
//    public void putBytes(int idx, byte[] bytes) {
//        bb.put(bytes);
//    }
//
//    public Short getShort() {
//        return bb.getShort();
//    }
//
//    public Short getShort(int idx) {
//        return bb.getShort(idx);
//    }
//
//    public int getInt() {
//        return bb.getInt();
//    }
//
//    public int getInt(int idx) {
//        return bb.getInt(idx);
//    }
//
//    public byte getByte() {
//        return bb.get();
//    }
//
//    public byte getByte(int idx) {
//        return bb.get(idx);
//    }
//
//    public byte[] getBytes(int size) {
//        byte[] bytes = new byte[size];
//        bb.get(bytes);
//        return bytes;
//    }
//
//    public byte[] getBytes(int idx, int size) {
//        byte[] bytes = new byte[size];
//        bb.get(idx, bytes);
//        return bytes;
//    }
//
//
//}
