package com.qnenet.qne.system.api;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
@Scope("prototype")
public class QSessionInfo {
    public boolean isSystemLoggedIn;

    public boolean isLoggedIn;
    public boolean newSystemState;

    public QSessionInfo() {
//        map = new ConcurrentHashMap<>();
//        routesList = new ArrayList<>();
    }

}

//    public String sessionId;
//    public QNode node;
//    public QEndPointInfo endpoint;
//
//    public FixedVaadinServlet fixedVaadinServlet;
//    public MainLayout mainLayout;
//
//    public boolean isLoggedIn;
//    public int loginTries;
////    public String allowedViewsName;
//
//
//    public QSystem system;
//    public QSystemManager systemManager;
//
//    public QHelp help;
//
//    public QSerialization serialization;
//
////    public ConcurrentHashMap<String, Object> map;
//
//    public ArrayList<String> routesList;
//
//    //    public QAddonsManager addonsManager;
//    public QNewSystemState newSystemState;
//    public boolean newSystemViewInstalled;
//    public QView currentView;
//    public QView prevView;
////    public QRouteManager routeManager;
////    public ConcurrentHashMap<String, ArrayList<VerticalLayout>> helpMap;
//
////    public QuickManager quickManager;
////    public ArrayList<String> quickList;
////    public ConcurrentHashMap<String, QuickItem> quickMap;
////    public boolean isDemo;
//
