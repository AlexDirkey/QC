module org.example.qc {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.qc to javafx.fxml;
    exports org.example.qc;
}