package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

public class QAController {

    @FXML
    private ListView<String> photoListView;

    @FXML
    private void onApproveButtonClick(ActionEvent event) {
        String selected = photoListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Godkendelse");
            alert.setHeaderText(null);
            alert.setContentText("Billede godkendt: " + selected);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ingen valgt");
            alert.setHeaderText(null);
            alert.setContentText("Vælg et billede først.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onRejectButtonClick(ActionEvent event) {
        String selected = photoListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Afvist");
            alert.setHeaderText(null);
            alert.setContentText("Billede afvist: " + selected);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ingen valgt");
            alert.setHeaderText(null);
            alert.setContentText("Vælg et billede først.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onGenerateReportClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rapport genereret");
        alert.setHeaderText(null);
        alert.setContentText("Rapporten er genereret og klar til download.");
        alert.showAndWait();
    }
}