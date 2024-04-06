package com.qnenet.qne.objects.classes;//package com.qnenet.qne.objects.intf;
//
//import com.qnenet.qne.classes.objectsQNE.QInviteInfo;
//import com.qnenet.qne.classes.objectsQNE.QEndPointProps;
//import org.jasypt.util.binary.AES256BinaryEncryptor;
//
//import java.nio.file.Path;
//import java.util.ArrayList;
//
//public interface QEndPointInfo {
//    void initNew(QNode node, QInviteInfo inviteInfo) ;
//
//    void restartInit(QNode node, char[] littlePassword, Path memberPath);
//
//    void setMemberInfo(QEndPointProps memberInfo);
//
//    public void saveMemberInfo();
//
//    public void loadMemberInfo(char[] littlePassword);
//
//
//    String getName();
//
//    char[] getLittlePassword();
//
//    void setEmail(String email);
//
//    QEndPointProps getMemberInfo();
//
//    ArrayList<String> getRoutesList();
//
//    String getMemberDirStr();
//
//    AES256BinaryEncryptor getEncryptor();
//}
