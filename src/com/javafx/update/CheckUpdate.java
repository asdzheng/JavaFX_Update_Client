/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javafx.update;

import com.javafx.update.tool.ClientRequestDatasTool;
import com.javafx.update.tool.UpdateTool;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class CheckUpdate extends Application {
    
    //version.ini record the version data in projectPath/conf
    private static String LocalVerFileName;

    public static boolean isUpdate() {
        LocalVerFileName = getProjectFolder() + "conf/version.ini";
        boolean isUpdated = false;
        String isUpdateStr = "";
        System.out.println("LocalVerFileName ：" + LocalVerFileName + "| localversion : " + getNowVer(LocalVerFileName));
        //send local version data to server
        HashMap<String, Object> requestParams = new HashMap<String, Object>();
        requestParams.put("localVersion", getNowVer(LocalVerFileName));
        HashMap<String, String> resposeParams = ClientRequestDatasTool.getDatas(requestParams);
        if (resposeParams != null && requestParams.size() > 0) {
            isUpdateStr = resposeParams.get("isUpdateStr");
            if (isUpdateStr.equalsIgnoreCase("true")) {
                isUpdated = true;
            }
        }
        return isUpdated;

    }
    
    private static String getNowVer(String LocalVerFileName) {
        return UpdateTool.getNowVer(LocalVerFileName);
    }

    /**
     * get project path
     * @return
     * @throws java.lang.Exception
     */
    private static String getProjectFolder() {
        return UpdateTool.getProjectFolderPath();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        FlowPane flow = new FlowPane();
        flow.setAlignment(Pos.CENTER);
        root.setCenter(flow);

        Hyperlink link = new Hyperlink("check update");
        flow.getChildren().addAll(link );

        link.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(isUpdate()) {
                    try {
                        new UpdateApp().start(new Stage());
                        stage.hide();
                    } catch (Exception ex) {
                        Logger.getLogger(CheckUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        Scene scene = new Scene(root, 600, 800);

        stage.setScene(scene);
        stage.show();
       
    }
}
