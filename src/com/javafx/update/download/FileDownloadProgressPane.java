/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javafx.update.download;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Richard
 */
public class FileDownloadProgressPane extends StackPane implements ProgressChangeListener{
    private ProgressBar progressBar;
    private Label label;
    private DoubleProperty progressProperty;
    private String initText = "Start download file...";

    public FileDownloadProgressPane() {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: rgba(255, 255, 255, 1)");
        borderPane.setPadding(new Insets(20));

        label = new Label(initText);
        borderPane.setTop(label);

        progressBar = new ProgressBar(0);
        borderPane.setPrefSize(300, 100);
        borderPane.setMaxSize(300, 100);
        borderPane.setCenter(progressBar);

        progressBar.maxWidthProperty().bind(borderPane.widthProperty());

        this.setStyle("-fx-background-color: rgba(200, 200, 200, 0.4)");
        this.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (!t1) {
                    progressBar.setProgress(0);
                    label.setText(initText);
                }
            }
        });
        this.getChildren().add(borderPane);

        progressProperty = progressBar.progressProperty();
    }

    public DoubleProperty progressProperty() {
        return progressProperty;
    }

    public void setProgress(double progress) {
        this.progressBar.setProgress(progress);
    }

    public void setDisplayText(String text) {
        label.setText(text);
    }

    @Override
    public void progressChange(final ProgressData data) {
        data.calculatePercent();
        final double curProgress = data.getPercent() / 100;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(curProgress);
                label.setText("Download... " + (int)data.getPercent() + "%");
            }
        });
    }
}
