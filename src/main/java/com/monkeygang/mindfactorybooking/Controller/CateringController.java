package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.DAO.BookingDAO;
import com.monkeygang.mindfactorybooking.DAO.Booking_CateringDAO;
import com.monkeygang.mindfactorybooking.Objects.Catering;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CateringController {



    public CateringController() {

    }
    public void initialize() {

        halfDayCoffeeMeetingRadioButton.setOnAction(e -> radioButtonSelected(halfDayCoffeeMeetingRadioButton));
        halfDayMeetingRadioButton.setOnAction(e -> radioButtonSelected(halfDayMeetingRadioButton));
        wholeDayMeetingRadioButton.setOnAction(e -> radioButtonSelected(wholeDayMeetingRadioButton));

    }

    private void radioButtonSelected(RadioButton radioButton) {

        halfDayCoffeeMeetingRadioButton.setSelected(false);
        halfDayMeetingRadioButton.setSelected(false);
        wholeDayMeetingRadioButton.setSelected(false);

        radioButton.setSelected(true);

    }

    public void submitButtonClicked() throws SQLException, IOException {

        Catering catering;

        System.out.println("submit button clicked");

        if (halfDayCoffeeMeetingRadioButton.isSelected()){
            System.out.println("halfDayCoffeeMeeting selected");
            catering = new Catering(1, "halfDayCoffeeMeeting");
        } else if (halfDayMeetingRadioButton.isSelected()){
            System.out.println("halfDayMeeting selected");
            catering = new Catering(2, "halfDayMeeting");
        }else if (wholeDayMeetingRadioButton.isSelected()){
            System.out.println("wholeDayMeeting selected");
            catering = new Catering(3, "wholeDayMeeting");
        }else {
            System.out.println("nothing selected");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No catering selected");
            alert.setContentText("Please select a catering option");
            alert.showAndWait();
            return;
        }

        Booking_CateringDAO booking_cateringDAO = new Booking_CateringDAO();
        BookingDAO bookingDAO = new BookingDAO();


        bookingDAO.save(CurrentBookingSingleton.getInstance().getBooking());

        CurrentBookingSingleton.getInstance().setCurrentBooking(bookingDAO.getFromTimeStamps(CurrentBookingSingleton.getInstance().getBooking().getStartTime(), CurrentBookingSingleton.getInstance().getBooking().getEndTime()));

        System.out.println("Current booking id = " + CurrentBookingSingleton.getInstance().getBooking().getId());
        System.out.println("Current catering id = " + catering.getId());

        booking_cateringDAO.save(catering);


        Stage stage = (Stage) submitButton.getScene().getWindow();

        stage.close();



    }

    @FXML
    private RadioButton halfDayCoffeeMeetingRadioButton;

    @FXML
    private RadioButton halfDayMeetingRadioButton;

    @FXML
    private Button submitButton;

    @FXML
    private RadioButton wholeDayMeetingRadioButton;

}

