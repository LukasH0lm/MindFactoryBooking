package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.DAO.BookingDAO;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class BookingController {

    // don't know if static is needed, but we can't use it in a method as we need it at initialization
    static boolean isEdit = false;
    int bookingID; // used for editing
    BookingDAO BookingDAOImpl = new BookingDAO();


    public BookingController() throws SQLException, IOException {


    }

    public void initialize() {

        startTimeCombobox.getItems().addAll("7:00", "8:00", "9:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        endTimeCombobox.getItems().addAll("7:00", "8:00", "9:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        if (CurrentBookingSingleton.getInstance().getIsEdit()) {
            fillFields();

        } else {
            deleteButton.setVisible(false);
        }


    }

    private void fillFields() {

        Booking booking = CurrentBookingSingleton.getInstance().getBooking();

        bookingID = booking.getId();

        organisationTextfield.setText(booking.getOrganisation());
        fieldTextfield.setText(booking.getField());
        responsibleTextfield.setText(booking.getResponsible());
        amountOfVisitorsTextfield.setText(String.valueOf(booking.getAmount_of_people()));
        phoneTextfield.setText(booking.getTelephone());
        titelTextfield.setText(booking.getTitle_of_responsible());


    }


    @FXML
    void onSubmitButtonClick(ActionEvent event) throws SQLException, IOException {


        //Checking if all fields are filled out, we should probably add some more checks here
        if (organisationTextfield.getText().equals("")
                || fieldTextfield.getText().equals("")
                || responsibleTextfield.getText().equals("")
                || amountOfVisitorsTextfield.getText().equals("")
                || phoneTextfield.getText().equals("")
                || titelTextfield.getText().equals("")) {

            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setAlwaysOnTop(false);
            stage.setOpacity(0.5);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please fill out all fields!");
            alert.showAndWait();

            alert.setOnCloseRequest( (e) -> {
                stage.setAlwaysOnTop(true);
                stage.setOpacity(1);
            });

        } else {

            if (CurrentBookingSingleton.getInstance().getIsEdit()) {

                //if editing, does not work rn:

                String[] args = new String[1];

                if (isColliding(CurrentBookingSingleton.getInstance().getBooking())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("This time is already booked!");
                    alert.showAndWait();
                    return;
                }

                BookingDAOImpl.update(CurrentBookingSingleton.getInstance().getBooking(), args);

            } else {


                Booking booking = new Booking(
                        -1,
                        //startTime
                        new Timestamp(
                                startDatePicker.getValue().getYear() - 1900,
                                startDatePicker.getValue().getMonthValue() -1, // -1 because timestamp starts at 0,
                                startDatePicker.getValue().getDayOfMonth(),
                                Integer.parseInt(startTimeCombobox.getValue().substring(0, 2).replace(":", "")),
                                0, 0, 0)
                        ,
                        //endTime
                        new Timestamp(
                                endDatePicker.getValue().getYear() - 1900,
                                endDatePicker.getValue().getMonthValue() -1, // -1 because timestamp starts at 0,
                                endDatePicker.getValue().getDayOfMonth(),
                                Integer.parseInt(endTimeCombobox.getValue().substring(0, 2).replace(":", "")),
                                0, 0, 0)
                        ,

                        organisationTextfield.getText(),
                        fieldTextfield.getText(),
                        responsibleTextfield.getText(),
                        Integer.parseInt(amountOfVisitorsTextfield.getText()),
                        phoneTextfield.getText(),
                        titelTextfield.getText()
                );

                if (booking.getStartTime().after(booking.getEndTime())) {
                    Stage stage = (Stage) submitButton.getScene().getWindow();
                    stage.setAlwaysOnTop(false);
                    //stage.setOpacity(0.5); doesn't reset for some reason

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Start time is after end time!");
                    alert.showAndWait();

                    alert.setOnCloseRequest( (e) -> {
                        stage.setAlwaysOnTop(true);
                        stage.setOpacity(1);
                    });
                    return;
                }



                if (isColliding(booking)) {

                    Stage stage = (Stage) submitButton.getScene().getWindow();
                    stage.setAlwaysOnTop(false);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("This time is already booked!");
                    alert.showAndWait();

                    alert.setOnCloseRequest( (e) -> {
                        stage.setAlwaysOnTop(true);
                    });
                    //returning as a failsafe, should never happen
                    return;
                } else {

                    //we wait with adding the booking to the database until the catering is done
                    CurrentBookingSingleton.getInstance().setCurrentBooking(booking);

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/monkeygang/mindfactorybooking/view/catering-view.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Mind Factory Booking - Catering");
                    stage.getIcons().add(new javafx.scene.image.Image("file:src/main/resources/com/monkeygang/mindfactorybooking/logo.jpg"));


                    stage.setScene(new Scene(fxmlLoader.load()));
                    stage.show();

                    Stage thisStage = (Stage) submitButton.getScene().getWindow();
                    thisStage.close();


                }
            }
        }
    }


    public boolean isColliding(Booking currentBooking) {
        List<Booking> bookings = new LinkedList<>();

        try {
            bookings = BookingDAOImpl.getAll();
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
            BookingDAOImpl.delete(CurrentBookingSingleton.getInstance().getBooking());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();


    }


    @FXML
    private TextField amountOfVisitorsTextfield;

    @FXML
    private TextField fieldTextfield;

    @FXML
    private TextField organisationTextfield;

    @FXML
    private TextField phoneTextfield;

    @FXML
    private TextField responsibleTextfield;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField titelTextfield;

    @FXML
    private ComboBox<String> startTimeCombobox;

    @FXML
    private ComboBox<String> endTimeCombobox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;


}
