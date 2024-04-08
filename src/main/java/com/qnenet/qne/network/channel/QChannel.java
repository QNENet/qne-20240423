package com.qnenet.qne.network.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public abstract class QChannel {

    public abstract void handlePacketBB(ByteBuffer bb, InetSocketAddress srcInetAddr);
    
}
