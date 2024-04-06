package com.qnenet.qne.objects.classes;

//import com.qnenet.qne.channel.QChannel;
//import com.qnenet.qne.objectsQNE.intf.QNetMsg;

public class QChannelInfo {

    // send as payload on first handshake exchange

    public int channelId;
    public int role;
    public QEPAddr initiatorEPAddr;
    public QEPAddr responderEPAddr;

}
