//package com.qnenet.qne.network.nicknames;
//
//
//import com.qnenet.qne.objects.classes.QEPAddr;
//import com.qnenet.qne.objects.impl.QNEObjects;
//import com.qnenet.qne.system.impl.QNEPaths;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//public class QNickNames {
//
//    @Autowired
//    QNEPaths qnePaths;
//
//    @Autowired
//    QNEObjects qobjs;
//
//
//    private Path nicknamesMapFilePath;
//    private Map<String, QEPAddr> nicknamesMap;
//
//    @PostConstruct
//    public void init() {
//
//        nicknamesMapFilePath = Paths.get(qnePaths.getKnownPath().toString(), "nicknames.map");
//
//        if (Files.notExists(nicknamesMapFilePath)) {
//            nicknamesMap = new ConcurrentHashMap<>();
//            saveNicknamesMap();
//        } else {
//            loadNicknamesMap();
//        }
//
//    }
//
//    public void destroy(){
//        System.out.println("Shutting down NickNames bean");
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// NickNames ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void addNickname(String nickName, QEPAddr epAddr) {
//        nicknamesMap.put(nickName, epAddr);
//        saveNicknamesMap();
//    }
//
//    public void removeNickname(String nickName) {
//        nicknamesMap.remove(nickName);
//        saveNicknamesMap();
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Persistence ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    private void saveNicknamesMap() {
//        qobjs.saveObjToFile(nicknamesMapFilePath, nicknamesMap);
//    }
//
//    private void loadNicknamesMap() {
//        nicknamesMap = (Map<String, QEPAddr>) qobjs.loadObjFromFile(nicknamesMapFilePath);
//    }
//
//    public QEPAddr getEndPointByNickName(String nickName) {
//        return nicknamesMap.get(nickName);
//    }
//
//    public void clearAll() {
//        nicknamesMap.clear();
//        saveNicknamesMap();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} /////// End Class ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
