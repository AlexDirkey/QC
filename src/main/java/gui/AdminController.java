package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminController extends BaseController {

    @FXML
    private ListView<String> userListView;

    @FXML
    private void onAssignRoleButtonClick(ActionEvent event) {
        String selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            showInfo("Tildel rolle", "Rolle tildelt til: " + selectedUser);
        } else {
            showWarning("Ingen valgt", "Vælg en bruger først.");
        }
    }

    @FXML
    private void onViewLogsButtonClick(ActionEvent event) {
        showInfo("Log visning", "System logs er vist.");
    }

    @FXML
    private void onManageUsersButtonClick(ActionEvent event) {
        showInfo("Brugeradministration", "Brugeradministration åbnet.");
    }
}
