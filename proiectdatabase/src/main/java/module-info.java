module com.example.proiectdatabase {
    requires javafx.graphics;
    requires java.sql;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.proiectdatabase to javafx.fxml;
    exports com.example.proiectdatabase;
}