package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

/**
 * OperatorController håndterer OperatorView GUI.
 */
public class OperatorController {

    @FXML
    private ListView<String> pendingOrdersList;

    @FXML
    private ListView<String> inReviewOrdersList;

    @FXML
    private ListView<String> completedOrdersList;

    @FXML
    private TextField orderNumberField;

    @FXML
    private Label statusLabel;

    @FXML
    private void onAddCommentButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tilføj kommentar");
        alert.setHeaderText(null);
        alert.setContentText("Funktionen til at tilføje kommentarer er endnu ikke implementeret.");
        alert.showAndWait();
    }

    @FXML
    private void onUploadButtonClick() {
        String orderNumber = orderNumberField.getText().trim();
        if (orderNumber.isEmpty()) {
            statusLabel.setText("Indtast et ordrenummer.");
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Vælg billede til upload");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Billeder", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                statusLabel.setText("Billede valgt: " + selectedFile.getName());
            } else {
                statusLabel.setText("Ingen fil valgt.");
            }
        }
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent event) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Log ud");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Er du sikker på, at du vil logge ud?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginView.fxml"));
                    Scene scene = new Scene(loader.load());
                    Stage stage = (Stage) orderNumberField.getScene().getWindow();
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


