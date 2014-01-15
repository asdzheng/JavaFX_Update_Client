package com.javafx.update.tool;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/*
 * 压缩文件夹
 */
public class ZipTool {

    private String currentZipFilePath;
    private String sourceFilePath;
    private String pathHead;
    private ZipOutputStream zos;
    private FileInputStream fis;

    public ZipTool(String sourceFilePath, String currentZipFilePath) {
        try {
            this.sourceFilePath = sourceFilePath;
            this.currentZipFilePath = currentZipFilePath;
            pathHead = this.sourceFilePath.substring(0, this.sourceFilePath.lastIndexOf("/"));
            zos = new ZipOutputStream(new FileOutputStream(this.currentZipFilePath));
            //设定文件压缩级别
            zos.setLevel(9);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ZipTool.unZip("D://v140.b2.zip", "D://");
    }

    public static void unZip(String zipfile, String destDir) {
        unZip(zipfile, destDir, null);
    }

    public static void unZip(String zipfile, String destDir, String reName) {
//        destDir = destDir.endsWith("/") ? destDir : destDir + "/";
        byte b[] = new byte[1024];
        int length;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(new File(zipfile));
            Enumeration enumeration = zipFile.entries();
            ZipEntry zipEntry = null;
            while (enumeration.hasMoreElements()) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                File loadFile;
                if (!isEmpty(reName)) {
                    loadFile = new File(destDir + reName);
                } else {
                    loadFile = new File(destDir + zipEntry.getName());
                }
                if (zipEntry.isDirectory()) {
                    loadFile.mkdirs();
                } else {
                    if (!loadFile.getParentFile().exists()) {
                        loadFile.getParentFile().mkdirs();
                    }
                    OutputStream outputStream = new FileOutputStream(loadFile);
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    while ((length = inputStream.read(b)) > 0) {
                        outputStream.write(b, 0, length);
                    }

                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                }
            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmpty(Object value) {
        if (value == null || "".equals(value.toString().trim())) {
            return true;
        } else {
            return false;
        }
    }
}
