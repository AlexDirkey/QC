package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import gui.NotificationHelper;

public class LoginController extends BaseController {
    private String selectedRole;
    private final NotificationHelper notifier = new NotificationHelper(this);

    //Modtager rollen fra Roleselector
    public void setSelectedRole(String role) {
        this.selectedRole = role;
    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        if (selectedRole == null) {
            notifier.showWarning("Ingen rolle valgt",
                    "Gå tilbage og vælg en rolle før du logger ind.");
            return;
        }
        try {
            // Konverterer String → enum og skift scene
            View target = View.valueOf(selectedRole.toUpperCase());
            changeScene(target, getStageFromEvent(event));
        } catch (IllegalArgumentException e) {
            notifier.showWarning("Ukendt rolle",
                    "Rollen '" + selectedRole + "' er ikke understøttet.");
        }
    }

    @FXML
    private void onBackButtonClick(ActionEvent event) {
        // Gå tilbage til rollevalg
        changeScene(View.ROLE_SELECTION, getStageFromEvent(event));
    }
}
