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

package com.qnenet.qne.system.constants;

public interface QNetMsgConstants {
    public static final int NET_MSG_RESPONSE_STATUS_OK = 0;
    public static final int NET_MSG_RESPONSE_STATUS_WRONG_NODE_TYPE = 1;
    public static final int NET_MSG_RESPONSE_STATUS_WRONG_NET_MSG_TYPE = 2;

    public static final int HANDSHAKE_REQUEST = 1;
    public static final int HANDSHAKE_RESPONSE = 2;

    public static final int ENCRYPTED_RT_REQUEST = 3;

    public static final int ENCRYPTED_RT_RESPONSE = 4;

    public static final int ENCRYPTED_RT_REQUEST_PLUS_PLAIN_BYTES = 5;

    public static final int ENCRYPTED_RT_RESPONSE_PLUS_PLAIN_BYTES = 6;


}
