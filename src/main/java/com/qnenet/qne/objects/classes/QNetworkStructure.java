package com.qnenet.qne.objects.classes;


import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QNetworkStructure {

    public ArrayList<QEPAddr> discoveryServersList;
    public ArrayList<QEPAddr> certificateServersList;
    public Map<Integer, ArrayList<QEPAddr>> segmentServersMap;
    public long timestamp;

}
