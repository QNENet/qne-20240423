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
import com.qnenet.qne.objects.classes.QMappedPort;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecation")
public class QGatewayDevice {

    private static final int DEFAULT_HTTP_RECEIVE_TIMEOUT = 7000;

    private String st;
    private String location;
    private String serviceType;
    private String serviceTypeCIF;
    private String urlBase;
    private String controlURL;
    private String controlURLCIF;
    private String eventSubURL;
    private String eventSubURLCIF;
    private String sCPDURL;
    private String sCPDURLCIF;
    private String deviceType;
    private String deviceTypeCIF;


    private String friendlyName;

    private String manufacturer;

    private String modelDescription;

    private String presentationURL;


    private String wanAddress;

    private String lanAddress;

    private String modelNumber;

    private String modelName;

    private transient InetAddress localInetAddress;

    private QInetAddr localQInetAddr;

    private static int httpReadTimeout = DEFAULT_HTTP_RECEIVE_TIMEOUT;

    public QGatewayDevice() {
    }

    public void loadDescription() throws SAXException, IOException {

        URLConnection urlConn = new URL(getLocation()).openConnection();
        urlConn.setReadTimeout(httpReadTimeout);

        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(new QGatewayDeviceHandler(this));
        parser.parse(new InputSource(urlConn.getInputStream()));


        /* fix urls */
        String ipConDescURL;
        if (urlBase != null && urlBase.trim().length() > 0) {
            ipConDescURL = urlBase;
        } else {
            ipConDescURL = location;
        }

        int lastSlashIndex = ipConDescURL.indexOf('/', 7);
        if (lastSlashIndex > 0) {
            ipConDescURL = ipConDescURL.substring(0, lastSlashIndex);
        }


        sCPDURL = copyOrCatUrl(ipConDescURL, sCPDURL);
        controlURL = copyOrCatUrl(ipConDescURL, controlURL);
        controlURLCIF = copyOrCatUrl(ipConDescURL, controlURLCIF);
        presentationURL = copyOrCatUrl(ipConDescURL, presentationURL);
    }

    public static Map<String, String> simpleUPnPcommand(String url,
                                                        String service, String action, Map<String, String> args)
            throws IOException, SAXException {
        String soapAction = "\"" + service + "#" + action + "\"";
        StringBuilder soapBody = new StringBuilder();

        soapBody.append("<?xml version=\"1.0\"?>\r\n" +
                "<SOAP-ENV:Envelope " +
                "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                "<SOAP-ENV:Body>" +
                "<m:" + action + " xmlns:m=\"" + service + "\">");

        if (args != null && args.size() > 0) {

            Set<Map.Entry<String, String>> entrySet = args.entrySet();

            for (Map.Entry<String, String> entry : entrySet) {
                soapBody.append("<" + entry.getKey() + ">" + entry.getValue() +
                        "</" + entry.getKey() + ">");
            }

        }

        soapBody.append("</m:" + action + ">");
        soapBody.append("</SOAP-ENV:Body></SOAP-ENV:Envelope>");

        URL postUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) postUrl.openConnection();

        conn.setRequestMethod("POST");
        conn.setConnectTimeout(httpReadTimeout);
        conn.setReadTimeout(httpReadTimeout);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml");
        conn.setRequestProperty("SOAPAction", soapAction);
        conn.setRequestProperty("Connection", "Close");

        byte[] soapBodyBytes = soapBody.toString().getBytes();

        conn.setRequestProperty("Content-Length",
                String.valueOf(soapBodyBytes.length));

        conn.getOutputStream().write(soapBodyBytes);

        Map<String, String> nameValue = new HashMap<String, String>();
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(new QNameValueHandler(nameValue));
        if (conn.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            try {
                // attempt to parse the error message
                parser.parse(new InputSource(conn.getErrorStream()));
            } catch (SAXException e) {
                // ignore the exception
                // FIXME We probably need to find a better way to return
                // significant information when we reach this point
            }
            conn.disconnect();
            return nameValue;
        } else {
            parser.parse(new InputSource(conn.getInputStream()));
            conn.disconnect();
            return nameValue;
        }
    }

    public boolean isConnected() throws IOException, SAXException {
        Map<String, String> nameValue = simpleUPnPcommand(controlURL,
                serviceType, "GetStatusInfo", null);

        String connectionStatus = nameValue.get("NewConnectionStatus");
        if (connectionStatus != null
                && connectionStatus.equalsIgnoreCase("Connected")) {
            return true;
        }

        return false;
    }

    public String getExternalIPAddress() throws IOException, SAXException {
        Map<String, String> nameValue = simpleUPnPcommand(controlURL,
                serviceType, "GetExternalIPAddress", null);

        return nameValue.get("NewExternalIPAddress");
    }

    public boolean addPortMapping(int externalPort, int internalPort,
                                  String internalClient, String protocol, String description)
            throws IOException, SAXException {
        Map<String, String> args = new LinkedHashMap<String, String>();
        args.put("NewRemoteHost", "");    // wildcard, any remote host matches
        args.put("NewExternalPort", Integer.toString(externalPort));
        args.put("NewProtocol", protocol);
        args.put("NewInternalPort", Integer.toString(internalPort));
        args.put("NewInternalClient", internalClient);
        args.put("NewEnabled", Integer.toString(1));
        args.put("NewPortMappingDescription", description);
        args.put("NewLeaseDuration", Integer.toString(0));

        Map<String, String> nameValue = simpleUPnPcommand(controlURL,
                serviceType, "AddPortMapping", args);

        return nameValue.get("errorCode") == null;
    }

    public boolean getSpecificPortMappingEntry(int externalPort,
                                               String protocol, final QMappedPort mappedPort)
            throws IOException, SAXException {

        mappedPort.setExternalPort(externalPort);
        mappedPort.setProtocol(protocol);

        Map<String, String> args = new LinkedHashMap<String, String>();
        args.put("NewRemoteHost", ""); // wildcard, any remote host matches
        args.put("NewExternalPort", Integer.toString(externalPort));
        args.put("NewProtocol", protocol);

        Map<String, String> nameValue = simpleUPnPcommand(controlURL,
                serviceType, "GetSpecificPortMappingEntry", args);

        if (nameValue.isEmpty() || nameValue.containsKey("errorCode"))
            return false;

        if (!nameValue.containsKey("NewInternalClient") ||
                !nameValue.containsKey("NewInternalPort"))
            return false;

        mappedPort.setProtocol(protocol);
        mappedPort.setEnabled(nameValue.get("NewEnabled"));
        mappedPort.setInternalClient(nameValue.get("NewInternalClient"));
        mappedPort.setExternalPort(externalPort);
        mappedPort.setPortMappingDescription(nameValue.get("NewPortMappingDescription"));
        mappedPort.setRemoteHost(nameValue.get("NewRemoteHost"));

        try {
            mappedPort.setInternalPort(Integer.parseInt(nameValue.get("NewInternalPort")));
        } catch (NumberFormatException nfe) {
            // skip bad port
        }


        return true;
    }

    public boolean getMappedPortByIdx(int index, final QMappedPort mappedPort)
            throws IOException, SAXException {
        Map<String, String> args = new LinkedHashMap<String, String>();
        args.put("NewPortMappingIndex", Integer.toString(index));

        Map<String, String> nameValue = simpleUPnPcommand(controlURL,
                serviceType, "GetGenericPortMappingEntry", args);

        if (nameValue.isEmpty() || nameValue.containsKey("errorCode"))
            return false;

        mappedPort.setRemoteHost(nameValue.get("NewRemoteHost"));
        mappedPort.setInternalClient(nameValue.get("NewInternalClient"));
        mappedPort.setProtocol(nameValue.get("NewProtocol"));
        mappedPort.setEnabled(nameValue.get("NewEnabled"));
        mappedPort.setPortMappingDescription(
                nameValue.get("NewPortMappingDescription"));

        try {
            mappedPort.setInternalPort(Integer.parseInt(nameValue.get("NewInternalPort")));
        } catch (Exception e) {
        }

        try {
            mappedPort.setExternalPort(
                    Integer.parseInt(nameValue.get("NewExternalPort")));
        } catch (Exception e) {
        }

        return true;
    }

    public Integer getPortMappingNumberOfEntries() throws IOException, SAXException {
        Map<String, String> nameValue = simpleUPnPcommand(controlURL,
                serviceType, "GetPortMappingNumberOfEntries", null);

        Integer portMappingNumber = null;

        try {
            portMappingNumber = Integer.valueOf(
                    nameValue.get("NewPortMappingNumberOfEntries"));
        } catch (Exception e) {
        }

        return portMappingNumber;
    }

    public boolean deletePortMapping(int externalPort, String protocol) throws IOException, SAXException {
        Map<String, String> args = new LinkedHashMap<String, String>();
        args.put("NewRemoteHost", "");
        args.put("NewExternalPort", Integer.toString(externalPort));
        args.put("NewProtocol", protocol);
        @SuppressWarnings("unused")
        Map<String, String> nameValue = simpleUPnPcommand(controlURL,
                serviceType, "DeletePortMapping", args);

        return true;
    }

    public InetAddress getLocalInetAddress() {
        localInetAddress = localQInetAddr.getInetAddr();
        return localInetAddress;
    }

    public void setLocalInetAddress(InetAddress localInetAddress) {
        this.localInetAddress = localInetAddress;
        String hostAddress = localInetAddress.getHostAddress();
        localQInetAddr = new QInetAddr(hostAddress);
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceTypeCIF() {
        return serviceTypeCIF;
    }

    public void setServiceTypeCIF(String serviceTypeCIF) {
        this.serviceTypeCIF = serviceTypeCIF;
    }

    public String getControlURL() {
        return controlURL;
    }

    public void setControlURL(String controlURL) {
        this.controlURL = controlURL;
    }

    public String getControlURLCIF() {
        return controlURLCIF;
    }

    public void setControlURLCIF(String controlURLCIF) {
        this.controlURLCIF = controlURLCIF;
    }

    public String getEventSubURL() {
        return eventSubURL;
    }

    public void setEventSubURL(String eventSubURL) {
        this.eventSubURL = eventSubURL;
    }

    public String getEventSubURLCIF() {
        return eventSubURLCIF;
    }

    public void setEventSubURLCIF(String eventSubURLCIF) {
        this.eventSubURLCIF = eventSubURLCIF;
    }

    public String getSCPDURL() {
        return sCPDURL;
    }

    public void setSCPDURL(String sCPDURL) {
        this.sCPDURL = sCPDURL;
    }

    public String getSCPDURLCIF() {
        return sCPDURLCIF;
    }

    public void setSCPDURLCIF(String sCPDURLCIF) {
        this.sCPDURLCIF = sCPDURLCIF;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceTypeCIF() {
        return deviceTypeCIF;
    }

    public void setDeviceTypeCIF(String deviceTypeCIF) {
        this.deviceTypeCIF = deviceTypeCIF;
    }

    public String getURLBase() {
        return urlBase;
    }

    public void setURLBase(String uRLBase) {
        this.urlBase = uRLBase;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    public String getPresentationURL() {
        return presentationURL;
    }

    public void setPresentationURL(String presentationURL) {
        this.presentationURL = presentationURL;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public static int getHttpReadTimeout() {
        return httpReadTimeout;
    }

    public static void setHttpReadTimeout(int milliseconds) {
        httpReadTimeout = milliseconds;
    }

    private String copyOrCatUrl(String dst, String src) {
        if (src != null) {
            if (src.startsWith("http://")) {
                dst = src;
            } else {
                if (!src.startsWith("/")) {
                    dst += "/";
                }
                dst += src;
            }
        }
        return dst;
    }

    //    public void setWanAddress(String externalIPAddress) {
//        this.wanAddress = externalIPAddress;
//    }
//
//    public String getLanAddress() {
//         return lanAddress;
//
//    }
    public void setWanAddress(String externalIPAddress) {
        this.wanAddress = externalIPAddress;
    }

    public void setLanAddress() {
        this.lanAddress = localInetAddress.getHostAddress();
    }

    public String getAsString(int arrayIdx) {
        StringBuffer sBuf = new StringBuffer();
        sBuf.append("Listing gateway details of device #");
        sBuf.append(arrayIdx);
        sBuf.append("\n\t");
        sBuf.append("Manufacturer = ");
        sBuf.append(manufacturer);
        sBuf.append("\n\t");
        sBuf.append("Model Desc = ");
        sBuf.append(modelDescription);
        sBuf.append("\n\t");
        sBuf.append("Model Name = ");
        sBuf.append(modelName);
        sBuf.append("\n\t");
        sBuf.append("Model Number = ");
        sBuf.append(modelNumber);
        sBuf.append("\n\t");
        sBuf.append("Friendly Name = ");
        sBuf.append(friendlyName);
        sBuf.append("\n\t");
        sBuf.append("Presentation URL = ");
        sBuf.append(presentationURL);
        sBuf.append("\n\t");
        sBuf.append("Wan IP Address = ");
        sBuf.append(wanAddress);
        sBuf.append("\n\t");
        sBuf.append("Lan IP Address = ");
        sBuf.append(lanAddress);
        sBuf.append("\n\t");
        return sBuf.toString();
    }

    public QInetAddr getQINetAddr() {
        return localQInetAddr;
    }
}
