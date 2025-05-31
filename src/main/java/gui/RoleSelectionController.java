package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.NotificationHelper;

//Controller til rollevalg
public class RoleSelectionController extends BaseController {

    private final NotificationHelper notifier = new NotificationHelper(this);

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

    //FXML
    //Sender brugeren videre til scene, baseret på rolle
    private void openLoginView(ActionEvent event, String role) {
        Stage stage = getStageFromEvent(event);
        try {
            // Hent FXML-sti fra enum
            String fxml = View.LOGIN.getFxmlPath();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            // Sæt rolle på netop denne controller-instans
            LoginController loginCtrl = loader.getController();
            loginCtrl.setSelectedRole(role);

            // Opret scene + CSS
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource(View.LOGIN.getCssPath()).toExternalForm()
            );

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            notifier.showWarning("Navigation fejl",
                    "Kunne ikke åbne login: " + e.getMessage());
        }
    }
}
