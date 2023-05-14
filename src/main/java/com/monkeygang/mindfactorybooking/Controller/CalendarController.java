package com.monkeygang.mindfactorybooking.Controller;

import com.itextpdf.text.DocumentException;
import com.monkeygang.mindfactorybooking.Dao.BookingDao;
import com.monkeygang.mindfactorybooking.BookingApplication;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.Objects.Organization;
import com.monkeygang.mindfactorybooking.utility.PDFMaker;

import com.monkeygang.mindfactorybooking.utility.RectangleYPositionComparator;
import javafx.application.Platform;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class CalendarController {

    private List<Booking> allBookings = new ArrayList<>();

    private final DecimalFormat df = new DecimalFormat("00.00");

    private final BookingDao bookingDAO = new BookingDao();

    public double uiStartTime = 07.00;

    public double heightPrLabel = 0.0;

    public double spacingPrLabel = 0.0;

    //TODO: shutdown threads on close

    ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newCachedThreadPool();
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


        // updates ui every 5 seconds
        initializeUpdatingThread();





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

        System.out.println("Loading bookings...");

        // The way we do it ensures we get a booking with a customer with a organisation
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



            // Vi skal finde ugenummeret af datoen.
            Calendar calBookingDate = Calendar.getInstance();
            calBookingDate.setTime(bookingDate);
            int bookingDateWeek = calBookingDate.get(Calendar.WEEK_OF_YEAR);



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

        fillCalendarWithBlankSquares();



    }


    private void createSingleBooking(Booking booking) throws SQLException, IOException {

        double rectangleHeight = (booking.getEndTime().getHours() - booking.getStartTime().getHours()) * (spacingPrLabel + heightPrLabel) + (booking.getEndTime().getMinutes() * (spacingPrLabel + heightPrLabel) / 60);
        double rectangleYStartPosition = (booking.getStartTime().getHours() - uiStartTime) * (spacingPrLabel + heightPrLabel);


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

    private void createSingleBookingFixedValues(Booking booking, Timestamp bookingStartTime, Timestamp bookingEndTime) throws SQLException, IOException {

        double RectangleHeight = (bookingEndTime.getHours() - bookingStartTime.getHours()) * (spacingPrLabel + heightPrLabel) + (bookingEndTime.getMinutes() * (spacingPrLabel + heightPrLabel) / 60);
        double RectangleYStartPosition = (bookingStartTime.getHours() - uiStartTime) * (spacingPrLabel + heightPrLabel);


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


    private void createBookingMultipleDays(Booking booking) throws SQLException, IOException {

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


    public StackPane generateAvailableBookingStack(Booking booking, double rectangleHeight, double rectangleYStartPosition) {

        Rectangle bookingRectangle = new Rectangle(50, rectangleHeight);
        bookingRectangle.setFill(Color.TRANSPARENT);

        StackPane stack = new StackPane(bookingRectangle);

        stack.setLayoutY(rectangleYStartPosition);
        stack.setPrefHeight(rectangleHeight);

        bookingCreate(bookingRectangle, booking , stack);

        return stack;

    }

    public StackPane generateBookingStack(Booking booking, double rectangleHeight, double rectangleYStartPosition) throws SQLException, IOException {

        Rectangle bookingRectangle = new Rectangle(50, rectangleHeight);
        bookingRectangle.setFill(Color.RED);

        Organization currentOrganization = bookingDAO.getOrganisation(booking);

        Label bookingLabel = new Label(currentOrganization.getName());

        bookingLabel.setAlignment(Pos.CENTER);

        bookingLabel.setTextFill(Paint.valueOf("white"));
        bookingLabel.setMaxWidth(45); // 50 - 5 to give the text to space to breathe
        bookingLabel.setWrapText(true);

        StackPane stack = new StackPane(bookingRectangle, bookingLabel);

        stack.setLayoutY(rectangleYStartPosition);
        stack.setPrefHeight(rectangleHeight);

        bookingInitialize(bookingRectangle, booking , stack);

        return stack;

    }




    //opens the booking the rectangle is representing
    public void bookingInitialize(Rectangle bookingRectangle, Booking booking, StackPane stack) {

        //bookingRectangle isnt used anymore
        //we should discuss if we want to keep it or not

        stack.setOnMouseClicked(event -> {
            CurrentBookingSingleton.getInstance().setCurrentBooking(booking);


            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() == 2) {

                    CurrentBookingSingleton.getInstance().setCurrentBooking(booking);
                    CurrentBookingSingleton.getInstance().setCustomer(booking.getCustomer());
                    CurrentBookingSingleton.getInstance().setCurrentOrganization(booking.getCustomer().getOrganisation());

                    CurrentBookingSingleton.getInstance().setIsEdit(true);

                    loadBookingUI();


                }
            }


        });


    }


    public void bookingCreate(Rectangle bookingRectangle, Booking booking, StackPane stack) {

        stack.setOnMouseClicked(event -> {

            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() == 2) {

                    try {
                        CurrentBookingSingleton.getInstance().reset();
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    loadBookingUI();


                }
            }


        });


    }

    public void fillCalendarWithBlankSquares()  {

        // Vi vil gerne have usynlige rektangler, der hvor der ikke er nogen bookings registreret
        // Det vil vi gerne, så man kan trykke i de firkanter, og derved lave en booking der.
        // Grunden til det er fordi så kan vi få tidsrummet, som er frit ud fra de tomme firkanters længde, ligesom vi gør i resten af ui.


        // Vi laver en liste af vores panes, så vi kan loope igennem dem.
        LinkedList<Pane> panesForEachDay = new LinkedList<>();

        // Vi skal bruge listen her til at tilføje rektanglerne fra hvert pane.
        LinkedList<StackPane> eksisterendeRektanglerForHvertPane = new LinkedList<>();


        LocalDate selectedDate = datePicker.getValue();

        LocalDate nearestMonday = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        int dayOfMonth = nearestMonday.getDayOfMonth();


        // Vi adder vores panes til listen
        panesForEachDay.add(paneMandag);
        panesForEachDay.add(paneTirsdag);
        panesForEachDay.add(paneOnsdag);
        panesForEachDay.add(paneTorsdag);
        panesForEachDay.add(paneFredag);
        panesForEachDay.add(paneLordag);
        panesForEachDay.add(paneSondag);



        //Vi går igennem hver pane, og hvis de er tomme, så laver vi et rektangel, der fylder hele panelet.
        for (Pane pane : panesForEachDay) {
            if (pane.getChildren().isEmpty()) {


                double rectangleHeight = 540 - (vBoxTid.getSpacing() + heightPrLabel);
                double rectangleYStartPosition = 0.0;

                createAvailableBookingStack(dayOfMonth, pane, rectangleHeight, rectangleYStartPosition);


            }
            else if (!pane.getChildren().isEmpty()) {
                // Hvis panet vi ser på ikke er tomt, skal vi få de eksisterende bookinger i panet
                // Vi skal ud fra dem beregne de tomme felter, og fylde dem med usynlige rektangler.

                //Vi går igennem alle elementerne for hver pane
                for (Node node : pane.getChildren()) {
                    //Vi tjekker om elementet vi kigger på er et rektangel
                    if (node instanceof StackPane) {
                        // Vi caster til (Rectangle)
                        StackPane rectangle = (StackPane) node;


                        // Vi tilføjer rektanglen til listen.
                        eksisterendeRektanglerForHvertPane.add(rectangle);

                    }
                }

                // Vi sortere listen med rektangler efter laveste y position først
                // Dette gør vi, da vi skal have rektanglerne efter laveste tid i ui.
                eksisterendeRektanglerForHvertPane.sort(new RectangleYPositionComparator());

                // Vi sætter værdien til 0, da vi gerne vil tjekke om der er et mellemrum fra starten
                // kalenderne til den næste rektangel.

                double calendarStartYPosition = 0;
                double calenderEndYPosition = 540;
                int lastRectangleIndex = eksisterendeRektanglerForHvertPane.size() - 1;



                //Vi starter med at tjekke, om der er afstand mellem det første rektangel
                //I hvert pane, og starten på kalenderen
                // Hvis der er afstand, skal vi have lagt en rektangel ind, som går fra starten af kalenderen,
                // og til starten af den første booking/bookingrektangel.
                if (eksisterendeRektanglerForHvertPane.get(0).getLayoutY() - calendarStartYPosition > 1) {
                    // Vi laver et rektangel, der går fra starten af kalenderen, og til starten af den første booking/bookingrektangel.

                    double rectangleHeight = eksisterendeRektanglerForHvertPane.get(0).getLayoutY() - calendarStartYPosition;
                    double rectangleYStartPosition = calendarStartYPosition;

                    createAvailableBookingStack(dayOfMonth, pane, rectangleHeight, rectangleYStartPosition);

                }



                for (int i = 1; i < eksisterendeRektanglerForHvertPane.size(); i++) {

                    double previousRectangleYPosition = eksisterendeRektanglerForHvertPane.get(i - 1).getLayoutY();
                    double previousRectangleHeight = eksisterendeRektanglerForHvertPane.get(i - 1).getPrefHeight();

                    double rectangleHeight = eksisterendeRektanglerForHvertPane.get(i).getLayoutY() - (previousRectangleYPosition + previousRectangleHeight);
                    double rectangleYStartPosition = eksisterendeRektanglerForHvertPane.get(i - 1).getLayoutY() + eksisterendeRektanglerForHvertPane.get(i - 1).getPrefHeight();


                    if (eksisterendeRektanglerForHvertPane.get(i).getLayoutY() - previousRectangleYPosition > 1) {

                        createAvailableBookingStack(dayOfMonth, pane, rectangleHeight, rectangleYStartPosition);


                    }
                }


              if (calenderEndYPosition - (eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getLayoutY() + eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getPrefHeight() + (vBoxTid.getSpacing() + heightPrLabel)) > 1) {

                   double rectangleHeight = calenderEndYPosition - (eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getLayoutY() + eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getPrefHeight()) - (vBoxTid.getSpacing() + heightPrLabel);
                   double rectangleYStartPosition = eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getLayoutY() + eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getPrefHeight();


                  createAvailableBookingStack(dayOfMonth, pane, rectangleHeight, rectangleYStartPosition);

                }
            }


            dayOfMonth++;
            // Vi  clear vores liste, så vi ikke har rektanglerne fra sidste pane med.
            eksisterendeRektanglerForHvertPane.clear();


        }
    }

    private void createAvailableBookingStack(int dayOfMonth, Pane pane, double rectangleHeight, double rectangleYStartPosition) {
        Timestamp startTime = new Timestamp(datePicker.getValue().getYear(), datePicker.getValue().getMonthValue(), dayOfMonth, (int) (uiStartTime + (rectangleYStartPosition / 45)), 00, 00, 00);
        Timestamp endTime = new Timestamp(datePicker.getValue().getYear(), datePicker.getValue().getMonthValue(), dayOfMonth, (int) (uiStartTime + (rectangleHeight / 45)), 00, 00, 00);

        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + endTime);

        Booking availableBooking = new Booking(startTime, endTime);

        pane.getChildren().add(generateAvailableBookingStack(availableBooking, rectangleHeight, rectangleYStartPosition));
    }


    //booking in this instance refers to the entire booking (booking, customer, catering, activity, etc.)
    private void loadBookingUI() {


        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("view/organization-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Organization");
            stage.setScene(new Scene(root));
            stage.setAlwaysOnTop(true);

            Stage currentStage = (Stage) calendarAnchorPane.getScene().getWindow();


            currentStage.setOpacity(0.5); //makes the main window a bit transparent

            AnchorPane bookingAnchorPane = (AnchorPane) currentStage.getScene().getRoot();

            bookingAnchorPane.setDisable(true);

            stage.setOnCloseRequest(event -> {

                currentStage.setOpacity(1);
                bookingAnchorPane.setDisable(false);
                //fake waiting to let the ui update
                /*
                try {
                    //Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }*/



                try {
                    loadBookings();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });



            stage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initializeUpdatingThread(){


        executor.execute(() -> {

            while (true) {

                //sleep for 5 seconds
                //forgot to add this and my machine sounded like it was running source 2 :p
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Platform.runLater(() -> {
                    try {
                        loadBookings();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });



            }

        });



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

    public void onCreateButtonClick() throws SQLException, IOException {

        //starts the booking process
        //organisation -> booking -> catering -> activity -> customer -> confirm

        CurrentBookingSingleton.getInstance().reset();
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