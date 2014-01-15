package com.javafx.update.download;


/**
 * 这个接口用于上传文件时监听进度变化信息
 *
 * @author Richard
 */
public interface ProgressChangeListener {

    public void progressChange(ProgressData data);
}
