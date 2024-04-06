package com.qnenet.qne.ui.views.invite.service;

import com.qnenet.qne.objects.classes.QInviteInfo;
import com.qnenet.qne.objects.impl.QNEObjects;
import com.qnenet.qne.system.impl.QNEPaths;
import com.qnenet.qne.system.impl.QSystem;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;


//@Service
public class QInviteService {

    @Autowired
    QNEPaths qnePaths;

    @Autowired
    QSystem system;

    @Autowired
    QNEObjects qobjs;



    private Path invitesMapFilePath;
    private ConcurrentHashMap<Integer, QInviteInfo> invitesMap;

    public QInviteService() {
//        invitesMapFilePath = Paths.get(qnePaths.getNodePath().toString(), "invites.map");
//        if (Files.notExists(invitesMapFilePath)) {
//            invitesMap = new ConcurrentHashMap<>();
//            saveInvitesMap();
//        } else {
//            loadInvitesMap();
//        }
    }

//    @Override
//    public QEndPointInfo addMember(QInviteInfo inviteInfo) {
//        QEndPointInfo endpoint = memberFactory.getService();
//        endpoint.initNew(this, inviteInfo);
//        return endpoint;
//    }

//    public boolean checkIfEmailAlreadyInvited(String email) {
//        for (QEndPointInfo endpoint : members.values()) {
//            if (endpoint.getMemberInfo().email.equals(email)) return true;
//        }
//        return false;
//    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////// Invites /////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


    public void createEmail(QInviteInfo inviteInfo, Desktop desktop) {
        ExecutorService executor = system.getExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
//                        createEmail(inviteInfo);
                StringBuilder sb = new StringBuilder();
                sb.append("mailto:");
                sb.append(inviteInfo.mainEmailAddress);
                sb.append("?subject=");
//                        sb.append("Quick%20N%20Easy%20Invite");
//                        sb.append("&body=");
//                        sb.append("Invite%20to%20try%20Quick%20N%20Easy%0D%0A");
//                        sb.append("Quick%20N%20Easy%20is%20great");

                sb.append("Quick N Easy Invite".replace(" ", "%20"));
                sb.append("&body=");
                sb.append("Invite to try Quick N Easy".replace(" ", "%20"));
                sb.append("Quick N Easy is great".replace(" ", "%20"));


//                        String message = "mailto:" + emailValue + "?subject=QuickNEasyInvite&body=Invite";
                String message = sb.toString();
                System.out.println(message);
                URI uri = URI.create(message);
                try {
                    desktop.mail(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void addInvite(QInviteInfo inviteInfo) {
        invitesMap.put(inviteInfo.inviteId, inviteInfo);
        saveInvitesMap();
    }

    public void removeInvite(QInviteInfo inviteInfo) {
        invitesMap.remove(inviteInfo.inviteId);
        saveInvitesMap();
    }


    public QInviteInfo getInviteInfo(int inviteId) {
        return invitesMap.get(inviteId);
    }


///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Persistence ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    private void saveInvitesMap() {
        qobjs.saveObjToEncFile(invitesMapFilePath, invitesMap, system.getBigEncryptor());
    }

    private void loadInvitesMap() {
        invitesMap = (ConcurrentHashMap<Integer, QInviteInfo>) qobjs.loadObjFromEncFile(invitesMapFilePath, system.getBigEncryptor());
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
} /////// End Class ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
