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
import java.util.List;

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

        Timestamp dateTime = new Timestamp(2023 - 1900, 4, 28, 11, 0, 0, 0 );
        Timestamp dateTime2 = new Timestamp(2023 - 1900, 4, 28, 13, 0, 0, 0 );

        Booking booking1 = new Booking(-1,dateTime, dateTime2, "Test", "Test", "Test", 1, "Test", "Test");



        List<Booking> bookings = bookingDAO.getAll();

        for(Booking booking : bookings){
            System.out.println(booking.getStartTime());
            System.out.println(booking.getEndTime());
        }




        //bookingDAO.save(booking1);

        PDFMaker.HelloWordPDF();
       // MailSender.sendTestMail();

        launch();
    }
}