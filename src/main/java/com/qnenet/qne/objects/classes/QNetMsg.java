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


import java.util.Map;

import com.qnenet.qne.network.channel.QClientChannel;
import com.qnenet.qne.network.channel.QServerChannel;

public abstract class QNetMsg {

    public int status;
    @SuppressWarnings("rawtypes")
    public Map map;


    public abstract void handleRequest(QServerChannel serverChannel, QNEPacket qPacket);

    public abstract void handleResponse(QClientChannel clientChannel, QNEPacket qPacket);

    @SuppressWarnings("unchecked")
    public Map<Object, Object> getMap() {
        return map;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}