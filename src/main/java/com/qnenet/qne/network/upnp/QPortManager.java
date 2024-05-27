// package com.qnenet.qne.network.upnp;

// import com.qnenet.qne.network.known.QKnown;
// import com.qnenet.qne.objects.classes.*;
// import com.qnenet.qne.objects.impl.QNEObjects;
// import com.qnenet.qne.system.constants.QSysConstants;
// import com.qnenet.qne.system.impl.QNEPaths;
// import com.qnenet.qne.system.impl.QSystem;
// import com.qnenet.qne.system.utils.QFileUtils;
// import com.qnenet.qne.system.utils.QNetworkUtils;
// import jakarta.annotation.PreDestroy;
// import org.apache.commons.io.FileUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.env.Environment;
// import org.springframework.stereotype.Service;
// import org.xml.sax.SAXException;

// import javax.xml.parsers.ParserConfigurationException;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.ArrayList;
// import java.util.Map;
// import java.util.concurrent.ExecutorService;

// @Service
// public class QPortManager {

//     @Autowired
//     QNEPaths qnePaths;

//     @Autowired
//     QKnown known;

//     @SuppressWarnings("rawtypes")
//     @Autowired
//     QNEObjects qobjs;

//     @Autowired
//     Environment env;

//     private QGatewayDevice activeGW;

//     private String lanIPAddress;
//     private String wanIPAddress;

//     private ArrayList<QMappedPort> otherMappedPorts = new ArrayList<>();
//     private ArrayList<QMappedPort> otherQNEMappedPorts = new ArrayList<>();
//     private ArrayList<QMappedPort> myMappedPorts = new ArrayList<>();

//     private Path portsPath;
//     private int systemPort;

//     private QSystem system;

//     @SuppressWarnings("unused")
//     private Map<QInetAddr, QGatewayDevice> gateways;
//     @SuppressWarnings("unused")
//     private boolean isNew = false;
//     @SuppressWarnings("unused")
//     private ExecutorService executor;

//     private boolean needManualPortSetting = false;
//     private Path prevWanIPAddressFilePath;
//     private QEPAddrPair systemEPAddrPair;

//     //////// init /////////////////////////////////////////////////////////////////////////////////
//     //////// init /////////////////////////////////////////////////////////////////////////////////
//     //////// init /////////////////////////////////////////////////////////////////////////////////

//     // system not autowired because of circ ref
//     public void init(QSystem system) {
// //        system.getExecutor().submit(() -> {
//         this.system = system;
//         executor = system.getExecutor();

//         String portStr = env.getProperty("server.port");
//         systemPort = Integer.parseInt(portStr);
//         portsPath = Paths.get(qnePaths.getSystemPath().toString(), "ports");
//         prevWanIPAddressFilePath = Paths.get(portsPath.toString(), "prevWanIpAddr.pathStr");

//         if (QFileUtils.checkDirectory(portsPath)) {
//             isNew = true;
//         }

//         gateways = discoverGateways();
//         if (activeGW == null) { // new system, gateway device not found
//             needManualPortSetting();
//         } else {
//             boolean hasGateway = findGateway();
//             if (!hasGateway) needManualPortSetting(); // port setting failed
//         }
// //        });
//     }

//     private boolean findGateway() {
//         lanIPAddress = activeGW.getLocalInetAddress().getHostAddress();
//         try {
//             getAndCheckWanIPAddress();
//         } catch (IOException e) {
//             throw new RuntimeException(e);
//         } catch (SAXException e) {
//             throw new RuntimeException(e);
//         }
//         getAllMappedPorts();
//         if (myMappedPorts.isEmpty()) {
//             try {
//                 activeGW.addPortMapping(systemPort, systemPort, lanIPAddress, QSysConstants.PROTOCOL_TCP,
//                         QSysConstants.PORT_MAP_DESCRIPTION);
//                 activeGW.addPortMapping(systemPort, systemPort, lanIPAddress, QSysConstants.PROTOCOL_UDP,
//                         QSysConstants.PORT_MAP_DESCRIPTION);
//             } catch (IOException e) {
//                 throw new RuntimeException(e);
//             } catch (SAXException e) {
//                 throw new RuntimeException(e);
//             }
//             getAllMappedPorts();
//         }

//         if (myMappedPorts.size() != 2)
//             return false;
//         else {
//             setSystemEPAddr();
//             return true;
//         }
//     }

//     private void setSystemEPAddr() {
//         systemEPAddrPair = new QEPAddrPair();
//         systemEPAddrPair.epId = null;
//         systemEPAddrPair.epIdx = QSysConstants.SYSTEM_EP_IDX;
//         systemEPAddrPair.wanIPAddrBytes = QNetworkUtils.ipAddressToBytes(wanIPAddress);
//         systemEPAddrPair.lanIPAddrBytes = QNetworkUtils.ipAddressToBytes(lanIPAddress);
//         systemEPAddrPair.port = systemPort;
//     }

//     private String getAndCheckWanIPAddress() throws IOException, SAXException {

//         wanIPAddress = activeGW.getExternalIPAddress();
//         if (Files.notExists(prevWanIPAddressFilePath)) {
//             wanIPAddressHasChanged();
//         }

//         @SuppressWarnings("deprecation")
//         String prevWanIpAddress = FileUtils.readFileToString(prevWanIPAddressFilePath.toFile());
//         if (prevWanIpAddress.equals(wanIPAddress)) return wanIPAddress;
//         wanIPAddressHasChanged();
//         return wanIPAddress;
//     }

//     @SuppressWarnings("deprecation")
//     private void wanIPAddressHasChanged() throws IOException {
//         FileUtils.writeStringToFile(prevWanIPAddressFilePath.toFile(), wanIPAddress);
//         system.updateCertificate(wanIPAddress);
//     }

//     private void getAllMappedPorts() {
//         int idx = 0;
//         boolean mappedPortFound;
//         do {
//             QMappedPort mappedPort = new QMappedPort();
//             try {
//                 mappedPortFound = activeGW.getMappedPortByIdx(idx, mappedPort);
//             } catch (IOException e) {
//                 throw new RuntimeException(e);
//             } catch (SAXException e) {
//                 throw new RuntimeException(e);
//             }
//             if (mappedPortFound) {
//                 if (mappedPort.getPortMappingDescription().equals(QSysConstants.PORT_MAP_DESCRIPTION)) {
//                     if (mappedPort.getExternalPort() == systemPort) myMappedPorts.add(mappedPort);
//                     else otherQNEMappedPorts.add(mappedPort);
//                 } else {
//                     otherMappedPorts.add(mappedPort);
//                 }
//             } else break;
//             idx++;
//         } while (mappedPortFound);
//     }


//     private void needManualPortSetting() {
//         needManualPortSetting = true;
//         wanIPAddress = QNetworkUtils.getExternalAddress();
//         lanIPAddress = QNetworkUtils.getLanIPAddress();
//         setSystemEPAddr();
//     }

//     public boolean isNeedManualPortSetting() {
//         return needManualPortSetting;
//     }


// //////// open & close port ////////////////////////////////////////////////////////////////////////
// //////// open & close port ////////////////////////////////////////////////////////////////////////
// //////// open & close port ////////////////////////////////////////////////////////////////////////

//     @SuppressWarnings("unused")
//     private boolean openPort(QMappedPort mappedPort) {
//         try {
//             return activeGW.addPortMapping(mappedPort.getExternalPort(), mappedPort.getInternalPort(),
//                     mappedPort.getInternalClient(), mappedPort.getProtocol(),
//                     mappedPort.getPortMappingDescription());
//         } catch (IOException e) {
//             return false;
//         } catch (SAXException e) {
//             return false;
//         }
//     }

//     private boolean closePort(QMappedPort mappedPort) {
//         try {
//             return activeGW.deletePortMapping(mappedPort.getInternalPort(), mappedPort.getProtocol());
//         } catch (IOException e) {
//             return false;
//         } catch (SAXException e) {
//             return false;
//         }
//     }

// //////////// Discover Gateway /////////////////////////////////////////////////////////////////////
// //////////// Discover Gateway /////////////////////////////////////////////////////////////////////
// //////////// Discover Gateway /////////////////////////////////////////////////////////////////////

//     private Map<QInetAddr, QGatewayDevice> discoverGateways() {
//         QGatewayDiscover gatewayDiscover = new QGatewayDiscover();
//         system.say("Looking for Gateway Devices...");

//         Map<QInetAddr, QGatewayDevice> gateways = null;
//         try {
//             gateways = gatewayDiscover.discover();
//         } catch (IOException e) {
//             throw new RuntimeException(e);
//         } catch (SAXException e) {
//             throw new RuntimeException(e);
//         } catch (ParserConfigurationException e) {
//             throw new RuntimeException(e);
//         }

//         if (gateways.isEmpty()) {
//             system.say("No gateways found");
//             system.say("Stopping weupnp");
//             activeGW = null;
//             return gateways;
//         }
//         system.say(gateways.size() + " gateway(s) found\n");

//         for (QInetAddr qInetAddr : gateways.keySet()) {
//             QGatewayDevice gatewayDevice = gateways.get(qInetAddr);
//             try {
//                 gatewayDevice.setWanAddress(gatewayDevice.getExternalIPAddress());
//                 gatewayDevice.setLanAddress();
//             } catch (IOException e) {
//                 throw new RuntimeException(e);
//             } catch (SAXException e) {
//                 throw new RuntimeException(e);
//             }

//             system.say(gatewayDevice.getAsString(gateways.size() - 1));

//         }
//         activeGW = gatewayDiscover.getValidGateway();
//         return gateways;
//     }

// ///////////////////////////////////////////////////////////////////////////////////////////////////
// //////////// Getters & Setters ////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////////////////

// //    private void say(String line) {
// //        String timeStamp = DateFormat.getTimeInstance().format(new Date());
// //        String logline = timeStamp + ": " + line + "\n";
// //        System.out.print(logline);
// //    }

// //////////// Destroy //////////////////////////////////////////////////////////////////////////////
// //////////// Destroy //////////////////////////////////////////////////////////////////////////////
// //////////// Destroy //////////////////////////////////////////////////////////////////////////////

//     public void closeMyPorts() {
//         for (QMappedPort mappedPort : myMappedPorts) {
//             closePort(mappedPort);
//         }
//     }

//     public void closeOtherQNEPorts() {
//         for (QMappedPort mappedPort : otherQNEMappedPorts) {
//             closePort(mappedPort);
//         }
//     }

//     @PreDestroy
//     public void destroy() {
//         boolean remove = Boolean.parseBoolean(env.getProperty("remove.my,port.mapping.on.destroy"));
//         boolean removeOther = Boolean.parseBoolean(env.getProperty("remove.other.qne.port.mapping.on.destroy"));
//         if (remove) {
//             system.say("QPortManager Ports Closed on Destroy");
//             closeMyPorts();
//             if (removeOther) closeOtherQNEPorts();
//         }
//         System.out.println("QPortManager Callback triggered - @PreDestroy.");
//     }

// //////////// End Class //////////////////////////////////////////////////////////////////////////
// } ////////// End Class //////////////////////////////////////////////////////////////////////////
// //////////// End Class //////////////////////////////////////////////////////////////////////////



