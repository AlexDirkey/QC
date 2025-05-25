package gui;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LoginController extends BaseController {

    private String selectedRole;

    public void setSelectedRole(String role) {
        this.selectedRole = role;
    }

    @FXML
    private void onLoginButtonClick(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        // Fremtidig validering:
        // String username = usernameField.getText();
        // String password = passwordField.getText();
        // User user = userRepository.findByUsername(username);
        // if (user != null && user.getPassword().equals(password)) {...}

        switch (selectedRole) {
            case "Admin" -> changeScene("/gui/AdminView.fxml", stage);
            case "Operator" -> changeScene("/gui/OperatorView.fxml", stage);
            case "QA" -> changeScene("/gui/QAView.fxml", stage);
            default -> showWarning("Ukendt rolle", "Rollen er ikke understøttet.");
        }
    }

    @FXML
    private void onBackButtonClick(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        changeScene("/gui/RoleSelectionView.fxml", stage);
    }
}
