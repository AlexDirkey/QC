package gui;

import gui.util.InputManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import gui.util.InputManager;
import gui.NotificationHelper;
import bll.AuthService;
import gui.View;

/**
 * Controller til at oprette nye brugere manuelt gennem GUI.
 * Benytter InputValidator til validering og NotificationHelper til beskeder.
 */
public class CreateUserController extends BaseController {

    @FXML private TextField usernameField;      // Inputfelt til brugernavn
    @FXML private PasswordField passwordField;  // Inputfelt til password
    @FXML private TextField roleField;          // Inputfelt til rolle

    // Brug AuthService til brugeroprettelse
    private final AuthService authService = new AuthService();
    private final NotificationHelper notifier = new NotificationHelper(this);

    /**
     * Kaldes når 'Create'-knappen trykkes.
     * Validerer med InputValidator og opretter bruger via AuthService.
     */
    @FXML
    private void onCreateButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();

        // Tjek for tomme felter
        if (!InputManager.isFilled(username, password, role)) {
            notifier.warnMissingInput();
            return;
        }

        // Opret bruger via AuthService
        try {
            authService.addUser(username, password, role);
            notifier.infoUserCreated(username, role);
            clearForm();
        } catch (Exception e) {
            showWarning("Oprettelse mislykkedes", "Kunne ikke oprette bruger: " + e.getMessage());
        }
    }

    /**
     * Skifter tilbage til AdminView, når 'Back'-knappen trykkes.
     */
    @FXML
    private void onBackButtonClick(ActionEvent event) {
        changeScene(View.ADMIN, getStageFromEvent(event));
    }

    /**
     * Rydder alle inputfelter.
     */
    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        roleField.clear();
    }
}

