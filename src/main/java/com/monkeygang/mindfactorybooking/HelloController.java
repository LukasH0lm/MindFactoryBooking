package com.monkeygang.mindfactorybooking;

import com.monkeygang.mindfactorybooking.DAO.BookingDAO;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.util.*;


public class HelloController {

    private final DecimalFormat df = new DecimalFormat("00.00");

    BookingDAO bookingDAO = new BookingDAO();

    public HelloController() throws SQLException {
    }

    public void previousWeekButtonPressed() {

        datePicker.setValue(datePicker.getValue().minusDays(7));

    }

    public void nextWeekButtonPressed() {

        datePicker.setValue(datePicker.getValue().plusDays(7));
    }


    public void initialize() throws SQLException {


        // Vi sætter startdatoen til at være dagens dato
        datePicker.setValue(LocalDate.now());

        // Vi sætter decimalformatet til at være med punktum i stedet for komma, da vi skal bruge det til at lave tidspunkter
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));

        ugeLabel.setText("Uge " + datePicker.getValue().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));


        double time = 07.00;

/*
        while (time <= 18.00) {
            // Vi laver flere labels med tidspunkterne indtil vi når 18.00
            Label label = new Label(df.format(time));
            label.setPrefWidth(30);
            label.setPrefHeight(15);
            label.setMaxWidth(label.getPrefWidth());
            label.setMaxHeight(label.getPrefHeight());
            label.setMinWidth(label.getPrefWidth());
            label.setMinHeight(label.getPrefHeight());

            // Tilføjer labels til vBoxTid
            vBoxTid.getChildren().add(label);

            // Vi tilføjer 1.00 til tiden, så vi kan lave labels for hver  time
            time += 1.00;


            // Vi tilføjer højden af labelen til timeLabelsHeight, så vi kan sætte højden på hBoxCalendar og vBoxTid
            timeLabelsHeight += label.getPrefHeight();
            timeLabelsHeight += vBoxTid.getSpacing();


        }*/


        //Vi sætter højden på hBoxCalendar og vBoxTid, samt linjerne, som opdeler vores kalender.
        // Vi minusser med vBoxTid.getSpacing(), da når der er spacing i en vBox, så bliver der tilføjet spacing efter det sidste element, og vi vil have at kalenderen slutter ved det sidste tidspunkt.



        datePickerInitialize();


    }


    public void datePickerInitialize(){

        final int spacingPrLabel = (int) vBoxTid.getSpacing();
        final double heightPrLabel = 18;

        // Vi sætter ugenummeret til at være ugenummeret for den valgte dato, hver gang der sker ændringer i datePicker
        datePicker.setOnAction(event -> {

            List allBookings = null;
            try {
                allBookings = bookingDAO.getAll();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Datoen skal konveretes til date.
            LocalDate localDateOnAction = datePicker.getValue();
            Date dateOnAction = Date.from(localDateOnAction.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Vi skal finde ugenummeret af datoen.
            Calendar calOnAction = Calendar.getInstance();
            calOnAction.setTime(dateOnAction);
            int weekOnAction = calOnAction.get(Calendar.WEEK_OF_YEAR);


            ugeLabel.setText("Uge " + weekOnAction);


            paneMandag.getChildren().clear();
            paneTirsdag.getChildren().clear();
            paneOnsdag.getChildren().clear();
            paneTorsdag.getChildren().clear();
            paneFredag.getChildren().clear();
            paneLordag.getChildren().clear();
            paneSondag.getChildren().clear();

            for (Object object : allBookings) {

                Booking booking;

                //not necessary to check if object is a booking, since we know it is
                //but it's good practice
                if (object.getClass() == Booking.class) {

                    booking = (Booking) object;
                }else {
                    System.out.println("object is not a booking");
                    return;
                }


                Date bookingDate =  booking.getStartTime();

                System.out.println(bookingDate);


                // Vi skal finde ugenummeret af datoen.
                Calendar calBookingDate = Calendar.getInstance();
                calBookingDate.setTime(bookingDate);
                int bookingDateWeek = calBookingDate.get(Calendar.WEEK_OF_YEAR);


                LinkedList<Object> datesThatMatchesWeek = new LinkedList<>();

                System.out.println("bookingDateWeek " + bookingDateWeek);
                System.out.println("WeekOnAction " + weekOnAction);

                if (bookingDateWeek == weekOnAction) {
                    datesThatMatchesWeek.add(booking);

                    Double RectangleHeight = (booking.getEndTime().getHours() - booking.getStartTime().getHours()) * (spacingPrLabel + heightPrLabel);
                    Double RectangleYStartPosition = ((booking.getStartTime().getHours() - 7) * (spacingPrLabel + heightPrLabel));


                    Rectangle bookingRectangle = new Rectangle(50, RectangleHeight);

                    bookingRectangle.setFill(Color.RED);


                    bookingRectangle.setY(RectangleYStartPosition);

                    System.out.println(booking.getStartTime().getDay());

                    if (booking.getStartTime().getDay() == 1) {
                        paneMandag.getChildren().add(bookingRectangle);

                    }
                    if (booking.getStartTime().getDay() == 2) {
                        paneTirsdag.getChildren().add(bookingRectangle);

                    }
                    if (booking.getStartTime().getDay() == 3) {
                        paneOnsdag.getChildren().add(bookingRectangle);

                    }
                    if (booking.getStartTime().getDay() == 4) {
                        paneTorsdag.getChildren().add(bookingRectangle);

                    }
                    if (booking.getStartTime().getDay() == 5) {
                        paneFredag.getChildren().add(bookingRectangle);

                    }
                    if (booking.getStartTime().getDay() == 6) {
                        paneLordag.getChildren().add(bookingRectangle);

                    }
                    if (booking.getStartTime().getDay() == 0) {
                        paneSondag.getChildren().add(bookingRectangle);
                    }


                }


            }


        });

    }


    @FXML
    private DatePicker datePicker;

    @FXML
    private Label ugeLabel;
    @FXML
    private HBox hBoxCalendar;

    @FXML
    private Line hBoxLineFive;

    @FXML
    private Line hBoxLineFour;

    @FXML
    private Line hBoxLineOne;

    @FXML
    private Line hBoxLineSix;

    @FXML
    private Line hBoxLineThree;


    @FXML
    private Button nextWeekButton;

    @FXML
    private Button previousWeekButton;

    @FXML
    private Line hBoxLineTwo;


    @FXML
    private Pane paneFredag;

    @FXML
    private Pane paneLordag;

    @FXML
    private Pane paneMandag;

    @FXML
    private Pane paneOnsdag;

    @FXML
    private Pane paneSondag;

    @FXML
    private Pane paneTirsdag;

    @FXML
    private Pane paneTorsdag;


    @FXML
    private VBox vBoxTid;


}