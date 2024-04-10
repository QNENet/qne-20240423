// package com.qnenet.qne.network.channel;

// import com.qnenet.qne.qneapi.classes.QChannelMsg;
// import com.qnenet.qne.qneapi.classes.QNetAddress;
// import com.qnenet.qne.qneapi.intf.*;
// import com.qnenet.qne.qneutils.QStringUtils;
// import com.qnenet.qne.qnoiseprotocol.api.QNoiseProtocol;
// import com.qnenet.qne.qnoiseprotocol.api.p1.CipherState;
// import com.qnenet.qne.qnoiseprotocol.api.p1.CipherStatePair;
// import com.qnenet.qne.qnoiseprotocol.api.p1.Curve25519DHState;
// import com.qnenet.qne.qnoiseprotocol.api.p1.HandshakeState;
// import com.qnenet.qne.qserialization.api.QSerialization;
// import com.qnenet.qne.qsystem.api.QSystem;
// import io.netty.buffer.ByteBuf;
// import io.netty.buffer.Unpooled;
// import org.osgi.service.component.annotations.*;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import javax.crypto.BadPaddingException;
// import javax.crypto.ShortBufferException;
// import java.security.NoSuchAlgorithmException;

// ///////////////////////////////////////////////////////////////////////////////////////////////////
// ///////// Class ///////////////////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////////////////

// public class QChannelImpl implements QChannel {

//     private static final long CHANNEL_MSG_TIMEOUT_1 = 2000;
//     private static final long CHANNEL_MSG_TIMEOUT_2 = 4000;
//     private static final long CHANNEL_MSG_TIMEOUT_3 = 8000;
//     Logger log = LoggerFactory.getLogger(QChannelImpl.class);

//     @Reference
//     QSystem system;

//     @Reference
//     QPacketHandler packetHandler;

//     @Reference
//     QNoiseProtocol noise;

//     @Reference
//     QSerialization serialization;

//     QNode node;

//     private HandshakeState handshake;
//     private static final int HANDSHAKE_BUFFER_SIZE = 65535;
//     private byte[] workBuffer = new byte[HANDSHAKE_BUFFER_SIZE];

//     private CipherStatePair cipherPair;

//     private String displayName;
//     private QNetMsg firstNetMsg;
//     private String connectionName;
//     private String channelName;
//     private int channelId;
//     private int role;
//     private boolean isNew;
//     private QNetAddress destNetAddress;
//     private boolean busy;
// //    private UUID srcUUID;
// //    private UUID destUUID;
// //    private QChannelTimerTask channelMsgTimerTask;
//     private QChannelMsg currentChannelMsg;
//     private int srcIdx;
//     private int destIdx;
//     private byte[] privateKeyClone;
//     private byte[] publicKeyClone;
//     private boolean isMemberChannel;

// //    private QNode node;
// //    private boolean doChannelMsgTimeouts;
// //    private QChannelTimerTask channelMsgTimeoutTask;

// ///////////////////////////////////////////////////////////////////////////////////////////////////
// /////// Activator /////////////////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////////////////

//     @Activate
//     public void activate() {
// //        channelMsgTimeoutTask = new QChannelTimerTask();
// //        doChannelMsgTimeouts = system.doChannelMsgTimeouts();
//         log.info("Hello from -> " + getClass().getSimpleName());
//     }

//     @Deactivate
//     public void deactivate() {
//         log.info("Goodbye from -> " + getClass().getSimpleName());
//     }


// ///////////////////////////////////////////////////////////////////////////////////////////////////
// ///////// Start Handshake /////////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////////////////

//     @Override
//     public void startHandshake(QChannelMsg channelMsg) {
//         currentChannelMsg = channelMsg;
//         if(channelMsg.destIdx == 0) {
//             isMemberChannel = false;
//             privateKeyClone = node.getNoisePrivateKeyClone();
//             publicKeyClone = node.getNoisePublicKeyClone();
//         } else {
//             isMemberChannel = true;
//             QMember member = node.getMember(destIdx);
//         }
//         try {
//             handshake = noise.createHandshakeState("Noise_XX_25519_ChaChaPoly_BLAKE2b", role);
//             Curve25519DHState staticKeypair = new Curve25519DHState();
// //            byte[] privateKeyClone = node.getNoisePrivateKeyClone();
// //            byte[] publicKeyClone = node.getNoisePublicKeyClone();
//             staticKeypair.setKeyPair(privateKeyClone, publicKeyClone);
//             handshake.setLocalKeyPair(staticKeypair);
//             handshake.start();
//             int action = handshake.getAction();
//             if (handshake.getAction() == HandshakeState.WRITE_MESSAGE) {
//                 int size = handshake.writeMessage(workBuffer, 0, null, 0, 0);
//                 channelMsg.bytes = workBufferBytes(workBuffer, size);
//                 sendChannelMsg(currentChannelMsg);
//             }
//         } catch (NoSuchAlgorithmException e) {
//             badResult("NoSuchAlgorithmException -> " + e.getMessage());
// //            throw new RuntimeException(e);
//         } catch (ShortBufferException e) {
//             badResult("ShortBufferException -> " + e.getMessage());
// //            throw new RuntimeException(e);
//         }
//     }

//     private byte[] workBufferBytes(byte[] workBuffer, int size) {
//         ByteBuf bb = Unpooled.buffer(size);
//         bb.writeBytes(workBuffer, 0, size);
//         return bb.array();
//     }

// ///////////////////////////////////////////////////////////////////////////////////////////////////
// /////// Handshake Request & Response ////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////////////////

//     public void handshakeRequest() {
//         if (isNew) {
//             startHandshake(currentChannelMsg);
//             isNew = false;
//         }
//         if (handshake.getAction() == HandshakeState.READ_MESSAGE) {
//             int payloadSize = 0;
//             try {
//                 payloadSize = handshake.readMessage(currentChannelMsg.bytes, 0, currentChannelMsg.bytes.length, workBuffer, 0);
//                 if (payloadSize > 0) {
//                     byte[] payloadBytes = new byte[payloadSize];
//                     ByteBuf bb1 = Unpooled.wrappedBuffer(workBuffer);
//                     bb1.readBytes(payloadBytes);
//                     firstNetMsg = (QNetMsg) serialization.objFromBytes(payloadBytes);
//                 }
//                 afterRead();
//             } catch (ShortBufferException e) {
//                 badResult("ShortBufferException -> " + e.getMessage());
// //               throw new RuntimeException(e);
//             } catch (BadPaddingException e) {
//                 badResult("BadPaddingException -> " + e.getMessage());
// //                throw new RuntimeException(e);
//             }
//         }
//     }


//     public void handshakeResponse() {
//         if (handshake.getAction() == HandshakeState.READ_MESSAGE) {
//             int payloadSize = 0;
//             try {
//                 payloadSize = handshake.readMessage(currentChannelMsg.bytes, 0, currentChannelMsg.bytes.length, workBuffer, 0);
//             } catch (ShortBufferException e) {
//                 badResult("ShortBufferException -> " + e.getMessage());
// //                throw new RuntimeException(e);
//             } catch (BadPaddingException e) {
//                 badResult("BadPaddingException -> " + e.getMessage());
//                 // throw new RuntimeException(e);
//             }
//             afterRead();
//         }
//     }


// ///////////////////////////////////////////////////////////////////////////////////////////////////
// /////////// After Read ////////////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////////////////

//     private void afterRead() {
//         int action = handshake.getAction();
//         switch (action) {
//             case HandshakeState.WRITE_MESSAGE:
//                 byte[] payload = null;
//                 int payloadLength = 0;
//                 if (firstNetMsg != null) {
//                     payload = serialization.objToBytes(firstNetMsg);
//                     payloadLength = payload.length;
//                 }

//                 int size = 0;
//                 try {
//                     size = handshake.writeMessage(workBuffer, 0, payload, 0, payloadLength);
//                 } catch (ShortBufferException e) {
//                     badResult("ShortBufferException  -> " + e.getMessage());
//                 //throw new RuntimeException(e);
//                 }
//                 currentChannelMsg.bytes = workBufferBytes(workBuffer, size);
//                 reverseChannelMsgDirection(currentChannelMsg);
//                 sendChannelMsg(currentChannelMsg);
//                 afterWrite();
//                 break;

//             case HandshakeState.SPLIT: // server split
//                 cipherPair = handshake.split();
//                 if (handshake.getAction() == HandshakeState.COMPLETE) {
// //                    printMsg(displayName + " -> Handshake COMPLETE");
//                     if (firstNetMsg != null) {
//                         currentChannelMsg.netMsg = firstNetMsg;
//                         currentChannelMsg.netMsg.handleRequest(currentChannelMsg);
//                     }

//                 } else {
//                     badResult(displayName + " Noise afterRead HandshakeState.FAILED");
//                     return;
//                 }
//                 break;

//             case HandshakeState.FAILED:
//                 printMsg(displayName + "After Read Handshake FAILED -> pktId : ");
//                 badResult("afterRead -> handshake FAILED");
//         }
//     }

// ///////////////////////////////////////////////////////////////////////////////////////////////////
// /////////// After Write ///////////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////////////////

//     private void afterWrite() {
//         switch (handshake.getAction()) {
//             case HandshakeState.SPLIT: // client split
// //                printMsg(displayName + "After Write Handshake SPLIT -> pktId : "); // + handshakePktId);
//                 cipherPair = handshake.split();
//                 if (handshake.getAction() == HandshakeState.COMPLETE) {
// //                    printMsg(displayName + " -> Handshake COMPLETE");
//                     return;
//                 }
//                 break;
//             case HandshakeState.FAILED:
//                 printMsg(displayName + "After Write Handshake FAILED -> pktId : "); // + handshakePktId);
// //                makeSafe(rt);

//             case HandshakeState.READ_MESSAGE:
//                 break;
//         }
//     }

// ///////////////////////////////////////////////////////////////////////////////////////////////////
// /////////// Encrypt & Decrypt /////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////////////////

//     @Override
//     public byte[] encryptBytes(byte[] bytes) {
//         CipherState cipherStateSender = cipherPair.getSender();
//         int encryptedSize = 0;
//         try {
//             encryptedSize = cipherStateSender.encryptWithAd(null, bytes, 0, workBuffer, 0, bytes.length);
//         } catch (ShortBufferException e) {
//             badResult("ShortBufferException  -> " + e.getMessage());
//             //throw new RuntimeException(e);
//         }
//         ByteBuf bb1 = Unpooled.wrappedBuffer(workBuffer);
//         byte[] encryptedBytes = new byte[encryptedSize];
//         bb1.readBytes(encryptedBytes);
//         return encryptedBytes;
//     }


//     @Override
//     public byte[] decryptBytes(byte[] bytes) {
//         CipherState cipherStateReceiver = cipherPair.getReceiver();
//         int decryptedSize = 0;
//         try {
//             decryptedSize = cipherStateReceiver.decryptWithAd(null, bytes, 0, workBuffer, 0, bytes.length);
//         } catch (ShortBufferException e) {
//             badResult("ShortBufferException  -> " + e.getMessage());
//             //throw new RuntimeException(e);
//         } catch (BadPaddingException e) {
//             badResult("BadPaddingException -> " + e.getMessage());
// //            throw new RuntimeException(e);
//         }
//         ByteBuf bb = Unpooled.wrappedBuffer(workBuffer);
//         byte[] plainBytes = new byte[decryptedSize];
//         bb.readBytes(plainBytes);
//         return plainBytes;
//     }

// ///////////////////////////////////////////////////////////////////////////////////////////////////
// /////// Send NetMsg ChannelMsg ////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////////////////

//     @Override
//     public void sendChannelMsgWithNetMsg(QChannelMsg channelMsg) {
//         reverseChannelMsgDirection(channelMsg);
//         byte[] netMsgPlainBytes = serialization.objToBytes(channelMsg.netMsg);
// //        printPrettyMsg("Response NetMsg Bytes before encrypt", netMsgPlainBytes);
//         byte[] netMsgEncryptedBytes = encryptBytes(netMsgPlainBytes);
// //        printPrettyMsg("Response ChannelMsg Bytes after encrypt", netMsgEncryotedBytes);
//         channelMsg.bytes = netMsgEncryptedBytes;
//         channelMsg.role = role;
//         sendChannelMsg(currentChannelMsg);

// //        byte[] channelMsgBytes = serialization.objToBytes(channelMsg);
// //
// //        packetHandler.sendPacket(this, channelMsgBytes);

//     }

// ///////////////////////////////////////////////////////////////////////////////////////////////////
// /////// Send Channel Msg //////////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////////////////


// //    public void sendChannelMsg(QChannelMsg channelMsg) {
// //        this.currentChannelMsg = channelMsg;
// //        sendChannelMsg();
// //    }

//     @Override
//     public void sendChannelMsg(QChannelMsg channelMsg) {
// //        byte[] channelMsgBytes = serialization.objToBytes(currentChannelMsg);
//         packetHandler.sendPacket(this, currentChannelMsg);
// //        if (doChannelMsgTimeouts) {
// //            node.getChannelMsgTimer().schedule(new QChannelTimerTask(), CHANNEL_MSG_TIMEOUT_2);
// //        }
//         if (role == HandshakeState.RESPONDER && (!currentChannelMsg.keepAlive) && cipherPair != null) {
//             node.removeServerChannel(this);
//         }
//         currentChannelMsg = null;
//     }

//     @Override
//     public void receiveChannelMsg(QChannelMsg channelMsg){
//         currentChannelMsg = channelMsg;
// //        if (channelMsgTimerTask != null) {
// //            channelMsgTimerTask.cancel();
// //        }
//         currentChannelMsg.channel = this;
//         if (cipherPair == null) {
//             if (currentChannelMsg.role == HandshakeState.INITIATOR) {
//                 handshakeRequest();
//             } else {
//                 handshakeResponse();
//             }

//         } else {
//             byte[] netMsgEncryptedBytes = currentChannelMsg.bytes;
//             byte[] netMsgPlainBytes = decryptBytes(netMsgEncryptedBytes);
//             currentChannelMsg.netMsg = (QNetMsg) serialization.objFromBytes(netMsgPlainBytes);
//             if (currentChannelMsg.role == HandshakeState.INITIATOR) {
//                 currentChannelMsg.netMsg.handleRequest(currentChannelMsg);
//             } else {
//                 currentChannelMsg.netMsg.handleResponse(currentChannelMsg);
//             }
//             currentChannelMsg = null;
//         }
//     }

// //    class QChannelTimerTask extends TimerTask {
// //        public void run() {
// //            System.out.println("ChannelMsg timed out");
// //        }
// //    }

//     private static void reverseChannelMsgDirection(QChannelMsg channelMsg) {
// //        UUID tmp = channelMsg.initiatorUUID;
// //        channelMsg.initiatorUUID = channelMsg.responderUUID;
// //        channelMsg.responderUUID = tmp;
//         if (channelMsg.role == HandshakeState.INITIATOR) channelMsg.role = HandshakeState.RESPONDER;
//         else channelMsg.role = channelMsg.role = HandshakeState.INITIATOR;
//     }

// ///////////////////////////////////////////////////////////////////////////////////////////////////
// /////////// Utils /////////////////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////////////////

//     @Override
//     public void setDestinationNetAddress(QNetAddress destNetAddress) {
//         this.destNetAddress = destNetAddress;
//     }

//     @Override
//     public int getRole() {
//         return role;
//     }

//     @Override
//     public QNetAddress getDestNetAddress() {
//         return destNetAddress;
//     }

//     @Override
//     public void setFirstMessage(QNetMsg firstMsg) {
//         this.firstNetMsg = firstMsg;
//     }

// //    @Override
// //    public void setSrcUUID(UUID uuid) {
// //        this.srcUUID = uuid;
// //    }
// //
// //    public void setDestUUID(UUID uuid) {
// //        this.destUUID = uuid;
// //    }

//     @Override
//     public boolean isNew() {
//         return isNew;
//     }

//     @Override
//     public boolean isFree() {
//         return !busy;
//     }

//     @Override
//     public void setNode(QNode node) {
//         this.node = node;
//     }

//     @Override
//     public QNode getNode() {
//         return node;
//     }

//     private void printMsg(String msg) {
//         System.out.println(msg);
//         log.info(msg);
//     }

//     @Override
//     public void close() {
// //        sendClosePacket();
// //        makeSafe(null);
//     }

//     @Override
//     public void setChannelId(int channelId) {
//         this.channelId = channelId;
//     }

//     @Override
//     public int getChannelId() {
//         return channelId;
//     }

//     @Override
//     public void setRole(int role) {
//         this.role = role;
//     }

//     @Override
//     public void setDisplayName(String name) {
//         this.displayName = name;
//     }

//     @Override
//     public void setIsNew(boolean b) {
//         isNew = b;
//     }

//     private void badResult(String s) {
//         String msg = "BadResult -> " + s;
//         printMsg(msg);
// //        packethandler.badResult();
// //        throw new QNodeException("Channel Failed");
// //        makeSafe(rt);
//     }

//     private void makeSafe(QChannelMsg channelMsg) {

//     }

//     private void printPrettyMsg(String msg, byte[] bytes) {
//         printMsg(displayName + msg + " -> " + QStringUtils.prettyArrayBytes(bytes));
//     }

//     public int getSrcIdx() {
//         return srcIdx;
//     }

//     @Override
//     public void setSrcIdx(int srcIdx) {
//         this.srcIdx = srcIdx;
//     }

//     public int getDestIdx() {
//         return destIdx;
//     }

//     @Override
//     public void setDestIdx(int destIdx) {
//         this.destIdx = destIdx;
//     }

// ///////////////////////////////////////////////////////////////////////////////////////////////////
// } ///////// End Class /////////////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////////////////
