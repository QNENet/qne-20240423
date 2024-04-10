package com.qnenet.qne.objects.classes;

//import com.qnenet.qne.utils.QNetworkUtils;

import com.qnenet.qne.system.utils.QNetworkUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The QInetAddr class represents an IP address.
 */
public class QInetAddr {

    private byte[] ipAddressBytes;

    /**
     * Default constructor for Kryo serialization.
     */
    public QInetAddr() {
        // for Kryo
    }

    /**
     * Constructs a QInetAddr object from an InetAddress.
     *
     * @param inetAddress The InetAddress object representing the IP address.
     */
    public QInetAddr(InetAddress inetAddress) {

    }

    /**
     * Constructs a QInetAddr object from a byte array.
     *
     * @param ipAddressBytes The byte array representing the IP address.
     */
    public QInetAddr(byte[] ipAddressBytes) {
        this.ipAddressBytes = ipAddressBytes;
    }

    /**
     * Constructs a QInetAddr object from a string representation of an IP address.
     *
     * @param ipAddress The string representation of the IP address.
     */
    public QInetAddr(String ipAddress) {
        this.ipAddressBytes = QNetworkUtils.ipAddressToBytes(ipAddress);
    }

    /**
     * Returns the InetAddress object representing the IP address.
     *
     * @return The InetAddress object representing the IP address.
     * @throws RuntimeException if the IP address is unknown.
     */
    public InetAddress getInetAddr() {
        try {
            return InetAddress.getByAddress(ipAddressBytes);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the byte array representing the IP address.
     *
     * @return The byte array representing the IP address.
     */
    public byte[] getIPAddrBytes() {
        return ipAddressBytes;
    }
}
