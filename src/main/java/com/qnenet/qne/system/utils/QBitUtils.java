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

package com.qnenet.qne.system.utils;

public class QBitUtils {

    public final static byte setByteBitValue(byte targetByte, int bitPosition, boolean bitValue) {
        if (bitValue) return (byte) (targetByte | (1 << bitPosition));
        return (byte) (targetByte & ~(1 << bitPosition));
    }

    public final static Boolean getByteBitValue(byte targetByte, int bitPosition) {
        return (targetByte & (1 << bitPosition)) != 0;
    }

}

