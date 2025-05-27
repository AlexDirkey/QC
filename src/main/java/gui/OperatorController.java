package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import dal.DataBaseConnector;

public class OperatorController extends BaseController {

    @FXML
    private ListView<String> pendingOrdersList;

    @FXML
    private ListView<String> inReviewOrdersList;

    @FXML
    private ListView<String> completedOrdersList;

    @FXML
    private TextField orderNumberField;

    @FXML
    private Label statusLabel;

    private final List<File> uploadedPhotos = new ArrayList<>();
    private final DataBaseConnector databaseConnector = new DataBaseConnector();

    private final String currentUser = "demo_user"; // TODO: Replace with actual logged-in user

    @FXML
    private void onUploadButtonClick() {
        if (uploadedPhotos.size() >= 5) {
            statusLabel.setText("Maksimalt 5 billeder tilladt.");
            return;
        }

        String orderNumber = orderNumberField.getText().trim();
        if (orderNumber.isEmpty()) {
            statusLabel.setText("Indtast et ordrenummer.");
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Vælg billede til upload");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Billeder", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                uploadedPhotos.add(selectedFile);
                savePhotoToDatabase(selectedFile, orderNumber);
                statusLabel.setText("Billede valgt (" + uploadedPhotos.size() + "/5): " + selectedFile.getName());
            } else {
                statusLabel.setText("Ingen fil valgt.");
            }
        }
    }

    private void savePhotoToDatabase(File imageFile, String orderNumber) {
        String sql = "INSERT INTO photos (order_number, image_data, uploaded_by) VALUES (?, ?, ?)";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(imageFile)) {

            stmt.setString(1, orderNumber);
            stmt.setBinaryStream(2, fis, (int) imageFile.length());
            stmt.setString(3, currentUser);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            showWarning("Fejl ved upload", "Kunne ikke gemme billedet i databasen.");
        }
    }

    @FXML
    private void onAddCommentButtonClick(ActionEvent event) {
        showInfo("Tilføj kommentar", "Funktionen til at tilføje kommentarer er endnu ikke implementeret.");
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        changeScene("/gui/RoleSelectionView.fxml", stage);
    }
}





