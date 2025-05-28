package gui;

import bll.PhotoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Photo;

import java.io.File;
import java.util.List;

public class QAController {

    @FXML
    private ListView<String> photoListView;

    @FXML
    private ImageView photoPreview;

    @FXML
    private Button approveButton;

    @FXML
    private Button rejectButton;

    @FXML
    private Label statusLabel;

    private final PhotoService photoService = new PhotoService();
    private ObservableList<Photo> pendingPhotos;

    @FXML
    public void initialize() {
        loadPendingPhotos();
        photoListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> onPhotoSelected());
    }

    private void loadPendingPhotos() {
        List<Photo> photos = photoService.getUnapprovedPhotos();
        pendingPhotos = FXCollections.observableArrayList(photos);

        ObservableList<String> displayList = FXCollections.observableArrayList();
        for (Photo p : pendingPhotos) {
            displayList.add("Ordre: " + p.getOrderNumber() + " | Bruger: " + p.getUploadedBy() + " | " + p.getUploadedAt());
        }
        photoListView.setItems(displayList);
    }

    private void onPhotoSelected() {
        int index = photoListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < pendingPhotos.size()) {
            Photo selectedPhoto = pendingPhotos.get(index);
            File file = new File(selectedPhoto.getFilePath());
            if (file.exists()) {
                photoPreview.setImage(new Image(file.toURI().toString()));
                statusLabel.setText("");
            } else {
                statusLabel.setText("Billedet kunne ikke indlæses.");
                photoPreview.setImage(null);
            }
        }
    }

    @FXML
    private void onApproveButtonClick() {
        updatePhotoStatus(true);
    }

    @FXML
    private void onRejectButtonClick() {
        updatePhotoStatus(false);
    }

    private void updatePhotoStatus(boolean approved) {
        int index = photoListView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            Photo selectedPhoto = pendingPhotos.get(index);
            try {
                photoService.updatePhotoStatus(selectedPhoto.getId(), approved);
                statusLabel.setText(approved ? "Billedet blev godkendt." : "Billedet blev afvist.");
                loadPendingPhotos();
                photoPreview.setImage(null);
            } catch (Exception e) {
                statusLabel.setText("Fejl ved opdatering.");
                System.err.println("Opdateringsfejl: " + e.getMessage());
            }
        }
    }
}
