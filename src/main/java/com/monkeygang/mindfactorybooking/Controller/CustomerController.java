package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.Objects.Customer;
import com.monkeygang.mindfactorybooking.Objects.Organisation_type;
import com.monkeygang.mindfactorybooking.utility.AlertHandler;
import com.monkeygang.mindfactorybooking.utility.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerController {


    public CustomerController() {

    }

    public void initialize() {

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();

        if (currentBookingSingleton.getCustomer() != null) {
            nameTextField.setText(currentBookingSingleton.getCustomer().getName());
            titelTextField.setText(currentBookingSingleton.getCustomer().getTitle());
            mailTextField.setText(currentBookingSingleton.getCustomer().getEmail());
            telephoneTextField.setText(currentBookingSingleton.getCustomer().getPhone());
        }

        if (currentBookingSingleton.getOrganization().getType().equals(Organisation_type.SCHOOL)) {
            subjectLabel.setVisible(true);
            subjectTextField.setVisible(true);
            if (currentBookingSingleton.getSubject() != null) {
                subjectTextField.setText(currentBookingSingleton.getSubject().getName());
            }
        } else if (currentBookingSingleton.getOrganization().getType().equals(Organisation_type.PRIVATE)) {
            subjectLabel.setVisible(false);
            subjectTextField.setVisible(false);
        }

    }


    @FXML
    private Button backButton;

    @FXML
    private Button cancelButton;

    @FXML
    private AnchorPane container;

    @FXML
    private TextField mailTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button nextButton;

    @FXML
    private TextField telephoneTextField;

    @FXML
    private TextField titelTextField;

    @FXML
    private TextField amountOfPeopleTextField;

    @FXML
    private Label subjectLabel;

    @FXML
    private TextField subjectTextField;

    @FXML
    void onBackButtonClick(ActionEvent event) throws IOException {

        SceneChanger sceneChanger = new SceneChanger();

        Scene scene = container.getScene();

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
    void onCancelButtonClick(ActionEvent event) {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();


    }

    @FXML
    void onNextButtonClick(ActionEvent event) throws IOException {

        if (nameTextField.getText().isEmpty() || titelTextField.getText().isEmpty() || mailTextField.getText().isEmpty() || telephoneTextField.getText().isEmpty() || amountOfPeopleTextField.getText().isEmpty()) {

            AlertHandler alertHandler = new AlertHandler();

            Stage stage = (Stage) container.getScene().getWindow();

            alertHandler.showAlert(stage, "Error", "Missing fields", "Udfyld venligst alle felter");

            return;

        }

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();

        Customer customer = new Customer(
                nameTextField.getText(),
                titelTextField.getText(),
                mailTextField.getText(),
                telephoneTextField.getText()
        );

        //i know this is janky af but im too tired, i just want to go to sleep, let the darkness embrace me
        currentBookingSingleton.setCustomer(customer);
        currentBookingSingleton.getBooking().setCustomer(customer);

        currentBookingSingleton.getBooking().setAmount_of_people(Integer.parseInt(amountOfPeopleTextField.getText()));

        SceneChanger sceneChanger = new SceneChanger();

        Scene scene = container.getScene();

        sceneChanger.changeScene(scene, container, "finalize", true);

    }

}
