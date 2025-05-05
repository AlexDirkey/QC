package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

public class AdminController {

    @FXML
    private ListView<String> userListView;

    @FXML
    private void onAssignRoleButtonClick(ActionEvent event) {
        String selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tildel rolle");
            alert.setHeaderText(null);
            alert.setContentText("Rolle tildelt til: " + selectedUser);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ingen valgt");
            alert.setHeaderText(null);
            alert.setContentText("Vælg en bruger først.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onViewLogsButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Log visning");
        alert.setHeaderText(null);
        alert.setContentText("System logs er vist.");
        alert.showAndWait();
    }

    @FXML
    private void onManageUsersButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Brugeradministration");
        alert.setHeaderText(null);
        alert.setContentText("Brugeradministration åbnet.");
        alert.showAndWait();
    }
}

