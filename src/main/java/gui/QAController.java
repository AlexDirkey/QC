package gui;

import bll.PhotoService;
import bll.ReportService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Photo;


import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Controller til QA-flow, hvor fotos kan godkendes eller afvises
 * og der kan genereres PDF-rapporter med godkendte fotos sendt via e-mail.
 */
public class QAController extends BaseController {

    @FXML private ListView<String> photoListView;
    @FXML private ImageView photoPreview;
    @FXML private Button approveButton;
    @FXML private Button rejectButton;
    @FXML private Button generateReportButton;
    @FXML private Button viewHistoryButton;
    @FXML private Label statusLabel;

    // Services injected for testbarhed og separation of concerns
    private final PhotoService photoService = new PhotoService();
    private final ReportService reportService = new ReportService(photoService);
    private final gui.NotificationHelper notifier = new gui.NotificationHelper(this);

    private List<Photo> pendingPhotos;

    /**
     * Initialiserer controller: loader u-godkendte fotos og sætter listener på ListView.
     */
    @FXML
    public void initialize() {
        loadPendingPhotos();
        photoListView.getSelectionModel().selectedIndexProperty()
                .addListener((obs, oldIdx, newIdx) -> onPhotoSelected());
    }

    /**
     * Henter alle u-godkendte fotos fra service og viser dem i listen.
     */
    private void loadPendingPhotos() {
        pendingPhotos = photoService.getUnapprovedPhotos();
        ObservableList<String> display = FXCollections.observableArrayList();
        for (Photo p : pendingPhotos) {
            display.add(String.format("Ordre: %s | Bruger: %s | %s",
                    p.getOrderNumber(), p.getUploadedBy(), p.getUploadedAt()));
        }
        photoListView.setItems(display);
        photoPreview.setImage(null);
        statusLabel.setText("");
    }

    /**
     * Vis preview for det valgte foto eller vis fejl.
     */
    private void onPhotoSelected() {
        int idx = photoListView.getSelectionModel().getSelectedIndex();
        if (idx < 0 || idx >= pendingPhotos.size()) return;

        Photo photo = pendingPhotos.get(idx);
        File file = new File(photo.getFilePath());
        if (file.exists()) {
            photoPreview.setImage(new Image(file.toURI().toString()));
            statusLabel.setText("");
        } else {
            photoPreview.setImage(null);
            notifier.showWarning("Billede ikke fundet", "Kunne ikke indlæse det valgte billede.");
        }
    }

    /**
     * Opdaterer status for valgt foto og refresher listen.
     */
    @FXML
    private void onApproveButtonClick() {
        updatePhotoStatus(true);
    }

    @FXML
    private void onRejectButtonClick() {
        updatePhotoStatus(false);
    }

    @FXML
    protected void navigateBackToRoleSelection(ActionEvent event) {
        // Gå tilbage til rollevalg
        changeScene(View.ROLE_SELECTION, getStageFromEvent(event));
    }

    private void updatePhotoStatus(boolean approved) {
        int idx = photoListView.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            notifier.showWarning("Ingen valgt", "Vælg et foto først.");
            return;
        }
        Photo photo = pendingPhotos.get(idx);
        try {
            photoService.updatePhotoStatus(photo.getId(), approved);
            notifier.showInfo("Status opdateret",
                    approved ? "Foto godkendt." : "Foto afvist.");
            loadPendingPhotos();
        } catch (Exception e) {
            notifier.showWarning("Opdateringsfejl",
                    "Kunne ikke opdatere foto: " + e.getMessage());
        }
    }

    /**
     * Genererer og sender PDF-rapport til angivet e-mail.
     */
    @FXML
    private void onGenerateReportClick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Send rapport");
        dialog.setHeaderText("Indtast kundens e-mailadresse");
        dialog.setContentText("E-mail:");

        String email = dialog.showAndWait().orElse(null);
        if (email == null || email.isBlank()) {
            notifier.showWarning("Ingen e-mail", "E-mailadresse er påkrævet.");
            return;
        }

        try {
            reportService.generateAndSendApprovedReport(email);
            notifier.showInfo("Rapport sendt", "E-mail sendt til: " + email);
        } catch (Exception e) {
            notifier.showWarning("Rapportfejl",
                    "Kunne ikke generere eller sende rapport: " + e.getMessage());
        }
    }

    /**
     * Åbner historik-vinduet for den valgte ordre.
     */
    @FXML
    private void onViewHistoryClick() {
        int idx = photoListView.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            notifier.showWarning("Ingen valgt", "Vælg en ordre først.");
            return;
        }
        String orderNumber = pendingPhotos.get(idx).getOrderNumber();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.HISTORIK.getFxmlPath()));
            Parent root = loader.load();
            HistorikController hist = loader.getController();
            hist.loadHistorik(orderNumber);
            Stage stage = new Stage();
            stage.setTitle("Ordrehistorik");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            notifier.showWarning("Fejl", "Kunne ikke åbne historik: " + e.getMessage());
        }
    }
}