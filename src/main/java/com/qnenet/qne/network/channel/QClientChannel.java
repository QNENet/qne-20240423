package com.qnenet.qne.network.channel;



// noise-java is a Java implementation of the Noise Protocol Framework

// https://noiseprotocol.org/

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.ShortBufferException;

import com.qnenet.qne.network.node.QNode;
import com.qnenet.qne.objects.classes.QNEPacket;
import com.qnenet.qne.objects.classes.QPayload;
import com.southernstorm.noise.protocol.DHState;
import com.southernstorm.noise.protocol.HandshakeState;

public class QClientChannel {

    // private static final long CHANNEL_MSG_TIMEOUT_1 = 2000;
    // private static final long CHANNEL_MSG_TIMEOUT_2 = 4000;
    // private static final long CHANNEL_MSG_TIMEOUT_3 = 8000;
    // private static final int HANDSHAKE_BUFFER_MAX_PAYLOAD_SIZE = 65507;
    private static final int HANDSHAKE_BUFFER_SIZE = 65535;

    // private InetSocketAddress destination;
    private QNEPacket firstQNEPacket;
    private InetSocketAddress destInetSocketAddress;
    private short srcEPIdx;
    private short destEPIdx;
    private int channelId;

    private QPayload firstPayload;
    private int firstPacketId;

    private HandshakeState handshake;
    private byte[] workBuffer = new byte[HANDSHAKE_BUFFER_SIZE];
    private QNode node;

    public QClientChannel(QNode node, byte[] keyPairClone, QNEPacket firstQNEPacket) throws NoSuchAlgorithmException, ShortBufferException {
        this.node = node;
        this.firstQNEPacket = firstQNEPacket;
        this.destInetSocketAddress = firstQNEPacket.sendDestInetSocketAddress;
        this.srcEPIdx = firstQNEPacket.srcEPIdx;
        this.destEPIdx = firstQNEPacket.destEPIdx;
        this.channelId = firstQNEPacket.channelId;

        this.firstPayload = firstQNEPacket.payload;
        this.firstPacketId = firstQNEPacket.packetId;

        startHandshake(keyPairClone);
    }

    private void startHandshake(byte[] keyPairClone) throws NoSuchAlgorithmException, ShortBufferException {
        handshake = new HandshakeState("Noise_XX_25519_ChaChaPoly_BLAKE2b", HandshakeState.INITIATOR);
        DHState localKeyPair = handshake.getLocalKeyPair();
        localKeyPair.setPrivateKey(keyPairClone, 0);
        localKeyPair.setPublicKey(keyPairClone, 32);
        handshake.start();
        writeMessage(null);
    }

    private void writeMessage(byte[] payload) throws ShortBufferException {
        int payloadLength;
        if (payload == null) {
            payloadLength = 0;

        } else {
            payloadLength = payload.length;
        }
        int size = handshake.writeMessage(workBuffer, 0, payload, 0, payloadLength);
        byte[] handshakeBytes = Arrays.copyOf(workBuffer, size);
        QPayload handshakePayload = new QPayload(QPayload.HANDSHAKE, handshakeBytes);
        QNEPacket qnePacket = new QNEPacket(srcEPIdx, destInetSocketAddress, destEPIdx, handshakePayload, channelId);
        node.sendPacket(qnePacket);
        // readMesssage();
    }

    public void readMessage(QNEPacket qnePacket) throws ShortBufferException, BadPaddingException {
        if (qnePacket.payload.type == QPayload.HANDSHAKE) {
                readHandshakeMessage(qnePacket);
             return;
        }

        switch (qnePacket.payload.type) {
            case QPayload.PLAIN_STRING:
            String result = new String(qnePacket.payload.bytes);
                System.out.println(result);
                break;
        
            default:
                break;
        }

    }

    private void readHandshakeMessage(QNEPacket qnePacket) throws ShortBufferException, BadPaddingException {
        int payloadSize = 0;
        byte[] msgBytes = qnePacket.payload.bytes;
        payloadSize = handshake.readMessage(msgBytes, 0, msgBytes.length, workBuffer, 0);
        if (payloadSize > 0) {
            byte[] payloadBytes = new byte[payloadSize];
            ByteBuffer payloadBB = ByteBuffer.wrap(workBuffer);
            payloadBB.get(payloadBytes);


            // QChannelInfo reeivedChannelInfo = (QChannelInfo) qobjs.objFromBytes(payloadBytes);
            // channelInfo.initiatorEPAddr = reeivedChannelInfo.initiatorEPAddr;
        }
    }

    // private byte[] getHandshakeBytes(byte[] workBuffer, int size) {
    //     ByteBuffer bb = ByteBuffer.wrap(workBuffer, 0, size);
    //     return bb.array();
    // }


}
