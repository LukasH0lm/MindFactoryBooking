package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.Objects.Organisation_type;
import com.monkeygang.mindfactorybooking.utility.AlertHandler;
import com.monkeygang.mindfactorybooking.utility.SceneChanger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;

public class TransportController {


    public TransportController() {

    }

    public void initialize() {

        if (CurrentBookingSingleton.getInstance().getBooking().isIs_transport_public()) {
            offentligTransportRadioButton.setSelected(true);
        } else {
            privatTransportRadioButton.setSelected(true);
        }

    }


    @FXML
    private void radioButtonSelected(Event event) {

        privatTransportRadioButton.setSelected(false);
        offentligTransportRadioButton.setSelected(false);

        String buttonName = event.getSource().toString().split("'")[1].split("'")[0];

        switch (buttonName) {
            case "Offentlig Transport" -> offentligTransportRadioButton.setSelected(true);
            case "Privat Transport" -> privatTransportRadioButton.setSelected(true);
        }


    }


    @FXML
    void onNextButtonClicked() throws IOException, SQLException {

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();

        AlertHandler alertHandler = new AlertHandler();

        if (afgangTextfield.getText().isEmpty() || ankomstTextfield.getText().isEmpty()) {

            Stage stage = (Stage) nextButton.getScene().getWindow();

            alertHandler.showAlert(stage, "Udfyld venligst alle felter", "deez","gør det");
            return;
        }

        if (privatTransportRadioButton.isSelected() == false && offentligTransportRadioButton.isSelected() == false) {

            Stage stage = (Stage) nextButton.getScene().getWindow();

            alertHandler.showAlert(stage, "Vælg venligst en transportform", "deez","gør det");
            return;
        }

        if (ankomstTextfield.getText().equals(afgangTextfield.getText())) {

            Stage stage = (Stage) nextButton.getScene().getWindow();

            alertHandler.showAlert(stage, "Ankomst og afgang kan ikke være det samme", "deez","gør det");
            return;
        }

        //if booking is one day the time of arrival can't be after the time of departure
        if (currentBookingSingleton.getBooking().getStartTime().getDate() != currentBookingSingleton.getBooking().getEndTime().getDate()) {

            if (Time.valueOf(ankomstTextfield.getText() + ":00").after(Time.valueOf(afgangTextfield.getText() + ":00"))) {

                Stage stage = (Stage) nextButton.getScene().getWindow();

                alertHandler.showAlert(stage, "Ankomst kan ikke være efter afgang", "deez","gør det");
                return;
            }


        }





        if (privatTransportRadioButton.isSelected()) {
            CurrentBookingSingleton.getInstance().getBooking().setIs_transport_public(false);
        }

        if (offentligTransportRadioButton.isSelected()) {
            CurrentBookingSingleton.getInstance().getBooking().setIs_transport_public(true);
        }


CurrentBookingSingleton.getInstance().getBooking().setArrival_time(Time.valueOf(ankomstTextfield.getText() + ":00"));
        CurrentBookingSingleton.getInstance().getBooking().setDeparture_time(Time.valueOf(afgangTextfield.getText() + ":00"));

        Scene scene = nextButton.getScene();

        SceneChanger sceneChanger = new SceneChanger();

        String view = null;

        if (CurrentBookingSingleton.getInstance().getOrganization().getType().equals(Organisation_type.SCHOOL)) {
            view = "school-activity";
        } else if (CurrentBookingSingleton.getInstance().getOrganization().getType().equals(Organisation_type.PRIVATE)) {
            view = "private-activity";
        }

        if (view == null) {
            System.out.println("view is null");
            System.out.println("organization type is " + CurrentBookingSingleton.getInstance().getOrganization().getType());
            return;
        }

        sceneChanger.changeScene(scene, container, view, true);


    }

    @FXML
    public void onBackButtonClicked() throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        Scene scene = nextButton.getScene();
        sceneChanger.changeScene(scene, container, "redskaber", false);
    }

    @FXML
    public void onCancelButtonClick() throws IOException {


        Stage stage = (Stage) nextButton.getScene().getWindow();
        stage.close();

    }


    @FXML
    private TextField afgangTextfield;

    @FXML
    private TextField ankomstTextfield;

    @FXML
    private Button backButton;

    @FXML
    private AnchorPane container;

    @FXML
    private Button nextButton;

    @FXML
    private Button nextButton1;

    @FXML
    private RadioButton offentligTransportRadioButton;

    @FXML
    private RadioButton privatTransportRadioButton;


}
