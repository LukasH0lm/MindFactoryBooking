module com.monkeygang.mindfactorybooking {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.monkeygang.mindfactorybooking to javafx.fxml;
    exports com.monkeygang.mindfactorybooking;
}