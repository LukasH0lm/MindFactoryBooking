package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.Dao.CateringDao;
import com.monkeygang.mindfactorybooking.Objects.Catering;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.utility.SceneChanger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class CateringController {

    //needs a cancel button


    public CateringController() {

    }

    public void initialize() {


        if (CurrentBookingSingleton.getInstance().getCateringId() != -1) {
            System.out.println("catering id is not -1");
            System.out.println("catering id is " + CurrentBookingSingleton.getInstance().getCateringId());
            switch (CurrentBookingSingleton.getInstance().getCateringId()) {
                case 0 -> noCateringRadioButton.setSelected(true);
                case 1 -> halfDayCoffeeMeetingRadioButton.setSelected(true);
                case 2 -> halfDayMeetingRadioButton.setSelected(true);
                case 3 -> wholeDayMeetingRadioButton.setSelected(true);
            }
        }

    }

    @FXML
    private void radioButtonSelected(Event event) {

        System.out.println("radio button selected");

        halfDayCoffeeMeetingRadioButton.setSelected(false);
        halfDayMeetingRadioButton.setSelected(false);
        wholeDayMeetingRadioButton.setSelected(false);
        noCateringRadioButton.setSelected(false);

        //gets the name of the radio button in the ui that was selected
        //this is a very confusing way to do it, maybe there is a better way

        String buttonName = event.getSource().toString().split("'")[1].split("'")[0];

        System.out.println(buttonName + " selected");

        switch (buttonName) {
            case "Halvdags kaffemøde" -> halfDayCoffeeMeetingRadioButton.setSelected(true);
            case "Halvdagsmødepakke" -> halfDayMeetingRadioButton.setSelected(true);
            case "Heldagsmødepakke" -> wholeDayMeetingRadioButton.setSelected(true);
            case "ingen forplejning" -> noCateringRadioButton.setSelected(true);
        }

    }

    public void onNextButtonClicked() throws SQLException, IOException {


        System.out.println("submit button clicked");

        if (halfDayCoffeeMeetingRadioButton.isSelected()) {
            System.out.println("halfDayCoffeeMeeting selected");
            CurrentBookingSingleton.getInstance().setCateringId(1);
        } else if (halfDayMeetingRadioButton.isSelected()) {
            System.out.println("halfDayMeeting selected");
            CurrentBookingSingleton.getInstance().setCateringId(2);
        } else if (wholeDayMeetingRadioButton.isSelected()) {
            System.out.println("wholeDayMeeting selected");
            CurrentBookingSingleton.getInstance().setCateringId(3);
        } else if (noCateringRadioButton.isSelected()) {
            System.out.println("noCatering selected");
            CurrentBookingSingleton.getInstance().setCateringId(0);
        } else {
            System.out.println("nothing selected");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No catering option selected");
            alert.setContentText("Udfyld venligst catering");
            alert.showAndWait();
            return;
        }


        System.out.println("Current booking id = " + CurrentBookingSingleton.getInstance().getBooking().getId());
        System.out.println("Current catering id = " + CurrentBookingSingleton.getInstance().getCateringId());


        //in the new architecture, we wait until the end of the booking to save the catering
        //booking_cateringDAO.save(catering);

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();

        CateringDao cateringDao = new CateringDao();

        Catering catering = (Catering) cateringDao.get(currentBookingSingleton.getCateringId()).get();

        CurrentBookingSingleton.getInstance().setCurrentCatering(catering);


        Scene scene = nextButton.getScene();

        SceneChanger sceneChanger = new SceneChanger();


        sceneChanger.changeScene(scene, container, "redskaber", true);


    }

    @FXML
    public void onBackButtonClicked() throws IOException {
        Scene scene = nextButton.getScene();

        SceneChanger sceneChanger = new SceneChanger();

        sceneChanger.changeScene(scene, container, "booking", false);


    }


    @FXML
    private Button nextButton;

    @FXML
    private AnchorPane container;

    @FXML
    private RadioButton halfDayCoffeeMeetingRadioButton;

    @FXML
    private RadioButton halfDayMeetingRadioButton;


    @FXML
    private RadioButton wholeDayMeetingRadioButton;

    @FXML
    private RadioButton noCateringRadioButton;

}

