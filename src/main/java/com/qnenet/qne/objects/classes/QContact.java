package com.qnenet.qne.objects.classes;//
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

import java.util.UUID;

public class QContact extends QAbstractEntity {

    private int localId;
    private String localNickName;
    private String entityName; // person or business display name
    private String mainMobile;
    private String mainEmail;
    private long locationGeoNameId;

    private UUID memberUUID; // null if not a endpoint
    private byte[] photoBytes; // jpeg

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getLocalNickName() {
        return localNickName;
    }

    public void setLocalNickName(String localNickName) {
        this.localNickName = localNickName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getMainMobile() {
        return mainMobile;
    }

    public void setMainMobile(String mainMobile) {
        this.mainMobile = mainMobile;
    }

    public String getMainEmail() {
        return mainEmail;
    }

    public void setMainEmail(String mainEmail) {
        this.mainEmail = mainEmail;
    }

    public long getLocationGeoNameId() {
        return locationGeoNameId;
    }

    public void setLocationGeoNameId(long locationGeoNameId) {
        this.locationGeoNameId = locationGeoNameId;
    }

    public UUID getMemberUUID() {
        return memberUUID;
    }

    public void setMemberUUID(UUID memberUUID) {
        this.memberUUID = memberUUID;
    }

    public byte[] getPhotoBytes() {
        return photoBytes;
    }

    public void setPhotoBytes(byte[] photoBytes) {
        this.photoBytes = photoBytes;
    }
}
