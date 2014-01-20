/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javafx.update;

import com.javafx.update.download.FileDownloadProgressPane;
import com.javafx.update.download.FileDownloadTask;
import com.javafx.update.model.FileData;
import com.javafx.update.tool.UpdateTool;
import com.javafx.update.tool.ZipTool;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Richard
 */
public class UpdateApp extends Application {
    
    private FileDownloadProgressPane indicator;
    private ProgressIndicator progress;
    private String localVerFileName = getProjectFolder() + "conf/version.ini";
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Updating...");
        Group root = new Group();
        GridPane g = new GridPane();
        
        indicator = new FileDownloadProgressPane();
        progress = new ProgressIndicator();
        progress.setPrefSize(80, 80);
        g.add(progress, 1, 1);
        g.add(indicator, 1, 2);
        
        g.setHgap(40);
        g.setVgap(40);
        
        root.getChildren().add(g);
        
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
        
        processUpdate();
    }
    
    private void processUpdate() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("localVersion", getNowVer(localVerFileName));
        params.put("localFolder", getProjectFolder());
        indicator.setVisible(true);
        progress.setVisible(true);
        final FileDownloadTask downloadTask = new FileDownloadTask("UpdateFileAction", params, indicator);
        
        new Thread(downloadTask).start();
        downloadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        indicator.setProgress(1);
                        indicator.setDisplayText("100%");
                    }
                });
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        indicator.setVisible(false);
                        progress.setVisible(false);
                    }
                });
                HashMap<String, Object> results = downloadTask.getValue();
                String result = (String) results.get("actionResult");
                if(result == null) {
                    JOptionPane.showMessageDialog(null, "No update zip!");
                    System.exit(0);
                }
                if (result.equalsIgnoreCase("failed")) {
                    JOptionPane.showMessageDialog(null, "Update Fail!");
                    System.exit(0);
                } else {
                    System.out.println("results : " + results);
                    processLocalFile(results);
                    JOptionPane.showMessageDialog(null, "Update Success");
                }
            }
        });
    }
    
    public void processLocalFile(HashMap<String, Object> results) {
        String localUpdatePath = "";
        String projectFolder = getProjectFolder();
        String updateVersion = (String) results.get("lastVersion");
        ArrayList<FileData> versions = (ArrayList<FileData>) results.get("files");
        for (FileData version : versions) {
            localUpdatePath = projectFolder + version.getFileName() + version.getExtension();
            //解压出来的文件不能包含中文，不然会报错
            ZipTool.unZip(localUpdatePath, projectFolder);
            deleteFile(localUpdatePath);
        }
        // UpdateLocalVerFile(localVerFileName, updateVersion);
        System.out.println("/n文件下载完成/n");
    }
    
    private void UpdateLocalVerFile(String localVerFileName, String updateVersion) {
        //把本地版本文件更新为网络同步
        FileWriter verOS = null;
        BufferedWriter bw = null;
        try {
            verOS = new FileWriter(localVerFileName);
            bw = new BufferedWriter(verOS);
            bw.write(updateVersion);
            bw.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
    
    private static String getNowVer(String LocalVerFileName) {
        return UpdateTool.getNowVer(LocalVerFileName);
    }

    /**
     * 得到工程文件路径
     *
     * @return
     * @throws java.lang.Exception
     */
    public static String getProjectFolder() {
        return UpdateTool.getProjectFolder();
    }

    //在Applicatin里面，main方法会直接被忽视掉，直接运行start方法
     public static void main(String[] args) {
        launch(args);
    }
}