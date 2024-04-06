package com.qnenet.qne.store.storesPrev.mapStore.api;

import com.qnenet.qne.objects.classes.QStoreObject;

import java.nio.file.Path;

public interface QMapStore {

    public void init(Path storePath, int processUpdatesCount);

    public boolean saveStoreObject(QStoreObject obj);

    QStoreObject loadStoreObject(int classId, Object id);

//    public Long getTotalObjCount();
//
//    public ArrayList<Object> getAllObjsWithClassId(int classId);

}
