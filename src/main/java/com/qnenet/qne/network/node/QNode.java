package com.qnenet.qne.network.node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import javax.crypto.BadPaddingException;
import javax.crypto.ShortBufferException;

import java.util.Map;
import com.qnenet.qne.network.channel.QClientChannel;
import com.qnenet.qne.network.channel.QServerChannel;
import com.qnenet.qne.objects.classes.QEPAddr;
import com.qnenet.qne.objects.classes.QNEPacket;
import com.qnenet.qne.objects.classes.QPayload;

/**
 * Represents a node in the QNE network.
 * A QNode listens for incoming packets on a specified port, adds them to a
 * packet queue,
 * and handles each packet in the queue.
 */
public class QNode {

    private String ipAddress;
    public int port;
    private DatagramSocket socket;
    private ConcurrentLinkedQueue<DatagramPacket> packetQueue = new ConcurrentLinkedQueue<>();
    private Map<Integer, QClientChannel> clientChannelsByIdMap = new ConcurrentHashMap<>();
    private Map<Integer, QServerChannel> serverChannelsByIdMap = new ConcurrentHashMap<>();
    private byte[] nodeKeyPair;
    private ExecutorService executor;
    private QServerChannel serverChannel;

    /**
     * Constructs a QNode object with the specified port number.
     * 
     * @param port the port number to listen for incoming packets
     * @param executor 
     */
    public QNode(String ipAddress, int port, byte[] nodeKeyPair, ExecutorService executor) {
        try {
            this.executor = executor;
            this.ipAddress = ipAddress;
            this.port = port;
            this.nodeKeyPair = nodeKeyPair;
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public InetSocketAddress getInetSocketAddress() {
        return new InetSocketAddress(ipAddress, port);
    }

    /**
     * Starts a virtual thread that receives packets and puts them in a queue.
     * This method continuously listens for incoming packets on the specified port,
     * adds them to the packet queue, and handles each packet in the queue.
     */
    public void startReceivePacketLoop() {
        Thread.startVirtualThread(() -> {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                packetQueue.add(packet);

                // Loop to take packet off queue and handle it
                while (!packetQueue.isEmpty()) {
                    DatagramPacket queuedPacket = packetQueue.poll();
                    try {
                        handlePacket(queuedPacket);
                    } catch (ShortBufferException | BadPaddingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void handlePacket(DatagramPacket packet) throws ShortBufferException, BadPaddingException {
        QNEPacket qnePacket = new QNEPacket(packet);
        if (qnePacket.channelId > 0) { // server
            serverChannel = serverChannelsByIdMap.get(qnePacket.channelId);
            if (serverChannel == null) {
                executor.submit(() -> {
                    try {
                        serverChannel = new QServerChannel(this, nodeKeyPair.clone()); // Instantiate QServerChannel
                        serverChannelsByIdMap.put(qnePacket.channelId, serverChannel); // Cast packetData.channelId to Integer
                    } catch (NoSuchAlgorithmException | ShortBufferException e) {
                        e.printStackTrace();
                    }
                });
            }
            serverChannel.readMessage(qnePacket);
        } else { // client
            QClientChannel clientChannel = clientChannelsByIdMap.get(-qnePacket.channelId);
            if (clientChannel == null)
                throw new RuntimeException("Client channel not found");
            clientChannel.readMessage(qnePacket);
        }
    }

    public void sendPacket(QNEPacket qnePacket) {
        var bytes = qnePacket.pack();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, qnePacket.destInetSocketAddress);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point of the application.
     * Uncomment the code in this method to create QNode instances,
     * start the receive packet loop, and send packets between nodes.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // QNode node1 = new QNode(8080);
        // node1.startReceivePacketLoop();
        // QNode node2 = new QNode(8081);
        // node2.startReceivePacketLoop();

        // node1.sendPacket(node1.createPacket("Hello".getBytes(), new
    }

}
