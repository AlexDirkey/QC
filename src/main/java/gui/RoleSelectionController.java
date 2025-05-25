package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class RoleSelectionController extends BaseController {

    private void openLoginView(ActionEvent event, String role) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        changeScene("/gui/LoginView.fxml", stage);
        // Sætter rollen efter sceneskift – brug evt. globalt state hvis nødvendigt
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


