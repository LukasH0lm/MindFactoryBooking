package com.monkeygang.mindfactorybooking;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.time.LocalDate;
import java.time.temporal.IsoFields;


public class HelloController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label ugeLabel;
    @FXML
    private HBox hBoxCalendar;

    @FXML
    private Line hBoxLineFive;

    @FXML
    private Line hBoxLineFour;

    @FXML
    private Line hBoxLineOne;

    @FXML
    private Line hBoxLineSix;

    @FXML
    private Line hBoxLineThree;

    @FXML
    private Line hBoxLineTwo;

    @FXML
    private VBox vBoxFredag;

    @FXML
    private VBox vBoxLørdag;

    @FXML
    private VBox vBoxMandag;

    @FXML
    private VBox vBoxOnsdag;

    @FXML
    private VBox vBoxSøndag;

    @FXML
    private VBox vBoxTid;

    @FXML
    private VBox vBoxTirsdag;

    @FXML
    private VBox vBoxTorsdag;



    public void initialize() {

        // Vi sætter startdatoen til at være dagens dato
        datePicker.setValue(LocalDate.now());

        // vi sætter ugenummert til at være ugenummeret for den valgte dato, når programmet starter.
        ugeLabel.setText("Uge " + datePicker.getValue().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));


        // Vi sætter ugenummeret til at være ugenummeret for den valgte dato, hver gang der sker ændringer i datePicker
        datePicker.setOnAction(event -> {
            int weekNumber = datePicker.getValue().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            ugeLabel.setText("Uge " + weekNumber);


            // her skal vi så også se på hvilke aftaler der er gemt i for den uge, som der er valgt,
            // og så ud fra de aftaler lave firkanter, som bliver vist i kalenderen





        });


        double time = 10.0;
        double timeLabelsHeight = 0.0;

        while (time <= 18.0) {

            //formatere tiden til at vise 2 decimaler, samt sætte 0 foran hvis der kun er 1 tal
            String hour = String.format("%02d", (int) time);
            // Vi tjekker om tiden er 30 minutter, og hvis den er, så sætter vi minutterne til 30, ellers sætter vi dem til 00
            String minute = (time % 1 == 0.5) ? "30" : "00";
            String timeStr = hour + "." + minute;

            // Vi laver flere labels med tidspunkterne indtil vi når 18.00
            Label label = new Label(timeStr);
            label.setPrefWidth(30);
            label.setPrefHeight(15);
            label.setMaxWidth(label.getPrefWidth());
            label.setMaxHeight(label.getPrefHeight());
            label.setMinWidth(label.getPrefWidth());
            label.setMinHeight(label.getPrefHeight());

            // Tilføjer labels til vBoxTid
            vBoxTid.getChildren().add(label);

            // Vi tilføjer 0.5 til tiden, så vi kan lave labels for hver halve time
            time += 0.5;


            // Vi tilføjer højden af labelen til timeLabelsHeight, så vi kan sætte højden på hBoxCalendar og vBoxTid
            timeLabelsHeight += label.getPrefHeight();
            timeLabelsHeight += vBoxTid.getSpacing();

        }


        //Vi sætter højden på hBoxCalendar og vBoxTid, samt linjerne, som opdeler vores kalender.
        hBoxCalendar.setPrefHeight(timeLabelsHeight);
        hBoxCalendar.setMaxHeight(hBoxCalendar.getPrefHeight());
        hBoxCalendar.setMinHeight(hBoxCalendar.getPrefHeight());


        vBoxTid.setPrefHeight(timeLabelsHeight);
        vBoxTid.setMaxHeight(vBoxTid.getPrefHeight());
        vBoxTid.setMinHeight(vBoxTid.getPrefHeight());

        hBoxLineOne.setEndY(timeLabelsHeight);
        hBoxLineTwo.setEndY(timeLabelsHeight);
        hBoxLineThree.setEndY(timeLabelsHeight);
        hBoxLineFour.setEndY(timeLabelsHeight);
        hBoxLineFive.setEndY(timeLabelsHeight);
        hBoxLineSix.setEndY(timeLabelsHeight);















    }
}