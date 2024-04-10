package com.qnenet.qne;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.qnenet.qne.network.node.QNode;
import com.qnenet.qne.objects.classes.QNEPacket;
import com.qnenet.qne.objects.classes.QPayload;
import com.qnenet.qne.system.utils.QSecurityUtils;

public class QNodeTest {

    private QNode node1;
    private QNode node2;
    private ExecutorService executor;

    @BeforeEach
    public void setUp() {
        executor = Executors.newVirtualThreadPerTaskExecutor();
        byte[] nodeKeyPair1 = QSecurityUtils.createNoiseKeypair();
        node1 = new QNode("localhost",8900, nodeKeyPair1, executor);
        byte[] nodeKeyPair2 = QSecurityUtils.createNoiseKeypair();
        node2 = new QNode("localhost", 8901, nodeKeyPair2, executor);
    }

    @Test
    public void testSendPacket() {
        QPayload payload = new QPayload((byte) QPayload.PLAIN_STRING, "Hello".getBytes());
        QNEPacket packetData = new QNEPacket((short) 0, "localhost", node2.port, (short) 0, payload, 123, 456);
        node1.sendPacket(packetData);
        // // Verify that the created packet has the correct data and destination
        // address
        // assertEquals(data.length, packet.getLength());
        // assertEquals(destination, packet.getSocketAddress());
    }

    // @Test
    // public void testHandlePacket() {
    // // Create a mock packet
    // DatagramPacket packet = mock(DatagramPacket.class);
    // InetSocketAddress srcInetAddr = new InetSocketAddress("localhost", 1234);
    // when(packet.getSocketAddress()).thenReturn(srcInetAddr);

    // // Create a mock channel
    // QChannel channel = mock(QChannel.class);
    // when(qNode.getChannelsByIdMap()).thenReturn(new ConcurrentHashMap<>());
    // when(qNode.getChannelsByIdMap().remove(anyInt())).thenReturn(channel);

    // // Invoke the handlePacket method
    // qNode.handlePacket(packet);

    // // Verify that the channel's handlePacketBB method is invoked with the
    // correct arguments
    // verify(channel).handlePacketBB(any(ByteBuffer.class), eq(srcInetAddr));
    // }

    // Add more tests for other methods in the QNode class

}