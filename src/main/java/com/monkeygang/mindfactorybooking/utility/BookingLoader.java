package com.monkeygang.mindfactorybooking.utility;

import com.monkeygang.mindfactorybooking.Controller.CalendarController;
import com.monkeygang.mindfactorybooking.DAO.BookingDAO;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class BookingLoader {


    private List<Booking> allBookings = new ArrayList<>();

    private final DecimalFormat df = new DecimalFormat("00.00");

    private final BookingDAO bookingDAO = new BookingDAO();

    public double startTime = 07.00;

    public double heightPrLabel = 0.0;

    public double spacingPrLabel = 0.0;


    CalendarController controller;

    public BookingLoader(CalendarController controller) throws SQLException, IOException {

        this.controller = controller;

        this.ugeLabel = controller.getUgeLabel();

        this.datePicker = controller.getDatePicker();

        allBookings = bookingDAO.getAll();

    }

    public void bookingInitialize(Rectangle bookingRectangle, Booking booking) {
        bookingRectangle.setOnMouseClicked(event -> {
            CurrentBookingSingleton.getInstance().setCurrentBooking(booking);


            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() == 2) {

                    CurrentBookingSingleton.getInstance().setCurrentBooking(booking);

                    CurrentBookingSingleton.getInstance().setIsEdit(true);

                    controller.loadBookingUI();


                }
            }

        });

    }

    private void createSingleBooking(Booking booking) {

        double RectangleHeight = (booking.getEndTime().getHours() - booking.getStartTime().getHours()) * (spacingPrLabel + heightPrLabel) + (booking.getEndTime().getMinutes() * (spacingPrLabel + heightPrLabel) / 60);
        double RectangleYStartPosition = (booking.getStartTime().getHours() - startTime) * (spacingPrLabel + heightPrLabel);


        Rectangle bookingRectangle = new Rectangle(50, RectangleHeight);
        bookingRectangle.setFill(Color.RED);
        bookingRectangle.setY(RectangleYStartPosition);

        bookingInitialize(bookingRectangle, booking);


        switch (booking.getStartTime().getDay()) {
            case 1 -> controller.getPaneMandag().getChildren().add(bookingRectangle);
            case 2 -> controller.getPaneTirsdag().getChildren().add(bookingRectangle);
            case 3 -> controller.getPaneOnsdag().getChildren().add(bookingRectangle);
            case 4 -> controller.getPaneTorsdag().getChildren().add(bookingRectangle);
            case 5 -> controller.getPaneFredag().getChildren().add(bookingRectangle);
            case 6 -> controller.getPaneLordag().getChildren().add(bookingRectangle);
            case 0 -> controller.getPaneSondag().getChildren().add(bookingRectangle);
        }

    }

    private void createSingleBookingFixedValues(Booking booking, Timestamp bookingStartTime, Timestamp bookingEndTime) {

        double RectangleHeight = (bookingEndTime.getHours() - bookingStartTime.getHours()) * (spacingPrLabel + heightPrLabel) + (bookingEndTime.getMinutes() * (spacingPrLabel + heightPrLabel) / 60);
        double RectangleYStartPosition = (bookingStartTime.getHours() - startTime) * (spacingPrLabel + heightPrLabel);

        Rectangle bookingRectangle = new Rectangle(50, RectangleHeight);
        bookingRectangle.setFill(Color.RED);
        bookingRectangle.setY(RectangleYStartPosition);


        bookingInitialize(bookingRectangle, booking);

        switch (bookingStartTime.getDay()) {
            case 1 -> controller.getPaneMandag().getChildren().add(bookingRectangle);
            case 2 -> controller.getPaneTirsdag().getChildren().add(bookingRectangle);
            case 3 -> controller.getPaneOnsdag().getChildren().add(bookingRectangle);
            case 4 -> controller.getPaneTorsdag().getChildren().add(bookingRectangle);
            case 5 -> controller.getPaneFredag().getChildren().add(bookingRectangle);
            case 6 -> controller.getPaneLordag().getChildren().add(bookingRectangle);
            case 0 -> controller.getPaneSondag().getChildren().add(bookingRectangle);
        }

    }


    private void createBookingMultipleDays(Booking booking) {

        int daysBetweenStartdateAndEndDate = booking.getEndTime().getDate() - booking.getStartTime().getDate();
        int currentDay = booking.getStartTime().getDate();

        // vi burde nok bruge endtime her fra kalenderen

        //vi laver start dagen her, da vi skal have et bestemt tidspunkt, hvor dagen starter.
        Timestamp firstDayEndTime = new Timestamp(booking.getStartTime().getYear(), booking.getStartTime().getMonth(), booking.getStartTime().getDate(), 18, 00, 00, 00);

        createSingleBookingFixedValues(booking, booking.getStartTime(), firstDayEndTime);

        System.out.println("test" + booking.getStartTime());

        currentDay++;

        //Vi laver dagene i mellem start dato og slut dato.
        //Vi skal ikke gå igennem denne loop, hvis der ikke er booket mere end 2 dage, da vi laver start og slut dag uden for loopen.
        //Vi minusser med 2, da vi ikke skal tælle de 2 dage med som vi laver ude for loopen.
        if (daysBetweenStartdateAndEndDate > 1) {
            for (int i = 0; i <= daysBetweenStartdateAndEndDate - 2; i++) {

                Timestamp fillerStart = new Timestamp(booking.getStartTime().getYear(), booking.getStartTime().getMonth(), currentDay, 7, 00, 00, 00);
                Timestamp fillerEnd = new Timestamp(booking.getEndTime().getYear(), booking.getEndTime().getMonth(), currentDay, 18, 00, 00, 00);

                createSingleBookingFixedValues(booking, fillerStart, fillerEnd);

                currentDay++;
            }
        }


        // vi burde nok bruge starttime her fra kalenderen

        //Vi laver slutdagen her, da vi skal have bestem tidspunkt for slutningen af dagen.
        Timestamp lastDayStartTime = new Timestamp(booking.getEndTime().getYear(), booking.getEndTime().getMonth(), currentDay, 7, 00, 00, 00);

        Timestamp lastDayEndTime = new Timestamp(booking.getEndTime().getYear(), booking.getEndTime().getMonth(), currentDay, booking.getEndTime().getHours(), booking.getEndTime().getMinutes(), booking.getEndTime().getSeconds(), booking.getEndTime().getNanos());

        createSingleBookingFixedValues(booking, lastDayStartTime, lastDayEndTime);


    }


    public void loadBookings() {

        double spacingPrLabel = controller.getvBoxTid().getSpacing();

        // Datoen skal konveretes til date. //hvorfor?
        LocalDate localDateOnAction = datePicker.getValue();
        Date dateOnAction = Date.from(localDateOnAction.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Vi skal finde ugenummeret af datoen.
        Calendar calOnAction = Calendar.getInstance();
        calOnAction.setTime(dateOnAction);
        int weekOnAction = calOnAction.get(Calendar.WEEK_OF_YEAR);
        int yearOnAction = calOnAction.get(Calendar.YEAR);


        ugeLabel.setText("Uge " + weekOnAction);


        controller.getPaneMandag().getChildren().clear();
        controller.getPaneTirsdag().getChildren().clear();
        controller.getPaneOnsdag().getChildren().clear();
        controller.getPaneTorsdag().getChildren().clear();
        controller.getPaneFredag().getChildren().clear();
        controller.getPaneLordag().getChildren().clear();
        controller.getPaneSondag().getChildren().clear();

        for (Booking booking : allBookings) {


            Date bookingDate = booking.getStartTime();

            System.out.println(bookingDate);


            // Vi skal finde ugenummeret af datoen.
            Calendar calBookingDate = Calendar.getInstance();
            calBookingDate.setTime(bookingDate);
            int bookingDateWeek = calBookingDate.get(Calendar.WEEK_OF_YEAR);

            System.out.println("bookingDateWeek " + bookingDateWeek);
            System.out.println("WeekOnAction " + weekOnAction);


            // vi ligger 1900 til, da Timestamp er underlig D:D:D:D:
            if (bookingDateWeek == weekOnAction && booking.getStartTime().getYear() + 1900 == yearOnAction) {


                if (!(booking.getStartTime().getDay() == booking.getEndTime().getDay())) {

                    createBookingMultipleDays(booking);

                } else {
                    createSingleBooking(booking);

                }


            }

        }


    }

    private Label ugeLabel;
    private DatePicker datePicker;

}
