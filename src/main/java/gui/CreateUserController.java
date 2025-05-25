package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import dal.UserRepository;
import model.User;

public class CreateUserController extends BaseController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField roleField;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    private void onCreateButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            showWarning("Udfyld alle felter", "Alle felter skal udfyldes før brugeren kan oprettes.");
            return;
        }

        User newUser = new User(username, password, role);
        userRepository.save(newUser);
        showInfo("Bruger oprettet", "Brugeren er blevet oprettet.");

        // Ryd felterne efter oprettelse
        usernameField.clear();
        passwordField.clear();
        roleField.clear();
    }

    @FXML
    private void onBackButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        changeScene("/gui/AdminView.fxml", stage);
    }
}