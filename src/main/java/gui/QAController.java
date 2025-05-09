package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;

public class QAController {
    @FXML
    private ListView<String> reviewOrdersList;

    public QAController() {}

    @FXML
    private void onApproveOrderClick(ActionEvent event) {
        String selected = reviewOrdersList.getSelectionModel().getSelectedItem();
        Alert alert;
        if (selected != null) {
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Godkendelse");
            alert.setHeaderText(null);
            alert.setContentText("Ordre godkendt: " + selected);
            alert.showAndWait();
        } else {
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Ingen valgt");
            alert.setHeaderText(null);
            alert.setContentText("Vælg en ordre først.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onRejectOrderClick(ActionEvent event) {
        String selected = reviewOrdersList.getSelectionModel().getSelectedItem();
        Alert alert;
        if (selected != null) {
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Afvist");
            alert.setHeaderText(null);
            alert.setContentText("Ordre afvist: " + selected);
            alert.showAndWait();
        } else {
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Ingen valgt");
            alert.setHeaderText(null);
            alert.setContentText("Vælg en ordre først.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent event) {
        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Bekræft logud");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Er du sikker på, at du vil logge ud?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginView.fxml"));
                    Scene scene = new Scene(loader.load());
                    Stage stage = (Stage) reviewOrdersList.getScene().getWindow();
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
