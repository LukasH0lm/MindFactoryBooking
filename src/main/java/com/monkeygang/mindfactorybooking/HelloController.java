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

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.util.*;


public class HelloController {

    public List<Booking> allBookings = new ArrayList<>();

    private final DecimalFormat df = new DecimalFormat("00.00");

    BookingDAO bookingDAO = new BookingDAO();

    public HelloController() throws SQLException, IOException {
    }


    public void initialize() throws SQLException {


        // Vi sætter startdatoen til at være dagens dato
        datePicker.setValue(LocalDate.now());


        // Datoen skal konveretes til date.
        LocalDate localDate = datePicker.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Vi skal finde ugenummeret af datoen.
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        // vi sætter ugenummert til at være ugenummeret for den valgte dato, når programmet starter.
        ugeLabel.setText("Uge " + week);



        // Vi får alle bookings fra databasen, og ligger dem i listen.
        try {
            allBookings.addAll(bookingDAO.getAll());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }


        // Vi sætter decimalformatet til at være med punktum i stedet for komma, da vi skal bruge det til at lave tidspunkter
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));


        double calenderStartTime = 07.00;
        double calendarEndTime = 18.00;
        double timeLabelsHeight = 0.0;


        double currentTime = calenderStartTime;

        while (currentTime <= calendarEndTime) {
            // Vi laver flere labels med tidspunkterne indtil vi når 18.00
            Label label = new Label(df.format(currentTime));
            label.setPrefWidth(30);
            label.setPrefHeight(15);
            label.setMaxWidth(label.getPrefWidth());
            label.setMaxHeight(label.getPrefHeight());
            label.setMinWidth(label.getPrefWidth());
            label.setMinHeight(label.getPrefHeight());

            // Tilføjer labels til vBoxTid
            vBoxTid.getChildren().add(label);

            // Vi tilføjer 1.00 til tiden, så vi kan lave labels for hver  time
            currentTime += 1.00;


            // Vi tilføjer højden af labelen til timeLabelsHeight, så vi kan sætte højden på hBoxCalendar og vBoxTid
            timeLabelsHeight += label.getPrefHeight();
            timeLabelsHeight += vBoxTid.getSpacing();


        }


        //Vi sætter højden på hBoxCalendar og vBoxTid, samt linjerne, som opdeler vores kalender.
        // Vi minusser med vBoxTid.getSpacing(), da når der er spacing i en vBox, så bliver der tilføjet spacing efter det sidste element, og vi vil have at kalenderen slutter ved det sidste tidspunkt.
        hBoxCalendar.setPrefHeight(timeLabelsHeight - vBoxTid.getSpacing());
        hBoxCalendar.setMaxHeight(hBoxCalendar.getPrefHeight());
        hBoxCalendar.setMinHeight(hBoxCalendar.getPrefHeight());


        vBoxTid.setPrefHeight(timeLabelsHeight - vBoxTid.getSpacing());
        vBoxTid.setMaxHeight(vBoxTid.getPrefHeight());
        vBoxTid.setMinHeight(vBoxTid.getPrefHeight());


        hBoxLineOne.setEndY(timeLabelsHeight - vBoxTid.getSpacing());
        hBoxLineTwo.setEndY(timeLabelsHeight - vBoxTid.getSpacing());
        hBoxLineThree.setEndY(timeLabelsHeight - vBoxTid.getSpacing());
        hBoxLineFour.setEndY(timeLabelsHeight - vBoxTid.getSpacing());
        hBoxLineFive.setEndY(timeLabelsHeight - vBoxTid.getSpacing());
        hBoxLineSix.setEndY(timeLabelsHeight - vBoxTid.getSpacing());


        // Vi sætter ugenummeret til at være ugenummeret for den valgte dato, hver gang der sker ændringer i datePicker


        datePickerInitialize((int) calenderStartTime);

        datePicker.setValue(LocalDate.now());

    }


    public void datePickerInitialize(int startTime) {

        final int spacingPrLabel = (int) vBoxTid.getSpacing();
        final int heightPrLabel = 15;


        datePicker.setOnAction(event -> {

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

            for (Booking booking : allBookings) {


                Date bookingDate = booking.getStartTime();

                System.out.println(bookingDate);


                // Vi skal finde ugenummeret af datoen.
                Calendar calBookingDate = Calendar.getInstance();
                calBookingDate.setTime(bookingDate);
                int bookingDateWeek = calBookingDate.get(Calendar.WEEK_OF_YEAR);


                LinkedList<Booking> datesThatMatchesWeek = new LinkedList<>();

                System.out.println("bookingDateWeek " + bookingDateWeek);
                System.out.println("WeekOnAction " + weekOnAction);

                if (bookingDateWeek == weekOnAction) {
                    datesThatMatchesWeek.add(booking);

                    Integer RectangleHeight = (booking.getEndTime().getHours() - booking.getStartTime().getHours()) * (spacingPrLabel + heightPrLabel);
                    Integer RectangleYStartPosition = ((booking.getStartTime().getHours() - startTime) * (spacingPrLabel + heightPrLabel));


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


    public void previousWeekButtonPressed() {

        datePicker.setValue(datePicker.getValue().minusDays(7));


    }

    public void nextWeekButtonPressed() {

        datePicker.setValue(datePicker.getValue().plusDays(7));
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