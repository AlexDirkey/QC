package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RoleSelectionController {

    private void openLoginView(ActionEvent event, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/LoginView.fxml"));
            Scene scene = new Scene(loader.load());
            LoginController loginController = loader.getController();
            loginController.setSelectedRole(role);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onQARoleSelected(ActionEvent event) {
        openLoginView(event, "QA");  // <-- Fix: sender event med
    }

    @FXML
    private void onOperatorRoleSelected(ActionEvent event) {
        openLoginView(event, "Operator");  // <-- Fix: sender event med
    }

    @FXML
    private void onAdminRoleSelected(ActionEvent event) {
        openLoginView(event, "Admin");  // <-- Fix: sender event med
    }
}

