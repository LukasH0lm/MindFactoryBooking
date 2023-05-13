package com.monkeygang.mindfactorybooking.utility;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertHandler {

    public void showAlert(Stage stage, String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        disableScene(stage, alert);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void disableScene(Stage stage, Alert alert) {

        stage.setAlwaysOnTop(false);
        stage.setOpacity(0.5);

        alert.setOnCloseRequest((e) -> {
            stage.setAlwaysOnTop(true);
            stage.setOpacity(1);
        });


    }

}
