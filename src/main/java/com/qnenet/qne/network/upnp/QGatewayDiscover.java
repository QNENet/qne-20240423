/*
 *              weupnp - Trivial upnp java library
 *
 * Copyright (C) 2008 Alessandro Bahgat Shehata, Daniele Castagna
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Alessandro Bahgat Shehata - ale dot bahgat at gmail dot com
 * Daniele Castagna - daniele dot castagna at gmail dot com
 *
 */
package com.qnenet.qne.network.upnp;

import com.qnenet.qne.objects.classes.QInetAddr;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class QGatewayDiscover {

    public static final int PORT = 1900;

    public static final String IP = "239.255.255.250";

    private static final int DEFAULT_TIMEOUT = 3000;

    private int timeout = DEFAULT_TIMEOUT;

    private String[] searchTypes;

    private static final String[] DEFAULT_SEARCH_TYPES =
        {
            "urn:schemas-upnp-org:device:InternetQGatewayDevice:1",
            "urn:schemas-upnp-org:service:WANIPConnection:1",
            "urn:schemas-upnp-org:service:WANPPPConnection:1"
        };

    private final Map<QInetAddr, QGatewayDevice> devices = new HashMap<>();

    private class SendDiscoveryThread extends Thread {
        InetAddress ip;
        String searchMessage;

        SendDiscoveryThread(InetAddress localIP, String searchMessage) {
            this.ip = localIP;
            this.searchMessage = searchMessage;
        }

        @Override
        public void run() {

            DatagramSocket ssdp = null;

            try {
                // Create socket bound to specified local address
                ssdp = new DatagramSocket(new InetSocketAddress(ip, 0));

                byte[] searchMessageBytes = searchMessage.getBytes();
                DatagramPacket ssdpDiscoverPacket = new DatagramPacket(searchMessageBytes, searchMessageBytes.length);
                ssdpDiscoverPacket.setAddress(InetAddress.getByName(IP));
                ssdpDiscoverPacket.setPort(PORT);

                ssdp.send(ssdpDiscoverPacket);
                ssdp.setSoTimeout(QGatewayDiscover.this.timeout);

                boolean waitingPacket = true;
                while (waitingPacket) {
                    DatagramPacket receivePacket = new DatagramPacket(new byte[1536], 1536);
                    try {
                        ssdp.receive(receivePacket);
                        byte[] receivedData = new byte[receivePacket.getLength()];
                        System.arraycopy(receivePacket.getData(), 0, receivedData, 0, receivePacket.getLength());

                        // Create QGatewayDevice from response
                        QGatewayDevice gatewayDevice = parseMSearchReply(receivedData);

                        gatewayDevice.setLocalInetAddress(ip);
                        gatewayDevice.loadDescription();

                        // verify that the search type is among the requested ones
                        if (Arrays.asList(searchTypes).contains(gatewayDevice.getSt())) {
                            synchronized (devices) {
                                devices.put(gatewayDevice.getQINetAddr(), gatewayDevice);
                                break; // device added for this ip, nothing further to do
                            }
                        }
                    } catch (SocketTimeoutException ste) {
                        waitingPacket = false;
                    }
                }

            } catch (Exception e) {
                // e.printStackTrace();
            } finally {
                if (null != ssdp) {
                    ssdp.close();
                }
            }
        }
    }

    public QGatewayDiscover() {
        this(DEFAULT_SEARCH_TYPES);
    }

    public QGatewayDiscover(String st) {
        this(new String[]{st});
    }

    public QGatewayDiscover(String[] types) {
        this.searchTypes = types;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int milliseconds) {
        this.timeout = milliseconds;
    }

    public Map<QInetAddr, QGatewayDevice> discover() throws SocketException, UnknownHostException, IOException, SAXException, ParserConfigurationException {

        Collection<InetAddress> ips = getLocalInetAddresses(true, false, false);

        for (int i = 0; i < searchTypes.length; i++) {

            String searchMessage = "M-SEARCH * HTTP/1.1\r\n" +
                    "HOST: " + IP + ":" + PORT + "\r\n" +
                    "ST: " + searchTypes[i] + "\r\n" +
                    "MAN: \"ssdp:discover\"\r\n" +
                    "MX: 2\r\n" +    // seconds to delay response
                    "\r\n";

            // perform search requests for multiple network adapters concurrently
            Collection<SendDiscoveryThread> threads = new ArrayList<SendDiscoveryThread>();
            for (InetAddress ip : ips) {
                SendDiscoveryThread thread = new SendDiscoveryThread(ip, searchMessage);
                threads.add(thread);
                thread.start();
            }

            // wait for all search threads to finish
            for (SendDiscoveryThread thread : threads)
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    // continue with next thread
                }

            // If a search type found devices, don't try with different search type
            if (!devices.isEmpty())
                break;

        } // loop SEARCHTYPES

        return devices;
    }

    private QGatewayDevice parseMSearchReply(byte[] reply) {

        QGatewayDevice device = new QGatewayDevice();

        String replyString = new String(reply);
        StringTokenizer st = new StringTokenizer(replyString, "\n");

        while (st.hasMoreTokens()) {
            String line = st.nextToken().trim();

            if (line.isEmpty())
                continue;

            if (line.startsWith("HTTP/1.") || line.startsWith("NOTIFY *"))
                continue;

            String key = line.substring(0, line.indexOf(':'));
            String value = line.length() > key.length() + 1 ? line.substring(key.length() + 1) : null;

            key = key.trim();
            if (value != null) {
                value = value.trim();
            }

            if (key.compareToIgnoreCase("location") == 0) {
                device.setLocation(value);

            } else if (key.compareToIgnoreCase("st") == 0) {    // Search Target
                device.setSt(value);
            }
        }

        return device;
    }

    public QGatewayDevice getValidGateway() {

        for (QGatewayDevice device : devices.values()) {
            try {
                if (device.isConnected()) {
                    return device;
                }
            } catch (Exception e) {
            }
        }

        return null;
    }

    public Map<QInetAddr, QGatewayDevice> getAllQGateways() {
        return devices;
    }

    private List<InetAddress> getLocalInetAddresses(boolean getIPv4, boolean getIPv6, boolean sortIPv4BeforeIPv6) {
        List<InetAddress> arrayIPAddress = new ArrayList<InetAddress>();
        int lastIPv4Index = 0;

        // Get all network interfaces
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return arrayIPAddress;
        }

        if (networkInterfaces == null)
            return arrayIPAddress;

        // For every suitable network interface, get all IP addresses
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface card = networkInterfaces.nextElement();

            try {
                // skip devices, not suitable to search gateways for
                if (card.isLoopback() || card.isPointToPoint() ||
                        card.isVirtual() || !card.isUp())
                    continue;
            } catch (SocketException e) {
                continue;
            }

            Enumeration<InetAddress> addresses = card.getInetAddresses();

            if (addresses == null)
                continue;

            while (addresses.hasMoreElements()) {
                InetAddress inetAddress = addresses.nextElement();
                int index = arrayIPAddress.size();

                if (!getIPv4 || !getIPv6) {
                    if (getIPv4 && !Inet4Address.class.isInstance(inetAddress))
                        continue;

                    if (getIPv6 && !Inet6Address.class.isInstance(inetAddress))
                        continue;
                } else if (sortIPv4BeforeIPv6 && Inet4Address.class.isInstance(inetAddress)) {
                    index = lastIPv4Index++;
                }

                arrayIPAddress.add(index, inetAddress);
            }
        }

        return arrayIPAddress;
    }

}
