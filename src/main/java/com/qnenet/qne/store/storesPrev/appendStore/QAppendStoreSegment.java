/*
 * Copyright (C) 2022 Paul F Fraser <paulf@qnenet.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qnenet.qne.store.storesPrev.appendStore;

import com.qnenet.qne.objects.classes.QAbstractEntity;
import org.jasypt.util.binary.AES256BinaryEncryptor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public interface QAppendStoreSegment {

    void initNewAppendStoreSegment(QAppendStore parentStore, Path segmentFilePath, String rafMode, int capacity,
                                   long baseObjId, long segmentId, AES256BinaryEncryptor encryptor) throws IOException;

    void init(QAppendStore parentStore,
              Path segmentFilePath,
              String rafMode,
              int capacity,
              AES256BinaryEncryptor encryptor) throws IOException;

    Long addObject(Object obj) ;

    QAbstractEntity getObject(long hugeId) throws IOException;

    void getObjectsForClassId(int classId, ArrayList<Object> list) throws IOException;

    long getRafSize() throws IOException;

    long getSegmentId();

    Long getNextObjId();

    long getSegmentLastObjId();

    long getBaseObjId();

    public void closeRaf();

    Path getSegmentFilePath();

    int getCapacity();

}
