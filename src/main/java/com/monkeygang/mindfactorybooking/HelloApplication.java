package com.monkeygang.mindfactorybooking;

import com.itextpdf.text.DocumentException;
import com.monkeygang.mindfactorybooking.utility.MailSender;
import com.monkeygang.mindfactorybooking.utility.PDFMaker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mind Factory Booking - Admin");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException, DocumentException {

        PDFMaker.HelloWordPDF();
       // MailSender.sendTestMail();

        launch();
    }
}