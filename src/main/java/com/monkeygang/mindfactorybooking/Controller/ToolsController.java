package com.monkeygang.mindfactorybooking.Controller;

import com.monkeygang.mindfactorybooking.Dao.ToolsDao;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.Objects.Redskaber;
import com.monkeygang.mindfactorybooking.utility.SceneChanger;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ToolsController {

    public ToolsController() {

    }


    public void initialize() throws SQLException, IOException {

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();

        //Vi tilføjer alle redskaber fra databasen til listen.
        ToolsDao toolsDao = new ToolsDao();
        tilgængeligeRedskaber.getItems().addAll(toolsDao.getAll());

        if (!currentBookingSingleton.getCurrentRedskaber().isEmpty()) {
            redskaberValgt.getItems().addAll(currentBookingSingleton.getCurrentRedskaber());

            for (Redskaber redskaber : currentBookingSingleton.getCurrentRedskaber()) {
                tilgængeligeRedskaber.getItems().remove(redskaber);
            }
        }


    }


    @FXML
    void onNextButtonClicked() throws IOException {

        for (Redskaber redskaber : redskaberValgt.getItems()) {

            CurrentBookingSingleton.getInstance().setCurrentRedskaber(redskaber);

        }


        SceneChanger sceneChanger = new SceneChanger();

        Scene scene = næsteButton.getScene();

        sceneChanger.changeScene(scene, container, "transport", true);


    }

    @FXML
    public void onBackButtonClicked() throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        Scene scene = næsteButton.getScene();
        sceneChanger.changeScene(scene, container, "catering", false);
    }

    @FXML
    public void onCancelButtonClick() throws IOException {

        Stage stage = (Stage) næsteButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    public void rightArrowClicked() throws IOException {

        Redskaber redskaber = tilgængeligeRedskaber.getSelectionModel().getSelectedItem();


        if (redskaber != null) {
            tilgængeligeRedskaber.getItems().remove(redskaber);
            redskaberValgt.getItems().add(redskaber);
        }

    }

    @FXML
    public void leftArrowClicked() throws IOException {

        Redskaber redskaber = redskaberValgt.getSelectionModel().getSelectedItem();

        if (redskaber != null) {
            redskaberValgt.getItems().remove(redskaber);
            tilgængeligeRedskaber.getItems().add(redskaber);
        }


    }


    @FXML
    private Button annullerButton;

    @FXML
    private Button arrowLeft;

    @FXML
    private Button arrowRight;

    @FXML
    private AnchorPane container;

    @FXML
    private Button næsteButton;

    @FXML
    private ListView<Redskaber> redskaberValgt;

    @FXML
    private Button tilbageButton;

    @FXML
    private ListView<Redskaber> tilgængeligeRedskaber;

}
