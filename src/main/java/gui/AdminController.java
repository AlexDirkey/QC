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


//Controller til admin. Bruges til at oprette, slette, og tildele roller til brugere.

public class AdminController extends BaseController {

    @FXML private ListView<String> userListView; //Liste over eksisterende brugernavne
    @FXML private TextField usernameField; // Inputfelt til nye brugernavne
    @FXML private PasswordField passwordField; //Input til nye passwords
    @FXML private ComboBox<String> roleComboBox; //Dropdown-menu til de forskellige roller

    private final AuthService authService = new AuthService(); //bll til brugerhåndtering
    private final NotificationHelper notifier = new NotificationHelper(this); //Hjælpeclass til dialoger/notifikationer

    // roller automatisk ved indlæsning - fylder combobox med roller, og indlæser brugere
    @FXML
    private void initialize() {

        roleComboBox.setItems(FXCollections.observableArrayList("Admin", "QA", "Operator"));
        refreshUserList();
    }

    //Opdater de nyeste brugernavne
    private void refreshUserList() {

        userListView.setItems(
                FXCollections.observableArrayList(authService.getAllUsernames()) //Henter alle brugernavne
        );
    }

    //Håndterer 'opret bruger'
    //Checker, om alle felter er udfyldt, om brugernavnet eksisterer

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

    // Håndterer 'slet bruger'.
    @FXML
    private void onDeleteUserClick(ActionEvent event) {

        String sel = userListView.getSelectionModel().getSelectedItem();
        if (sel == null) { notifier.warnSelectUser(); return; }
        authService.deleteUser(sel);
        notifier.infoUserDeleted(sel);
        refreshUserList();
    }

    //Håndterer 'tildel rolle'

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

    // Håndterer 'log ud', og skifter tilbage til "forsiden", roleSelection
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