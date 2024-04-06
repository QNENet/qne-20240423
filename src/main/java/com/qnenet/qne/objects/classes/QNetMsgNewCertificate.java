package com.qnenet.qne.objects.classes;

import com.qnenet.qne.network.packet.QClientChannel;
import com.qnenet.qne.network.packet.QPacket;
import com.qnenet.qne.network.packet.QServerChannel;
import com.qnenet.qne.system.constants.QSysConstants;

import java.util.ArrayList;

public class QNetMsgNewCertificate extends QNetMsg {


    public long nodeId;
    public QEPAddrPair newNodeNetAddressPair;
    public byte[][] endCertificateBytes;
    public byte[] sslPublicKeyEncoded;

    @Override
    public void handleRequest(QServerChannel serverChannel, QPacket qPacket) {
        if (this instanceof QNetMsgNewCertificate) {

//            strings.add("Timestamp -> " + Long.valueOf(System.currentTimeMillis()));
            setStatus(QSysConstants.NET_MSG_RESPONSE_STATUS_OK);
        } else {
            setStatus(QSysConstants.NET_MSG_RESPONSE_STATUS_WRONG_NET_MSG_TYPE);
        }

    }

    @Override
    public void handleResponse(QClientChannel clientChannel, QPacket qPacket) {

    }
}
