package com.monkeygang.mindfactorybooking.Controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

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

    public void submitButtonClicked() {

        System.out.println("submit button clicked");

        if (halfDayCoffeeMeetingRadioButton.isSelected()){
            System.out.println("halfDayCoffeeMeeting selected");
        } else if (halfDayMeetingRadioButton.isSelected()){
            System.out.println("halfDayMeeting selected");
        }else if (wholeDayMeetingRadioButton.isSelected()){
            System.out.println("wholeDayMeeting selected");
        }else {
            System.out.println("nothing selected");
        }


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

