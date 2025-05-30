package gui;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.User;
import dal.UserRepository;
import gui.View;

// Controller, til at tildele roller til eksisterende brugere. Viser brugere, og opdaterer

public class AssignRoleController extends BaseController {

    @FXML
    private ChoiceBox<String> userChoiceBox;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private Button assignButton;

    @FXML
    private Label statusLabel;


    //Instansierer et UserRepository
    private final UserRepository userRepository = new UserRepository();

    @FXML
    public void initialize() {
        try {
            userChoiceBox.getItems().addAll(userRepository.getAllUsernames());
        } catch (Exception e) {
            statusLabel.setText("Kunne ikke hente brugere.");
        }

        roleChoiceBox.getItems().addAll("Admin", "Operator", "QA");
    }


    //FJERN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void handleBypassLogin(String role, Stage stage) {
        switch (role) {
            case "Admin"    -> changeScene(View.ADMIN, stage);
            case "Operator" -> changeScene(View.OPERATOR, stage);
            case "QA"       -> changeScene(View.QA, stage);
            default         -> showWarning("Ukendt rolle", "Rollen er ikke understøttet.");
        }
    }

    //Tildeler roller til valgt bruger - viser en alert, hvis brugeren ikke kan findes
    @FXML
    private void handleAssignRole() {
        String selectedUser = userChoiceBox.getValue();
        String selectedRole = roleChoiceBox.getValue();

        if (selectedUser == null || selectedRole == null) {
            statusLabel.setText("Vælg både bruger og rolle.");
            return;
        }

        User user = userRepository.findByUsername(selectedUser);
        if (user != null) {
            user.setRole(selectedRole);
            userRepository.updateUser(user);
            showAlert("Rolle tildelt", "Brugeren " + selectedUser + " er nu " + selectedRole + ".");
            statusLabel.setText("Rolle opdateret.");
        } else {
            statusLabel.setText("Bruger ikke fundet.");
        }
    }

    //Viser en alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
