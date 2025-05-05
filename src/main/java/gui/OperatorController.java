package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class OperatorController {

    @FXML
    private TextField orderNumberField;

    @FXML
    private Label statusLabel;

    @FXML
    private void onUploadButtonClick() {
        String orderNumber = orderNumberField.getText().trim();

        if (orderNumber.isEmpty()) {
            statusLabel.setText("Indtast et ordrenummer.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vælg billede til upload");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Billeder", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Her kan du tilføje kode til at gemme filen til databasen eller filesystemet
            statusLabel.setText("Billede valgt: " + selectedFile.getName());
        } else {
            statusLabel.setText("Ingen fil valgt.");
        }
    }
}

