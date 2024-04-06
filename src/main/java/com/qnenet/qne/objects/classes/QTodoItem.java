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

package com.qnenet.qne.objects.classes;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class QTodoItem extends QAbstractEntity {

    public String category; // private, social, business
    public String type; // appointment, event, to-do
    public String subType; // medical.gp medical.specialist hospital.admission
    public LocalDateTime startLocalDateTime;
    public LocalDateTime endLocalDateTime;
//    public long timeEpochSecs;
    public int durationSecs;
    public boolean durationIsAccurate;
    public int contactId;
    public String location;
    public String memo;
    public ArrayList<String> completionStage;
    public String todoItemType;

    public QTodoItem() {
    }
}
