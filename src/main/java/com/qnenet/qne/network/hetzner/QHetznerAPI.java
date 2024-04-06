package com.qnenet.qne.network.hetzner;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class QHetznerAPI {

    static Logger log = LoggerFactory.getLogger(QHetznerAPI.class);

    @SuppressWarnings("unused")
    private static final String API_URL = "https://api.hetzner.cloud/v1";

//    private Path hetznerCloudTokenFilePath;

    // private String hcloudToken = "yuPd4DZ3etMIVznUNRYUbwk4nRIDbEHlIlvBsBEhg5DvcE8rVma94tMpRkrusmv5";
    // private HetznerCloudAPI hetznerAPI;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////// Activate ///////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    @Activate
    public void activate() {
        // hetznerAPI = new HetznerCloudAPI(hcloudToken);

        // List<Server> servers = hetznerAPI.getServers().getServers();
//         for (Server server:servers) {
//             // String name = server.getName();
// //            server.
//         }

//        hetznerAPI.data

//        Path hetznerPath = Paths.get(system.getNodeDataPath().toString(), "hetzner");
//        hetznerCloudTokenFilePath = Paths.get(hetznerPath.toString(), "api.token");
//
//
////        hetznerInfoPath = Paths.get(system.getRepositoryPath().toString(), "hetznerInfo");
////        hetznerInfoFilePath = Paths.get(hetznerInfoPath.toString(), "hetzner");
////        hetznerCloudTokenFilePath = Paths.get(hetznerInfoPath.toString(), "hetzner.token");
//        if (Files.exists(hetznerCloudTokenFilePath)) {
//            hcloudToken = (String) node.loadObjFromEncFile(hetznerCloudTokenFilePath);
//        }
//
////        if (QFileUtils.checkDirectory(hetznerInfoPath)) {
////            newSystem();
////        } else {
////            restart();
////        }
//
//
////        if (hcloudToken == null || hcloudToken.isBlank()) {
////            throw new RuntimeException("no Hetzner cloud token provided");
////        }
//
//
////        if (hcloudToken == null || hcloudToken.isBlank()) {
////            throw new RuntimeException("no Hetzner cloud token provided");
////        }
////
////        this.hcloudToken = hcloudToken;
////
////        httpClient = HttpClient.newHttpClient();
////
////        hetznerInfoPath = Paths.get(system.getRepositoryPath().toString(), "hetznerInfo");
////        hetznerInfoFilePath = Paths.get(hetznerInfoPath.toString(), "hetzner.info");
////        if (QFileUtils.checkDirectory(hetznerInfoPath)) {
////            newSystem();
////        } else {
////            restart();
////        }
////
        log.info("Hello from -> " + getClass().getSimpleName());
    }


//    @Deactivate
    public void deactivate() {

//        log.info("Goodbye from -> " + getClass().getSimpleName());
    }

    public void init() {

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////// New System /////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//     private void newSystem() {
// //        JFrame jFrame = new JFrame();
// //        hcloudToken = JOptionPane.showInputDialog(jFrame, "Enter Hetzner Token");
// //        ArrayList<QLocation> locations = getLocations().getLocations();


// //        createHetznerInfo();
// //        saveHetznerInfo();

//     }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////// Restart ////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//     private void restart() {
// //        loadHetznerInfo();
// //        hetznerAPI = new HetznerCloudAPI(hetznerInfo.cloudToken);
//     }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////// Persistence ////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    private void saveHetznerInfo() {
//        node.saveObjToFile(hetznerInfoFilePath, hetznerInfo);
//    }
//
//    // if started, stopped then started again without karaf being stopped loadHetznerInfo fails
//    // because the QHetznerInfo class has 2 class loaders.
//    private void loadHetznerInfo() {
//        Object obj = node.loadObjFromFile(hetznerInfoFilePath);
//        if (obj instanceof QHetznerInfo) {
//            hetznerInfo = (QHetznerInfo) obj;
//        } else {
//            System.out.println("Bad");
//        }
//    }

///////////////////////////////////////////////////////////////////////////////////////////////////
} /////// End Class ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
