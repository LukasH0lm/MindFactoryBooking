package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.Dao.ActivityDao;
import com.monkeygang.mindfactorybooking.Objects.Activity;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.utility.SceneChanger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ActivityController {


    public ActivityController() {

    }



    public void initialize() {

        //different activities for school and buisness

        //omkring activities i databasen:
        // 0 = ingen aktivitet
        // privat aktiviteter starter ved 7
        // dette er muligvis et dårligt design da hvis en admin
        // tilføjer en ny aktivitet vil det resultere i at
        // skole aktiviteter og private aktiviteter bliver sammenflettet

        if (CurrentBookingSingleton.getInstance().getActivityId() != -1){
            System.out.println("activity id is not -1");
            switch (CurrentBookingSingleton.getInstance().getActivityId()) {
                case 16 -> kreativtSparkRadioButton.setSelected(true);
                case 17 -> idéGeneratorenRadioButton.setSelected(true);
                case 18 -> kreativTechRadioButton.setSelected(true);
                case 0 -> noActivityRadioButton.setSelected(true);
            }
        }

    }

    @FXML
    private void radioButtonSelected(Event event) {

        kreativtSparkRadioButton.setSelected(false);
        idéGeneratorenRadioButton.setSelected(false);
        kreativTechRadioButton.setSelected(false);
        noActivityRadioButton.setSelected(false);

        //gets the name of the radio button in the ui that was selected
        //this is a very confusing way to do it, maybe there is a better way

        String buttonName = event.getSource().toString().split("'")[1].split("'")[0];

        System.out.println(buttonName + " selected");

        kreativTechRadioButton.isSelected();

        switch (buttonName){
            case "Kreativt Spark" -> kreativtSparkRadioButton.setSelected(true);
            case "IdéGeneratoren" -> idéGeneratorenRadioButton.setSelected(true);
            case "Kreativ Tech" -> kreativTechRadioButton.setSelected(true);
            case "ingen aktivitet" -> noActivityRadioButton.setSelected(true);
        }

    }


    @FXML
    private RadioButton idéGeneratorenRadioButton;

    @FXML
    private AnchorPane container;

    @FXML
    private RadioButton kreativTechRadioButton;

    @FXML
    private RadioButton kreativtSparkRadioButton;

    @FXML
    private RadioButton noActivityRadioButton;

    @FXML
    private Button nextButton;

    @FXML
    void onNextButtonClicked(ActionEvent event) throws IOException, SQLException {

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();



        if (kreativtSparkRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(16);
        }

        if (idéGeneratorenRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(17);
        }

        if (kreativTechRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(18);
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
    public void onBackButtonClicked(ActionEvent event) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        Scene scene = nextButton.getScene();
        sceneChanger.changeScene(scene, container,"transport", false);
    }

    @FXML
    public void onCancelButtonClick(ActionEvent event) throws IOException {
        //TODO: add a confirmation dialog

        Stage stage = (Stage) nextButton.getScene().getWindow();
        stage.close();

    }

}
