module org.example.qc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.qc to javafx.fxml;
    exports org.example.qc;
    exports gui;
    opens gui to javafx.fxml;
}