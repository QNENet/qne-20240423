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

package com.qnenet.qne.system.utils;///*

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.Random;


public class QNetworkUtils {

    private static final int High = 65500;
    private static final int Low = 49335;
    static Random random = new Random();

///////////////////////////////////////////////////////////////////////////////////////////////////
//////////// Ports ////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public static Integer getFreePort() {

//		return 44912;
        int count = 0;
        while (count < 1000) {
            int possiblePort = random.nextInt(High - Low) + Low;
            if (isPortAvailable(possiblePort)) {
                return possiblePort;
            }
            count++;
        }
        return null;
    }

    public static boolean isPortAvailable(int port) {
        try (ServerSocket s = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
//////////// External Address /////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public static String getExternalAddress() {
        return getExternalAddressFromAmazon();
    }


        public static String getExternalAddressFromAmazon() {
        BufferedReader in = null;
        try {
            URI uri = new URI("http://checkip.amazonaws.com");
            URL url = uri.toURL();
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ip = in.readLine();
            return ip;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
//////////// Localhost Lan Address ////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


    public static String getLanIPAddress()  {
        try {
            return getLocalHostLANAddress().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


        /**
         * //     * from https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
         * //     * Returns an <code>InetAddress</code> object encapsulating what is most likely the machine's LAN IP address.
         * <p/>
         * This method is intended for use as a replacement of JDK method <code>InetAddress.getLocalHost</code>, because
         * that method is ambiguous on Linux systems. Linux systems enumerate the loopback network interface the same
         * way as regular LAN network interfaces, but the JDK <code>InetAddress.getLocalHost</code> method does not
         * specify the algorithm used to select the address returned under such circumstances, and will often return the
         * loopback address, which is not valid for network communication. Details
         * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037">here</a>.
         * <p/>
         * This method will scan all IP addresses on all network interfaces on the host machine to determine the IP address
         * most likely to be the machine's LAN address. If the machine has multiple IP addresses, this method will prefer
         * a site-local IP address (e.g. 192.168.x.x or 10.10.x.x, usually IPv4) if the machine has one (and will return the
         * first site-local address if the machine has more than one), but if the machine does not hold a site-local
         * address, this method will return simply the first non-loopback address found (IPv4 or IPv6).
         * <p/>
         * If this method cannot find a non-loopback address using this selection algorithm, it will fall back to
         * calling and returning the result of JDK method <code>InetAddress.getLocalHost</code>.
         * <p/>
         *
         * @throws UnknownHostException If the LAN address of the machine cannot be found.
         */
    public static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // Iterate all NICs (network interface cards)...
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // Iterate all IP addresses assigned to each card...
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {

                        if (inetAddr.isSiteLocalAddress()) {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr;
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return candidateAddress;
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
//////////// IP Converters ////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    // private static boolean isZeroArray(byte[] bytes) {
    //     for (int i = 0; i < bytes.length; i++) {
    //         if (bytes[i] != 0) return false;
    //     }
    //     return true;
    // }

    public static byte[] ipAddressToBytes(String ipAddr) {
        if (ipAddr == null) return null;
        InetAddress inetAddr = null;
        try {
            inetAddr = InetAddress.getByName(ipAddr);
        } catch (UnknownHostException e) {
            return null;
        }
        return inetAddr.getAddress();
    }

    public static String ipAddressFromBytes(byte[] ipAddrBytes) {
        if (ipAddrBytes == null) return null;
        InetAddress inetAddr = null;
        try {
            inetAddr = InetAddress.getByAddress(ipAddrBytes);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (inetAddr != null) {
            return inetAddr.getHostAddress();
        }
        return null;
    }


//    public static Integer ipv4StringToInt(String ipv4String) {
//
//        InetAddress inetAddr = null;
//        try {
//            inetAddr = InetAddress.getByName(ipv4String);
//        } catch (UnknownHostException e) {
////            e.printStackTrace();
//            return null;
//        }
//        return ByteBuffer.wrap(inetAddr.getAddress()).getInt();
//    }
//
//    public static String ipV4IntToString(int ipv4Int) {
//        ByteBuffer buffer = ByteBuffer.allocate(4);
//        buffer.putInt(ipv4Int);
//        byte[] b = buffer.array();
//
//        InetAddress inetAddr = null;
//        try {
//            inetAddr = InetAddress.getByAddress(b);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return inetAddr.getHostAddress();
//    }
//
//
//
//
//    public static long[] IPToLong(String addr) {
//        String[] addrArray = addr.split(":");//a IPv6 adress is of form 2607:f0d0:1002:0051:0000:0000:0000:0004
//        long[] num = new long[addrArray.length];
//
//        for (int i=0; i<addrArray.length; i++) {
//            num[i] = Long.parseLong(addrArray[i], 16);
//        }
//        long long1 = num[0];
//        for (int i=1;i<4;i++) {
//            long1 = (long1<<16) + num[i];
//        }
//        long long2 = num[4];
//        for (int i=5;i<8;i++) {
//            long2 = (long2<<16) + num[i];
//        }
//
//        long[] longs = {long2, long1};
//        return longs;
//    }
//
//
//    public static String longToIP(long[] ip) {
//        String ipString = "";
//        for (long crtLong : ip) {//for every long: it should be two of them
//
//            for (int i=0; i<4; i++) {//we display in total 4 parts for every long
//                ipString = Long.toHexString(crtLong & 0xFFFF) + ":" + ipString;
//                crtLong = crtLong >> 16;
//            }
//        }
//        return ipString;
//
//    }

///////////////////////////////////////////////////////////////////////////////////////////////////
//////////// Main /////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


//    static public void main(String[] args) {
//        String ipString = "2607:f0d0:1002:0051:0000:0000:0000:0004";
//        long[] asd = IPToLong(ipString);
//
//        System.out.println(longToIP(asd));
//    }

//    public static boolean netIsAvailable() {
//        try {
//            final URL url = new URL("http://www.google.com");
//            final URLConnection conn = url.openConnection();
//            conn.connect();
//            conn.getInputStream().close();
//            return true;
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            return false;
//        }
//    }
//
//    /*
//        path must be the full file path including the file name
//    */
//    public static void downloadFromUrl(URL url, Path path) throws IOException {
//        InputStream inputStream = null;
//        FileOutputStream fileOutputStream = null;
//
//        try {
//            URLConnection urlConn = url.openConnection();
//
//            inputStream = urlConn.getInputStream();
//            fileOutputStream = new FileOutputStream(path.toFile());
//
//            byte[] buffer = new byte[4096];
//            int length;
//
//            while ((length = inputStream.read(buffer)) > 0) {
//                fileOutputStream.write(buffer, 0, length);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            assert inputStream != null;
//            inputStream.close();
//            assert fileOutputStream != null;
//            fileOutputStream.close();
//        }
//    }
//
//    public static byte[] downloadImage(String urlString) {
//        try {
//            return IOUtils.toByteArray(new URL(urlString));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    public static String downloadRandomUser() throws IOException {
//        return IOUtils.toString(new URL("https://randomuser.me/api/?format=csv"), Charset.forName("UTF-8"));
//    }
//
//    public static Map<String, String> getInfo(String infoUrl) throws IOException {
//
//        ArrayList<String> r = getUrlLines(infoUrl);
//        // String[] r1 = r.get(0).split(",");
//
//        for (String string : r) {
//            System.out.println(string);
//        }
//
//        return null;
//    }
//
//    public static String getInternalAddress() {
//        try {
//            return InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String getScooterExternalAddress() {
//        ArrayList<String> scooterLines = null;
//        try {
//            scooterLines = getUrlLines("http://scooterlabs.com/echo");
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
////			e.printStackTrace();
////			return "100.100.100.100";
//            return null;
//        }
//
//        // System.out.println(ArrayUtils.stringArrayListToString(scooterLines));
//        return getExternalAddressFromScooterLines(scooterLines);
//    }
//
//
//    private static String getExternalAddressFromScooterLines(ArrayList<String> scooterLines) {
//        for (String inputLine : scooterLines) {
//            // System.out.println(inputLine);
//            if (inputLine.indexOf(" [client_ip]") > 0) {
//                int x = inputLine.indexOf("=>");
//                return inputLine.substring(x + 2).trim();
//            }
//        }
//        return null;
//    }
//
//    private static ArrayList<String> getUrlLines(String urlStr) throws IOException {
//        ArrayList<String> lines = new ArrayList<>();
//        URL url = new URL(urlStr);
//        URLConnection connection = url.openConnection();
//        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//            String inputLine = null;
//            while ((inputLine = in.readLine()) != null) {
//                lines.add(inputLine);
//            }
//        }
//        return lines;
//
//    }
//
//
//
//
//    public static Integer getRandomPin(int high, int low) {
//        int count = 0;
//        while (count < 200) {
//            int possiblePin = random.nextInt(High - Low) + Low;
//            if (testPin(possiblePin)) {
//                return possiblePin;
//            }
//            count++;
//        }
//        return null;
//    }
//
//    private static boolean testPin(int possiblePin) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    public static String getMACAddressAsString(byte[] mac) {
//        StringBuilder buf = new StringBuilder();
//        for (int idx = 0; idx < mac.length; idx++)
//            buf.append(String.format("%02X:", mac[idx]));
//        if (buf.length() > 0)
//            buf.deleteCharAt(buf.length() - 1);
//        return buf.toString();
//    }
//
//    /**
//     * Returns MAC address of the given interface name.
//     *
//     * @param interfaceName eth0, wlan0 or NULL=use first interface
//     * @return mac address or empty string
//     */
//    public static String getMACAddress(String interfaceName) {
//        try {
//            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface intf : interfaces) {
//                if (interfaceName != null) {
//                    if (!intf.getName().equalsIgnoreCase(interfaceName))
//                        continue;
//                }
//                byte[] mac = intf.getHardwareAddress();
//                if (mac == null)
//                    return "";
//                StringBuilder buf = new StringBuilder();
//                for (int idx = 0; idx < mac.length; idx++)
//                    buf.append(String.format("%02X:", mac[idx]));
//                if (buf.length() > 0)
//                    buf.deleteCharAt(buf.length() - 1);
//                return buf.toString();
//            }
//        } catch (Exception ex) {
//        } // for now eat exceptions
//        return "";
//        /*
//         * try { // this is so Linux hack return loadFileAsString("/sys/class/net/"
//         * +interfaceName + "/address").toUpperCase().trim(); } catch (IOException ex) {
//         * return null; }
//         */
//    }
//
//
//
//    public static void interfacesInspect() {
//        Enumeration<NetworkInterface> interfaces = null;
//        try {
//            interfaces = NetworkInterface.getNetworkInterfaces();
//            while (interfaces.hasMoreElements()) {
//                NetworkInterface networkInterface = interfaces.nextElement();
//                System.out.println("Network Interface Name : [" + networkInterface.getDisplayName() + "]");
//                System.out.println("Is It connected? : [" + networkInterface.isUp() + "]");
//                for (InterfaceAddress i : networkInterface.getInterfaceAddresses()) {
//                    System.out.println("Host Name : " + i.getAddress().getCanonicalHostName());
//                    System.out.println("Host Address : " + i.getAddress().getHostAddress());
//                }
//                System.out.println("----------------------");
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /*
//     *  Returns empty string if not on lan
//     *  Returns Ip Addr(s) and if Up -> 192.168.1.2-true;
//     *
//     * */
//    public static String isOnLan() {
//        StringBuilder sb = new StringBuilder();
//        Enumeration<NetworkInterface> interfaces = null;
//        try {
//            interfaces = NetworkInterface.getNetworkInterfaces();
//        } catch (SocketException e) {
//            return "";
//        }
//        while (interfaces.hasMoreElements()) {
//            NetworkInterface networkInterface = interfaces.nextElement();
//            for (InterfaceAddress i : networkInterface.getInterfaceAddresses()) {
//                String hostName = i.getAddress().getCanonicalHostName();
//                String hostAddress = i.getAddress().getHostAddress();
//                if (isLanAddress(hostAddress)) {
//                    boolean isUp = false;
//                    try {
//                        isUp = networkInterface.isUp();
//                    } catch (SocketException e) {
//                        isUp = false;
//                    }
////                    System.out.println("Lan Address -> " + hostAddress);
////                    System.out.println("isUp -> " + isUp);
//                    sb.append(hostAddress);
//                    sb.append("-");
//                    sb.append(String.valueOf(isUp));
//                    sb.append(";");
//                }
//            }
//        }
//        return sb.toString();
//    }
//
//    public static String isOnLanIPAddress() {
//        String str = isOnLan();
//        if (str.isEmpty()) return null;
//        return str.split("-")[0];
//    }
//
//
//    // local IP Addresses
////	10.0.0.0 - 10.255.255.255
////	172.16.0.0 - 172.31.255.255
////	192.168.0.0 - 192.168.255.255
//
//    public static boolean isLanAddress(String ipAddress) {
//        String[] parts = ipAddress.split("\\.");
//        if (parts.length != 4) return false;
//        int parts0Int = Integer.valueOf(parts[0]);
//
//        if (parts0Int == 192) {
//            int parts1Int = Integer.valueOf(parts[1]);
//            if (parts1Int != 168) return false;
//            int parts2Int = Integer.valueOf(parts[2]);
//            if (parts2Int < 0 || parts2Int > 255) return false;
//            int parts3Int = Integer.valueOf(parts[3]);
//            if (parts3Int < 0 || parts3Int > 255) return false;
//            return true;
//        }
//
//        if (parts0Int == 10) {
//            int parts1Int = Integer.valueOf(parts[1]);
//            if (parts1Int < 0 || parts1Int > 255) return false;
//            int parts2Int = Integer.valueOf(parts[2]);
//            if (parts2Int < 0 || parts2Int > 255) return false;
//            int parts3Int = Integer.valueOf(parts[3]);
//            if (parts3Int < 0 || parts3Int > 255) return false;
//            return true;
//        }
//
//        if (parts0Int == 172) {
//            int parts1Int = Integer.valueOf(parts[1]);
//            if (parts1Int < 16 || parts1Int > 31) return false;
//            int parts2Int = Integer.valueOf(parts[2]);
//            if (parts2Int < 0 || parts2Int > 255) return false;
//            int parts3Int = Integer.valueOf(parts[3]);
//            if (parts3Int < 0 || parts3Int > 255) return false;
//            return true;
//        }
//
//        return false;
//
//    }
//
//
//    private static void setWindowsLanIPAddr(String addr) throws IOException {
//        // String str1="192.168.0.201";
//        String str2 = "255.255.255.0";
//        String[] command1 = {"netsh", "interface", "ipv4", "set", "address", "name=", "Local Area Connection",
//                "source=static", "addr=", addr, "mask=", str2};
//        // Process pp = java.lang.Runtime.getRuntime().exec(command1);
//        Runtime.getRuntime().exec(command1);
//    }
//
//
//
//    public static InetSocketAddress socketAddressStringToInetSocketAddress(String socketAddressString) {
//        String[] split = socketAddressString.split(":");
//        return new InetSocketAddress(split[0], Integer.valueOf(split[1]));
//    }
//
//    public static String inetSocketAddressToSocketAddressString(InetSocketAddress socketAddress) {
//        return socketAddress.getHostString() + ":" + socketAddress.getPort();
//    }
//
//
//    public static void main(String[] args) throws Exception {
//
////        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
////        while (interfaces.hasMoreElements()) {
////            NetworkInterface networkInterface = interfaces.nextElement();
////            System.out.println("Network Interface Name : [" + networkInterface.getDisplayName() + "]");
////            System.out.println("Is It connected? : [" + networkInterface.isUp() + "]");
////            for (InterfaceAddress i : networkInterface.getInterfaceAddresses()) {
////                System.out.println("Host Name : " + i.getAddress().getCanonicalHostName());
////                System.out.println("Host Address : " + i.getAddress().getHostAddress());
////            }
////            System.out.println("----------------------");
////        }
//
////        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
////        while (interfaces.hasMoreElements()) {
////            NetworkInterface networkInterface = interfaces.nextElement();
////
//////            System.out.println("Network Interface Name : [" + networkInterface.getDisplayName() + "]");
//////            System.out.println("Is It connected? : [" + networkInterface.isUp() + "]");
////
////            for (InterfaceAddress i : networkInterface.getInterfaceAddresses()) {
////                String hostName = i.getAddress().getCanonicalHostName();
////                String hostAddress = i.getAddress().getHostAddress();
////
//////                hostAddress.split("\\.");
////
////                String[] parts1 = hostAddress.split("\\.");
////                if (parts1.length != 4) continue;
////                int parts0Int = Integer.valueOf(parts1[0]);
////                if (parts0Int == 10) {
////                    int parts1Int = Integer.valueOf(parts1[1]);
////                    if (parts1Int < 0 || parts1Int > 255) continue;
////                    int parts2Int = Integer.valueOf(parts1[2]);
////                    if (parts2Int < 0 || parts2Int > 255) continue;
////                    int parts3Int = Integer.valueOf(parts1[3]);
////                    if (parts3Int < 0 || parts3Int > 255) continue;
////                    continue;
////                }
////
//
//        System.out.println(isOnLan());
////                if (QNetworkUtils.isOnLan()) {
////                    System.out.println("isOnLan);
////                }
//    }

///////////////////////////////////////////////////////////////////////////////////////////////////
} //////////// End Class //////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////





