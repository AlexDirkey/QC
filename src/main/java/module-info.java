module org.example.qc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.qc to javafx.fxml;
    opens gui to javafx.fxml;

    exports org.example.qc;
    exports gui;
    exports bll;
    exports dal;
    exports model;

}