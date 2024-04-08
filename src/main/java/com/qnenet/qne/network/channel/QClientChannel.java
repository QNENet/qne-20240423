package com.qnenet.qne.network.channel;

import java.net.DatagramPacket;

// noise-java is a Java implementation of the Noise Protocol Framework

// https://noiseprotocol.org/

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.qnenet.qne.objects.classes.QEPAddr;
import com.qnenet.qne.objects.classes.QPayload;

public class QClientChannel extends QChannel {

    private static final long CHANNEL_MSG_TIMEOUT_1 = 2000;
    private static final long CHANNEL_MSG_TIMEOUT_2 = 4000;
    private static final long CHANNEL_MSG_TIMEOUT_3 = 8000;
    private static final int HANDSHAKE_BUFFER_SIZE = 65507;

    private InetSocketAddress destination;
    private ByteBuffer firstPayloadBuffer;

    public QClientChannel(QEPAddr destination,  QPayload firstPayload)  {
        // int channelId = getUniqueChannelId();
        // this.destination = destination;
        // setupPacket(destination, firstPayloadPacketType, firstPayload);

        // byte packetType = 0; // specify your packet type here
        // int packetHeaderchannelId = 0; // specify your channel id here
        // int packetHeaderpacketId = 0; // specify your packet id here
        // short packetHeaderSourceEPIdx = 0; // specify your channel type here
        // short packetHeaderDestinationEPIdx = 0; // specify your channel type here
        // int offset = 0; // specify your offset here
        // int length = firstPayload.length; // specify your length here
        // this.firstPayloadBuffer = ByteBuffer.wrap(firstPayload, offset, length);
        
    }

    @Override
    public void handlePacketBB(ByteBuffer bb, InetSocketAddress srcInetAddr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handlePacketBB'");
    } 


    // @Override
    // public void handlePacketBB(ByteBuffer bb, InetSocketAddress srcInetAddr) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'handlePacketBB'");
    // }
}
