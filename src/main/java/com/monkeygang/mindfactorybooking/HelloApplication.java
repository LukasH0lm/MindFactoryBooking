package com.monkeygang.mindfactorybooking;

import com.itextpdf.text.DocumentException;
import com.monkeygang.mindfactorybooking.DAO.BookingDAO;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import com.monkeygang.mindfactorybooking.utility.MailSender;
import com.monkeygang.mindfactorybooking.utility.PDFMaker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mind Factory Booking - Admin");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException, DocumentException, SQLException {

        BookingDAO bookingDAO = new BookingDAO();

        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        Timestamp dateTime2 = new Timestamp(System.currentTimeMillis() + 3600000);

        Booking booking = new Booking(1, dateTime, dateTime2, "EASV", "IT", "Erik", 10,  "12345678", "Doctor");

        bookingDAO.save(booking);

        PDFMaker.HelloWordPDF();
       // MailSender.sendTestMail();

        launch();
    }
}