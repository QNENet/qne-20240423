package com.qnenet.qne.network.channel;

import java.net.DatagramPacket;

// noise-java is a Java implementation of the Noise Protocol Framework

// https://noiseprotocol.org/

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

import com.southernstorm.noise.crypto.Curve25519;
import com.southernstorm.noise.protocol.HandshakeState;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QEncryptedClientChannel extends QChannel {

    private static final long CHANNEL_MSG_TIMEOUT_1 = 2000;
    private static final long CHANNEL_MSG_TIMEOUT_2 = 4000;
    private static final long CHANNEL_MSG_TIMEOUT_3 = 8000;
    private static final int HANDSHAKE_BUFFER_SIZE = 65507;

    private byte[] workBuffer = new byte[HANDSHAKE_BUFFER_SIZE];

    private HandshakeState handshake;
    private byte[] firstPayload;
    private InetSocketAddress destination;

    public QEncryptedClientChannel(InetSocketAddress destination, byte[] firstPayload, byte[] privateKeyClone,
            byte[] publicKeyClone) {
        this.destination = destination;
        this.firstPayload = firstPayload;

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        executor.execute(() -> {
            // Create a Noise protocol handshake state
            try {
                handshake = new HandshakeState("Noise_XX_25519_ChaChaPoly_BLAKE2b", HandshakeState.INITIATOR);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace(); // Should not happen
            }

            handshake.getLocalKeyPair().setPrivateKey(privateKeyClone, 0);
            handshake.getLocalKeyPair().setPublicKey(publicKeyClone, 0);

            handshake.start();
            handleHandshakeAction();
        });
    }

    private byte[] workBufferBytes(byte[] workBuffer, int size) {
        ByteBuffer bb = ByteBuffer.allocate(size);
        bb.put(workBuffer, 0, size);
        return bb.array();
    }

    private void handleHandshakeAction() {
        // int action = handshake.getAction();

        // if (action == HandshakeState.WRITE_MESSAGE) {
        // // Write the first handshake message
        // int size = handshake.writeMessage(workBuffer, 0, null, 0, 0);
        // // Send the message to the destination
        // byte[] sendBytes = workBufferBytes(workBuffer, size);
        // send(message);
        // }

        // // Handle the response
        // if (action == HandshakeState.READ_MESSAGE) {
        // // Read the response message
        // byte[] message = new byte[1024];
        // // Receive the message from the destination
        // receive(message);
        // // Process the message
        // handshake.readMessage(message, 0, message.length);
        // }

        // // Check if the handshake is complete
        // if (handshake.getAction() == HandshakeState.WRITE_MESSAGE) {
        // // Write the final handshake message
        // byte[] message = handshake.writeMessage(null, 0, 0);
        // // Send the message to the destination
        // send(message);
        // }

        // // Check if the handshake is complete
        // if (handshake.getAction() == HandshakeState.FINISHED) {
        // // Handshake is complete
        // // Create a new QClientChannelImpl object
        // // with the handshake state and destination
        // // for further communication
        // }

        // }

        // private void send(byte[] message) {
        // // Create a new packet with the message
        // DatagramPacket packet = createPacket(message, destination);
        // // Send the packet
        // sendPacket(packet);
        // }
    }

    @Override
    public void handlePacketBB(ByteBuffer bb, InetSocketAddress srcInetAddr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handlePacketBB'");
    }
}
