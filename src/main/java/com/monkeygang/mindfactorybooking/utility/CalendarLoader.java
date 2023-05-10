package com.monkeygang.mindfactorybooking.utility;

import com.monkeygang.mindfactorybooking.Controller.CalendarController;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.text.DecimalFormat;

public class CalendarLoader {

    CalendarController controller;

    private final DecimalFormat df = new DecimalFormat("00.00");

    public double heightPrLabel = 0.0;


    public CalendarLoader(CalendarController controller) {
        this.controller = controller;
        this.calendarAnchorPane = controller.getCalendarAnchorPane();
        this.hBoxCalendar = controller.getHBoxCalendar();
        this.vBoxTid = controller.getvBoxTid();
        this.timeComboBox = controller.getTimeComboBox();
        this.datePicker = controller.getDatePicker();
        this.ugeLabel = controller.getUgeLabel();
        this.paneMandag = controller.getPaneMandag();
        this.paneTirsdag = controller.getPaneTirsdag();
        this.paneOnsdag = controller.getPaneOnsdag();
        this.paneTorsdag = controller.getPaneTorsdag();
        this.paneFredag = controller.getPaneFredag();
        this.paneLordag = controller.getPaneLordag();
        this.paneSondag = controller.getPaneSondag();






    }

    public void generateCalendar(double calenderStartTime) {


        double calendarEndTime = Double.parseDouble(controller.getTimeComboBox().getValue().replace(":", "."));
        double timeLabelsHeight = 0.0;


        controller.getvBoxTid().getChildren().clear();

        //har lavet det om til et for loop fordi vi gør noget for hvert trin
        //er der ikke en smartere måde at iterate i gennem labelsne?
        for (double i = calenderStartTime; i <= calendarEndTime; i++) {

            // Vi laver flere labels med tidspunkterne indtil vi når 18.00
            Label label = new Label(df.format(i));

            // Tilføjer labels til vBoxTid
            controller.getvBoxTid().getChildren().add(label);




        }

        for (Node node : controller.getvBoxTid().getChildren()) {

            if (node.getClass() == Label.class) {

                Label label = (Label) node;

                // Vi tilføjer højden af labelen til timeLabelsHeight, så vi kan sætte højden på hBoxCalendar og vBoxTid

                label.setPrefWidth(30);
                label.setPrefHeight(15);
                label.setMaxWidth(label.getPrefWidth());
                label.setMaxHeight(label.getPrefHeight());
                label.setMinWidth(label.getPrefWidth());
                label.setMinHeight(label.getPrefHeight());


                Line timeLine = new Line();
                //timeLine.setStartX(0);
                //timeLine.setStartY(0);

                hBoxCalendar = controller.getHBoxCalendar();

                timeLine.setEndX(controller.getHBoxCalendar().getPrefWidth());
                timeLine.setLayoutX(hBoxCalendar.getLayoutX());
                timeLine.setLayoutY(hBoxCalendar.getLayoutY() + timeLabelsHeight);
                timeLine.setStroke(Color.rgb(169,169, 169));


                calendarAnchorPane = controller.getCalendarAnchorPane();

                calendarAnchorPane.getChildren().add(timeLine);


                timeLabelsHeight += label.getPrefHeight();
                timeLabelsHeight += controller.getvBoxTid().getSpacing();

                controller.heightPrLabel = label.getPrefHeight();





            }

        }


        //Vi sætter højden på hBoxCalendar og vBoxTid, samt linjerne, som opdeler vores kalender.
        // Vi minusser med vBoxTid.getSpacing(), da når der er spacing i en vBox, så bliver der tilføjet spacing efter det sidste element, og vi vil have at kalenderen slutter ved det sidste tidspunkt.
        hBoxCalendar.setPrefHeight(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        hBoxCalendar.setMaxHeight(hBoxCalendar.getPrefHeight());
        hBoxCalendar.setMinHeight(hBoxCalendar.getPrefHeight());
        // ikke fast værdi her (15) - SW
        vBoxTid.setPrefHeight(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        vBoxTid.setMaxHeight(vBoxTid.getPrefHeight());
        vBoxTid.setMinHeight(vBoxTid.getPrefHeight());
        hBoxLineOne.setEndY(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        hBoxLineTwo.setEndY(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        hBoxLineThree.setEndY(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        hBoxLineFour.setEndY(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        hBoxLineFive.setEndY(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));
        hBoxLineSix.setEndY(timeLabelsHeight - (vBoxTid.getSpacing() + heightPrLabel));


    }


    private AnchorPane calendarAnchorPane;
    private DatePicker datePicker;
    private Label ugeLabel;
    private HBox hBoxCalendar;
    private Line hBoxLineFive;
    private Line hBoxLineFour;
    private Line hBoxLineOne;
    private Line hBoxLineSix;
    private Line hBoxLineThree;
    private Button nextWeekButton;
    private Button previousWeekButton;
    private Line hBoxLineTwo;
    private Pane paneFredag;
    private Pane paneLordag;
    private Pane paneMandag;
    private Pane paneOnsdag;
    private Pane paneSondag;
    Pane paneTirsdag;
    Pane paneTorsdag;
    VBox vBoxTid;
    ComboBox<String> timeComboBox;


}
