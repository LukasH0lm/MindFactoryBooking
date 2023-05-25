package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.utility.DatabaseUpdaterSingleton;
import com.monkeygang.mindfactorybooking.utility.MailSender;
import com.monkeygang.mindfactorybooking.utility.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class FinalizeController {


    public FinalizeController() {

    }

    public void initialize() {

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();
        finalizeTextArea.setText(currentBookingSingleton.toUIString());

    }

    @FXML
    private Button backButton;

    @FXML
    private Button cancelButton;

    @FXML
    private AnchorPane container;

    @FXML
    private Button doneButton;

    @FXML
    private TextArea finalizeTextArea;

    @FXML
    void onBackButtonClick(ActionEvent event) throws IOException {

        SceneChanger sceneChanger = new SceneChanger();
        Scene scene = backButton.getScene();
        sceneChanger.changeScene(scene, container, "customer", false);


    }

    @FXML
    void onCancelButtonClick(ActionEvent event) {

        //TODO: add confirm dialog

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    void onDoneButtonClick(ActionEvent event) throws SQLException, IOException {

        //TODO: Save booking to database

        DatabaseUpdaterSingleton databaseUpdaterSingleton = DatabaseUpdaterSingleton.getInstance();

        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newCachedThreadPool();

        executor.execute(() -> {
            try {
                databaseUpdaterSingleton.addCurrentBookingSingletonToDatabase();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });

        //TODO: Send email to customer and organization
        MailSender mailSender = new MailSender();
        //mailSender.sendMailToCustomer();


        //TODO: Send email to AS???

        //mailSender.sendMailToAdmin();


        //Show confirmation message

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Booking confirmation");
        alert.setHeaderText("Booking confirmed");
        alert.setContentText("Din booking er blevet bekræftet. Du modtager snart en mail med oplysningerne.");
        // Make sure that the confirmation dialog is always on top of other windows.
        alert.initOwner(doneButton.getScene().getWindow());
        alert.showAndWait();

        //TODO: Clear current booking singleton
        //sletter bookingen
        //burde kun slette hvis den er temp
        //CurrentBookingSingleton.getInstance().reset();
        //TODO: Go back to start screen
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();


    }


}
