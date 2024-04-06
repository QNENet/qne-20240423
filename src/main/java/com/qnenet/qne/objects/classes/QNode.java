package com.qnenet.qne.objects.classes;///*
// * Copyright (C) 2022 Paul F Fraser <paulf@qnenet.com>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *     http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.qnenet.qne.objects.intf;
//
//import com.qnenet.qne.classes.objectsQNE.QChannelMsg;
//import com.qnenet.qne.classes.objectsQNE.QInviteInfo;
//import com.qnenet.qne.objects.classes.QNetAddressPair;
//import com.qnenet.qne.objects.classes.QNodeInfo;
//import org.jasypt.util.binary.AES256BinaryEncryptor;
//
//import java.awt.*;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.file.Path;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//import java.security.PrivateKey;
//import java.util.ArrayList;
//
//public interface QNode {
//
//    QEndPointInfo addMember(QInviteInfo inviteInfo);
//
//    void registerObject(String key, Object value);
//
//    void unregisterObject(String key);
//
//    Object getRegisteredObject(String key);
//
//    int getUniqueRTId();
//
//    QNodeInfo getNodeInfo();
//
////    Path getNodePath();
//
//    String getNodeDirStr();
//
//    Path getMembersPath();
//
//    byte[] getNoisePrivateKeyClone();
//
//    byte[] getNoisePublicKeyClone();
//
//    int getUniqueChannelId();
//
//    QPacketHandler getPacketHandler();
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Getters & Setters /////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    QChannel getOrCreateServerChannel(QChannelMsg channelMsg);
//
//    AES256BinaryEncryptor getEncryptor();
//
//    QChannel getClientChannel(int channelId);
//
//    void removeClientChannel(QChannel channel);
//
//    void removeServerChannel(QChannel channel);
//
//    void registerChannelMsgRT(QChannelMsg channelMsg);
//    void unregisterChannelMsgRT(int rtId);
//
//    QNetMsgOrigin getChannelMsgOrigin(QChannelMsg channelMsg);
//
//    void removeClientChannelIfNecessary(QChannelMsg channelMsg);
//
//    void processChannelMsg(QChannelMsg channelMsg);
//
//
//    void doRoundTripRequest(QNetMsg netMsg);
//
//    void newSystem() throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException;
//
//    void addNickname(String nickName, QNetAddressPair netAddressPair);
//
//    QInviteInfo getInviteInfo(int inviteId);
//
//    void saveNodeInfo();
//
////    QNetAddressPair getRandomDiscoveryServerNetAddress();
//
//    String getWanAddressStr();
//
//    String getLanAddressStr();
//
//    PrivateKey getSSLPrivateKey();
//
////    void swapNetEndPointInfo(QNetAddress netAddr);
//
////    void addOrUpdateNetEndPointInfo(QNetEndPointInfo netEndPointInfo, boolean doSave);
////
////    QNetEndPointInfo getSelfNetEndPointInfo();
////
////    QNetEndPointInfo getNetEndPointInfoByUUID(UUID uuid);
////
////    void removeNetEndPointInfo(UUID uuid);
//
//    QEndPointInfo getMember(int installIdx);
//
//    void registerMember(int installIdx, QEndPointInfo qMember);
//
//    boolean checkIfEmailAlreadyInvited(String email);
//
//    QEndPointInfo findMemberByPin(int pin);
//
//    boolean setMemberPin(int installIdx);
//
//    void createEmail(QInviteInfo inviteInfo, Desktop desktop);
//
//    void registerInvite(QInviteInfo inviteInfo);
//
//    ArrayList<String> getRoutesList();
//
//    void putArtifactBytesInRepository(String artifactName, byte[] artifactBytes);
//
//    RandomAccessFile getFileTransferRaf(String transferName);
//
//    Path getArtifactsPath();
//
//    long getFileCheckSum(Path rafPath);
//
//    boolean copyArtifactFromAnotherNodeIntoRpository(String artifactName);
//
//}
