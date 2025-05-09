package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminController {
    @FXML
    private ListView<String> userListView;

    public AdminController() {}

    @FXML
    private void onAssignRoleButtonClick(ActionEvent event) {
        String selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Tildel rolle");
            alert.setHeaderText(null);
            alert.setContentText("Rolle tildelt til: " + selectedUser);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Ingen valgt");
            alert.setHeaderText(null);
            alert.setContentText("Vælg en bruger først.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onViewLogsButtonClick(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Log visning");
        alert.setHeaderText(null);
        alert.setContentText("System logs er vist.");
        alert.showAndWait();
    }

    @FXML
    private void onManageUsersButtonClick(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Brugeradministration");
        alert.setHeaderText(null);
        alert.setContentText("Brugeradministration åbnet.");
        alert.showAndWait();
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
                    Stage stage = (Stage) userListView.getScene().getWindow();
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


