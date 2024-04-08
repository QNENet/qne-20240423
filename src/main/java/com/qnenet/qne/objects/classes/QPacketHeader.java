/*
 * Copyright (C) 2022 Paul F Fraser <paulf@qnenet.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qnenet.qne.objects.classes;

/**
 * Represents the header of a QPacket.
 */
public class QPacketHeader {

	public byte type;
	public int channelId;
	public int packetId;
	public short sourceEPIdx;
	public short destinationEPIdx;

	/**
	 * Constructs a new QPacketHeader object with the specified values.
	 *
	 * @param type              the type of the packet
	 * @param channelId         the ID of the channel
	 * @param packetId          the ID of the packet
	 * @param sourceEPIdx       the index of the source endpoint
	 * @param destinationEPIdx  the index of the destination endpoint
	 */
	public QPacketHeader(byte type, int channelId, int packetId, short sourceEPIdx, short destinationEPIdx) {
		this.type = type;
		this.channelId = channelId;
		this.packetId = packetId;
		this.sourceEPIdx = sourceEPIdx;
		this.destinationEPIdx = destinationEPIdx;
	}

	/**
	 * Constructs a new QPacketHeader object from the given byte array.
	 *
	 * @param bb the byte array containing the packet header data
	 */
	public QPacketHeader(byte[] bb) {
		this.type = bb[0];
		this.channelId = (bb[1] << 8) | bb[2];
		this.packetId = (bb[3] << 8) | bb[4];
		this.sourceEPIdx = (short) ((bb[5] << 8) | bb[6]);
		this.destinationEPIdx = (short) ((bb[7] << 8) | bb[8]);
	}
}
