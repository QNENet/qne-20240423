package com.qnenet.qne.objects.classes;

//import com.qnenet.qne.objects.intf.QNode;

import org.jasypt.util.binary.AES256BinaryEncryptor;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class QEndPointProps {

//    public QEPId qEPId;

    public int endPointType;  // default = QSysConstants.MEMBER_TYPE_NORMAL

//    public QEndPointInfo endPointInfo;

    public String endPointName;

    public int endPointIdx;

    public LocalDateTime createdDateTime;

//    public UUID uuid;

    public char[] bigPassword;
    public char[] littlePassword;

    public QNoiseKeypair noiseKeypair;

    public String endPointDirectory;
    public String endPointStoreDirectory;
    public String endPointInfoFilePathStr;



//    public ArrayList<Integer> statusList;

//    public String firstName;
//    public String knownAs;
//    public String lastName;
//    public String email;
//
//    public int inviteId;

    public transient Path endPointPath;
    public transient Path endPointStorePath;

    public transient AES256BinaryEncryptor bigEncryptor;
    public transient AES256BinaryEncryptor littleEncryptor;


//    public QEPId qepId;
//    public byte[] wanIPAddressBytes;
//    public byte[] lanIPAddressBytes;
//    public int port;
    public byte[] publicKeyBytes;
    public QEPAddrPair epAddrPair;
    public String mainEmail;
}
