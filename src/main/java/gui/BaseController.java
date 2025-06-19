package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
//Superclass, som QA- Operator- og AdminControllerne arver fra (inheritance)
public abstract class BaseController {

    //Skifter scene
    protected void changeScene(View view, Stage stage) {
        try {
            //Indlæser ift den scene, der er i Viewenum
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view.getFxmlPath()));
            Parent root = loader.load();

            // Log controller
            Object controller = loader.getController();
            if (controller != null)
                System.out.println("✅ Controller loaded: " + controller.getClass().getSimpleName());

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(view.getCssPath()).toExternalForm());

            //Viser scenen
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            //Vis en fejl, hvis Loader ikke loades
            showAlert(AlertType.ERROR, "Scene-skift fejl", "Kunne ikke skifte til " + view.name());
            e.printStackTrace();
        }
    }
    //Navigerer tilbage til RoleSelection

    protected void navigateBackToRoleSelection(ActionEvent event) {
        changeScene(View.ROLE_SELECTION, getStageFromEvent(event));
    }

    //Viser advarsel

    protected void showWarning(String title, String message) {
        showAlert(AlertType.WARNING, title, message);
    }

    //Viser dialogboks

    protected void showInfo(String title, String message) {
        showAlert(AlertType.INFORMATION, title, message);
    }

    //Viser alert
    protected void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Ved et tryk på knappen, skiftes der scene
    protected Stage getStageFromEvent(ActionEvent event) {
        return (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    }
}

