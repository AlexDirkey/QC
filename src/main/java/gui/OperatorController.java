package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;

public class OperatorController extends BaseController {

    @FXML
    private ListView<String> pendingOrdersList, inReviewOrdersList, completedOrdersList;

    @FXML
    private TextField orderNumberField;

    @FXML
    private Label statusLabel;

    @FXML
    private void onAddCommentButtonClick(ActionEvent event) {
        showInfo("Tilføj kommentar", "Funktionen til at tilføje kommentarer er endnu ikke implementeret.");
    }

    @FXML
    private void onUploadButtonClick() {
        String orderNumber = orderNumberField.getText().trim();
        if (orderNumber.isEmpty()) {
            statusLabel.setText("Indtast et ordrenummer.");
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Vælg billede til upload");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Billeder", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                statusLabel.setText("Billede valgt: " + selectedFile.getName());
            } else {
                statusLabel.setText("Ingen fil valgt.");
            }
        }
    }
}



