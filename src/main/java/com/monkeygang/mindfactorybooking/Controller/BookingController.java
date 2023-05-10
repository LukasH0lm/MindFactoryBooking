package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.DAO.BookingDAO;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import static com.monkeygang.mindfactorybooking.Controller.CalendarController.currentBooking;


public class BookingController {

    // don't know if static is needed, but we can't use it in a method as we need it at initialization
    static boolean isEdit = false;
    int bookingID; // used for editing
    BookingDAO BookingDAOImpl = new BookingDAO();


    public BookingController() throws SQLException, IOException {



    }

    public void initialize() {


        if (isEdit) {
            fillFields();
        }


    }

    private void fillFields() {

        Booking booking = currentBooking;

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

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please fill out all fields!");
            alert.showAndWait();

        } else {

            if (isEdit) {

                String[] args = new String[1];
                BookingDAOImpl.update(currentBooking, args);

            } else {

                Calendar dateTime = Calendar.getInstance();


                Booking booking = new Booking(
                        -1,
                        //Temporary solution, the controller is build as if we already have the times
                        new Timestamp(dateTime.get(Calendar.YEAR) - 1900, dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH), dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), 0, 0),
                        new Timestamp(dateTime.get(Calendar.YEAR) - 1900, dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH), dateTime.get(Calendar.HOUR_OF_DAY) + 2, dateTime.get(Calendar.MINUTE), 0, 0),


                        organisationTextfield.getText(),
                        fieldTextfield.getText(),
                        responsibleTextfield.getText(),
                        Integer.parseInt(amountOfVisitorsTextfield.getText()),
                        phoneTextfield.getText(),
                        titelTextfield.getText()
                );

                BookingDAOImpl.save(booking);

            }

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

    @FXML
    void onCancelButtonClick(ActionEvent event) {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
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
    private TextField titelTextfield;

    


}
