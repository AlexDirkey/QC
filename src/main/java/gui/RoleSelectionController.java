package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.NotificationHelper;

// Controller for rollevalgs-skærmen.
// Her vælger brugeren hvilken rolle de logger ind som (QA, Operator eller Admin).
// Når en rolle vælges, åbnes login-vinduet og rollen sendes med videre.

public class RoleSelectionController extends BaseController {

    // Hjælpeklasse til at vise notifikationer/advarsler, hvis noget går galt

    private final NotificationHelper notifier = new NotificationHelper(this);

    // Kaldt fra FXML når brugeren trykker på "QA"-knappen

    @FXML
    private void onQARoleSelected(ActionEvent event) {
        openLoginView(event, "QA"); // Send brugeren videre til login med rollen "QA"
    }

    // Kaldt fra FXML når brugeren trykker på "Operator"-knappen

    @FXML
    private void onOperatorRoleSelected(ActionEvent event) {
        openLoginView(event, "Operator"); // Send brugeren videre til login med rollen "Operator"
    }

    // Kaldt fra FXML når brugeren trykker på "Admin"-knappen

    @FXML
    private void onAdminRoleSelected(ActionEvent event) {
        openLoginView(event, "Admin"); // Send brugeren videre til login med rollen "Admin"
    }

    // Hjælpemetode der skifter til login-vinduet og sætter den valgte rolle på login-controlleren.
    // På den måde ved LoginController, hvilken rolle der er valgt, før brugeren indtaster brugeroplysninger.

    private void openLoginView(ActionEvent event, String role) {
        Stage stage = getStageFromEvent(event); // Finder det Stage-vindue, der udløste eventet
        try {
            // Hent FXML-stien for login fra enum View
            String fxml = View.LOGIN.getFxmlPath();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            // Når FXML er loadet, kan vi få fat i LoginController
            // Her "injicerer" vi den rolle, brugeren har valgt, så login kan tjekke rolle vs. bruger

            LoginController loginCtrl = loader.getController();
            loginCtrl.setSelectedRole(role);

            // Opret en ny Scene med root-layoutet fra FXML

            Scene scene = new Scene(root);

            // Tilføj CSS-styling fra enum View (fælles Style.css)

            scene.getStylesheets().add(
                    getClass().getResource(View.LOGIN.getCssPath()).toExternalForm()
            );

            // Skift scene i stage og vis vinduet/scenen

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            // Hvis noget går galt (fx FXML ikke findes), vis advarsels-popup til brugeren
            notifier.showWarning("Navigationsfejl",
                    "Kunne ikke åbne login: " + e.getMessage());
        }
    }
}

