package gui;

import bll.OperatorService;
import gui.NotificationHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Photo;
import util.OpenCVHelper;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class OperatorController extends BaseController {

    @FXML private ListView<String> pendingOrdersList;
    @FXML private ListView<String> inReviewOrdersList;
    @FXML private ListView<String> completedOrdersList;
    @FXML private TextField orderNumberField;
    @FXML private TextField commentField;
    @FXML private Button captureButton;
    @FXML private Button uploadButton;
    @FXML private Button saveOrderButton;
    @FXML private Label statusLabel;

    private final OperatorService operatorService = new OperatorService();
    private final NotificationHelper notifier = new NotificationHelper(this);
    private final List<File> uploadedPhotos = new ArrayList<>();
    private final String currentUser = "demo_user";

    @FXML
    protected void initialize() {
        loadOrders();
        saveOrderButton.setDisable(true);
        orderNumberField.textProperty().addListener((obs, oldV, newV) ->
                saveOrderButton.setDisable(orderNumberField.getText().trim().isEmpty() || uploadedPhotos.isEmpty())
        );
    }

    private void loadOrders() {
        List<Photo> pendingPhotos = operatorService.getPendingPhotos();
        Map<String, Long> groupedPending = pendingPhotos.stream()
                .collect(Collectors.groupingBy(Photo::getOrderNumber, Collectors.counting()));
        ObservableList<String> pendingItems = FXCollections.observableArrayList(
                groupedPending.entrySet().stream()
                        .map(e -> "Ordre: " + e.getKey() + " | Billeder: " + e.getValue())
                        .collect(Collectors.toList()));
        pendingOrdersList.setItems(pendingItems);

        List<Photo> completedPhotos = operatorService.getCompletedPhotos();
        ObservableList<String> inReviewItems = FXCollections.observableArrayList(
                completedPhotos.stream()
                        .filter(p -> "IN_REVIEW".equalsIgnoreCase(p.getStatus()))
                        .map(p -> "ID: " + p.getId() + " | Ordre: " + p.getOrderNumber())
                        .collect(Collectors.toList()));
        inReviewOrdersList.setItems(inReviewItems);

        ObservableList<String> doneItems = FXCollections.observableArrayList(
                completedPhotos.stream()
                        .filter(p -> "APPROVED".equalsIgnoreCase(p.getStatus()) || "REJECTED".equalsIgnoreCase(p.getStatus()))
                        .map(p -> "ID: " + p.getId() + " | Ordre: " + p.getOrderNumber() + " | " + p.getStatus())
                        .collect(Collectors.toList()));
        completedOrdersList.setItems(doneItems);
    }

    @FXML
    protected void onUploadButtonClick(ActionEvent event) {
        if (uploadedPhotos.size() >= 5) {
            notifier.showWarning("Maks billeder nået", "Maksimalt 5 billeder tilladt.");
            return;
        }
        if (orderNumberField.getText().trim().isEmpty()) {
            notifier.showWarning("Manglende ordrenummer", "Indtast ordrenummer før upload.");
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vælg billede til upload");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Billeder", "*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            uploadedPhotos.add(file);
            notifier.showInfo("Billede valgt", file.getName());
            saveOrderButton.setDisable(false);
        }
    }

    @FXML
    protected void onCameraCaptureClick(ActionEvent event) {
        if (uploadedPhotos.size() >= 5) {
            notifier.showWarning("Maks billeder nået", "Maksimalt 5 billeder tilladt.");
            return;
        }
        if (orderNumberField.getText().trim().isEmpty()) {
            notifier.showWarning("Manglende ordrenummer", "Indtast ordrenummer før optagelse.");
            return;
        }
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String path = System.getProperty("java.io.tmpdir") + File.separator + "capture_" + ts + ".jpg";
        boolean ok = OpenCVHelper.getInstance().saveFrameToFile(path);
        if (ok) {
            File img = new File(path);
            uploadedPhotos.add(img);
            notifier.showInfo("Billede taget", img.getName());
            saveOrderButton.setDisable(false);
        } else {
            notifier.showWarning("Kamera fejl", "Kunne ikke tage billede.");
        }
    }

    @FXML
    protected void onSaveOrderClick(ActionEvent event) {
        String orderNumber = orderNumberField.getText().trim();
        String comment = commentField.getText().trim();
        try {
            operatorService.saveOrder(orderNumber, uploadedPhotos, comment, currentUser);
            notifier.showInfo("Ordre gemt", orderNumber);
            uploadedPhotos.clear();
            orderNumberField.clear();
            commentField.clear();
            saveOrderButton.setDisable(true);
            loadOrders();
        } catch (Exception e) {
            notifier.showWarning("Fejl", "Kunne ikke gemme: " + e.getMessage());
        }
    }

    @FXML
    protected void onLogoutButtonClick(ActionEvent event) {
        changeScene(View.ROLE_SELECTION, getStageFromEvent(event));
    }
}










