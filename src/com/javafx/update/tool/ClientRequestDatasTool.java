package com.javafx.update.tool;

import com.javafx.update.download.ProgressChangeListener;
import com.javafx.update.download.ProgressData;
import com.javafx.update.model.FileData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * the tool for download file and get datas from server
 */
public class ClientRequestDatasTool {

    public static HashMap<String, Object> requestDownloadFile(HashMap<String, Object> params, ProgressChangeListener changeListener) throws ClassNotFoundException {

        HashMap<String, Object> results = null;
        //this request is get the file parameters
        params.put("type", "parameters");
        try {
            URL url = new URL("http://localhost:8080/JavaFX_Update_Server/FileDownloadServlet");
            URLConnection openConnection = url.openConnection();
            openConnection.setDoOutput(true);
            openConnection.setDoInput(true);
            openConnection.setRequestProperty("Content-Type", "application/octet-stream");

            ObjectOutputStream oos = new ObjectOutputStream(openConnection.getOutputStream());
            oos.writeObject(params);
            oos.flush();
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(openConnection.getInputStream());
            results = (HashMap<String, Object>) ois.readObject();
            ois.close();

            String result = (String) results.get("actionResult");
            if (!result.equalsIgnoreCase("failed")) {
                results = getDownloadResult(params, results, changeListener);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return results;
    }

    private static HashMap<String, Object> getDownloadResult(HashMap<String, Object> params, HashMap<String, Object> results, ProgressChangeListener changeListener) {
        String result = "success";
        String allFileSizeStr = (String) results.get("allFileSize");
        long allFileSize = Long.parseLong(allFileSizeStr);
        ArrayList<FileData> files = (ArrayList<FileData>) results.get("files");
        //this request is download file
        params.put("type", "bytes");

        if (allFileSize == 0) {
            results.put("actionResult", "failed");
            return results;
        }

        for (FileData fileData : files) {
            if (!fileData.isNull()) {
                String filePath = fileData.getFilePath();
                String fileName = fileData.getFileName();
                String fileFolder = fileData.getLocalFolder();
                String extension = fileData.getExtension();

                params.put("filePath", filePath);
                result = startDownload(fileFolder, params, fileName, extension, allFileSize, changeListener);
                if (result.equalsIgnoreCase("failed")) {
                    break;
                }
            }
        }
        results.put("actionResult", result);
        return results;
    }

    private static String startDownload(String fileFolder, HashMap<String, Object> params,
            String fileName, String extension, long allFileSize, ProgressChangeListener changeListener) {
        String result = "success";
        try {
            URL url = new URL("http://localhost:8080/JavaFX_Update_Server/FileDownloadServlet");
            URLConnection openConnection = url.openConnection();
            openConnection.setDoOutput(true);
            openConnection.setDoInput(true);
            openConnection.setRequestProperty("Content-Type", "application/octet-stream");

            ObjectOutputStream oos = new ObjectOutputStream(openConnection.getOutputStream());
            oos.writeObject(params);
            oos.flush();
            oos.close();

            result = processFile(openConnection, fileFolder, fileName, extension, allFileSize, changeListener);
        } catch (IOException ex) {
            Logger.getLogger(ClientRequestDatasTool.class.getName()).log(Level.SEVERE, null, ex);
            result = "failed";
        }
        return result;
    }
    //update progress date
    private static String processFile(URLConnection openConnection, String fileFolder, String fileName,
            String extension, long allFileSize, ProgressChangeListener changeListener) {
        String result = "success";
        long totalBytesRead = 0;
        InputStream is = null;
        FileOutputStream fos = null;

        File file = new File(fileFolder + fileName + extension);
        createFolder(file.getParent());
        try {
            is = openConnection.getInputStream();
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 1024];
            int bytesRead = -1;
            ProgressData progress = new ProgressData();
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                progress.setCurSize(totalBytesRead);
                progress.setTotalSize(allFileSize);
                changeListener.progressChange(progress);
            }
            is.close();
            fos.close();
        } catch (IOException ex) {
            result = "failed";
            Logger.getLogger(ClientRequestDatasTool.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }
    
    public static HashMap<String, String> getDatas(HashMap<String, Object> requestParams) {
        HashMap<String, String> results = null;
        try {
            URL url = new URL("http://localhost:8080/JavaFX_Update_Server/CheckUpdateServlet");
            URLConnection openConnection = url.openConnection();
            openConnection.setDoOutput(true);
            openConnection.setDoInput(true);
            openConnection.setRequestProperty("Content-Type", "application/octet-stream");

            ObjectOutputStream oos = new ObjectOutputStream(openConnection.getOutputStream());
            oos.writeObject(requestParams);
            oos.flush();
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(openConnection.getInputStream());
            results = (HashMap<String, String>) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return results;
    }

    public static boolean createFolder(String path) {
        if (path == null || path.trim().equals("")) {
            return false;
        }

        File folder = new File(path);
        if (!folder.exists()) {
            return folder.mkdirs();
        }

        return false;
    }
}
