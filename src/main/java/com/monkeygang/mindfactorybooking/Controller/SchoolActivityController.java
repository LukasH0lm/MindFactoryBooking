package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.Dao.ActivityDao;
import com.monkeygang.mindfactorybooking.Objects.Activity;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.utility.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SchoolActivityController {



    public SchoolActivityController() {

    }

    public void initialize() {

        //different activities for school and buisness

        if (CurrentBookingSingleton.getInstance().getActivityId() != -1){
            System.out.println("activity id is not -1");
            switch (CurrentBookingSingleton.getInstance().getActivityId()) {
                case 10 -> idéfabrikkenRadioButton.setSelected(true);
                case 13 -> laserskærerRadioButton.setSelected(true);
                case 11 -> robotPåJobRadioButton.setSelected(true);
                case 14 -> RobottenRydderOpRadioButton.setSelected(true);
                case 12 -> naturturismeRadioButton.setSelected(true);
                case 15 -> SikkerhedRadioButton.setSelected(true);
                case 0 -> noActivityRadioButton.setSelected(true);
            }
        }

    }


    @FXML
    private RadioButton RobottenRydderOpRadioButton;

    @FXML
    private RadioButton SikkerhedRadioButton;

    @FXML
    private Button backButton;

    @FXML
    private Button cancelButton;

    @FXML
    private AnchorPane container;

    @FXML
    private RadioButton idéfabrikkenRadioButton;

    @FXML
    private RadioButton laserskærerRadioButton;

    @FXML
    private RadioButton naturturismeRadioButton;

    @FXML
    private Button nextButton;

    @FXML
    private RadioButton noActivityRadioButton;

    @FXML
    private RadioButton robotPåJobRadioButton;

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {

        SceneChanger sceneChanger = new SceneChanger();
        Scene scene = nextButton.getScene();
        sceneChanger.changeScene(scene, container,"catering", false);

    }

    @FXML
    void onCancelButtonClick(ActionEvent event) {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    void onNextButtonClicked(ActionEvent event) throws IOException, SQLException {

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();


        if (idéfabrikkenRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(10);
        }

        if (laserskærerRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(13);
        }

        if (robotPåJobRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(11);
        }

        if (RobottenRydderOpRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(14);
        }

        if (naturturismeRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(12);
        }

        if (SikkerhedRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(15);
        }

        if (noActivityRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(0);
        }


        ActivityDao activityDao = new ActivityDao();

        Activity activity = (Activity) activityDao.get(currentBookingSingleton.getActivityId()).get();

        CurrentBookingSingleton.getInstance().setActivity(activity);


        SceneChanger sceneChanger = new SceneChanger();

        Scene scene = nextButton.getScene();

        sceneChanger.changeScene(scene, container,"customer", true);




    }

    @FXML
    void radioButtonSelected(ActionEvent event) {

        idéfabrikkenRadioButton.setSelected(false);
        laserskærerRadioButton.setSelected(false);
        robotPåJobRadioButton.setSelected(false);
        RobottenRydderOpRadioButton.setSelected(false);
        naturturismeRadioButton.setSelected(false);
        SikkerhedRadioButton.setSelected(false);
        noActivityRadioButton.setSelected(false);

        //gets the name of the radio button in the ui that was selected
        //this is a very confusing way to do it, maybe there is a better way

        String buttonName = event.getSource().toString().split("'")[1].split("'")[0];

        System.out.println(buttonName + " selected");

        switch (buttonName){
            case "Idéfabrikken" -> idéfabrikkenRadioButton.setSelected(true);
            case "Digital fabrikation med laserskærer" -> laserskærerRadioButton.setSelected(true);
            case "Robot på job" -> robotPåJobRadioButton.setSelected(true);
            case "Robotten rydder op" -> RobottenRydderOpRadioButton.setSelected(true);
            case "Naturturisme ved Vadehavet" -> naturturismeRadioButton.setSelected(true);
            case "Skab sikkerhed i Vadehavet" -> SikkerhedRadioButton.setSelected(true);


            case "ingen aktivitet" -> noActivityRadioButton.setSelected(true);
        }


    }

}
