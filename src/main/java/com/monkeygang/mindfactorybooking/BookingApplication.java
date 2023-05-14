package com.monkeygang.mindfactorybooking;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

        //to make all threads shutdown upon close
        //note that this is a very brutal way to close threads
        //won't work if we have a temporary booking we want to delete before the program closes
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });


        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}