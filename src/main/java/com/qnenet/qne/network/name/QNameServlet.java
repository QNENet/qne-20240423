//package com.qnenet.qne.name;
//
//
//import com.qnenet.qne.network.known.QKnown;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.Servlet;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
////@Component(property = {"alias=/", "servlet-name=Name Servlet"})
//public class QNameServlet extends HttpServlet implements Servlet {
//
//    Logger log = LoggerFactory.getLogger(QNameServlet.class);
//
////    @Reference
////    QNode node;
//
//    @Reference
//    QKnown known;
//
//    @Reference
//    QSegmentServer segmentServer;
//    private int mySegmentId;
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////////
///////// Activate //////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @Activate
//    public void activate() throws IOException {
//        log.info("Hello from -> " + getClass().getSimpleName());
//    }
//
//    @Deactivate
//    public void deactivate() throws InterruptedException {
//        log.info("Goodbye from -> " + getClass().getSimpleName());
//    }
//
//
//    @Override
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        String servletPath = request.getServletPath();
//        System.out.println("Path Info -> " + servletPath);
//        String workPath = servletPath.replace("/", "");
//        if (workPath.isEmpty()) return;
//        if (workPath.startsWith("sw")) return;
//        if (workPath.startsWith("//sw-runtime-resources")) return;
//        if (workPath.length() > 20) return;
//        String[] split = workPath.split("-");
//        if (split.length != 2) return;
//        if (split[0].length() > 3) return;
//        if (split[1].length() > 16) return;
//        int segmentId = QBase36.qneNameToInt(split[0]);
//        if (segmentId > segmentServer.getLastSegmentId()) return;
//
//        String redirectString = null;
////        if (segmentId == mySegmentId) {
////            String segmentUrlStr = segmentServer..getNetAddressByName(split[1].toLowerCase());
////            redirectString = segmentUrlStr + servletPath;
////        } else {
////            String segmentUrlStr = known.getRandomRedirectURLforSegmentId(segmentId);
////            redirectString = segmentUrlStr + servletPath;
////        }
////        if (redirectString == null) return;
//
//        redirectString = "http://vaadin.com";
//
//
//
//
//
//
//
//        response.sendRedirect(redirectString);
////        response.sendRedirect(netAddress.getAsHTTPS());
//    }
//
//
////        try (PrintWriter writer = response.getWriter()) {
////            writer.println("<html>");
////            writer.println("<head>");
////            writer.println("<title>Example</title>");
////            writer.println("</head>");
////            writer.println("<body align='center'>");
////            writer.println("<h1>Example Servlet</h1>");
////            writer.println("</body>");
////            writer.println("</html>");
////        }
//}
//
//
//
