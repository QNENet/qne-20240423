package com.qnenet.qne.objects.classes;


import com.qnenet.qne.network.packet.QPacket;

public interface QPacketOwner {

    void processPacket(QPacket uPacket);

    void processFailedQPacket(QPacket uPacket);
}
