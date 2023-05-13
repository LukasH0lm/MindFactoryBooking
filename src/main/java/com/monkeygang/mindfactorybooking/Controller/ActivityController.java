package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.utility.SceneChanger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ActivityController {


    public ActivityController() {

    }



    public void initialize() {

        if (CurrentBookingSingleton.getInstance().getActivityId() != -1){
            System.out.println("activity id is not -1");
            switch (CurrentBookingSingleton.getInstance().getActivityId()) {
                case 1 -> kreativtSparkRadioButton.setSelected(true);
                case 2 -> idéGeneratorenRadioButton.setSelected(true);
                case 3 -> kreativTechRadioButton.setSelected(true);
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
    void onNextButtonClicked(ActionEvent event) throws IOException {

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();

        if (kreativtSparkRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(1);
        }

        if (idéGeneratorenRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(2);
        }

        if (kreativTechRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(3);
        }

        if (noActivityRadioButton.isSelected()){
            currentBookingSingleton.setActivityId(0);
        }

        SceneChanger sceneChanger = new SceneChanger();

        Scene scene = nextButton.getScene();

        sceneChanger.changeScene(scene, container,"customer", true);



    }

    @FXML
    public void onBackButtonClicked(ActionEvent event) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        Scene scene = nextButton.getScene();
        sceneChanger.changeScene(scene, container,"catering", false);
    }

}
