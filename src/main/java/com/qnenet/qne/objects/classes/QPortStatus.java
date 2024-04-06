//package com.qnenet.qne.objects.classes;
//
//import com.qnenet.qne.network.upnp.QGatewayDevice;
//import com.qnenet.qne.system.constants.QSysConstants;
//
//import java.net.InetAddress;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.Map;
//
//public class QPortStatus {
//
//    private transient ArrayList<QMappedPort> prevQNEPorts;
//    private transient ArrayList<QMappedPort> prevOtherPorts;
//
////    private Path currentFilePath;
//    private Path PreviousFilePath;
//
//    private ArrayList<QMappedPort> allMappedPorts;
//
//    private ArrayList<QMappedPort> currentQNEPorts;
//    private ArrayList<QMappedPort> currentOtherPorts;
//
//    private boolean portManagerInitOK;
//    private boolean portsSet;
////    private QEPAddrPair epAddrPair;
//    private boolean portsSetOK;
//    private int systemPort;
//    private boolean isFirst;
//    private boolean systemPortForwardedOK;
//
//    private String friendlyName;
//    private String presentationURL;
//    private String modelName;
//    private String modelNumber;
//    private String lanIPAddress;
//    private String wanIPAddress;
//
//    private QPortStatus previousPortStatus;
//    private Map<QInetAddr, QGatewayDevice> gateways;
//    private boolean portMappingChanged;
//
//
//    public QPortStatus() {
//    }
//
//    public void setPortManagerInitOK(boolean portManagerInitOK) {
//        this.portManagerInitOK = portManagerInitOK;
//    }
//
//
//    public void setPortsSet(boolean portsSet) {
//        this.portsSet = portsSet;
//    }
//
////    public void setEPAddressPair(QEPAddrPair epAddrPair) {
////        this.epAddrPair = epAddrPair;
////    }
//
////    public void setPortSetOk(boolean portSetOK) {
////        this.portsSetOK = portSetOK;
////    }
//
//    public void handleAllMappedPorts() {
////        if (prevMappedPorts == null) {
////            saveMappedPorts(currentMappedPorts);
////            prevMappedPorts = currentMappedPorts;
////        }
//
//        ArrayList<QMappedPort> prevQNEPorts = new ArrayList<>();
//        ArrayList<QMappedPort> prevOtherPorts = new ArrayList<>();
//        ArrayList<QMappedPort> currentQNEPorts = new ArrayList<>();
//        ArrayList<QMappedPort> currentOtherPorts = new ArrayList<>();
//
////        for (QMappedPort mappedPort: prevMappedPorts) {
////            if (mappedPort.getPortMappingDescription().equals(QSysConstants.PORT_MAP_DESCRIPTION)) {
////                prevQNEPorts.add(mappedPort);
////            } else {
////                prevOtherPorts.add(mappedPort);
////            }
////        }
//
////        for (QMappedPort mappedPort: currentMappedPorts) {
////            if (mappedPort.getPortMappingDescription().equals(QSysConstants.PORT_MAP_DESCRIPTION)) {
////                currentQNEPorts.add(mappedPort);
////            } else {
////                currentOtherPorts.add(mappedPort);
////            }
////        }
//    }
//
//    public void setSystemPort(int systemPort) {
//        this.systemPort = systemPort;
//    }
//
//    public void setIsFirst(boolean isFirst) {
//        this.isFirst = isFirst;
//    }
//
//    public boolean isFirst() {
//        return isFirst;
//    }
//
//    public void setSystemPortInGatewayOK(boolean portOK) {
//        this.systemPortForwardedOK = portOK;
//    }
//
//    public void setPreviousFilePath(Path previousPortFilePath) {
//        this.PreviousFilePath = previousPortFilePath;
//    }
//
//    public void setWanIPAddress(String wanIPAddress) {
//       this.wanIPAddress = wanIPAddress;
//    }
//
//    public void setLanIPAddress(String lanIPAddress) {
//        this.lanIPAddress = lanIPAddress;
//    }
//
//    public void setAllMappedPorts(ArrayList<QMappedPort> allMappedPorts) {
//        this.allMappedPorts = allMappedPorts;
////        handleAllMappedPorts();
//    }
//
//    public void setPreviousPortStatus(QPortStatus previousPortStatus) {
//        this.previousPortStatus = previousPortStatus;
//    }
//
//    public void setGateways(Map<QInetAddr, QGatewayDevice> gateways) {
//        this.gateways = gateways;
//    }
//
//    public void setPortMappingChanged(boolean b) {
//        this.portMappingChanged = b;
//    }
//
//    public String getWanIPAddress() {
//        return this.wanIPAddress;
//    }
//
//    public String getLanIPAddress() {
//        return this.wanIPAddress;
//    }
//
////    public String getPort() {
////        return this.port;
////    }
//}
