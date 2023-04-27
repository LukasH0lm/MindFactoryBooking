module com.monkeygang.mindfactorybooking {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jfree.jfreechart;
    requires itextpdf;
    requires java.desktop;
    requires jakarta.mail;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;


    opens com.monkeygang.mindfactorybooking to javafx.fxml;
    exports com.monkeygang.mindfactorybooking;
}