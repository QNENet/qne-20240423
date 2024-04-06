package com.qnenet.qne.objects.classes;


import com.qnenet.qne.system.utils.QFileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class QStoreInfo {
    public UUID uuid;
    public long qneGlobalId;
    public String storeName;
    public long timestamp;
    public String storePathStr;

    public transient Path storePath;

    public QStoreInfo(String storePathStr) {
        this.storePath = Paths.get(storePathStr);
        if (QFileUtils.checkDirectory(storePath)) {

        }
    }
}
