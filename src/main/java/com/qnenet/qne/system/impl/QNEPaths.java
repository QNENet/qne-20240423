package com.qnenet.qne.system.impl;

import com.qnenet.qne.system.constants.QSysConstants;
import com.qnenet.qne.system.utils.QFileUtils;
import com.qnenet.qne.system.utils.QSecurityUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class QNEPaths {
    private Path userHomePath;
    private Path qnePath;
    private Path systemPath;
    private Path knownPath;
    private Path tmpPath;
//    private final Path workPath;
//    private final Path nodePath;
    private Path endPointsPath;
    private Path installPath;
    private Path installSystemPropsFilePath;
    private Path sysPropsMapFilePath;
//    private final Path endPointRestartInfoListFilePath;

    //    private final Path keystorePath;
    private Path keystoreFilePath;
    private Path installerPropsFilePath;
    //    private final Path discoveryRepositoryPath;
    private Path repositoryPath;


    public Path getUserHomePath() {
        return userHomePath;
    }

    public Path getInstallPath() {
        return installPath;
    }

    public Path getInstallSystemPropsFilePath() {
        return installSystemPropsFilePath;
    }

    public Path getSysPropsMapFilePath() {
        return sysPropsMapFilePath;
    }


    @PostConstruct
    public void postConstruct() {

//    public QNEPaths() {
        userHomePath = Paths.get(System.getProperty("user.home"));
        qnePath = Paths.get(userHomePath.toString(), QSysConstants.APP_NAME);

        systemPath = Paths.get(qnePath.toString(), "system");
        knownPath = Paths.get(systemPath.toString(), "known");
        endPointsPath = Paths.get(systemPath.toString(), "endPoints");
        tmpPath = Paths.get(systemPath.toString(), "tmp");
        repositoryPath = Paths.get(qnePath.toString(), "repository");
        keystoreFilePath = Paths.get(repositoryPath.toString(), "keystore.p12");

        Paths.get(qnePath.toString(), "discoveryServer");


        installPath = Paths.get(qnePath.toString(), "install");
        installSystemPropsFilePath = Paths.get(installPath.toString(), "installer.props");

        installerPropsFilePath = Paths.get(System.getProperty("user.home"), QSysConstants.APP_NAME, "install", "installer.props");

        sysPropsMapFilePath = Paths.get(systemPath.toString(), "sysProps.map");


    }


    public Path getQnePath() {
        return qnePath;
    }

    public Path getSystemPath() {
        return systemPath;
    }

    public Path getKeystoreFilePath() {
        return keystoreFilePath;
    }

    public Path getEndPointsPath() {
        return endPointsPath;
    }

    public Path getInstallerPropsFilePath() {
        return installerPropsFilePath;
    }

    public boolean checkDirectory(Path path) {
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Path getTmpPath() {
        return tmpPath;
    }

    public Path createTmpPath() {
        Path path = Paths.get(tmpPath.toString(),
                new String(QSecurityUtils.generateLittlePassword(6)));
        if (QFileUtils.checkDirectory(path)) {
            return path;
        }
        return null;
    }

//    public Path getDiscoveryRepositoryPath() {
//        return discoveryRepositoryPath;
//    }

    public Path getRepositoryPath() {
        return repositoryPath;
    }



    public Path getKnownPath() {
        return knownPath;
    }
}
