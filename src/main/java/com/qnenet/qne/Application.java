package com.qnenet.qne;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.Collections;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.qnenet.qne.system.constants.QSysConstants;
import com.qnenet.qne.system.utils.QNetworkUtils;
import com.qnenet.qne.system.utils.QThreadUtils;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// // import com.vaadin.flow.server.PWA;
// // import com.vaadin.flow.theme.Theme;

// @SpringBootApplication
// // @Theme("qneTheme")
// // @PWA(name = "Quick N Easy", shortName = "QNE", offlinePath = "offline.html", offlineResources = {
// // 		"images/offline.png" })
// public class QneApplication {

// 	public static void main(String[] args) {
// 		SpringApplication.run(QneApplication.class, args);
// 	}

// }

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@SpringBootApplication
@Theme(value = "qneTheme", variant = Lumo.LIGHT)
@PWA(
        name = "Quick N Easy",
        shortName = "QNE",
        offlinePath = "offline.html",
        offlineResources = {"images/offline.png"}
)
public class Application implements AppShellConfigurator {

    private static SpringApplication app;
    private static ConfigurableApplicationContext appCtx;

    @SuppressWarnings("rawtypes")
    @Bean
    public TomcatProtocolHandlerCustomizer tomcatProtocolHandlerCustomizer() {
        return handler -> handler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }


    public static void main(String[] args) throws IOException {
        runApp();
        appCtx = app.run(args);
    }

    private static void runApp() {
        QThreadUtils.showThreadName("Application");
        Security.addProvider(new BouncyCastleProvider());
        // Path installPath = Paths.get(System.getProperty("user.home"),
        //         QSysConstants.APP_NAME, "install");
        // Path systemPortFilePath = Paths.get(installPath.toString(), "systemPort.prop");

        // String portInfoStr = null;
        // try {
        //     if (Files.notExists(systemPortFilePath)) {
        //         portInfoStr = String.valueOf(QNetworkUtils.getFreePort());
        //         FileUtils.writeStringToFile(systemPortFilePath.toFile(), portInfoStr, Charset.defaultCharset());
        //     } else {
        //         portInfoStr = FileUtils.readFileToString(systemPortFilePath.toFile(), Charset.defaultCharset());
        //     }
        // } catch (IOException e) {
        //     throw new RuntimeException(e);
        // }
        // String portStr = portInfoStr.split(",")[0].trim();

        app = new SpringApplication(Application.class);
 /*        app.setHeadless(false);
        app.setDefaultProperties(Collections.singletonMap("server.port", portStr));
  */   }

    static void shutdown() {
        if (appCtx.isActive()) appCtx.close();
    }

    static void restart() {
        var args = appCtx.getBean(ApplicationArguments.class);
        Thread thread = new Thread(() -> {
            runApp();
            appCtx = app.run(args.getSourceArgs());

        });
        thread.setDaemon(false);
        thread.start();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
}  /////////// End Class //////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////