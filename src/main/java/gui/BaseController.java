package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * BaseController giver fælles GUI-funktionalitet:
 * - scene-skift vha. enum View (FXML- og CSS-stier)
 * - visning af advarsels- og infodialoger
 *
 * For at CSS anvendes korrekt, lægges Style.css under:
 * src/main/resources/gui/Style.css
 */
public abstract class BaseController {

    /**
     * Skifter scene ved at anvende enum View, der indeholder stier til FXML og CSS.
     *
     * @param view  enum-værdi med FXML- og CSS-stier
     * @param stage den Stage, hvor scenen skal skiftes
     */
    protected void changeScene(View view, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view.getFxmlPath()));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Scene-skift fejl", "Kunne ikke skifte til " + view.name());
            e.printStackTrace();
        }
    }

    /**
     * Navigerer tilbage til RoleSelectionView.fxml med korrekt stylesheet.
     *
     * @param event ActionEvent fra fx "Tilbage"-knap
     */
    protected void navigateBackToRoleSelection(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/RoleSelectionView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());

            Stage stage = getStageFromEvent(event);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Fejl", "Kunne ikke skifte til forsiden.");
            e.printStackTrace();
        }
    }

    /**
     * Viser en advarselsdialog (warning).
     */
    protected void showWarning(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Viser en infodialog (information).
     */
    protected void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Viser generisk Alert af vilkårlig type.
     */
    protected void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Udtrækker Stage fra et ActionEvent, så vi genbruger kode til scene-skift.
     */
    protected Stage getStageFromEvent(ActionEvent event) {
        return (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    }
}

