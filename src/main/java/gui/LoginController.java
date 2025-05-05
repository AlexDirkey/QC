
package gui;

import bll.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * LoginController håndterer login GUI og logik.
 */
public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    @FXML
    private void onHelloButtonClick(ActionEvent event) {
        // Midlertidig løsning for at matche FXML event handler
        System.out.println("Hello button clicked (placeholder)");
    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String role = authService.login(username, password);

        if (role != null) {
            // Login succes – vis besked eller skift scene her
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Succes");
            alert.setHeaderText(null);
            alert.setContentText("Velkommen, " + role + "!");
            alert.showAndWait();

            // TODO: Load næste scene baseret på rolle

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Fejl");
            alert.setHeaderText(null);
            alert.setContentText("Forkert brugernavn eller kodeord.");
            alert.showAndWait();
        }
    }
}