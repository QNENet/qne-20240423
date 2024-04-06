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

public class QStringUtils {




    public static String int2(int val) {
        return String.format("%02d", val);
    }

    public static String int3(int val) {
        return String.format("%03d", val);
    }

    public static String int4(int val) {
        return String.format("%04d", val);
    }

    public static String int6(int val) {
        return String.format("%06d", val);
    }

    public static String int8(int val) {
        return String.format("%08d", val);
    }

    public static String long19(long val) {
        return longZeros(val, 19);
    }

    public static String longZeros(long value, int paddingLength) {
        return String.format("%0" + paddingLength + "d", value);
    }




    public static String prettyArrayBytes(byte[] bytes) {
        return encodeHexString(bytes);
    }

    public static String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        int count = 0;
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
            count += 1;
            if (count % 4 == 0) hexStringBuffer.append(" ");
        }
        return hexStringBuffer.toString();
    }

    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }


//    public static String qneIdToDisplayString(long qneId) {
//        String str = QBase36.qneIdToQNEIdString(qneId);
//        return str.substring(0, 3) + " " + str.substring(3, str.length());
//    }
//
//
//    public static String expandDigits(String str) {
//        str = str.toLowerCase();
//        StringBuilder sb = new StringBuilder();
//
//        for (int index = 0; index < str.length(); index++) {
//            char aChar = str.charAt(index);
//
//            switch (aChar) {
//                case '0':
//                    sb.append("ZERO-");
//                    break;
//                case '1':
//                    sb.append("ONE-");
//                    break;
//                case '2':
//                    sb.append("TWO-");
//                    break;
//                case '3':
//                    sb.append("THREE-");
//                    break;
//                case '4':
//                    sb.append("FOUR-");
//                    break;
//                case '5':
//                    sb.append("FIVE-");
//                    break;
//                case '6':
//                    sb.append("SIX-");
//                    break;
//                case '7':
//                    sb.append("SEVEN-");
//                    break;
//                case '8':
//                    sb.append("EIGHT-");
//                    break;
//                case '9':
//                    sb.append("NINE-");
//                    break;
//
//                case 'a':
//                    sb.append("AY-");
//                    break;
//                case 'b':
//                    sb.append("BEE-");
//                    break;
//                case 'c':
//                    sb.append("CEE-");
//                    break;
//                case 'd':
//                    sb.append("DEE-");
//                    break;
//                case 'e':
//                    sb.append("EEE-");
//                    break;
//                case 'f':
//                    sb.append("EFF-");
//                    break;
//                case 'g':
//                    sb.append("GEE-");
//                    break;
//                case 'h':
//                    sb.append("AITCH-");
//                    break;
//                case 'i':
//                    sb.append("EYE-");
//                    break;
//                case 'j':
//                    sb.append("JAY-");
//                    break;
//                case 'k':
//                    sb.append("KAY-");
//                    break;
//                case 'l':
//                    sb.append("ELL-");
//                    break;
//                case 'm':
//                    sb.append("EMM-");
//                    break;
//                case 'n':
//                    sb.append("ENN-");
//                    break;
//                case 'o':
//                    sb.append("OH-");
//                    break;
//                case 'p':
//                    sb.append("PEA-");
//                    break;
//                case 'q':
//                    sb.append("QUE-");
//                    break;
//                case 'r':
//                    sb.append("ARRH-");
//                    break;
//                case 's':
//                    sb.append("ESS-");
//                    break;
//                case 't':
//                    sb.append("TEE-");
//                    break;
//                case 'u':
//                    sb.append("YOU-");
//                    break;
//                case 'v':
//                    sb.append("VEE-");
//                    break;
//                case 'w':
//                    sb.append("DUBLU-");
//                    break;
//                case 'x':
//                    sb.append("EX-");
//                    break;
//                case 'y':
//                    sb.append("WHY-");
//                    break;
//                case 'z':
//                    sb.append("ZED-");
//                    break;
//            }
//        }
//        return QStringUtils.deleteLastCharacter(sb.toString());
//
//    }
//
//    private static String deleteLastCharacter(String str) {
//        return str.substring(0, str.length() - 1);
//    }
//
//    public static String toASCII(String text) {
//        byte[] bytes;
//        bytes = text.getBytes(StandardCharsets.US_ASCII);
//        return Arrays.toString(bytes);
//    }
//
//
//
//    // https://www.tutorialspoint.com/javaexamples/regular_email.htm
//    public static boolean validateEmailAddress(String emailAddress) {
//        String EMAIL_REGEX = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
//        return emailAddress.matches(EMAIL_REGEX);
//    }
//
//
//    public static String subtractIntFromNumericString(String numericString, int amount) {
//        String[] parts = numericString.split(".");
//        return String.valueOf(Integer.valueOf(parts[0]) - amount);
//    }
//
//    public static String addIntToNumericString(String numericString, int amount) {
//        String[] parts = numericString.split(".");
//        return String.valueOf(Integer.valueOf(parts[0]) + amount);
//    }
//
////    public static String html2text(String html) {
////        return Jsoup.parse(html).text();
////    }
//
//    public static String removeLastChar(String menuRoute) {
//        return menuRoute.substring(0, menuRoute.length() - 1);
//    }
//
//    public static String bytesToString(byte[] bytes) {
//        return new String(bytes, Charset.forName("UTF-8"));
//    }
//
//    public static byte[] stringToBytes(String str) {
//        return str.getBytes(Charset.forName("UTF-8"));
//    }
//
////    public static boolean validateDotName(String dotName, int maxTotalSize, int maxSegmentSize, int maxNameSize) {
////        String name = dotName.trim().toLowerCase();
////        if (name.length() > maxTotalSize) return false;
////        String[] split = name.split("-");
////        if (split.length != 2) return false;
////        if (QBase36.qneNameToInt(split[0]) > maxSegmentSize) return false;
////        if (split[1].length() > maxNameSize) return false;
////
////        char[] chars = name.toCharArray();
////        for (int i = 0; i < chars.length; i++) {
////            char aChar = chars[i];
////            if (Character.isLetterOrDigit(aChar) || aChar == 46) continue;
////            return false;
////        }
////        return true;
////    }
//
//
//
//
//
//    public static String fixCSVNull(String str) {
//        if(str == null) return "";
//        if(str.equalsIgnoreCase("<null>")) return "";
//        return str;
//    }
//
//    public static String memberIdAsString(long nodeId, int installIdx) {
//        String nodeIdStr = QBase36.longToBase36Str8with1Dot(nodeId);
//        return installIdx + "@" + nodeIdStr;
//    }
}