package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class QAController extends BaseController {

    @FXML
    private ListView<String> reviewOrdersList;

    @FXML
    private void onApproveOrderClick(ActionEvent event) {
        String selected = reviewOrdersList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showInfo("Godkendelse", "Ordre godkendt: " + selected);
        } else {
            showWarning("Ingen valgt", "Vælg en ordre først.");
        }
    }

    @FXML
    private void onRejectOrderClick(ActionEvent event) {
        String selected = reviewOrdersList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showInfo("Afvist", "Ordre afvist: " + selected);
        } else {
            showWarning("Ingen valgt", "Vælg en ordre først.");
        }
    }
}
