package com.qnenet.qne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.qnenet.qne.network.node.QNode;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class QNodeTest {

    private QNode node1;
    private QNode node2;

    @BeforeEach
    public void setUp() {
        node1 = new QNode(8900);
        node2 = new QNode(8901);
    }

    @Test
    public void testCreatePacket() {
        // Create test data and destination address
        // byte[] data = "Hello".getBytes();
        // InetSocketAddress destination = new InetSocketAddress("localhost", 8901);

        // // Invoke the createPacket method
        // DatagramPacket packet = node1.createPacket(data, destination);

        // // Verify that the created packet has the correct data and destination address
        // assertEquals(data.length, packet.getLength());
        // assertEquals(destination, packet.getSocketAddress());
    }


    // @Test
    // public void testHandlePacket() {
    //     // Create a mock packet
    //     DatagramPacket packet = mock(DatagramPacket.class);
    //     InetSocketAddress srcInetAddr = new InetSocketAddress("localhost", 1234);
    //     when(packet.getSocketAddress()).thenReturn(srcInetAddr);

    //     // Create a mock channel
    //     QChannel channel = mock(QChannel.class);
    //     when(qNode.getChannelsByIdMap()).thenReturn(new ConcurrentHashMap<>());
    //     when(qNode.getChannelsByIdMap().remove(anyInt())).thenReturn(channel);

    //     // Invoke the handlePacket method
    //     qNode.handlePacket(packet);

    //     // Verify that the channel's handlePacketBB method is invoked with the correct arguments
    //     verify(channel).handlePacketBB(any(ByteBuffer.class), eq(srcInetAddr));
    // }


    // Add more tests for other methods in the QNode class

}