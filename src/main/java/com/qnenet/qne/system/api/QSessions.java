package com.qnenet.qne.system.api;

import com.qnenet.qne.system.constants.QSysConstants;
import com.qnenet.qne.system.impl.QSystem;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.concurrent.ConcurrentHashMap;

//@Component
public class QSessions {

    @Autowired
    ApplicationContext ctx;

    @Autowired
    QSystem system;

    ConcurrentHashMap<String, QSessionInfo> sessions = new ConcurrentHashMap<>();

    public QSessions() {
        System.out.println("Start Sessions");
    }


    public QSessionInfo sessionInit(VaadinSession session) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, NoSuchProviderException {
//        if (sessions.isEmpty()) {
//            System.out.println("Needs Restart");
//            system.restart(null);
//        }


        String sessionId = session.getSession().getId();
        QSessionInfo sessionInfo = sessions.get(sessionId);
        if (sessionInfo == null) {
            sessionInfo = (QSessionInfo) ctx.getBean("QSessionInfo.class");
            if (sessions.isEmpty()) {
                sessionInfo.isSystemLoggedIn = false;
            }
            sessions.put(sessionId, sessionInfo);
            session.setAttribute(QSysConstants.SESSION_INFO, sessionInfo);
        }
        return sessionInfo;
    }
}
