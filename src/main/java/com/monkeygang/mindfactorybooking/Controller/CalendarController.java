package com.monkeygang.mindfactorybooking.Controller;

import com.itextpdf.text.DocumentException;
import com.monkeygang.mindfactorybooking.Dao.BookingDao;
import com.monkeygang.mindfactorybooking.BookingApplication;
import com.monkeygang.mindfactorybooking.Dao.Booking_ActivityDao;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private List<Booking> allBookings = Collections.synchronizedList(new ArrayList<>());

    private final DecimalFormat df = new DecimalFormat("00.00");

    private final BookingDao bookingDAO = new BookingDao();

    Booking_ActivityDao booking_activityDao = new Booking_ActivityDao();

    public double uiStartTime = 07.00;

    public double heightPrLabel = 0.0;

    public double spacingPrLabel = 0.0;

    private boolean isUpdating = true;

    //TODO: shutdown threads on close

    ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newCachedThreadPool();

    public CalendarController() throws SQLException, IOException {
    }


    public void initialize() throws SQLException, IOException {


        // Vi sætter startdatoen til at være dagens dato
        datePicker.setValue(LocalDate.now());


        // Datoen skal konveretes til date.
        // det virker bøffet det her, hvorfor konvertere vi en dato til en dato?
        LocalDate localDate = datePicker.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        datePicker.onActionProperty().setValue(e -> {

            try {
                loadBookings(allBookings);
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
        ugeLabel.setText("Uge: " + week);


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


        //
        loadBookings(allBookings);


    }


    private void generateCalendar(double calenderStartTime) {


        double calendarEndTime = 18.0;
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
                timeLine.setStroke(Color.rgb(169, 169, 169));
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


    private void loadBookings(List<Booking> bookings) throws SQLException, IOException {


        if (bookings == null) {
            System.out.println("bookings er null");
        }

        spacingPrLabel = vBoxTid.getSpacing();

        // Datoen skal konveretes til date. //hvorfor?
        LocalDate localDateOnAction = datePicker.getValue();
        Date dateOnAction = Date.from(localDateOnAction.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Vi skal finde ugenummeret af datoen.
        Calendar calOnAction = Calendar.getInstance();
        calOnAction.setTime(dateOnAction);
        int weekOnAction = calOnAction.get(Calendar.WEEK_OF_YEAR);
        int yearOnAction = calOnAction.get(Calendar.YEAR);


        ugeLabel.setText("Uge: " + weekOnAction);


        paneMandag.getChildren().clear();
        paneTirsdag.getChildren().clear();
        paneOnsdag.getChildren().clear();
        paneTorsdag.getChildren().clear();
        paneFredag.getChildren().clear();
        paneLordag.getChildren().clear();
        paneSondag.getChildren().clear();


        for (Booking booking : bookings) {


            Date bookingDate = booking.getStartTime();


            // Vi skal finde ugenummeret af datoen.
            Calendar calBookingDate = Calendar.getInstance();
            calBookingDate.setTime(bookingDate);
            int bookingDateWeek = calBookingDate.get(Calendar.WEEK_OF_YEAR);


            // vi ligger 1900 til, da Timestamp er underlig D:D:D:D:
            if (bookingDateWeek == weekOnAction && booking.getStartTime().getYear() + 1900 == yearOnAction) {

                if (!(booking.getStartTime().getDay() == booking.getEndTime().getDay())) {

                    createBookingMultipleDays(booking);

                } else {
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
        Timestamp firstDayEndTime = new Timestamp(booking.getStartTime().getYear(), booking.getStartTime().getMonth(), booking.getStartTime().getDate(), 18, 00, 00, 00);

        createSingleBookingFixedValues(booking, booking.getStartTime(), firstDayEndTime);


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


    public void generateAvailableBookingStack(int dayOfMonth, Pane pane, double rectangleHeight, double rectangleYStartPosition) {

        double startTimeHours = (int) (uiStartTime + (rectangleYStartPosition / 45));
        double endTimeHours = (int) (startTimeHours + (rectangleHeight / 45));


        Timestamp startTime = new Timestamp(datePicker.getValue().getYear() - 1900, datePicker.getValue().getMonthValue() - 1, dayOfMonth, (int) startTimeHours, 00, 00, 00);
        Timestamp endTime = new Timestamp(datePicker.getValue().getYear() - 1900, datePicker.getValue().getMonthValue() - 1, dayOfMonth, (int) endTimeHours, 00, 00, 00);


        Booking availableBooking = new Booking(startTime, endTime);


        Rectangle bookingRectangle = new Rectangle(100, rectangleHeight);
        bookingRectangle.setFill(Color.TRANSPARENT);

        StackPane stack = new StackPane(bookingRectangle);

        stack.setLayoutY(rectangleYStartPosition);
        stack.setPrefHeight(rectangleHeight);

        bookingCreate(availableBooking, stack);

        pane.getChildren().add(stack);


    }

    public StackPane generateBookingStack(Booking booking, double rectangleHeight, double rectangleYStartPosition) throws SQLException, IOException {

        Rectangle bookingRectangle = new Rectangle(100, rectangleHeight);
        bookingRectangle.setFill(Color.RED);

        Organization currentOrganization = bookingDAO.getOrganisation(booking);


        Label bookingLabel = new Label(currentOrganization.getName());


        bookingLabel.setAlignment(Pos.CENTER);


        bookingLabel.setTextFill(Paint.valueOf("white"));
        bookingLabel.setMaxWidth(45); // 50 - 5 to give the text to space to breathe
        bookingLabel.setWrapText(true);


        int activityID = booking_activityDao.getActivityIDbyBookingID(booking.getId());

        ImageView activityIcon = new ImageView();


        //Hvis det er en aktivitet, så skal vi have billedet fra aktiviteten.
        // Vi returner 0, når booking id ikke findes i aktivitets tabellen, så når den er 0, så sætter vi billedet til møde,
        // da vi ser en booking som et møde, når der ikke er valgt nogen aktivitet.

        switch (activityID) {
            case 0 ->
                    activityIcon.setImage(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/møde.png"));
            case 10 ->
                    activityIcon.setImage(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/idefabrikken.png"));
            case 13 ->
                    activityIcon.setImage(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/digitalfabrikation.png"));
            case 11 ->
                    activityIcon.setImage(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/robotpåjob.png"));
            case 14 ->
                    activityIcon.setImage(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/robottenrydderop.png"));
            case 12 ->
                    activityIcon.setImage(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/naturismevedvadehavet.png"));
            case 15 ->
                    activityIcon.setImage(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/skabsikkerhedivadehavet.png"));
            case 16 ->
                    activityIcon.setImage(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/kreativspark.png"));
            case 17 ->
                    activityIcon.setImage(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/ideGeneratoren.png"));
            case 18 ->
                    activityIcon.setImage(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/kreativtech.png"));
        }


        activityIcon.setTranslateX(35);
        activityIcon.setTranslateY(-((rectangleHeight / 2) - 7));

        StackPane stack = new StackPane(bookingRectangle, bookingLabel, activityIcon);

        stack.setLayoutY(rectangleYStartPosition);
        stack.setPrefHeight(rectangleHeight);

        bookingInitialize(bookingRectangle, booking, stack);

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


    public void bookingCreate(Booking booking, StackPane stack) {

        stack.setOnMouseClicked(event -> {

            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() == 2) {

                    try {
                        CurrentBookingSingleton.getInstance().reset();
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }

                    CurrentBookingSingleton.getInstance().setCurrentBooking(booking);
                    loadBookingUI();


                }
            }


        });


    }

    public void fillCalendarWithBlankSquares() {

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

                generateAvailableBookingStack(dayOfMonth, pane, rectangleHeight, rectangleYStartPosition);


            } else if (!pane.getChildren().isEmpty()) {
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

                final double calendarStartYPosition = 0;
                final double calenderEndYPosition = hBoxCalendar.getPrefHeight();


                double startRectangleYStartPosition = calendarStartYPosition;


                double spacingBetweenStartOfCalendarAndFirstBooking = eksisterendeRektanglerForHvertPane.get(0).getLayoutY()
                        - calendarStartYPosition;

                // vi reassigner en variable her, det gør vi for at koden er mere læselig. Det er ikke nødvendigt.
                double startRectangleHeight = spacingBetweenStartOfCalendarAndFirstBooking;


                //Vi starter med at tjekke, om der er afstand mellem det første rektangel
                //I hvert pane, og starten på kalenderen
                // Hvis der er afstand, skal vi have lagt en rektangel ind, som går fra starten af kalenderen,
                // og til starten af den første booking/bookingrektangel.
                if (spacingBetweenStartOfCalendarAndFirstBooking > 1) {
                    // Vi laver et rektangel, der går fra starten af kalenderen, og til starten af den første booking/bookingrektangel.

                    generateAvailableBookingStack(dayOfMonth, pane, startRectangleHeight, startRectangleYStartPosition);


                }


                for (int i = 1; i < eksisterendeRektanglerForHvertPane.size(); i++) {

                    int lastRectangleIndex = i - 1;

                    double previousRectangleYPosition = eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getLayoutY();
                    double previousRectangleHeight = eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getPrefHeight();

                    double currentRectangleHeight = eksisterendeRektanglerForHvertPane.get(i).getLayoutY()
                            - (previousRectangleYPosition + previousRectangleHeight);


                    double currentRectangleYStartPosition = eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getLayoutY()
                            + eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getPrefHeight();

                    double spacingBetweenBooking = (previousRectangleYPosition + previousRectangleHeight)
                            - (currentRectangleYStartPosition - currentRectangleHeight);

                    if (spacingBetweenBooking > 1) {

                        generateAvailableBookingStack(dayOfMonth, pane, currentRectangleHeight, currentRectangleYStartPosition);


                    }

                }

                final int lastRectangleIndex = eksisterendeRektanglerForHvertPane.size() - 1;


                double endRectangleYStartPosition = eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getLayoutY()
                        + eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getPrefHeight();


                double spacingBetweenLastBookingAndEndOfCalendar = calenderEndYPosition
                        - (eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getLayoutY()
                        + eksisterendeRektanglerForHvertPane.get(lastRectangleIndex).getPrefHeight());


                double endRectangleHeight = spacingBetweenLastBookingAndEndOfCalendar;


                if (spacingBetweenLastBookingAndEndOfCalendar > 1) {

                    generateAvailableBookingStack(dayOfMonth, pane, endRectangleHeight, endRectangleYStartPosition);


                }

            }


            dayOfMonth++;
            // Vi  clear vores liste, så vi ikke har rektanglerne fra sidste pane med.
            eksisterendeRektanglerForHvertPane.clear();


        }
    }


    //booking in this instance refers to the entire booking (booking, customer, catering, activity, etc.)
    private void loadBookingUI() {


        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("view/organization-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Booking");

            stage.getIcons().add(new javafx.scene.image.Image("file:src/main/resources/com/monkeygang/mindfactorybooking/logo.jpg"));


            stage.setScene(new Scene(root));
            stage.setAlwaysOnTop(true);

            Stage currentStage = (Stage) calendarAnchorPane.getScene().getWindow();


            currentStage.setOpacity(0.5); //makes the main window a bit transparent

            AnchorPane bookingAnchorPane = (AnchorPane) currentStage.getScene().getRoot();

            bookingAnchorPane.setDisable(true);


            stage.setOnHiding(event -> {

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
                    loadBookings(allBookings);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });


            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initializeUpdatingThread() {

        //  ArrayList<Booking> allBookingsonThread = new ArrayList<>();


        Runnable runnableTask = () -> {


            BookingDao BookingDAO = null;
            try {
                BookingDAO = new BookingDao();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            while (isUpdating) {

                //sleep for 5 seconds
                //forgot to add this and my machine sounded like it was running source 2 :p
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                try {
                    allBookings.clear();
                    allBookings.addAll(BookingDAO.getAll());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Platform.runLater(() -> {
                    try {
                        loadBookings(allBookings);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });


            }

        };


        executor.execute(runnableTask);

        //Thread thread = new Thread(runnableTask);
        //thread.start();

    }

    ;


    public void onSearchButtonClick() {

        for (Booking booking : allBookings) {
            if (booking.hashCode() == Integer.parseInt(SearchTextField.getText())) {
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


    public void startUpdate() {
        isUpdating = true;
    }

    public void stopUpdate() {
        isUpdating = false;
    }

    public void onDashboardButtonClick() throws IOException {

        stopUpdate();

        Stage currentStage = (Stage) cDashboardButton.getScene().getWindow();


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/monkeygang/mindfactorybooking/view/dashboard-view.fxml"));
        Parent newRoot = fxmlLoader.load();


        Scene newScene = new Scene(newRoot);


        currentStage.setScene(newScene);
        currentStage.setTitle("Mind Factory Booking - Admin");
        currentStage.show();


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
    private Button cDashboardButton;


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