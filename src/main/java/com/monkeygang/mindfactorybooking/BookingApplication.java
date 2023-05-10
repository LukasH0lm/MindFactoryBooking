package com.monkeygang.mindfactorybooking;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class BookingApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("view/calendar-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mind Factory Booking - Admin");
        stage.getIcons().add(new javafx.scene.image.Image("file:src/main/resources/com/monkeygang/mindfactorybooking/logo.jpg"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}