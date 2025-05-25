package gui;

import bll.AuthService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminController extends BaseController {

    @FXML
    private ListView<String> userListView;


    private final AuthService authService = new AuthService();

    // Fjernet felter: usernameField, passwordField, roleComboBox

    @FXML
    private void initialize() {
        loadUsers();
    }

    private void loadUsers() {
        try {
            var usernames = authService.getAllUsernames();
            if (usernames != null) {
                userListView.setItems(FXCollections.observableArrayList(usernames));
            }
        } catch (Exception e) {
            // Ignorer fejl helt for at muliggøre adgang uden forbindelse
        }

    }

    @FXML
    private void onCreateUserClick(ActionEvent event) {
        showInfo("Opret bruger", "Denne funktion er deaktiveret i demoversionen.");
}

@FXML
private void onDeleteUserClick(ActionEvent event) {
    String selectedUser = userListView.getSelectionModel().getSelectedItem();
    if (selectedUser != null) {
        authService.deleteUser(selectedUser);
        showInfo("Bruger slettet", "Bruger " + selectedUser + " er blevet slettet.");
        loadUsers();
    } else {
        showWarning("Ingen valgt", "Vælg en bruger der skal slettes.");
    }
}

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

@FXML
private void onLogoutButtonClick(ActionEvent event) {
    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    changeScene("/gui/RoleSelectionView.fxml", stage);
}
}
