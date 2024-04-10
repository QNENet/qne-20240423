package com.qnenet.qne.objects.classes;

import com.qnenet.qne.network.channel.QClientChannel;
import com.qnenet.qne.network.channel.QServerChannel;
import com.qnenet.qne.system.constants.QSysConstants;

public class QNetMsgNewCertificate extends QNetMsg {


    public long nodeId;
    public QEPAddrPair newNodeNetAddressPair;
    public byte[][] endCertificateBytes;
    public byte[] sslPublicKeyEncoded;

    @Override
    public void handleRequest(QServerChannel serverChannel, QNEPacket qPacket) {
        if (this instanceof QNetMsgNewCertificate) {

//            strings.add("Timestamp -> " + Long.valueOf(System.currentTimeMillis()));
            setStatus(QSysConstants.NET_MSG_RESPONSE_STATUS_OK);
        } else {
            setStatus(QSysConstants.NET_MSG_RESPONSE_STATUS_WRONG_NET_MSG_TYPE);
        }

    }

    @Override
    public void handleResponse(QClientChannel clientChannel, QNEPacket qPacket) {

    }
}
