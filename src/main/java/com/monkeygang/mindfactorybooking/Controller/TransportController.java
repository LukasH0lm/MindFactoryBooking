package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.Dao.ActivityDao;
import com.monkeygang.mindfactorybooking.Dao.TransportDao;
import com.monkeygang.mindfactorybooking.Objects.Activity;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.Objects.Organisation_type;
import com.monkeygang.mindfactorybooking.Objects.Transport;
import com.monkeygang.mindfactorybooking.utility.SceneChanger;
import javafx.event.ActionEvent;
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

public class TransportController {


    public TransportController() {

    }

    public void initialize() {

        if (CurrentBookingSingleton.getInstance().getTransportId() != -1){
            System.out.println("activity id is not -1");
            switch (CurrentBookingSingleton.getInstance().getTransportId()) {
                case 2 -> offentligTransportRadioButton.setSelected(true);
                case 1 -> privatTransportRadioButton.setSelected(true);

            }
        }

    }


    @FXML
    private void radioButtonSelected(Event event) {

        privatTransportRadioButton.setSelected(false);
        offentligTransportRadioButton.setSelected(false);

        String buttonName = event.getSource().toString().split("'")[1].split("'")[0];

        switch (buttonName){
            case "Offentlig Transport" -> offentligTransportRadioButton.setSelected(true);
            case "Privat Transport" -> privatTransportRadioButton.setSelected(true);
        }




    }


    @FXML
    void onNextButtonClicked() throws IOException, SQLException {

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();



        if (offentligTransportRadioButton.isSelected()){
            currentBookingSingleton.setTransportId(2);
        }

        if (privatTransportRadioButton.isSelected()){
            currentBookingSingleton.setTransportId(1);
        }

        TransportDao transportDao = new TransportDao();

        Transport transport = (Transport) transportDao.get(currentBookingSingleton.getTransportId()).get();

        CurrentBookingSingleton.getInstance().setTransport(transport);





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

        sceneChanger.changeScene(scene,container, view, true);





    }

    @FXML
    public void onBackButtonClicked() throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        Scene scene = nextButton.getScene();
        sceneChanger.changeScene(scene, container,"redskaber", false);
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
