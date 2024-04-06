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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class QPropertyUtils {

    public static Properties loadPropertiesFromFile(Path filePath) {
//        QFileUtils.checkDirectory(filePath.getParent());
        Properties properties = new Properties();
//        if (Files.notExists(filePath)) {
//            savePropertiesToFile(filePath, properties);
//        }
        try {
            properties.load(new FileInputStream(filePath.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;

    }

    public static boolean savePropertiesToFile(Path filePath, Properties properties) {
        QFileUtils.checkDirectory(filePath.getParent());
        try {
            properties.store(new FileOutputStream(filePath.toString()), null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Map<String, Object> propsToMap(Path propsPath) {
        HashMap<String, Object> map = new HashMap<>();
        Properties properties = loadPropertiesFromFile(propsPath);
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String key = (String) propertyNames.nextElement();
            map.put(key, properties.getProperty(key));
        }
        return map;
    }

    public static void addPropsToMap(Path propsPath, Map<String, String> map) {
        Properties properties = loadPropertiesFromFile(propsPath);
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String key = (String) propertyNames.nextElement();
            map.put(key, properties.getProperty(key));
        }
    }


}