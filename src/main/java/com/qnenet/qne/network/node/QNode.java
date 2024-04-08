package com.qnenet.qne.network.node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;
import com.qnenet.qne.network.channel.QChannel;
import com.qnenet.qne.objects.classes.QEPAddr;
import com.qnenet.qne.objects.classes.QPacketHeader;
import com.qnenet.qne.objects.classes.QPayload;

/**
 * Represents a node in the QNE network.
 * A QNode listens for incoming packets on a specified port, adds them to a
 * packet queue,
 * and handles each packet in the queue.
 */
public class QNode {

    private DatagramSocket socket;
    private ConcurrentLinkedQueue<DatagramPacket> packetQueue = new ConcurrentLinkedQueue<>();
    private Map<Integer, QChannel> channelsByIdMap = new ConcurrentHashMap<>();

    /**
     * Constructs a QNode object with the specified port number.
     * 
     * @param port the port number to listen for incoming packets
     */
    public QNode(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                packetQueue.add(packet);

                // Loop to take packet off queue and handle it
                while (!packetQueue.isEmpty()) {
                    DatagramPacket queuedPacket = packetQueue.poll();
                    handlePacket(queuedPacket);
                }
            }
        });
    }

    /**
     * Handles an incoming packet by extracting the source address, channel ID,
     * and channel associated with the packet, and then invoking the appropriate
     * method on the channel to handle the packet.
     * 
     * @param packet the incoming packet to handle
     */
    private void handlePacket(DatagramPacket packet) {
        InetSocketAddress srcInetAddr = (InetSocketAddress) packet.getSocketAddress();
        ByteBuffer bb = ByteBuffer.wrap(packet.getData());
        int channelId = bb.getInt(0);

        QChannel channel = channelsByIdMap.remove(channelId); // get channel by id
        if (channel != null) {
            channel.handlePacketBB(bb, srcInetAddr);
        }
    }

    /**
     * Creates a new DatagramPacket with the specified data and destination address.
     * 
     * @param data        the data to include in the packet
     * @param destination the destination address of the packet
     * @return the created DatagramPacket
     */
    public DatagramPacket createPacket(QPayload payload, QEPAddr destEP) {

//         destEP.epId;
// destEP.

        // QPacketHeader header = new QPacketHeader(payload.bytes);
        // switch (payload.type) {
        //     case QPayload.PLAIN_BYTES:

        //         break;
        //     case QPayload.PLAIN_STRING:

        //         break;

        //     default:
        //         break;
        // }

        // return new DatagramPacket(data, data.length, destEP.getSocketAddr());
        return null;
    }

    /**
     * Sends a DatagramPacket to its destination.
     * 
     * @param packet the packet to send
     */
    public void sendPacket(DatagramPacket packet) {
        try (DatagramSocket socket = new DatagramSocket()) {
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
