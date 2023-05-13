package com.monkeygang.mindfactorybooking.utility;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class SceneChanger {


    public void changeScene(Scene scene,Parent container, String viewName, boolean isForward) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/com/monkeygang/mindfactorybooking/view/" + viewName + "-view.fxml"));


        if (isForward) {
            root.translateXProperty().set(scene.getWidth());
        } else {
            root.translateXProperty().set(-scene.getWidth());
        }


        StackPane parentContainer = (StackPane) scene.getRoot();

        parentContainer.getChildren().add(root);



        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> {
            parentContainer.getChildren().remove(container);
        });
        timeline.play();

    }




}
