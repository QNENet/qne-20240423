package com.qnenet.qne.objects.classes;



public interface QPacketOwner {

    void processPacket(QNEPacket uPacket);

    void processFailedQPacket(QNEPacket uPacket);
}
