package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.Dao.OrganisationDao;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.Objects.Organization;
import com.monkeygang.mindfactorybooking.utility.AlertHandler;
import com.monkeygang.mindfactorybooking.utility.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class OrganizationController {

    OrganisationDao organisationDao = new OrganisationDao();

    CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();

    public OrganizationController() throws SQLException, IOException {

    }

    public void initialize() throws SQLException, IOException {

        organisationCombobox.getItems().addAll(organisationDao.getAll());

        if (currentBookingSingleton.getOrganization() != null) {
            organisationCombobox.setValue(currentBookingSingleton.getOrganization());
        }

    }

    @FXML
    private Button cancelButton;

    @FXML
    private AnchorPane container;

    @FXML
    private ComboBox<Organization> organisationCombobox;

    @FXML
    private Button nextButton;

    @FXML
    void onCancelButtonClick(ActionEvent event) {

        System.out.println("Cancel button clicked");

        //TODO: add cancel button functionality

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    void onNextButtonClick(ActionEvent event) throws IOException {

        System.out.println("organization selected: " + organisationCombobox.getValue().getId());

        //this is a bad way to do this, but it works for now

        if (organisationCombobox.getValue().getId() == -1) {
            System.out.println("No organization selected");

            showNoOrganizationAlert();
            return;
        }


        currentBookingSingleton = CurrentBookingSingleton.getInstance();

        currentBookingSingleton.setCurrentOrganization(organisationCombobox.getValue());

        //inspired by https://www.youtube.com/watch?v=cqskg3DYH8g

        Scene scene = nextButton.getScene();

        SceneChanger sceneChanger = new SceneChanger();

        sceneChanger.changeScene(scene, container, "booking", true);


    }

    public void showNoOrganizationAlert() {

        Stage stage = (Stage) nextButton.getScene().getWindow();

        AlertHandler alertHandler = new AlertHandler();

        alertHandler.showAlert(stage,
                "No organization selected",
                "Please select an organization",
                "Du har ikke valgt en organisation. Vælg venligst en organisation for at fortsætte.");

    }

}
