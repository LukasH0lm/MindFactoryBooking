package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.Dao.BookingDao;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.utility.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class BookingController {

    // don't know if static is needed, but we can't use it in a method as we need it at initialization
    static boolean isEdit = false;
    int bookingID; // used for editing
    BookingDao bookingDaoImpl = new BookingDao();
    private Executor exec;


    public BookingController() throws SQLException, IOException {


    }

    public void initialize() {

        startTimeCombobox.getItems().addAll("7:00", "8:00", "9:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        endTimeCombobox.getItems().addAll("7:00", "8:00", "9:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        //TODO: implement edit booking
        /*
        if (CurrentBookingSingleton.getInstance().getIsEdit()) {
            fillFields();

        } else {
            deleteButton.setVisible(false);
        }*/

        if (CurrentBookingSingleton.getInstance().getBooking() != null) {
            fillFields();
        }

        exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });


    }

    private void fillFields() {

        Booking booking = CurrentBookingSingleton.getInstance().getBooking();

        bookingID = booking.getId();

        startDatePicker.setValue(booking.getStartTime().toLocalDateTime().toLocalDate());
        endDatePicker.setValue(booking.getEndTime().toLocalDateTime().toLocalDate());
        startTimeCombobox.setValue(booking.getStartTime().toLocalDateTime().toLocalTime().toString().substring(0, 5));
        endTimeCombobox.setValue(booking.getEndTime().toLocalDateTime().toLocalTime().toString().substring(0, 5));



    }


    @FXML
    void onNextButtonClick(ActionEvent event) throws SQLException, IOException {

        //TODO: this method is too large, we should split it up into smaller methods

        //Checking if all fields are filled out, we should probably add some more checks here
        if (
                startDatePicker.getValue() == null
                        || endDatePicker.getValue() == null
                        || startTimeCombobox.getValue() == null
                        || endTimeCombobox.getValue() == null
        ) {

            showEmptyFieldAlert();
            // I know we could make the new conditional an if else, but I think this is more readable
            return;

        }


        if (CurrentBookingSingleton.getInstance().getIsEdit()) {
            editBooking();
        } else {
            newBooking();
        }
    }


    public void newBooking() throws IOException {

        //TODO: make this make a temporary booking, and then check if it collides with other bookings
        //TODO: then save is as a temoprary booking


        Booking booking = new Booking(

                //startTime
                new Timestamp(
                        startDatePicker.getValue().getYear() - 1900,
                        startDatePicker.getValue().getMonthValue() - 1, // -1 because timestamp starts at 0,
                        startDatePicker.getValue().getDayOfMonth(),
                        Integer.parseInt(startTimeCombobox.getValue().substring(0, 2).replace(":", "")),
                        0, 0, 0)
                ,
                //endTime
                new Timestamp(
                        endDatePicker.getValue().getYear() - 1900,
                        endDatePicker.getValue().getMonthValue() - 1, // -1 because timestamp starts at 0,
                        endDatePicker.getValue().getDayOfMonth(),
                        Integer.parseInt(endTimeCombobox.getValue().substring(0, 2).replace(":", "")),
                        0, 0, 0)
                ,

                -1


                //TODO: submit as temporary booking and set database id to the id of the temporary booking

        );


        if (booking.getStartTime().after(booking.getEndTime())) {
            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setAlwaysOnTop(false);
            //stage.setOpacity(0.5); doesn't reset for some reason

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Start time is after end time!");
            alert.showAndWait();

            alert.setOnCloseRequest((e) -> {
                stage.setAlwaysOnTop(true);
                stage.setOpacity(1);
            });
            return;
        }

        if (booking.getStartTime().before(new Timestamp(System.currentTimeMillis()))) {
            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setAlwaysOnTop(false);
            stage.setOpacity(0.5);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Start time is before current time!");


            alert.setOnCloseRequest((e) -> {
                stage.setAlwaysOnTop(true);
                stage.setOpacity(1);
            });

            alert.showAndWait();
            return;
        }


        if (isColliding(booking)) {

            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setAlwaysOnTop(false);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("This time is already booked!");
            alert.showAndWait();

            alert.setOnCloseRequest((e) -> {
                stage.setAlwaysOnTop(true);
            });
            //returning as a failsafe, should never happen
            return;
        } else {

            //we wait with adding the booking to the database until the catering is done
            CurrentBookingSingleton.getInstance().setCurrentBooking(booking);

            loadCateringView();


        }


    }


    public void editBooking() {

        // the args are intended to be used for the update method, but we don't use them
        String[] args = new String[1];

        if (isColliding(CurrentBookingSingleton.getInstance().getBooking())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("This time is already booked!");
            alert.showAndWait();
            return;
        }

        Timestamp startTime = Timestamp.valueOf(startDatePicker.getValue().atTime(Integer.parseInt(startTimeCombobox.getValue().substring(0, 2).replace(":", "")), 0));
        Timestamp endTime = Timestamp.valueOf(endDatePicker.getValue().atTime(Integer.parseInt(endTimeCombobox.getValue().substring(0, 2).replace(":", "")), 0));


        Booking updatedBooking = new Booking(
                startTime,
                endTime,

                -1

        );

        CurrentBookingSingleton.getInstance().setCurrentBooking(updatedBooking);

        // we need to update the booking in the database
        // idk if this is a good way to do it, but it's technically in a thread now
        exec.execute(() -> {
            try {
                bookingDaoImpl.update(CurrentBookingSingleton.getInstance().getBooking(), args);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });


    }

    public void showEmptyFieldAlert() {
        Stage stage = (Stage) nextButton.getScene().getWindow();
        stage.setAlwaysOnTop(false);
        stage.setOpacity(0.5);

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setOnCloseRequest((e) -> {
            stage.setAlwaysOnTop(true);
            stage.setOpacity(1);
        });

        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText("Please fill out all fields!");
        alert.showAndWait();


    }


    public boolean isColliding(Booking currentBooking) {
        List<Booking> bookings = new LinkedList<>();

        try {
            bookings = bookingDaoImpl.getAll();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }

        //checks for collisions
        //checks:
        //if current booking starts or end at the same time as another booking
        //if current booking is between another booking
        //if another booking is between current booking

        //doesn't check if current booking is right before or right after another booking

        for (Booking booking : bookings) {

            if (booking.getId() == currentBooking.getId()) {
                continue;
            }


            // if current booking starts or end at the same time as another booking
            if (booking.getStartTime().equals(currentBooking.getStartTime()) || booking.getEndTime().equals(currentBooking.getEndTime())) {
                System.out.println("Collision: starts or ends at the same time as another booking");
                return true;
            }

            // if current booking is between another booking
            if (booking.getStartTime().before(currentBooking.getStartTime()) && booking.getEndTime().after(currentBooking.getStartTime())) {
                System.out.println("Collision: is between another booking");
                return true;
            }

            // if another booking is between current booking
            if (currentBooking.getStartTime().before(booking.getStartTime()) && currentBooking.getEndTime().after(booking.getStartTime())) {
                System.out.println("Collision: another booking is between current booking");
                return true;
            }


        }

        return false;

    }

    @FXML
    void onCancelButtonClick(ActionEvent event) {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    void onDeleteButtonClick() {

        try {
            System.out.println("deleting:" + CurrentBookingSingleton.getInstance().getBooking().getId());
            //TODO: modify delete method to delete all tables that are connected to the booking
            bookingDaoImpl.delete(CurrentBookingSingleton.getInstance().getBooking());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();


    }

    public void loadCateringView() throws IOException {

        //inspired by https://www.youtube.com/watch?v=cqskg3DYH8g

        Scene scene = nextButton.getScene();

        SceneChanger sceneChanger = new SceneChanger();

        sceneChanger.changeScene(scene, container, "catering", true);


    }

    @FXML
    void onBackButtonClick(ActionEvent event) throws IOException {

        SceneChanger sceneChanger = new SceneChanger();

        Scene scene = cancelButton.getScene();

        sceneChanger.changeScene(scene, container, "organization", false);

    }

    @FXML
    private AnchorPane container;


    @FXML
    private Button nextButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;


    @FXML
    private ComboBox<String> startTimeCombobox;

    @FXML
    private ComboBox<String> endTimeCombobox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;


}
