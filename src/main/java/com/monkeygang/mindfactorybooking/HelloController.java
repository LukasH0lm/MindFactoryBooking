package com.monkeygang.mindfactorybooking;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.Locale;


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
    private Button nextWeekButton;

    @FXML
    private Button previousWeekButton;

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

    private final DecimalFormat df = new DecimalFormat("00.00");

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

        // Vi sætter decimalformatet til at være med punktum i stedet for komma, da vi skal bruge det til at lave tidspunkter
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));


        double time = 07.00;
        double timeLabelsHeight = 0.0;

        while (time <= 18.00) {
            // Vi laver flere labels med tidspunkterne indtil vi når 18.00
            Label label = new Label(df.format(time));
            label.setPrefWidth(30);
            label.setPrefHeight(15);
            label.setMaxWidth(label.getPrefWidth());
            label.setMaxHeight(label.getPrefHeight());
            label.setMinWidth(label.getPrefWidth());
            label.setMinHeight(label.getPrefHeight());

            // Tilføjer labels til vBoxTid
            vBoxTid.getChildren().add(label);

            // Vi tilføjer 1.00 til tiden, så vi kan lave labels for hver  time
            time += 1.00;


            // Vi tilføjer højden af labelen til timeLabelsHeight, så vi kan sætte højden på hBoxCalendar og vBoxTid
            timeLabelsHeight += label.getPrefHeight();
            timeLabelsHeight += vBoxTid.getSpacing();

        }


        //Vi sætter højden på hBoxCalendar og vBoxTid, samt linjerne, som opdeler vores kalender.
        // Vi minusser med vBoxTid.getSpacing(), da når der er spacing i en vBox, så bliver der tilføjet spacing efter det sidste element, og vi vil have at kalenderen slutter ved det sidste tidspunkt.
        hBoxCalendar.setPrefHeight(timeLabelsHeight - vBoxTid.getSpacing());
        hBoxCalendar.setMaxHeight(hBoxCalendar.getPrefHeight());
        hBoxCalendar.setMinHeight(hBoxCalendar.getPrefHeight());


        vBoxTid.setPrefHeight(timeLabelsHeight - vBoxTid.getSpacing());
        vBoxTid.setMaxHeight(vBoxTid.getPrefHeight());
        vBoxTid.setMinHeight(vBoxTid.getPrefHeight());


        hBoxLineOne.setEndY(timeLabelsHeight - vBoxTid.getSpacing());
        hBoxLineTwo.setEndY(timeLabelsHeight - vBoxTid.getSpacing());
        hBoxLineThree.setEndY(timeLabelsHeight - vBoxTid.getSpacing());
        hBoxLineFour.setEndY(timeLabelsHeight - vBoxTid.getSpacing());
        hBoxLineFive.setEndY(timeLabelsHeight - vBoxTid.getSpacing());
        hBoxLineSix.setEndY(timeLabelsHeight - vBoxTid.getSpacing());















    }
}