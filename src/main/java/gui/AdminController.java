package gui;

import bll.AuthService;
import gui.util.InputManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Optional;

// Controller til admin. Bruges til at oprette, slette, og tildele roller til brugere.

public class AdminController extends BaseController {

    @FXML private ListView<String> userListView;     // Liste over eksisterende brugernavne
    @FXML private TextField        usernameField;    // Inputfelt til nye brugernavne
    @FXML private PasswordField    passwordField;    // Input til nye passwords
    @FXML private ComboBox<String> roleComboBox;     // Dropdown-menu til de forskellige roller

    private final AuthService authService = new AuthService();                 // BLL til brugerhåndtering
    private final NotificationHelper notifier = new NotificationHelper(this);  // Hjælpeklasse til dialoger/notifikationer

    // Fylder combobox med roller og indlæser brugere ved visning
    @FXML
    private void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("Admin", "QA", "Operator"));
        refreshUserList();
    }

    // Opdaterer listen over brugere
    private void refreshUserList() {
        userListView.setItems(FXCollections.observableArrayList(authService.getAllUsernames()));
    }

    // Håndterer 'opret bruger'
    @FXML
    private void onCreateUserClick(ActionEvent event) {
        String u = usernameField.getText();
        String p = passwordField.getText();
        String r = roleComboBox.getValue();

        if (!InputManager.isFilled(u, p, r)) {
            notifier.warnMissingInput();
            return;
        }
        if (authService.userExists(u)) {
            notifier.warnUserExists(u);
            return;
        }

        authService.addUser(u, p, r);
        notifier.infoUserCreated(u, r);
        clearForm();
        refreshUserList();
    }

    // Håndterer 'slet bruger' (med bekræftelsesdialog)
    @FXML
    private void onDeleteUserClick(ActionEvent event) {
        String sel = userListView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            notifier.warnSelectUser();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Bekræft sletning");
        confirm.setHeaderText("Er du sikker på, at du vil slette denne bruger?");
        confirm.setContentText("Bruger: " + sel);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                authService.deleteUser(sel);   // slet i BLL/DB
                refreshUserList();             // opdater UI-listen
                notifier.infoUserDeleted(sel); // succes-besked
            } catch (Exception ex) {
                notifier.showWarning("Fejl", "Kunne ikke slette brugeren: " + ex.getMessage());
            }
        }
        // Hvis CANCEL: gør ingenting (ingen popup)
    }

    // Håndterer 'tildel rolle'
    @FXML
    private void onAssignRoleClick(ActionEvent event) {
        String sel = userListView.getSelectionModel().getSelectedItem();
        String r   = roleComboBox.getValue();

        if (!InputManager.isFilled(sel, r)) {
            notifier.warnSelectUserAndRole();
            return;
        }

        authService.assignRoleToUser(sel, r);
        notifier.infoRoleAssigned(sel, r);
        roleComboBox.getSelectionModel().clearSelection();
    }

    // Håndterer 'log ud' → tilbage til roleSelection
    @FXML
    private void onLogoutClick(ActionEvent event) {
        changeScene(View.ROLE_SELECTION, getStageFromEvent(event));
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }
}
