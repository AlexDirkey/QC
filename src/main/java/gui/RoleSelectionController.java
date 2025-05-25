package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RoleSelectionController extends BaseController {

    private void openLoginView(ActionEvent event, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginView.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            loginController.setSelectedRole(role);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onQARoleSelected(ActionEvent event) {
        openLoginView(event, "QA");
    }

    @FXML
    private void onOperatorRoleSelected(ActionEvent event) {
        openLoginView(event, "Operator");
    }

    @FXML
    private void onAdminRoleSelected(ActionEvent event) {
        openLoginView(event, "Admin");
    }
}

