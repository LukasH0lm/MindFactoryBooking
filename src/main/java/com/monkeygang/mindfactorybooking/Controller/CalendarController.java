package com.monkeygang.mindfactorybooking.Controller;

import com.itextpdf.text.DocumentException;
import com.monkeygang.mindfactorybooking.DAO.BookingDAO;
import com.monkeygang.mindfactorybooking.BookingApplication;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.utility.PDFMaker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;



public class CalendarController {

    private List<Booking> allBookings = new ArrayList<>();

    private final DecimalFormat df = new DecimalFormat("00.00");

    private final BookingDAO bookingDAO = new BookingDAO();

    public double startTime = 07.00;

    public double heightPrLabel = 0.0;

    public double spacingPrLabel = 0.0;



    public CalendarController() throws SQLException, IOException {
    }



    public void initialize() throws SQLException, IOException {

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

            try {
                loadBookings();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

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
                timeLine.setOpacity(0.5);


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

    private void loadBookings() throws SQLException, IOException {

        allBookings = bookingDAO.getAll();

        spacingPrLabel = vBoxTid.getSpacing();

        // Datoen skal konveretes til date. //hvorfor?
        LocalDate localDateOnAction = datePicker.getValue();
        Date dateOnAction = Date.from(localDateOnAction.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Vi skal finde ugenummeret af datoen.
        Calendar calOnAction = Calendar.getInstance();
        calOnAction.setTime(dateOnAction);
        int weekOnAction = calOnAction.get(Calendar.WEEK_OF_YEAR);
        int yearOnAction = calOnAction.get(Calendar.YEAR);


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



            // vi ligger 1900 til, da Timestamp er underlig D:D:D:D:
            if (bookingDateWeek == weekOnAction && booking.getStartTime().getYear() + 1900 == yearOnAction) {



                if (!(booking.getStartTime().getDay() == booking.getEndTime().getDay())){

                    createBookingMultipleDays(booking);

                }
                else{
                    createSingleBooking(booking);

                }



            }

        }


    }


    private void createSingleBooking(Booking booking){

        double rectangleHeight = (booking.getEndTime().getHours() - booking.getStartTime().getHours()) * (spacingPrLabel + heightPrLabel) + (booking.getEndTime().getMinutes() * (spacingPrLabel + heightPrLabel) / 60);
        double rectangleYStartPosition = (booking.getStartTime().getHours() - startTime) * (spacingPrLabel + heightPrLabel);


        StackPane stack = generateBookingStack(booking, rectangleHeight, rectangleYStartPosition);


        switch (booking.getStartTime().getDay()) {
            case 1 -> paneMandag.getChildren().add(stack);
            case 2 -> paneTirsdag.getChildren().add(stack);
            case 3 -> paneOnsdag.getChildren().add(stack);
            case 4 -> paneTorsdag.getChildren().add(stack);
            case 5 -> paneFredag.getChildren().add(stack);
            case 6 -> paneLordag.getChildren().add(stack);
            case 0 -> paneSondag.getChildren().add(stack);
        }

    }

    private void createSingleBookingFixedValues(Booking booking, Timestamp bookingStartTime, Timestamp bookingEndTime){

        double RectangleHeight = (bookingEndTime.getHours() - bookingStartTime.getHours()) * (spacingPrLabel + heightPrLabel) + (bookingEndTime.getMinutes() * (spacingPrLabel + heightPrLabel) / 60);
        double RectangleYStartPosition = (bookingStartTime.getHours() - startTime) * (spacingPrLabel + heightPrLabel);


        StackPane stack = generateBookingStack(booking, RectangleHeight, RectangleYStartPosition);

        switch (bookingStartTime.getDay()) {
            case 1 -> paneMandag.getChildren().add(stack);
            case 2 -> paneTirsdag.getChildren().add(stack);
            case 3 -> paneOnsdag.getChildren().add(stack);
            case 4 -> paneTorsdag.getChildren().add(stack);
            case 5 -> paneFredag.getChildren().add(stack);
            case 6 -> paneLordag.getChildren().add(stack);
            case 0 -> paneSondag.getChildren().add(stack);
        }

    }


    private void createBookingMultipleDays(Booking booking){

        int daysBetweenStartdateAndEndDate = booking.getEndTime().getDate() - booking.getStartTime().getDate();
        int currentDay = booking.getStartTime().getDate();

        // vi burde nok bruge endtime her fra kalenderen

        //vi laver start dagen her, da vi skal have et bestemt tidspunkt, hvor dagen starter.
        Timestamp firstDayEndTime = new Timestamp(booking.getStartTime().getYear(), booking.getStartTime().getMonth(), booking.getStartTime().getDate(), 18, 00, 00, 00 );

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
        Timestamp lastDayStartTime = new Timestamp(booking.getEndTime().getYear(), booking.getEndTime().getMonth(), currentDay, 7, 00, 00, 00 );

        Timestamp lastDayEndTime = new Timestamp(booking.getEndTime().getYear(), booking.getEndTime().getMonth(), currentDay, booking.getEndTime().getHours(), booking.getEndTime().getMinutes(), booking.getEndTime().getSeconds(), booking.getEndTime().getNanos());

        createSingleBookingFixedValues(booking, lastDayStartTime, lastDayEndTime);


    }


    public StackPane generateBookingStack(Booking booking, double rectangleHeight, double rectangleYStartPosition){

        Rectangle bookingRectangle = new Rectangle(50, rectangleHeight);
        bookingRectangle.setFill(Color.RED);

        Label bookingLabel = new Label(booking.getOrganisation());

        bookingLabel.setAlignment(Pos.CENTER);
        bookingLabel.setTextFill(Paint.valueOf("white"));

        StackPane stack = new StackPane(bookingRectangle, bookingLabel);

        stack.setLayoutY(rectangleYStartPosition);

        bookingInitialize(bookingRectangle, booking , stack);

        return stack;

    }




    //opens the booking the rectangle is representing
    public void bookingInitialize(Rectangle bookingRectangle, Booking booking, StackPane stack) {
        stack.setOnMouseClicked(event -> {
            CurrentBookingSingleton.getInstance().setCurrentBooking(booking);


            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() == 2) {

                    CurrentBookingSingleton.getInstance().setCurrentBooking(booking);

                    CurrentBookingSingleton.getInstance().setIsEdit(true);

                    loadBookingUI();


                }
            }


        });


    }

    private void loadBookingUI() {



        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("view/booking-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Booking");
            stage.setScene(new Scene(root));
            stage.setAlwaysOnTop(true);

            Stage currentStage = (Stage) calendarAnchorPane.getScene().getWindow();


            stage.setOnCloseRequest(event -> {

                currentStage.setOpacity(1);

                try {
                    loadBookings();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            currentStage.setOpacity(0.5);

            stage.show();





        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onSearchButtonClick(){

        for (Booking booking : allBookings){
            if (booking.hashCode() == Integer.parseInt(SearchTextField.getText())){
                CurrentBookingSingleton.getInstance().setCurrentBooking(booking);
                CurrentBookingSingleton.getInstance().setIsEdit(true);
                loadBookingUI();
            }
        }

    }

    public void onCreateButtonClick(){
        CurrentBookingSingleton.getInstance().setIsEdit(false);
        loadBookingUI();
    }



    public void previousWeekButtonPressed() {

        datePicker.setValue(datePicker.getValue().minusDays(7));


    }

    public void nextWeekButtonPressed() {

        datePicker.setValue(datePicker.getValue().plusDays(7));
    }

    public void createPDF() throws DocumentException, IOException {
        PDFMaker.HelloWordPDF();
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

    @FXML
    private TextField SearchTextField;


}