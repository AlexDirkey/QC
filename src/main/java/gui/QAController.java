package gui;

import bll.DocumentationService;
import bll.PhotoService;
import bll.ReportService;
import gui.NotificationHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Photo;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class QAController extends BaseController {

    private final PhotoService photoService = new PhotoService();
    private final ReportService reportService = new ReportService(photoService);
    private final DocumentationService docService = new DocumentationService(reportService, photoService);
    private final NotificationHelper notifier = new NotificationHelper(this);

    @FXML private ListView<String> photoListView;
    @FXML private ImageView        photoPreview;
    @FXML private Button           approveButton;
    @FXML private Button           rejectButton;
    @FXML private Label            statusLabel;

    private List<Photo> pendingPhotos;

    @FXML
    private void initialize() {
        loadPendingPhotos();
        photoListView.getSelectionModel().selectedIndexProperty()
                .addListener((obs, oldIdx, newIdx) -> onPhotoSelected());
    }

    // HENT PENDING (afventer QA)
    private void loadPendingPhotos() {
        pendingPhotos = photoService.getPhotosWithStatus("PENDING");
        ObservableList<String> display = FXCollections.observableArrayList();
        for (Photo p : pendingPhotos) {
            String comment = (p.getComment() != null && !p.getComment().isBlank())
                    ? p.getComment() : "Ingen kommentarer";
            display.add(String.format("Ordre: %s | Bruger: %s | %s | Kommentar: %s",
                    p.getOrderNumber(), p.getUploadedBy(), p.getUploadedAt(), comment));
        }
        photoListView.setItems(display);
        photoPreview.setImage(null);
        statusLabel.setText("");
    }

    private void onPhotoSelected() {
        int idx = photoListView.getSelectionModel().getSelectedIndex();
        if (idx < 0 || idx >= pendingPhotos.size()) {
            photoPreview.setImage(null);
            return;
        }
        Photo photo = pendingPhotos.get(idx);
        File file = new File(photo.getFilePath());
        if (file.exists()) {
            photoPreview.setImage(new Image(file.toURI().toString()));
            statusLabel.setText("");
        } else {
            photoPreview.setImage(null);
            statusLabel.setText("Fil ikke fundet: " + file.getAbsolutePath());
        }
    }

    @FXML
    private void onApproveButtonClick(ActionEvent event) {
        updateSelectedPhotoStatus(true);
    }

    @FXML
    private void onRejectButtonClick(ActionEvent event) {
        updateSelectedPhotoStatus(false);
    }

    private void updateSelectedPhotoStatus(boolean approved) {
        int idx = photoListView.getSelectionModel().getSelectedIndex();
        if (idx < 0 || idx >= pendingPhotos.size()) {
            notifier.showWarning("Intet valgt", "Vælg et foto først.");
            return;
        }
        Photo photo = pendingPhotos.get(idx);
        try {
            photoService.updatePhotoStatus(photo.getId(), approved);
            notifier.showInfo("Status opdateret", approved ? "Foto godkendt." : "Foto afvist.");
            loadPendingPhotos();
        } catch (Exception e) {
            notifier.showWarning("Opdateringsfejl", "Kunne ikke opdatere foto: " + e.getMessage());
        }
    }

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

        int idx = photoListView.getSelectionModel().getSelectedIndex();
        if (idx < 0 || idx >= pendingPhotos.size()) {
            notifier.showWarning("Intet valgt", "Vælg et foto/ordre først.");
            return;
        }
        String orderNumber = pendingPhotos.get(idx).getOrderNumber();

        approveButton.setDisable(true);
        rejectButton.setDisable(true);
        statusLabel.setText("Genererer rapport...");

        Task<File> generateTask = new Task<>() {
            @Override
            protected File call() throws Exception {
                return docService.generateReportForOrder(orderNumber);
            }
        };

        generateTask.setOnSucceeded(ev -> {
            File pdf = generateTask.getValue();
            statusLabel.setText("Rapport klar.");

            Alert preview = new Alert(Alert.AlertType.CONFIRMATION);
            preview.setTitle("Rapport genereret");
            preview.setHeaderText("Rapport for ordre " + orderNumber + " er genereret.");
            ButtonType openBtn = new ButtonType("Åbn");
            ButtonType sendBtn = new ButtonType("Send");
            ButtonType cancel  = ButtonType.CANCEL;
            preview.getButtonTypes().setAll(openBtn, sendBtn, cancel);

            Optional<ButtonType> res = preview.showAndWait();

            if (res.isPresent() && res.get() == openBtn) {
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(pdf);
                    } else {
                        notifier.showWarning("Kan ikke åbne", "Desktop-integration er ikke understøttet.");
                    }
                } catch (IOException e) {
                    notifier.showWarning("Åbn fejl", e.getMessage());
                } finally {
                    approveButton.setDisable(false);
                    rejectButton.setDisable(false);
                }

            } else if (res.isPresent() && res.get() == sendBtn) {
                statusLabel.setText("Sender rapport...");
                Task<Void> sendTask = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        docService.sendReport(email, orderNumber, pdf);
                        return null;
                    }
                };
                sendTask.setOnSucceeded(e2 -> {
                    statusLabel.setText("Rapport sendt.");
                    approveButton.setDisable(false);
                    rejectButton.setDisable(false);
                });
                sendTask.setOnFailed(e2 -> {
                    statusLabel.setText("");
                    approveButton.setDisable(false);
                    rejectButton.setDisable(false);
                    notifier.showWarning("Fejl", "Kunne ikke sende rapport: " + sendTask.getException().getMessage());
                });
                new Thread(sendTask).start();

            } else {
                statusLabel.setText("");
                approveButton.setDisable(false);
                rejectButton.setDisable(false);
            }
        });

        generateTask.setOnFailed(ev -> {
            statusLabel.setText("");
            approveButton.setDisable(false);
            rejectButton.setDisable(false);
            notifier.showWarning("Fejl", "Kunne ikke generere rapport: " + generateTask.getException().getMessage());
        });

        new Thread(generateTask).start();
    }

    @FXML
    private void onViewHistoryClick(ActionEvent event) {
        int idx = photoListView.getSelectionModel().getSelectedIndex();
        if (idx < 0 || idx >= pendingPhotos.size()) {
            notifier.showWarning("Intet valgt", "Vælg et foto/ordre først.");
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

    @FXML
    private void navigateBack(ActionEvent event) {
        changeScene(View.ROLE_SELECTION, getStageFromEvent(event));
    }
}


