package com.javafx.update.download;


import com.javafx.update.tool.ClientRequestDatasTool;
import java.util.HashMap;
import javafx.concurrent.Task;

/**
 * 
 * asynchronous download task
 */
public class FileDownloadTask extends Task<HashMap<String, Object>> {


    private HashMap<String, Object> params;
    private FileDownloadProgressPane changeListener;

    public FileDownloadTask(HashMap<String, Object> params, FileDownloadProgressPane changeListener) {
        this.params = params;
        this.changeListener = changeListener;
    }

    @Override
    protected HashMap<String, Object> call() throws Exception {
        updateProgress(0d, 1d);
        ProgressChangeListener listener = new ProgressChangeListener() {
            @Override
            public void progressChange(ProgressData data) {
                changeListener.setVisible(true);
                data.calculatePercent();
                double curProgress = data.getPercent() / 100;
                updateProgress(curProgress, 1);
                if (changeListener != null) {
                    changeListener.progressChange(data);
                }
            }
        };
        HashMap<String, Object> results = new HashMap<String, Object>();
        results = ClientRequestDatasTool.requestDownloadFile(params,listener);
        return results;
    }
}
