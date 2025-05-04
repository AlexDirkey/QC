
package gui.controller;

import bll.AuthService;
import dal.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = authService.authenticate(username, password);

        if (user != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText(null);
            alert.setContentText("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
            alert.showAndWait();

            // RUTE BASERET PÅ ROLLE
            switch (user.getRole().toLowerCase()) {
                case "operator":
                    loadView("/gui/view/OperatorView.fxml", event);
                    break;
                case "qa":
                    loadView("/gui/view/QAView.fxml", event);
                    break;
                case "admin":
                    loadView("/gui/view/AdminView.fxml", event);
                    break;
                default:
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setContentText("Unknown role: " + user.getRole());
                    error.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }

    private void loadView(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to load view: " + fxmlPath);
            alert.showAndWait();
        }
    }
}