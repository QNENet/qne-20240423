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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class QFileUtils {

    /*
     * returns true if dir created returns false if already exists
     */
    public static boolean checkDirectory(Path path) {
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
                return true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    public static File[] filesInDir(Path path) {
        return Objects.requireNonNull(path.toFile().listFiles());
    }

    public static File[] filesInDirSorted(Path path) {
        File[] files = Objects.requireNonNull(path.toFile().listFiles());
        Arrays.sort(files);
        return files;
    }

    public static int filesInDirCount(Path path) {
        return filesInDir(path).length;
    }

    public static void move(Path srcPath, Path destPath) {
        try {
            Files.move(srcPath,destPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path getLatestFileInDir(Path dir) {
        File[] files = filesInDir(dir);
        Arrays.sort(files);
        return files[files.length - 1].toPath();
    }


//    public static Path getQNEPath() {
//        String currentPathStr = Paths.get(".").toAbsolutePath().toString();
//        String[] parts = null;
////        Path userHomePath;
//        Path qnePath;
//        Path userHomePath;
//        if (System.getProperty("os.name").toLowerCase().contains("win")) {
//            parts = currentPathStr.split("\\\\");
//            userHomePath = Paths.get(parts[0], parts[1], parts[2]);
//            qnePath = Paths.get(userHomePath.toString(), parts[3]);
//        } else {
//            parts = currentPathStr.split("/");
//            userHomePath = Paths.get("/", parts[1], parts[2]);
//            qnePath = Paths.get(userHomePath.toString(), parts[3]);
//        }
//        return qnePath;
//    }
//
//    public static Path getSystemPath() {
//        return Paths.get(getQNEPath().toString(), "system");
//    }
//
//
//    public static boolean writeStringToFile(Path filePath, String value) {
//        try {
//            FileUtils.writeStringToFile(filePath.toFile(), value, StandardCharsets.UTF_8);
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//    }
//
//    public static String readStringFromFile(Path filePath) {
//        try {
//            return FileUtils.readFileToString(filePath.toFile(), StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            return null;
//        }
//    }
//
//    public static boolean writeBytesToFile(Path filePath, byte[] bytes) {
//        try {
//            FileUtils.writeByteArrayToFile(filePath.toFile(), bytes);
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//    }
//
//    public static byte[] readBytesFromFile(Path filePath) {
//        try {
//            return FileUtils.readFileToByteArray(filePath.toFile());
//        } catch (IOException e) {
//            return null;
//        }
//    }
//
//    public static boolean ifNotExists(Path path) {
//        if (Files.notExists(path)) {
//            try {
//                Files.createDirectories(path);
//                return true;
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//
//    public static void createFlagFile(Path path) {
//        try {
//            Files.createFile(path);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    public static void removeFlagFile(Path path) {
//        try {
//            Files.delete(path);
//        } catch (IOException e) {
//            // do nothing if not exists
//        }
//    }
//
//    public static boolean flagFileExists(Path path) {
//        return Files.exists(path);
//    }
//
//    public static ArrayList<String> readLines(Path path) {
//        // ArrayList<String> result = new ArrayList<>();
//        try {
//
//            FileReader fileReader = new FileReader(path.toFile());
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            ArrayList<String> lines = new ArrayList<>();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                lines.add(line);
//            }
//            bufferedReader.close();
//            return lines;
//
//        } catch (Exception e) {
//            return null;
//        }
//
//        // try (Stream<String> stream = Files.lines(path)) {
//        // stream.forEach((line) -> {
//        // result.add(line);
//        // });
//        // } catch (IOException e) {
//        // e.printStackTrace();
//        // }
//        // return result;
//    }
//
//    public static File[] listFilesInDir(Path path) {
//        return path.toFile().listFiles();
//    }
//
//    public static int subDirCount(Path path) {
//        int count = 0;
//        File[] files = path.toFile().listFiles();
//        for (File file: files) {
//           if (file.isDirectory()) count++;
//        }
//        return count;
//    }
//
//    public static List<String> getSortedListOfFileNamesInDirectory(Path dirPath) {
//        ArrayList<String> result = new ArrayList<String>();
//        File[] files = listFilesInDir(dirPath);
//        for (File file: files) {
//           result.add(file.getName());
//        }
//        Collections.sort(result);
//        return result;
//    }
//
//
//
//
//    public static List<String> getFilePathsRecursive(Path startPath) {
//        ArrayList<String> result = new ArrayList<>();
//        return getFileNamesInDirRecursive(result, startPath);
//    }
//
//    public static List<String> getFilePathsRecursiveByFileExtension(Path startPath, String extension) {
//        ArrayList<String> result = new ArrayList<>();
//        return getFileNamesInDirRecursiveByExt(result, startPath, extension);
//    }
//
//    public static List<String> getFileNamesInDirRecursive(List<String> fileNames, Path dir) {
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
//            for (Path path : stream) {
//                if (path.toFile().isDirectory()) {
//                    getFileNamesInDirRecursive(fileNames, path);
//                } else {
////                    String pathStr = path.toAbsolutePath().toString();
//                    String pathStr = path.toString();
//                    fileNames.add(pathStr);
////                    System.out.println(pathStr);
//                }
//            }
//        } catch (IOException e) {
//            fileNames.add(e.getMessage());
//            System.out.println(e.getMessage());
//        }
//        return fileNames;
//    }
//
//    public static List<String> getFileNamesInDirRecursiveByExt(List<String> fileNames, Path dir, String fileExtension) {
//        if (!fileExtension.startsWith(".")) fileExtension = ("." + fileExtension).toLowerCase();
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
//            for (Path path : stream) {
//                if (path.toFile().isDirectory()) {
//                    getFileNamesInDirRecursiveByExt(fileNames, path, fileExtension);
//                } else {
//                    String pathStr = path.toAbsolutePath().toString().toLowerCase();
//                    if (pathStr.endsWith(fileExtension)) {
//                        fileNames.add(pathStr);
//                    }
////                    System.out.println(path.getFileName());
//                }
//            }
//        } catch (IOException e) {
//            fileNames.add(e.getMessage());
//            System.out.println(e.getMessage());
////            e.printStackTrace();
//        }
//        return fileNames;
//    }
//
//
//    public static List<String[]> processCsv(Path path) {
//        CsvParserSettings settings = new CsvParserSettings();
//        // the file used in the example uses '\n' as the line separator sequence.
//        // the line separator sequence is defined here to ensure systems such as MacOS
//        // and Windows
//        // are able to process this file correctly (MacOS uses '\r'; and Windows uses
//        // '\r\n').
//        settings.getFormat().setLineSeparator("\n");
//        settings.setNullValue("<NULL>");
//
//        // creates a CSV parser
//        CsvParser parser = new CsvParser(settings);
//
//        // parses all rows in one go.
//        List<String[]> allRows = null;
//        try {
//            allRows = parser.parseAll(new FileReader(path.toFile()));
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return allRows;
//    }
//
////    public static void downloadFromUrl(URL url, Path path) throws IOException {
////        InputStream inputStream = null;
////        FileOutputStream fileOutputStream = null;
////
////        try {
////            URLConnection urlConn = url.openConnection();
////
////            inputStream = urlConn.getInputStream();
////            fileOutputStream = new FileOutputStream(path.toFile());
////
////            byte[] buffer = new byte[4096];
////            int length;
////
////            while ((length = inputStream.read(buffer)) > 0) {
////                fileOutputStream.write(buffer, 0, length);
////            }
////        } finally {
////            assert inputStream != null;
////            inputStream.close();
////            assert fileOutputStream != null;
////            fileOutputStream.close();
////        }
////    }
//
//        public static void systemDrives () {
//
//            File[] paths;
//            FileSystemView fsv = FileSystemView.getFileSystemView();
//
//// returns pathnames for files and directory
//            paths = File.listRoots();
//
//// for each pathname in pathname array
//            for (File path : paths) {
//                // prints file and directory paths
//                System.out.println("Drive Name: " + path);
//                System.out.println("Description: " + fsv.getSystemTypeDescription(path));
//            }
//        }
//
//
////        public static void main (String[] args){
//////        File[] roots = File.listRoots();
//////        for(File root: roots){
//////            System.out.println(root);
//////        }
////
//////        QFileUtils.systemDrives();
////
//////        String startPath = "/home/paulf";
////            String startPath = "/home";
////            long start = QDateTime.timeStampNow();
////            System.out.println("start -> " + start);
////            List<String> result = QFileUtils.getFilePathsRecursive(Paths.get(startPath));
////            long stop = QDateTime.timeStampNow();
////            System.out.println("stop -> " + stop);
////            long elapsed = (stop - start);
////            System.out.println("time taken secs -> " + elapsed);
////            System.out.println("number of entries -> " + result.size());
////        }
//

}
