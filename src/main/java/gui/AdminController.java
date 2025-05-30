package gui;

import bll.AuthService;
import gui.util.InputManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller til admin-panelet, nu med kortere, klar logik ved brug af hjælpeklasser.
 */
public class AdminController extends BaseController {

    @FXML private ListView<String> userListView;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    private final AuthService authService = new AuthService();
    private final NotificationHelper notifier = new NotificationHelper(this);

    @FXML
    private void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("Admin", "QA", "Operator"));
        refreshUserList();
    }

    private void refreshUserList() {
        userListView.setItems(
                FXCollections.observableArrayList(authService.getAllUsernames())
        );
    }

    @FXML
    private void onCreateUserClick(ActionEvent event) {
        String u = usernameField.getText();
        String p = passwordField.getText();
        String r = roleComboBox.getValue();

        if (!InputManager.isFilled(u, p, r)) {
            notifier.warnMissingInput(); return;
        }
        if (authService.userExists(u)) {
            notifier.warnUserExists(u); return;
        }
        authService.addUser(u, p, r);
        notifier.infoUserCreated(u, r);
        clearForm();
        refreshUserList();
    }

    @FXML
    private void onDeleteUserClick(ActionEvent event) {
        String sel = userListView.getSelectionModel().getSelectedItem();
        if (sel == null) { notifier.warnSelectUser(); return; }
        authService.deleteUser(sel);
        notifier.infoUserDeleted(sel);
        refreshUserList();
    }

    @FXML
    private void onAssignRoleClick(ActionEvent event) {
        String sel = userListView.getSelectionModel().getSelectedItem();
        String r   = roleComboBox.getValue();
        if (!InputManager.isFilled(sel, r)) {
            notifier.warnSelectUserAndRole(); return;
        }
        authService.assignRoleToUser(sel, r);
        // evt. notifier.infoRoleAssigned(sel, r);
    }

    @FXML
    private void onLogoutClick(ActionEvent event) {
        // Gå tilbage til rollevalg
        changeScene(View.ROLE_SELECTION, getStageFromEvent(event));
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }
}