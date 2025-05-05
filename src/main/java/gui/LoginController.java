package gui;

import bll.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * LoginController håndterer login GUI og logik.
 */
public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> devRoleComboBox;

    private AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    @FXML
    private void initialize() {
        devRoleComboBox.getItems().addAll("admin", "QA", "Operator", "user");
    }

    @FXML
    private void onDeveloperButtonClick(ActionEvent event) {
        String selectedRole = devRoleComboBox.getValue();
        if (selectedRole == null || selectedRole.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advarsel");
            alert.setHeaderText(null);
            alert.setContentText("Vælg en rolle fra dropdown menuen.");
            alert.showAndWait();
            return;
        }

        String fxmlFile = "/gui/" + selectedRole + "View.fxml";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fejl");
            alert.setHeaderText(null);
            alert.setContentText("Kunne ikke åbne: " + fxmlFile);
            alert.showAndWait();
        }
    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String role = authService.login(username, password);

        if (role != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Succes");
            alert.setHeaderText(null);
            alert.setContentText("Velkommen, " + role + "!");
            alert.showAndWait();

            String fxmlFile = "/gui/" + role + "View.fxml";
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Fejl");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Kunne ikke åbne: " + fxmlFile);
                errorAlert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Fejl");
            alert.setHeaderText(null);
            alert.setContentText("Forkert brugernavn eller kodeord.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onHelloButtonClick(ActionEvent event) {
        System.out.println("Hello button clicked (placeholder)");
    }
}

