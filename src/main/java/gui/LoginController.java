package gui;

import bll.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController extends BaseController {

    @FXML
    private Label roleLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final AuthService authService = new AuthService();
    private String selectedRole = "";

    public void setSelectedRole(String role) {
        this.selectedRole = role;
        if (roleLabel != null) {
            roleLabel.setText("BelSign " + role);
        }
    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Udfyld både brugernavn og kodeord.");
            return;
        }

        String authenticatedRole = authService.login(username, password);

        if (authenticatedRole != null && authenticatedRole.equalsIgnoreCase(selectedRole)) {
            showAlert(Alert.AlertType.INFORMATION, "Velkommen, " + authenticatedRole + "!");
            loadRoleView(authenticatedRole, event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Forkert brugernavn, kodeord eller rolle.");
        }
    }

    private void loadRoleView(String role, ActionEvent event) {
        String fxmlFile = "/gui/" + role.toLowerCase() + "view.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
