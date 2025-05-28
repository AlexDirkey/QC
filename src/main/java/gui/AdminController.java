package gui;

import bll.AuthService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminController extends BaseController {

    @FXML
    private ListView<String> userListView;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    private final AuthService authService = new AuthService();

    @FXML
    private void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("Admin", "QA", "Operator"));
        loadUsers();
    }

    private void loadUsers() {
        userListView.setItems(FXCollections.observableArrayList(authService.getAllUsernames()));
    }

    @FXML
    private void onCreateUserClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showWarning("Manglende input", "Udfyld alle felter for at oprette bruger.");
            return;
        }

        if (authService.userExists(username)) {
            showWarning("Eksisterende bruger", "Brugernavnet er allerede i brug.");
            return;
        }

        authService.addUser(username, password, role);
        showInfo("Bruger oprettet", "Bruger " + username + " blev oprettet som " + role + ".");
        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        loadUsers();
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
        String role = roleComboBox.getValue();

        if (selectedUser != null && role != null) {
            authService.assignRoleToUser(selectedUser, role);
            showInfo("Rolle tildelt", "Brugeren " + selectedUser + " blev tildelt rollen " + role + ".");
        } else {
            showWarning("Manglende valg", "Vælg både en bruger og en rolle.");
        }
    }

    @FXML
    private void onViewLogsButtonClick(ActionEvent event) {
        showInfo("Log visning", "System logs vises her.");
    }

    @FXML
    private void onManageUsersButtonClick(ActionEvent event) {
        showInfo("Brugeradministration", "Administrationspanel åbnet.");
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent event) {
        changeScene("/gui/LoginView.fxml", getStageFromEvent(event));
    }
}