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

import java.util.UUID;

public class QBase36 {

    public static String qneIdToQNEIdString(long value) {
        return Long.toString(value, 36);
    }

    public static String longToBase36Str8with1Dot(long value) {
        String str = Long.toString(value, 36);
        if (str.length() != 8) return null;
        String str1 = str.substring(0, 4);
        String str2 = str.substring(4, 8);
        return str1 + "." + str2;
    }

    public static String uuidToBase36Str(UUID uuid) {
        long l1 = uuid.getMostSignificantBits();
        long l2 = uuid.getLeastSignificantBits();
        return Long.toString(l1, 36) + "$" +Long.toString(l2, 36);
    }

    public static UUID uuidFromBase36Str(String uuidBase36Str) {
        String[] split = uuidBase36Str.split("$");
//        int l = uuidBase36Str.length();
//        int hl = l / 2;
//        String s1 = uuidBase36Str.substring(0, hl);
//        String s2 = uuidBase36Str.substring(hl, l);
        long msb = Long.valueOf(split[0], 36);
        long lsb = Long.valueOf(split[1], 36);
        return new UUID(msb, lsb);
    }
//
//    public static String qneIdToFormattedQNEIdString(long value) {
//        String str = Long.toString(value, 36);
//        return str.substring(0, 3) + " " + str.substring(3, str.length());
//    }
//
//
//    public static long qneIdStringToQNEId(String qneIdString) {
//        String replace = qneIdString.replace(" ", "");
//        return Long.parseLong(replace, 36);
//    }
//
//    public static String intToQNEName(int value) {
//        return Integer.toString(value, 36);
//    }
//
//    public static String intToBase36(int value) {
//        return Integer.toString(value, 36);
//    }
//
//    public static String longToBase36(long value) {
//        return Long.toString(value, 36);
//    }
//
//    public static String longToBase36Str8with3Dots(long value) {
//        String str = Long.toString(value, 36);
//        if (str.length() != 8) return null;
//        String str1 = str.substring(0, 2);
//        String str2 = str.substring(2, 4);
//        String str3 = str.substring(4, 6);
//        String str4 = str.substring(6, 8);
//        return str1 + "." + str2 + "." + str3 + "." + str4;
//    }
//
//
//
//    public static Long Base36StringToLong(String base36Str) {
//        base36Str = base36Str.replace(".", " ");
//        base36Str = base36Str.replace(" ", "");
//        return Long.valueOf(base36Str, 36);
//    }
//
//    public static int qneNameToInt(String qneName) {
//        return Integer.parseInt(qneName, 36);
//    }

//    public static Long idNameToId(String base36Id) {
//        base36Id = base36Id.replace(" ", "").replace("-", "").replace(".", "");
//        return qneNameToLong(base36Id);
//    }

//    public static String memberIdFormatted(long memberId) {
//        if (memberId < QSystemConstants.MIN_MEMBER_ID || memberId > QSystemConstants.MAX_MEMBER_ID) {
//            return "";
//        }
//        String str = longToQNEName(memberId);
//        return str.substring(0, 3) + " " + str.substring(3, 7);
//    }

}
