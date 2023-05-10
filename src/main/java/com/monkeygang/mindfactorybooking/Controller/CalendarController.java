package com.monkeygang.mindfactorybooking.Controller;

import com.itextpdf.text.DocumentException;
import com.monkeygang.mindfactorybooking.DAO.BookingDAO;
import com.monkeygang.mindfactorybooking.BookingApplication;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import com.monkeygang.mindfactorybooking.utility.BookingLoader;
import com.monkeygang.mindfactorybooking.utility.CalendarLoader;
import com.monkeygang.mindfactorybooking.utility.PDFMaker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;



public class CalendarController {

    private List<Booking> allBookings = new ArrayList<>();

    private final DecimalFormat df = new DecimalFormat("00.00");

    private final BookingDAO bookingDAO = new BookingDAO();

    public double heightPrLabel = 0.0;

    private BookingLoader bookingLoader;

    private CalendarLoader calendarLoader;



    public CalendarController() throws SQLException, IOException {

        bookingLoader = new BookingLoader(this);
        calendarLoader = new CalendarLoader(this);

    }


    public void initialize() throws SQLException, IOException {



        //mest for at teste om ui er "agilt"
        getTimeComboBox().setValue("18:00");
        getTimeComboBox().getItems().add("18:00");
        getTimeComboBox().getItems().add("24:00");
        getTimeComboBox().onActionProperty().setValue(e -> {
            calendarLoader.generateCalendar(7);
        });

        // Vi sætter startdatoen til at være dagens dato
        getDatePicker().setValue(LocalDate.now());


        // Datoen skal konveretes til date.
        // det virker bøffet det her, hvorfor konvertere vi en dato til en dato?
        LocalDate localDate = getDatePicker().getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        getDatePicker().onActionProperty().setValue(e -> {

            bookingLoader.loadBookings();

        });



        // Vi skal finde ugenummeret af datoen.
        //og nu laver vi en ny Dato???
        //vi burde have et enkelt object til at få datoen og ugenummeret.
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        // vi sætter ugenummert til at være ugenummeret for den valgte dato, når programmet starter.
        getUgeLabel().setText("Uge " + week);


        // Vi får alle bookings fra databasen, og ligger dem i listen.
        try {
            allBookings.addAll(bookingDAO.getAll());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }


        // Vi sætter decimalformatet til at være med punktum i stedet for komma, da vi skal bruge det til at lave tidspunkter
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));


        double calenderStartTime = 07.00;



        calendarLoader.generateCalendar(calenderStartTime);

        bookingLoader.loadBookings();

    }




    public void loadBookingUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("view/booking-view.fxml"));
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

        getDatePicker().setValue(getDatePicker().getValue().minusDays(7));


    }

    public void nextWeekButtonPressed() {

        getDatePicker().setValue(getDatePicker().getValue().plusDays(7));
    }

    public void createPDF() throws DocumentException, IOException {
        PDFMaker.HelloWordPDF();
    }

    @FXML
    private AnchorPane calendarAnchorPane;



    @FXML
    private DatePicker datePicker;

    @FXML
    private Label ugeLabel;

    @FXML
    private HBox HBoxCalendar;

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


    public AnchorPane getCalendarAnchorPane() {
        return calendarAnchorPane;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public Label getUgeLabel() {
        return ugeLabel;
    }

    public HBox getHBoxCalendar() {
        return HBoxCalendar;
    }

    public Line gethBoxLineFive() {
        return hBoxLineFive;
    }

    public Line gethBoxLineFour() {
        return hBoxLineFour;
    }

    public Line gethBoxLineOne() {
        return hBoxLineOne;
    }

    public Line gethBoxLineSix() {
        return hBoxLineSix;
    }

    public Line gethBoxLineThree() {
        return hBoxLineThree;
    }

    public Line gethBoxLineTwo() {
        return hBoxLineTwo;
    }

    public Pane getPaneFredag() {
        return paneFredag;
    }

    public Pane getPaneLordag() {
        return paneLordag;
    }

    public Pane getPaneMandag() {
        return paneMandag;
    }

    public Pane getPaneOnsdag() {
        return paneOnsdag;
    }

    public Pane getPaneSondag() {
        return paneSondag;
    }

    public Pane getPaneTirsdag() {
        return paneTirsdag;
    }

    public Pane getPaneTorsdag() {
        return paneTorsdag;
    }

    public VBox getvBoxTid() {
        return vBoxTid;
    }

    public ComboBox<String> getTimeComboBox() {
        return timeComboBox;
    }
}