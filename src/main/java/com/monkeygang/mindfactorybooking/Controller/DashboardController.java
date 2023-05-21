package com.monkeygang.mindfactorybooking.Controller;


import com.monkeygang.mindfactorybooking.Dao.BookingDao;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;



public class DashboardController {

    private  final List<Image> images = new ArrayList<>();

    private Timer timer;

    BookingDao bookingDao = new BookingDao();

    private  int currentImageIndex = 0;

    private boolean isUpdating = true;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public DashboardController() throws SQLException, IOException {
    }

    public void initialize() throws SQLException, IOException {

        customImageClicked(customImageview);

        images.add(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/defaultSlideShowPicture1.jpg"));
        images.add(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/defaultSlideShowPicture2.jpg"));
        images.add(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/defaultSlideShowPicture3.jpg"));
        images.add(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/defaultSlideShowPicture4.jpg"));
        images.add(new Image("file:src/main/resources/com/monkeygang/mindfactorybooking/defaultSlideShowPicture5.jpg"));

        generateStatistics();

        if (isUpdating){
            displayImages();
            updateStatistics();
        }












    }

    public void displayImages(){

        Timer timer = new Timer();
        timer.schedule(new java.util.TimerTask() {

            public void run() {


                if (!isUpdating){
                    timer.cancel();
                }

                if (currentImageIndex >= images.size()) {
                    currentImageIndex = 0;
                }
                customImageview.setImage(images.get(currentImageIndex));
                currentImageIndex++;


            }
        }, 0,5000);






    }
    public void customImageClicked(ImageView imageview) {

        imageview.setOnMouseClicked(event -> {

            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() == 2) {

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Vælg billede");
                    fileChooser.getExtensionFilters().addAll();
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

                    List<File> files = fileChooser.showOpenMultipleDialog(new Stage());


                    //Vi tjekker om der er valgt en fil/filer
                    if (files != null) {

                        //Vi fjerner de forrige billeder fra listen
                        images.clear();

                        //Vi tilføjer de nye billeder til listen
                        for (File file : files) {
                            images.add(new Image(file.toURI().toString()));
                        }
                    }


                    }
                }

        });


    }


    public void onCalendarButtonClick() throws IOException {


        stopUpdate();

        Stage currentStage = (Stage) dCalendarButton.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/monkeygang/mindfactorybooking/view/calendar-view.fxml"));
        Parent newRoot = fxmlLoader.load();

        Scene newScene = new Scene(newRoot);

        currentStage.setScene(newScene);
        currentStage.setTitle("Mind Factory Booking - Admin");
        currentStage.show();


    }

    public String udregnBelægningIProcent(int antalBookinger, int belægning){



        double result = (double) antalBookinger / belægning * 100;

        return df.format(result);

    }


    public void updateStatistics() throws SQLException, IOException {

        Timer timer = new Timer();
        timer.schedule(new java.util.TimerTask() {

            public void run() {

                if (!isUpdating){
                    timer.cancel();
                }


                Platform.runLater(() -> {
                    try {
                        generateStatistics();
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }
                });



            }
        }, 0,5000);

    }


    public void generateStatistics() throws SQLException, IOException {
        statistikBarChart.getData().clear();

        final int ungdomsUddannelseBelægning = 36;
        final int erhvervsAkademiskUddannelseBelægning = 24;
        final int eccoBelægning = 18;
        final int tønderKommuneBelægning = 44;



        int easvAntalBookinger = 0; //EASV
        int tGAntalBookinger = 0; //Tønder Gymnasium
        int dBGAntalBookinger = 0; //Det Blå Gymnasium
        int tKAntalBookinger = 0; //Tønder Kommune
        int eAntalBookinger = 0;   //ECCO


        List<Booking> bookingsForTheChosenYear = new LinkedList<>();

        bookingsForTheChosenYear = bookingDao.getAll();



        for (Booking booking : bookingsForTheChosenYear) {

            switch (bookingDao.getOrganisation(booking).toString()) {
                case "EASV" -> easvAntalBookinger++;
                case "Tønder Gymnasium" -> tGAntalBookinger++;
                case "Det Blå Gymnasium" -> dBGAntalBookinger++;
                case "Tønder Kommune" -> tKAntalBookinger++;
                case "ECCO" -> eAntalBookinger++;

            }

        }


        BarChart.Series<String, Integer> organisationerSeries = new BarChart.Series<>();

        organisationerSeries.getData().add(new XYChart.Data<>("EASV\n" + "Belægning i dage: " + easvAntalBookinger
                + "\nBelægning i procent: " + udregnBelægningIProcent(easvAntalBookinger, erhvervsAkademiskUddannelseBelægning) + "%", easvAntalBookinger));

        organisationerSeries.getData().add(new XYChart.Data<>("Tønder Gymnasium\n" + "Belægning i dage: " + tGAntalBookinger
                + "\nBelægning i procent: " + udregnBelægningIProcent(tGAntalBookinger, ungdomsUddannelseBelægning) + "%", tGAntalBookinger));

        organisationerSeries.getData().add(new XYChart.Data<>("Det Blå Gymnasium\n"+ "Belægning i dage: " + dBGAntalBookinger
                + "\nBelægning i procent: " + udregnBelægningIProcent(dBGAntalBookinger, ungdomsUddannelseBelægning) + "%", dBGAntalBookinger));

        organisationerSeries.getData().add(new XYChart.Data<>("Tønder Kommune\n" + "Belægning i dage: " + tKAntalBookinger
                + "\nBelægning i procent: " + udregnBelægningIProcent(tKAntalBookinger, tønderKommuneBelægning) + "%", tKAntalBookinger));

        organisationerSeries.getData().add(new XYChart.Data<>("Ecco\n" + "Belægning i dage: " + eAntalBookinger
                + "\nBelægning i procent: " + udregnBelægningIProcent(eAntalBookinger, eccoBelægning) + "%", eAntalBookinger));


        xAxis.setLabel("Organisationer");
        yAxis.setLabel("Belægning");


        statistikBarChart.setTitle("Belægning for organisationer");
        statistikBarChart.setLegendVisible(false);
        statistikBarChart.setAnimated(false);


        statistikBarChart.getData().add(organisationerSeries);




    }


    public void startUpdate() {
        isUpdating = true;
    }

    public void stopUpdate() {
        isUpdating = false;
    }

    @FXML
    private Button dCalendarButton;

    @FXML
    private ImageView customImageview;

    @FXML
    private BarChart<String, Integer> statistikBarChart;


    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;



}


