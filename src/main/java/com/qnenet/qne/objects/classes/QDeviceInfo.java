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

public class QDeviceInfo extends QAbstractEntity {
    public boolean isHeadless;
    public int cpuCount;
    public int cpuSpeed;
    public int swapMemory;
    public int totalMemory;
    public int multipleProcessors;

    public String machineIpAddress;
//    public QEndPointInfo endPoint;

    public String platformName;
    public String systemLocale;
    public String osxMajorVersion;
    public String osxVersion;
    public String windowsOSName;

//    public QDeviceInfo() {
//        // kryo requires no param version
//    }
//
//
//
//    public QDeviceInfo(System system) {
//        Properties sysProps = system.getSysP
//        cpuCount = sysProps
//        cpuSpeed = nodeInfo.cpuSpeed; // Integer.valueOf(systemProps.getProperty("machine.cpu.speed"));
//        swapMemory = nodeInfo.swapMemory; //Integer.valueOf(systemProps.getProperty("machine.swap.memory"));
//        totalMemory = nodeInfo.totalMemory; //Integer.valueOf(systemProps.getProperty("machine.total.memory"));
//        multipleProcessors = nodeInfo.multipleProcessors; // Integer.valueOf(systemProps.getProperty("platform.has.multiple.processors"));
//        machineIpAddress = nodeInfo.machineIpAddress; //systemProps.getProperty("machine.ip.address");
//        netAddressPair = nodeInfo.netAddressPair;
//        platformName = nodeInfo.platformName; // systemProps.getProperty("platform.name");
//        systemLocale = nodeInfo.systemLocale; // systemProps.getProperty("system.locale");
//        isHeadless = nodeInfo.isHeadless; // QConvertUtils.getBooleanFromString(systemProps.getProperty(QSystemConstants.IS_HEADLESS));
//        osxMajorVersion = nodeInfo.osxMajorVersion; // osxMV;
//        osxVersion = nodeInfo.osxVersion; // osxV;
//        windowsOSName = nodeInfo.windowsOSName; // winOSName;
//    }
}