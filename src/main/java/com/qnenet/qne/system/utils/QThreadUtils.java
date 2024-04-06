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

package com.qnenet.qne.system.utils;


import org.apache.commons.lang3.ThreadUtils;

public class QThreadUtils {

    public static void displayAllThreads() {
        for (Thread t : ThreadUtils.getAllThreads()) {
            System.out.println(t.getName() + ", " + t.isDaemon());
        }
    }

    public static void setThreadName(String name) {
        Thread.currentThread().setName(name);
        showThreadName("Set Thread Name : " + name);
    }

    public static void showThreadName(String threadLocation) {
        String threadName = Thread.currentThread().getName();
        if (threadName == null || threadName.isEmpty()) threadName = "No Name";
        System.out.println("Thread Name :: " + threadLocation + " -> " + threadName);
//        System.out.println(threadLocation + " : Thread Name -> " + threadName);
    }

}

