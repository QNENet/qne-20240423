//package com.qnenet.qne.network.channel;
//
//import com.qnenet.qne.objects.impl.QNEObjects;
//import com.qnenet.qne.system.constants.QSysConstants;
//import com.qnenet.qne.network.endpoint.QEndPoint;
//import com.qnenet.qne.objects.classes.QChannelInfo;
//import com.qnenet.qne.objects.classes.QEPAddr;
//import com.qnenet.qne.objects.classes.QPacketOwner;
//import com.qnenet.qne.objects.classes.QNetMsg;
//import com.qnenet.qne.network.packet.QPacketHandler;
//import com.qnenet.qne.system.impl.QSystem;
//import com.qnenet.qne.system.utils.QThreadUtils;
//import com.southernstorm.noise.protocol.CipherState;
//import com.southernstorm.noise.protocol.CipherStatePair;
//import com.southernstorm.noise.protocol.DHState;
//import com.southernstorm.noise.protocol.HandshakeState;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.ShortBufferException;
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.security.NoSuchAlgorithmException;
//import java.util.Arrays;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import java.util.concurrent.LinkedBlockingQueue;
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Class ///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//@Component
//@Scope("prototype")
//public class QChannelServer {
//
//    private static final long CHANNEL_MSG_TIMEOUT_1 = 2000;
//    private static final long CHANNEL_MSG_TIMEOUT_2 = 4000;
//    private static final long CHANNEL_MSG_TIMEOUT_3 = 8000;
//
//    @Autowired
//    QSystem system;
//
//    @Autowired
//    QPacketHandler packetHandler;
//
//    @Autowired
//    QNEObjects qobjs;
//
//    ////// Handshake //////////////////////////////////////////////////////////////////////////////
//
//    private static final int HANDSHAKE_BUFFER_SIZE = 65535;
//
//    private HandshakeState handshake;
//    private byte[] workBuffer = new byte[HANDSHAKE_BUFFER_SIZE];
//
//    private CipherStatePair cipherPair;
//
//    ////// Fields /////////////////////////////////////////////////////////////////////////////////
//
//    QChannelInfo channelInfo;
//
//    String displayName;
//    int role;
//
//    InetSocketAddress returnSocketAddress;
//    QEndPoint endPoint;
//
//    private boolean sameLocation;
//    private boolean sameLan;
//    private boolean sameMachine;
//
//    private QNetMsg firstNetMsg;
//    private boolean busy;
//    private QPacketOwner owner;
//    private int channelStatus;
//
//    private final BlockingQueue<DatagramPacket> receivedPacketsQueue = new LinkedBlockingQueue<>();
//    private int mode;
//
///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// Constructor /////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public QChannelServer() {
//        QThreadUtils.showThreadName(getDisplayName() + " Channel constructor");
//    }
//
//
///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// Accept //////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    public void connect() throws ExecutionException, InterruptedException {
////
////        Future<DatagramPacket> handshakeFuture = (Future<DatagramPacket>) system.getExecutor().submit(this::run);
////
////        DatagramPacket packet = handshakeFuture.get();
////        QThreadUtils.showThreadName(getDisplayName() + " Channel Connect After Future Get");
////    }
////
////    private void run() {
////
////        QThreadUtils.setThreadName(String.valueOf(channelInfo.channelId));
////        QThreadUtils.showThreadName(getDisplayName() + " Channel Connect Future");
////        try {
////            startHandshake(null);
////        } catch (NoSuchAlgorithmException e) {
////            throw new RuntimeException(e);
////        } catch (ShortBufferException e) {
////            throw new RuntimeException(e);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        } catch (InterruptedException e) {
////            throw new RuntimeException(e);
////        } catch (BadPaddingException e) {
////            throw new RuntimeException(e);
////        }
////
//////            /* Run the handshake until we run out of things to read or write */
////        while (true) {
////            int action = handshake.getAction();
////            if (action == HandshakeState.WRITE_MESSAGE) {
////
////            } else if (action == HandshakeState.READ_MESSAGE) {
//////                    /* Read the next handshake message and discard the payload */
////                DatagramPacket receivedPacket = null;
////                try {
////                    receivedPacket = receivedPacketsQueue.take();
////                } catch (InterruptedException e) {
////                    throw new RuntimeException(e);
////                }
////
////                byte[] packetData = receivedPacket.getData();
////            } else {
////                break;
////            }
////        } // while loop
////
////
//////            DatagramPacket packet = receivedPacketsQueue.take();
//////            return packet;
////    }
//
//
//    public void accept(DatagramPacket packet) throws ExecutionException, InterruptedException {
//        Future<DatagramPacket> handshakeFuture = (Future<DatagramPacket>) system.getExecutor().submit(() -> {
//            QThreadUtils.setThreadName("S" + String.valueOf(channelInfo.channelId));
//            QThreadUtils.showThreadName(getDisplayName() + " Channel Accept Future");
//            try {
//                startHandshake(packet);
//            } catch (NoSuchAlgorithmException e) {
//                throw new RuntimeException(e);
//            } catch (ShortBufferException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            } catch (BadPaddingException e) {
//                throw new RuntimeException(e);
//            }
//
//
//        });
//
//        DatagramPacket receivedPacket = handshakeFuture.get();
//        QThreadUtils.showThreadName(getDisplayName() + " Channel Accept After Future Get");
//
//
//
//
//
////        DatagramPacket finalPacket = packet; // required for access in task thread
////        QThreadUtils.showThreadName(getDisplayName() + " Channel Server accept");
//////        returnSocketAddress = (InetSocketAddress) packet.getSocketAddress();
////        receivedPacketsQueue.put(finalPacket);
////        Future<DatagramPacket> handshake1 = system.getExecutor().submit(() -> {
////            QThreadUtils.setThreadName("S" + String.valueOf(channelInfo.channelId));
////            QThreadUtils.showThreadName(getDisplayName() + " Channel Future Responder Accept");
////            startHandshake(finalPacket);
////            return finalPacket;
////        });
////
////        packet = handshake1.get();
////        QThreadUtils.showThreadName(getDisplayName() + " Channel After Responder Accept Future Get");
////        System.out.println();
//    }
//
///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// Start Handshake /////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void startHandshake(DatagramPacket packet) throws NoSuchAlgorithmException,
//            ShortBufferException, IOException, InterruptedException, BadPaddingException {
//        QThreadUtils.showThreadName(getDisplayName() + " Channel startHandshake");
//        this.busy = true;
//
//        handshake = new HandshakeState("Noise_XX_25519_ChaChaPoly_BLAKE2b", channelInfo.role);
//
//        DHState localKeyPair = handshake.getLocalKeyPair();
//        byte[] noisePrivateKeyClone = endPoint.getNoisePrivateKeyClone();
//        localKeyPair.setPrivateKey(noisePrivateKeyClone, 0);
//
//        handshake.start();
//
//        mode = QSysConstants.CHANNEL_STATUS_NEW_CHANNEL;
//
//        //            /* Run the handshake until we run out of things to read or write */
//        while (true) {
//            int action = handshake.getAction();
//            if (action == HandshakeState.WRITE_MESSAGE) {
//                QThreadUtils.showThreadName(getDisplayName() + " Channel while loop");
//
//                byte[] payload;
//                int payloadLength;
//                if (mode == QSysConstants.CHANNEL_STATUS_NEW_CHANNEL) {
//                    payload = null;
//                    payloadLength = 0;
//                } else {
//                    payload = null;
//                    payloadLength = 0;
//                }
//
//                int size = handshake.writeMessage(workBuffer, 0, payload, 0, payloadLength);
//
//                byte[] bytes = Arrays.copyOf(workBuffer, size);
//                ByteBuffer bb = ByteBuffer.allocate(size + 12);
//                bb.putInt(0, channelInfo.initiatorEPAddr.epIdx);
//                bb.putInt(4, channelInfo.channelId);
//                bb.putInt(8, channelInfo.role);
//                bb.put(12, bytes);
//
//                DatagramPacket writePacket = new DatagramPacket(bb.array(), 0, bb.array().length, returnSocketAddress);
//                QThreadUtils.showThreadName(getDisplayName() + " Channel writeFirstResponderMessage sendPacket");
//
//                mode++;
//                packetHandler.sendPacket(writePacket);
//
//            } else if (action == HandshakeState.READ_MESSAGE) {
//
//                DatagramPacket receivedPacket = receivedPacketsQueue.take();
//                byte[] packetData = receivedPacket.getData();
//                int length = receivedPacket.getLength(); // packetData length is 65k, we have to use packet.getLength
//                ByteBuffer packetBB = ByteBuffer.wrap(packetData);
//                int endPointIdx = packetBB.getInt();
//                int channelId = packetBB.getInt();
//                int role = packetBB.getInt();
//                byte[] msgBytes = new byte[length - 12];
//                packetBB.get(msgBytes);
//
//                int payloadSize = 0;
//                payloadSize = handshake.readMessage(msgBytes, 0, msgBytes.length, workBuffer, 0);
//                if (payloadSize > 0) {
//                    byte[] payloadBytes = new byte[payloadSize];
//                    ByteBuffer payloadBB = ByteBuffer.wrap(workBuffer);
//                    payloadBB.get(payloadBytes);
//                    QChannelInfo reeivedChannelInfo = (QChannelInfo) qobjs.objFromBytes(payloadBytes);
//                    channelInfo.initiatorEPAddr = reeivedChannelInfo.initiatorEPAddr;
//                }
//            } else {
//                break;
//            }
//        } // while loop
//
//
////        int action = handshake.getAction();
////        if (action == HandshakeState.INITIATOR) {
////            writeFirstInitiatorMessage();
////        } else {
////            readFirstResponderMessage(packet);
////        }
//
//
//    }
//
//    public void receivePacket(DatagramPacket packet) throws InterruptedException {
//        receivedPacketsQueue.put(packet);
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////// Initiator Meassages  ////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
////
////    private void writeFirstInitiatorMessage() throws
////            ShortBufferException, IOException, BadPaddingException, InterruptedException {
////        QThreadUtils.showThreadName(getDisplayName() + " Channel writeFirstInitiatorMessage");
////        byte[] payload = qobjs.objToBytes(channelInfo);
////        int size = handshake.writeMessage(workBuffer, 0, payload, 0, payload.length);
////
////        byte[] bytes = Arrays.copyOf(workBuffer, size);
////        ByteBuffer bb = ByteBuffer.allocate(size + 12);
////        bb.putInt(0, channelInfo.responderEndPointInfo.endPointIdx);
////        bb.putInt(4, channelInfo.channelId);
////        bb.putInt(8, channelInfo.role);
////        bb.put(12, bytes);
////
////        DatagramPacket packet = new DatagramPacket(bb.array(), 0, bb.array().length, destSocketAddress);
////        QThreadUtils.showThreadName(getDisplayName() + " Channel writeFirstInitiatorMessage sendPacket");
////
////        packetHandler.sendPacket(packet);
//////        channelStatus = QSysConstants.CHANNEL_STATUS_RESPONDER_WAITING_FOR_HANDSHAKE_FIRST;
////    }
////
////    private void readFirstResponderMessage(DatagramPacket receivedPacket) throws
////            InterruptedException, ShortBufferException, BadPaddingException, IOException {
//////        ByteBuffer packetBB = channelReceivePacketBBQueue.take();
//////        int endPointIdx = packetBB.getInt();
////        byte[] packetData = receivedPacket.getData();
////        int length = receivedPacket.getLength(); // packetData length is 65k, we have to use packet.getLength
////        ByteBuffer packetBB = ByteBuffer.wrap(packetData, 0, length);
////        int endPointIdx = packetBB.getInt();
////        int channelId = packetBB.getInt();
////        int role = packetBB.getInt();
////        byte[] msgBytes = new byte[packetBB.remaining()];
////        packetBB.get(msgBytes, 0, msgBytes.length);
////
////        int payloadSize = 0;
////        payloadSize = handshake.readMessage(msgBytes, 0, msgBytes.length, workBuffer, 0);
////        if (payloadSize > 0) {
//////            byte[] payloadBytes = new byte[payloadSize];
//////            ByteBuffer payloadBB = ByteBuffer.wrap(workBuffer,0, payloadSize);
//////            payloadBB.get(payloadBytes);
////            Object obj = qobjs.objFromBytes(workBuffer);
////            if (obj == null) return;
////            if (obj instanceof QChannelInfo) {
////                QChannelInfo reeivedChannelInfo = (QChannelInfo) obj;
////                channelInfo.initiatorEndPointInfo = reeivedChannelInfo.initiatorEndPointInfo;
////            } else return;
////        }
////
////        byte[] payload = null;
////        int size = handshake.writeMessage(workBuffer, 0, payload, 0, 0);
////
////        byte[] bytes = Arrays.copyOf(workBuffer, size);
////        ByteBuffer bb = ByteBuffer.allocate(size + 12);
////        bb.putInt(0, channelInfo.initiatorEndPointInfo.endPointIdx);
////        bb.putInt(4, channelInfo.channelId);
////        bb.putInt(8, channelInfo.role);
////        bb.put(12, bytes);
////
////        DatagramPacket packet = new DatagramPacket(bb.array(), 0, bb.array().length, destSocketAddress);
////        packetHandler.sendPacket(packet);
////    }
////
////
////    private void readFirstInitiatorMessage(ByteBuffer packetBB) throws
////            ShortBufferException, BadPaddingException, InterruptedException {
//////        ByteBuffer packetBB = channelReceivePacketBBQueue.take();
////        int endPointIdx = packetBB.getInt();
////        int channelId = packetBB.getInt();
////        int role = packetBB.getInt();
////        byte[] msgBytes = new byte[packetBB.remaining()];
////        packetBB.get(12, msgBytes, 0, msgBytes.length);
////
////        int payloadSize = 0;
////        payloadSize = handshake.readMessage(msgBytes, 0, msgBytes.length, workBuffer, 0);
////        if (payloadSize > 0) {
////            byte[] payloadBytes = new byte[payloadSize];
////            ByteBuffer payloadBB = ByteBuffer.wrap(workBuffer);
////            payloadBB.get(payloadBytes);
////            QChannelInfo reeivedChannelInfo = (QChannelInfo) qobjs.objFromBytes(payloadBytes);
////            channelInfo.initiatorEndPointInfo = reeivedChannelInfo.initiatorEndPointInfo;
////        }
////    }
////
////    private void writeSecondInitiatorMessage() throws
////            ShortBufferException, IOException, BadPaddingException, InterruptedException {
////        byte[] payload = qobjs.objToBytes(channelInfo);
////        int size = handshake.writeMessage(workBuffer, 0, payload, 0, payload.length);
////
////        byte[] bytes = Arrays.copyOf(workBuffer, size);
////        ByteBuffer bb = ByteBuffer.allocate(size + 12);
////        bb.putInt(0, channelInfo.responderEndPointInfo.endPointIdx);
////        bb.putInt(4, channelInfo.channelId);
////        bb.putInt(8, channelInfo.role);
////        bb.put(12, bytes);
////
////        DatagramPacket packet = new DatagramPacket(bb.array(), 0, bb.array().length, destSocketAddress);
////        packetHandler.sendPacket(packet);
////    }
////
////
//
//
///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// Receive Packet /////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    public void receivePacket(DatagramPacket packet) throws
////            ShortBufferException, BadPaddingException, NoSuchAlgorithmException, IOException {
////        QThreadUtils.showThreadName(getDisplayName() + " Channel Receive Packet");
////        byte[] packetData = packet.getData();
////        int length = packet.getLength(); // packetData length is 65k, we have to use packet.getLength
////        ByteBuffer byteBuffer = ByteBuffer.wrap(packetData, 0, length);
////        int channelId = byteBuffer.getInt(4);
////        int role = reverseChannelMsgDirection(byteBuffer.getInt(8));
////        byte[] msgBytes = new byte[length - 12];
////        byteBuffer.get(12, msgBytes);
////
////        if (cipherPair != null) {
////            byte[] netMsgPlainBytes = decryptBytes(msgBytes);
////            QNetMsg netMsg = (QNetMsg) qobjs.objFromBytes(netMsgPlainBytes);
////            if (role == HandshakeState.INITIATOR) {
////                netMsg.handleRequest(netMsg);
////            } else {
////                netMsg.handleResponse(netMsg);
////            }
////            return;
////        }
////
////        if (handshake.getAction() == HandshakeState.READ_MESSAGE) {
////            int payloadSize = 0;
////            payloadSize = handshake.readMessage(msgBytes, 0, msgBytes.length, workBuffer, 0);
////            if (payloadSize > 0) {
////
////            }
////
////            afterRead();
////
////        }
////    }
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////// After Read ////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
////
//    private void afterRead() throws ShortBufferException {
//        int action = handshake.getAction();
//        switch (action) {
//            case HandshakeState.WRITE_MESSAGE:
//                byte[] payload = null;
//                int payloadLength = 0;
////                if (firstNetMsgBytes != null) {
////                    payload = firstNetMsgBytes;
////                    payloadLength = payload.length;
////                }
//
//                int size = 0;
//                size = handshake.writeMessage(workBuffer, 0, payload, 0, payloadLength);
//
////                netMsgHandshake.netMsgBytes = setHandshakeBytes(workBuffer, size);
////                reverseChannelMsgDirection(netMsgHandshake);
////                packetHandler.sendPacket(netMsgHandshake);
//                afterWrite();
//                break;
//
//            case HandshakeState.SPLIT: // server split
//                cipherPair = handshake.split();
//                if (handshake.getAction() == HandshakeState.COMPLETE) {
////                    printMsg(displayName + " -> Handshake COMPLETE");
////                    if (firstNetMsgBytes != null) {
////                        netMsgHandshake.netMsgBytes = firstNetMsgBytes;
////                        netMsgHandshake.handleRequest(netMsgHandshake);
////                    }
//
//                } else {
//                    badResult(displayName + " Noise afterRead HandshakeState.FAILED");
//                    return;
//                }
//                break;
//
//            case HandshakeState.FAILED:
//                printMsg(displayName + "After Read Handshake FAILED -> pktId : ");
//                badResult("afterRead -> handshake FAILED");
//        }
//    }
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////// After Write ///////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
////
//    private void afterWrite() {
//        switch (handshake.getAction()) {
//            case HandshakeState.SPLIT: // client split
//                printMsg(displayName + "After Write Handshake SPLIT -> pktId : "); // + handshakePktId);
//                cipherPair = handshake.split();
//                if (handshake.getAction() == HandshakeState.COMPLETE) {
//                    printMsg(displayName + " -> Handshake COMPLETE");
//                    return;
//                }
//                break;
//            case HandshakeState.FAILED:
//                printMsg(displayName + "After Write Handshake FAILED -> pktId : "); // + handshakePktId);
//                makeSafe();
////                makeSafe(rt);
//
//            case HandshakeState.READ_MESSAGE:
//                break;
//        }
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////// Encrypt & Decrypt /////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    //    @Override
//    public byte[] encryptBytes(byte[] bytes) throws ShortBufferException {
//        CipherState cipherStateSender = cipherPair.getSender();
//        int encryptedSize = 0;
//        encryptedSize = cipherStateSender.encryptWithAd(null, bytes, 0, workBuffer, 0, bytes.length);
//        ByteBuf bb1 = Unpooled.wrappedBuffer(workBuffer);
//        byte[] encryptedBytes = new byte[encryptedSize];
//        bb1.readBytes(encryptedBytes);
//        return encryptedBytes;
//    }
//
//
//    //    @Override
//    public byte[] decryptBytes(byte[] bytes) throws ShortBufferException, BadPaddingException {
//        CipherState cipherStateReceiver = cipherPair.getReceiver();
//        int decryptedSize = 0;
//        decryptedSize = cipherStateReceiver.decryptWithAd(null, bytes, 0, workBuffer, 0, bytes.length);
//        ByteBuf bb = Unpooled.wrappedBuffer(workBuffer);
//        byte[] plainBytes = new byte[decryptedSize];
//        bb.readBytes(plainBytes);
//        return plainBytes;
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Getters & Setters /////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void setReturnSocketAddress(InetSocketAddress socketAddress) {
//        this.returnSocketAddress = socketAddress;
//    }
//
//    public int getChannelId() {
//        return channelInfo.channelId;
//    }
//
//    public QEPAddr getInitiatorEPAddr() {
//        return channelInfo.initiatorEPAddr;
//    }
//
//    public QEPAddr getResponderQEPAddr() {
//        return channelInfo.responderEPAddr;
//    }
//
//    public String getDisplayName() {
//        return displayName;
//    }
//
//    public void setDisplayName(String displayName) {
//        this.displayName = displayName;
//    }
//
//    public int getRole() {
//        return role;
//    }
//
//    public void setRole(int role) {
//        this.role = role;
//    }
//
////    public InetSocketAddress getDestSocketAddress() {
////        return destSocketAddress;
////    }
//
//
////    public void setDestSocketAddress() {
////        String initiatorWanIPAddress = getInitiatorEndPointInfo().getWanIPAddress();
////        String initiatorLanIPAddress = getInitiatorEndPointInfo().getLanIPAddress();
////        int initiatorPort = getInitiatorEndPointInfo().port;
////        String responderWanIPAddress = getInitiatorEndPointInfo().getWanIPAddress();
////        String responderLanIPAddress = getInitiatorEndPointInfo().getLanIPAddress();
////        int responderPort = getResponderEndPointInfo().port;
////
////        if (channelInfo.role == HandshakeState.INITIATOR) { // initiator
////            // different location
////            if (!initiatorWanIPAddress.equals(responderWanIPAddress)) {
////                destSocketAddress = new InetSocketAddress(responderWanIPAddress, responderPort);
////                sameLocation = false;
////                sameLan = false;
////                sameMachine = false;
////                return;
////            }
////
////            // same location
////            String destIPAddress;
////            int destPort;
////            if (initiatorLanIPAddress.equals(responderLanIPAddress)) { // same location
////                if (initiatorPort == responderPort) { //  same machine
////                    destIPAddress = responderLanIPAddress;
////                    destPort = responderPort;
////                    sameLocation = true;
////                    sameLan = true;
////                    sameMachine = true;
////                } else { //  same lan
////                    destIPAddress = responderLanIPAddress;
////                    destPort = responderPort;
////                    sameLocation = true;
////                    sameLan = true;
////                    sameMachine = false;
////                }
////                destSocketAddress = new InetSocketAddress(destIPAddress, destPort);
////                return;
////            }
////        }
////
////        // responder
////        // different location
////        if (!initiatorWanIPAddress.equals(responderWanIPAddress)) {
////            destSocketAddress = new InetSocketAddress(initiatorWanIPAddress, initiatorPort);
////            sameLocation = false;
////            sameLan = false;
////            sameMachine = false;
////            return;
////        }
////
////        // same location
////        String destIPAddress;
////        int destPort;
////        if (initiatorLanIPAddress.equals(responderLanIPAddress)) { // same location
////            if (initiatorPort == responderPort) { //  same machine
////                destIPAddress = initiatorLanIPAddress;
////                destPort = initiatorPort;
////                sameLocation = true;
////                sameLan = true;
////                sameMachine = true;
////            } else { //  same lan
////                destIPAddress = initiatorLanIPAddress;
////                destPort = initiatorPort;
////                sameLocation = true;
////                sameLan = true;
////                sameMachine = false;
////            }
////            destSocketAddress = new InetSocketAddress(destIPAddress, destPort);
////        }
////    }
//
//    public void setEndPoint(QEndPoint endPoint) {
//        this.endPoint = endPoint;
//    }
//
//    public void setChannelInfo(QChannelInfo channelInfo) {
//        this.channelInfo = channelInfo;
//    }
//
//    public void setFirstNetMsg(QNetMsg netMsg) {
//        this.firstNetMsg = netMsg;
//    }
//
//    public void setOwner(QPacketOwner channelOwner) {
//        this.owner = channelOwner;
//    }
//
//    public void sendNetMsg(QNetMsg netMsg) {
//        System.out.println("SEND NETMSG MISSING");
//    }
//
//    public void setStatus(int channelStatus) {
//        this.channelStatus = channelStatus;
//    }
//
////    public void setInitiatorSocketAddress(InetSocketAddress destSocketAddress) {
////        this.destSocketAddress = destSocketAddress;
////    }
//
//
//    private int reverseChannelMsgDirection(int role) {
//        if (role == HandshakeState.INITIATOR) role = HandshakeState.RESPONDER;
//        else role = HandshakeState.INITIATOR;
//        return role;
//    }
//
//
//    private void printMsg(String msg) {
//        System.out.println(msg);
////        log.info(msg);
//    }
//
//    private void badResult(String s) {
//        String msg = "BadResult -> " + s;
//        printMsg(msg);
////        packethandler.badResult();
////        throw new QNodeException("Channel Failed");
////        makeSafe(rt);
//    }
//
//    private void makeSafe() {
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} ///////// End Class /////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
////    /* Run the handshake until we run out of things to read or write */
////    while (ok) {
////        action = noise_handshakestate_get_action(handshake);
////        if (action == NOISE_ACTION_WRITE_MESSAGE) {
////            /* Write the next handshake message with a zero-length payload */
////            noise_buffer_set_output(mbuf, message + 2, sizeof(message) - 2);
////            err = noise_handshakestate_write_message(handshake, &mbuf, NULL);
////            if (err != NOISE_ERROR_NONE) {
////                noise_perror("write handshake", err);
////                ok = 0;
////                break;
////            }
////            message[0] = (uint8_t)(mbuf.size >> 8);
////            message[1] = (uint8_t)mbuf.size;
////            if (!echo_send(fd, message, mbuf.size + 2)) {
////                ok = 0;
////                break;
////            }
////        } else if (action == NOISE_ACTION_READ_MESSAGE) {
////            /* Read the next handshake message and discard the payload */
////            message_size = echo_recv(fd, message, sizeof(message));
////            if (!message_size) {
////                ok = 0;
////                break;
////            }
////            noise_buffer_set_input(mbuf, message + 2, message_size - 2);
////            err = noise_handshakestate_read_message(handshake, &mbuf, NULL);
////            if (err != NOISE_ERROR_NONE) {
////                noise_perror("read handshake", err);
////                ok = 0;
////                break;
////            }
////        } else {
////            /* Either the handshake has finished or it has failed */
////            break;
////        }
////    }
////
////    /* If the action is not "split", then the handshake has failed */
////    if (ok && noise_handshakestate_get_action(handshake) != NOISE_ACTION_SPLIT) {
////        fprintf(stderr, "protocol handshake failed\n");
////        ok = 0;
////    }
////
////    /* Split out the two CipherState objects for send and receive */
////    if (ok) {
////        err = noise_handshakestate_split(handshake, &send_cipher, &recv_cipher);
////        if (err != NOISE_ERROR_NONE) {
////            noise_perror("split to start data transfer", err);
////            ok = 0;
////        }
////    }
////
////    /* We no longer need the HandshakeState */
////    noise_handshakestate_free(handshake);
////    handshake = 0;
////
////    /* If we will be padding messages, we will need a random number generator */
////    if (ok && padding) {
////        err = noise_randstate_new(&rand);
////        if (err != NOISE_ERROR_NONE) {
////            noise_perror("random number generator", err);
////            ok = 0;
////        }
////    }
////
////    /* Tell the user that the handshake has been successful */
////    if (ok) {
////        printf("%s handshake complete.  Enter text to be echoed ...\n", protocol);
////    }
////
////    /* Read lines from stdin, send to the server, and wait for echoes */
////    max_line_len = sizeof(message) - 2 - noise_cipherstate_get_mac_length(send_cipher);
////    while (ok && fgets((char *)(message + 2), max_line_len, stdin)) {
////        /* Pad the message to a uniform size */
////        message_size = strlen((const char *)(message + 2));
////        if (padding) {
////            err = noise_randstate_pad
////                    (rand, message + 2, message_size, max_line_len,
////                            NOISE_PADDING_RANDOM);
////            if (err != NOISE_ERROR_NONE) {
////                noise_perror("pad", err);
////                ok = 0;
////                break;
////            }
////            message_size = max_line_len;
////        }
////
////        /* Encrypt the message and send it */
////        noise_buffer_set_inout
////                (mbuf, message + 2, message_size, sizeof(message) - 2);
////        err = noise_cipherstate_encrypt(send_cipher, &mbuf);
////        if (err != NOISE_ERROR_NONE) {
////            noise_perror("write", err);
////            ok = 0;
////            break;
////        }
////        message[0] = (uint8_t)(mbuf.size >> 8);
////        message[1] = (uint8_t)mbuf.size;
////        if (!echo_send(fd, message, mbuf.size + 2)) {
////            ok = 0;
////            break;
////        }
////
////        /* Wait for a message from the server */
////        message_size = echo_recv(fd, message, sizeof(message));
////        if (!message_size) {
////            fprintf(stderr, "Remote side terminated the connection\n");
////            ok = 0;
////            break;
////        }
////
////        /* Decrypt the incoming message */
////        noise_buffer_set_input(mbuf, message + 2, message_size - 2);
////        err = noise_cipherstate_decrypt(recv_cipher, &mbuf);
////        if (err != NOISE_ERROR_NONE) {
////            noise_perror("read", err);
////            ok = 0;
////            break;
////        }
////
////        /* Remove padding from the message if necessary */
////        if (padding) {
////            /* Find the first '\n' and strip everything after it */
////            const uint8_t *end = (const uint8_t *)
////            memchr(mbuf.data, '\n', mbuf.size);
////            if (end)
////                mbuf.size = end + 1 - mbuf.data;
////        }
////
////        /* Write the echo to standard output */
////        fputs("Received: ", stdout);
////        fwrite(mbuf.data, 1, mbuf.size, stdout);
////    }
////
////    /* Clean up and exit */
////    noise_cipherstate_free(send_cipher);
////    noise_cipherstate_free(recv_cipher);
////    noise_randstate_free(rand);
////    echo_close(fd);
////    return ok ? 0 : 1;
////}
////
////#include "echo-common.c"
//
//
////                    /* Write the next handshake message with a zero-length payload */
////
////                    QThreadUtils.showThreadName(getDisplayName() + " Channel writeFirstInitiatorMessage");
////                    byte[] payload = qobjs.objToBytes(channelInfo);
////                    int size = handshake.writeMessage(workBuffer, 0, payload, 0, payload.length);
////
////                    byte[] bytes = Arrays.copyOf(workBuffer, size);
////                    ByteBuffer bb = ByteBuffer.allocate(size + 12);
////                    bb.putInt(0, channelInfo.responderEndPointInfo.endPointIdx);
////                    bb.putInt(4, channelInfo.channelId);
////                    bb.putInt(8, channelInfo.role);
////                    bb.put(12, bytes);
////
////                    DatagramPacket packet = new DatagramPacket(bb.array(), 0, bb.array().length, destSocketAddress);
////                    QThreadUtils.showThreadName(getDisplayName() + " Channel writeFirstInitiatorMessage sendPacket");
////
////                    packetHandler.sendPacket(packet);
//////                    break;
////
////////            noise_buffer_set_output(mbuf, message + 2, sizeof(message) - 2);
////////            err = noise_handshakestate_write_message(handshake, &mbuf, NULL);
////////            if (err != NOISE_ERROR_NONE) {
////////                noise_perror("write handshake", err);
////////                ok = 0;
////////                break;
////////            }
////////            message[0] = (uint8_t)(mbuf.size >> 8);
////////            message[1] = (uint8_t)mbuf.size;
////////            if (!echo_send(fd, message, mbuf.size + 2)) {
////////                ok = 0;
////////                break;
//////
//////                            byte[] payload = null;
//////                            int payloadLength = 0;
//////                            if (firstNetMsg != null) {
//////                                payload = firstNetMsgBytes;
//////                                payloadLength = payload.length;
//////                            }
//////
//////                            int size = 0;
//////                            size = handshake.writeMessage(workBuffer, 0, payload, 0, payloadLength);
//////                            byte[] netMsgBytes = setHandshakeBytes(workBuffer, size);
//////                            reverseChannelMsgDirection(netMsgHandshake);
//////                            packetHandler.sendPacket(netMsgHandshake);
//////                            afterWrite();
//////                            break;
//////
////                continue;
//
//
////                    int length = receivedPacket.getLength(); // packetData length is 65k, we have to use packet.getLength
////                    ByteBuffer packetBB = ByteBuffer.wrap(packetData, 0, length);
////                    int endPointIdx = packetBB.getInt();
////                    int channelId = packetBB.getInt();
////                    int role = packetBB.getInt();
////                    byte[] msgBytes = new byte[packetBB.remaining()];
////                    packetBB.get(msgBytes, 0, msgBytes.length);
////
////                    int payloadSize = 0;
////                    payloadSize = handshake.readMessage(msgBytes, 0, msgBytes.length, workBuffer, 0);
////                    if (payloadSize > 0) {
//////            byte[] payloadBytes = new byte[payloadSize];
//////            ByteBuffer payloadBB = ByteBuffer.wrap(workBuffer,0, payloadSize);
//////            payloadBB.get(payloadBytes);
////                        Object obj = qobjs.objFromBytes(workBuffer);
////                        if (obj == null) return;
////                        if (obj instanceof QChannelInfo) {
////                            QChannelInfo reeivedChannelInfo = (QChannelInfo) obj;
////                            channelInfo.initiatorEndPointInfo = reeivedChannelInfo.initiatorEndPointInfo;
////                        } else return;
////                    }
////
////                    byte[] payload = null;
////                    int size = handshake.writeMessage(workBuffer, 0, payload, 0, 0);
////
////                    byte[] bytes = Arrays.copyOf(workBuffer, size);
////                    ByteBuffer bb = ByteBuffer.allocate(size + 12);
////                    bb.putInt(0, channelInfo.initiatorEndPointInfo.endPointIdx);
////                    bb.putInt(4, channelInfo.channelId);
////                    bb.putInt(8, channelInfo.role);
////                    bb.put(12, bytes);
////
////                    DatagramPacket packet = new DatagramPacket(bb.array(), 0, bb.array().length, destSocketAddress);
////                    packetHandler.sendPacket(packet);
//////                    break;
////                continue;
//
//
////        QThreadUtils.showThreadName(getDisplayName() + " Channel Receive Packet");
////        byte[] packetData = packet.getData();
////        int length = packet.getLength(); // packetData length is 65k, we have to use packet.getLength
////        ByteBuffer byteBuffer = ByteBuffer.wrap(packetData, 0, length);
////        int channelId = byteBuffer.getInt(4);
////        int role = reverseChannelMsgDirection(byteBuffer.getInt(8));
////        byte[] msgBytes = new byte[length - 12];
////        byteBuffer.get(12, msgBytes);
//
//
////                byte[] payloadBytes = new byte[payloadSize];
////                ByteBuffer bb1 = Unpooled.wrappedBuffer(workBuffer);
////                bb1.readBytes(payloadBytes);
//
////                if (channelInfo == null && role == HandshakeState.RESPONDER) {
////                    // this is new server channel
////                    this.channelInfo = (QChannelInfo) qobjs.objFromBytes(payloadBytes);
////
////                    displayName = "SERVER";
////                    role = HandshakeState.RESPONDER);
////                    setNew(true);
////                    endPoint = system.getEndPoint(channelInfo.responderEndPointInfo.endPointIdx);
////                    startHandshake();
////                }
//
////                    firstNetMsgBytes = payloadBytes;
//
//
////            int action = handshake.getAction();
////            switch (action) {
////                case HandshakeState.WRITE_MESSAGE:
////                    byte[] payload = null;
////                    int payloadLength = 0;
////                    if (firstNetMsg != null) {
////                        payload = firstNetMsgBytes;
////                        payloadLength = payload.length;
////                    }
////
////                    int size = 0;
////                    size = handshake.writeMessage(workBuffer, 0, payload, 0, payloadLength);
////                    byte[] netMsgBytes = setHandshakeBytes(workBuffer, size);
////                    reverseChannelMsgDirection(netMsgHandshake);
////                    packetHandler.sendPacket(netMsgHandshake);
////                    afterWrite();
////                    break;
////
////                case HandshakeState.SPLIT: // server split
////                    cipherPair = handshake.split();
////                    if (handshake.getAction() == HandshakeState.COMPLETE) {
//////                    printMsg(displayName + " -> Handshake COMPLETE");
////                        if (firstNetMsgBytes != null) {
////                            netMsgHandshake.netMsgBytes = firstNetMsgBytes;
////                            netMsgHandshake.handleRequest(netMsgHandshake);
////                        }
////
////                    } else {
////                        badResult(displayName + " Noise afterRead HandshakeState.FAILED");
////                        return;
////                    }
////                    break;
////
////                case HandshakeState.FAILED:
////                    printMsg(displayName + "After Read Handshake FAILED -> pktId : ");
////                    badResult("afterRead -> handshake FAILED");
////            }
////
////
//
//
////    public void receivePacket(DatagramPacket packet) {
////        if (cipherPair != null) { // handshake
//////            byte[] netMsgPlainBytes = decryptBytes(msgBytes);
//////            QNetMsg netMsg = (QNetMsg) qobjs.objFromBytes(netMsgPlainBytes);
//////            if (role == HandshakeState.INITIATOR) {
//////                netMsg.handleRequest(netMsg);
//////            } else {
//////                netMsg.handleResponse(netMsg);
//////            }
//////            return;
////
////        } else {
////
////        }
////    }
//
////    public void receivePacket(DatagramPacket packet) throws ShortBufferException, BadPaddingException {
////        QThreadUtils.showThreadName(getDisplayName() + " Channel Receive Packet");
////        byte[] packetData = packet.getData();
////        int length = packet.getLength(); // packetData length is 65k, we have to use packet.getLength
////        ByteBuffer byteBuffer = ByteBuffer.wrap(packetData, 0, length);
////        int channelId = byteBuffer.getInt(4);
////        int role = reverseChannelMsgDirection(byteBuffer.getInt(8));
////        byte[] msgBytes = new byte[length - 12];
////        byteBuffer.get(12, msgBytes);
////
////        int payloadSize = 0;
////        payloadSize = handshake.readMessage(msgBytes, 0, msgBytes.length, workBuffer, 0);
////        if (payloadSize > 0) {
////            byte[] payloadBytes = new byte[payloadSize];
////            ByteBuf bb1 = Unpooled.wrappedBuffer(workBuffer);
////            bb1.readBytes(payloadBytes);
////
////            if (channelInfo == null && role == HandshakeState.RESPONDER) {
////                // this is new server channel
////                this.channelInfo = (QChannelInfo) qobjs.objFromBytes(payloadBytes);
////
////                displayName = "SERVER";
////                role = HandshakeState.RESPONDER);
////                setNew(true);
////                endPoint = system.getEndPoint(channelInfo.responderEndPointInfo.endPointIdx);
////                startHandshake();
////            }
////
//////                    firstNetMsgBytes = payloadBytes;
////        }
////
////
////    }
//
////        if (cipherPair != null) {
////            byte[] netMsgPlainBytes = decryptBytes(msgBytes);
////            QNetMsg netMsg = (QNetMsg) qobjs.objFromBytes(netMsgPlainBytes);
////            if (role == HandshakeState.INITIATOR) {
////                netMsg.handleRequest(netMsg);
////            } else {
////                netMsg.handleResponse(netMsg);
////            }
////            return;
////        }
////
//
//
////        if (handshake.getAction() == HandshakeState.READ_MESSAGE) {
////            int payloadSize = 0;
////            payloadSize = handshake.readMessage(msgBytes, 0, msgBytes.length, workBuffer, 0);
////            if (payloadSize > 0) {
////                byte[] payloadBytes = new byte[payloadSize];
////                ByteBuf bb1 = Unpooled.wrappedBuffer(workBuffer);
////                bb1.readBytes(payloadBytes);
////
////                if (channelInfo == null && role == HandshakeState.RESPONDER) {
////                    // this is new server channel
////                    this.channelInfo = (QChannelInfo) qobjs.objFromBytes(payloadBytes);
////
////                    displayName = "SERVER";
////                    role = HandshakeState.RESPONDER);
////                    setNew(true);
////                    endPoint = system.getEndPoint(channelInfo.responderEndPointInfo.endPointIdx);
////                    startHandshake();
////                }
////
//////                    firstNetMsgBytes = payloadBytes;
////            }
////
//////            afterRead();
////
////            int action = handshake.getAction();
////            switch (action) {
////                case HandshakeState.WRITE_MESSAGE:
////                    byte[] payload = null;
////                    int payloadLength = 0;
////                    if (firstNetMsg != null) {
////                        payload = firstNetMsgBytes;
////                        payloadLength = payload.length;
////                    }
////
////                    int size = 0;
////                    size = handshake.writeMessage(workBuffer, 0, payload, 0, payloadLength);
////                    byte[] netMsgBytes = setHandshakeBytes(workBuffer, size);
////                    reverseChannelMsgDirection(netMsgHandshake);
////                    packetHandler.sendPacket(netMsgHandshake);
////                    afterWrite();
////                    break;
////
////                case HandshakeState.SPLIT: // server split
////                    cipherPair = handshake.split();
////                    if (handshake.getAction() == HandshakeState.COMPLETE) {
//////                    printMsg(displayName + " -> Handshake COMPLETE");
////                        if (firstNetMsgBytes != null) {
////                            netMsgHandshake.netMsgBytes = firstNetMsgBytes;
////                            netMsgHandshake.handleRequest(netMsgHandshake);
////                        }
////
////                    } else {
////                        badResult(displayName + " Noise afterRead HandshakeState.FAILED");
////                        return;
////                    }
////                    break;
////
////                case HandshakeState.FAILED:
////                    printMsg(displayName + "After Read Handshake FAILED -> pktId : ");
////                    badResult("afterRead -> handshake FAILED");
////            }
////
////
////        }
//
//
////        if (channelInfo.role  == HandshakeState.INITIATOR) {
////            channelStatus = QSysConstants.CHANNEL_STATUS_INITIATOR_HANDSHAKE_FIRST;
////            writeFirstInitiatorMessage();
////        } else {
////            channelStatus = QSysConstants.CHANNEL_STATUS_RESPONDER_HANDSHAKE_FIRST;
////            readFirstResponderMessage(packetBB);
////        }
//
//
///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// Responder Meassages  ////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    private void readFirstResponderMessage() throws InterruptedException, ShortBufferException, BadPaddingException, IOException {
////        ByteBuffer packetBB = channelReceivePacketBBQueue.take();
////        int endPointIdx = packetBB.getInt();
////        int channelId = packetBB.getInt();
////        int role = packetBB.getInt();
////        byte[] msgBytes = new byte[packetBB.remaining()];
////        packetBB.get(12, msgBytes, 0, msgBytes.length);
////
////        int payloadSize = 0;
////        payloadSize = handshake.readMessage(msgBytes, 0, msgBytes.length, workBuffer, 0);
////        if (payloadSize > 0) {
////            byte[] payloadBytes = new byte[payloadSize];
////            ByteBuffer payloadBB = ByteBuffer.wrap(workBuffer);
////            payloadBB.get(payloadBytes);
////            QChannelInfo reeivedChannelInfo = (QChannelInfo) qobjs.objFromBytes(payloadBytes);
////            channelInfo.initiatorEndPointInfo = reeivedChannelInfo.initiatorEndPointInfo;
////        }
////
////        byte[] payload = null;
////        int size = handshake.writeMessage(workBuffer, 0, payload, 0, 0);
////
////        byte[] bytes = Arrays.copyOf(workBuffer, size);
////        ByteBuffer bb = ByteBuffer.allocate(size + 12);
////        bb.putInt(0, channelInfo.initiatorEndPointInfo.endPointIdx);
////        bb.putInt(4, channelInfo.channelId);
////        bb.putInt(8, channelInfo.role);
////        bb.put(12, bytes);
////
////        DatagramPacket packet = new DatagramPacket(bb.array(), 0, bb.array().length, destSocketAddress);
////        packetHandler.sendPacket(packet);
////    }
//
//
///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// Do Action ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    private void doAction() throws ShortBufferException, IOException {
////
////        int action = handshake.getAction();
////        if (action == HandshakeState.WRITE_MESSAGE) {
////            byte[] payload = qobjs.objToBytes(channelInfo);
////            int size = handshake.writeMessage(workBuffer, 0, payload, 0, payload.length);
////            byte[] bytes = setHandshakeBytes(workBuffer, size, channelInfo.responderEndPointInfo.endPointIdx);
////            ByteBuffer bb = ByteBuffer.wrap(bytes, 12, bytes.length);
////            bb.putInt(0, channelInfo.responderEndPointInfo.endPointIdx);
////            bb.putInt(getChannelId());
////            bb.putInt(channelInfo.role);
////            DatagramPacket packet = new DatagramPacket(bb.array(), 0, bb.array().length, destSocketAddress);
////            packetHandler.sendPacket(packet);
////        } else if (action == HandshakeState.READ_MESSAGE) {
////
////
////            switch (action) {
////                case HandshakeState.WRITE_MESSAGE:
////                    byte[] payload = null;
////                    int payloadLength = 0;
//////                if (firstNetMsgBytes != null) {
//////                    payload = firstNetMsgBytes;
//////                    payloadLength = payload.length;
//////                }
////
////                    int size = 0;
////                    size = handshake.writeMessage(workBuffer, 0, payload, 0, payloadLength);
////                    netMsgHandshake.netMsgBytes = setHandshakeBytes(workBuffer, size);
////                    reverseChannelMsgDirection(netMsgHandshake);
////                    packetHandler.sendPacket(netMsgHandshake);
////                    afterWrite();
////                    break;
////
////                case HandshakeState.SPLIT: // server split
////                    cipherPair = handshake.split();
////                    if (handshake.getAction() == HandshakeState.COMPLETE) {
//////                    printMsg(displayName + " -> Handshake COMPLETE");
////                        if (firstNetMsgBytes != null) {
////                            netMsgHandshake.netMsgBytes = firstNetMsgBytes;
////                            netMsgHandshake.handleRequest(netMsgHandshake);
////                        }
////
////                    } else {
////                        badResult(displayName + " Noise afterRead HandshakeState.FAILED");
////                        return;
////                    }
////                    break;
////
////                case HandshakeState.FAILED:
////                    printMsg(displayName + "After Read Handshake FAILED -> pktId : ");
////                    badResult("afterRead -> handshake FAILED");
////            }
////
////        }
////    }
////
//
//
////// can't happen, channel must exist for client first read
//////        if (channelInfo == null && role == HandshakeState.INITIATOR) {
////
////
////        // need new server Channel
////        QChannelInfo channelInfo = new QChannelInfo();
////        channelInfo.channelId = channelId;
////        channelInfo.role = HandshakeState.RESPONDER;
////        channelInfo.initiatorEndPointInfo;
////        channelInfo.responderEndPointInfo;
////
////        channelInfo = new QChannelInfo();
////
////        setDisplayName("SERVER");
////        channel.setRole(HandshakeState.RESPONDER);
////        channel.setNew(true);
////        channel.setEndPoint(this);
////
////        channel.setSenderSocketAddress(senderSocketAddress);
////        channel.setMsgBB(msgBB);
////        return channel;
////
////
////        if (role == HandshakeState.INITIATOR) {
////
////
////        }
////
////
////    } else if(role==HandshakeState.RESPONDER)
////
////    {
////
////    }else if(role==HandshakeState.COMPLETE)
////
////    {
////
////    }else throw(new
////
////    RuntimeException());
////
////
////    }
//
////
//
//
////
////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// Handshake Request & Response //////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
////
////public void handshakeRequest(byte[]msgBytes)throws ShortBufferException,BadPaddingException{
//////        if (isNew) {
//////            startHandshake((QNetMsgHandshake) netMsgHandshake);
//////            isNew = false;
//////        }
////        if(handshake.getAction()==HandshakeState.READ_MESSAGE){
////        int payloadSize=0;
////        payloadSize=handshake.readMessage(msgBytes,0,msgBytes.length,workBuffer,0);
////        if(payloadSize>0){
////        byte[]payloadBytes=new byte[payloadSize];
////        ByteBuf bb1=Unpooled.wrappedBuffer(workBuffer);
////        bb1.readBytes(payloadBytes);
//////                    firstNetMsgBytes = payloadBytes;
////        }
////        afterRead();
////        }
////        }
////
////
////public void handshakeResponse()throws ShortBufferException{
////        if(handshake.getAction()==HandshakeState.READ_MESSAGE){
////        int payloadSize=0;
////        payloadSize=handshake.readMessage(netMsgHandshake.netMsgBytes,0,netMsgHandshake.netMsgBytes.length,workBuffer,0);
////        afterRead();
////        }
////        }
////
////
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// Send NetMsg ChannelMsg ////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
//////
////////    @Override
////////    public void doRoundTripRequest(QNetMsg netMsg) {
////////        system.qobjs.objToBytes(netMsg);
////////        packetHandler.sendPacket();
////////    }
//////
////////    @Override
////////    public void sendHandshakeBytes(byte[] handshakeBytes) {
//////////        reverseChannelMsgDirection(netMsg);
//////////        byte[] netMsgPlainBytes = system.qobjs.objToBytes(channelPacket.netMsg);
//////////        printPrettyMsg("Response NetMsg Bytes before encrypt", netMsgPlainBytes);
//////////        byte[] netMsgEncryptedBytes = encryptBytes(netMsgPlainBytes);
//////////        printPrettyMsg("Response ChannelMsg Bytes after encrypt", netMsgEncryotedBytes);
//////////        channelPacket.bytes = netMsgEncryptedBytes;
////////        netMsg.role = role;
//////////        sendPacket(currentChannelMsg);
////////        packetHandler.sendPacket(netMsg);
////////    }
//////
//////
////public void sendNetMsg(QNetMsg netMsg){
//////        reverseChannelMsgDirection(netMsg);
////////        byte[] netMsgPlainBytes = system.qobjs.objToBytes(channelPacket.netMsg);
////////        printPrettyMsg("Response NetMsg Bytes before encrypt", netMsgPlainBytes);
////////        byte[] netMsgEncryptedBytes = encryptBytes(netMsgPlainBytes);
////////        printPrettyMsg("Response ChannelMsg Bytes after encrypt", netMsgEncryotedBytes);
////////        channelPacket.bytes = netMsgEncryptedBytes;
//////        netMsg.role = role;
////////        sendPacket(currentChannelMsg);
//////        packetHandler.sendPacket(netMsg);
////        }
////
////
////public void handleReceivedMsg(InetSocketAddress senderSocketAddress,ByteBuf bb){
////        int endPointIdx=bb.getInt(bb.array().length-4);
////
////        byte[]msgBytes=new byte[bb.array().length-8];
////
////        if(endPointIdx< 0){ //handshake
////        receiveHandshakeMsg(msgBytes);
////        }else{
////        receiveTransportMag(msgBytes);
////        }
////        }
////
////public void receiveHandshakeMsg(byte[]msgBytes){
////
////        }
////
////public void receiveTransportMag(byte[]msgBytes){
////
////        }
////
////public void setSenderSocketAddress(InetSocketAddress senderSocketAddress){
////        this.sendeeSocketAddress=senderSocketAddress;
////        }
////
////public void setMsgBB(ByteBuf msgBB){
////        this.msgBB=msgBB;
////        }
////
////
////
////    @Override
////    public void receiveChannelPacket(QNetMsg channelMsg) {
////
////    }
////
//////    @Override
//////    public void receiveCompleteNetMsg(QNetMsg completeNetMsg) {
//////
//////    }
////
///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Send Channel Packet ///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
////
////    @Override
////    public void sendNetMsg(QNetMsg netMsg) {
////////        system.getExecutor().submit(() -> {
//////        netMsg.senderEPId = ep
//////
////////        });
//////        System.out.println("");
////    }
////
////    @Override
////    public void setInitiatorChannelInfo(QInitiatorChannelInfo info) {
////        this.initiatorChannelInfo = info;
////    }
////
////    @Override
////    public void setResponderChannelInfo(QResponderChannelInfo info) {
////        this.responderChannelInfo = info;
////    }
////
////    @Override
////    public int getInitiatorEndPointIdx() {
////        return initiatorEndPointIdx;
////    }
////
////    @Override
////    public int getResponderEndPointIdx() {
////        return responderEndPointIdx;
////    }
////
////    @Override
////    public void setChannelId(int channelId) {
////        this.channelId = channelId;
////    }
////
////    @Override
////    public int getChannelId() {
////        return 0;
////    }
////
////    @Override
////    public void setEndPoint(QEndPoint EndPoint) {
////
////    }
////
////    @Override
////    public void setInitiatorEndPointInfo(QEndPointInfo initiatorEndPointInfo) {
////        this.initiatorEndPointInfo = initiatorEndPointInfo;
////    }
////
////    @Override
////    public void setResponderEndPointInfo(QEndPointInfo responderEndPointInfo) {
////        this.responderEndPointInfo = responderEndPointInfo;
////    }
////
////    @Override
////    public void setInitiatorEndPointIdx() {
////        this.initiatorEndPointIdx = initiatorEndPointInfo.getEndPointIdx();
////    }
////
////
////    @Override
////    public void setResponderEndPointIdx() {
////        this.responderEndPointIdx = responderEndPointInfo.getEndPointIdx();
////
////    }
////
////    @Override
////    public void setFirstNetMsg(QNetMsg netMsg) {
////
////    }
////
////    @Override
////    public void setReceivedPacketSocketAddress(SocketAddress receivedPacketSocketAddress) {
////
////    }
////
//////    public void sendChannelMsg(QChannelMsg channelMsg) {
//////        this.currentChannelMsg = channelMsg;
//////        sendChannelMsg();
//////    }
////
//////    //    @Override
//////    public void sendHandshakePacket(QChannelMsg channelMsg) {
////////        byte[] channelMsgBytes = serialization.objToBytes(currentChannelMsg);
//////        packetHandler.sendPacket(channelMsg);
////////        if (doChannelMsgTimeouts) {
////////            node.getChannelMsgTimer().schedule(new QChannelTimerTask(), CHANNEL_MSG_TIMEOUT_2);
////////        }
//////        if (role == HandshakeState.RESPONDER && (!currentMsg.keepAlive) && cipherPair != null) {
////////            node.removeServerChannel(this);
//////            System.out.println("");
//////        }
//////        currentMsg = null;
//////    }
////
////    @Override
////    public void receiveChannelMsg(QNetMsg channelMsg) {
////        netMsgHandshake = channelMsg;
//////        if (channelMsgTimerTask != null) {
//////            channelMsgTimerTask.cancel();
//////        }
////        netMsgHandshake.channel = this;
////        if (cipherPair == null) {
////            if (netMsgHandshake.role == HandshakeState.INITIATOR) {
////                handshakeRequest();
////            } else {
////                handshakeResponse();
////            }
////
////        } else {
////            byte[] netMsgEncryptedBytes = netMsgHandshake.netMsgBytes;
////            byte[] netMsgPlainBytes = decryptBytes(netMsgEncryptedBytes);
////            netMsgHandshake = (QNetMsg) qobjs.objFromBytes(netMsgPlainBytes);
////            if (netMsgHandshake.role == HandshakeState.INITIATOR) {
////                netMsgHandshake.handleRequest(netMsgHandshake);
////            } else {
////                netMsgHandshake.handleResponse(netMsgHandshake);
////            }
////            netMsgHandshake = null;
////        }
////    }
////
//////    class QChannelTimerTask extends TimerTask {
//////        public void run() {
//////            System.out.println("ChannelMsg timed out");
//////        }
//////    }
////
////
///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////// Utils /////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
////
//////    @Override
//////    public QEndPointInfo getResponderEndPointInfo() {
//////        return responderEndPointInfo;
//////    }
////
//////    @Override
//////    public void setResponderEndPointAddress(QEndPointInfo reponderEndPointAddress) {
//////        this.responderEndPointAddress = reponderEndPointAddress;
//////    }
////
////    @Override
////    public void addNetMsg(QNetMsg netMsg) {
////        queue.add(netMsg);
////    }
////
////    @Override
////    public void setPrivateKeyClone(byte[] noisePrivateKeyClone) {
////        privateKeyClone = noisePrivateKeyClone;
////    }
////
////    @Override
////    public void setPublicKeyClone(byte[] noisePublicKeyClone) {
////        publicKeyClone = noisePublicKeyClone;
////    }
////
////    @Override
////    public void setFirstMsgBytes(byte[] bytes) {
////        this.firstNetMsgBytes = bytes;
////    }
////
////
//////    @Override
//////    public void setFirstMsg(QNetMsg netMsg) {
//////        this.firstNetMsg = netMsg;
//////    }
////
//////    @Override
//////    public QEndPointInfo getInitiatorEndPointInfo() {
//////        return initiatorEndPointInfo;
//////    }
////
////    //    @Override
////    public int getRole() {
////        return role;
////    }
////
//////    @Override
//////    public QEndPointInfo getDestEndPoint() {
//////        return destEndPoint;
//////    }
////
////
//////    @Override
//////    public void setSrcUUID(UUID uuid) {
//////        this.srcUUID = uuid;
//////    }
//////
//////    public void setDestUUID(UUID uuid) {
//////        this.destUUID = uuid;
//////    }
////
////    @Override
////    public boolean isNew() {
////        return isNew;
////    }
////
//////    @Override
//////    public void setRemoteEPId(QEPId remoteEPId) {
//////
//////    }
////
////
//////    @Override
//////    public boolean isFree() {
//////        return !busy;
//////    }
////
////
//////    public void setInitiatorEndPointInfo(QEndPointInfo initiatorEndPointInfo) {
//////        this.initiatorEndPointInfo = initiatorEndPointInfo;
//////    }
////
////    @Override
////    public void setResponderSocketAddress() {
//////        responderEndPointAddresstInfo = channelInfo.remoteEndPointInfo;
////        this.socketAddress = initiatorChannelInfo.remoteEndPointInfo.getSocketAddress();
////    }
////
////
//////    public QNode getNode() {
//////        return node;
//////    }
//
//
////
////    public void close() {
//////        sendClosePacket();
//////        makeSafe(null);
////    }
////
////
//////    public void setChannelId(int channelId) {
//////        this.channelId = channelId;
//////    }
//////
//////
//////    public int getChannelId() {
//////        return channelId;
//////    }
////
//////    @Override
//////    public void setChannelNumber(int channelId) {
//////        this.channelId = channelId;
//////    }
////
////    public void setRole(int role) {
////        this.role = role;
////    }
////
////    @Override
////    public void setDisplayName(String name) {
////        this.displayName = name;
////    }
////
////
////    public void setIsNew(boolean isNew) {
////        this.isNew = isNew;
////    }
//
//
////    private void makeSafe(QNetMsg netMsg) {
////    }
////
////    private void printPrettyMsg(String msg, byte[] bytes) {
////        printMsg(displayName + msg + " -> " + QStringUtils.prettyArrayBytes(bytes));
////    }
////
//////    public int getSrcIdx() {
//////        return srcIdx;
//////    }
////
////
//////    public void setSrcIdx(int srcIdx) {
//////        this.srcIdx = srcIdx;
//////    }
//////
//////    public int getDestIdx() {
//////        return destIdx;
//////    }
//////
//////
//////    public void setDestIdx(int destIdx) {
//////        this.destIdx = destIdx;
//////    }
////
//
//
//
//
//
//
//
//
