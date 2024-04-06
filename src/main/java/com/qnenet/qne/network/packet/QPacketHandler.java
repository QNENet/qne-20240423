
package com.qnenet.qne.network.packet;

import com.qnenet.qne.network.endpoint.QEndPoint;
import com.qnenet.qne.system.utils.QDatagramUtils;
import com.qnenet.qne.system.utils.QRandomUtils;
import com.southernstorm.noise.protocol.HandshakeState;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Class /////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

@Service
public class QPacketHandler {

    private static final int DATAGRAM_BUFFER_SIZE = 64 * 1024;      // 64KB
    // private static final long RESPONSE_TIMEOUT = 2000L;
    //    private static final long RESPONSE_TIMEOUT = 60 * 60 * 1000L; // 60 mins
    // private static final Integer MAX_TIMEOUTS = 3;

    private DatagramSocket socket;
    private boolean isRunning;

    private Thread listenThread;
    private int port = 12345;

    ArrayList<LinkedBlockingQueue<DatagramPacket>> receivedPacketsQueueList = new ArrayList<>();
    ConcurrentHashMap<Integer, QEndPoint> epsByIdxMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Integer, QChannel> allChannelsMap = new ConcurrentHashMap<>();

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Constructor ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    @PostConstruct
    public void postConstruct() {
        startListener();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Send Packet ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public void sendPacket(DatagramPacket packet) {
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Receive Packet ////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public void receivePacket(DatagramPacket packet) {
        QPacket qPacket = QDatagramUtils.unPackPacket(packet);
        if (qPacket.destRole == HandshakeState.INITIATOR) {
            QClientChannel clientChannel = (QClientChannel) allChannelsMap.get(qPacket.channelId);
            clientChannel.clientReceivePacket(qPacket);
            return;
        }
        QServerChannel serverChannel = (QServerChannel) allChannelsMap.get(-qPacket.channelId);
        if (serverChannel != null) {
            serverChannel.serverReceivePacket(qPacket);
            return;
        }

        QEndPoint endPoint = epsByIdxMap.get(qPacket.destEPAddr.getEPIdx());

        serverChannel = new QServerChannel(endPoint, qPacket.destEPAddr, qPacket.channelId);
        allChannelsMap.put(-serverChannel.getChannelId(), serverChannel);

        serverChannel.serverReceivePacket(qPacket);

    }


///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Listener //////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public void startListener() {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println("Socket create exception -> " + e.getMessage());
//            throw new RuntimeException(e);
        }
        listenThread = new Thread(() -> {
            try {
                while (isRunning) {
                    // Wait for a packet
                    byte[] buffer = new byte[DATAGRAM_BUFFER_SIZE];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet); // blocked until packet received
                    receivePacket(packet);
                }
            } catch (IOException e) {
//                throw new RuntimeException(e);
                System.out.println("Socket closed exception -> " + e.getMessage());

            }
        });
        listenThread.start();
        isRunning = true;
        System.out.println("LLL LLL LLL Socket listening on port -> " + port);
    }


    public void stopListener() {
        if (listenThread != null) {
            listenThread.interrupt();
            listenThread = null;
        }
        if (socket != null && !socket.isClosed()) {
            socket.close();
            socket = null;
        }
        isRunning = false;
        System.out.println("CCC CCC CCC Socket Closed");
    }

    public void registerEndPoint(int epIdx, QEndPoint testEP) {
        epsByIdxMap.put(epIdx, testEP);
    }

    public void addClientChannel(QChannel packetChannel) {
        allChannelsMap.put(packetChannel.getChannelId(), packetChannel);
    }

    public void addServerChannel(QChannel packetChannel) {
        allChannelsMap.put(-packetChannel.getChannelId(), packetChannel);
    }

    public int getUniqueChannelId() {
        int channelId = QRandomUtils.randomIntBetween(1, Integer.MAX_VALUE - 1);
        if (allChannelsMap.keySet().contains(channelId)) {
            getUniqueChannelId();
        }
        return channelId;
    }


    @PreDestroy
    public void destroy() {
        if (socket != null) {

            socket.close();
        }
//        boolean remove = Boolean.parseBoolean(env.getProperty("remove.my,port.mapping.on.destroy"));
//        boolean removeOther = Boolean.parseBoolean(env.getProperty("remove.other.qne.port.mapping.on.destroy"));
//        if (remove) {
//            system.say("QPortManager Ports Closed on Destroy");
//            closeMyPorts();
//            if (removeOther) closeOtherQNEPorts();
//        }
        System.out.println("QPacketHandler Callback triggered - @PreDestroy.");
        System.out.println("Socket Closed");
    }


///////////////////////////////////////////////////////////////////////////////////////////////////
} /////// End Class ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////









//package com.qnenet.qne.packet;
//
//import com.qnenet.qne.network.endpoint.QEndPoint;
//import com.qnenet.qne.network.endpoint.QEndPointManager;
//import com.qnenet.qne.system.impl.QNEPaths;
//import com.qnenet.qne.system.utils.QThreadUtils;
//import org.apache.commons.io.FileUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.ShortBufferException;
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.SocketException;
//import java.nio.ByteBuffer;
//import java.nio.charset.Charset;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.NoSuchAlgorithmException;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutionException;
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Class /////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////@Service
//public class QPacketHandler {
//
//    private static final int DATAGRAM_BUFFER_SIZE = 64 * 1024;      // 64KB
//    private static final long RESPONSE_TIMEOUT = 2000L;
//    //    private static final long RESPONSE_TIMEOUT = 60 * 60 * 1000L; // 60 mins
//    private static final Integer MAX_TIMEOUTS = 3;
//
////    @Autowired
////    Environment env;
//
//    @Autowired
//    QEndPointManager endPointManager;
//
//    @Autowired
//    QNEPaths qnePaths;
//
//    private DatagramSocket socket;
//    private transient boolean isRunning;
//    private final Timer timer = new Timer(true);
//
//    private Map<Integer, Integer> packetTimeoutCount = new ConcurrentHashMap<>();
//    private Map<Integer, DatagramPacket> packetsByPacketId = new ConcurrentHashMap<>();
//    private final Map<Integer, TimerTask> timeoutTasks = new ConcurrentHashMap<>();
//    private boolean doSocketTimeOut;
//    private Thread listenThread;
//    private int port;
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Constructor ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public QPacketHandler() throws IOException {
//        Path userHomePath = Paths.get(System.getProperty("user.home"));
//        Path installPath = Paths.get(userHomePath.toString(), "QNEVaadin", "install");
//
////        init();
////    }
////
//////    @PostConstruct
////    public void init() throws IOException {
//        Path systemPortFilePath = Paths.get(installPath.toString(), "systemPort.prop");
//        String portString = FileUtils.readFileToString(systemPortFilePath.toFile(), Charset.defaultCharset());
//        port = Integer.valueOf(portString);
//        startListener();
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Send Packet ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void sendPacket(DatagramPacket packet) throws IOException {
//        QThreadUtils.showThreadName("Packet Handler sendPacket");
//        socket.send(packet);
//    }
//
////    private void showThreadName() {
////        String name = Thread.currentThread().getName();
////        System.out.println("Thread Name -> " + name);
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Receive Packet ////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void receivePacket(DatagramPacket packet) throws ShortBufferException, BadPaddingException, NoSuchAlgorithmException, IOException, InterruptedException, ExecutionException {
//        QThreadUtils.showThreadName("Packet Handler receivePacket");
//        byte[] packetData = packet.getData();
//        int length = packet.getLength(); // packetData length is 65k, we have to use packet.getLength
//        ByteBuffer packetBB = ByteBuffer.wrap(packetData, 0, length);
//        int endPointIdx = packetBB.getInt(0);
//        int channelId = packetBB.getInt(4);
//        int role = packetBB.getInt(8);
//        QEndPoint endPoint = endPointManager.getEndPointByIdx(endPointIdx);
//        endPoint.packetReceived(packet, channelId, role);
////        endPoint.handleReceivedPacket(packet);
//    }
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Listener //////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void startListener() {
//        doSocketTimeOut = false;
////        doSocketTimeOut = Boolean.parseBoolean(env.getProperty("do.socket.timeout"));
//        try {
//            socket = new DatagramSocket(port);
//        } catch (SocketException e) {
//            throw new RuntimeException(e);
//        }
//        listenThread = new Thread(() -> {
//            Thread.currentThread().setName("packet-handler-listen-thread");
//            try {
//                listen();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (ShortBufferException e) {
//                throw new RuntimeException(e);
//            } catch (BadPaddingException e) {
//                throw new RuntimeException(e);
//            } catch (NoSuchAlgorithmException e) {
//                throw new RuntimeException(e);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        listenThread.start();
//        isRunning = true;
//        System.out.println("LLL LLL LLL Socket listening on port -> " + port);
//    }
//
//
//    public void stopListener() {
//        if (listenThread != null) {
//            listenThread.interrupt();
//            listenThread = null;
//        }
//        if (socket != null && !socket.isClosed()) {
//            socket.close();
//            socket = null;
//        }
//        isRunning = false;
//        System.out.println("CCC CCC CCC Socket Closed");
//    }
//
//    //  Listen for incoming packages in a separate thread
//    private void listen() throws IOException, ShortBufferException, BadPaddingException, NoSuchAlgorithmException, InterruptedException {
//        try {
//            while (isRunning) {
//                // Wait for a packet
//                byte[] buffer = new byte[DATAGRAM_BUFFER_SIZE];
//                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//                socket.receive(packet); // blocked until packet received
//                QThreadUtils.showThreadName("PacketHandler Listen Loop");
//                receivePacket(packet);
//            }
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        } finally {
//            stopListener();
//        }
//    }
//
////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Getters & Setters /////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    private void badResult(String s) {
////        printMsg("BadResult -> " + s);
//////        makeSafe(rt);
////    }
////
////    private void printMsg(String msg) {
////        System.out.println(msg);
//////        log.info(msg);
////    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// TimeOut ////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    public void handleTimeout(QNetMsg netMsg) {
////        System.out.println("Timeout");
////
////        TimerTask task = (TimerTask) timeoutTasks.remove(netMsg.roundTripId);
////        if (task != null) {
////            task.cancel();
////        }
////        packetsByPacketId.remove(netMsg.roundTripId);
////
////        boolean resend = true;
////        Integer timeoutCount = packetTimeoutCount.get(netMsg.roundTripId);
////        if (timeoutCount == null) {
////            packetTimeoutCount.put(netMsg.roundTripId, 1);
////        } else if (timeoutCount < MAX_TIMEOUTS) {
////            packetTimeoutCount.put(netMsg.roundTripId, ++timeoutCount);
////        } else {
////            packetTimeoutCount.remove(netMsg.roundTripId);
////            resend = false;
////        }
////        if (resend) {
////            System.out.println("Resend");
//////            sendPacket(channelMsg.channel, channelMsg);
////        }
////
////    }
////
////    public void setPort(int port) {
////        this.port = port;
////    }
////
////
////-------------------------------------------------------------------------------------------------
////--------Timeout Class----------------------------------------------------------------------------
////-------------------------------------------------------------------------------------------------
//
////    /**
////     * Task that gets called by a separate thread if a timeout for a receiver occurs.
////     * //     * When a reply arrives this task must be canceled using the <code>cancel()</code>
////     * method inherited from <code>TimerTask</code>. In this case the caller is
////     * responsible for removing the task from the <code>tasks</code> map.
////     */
////    class QTimeoutTask extends TimerTask {
////
////        private final QNetMsg channelMsg;
////
////        public QTimeoutTask(QNetMsg channelMsg) {
////            this.channelMsg = channelMsg;
////        }
////
////        @Override
////        public void run() {
////            if (!isRunning) {
////                return;
////            }
////            handleTimeout(channelMsg);
////        }
////    }
//
////-------------------------------------------------------------------------------------------------
////--------- End Timeout Inner Class --------------------------------------------------------------
////-------------------------------------------------------------------------------------------------
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//} /////// End Class ///////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
