package com.javafx.update.model;

import java.io.Serializable;

public class FileData implements Serializable {

    private String fileName;
    private String filePath;
    private String localFolder;
    private String extension;
    private long fileSize;
    private boolean isNull;

    public boolean isNull() {
        return isNull;
    }

    public void setIsNull(boolean isNull) {
        this.isNull = isNull;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getLocalFolder() {
        return localFolder;
    }

    public void setlocalFolder(String localFolder) {
        this.localFolder = localFolder;
    }
}
