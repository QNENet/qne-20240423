
package com.qnenet.qne.objects.classes;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.qnenet.qne.system.utils.QRandomUtils;

public class QNEPacket {

	public InetSocketAddress destInetSocketAddress;
	public short srcEPIdx;
	public short destEPIdx;
	public QPayload payload;
	public int channelId;
	public int packetId;

	public QNEPacket(short srcEPIdx, InetSocketAddress destInetSocketAddress, short destEPIdx, QPayload payload, int channelId) {
		this.srcEPIdx = srcEPIdx;
		this.destInetSocketAddress = destInetSocketAddress;
		this.destEPIdx = destEPIdx;
		this.payload = payload;
		this.channelId = channelId;
		this.packetId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE);
	}


	public QNEPacket(short srcEPIdx, String destIpAddress, int destPort, short destEPIdx, QPayload payload, int channelId) {
		this.srcEPIdx = srcEPIdx;
		this.destInetSocketAddress = new InetSocketAddress(destIpAddress, destPort);
		this.destEPIdx = destEPIdx;
		this.payload = payload;
		this.channelId = channelId;
		this.packetId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE);
	}
	public QNEPacket(short srcEPIdx, String destIpAddress, int destPort, short destEPIdx, QPayload payload, int channelId, int packetId) {
		this.srcEPIdx = srcEPIdx;
		this.destInetSocketAddress = new InetSocketAddress(destIpAddress, destPort);
		this.destEPIdx = destEPIdx;
		this.payload = payload;
		this.channelId = channelId;
		this.packetId = packetId;
	}

	public QNEPacket(DatagramPacket packet) {
		destInetSocketAddress = (InetSocketAddress) packet.getSocketAddress();
		ByteBuffer bb = ByteBuffer.wrap(packet.getData());
		srcEPIdx = bb.getShort();
		destEPIdx = bb.getShort();
		channelId = bb.getInt();
		packetId = bb.getInt();
		payload = new QPayload();
		payload.type = bb.get();
		payload.bytes = new byte[bb.remaining()];
		bb.get(payload.bytes);
	}



	public byte[] pack() {
		ByteBuffer bb = ByteBuffer.allocate(13 + payload.bytes.length);
		bb.putShort(srcEPIdx);
		bb.putShort(destEPIdx);
		bb.putInt(channelId);
		bb.putInt(packetId);
		bb.put(payload.type);
		bb.put(payload.bytes);
		return bb.array();
	}

}
