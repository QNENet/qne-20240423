package com.qnenet.qne.network.node.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Service;

import com.qnenet.qne.network.node.api.QNode;

// create class to send and receive datagram packets.
// the receive method has a virtual thread loop which places packets in a queue.
// the receive method takes packets off the queue and runs a method handlePacket
// also in a virtual thread loop
// the send method sends packets
// the class has a method to create a new packet

public class QNodeImpl implements QNode {

    private int port;
    private ConcurrentLinkedQueue<DatagramPacket> packetQueue = new ConcurrentLinkedQueue<>();

    public QNodeImpl(int port) {
        this.port = port;
    }

    // Method to start a virtual thread that receives packets and puts them in a
    // queue
    public void startReceivePacketLoop() {
        Thread receiveThread = Thread.startVirtualThread(() -> {
            try {
                try (DatagramSocket socket = new DatagramSocket(port)) {
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    while (true) {
                        socket.receive(packet);
                        packetQueue.add(packet);

                        // Loop to take packet off queue and handle it
                        while (!packetQueue.isEmpty()) {
                            DatagramPacket queuedPacket = packetQueue.poll();
                            handlePacket(queuedPacket);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // receiveThread.start();
    }

    // Placeholder method to handle packets
    private void handlePacket(DatagramPacket packet) {
        // Add your packet handling logic here
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // Method to send a packet
    public void sendPacket(DatagramPacket packet) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to create a new packet
    public DatagramPacket createPacket(byte[] data, InetSocketAddress destination) {
        return new DatagramPacket(data, data.length, destination);
    }

    public static void main(String[] args) {
        // QNodeImpl node1 = new QNodeImpl(8080);
        // node1.startReceivePacketLoop();
        // QNodeImpl node2 = new QNodeImpl(8081);
        // node2.startReceivePacketLoop();

        // node1.sendPacket(node1.createPacket("Hello".getBytes(), new
        // InetSocketAddress("localhost", 8081)));

    }

}