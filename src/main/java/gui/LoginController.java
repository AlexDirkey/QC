package gui;

import bll.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import gui.util.SessionService;
import gui.NotificationHelper;
import model.User;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

// Controller for login-skærmen.
// Håndterer input af brugernavn/kodeord, validerer login via AuthService,
// og navigerer brugeren videre til det rigtige view baseret på rolle.

public class LoginController extends BaseController {
    private String selectedRole; // Rollen valgt i RoleSelectionController (QA, Operator, Admin)
    private final NotificationHelper notifier = new NotificationHelper(this); // Hjælper til dialogbeskeder
    private final AuthService authService = new AuthService(); // Service til autentificering

    // FXML-felter (forbundet til LoginView.fxml)

    @FXML private TextField usernameField;    // Inputfelt til brugernavn
    @FXML private PasswordField passwordField; // Inputfelt til kodeord (skjult tekst)

    // Setter: modtager rollen fra RoleSelectionController (så vi kan matche den ved login)

    public void setSelectedRole(String role) {
        this.selectedRole = role;
    }

    // Event handler: Kaldt når brugeren trykker på "Login"-knappen

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Første trin: valider at felterne ikke er tomme

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            notifier.showWarning("Manglende oplysninger", "Udfyld både brugernavn og kodeord.");
            return;
        }

        try {
            // 1) Autentificér via AuthService (bruger DB + BCrypt)

            User user = authService.authenticate(username, password);

            // 2) Hvis rolle blev valgt før login → tjek at det matcher brugerens rolle i DB

            if (selectedRole != null && !user.getRole().equalsIgnoreCase(selectedRole)) {
                notifier.showWarning("Rolle mismatch",
                        "Du valgte '" + selectedRole + "', men brugeren er '" + user.getRole() + "'.");
                return;
            }

            // 3) Login lykkedes: gem den aktuelle bruger i SessionService (Singleton)
            SessionService.getInstance().login(user);

            // 4) Navigér til det rigtige view ud fra brugerens rolle
            // Enum View oversætter rollen til korrekt FXML-fil

            View target = View.valueOf(user.getRole().toUpperCase());
            changeScene(target, getStageFromEvent(event));

        } catch (AuthService.AuthException ex) {
            // Hvis AuthService smider exception (fx forkert password)

            notifier.showWarning("Login fejlede", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            // Hvis der kommer en rolle vi ikke understøtter i enum View
            notifier.showWarning("Ukendt rolle", "Rollen '" + selectedRole + "' er ikke understøttet.");
        }
    }

    // Event handler: Kaldt hvis brugeren trykker på "Tilbage"-knappen

    @FXML
    private void onBackButtonClick(ActionEvent event) {
        // Skift tilbage til rollevalgsskærmen
        changeScene(View.ROLE_SELECTION, getStageFromEvent(event));
    }
}
