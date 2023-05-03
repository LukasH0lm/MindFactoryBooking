package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.DAO.BookingDAO;
import com.monkeygang.mindfactorybooking.HelloApplication;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


import static com.monkeygang.mindfactorybooking.Controller.BookingController.isEdit;



public class CalendarController {

    private List<Booking> allBookings = new ArrayList<>();

    private final DecimalFormat df = new DecimalFormat("00.00");

    private final BookingDAO bookingDAO = new BookingDAO();

    public static Booking currentBooking;

    public double startTime = 07.00;

    public double heightPrLabel = 0.0;

    public double spacingPrLabel = 0.0;



    public CalendarController() throws SQLException, IOException {
    }


    public void initialize() throws SQLException {

        //mest for at teste om ui er "agilt"
        timeComboBox.setValue("18:00");
        timeComboBox.getItems().add("18:00");
        timeComboBox.getItems().add("24:00");
        timeComboBox.onActionProperty().setValue(e -> {
            generateCalendar(7);
        });

        // Vi sætter startdatoen til at være dagens dato
        datePicker.setValue(LocalDate.now());


        // Datoen skal konveretes til date.
        // det virker bøffet det her, hvorfor konvertere vi en dato til en dato?
        LocalDate localDate = datePicker.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        datePicker.onActionProperty().setValue(e -> {

            loadBookings();

        });



        // Vi skal finde ugenummeret af datoen.
        //og nu laver vi en ny Dato???
        //vi burde have et enkelt object til at få datoen og ugenummeret.
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

        generateCalendar(calenderStartTime);


        // Vi sætter ugenummeret til at være ugenummeret for den valgte dato, hver gang der sker ændringer i datePicker

        loadBookings();

    }


    private void generateCalendar(double calenderStartTime) {


        double calendarEndTime = Double.parseDouble(timeComboBox.getValue().replace(":", "."));
        double timeLabelsHeight = 0.0;


        vBoxTid.getChildren().clear();

        //har lavet det om til et for loop fordi vi gør noget for hvert trin
        //er der ikke en smartere måde at iterate i gennem labelsne?
        for (double i = calenderStartTime; i <= calendarEndTime; i++) {

            // Vi laver flere labels med tidspunkterne indtil vi når 18.00
            Label label = new Label(df.format(i));

            // Tilføjer labels til vBoxTid
            vBoxTid.getChildren().add(label);




        }

        for (Node node : vBoxTid.getChildren()) {

            if (node.getClass() == Label.class) {

                Label label = (Label) node;

                // Vi tilføjer højden af labelen til timeLabelsHeight, så vi kan sætte højden på hBoxCalendar og vBoxTid

                label.setPrefWidth(30);
                label.setPrefHeight(15);
                label.setMaxWidth(label.getPrefWidth());
                label.setMaxHeight(label.getPrefHeight());
                label.setMinWidth(label.getPrefWidth());
                label.setMinHeight(label.getPrefHeight());


                Line timeLine = new Line();
                //timeLine.setStartX(0);
                //timeLine.setStartY(0);
                timeLine.setEndX(hBoxCalendar.getPrefWidth());
                timeLine.setLayoutX(hBoxCalendar.getLayoutX());
                timeLine.setLayoutY(hBoxCalendar.getLayoutY() + timeLabelsHeight);
                timeLine.setStroke(Color.rgb(169,169, 169));


                calendarAnchorPane.getChildren().add(timeLine);


                timeLabelsHeight += label.getPrefHeight();
                timeLabelsHeight += vBoxTid.getSpacing();

                heightPrLabel = label.getPrefHeight();





            }

        }


        //Vi sætter højden på hBoxCalendar og vBoxTid, samt linjerne, som opdeler vores kalender.
        // Vi minusser med vBoxTid.getSpacing(), da når der er spacing i en vBox, så bliver der tilføjet spacing efter det sidste element, og vi vil have at kalenderen slutter ved det sidste tidspunkt.
        hBoxCalendar.setPrefHeight(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        hBoxCalendar.setMaxHeight(hBoxCalendar.getPrefHeight());
        hBoxCalendar.setMinHeight(hBoxCalendar.getPrefHeight());


        // ikke fast værdi her (15) - SW
        vBoxTid.setPrefHeight(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        vBoxTid.setMaxHeight(vBoxTid.getPrefHeight());
        vBoxTid.setMinHeight(vBoxTid.getPrefHeight());


        hBoxLineOne.setEndY(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        hBoxLineTwo.setEndY(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        hBoxLineThree.setEndY(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        hBoxLineFour.setEndY(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        hBoxLineFive.setEndY(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        hBoxLineSix.setEndY(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));


    }



    private void loadBookings() {

        spacingPrLabel = vBoxTid.getSpacing();

        // Datoen skal konveretes til date. //hvorfor?
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

            System.out.println("bookingDateWeek " + bookingDateWeek);
            System.out.println("WeekOnAction " + weekOnAction);

            if (bookingDateWeek == weekOnAction) {

                double RectangleHeight = (booking.getEndTime().getHours() - booking.getStartTime().getHours()) * (spacingPrLabel + heightPrLabel);
                double RectangleYStartPosition = (booking.getStartTime().getHours() - startTime) * (spacingPrLabel + heightPrLabel);


                Rectangle bookingRectangle = new Rectangle(50, RectangleHeight);
                bookingRectangle.setFill(Color.RED);
                bookingRectangle.setY(RectangleYStartPosition);

                bookingInitialize(bookingRectangle, booking);


                switch (booking.getStartTime().getDay()) {
                    case 1 -> paneMandag.getChildren().add(bookingRectangle);
                    case 2 -> paneTirsdag.getChildren().add(bookingRectangle);
                    case 3 -> paneOnsdag.getChildren().add(bookingRectangle);
                    case 4 -> paneTorsdag.getChildren().add(bookingRectangle);
                    case 5 -> paneFredag.getChildren().add(bookingRectangle);
                    case 6 -> paneLordag.getChildren().add(bookingRectangle);
                    case 0 -> paneSondag.getChildren().add(bookingRectangle);
                }


            }

        }


    }


    public void bookingInitialize(Rectangle bookingRectangle, Booking booking) {
        bookingRectangle.setOnMouseClicked(event -> {
            currentBooking = booking;


            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() == 2) {
                    isEdit = true;
                    loadBookingUI();


                }
            }


        });


    }

    private void loadBookingUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/booking-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Booking");
            stage.setScene(new Scene(root));
            stage.setAlwaysOnTop(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void previousWeekButtonPressed() {

        datePicker.setValue(datePicker.getValue().minusDays(7));


    }

    public void nextWeekButtonPressed() {

        datePicker.setValue(datePicker.getValue().plusDays(7));
    }


    @FXML
    AnchorPane calendarAnchorPane;

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

    @FXML
    private ComboBox<String> timeComboBox;


}