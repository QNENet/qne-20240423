package com.qnenet.qne.objects.classes;

/*
 * Copyright (C) 2022 Paul F Fraser <paulf@qnenet.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//import com.qnenet.qne.channel.QChannel;
//import com.qnenet.qne.packet.QClientChannel;
//import com.qnenet.qne.packet.QPacket;
//import com.qnenet.qne.packet.QServerChannel;

import com.qnenet.qne.network.packet.QClientChannel;
import com.qnenet.qne.network.packet.QPacket;
import com.qnenet.qne.network.packet.QServerChannel;

import java.util.Map;

public abstract class QNetMsg {

//    public int channelId;
//    public int roundTripId;
//    public int initiatorEndpointIdx;
//    public int responderEndpointIdx;
//    public int role;
//    public byte[] netMsgBytes;

    public int status;
    public Map map;
//    public boolean keepAlive;
//    public byte retryCount;

//    public transient QEPId senderEPId;
//
//    public QEndPointInfo senderEndPointInfo;


    public abstract void handleRequest(QServerChannel serverChannel, QPacket qPacket);

    public abstract void handleResponse(QClientChannel clientChannel, QPacket qPacket);

    public Map<Object, Object> getMap() {
        return map;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

//    public transient QPacketOwner origin;
//    public transient QChannel channel;

//    public QEndPointInfo getSenderEndPointAddress() {
//        return senderEndPointInfo;
//    }
}