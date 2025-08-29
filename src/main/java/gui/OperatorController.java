package gui;

import bll.OperatorService;
import gui.NotificationHelper;
import gui.util.SessionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Photo;
import util.OpenCVHelper;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Controller for operatorens GUI.
// Operator kan uploade billeder (enten fra fil eller kamera),
// knytte dem til et ordrenummer og gemme ordren i systemet.
// Viser samtidig lister over ordre-statusser (PENDING, IN_REVIEW, COMPLETED)

public class OperatorController extends BaseController {

    // ===== FXML-felter (matcher OperatorView.fxml) =====
    @FXML private TextField orderNumberField;    // Inputfelt til ordrenummer
    @FXML private TextField commentField;        // Inputfelt til evt. kommentar
    @FXML private Button   saveOrderButton;      // Knappen til at gemme ordren
    @FXML private Button   captureButton;        // Knappen til at tage et billede via kamera
    @FXML private Label    statusLabel;          // Statusbeskeder til operatoren

    // Tre lister i GUI’en → opdelt efter status
    @FXML private ListView<String> pendingOrdersList;    // Ordrer, der er pending (uploadet men ikke reviewet)
    @FXML private ListView<String> inReviewOrdersList;   // Ordrer, der er sendt til QA
    @FXML private ListView<String> completedOrdersList;  // Ordrer, der er færdigbehandlet (APPROVED/REJECTED)


    private final OperatorService    operatorService  = new OperatorService(); // Logik for operator
    private final NotificationHelper notifier         = new NotificationHelper(this); // Dialog-hjælper
    private final List<File>         uploadedPhotos   = new ArrayList<>(); // Midlertidig liste af valgte billeder
    private String                   currentUser; // Hentes fra session (bruger der er logget ind)

    // Initialisering efter FXML er loadet

    @FXML
    protected void initialize() {
        // Hent brugernavn fra singleton SessionService

        currentUser = SessionService.getInstance().currentUsername();

        // Indlæs ordre-lister ved start

        loadOrders();

        // Gem-knap deaktiveret indtil vi har input (vises som sløret)

        saveOrderButton.setDisable(true);

        // Tilføj listener: aktiver “Gem ordren”, når der både er ordrenummer og mindst ét billede

        orderNumberField.textProperty().addListener((obs, oldV, newV) ->
                saveOrderButton.setDisable(
                        orderNumberField.getText().trim().isEmpty() || uploadedPhotos.isEmpty()
                )
        );
    }

    // Henter fotos fra DB via OperatorService og opdeler i lister i GUI

    private void loadOrders() {
        // 1) Pending
        List<Photo> pending = operatorService.getPendingPhotos();
        ObservableList<String> pendingItems = FXCollections.observableArrayList(
                pending.stream()
                        .map(p -> "ID: " + p.getId() + " | Ordre: " + p.getOrderNumber())
                        .collect(Collectors.toList())
        );
        pendingOrdersList.setItems(pendingItems);

        // 2) In review
        List<Photo> inReview = operatorService.getInReviewPhotos();
        ObservableList<String> inReviewItems = FXCollections.observableArrayList(
                inReview.stream()
                        .map(p -> "ID: " + p.getId() + " | Ordre: " + p.getOrderNumber())
                        .collect(Collectors.toList())
        );
        inReviewOrdersList.setItems(inReviewItems);

        // 3) Completed (APPROVED/REJECTED)
        List<Photo> done = operatorService.getCompletedPhotos();
        ObservableList<String> doneItems = FXCollections.observableArrayList(
                done.stream()
                        .map(p -> "ID: " + p.getId()
                                + " | Ordre: " + p.getOrderNumber()
                                + " | " + p.getStatus())
                        .collect(Collectors.toList())
        );
        completedOrdersList.setItems(doneItems);
    }

    // Brugeren vælger en eksisterende billedfil på sin enhed

    @FXML
    protected void onUploadButtonClick(ActionEvent event) {
        if (uploadedPhotos.size() >= 5) {
            notifier.showWarning("Maks billeder nået", "Du kan kun uploade op til 5 billeder.");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vælg billede");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Billeder", "*.jpg", "*.jpeg", "*.png")
        );

        // Åbn filvælger i aktuelt vindue

        File pickedFile = chooser.showOpenDialog(((Stage) saveOrderButton.getScene().getWindow()));
        if (pickedFile != null) {
            uploadedPhotos.add(pickedFile);
            notifier.showInfo("Billede valgt", "Fil: " + pickedFile.getName());
            // Gem-knap kan nu aktiveres, hvis der også er ordrenummer
            saveOrderButton.setDisable(orderNumberField.getText().trim().isEmpty());
        }
    }

    // Brugeren tager et billede via OpenCV (webcam, eller hvad der nu er tilgængeligt på den valgte enhed). Gemmes i temp-dir

    @FXML
    protected void onCameraCaptureClick(ActionEvent event) {
        if (uploadedPhotos.size() >= 5) {
            notifier.showWarning("Maks billeder nået", "Du kan kun uploade op til 5 billeder.");
            return;
        }
        String orderNumber = orderNumberField.getText().trim();
        if (orderNumber.isEmpty()) {
            notifier.showWarning("Manglende ordrenummer", "Indtast ordrenummer før optagelse.");
            return;
        }

        // Filnavn med timestamp for at undgå overlap

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String tmpPath = System.getProperty("java.io.tmpdir") + File.separator + "capture_" + ts + ".jpg";

        // Brug OpenCV til at tage snapshot/frame

        boolean success = OpenCVHelper.getInstance().saveFrameToFile(tmpPath);
        if (success) {
            File snapshot = new File(tmpPath);
            uploadedPhotos.add(snapshot);
            notifier.showInfo("Billede taget", "Snapshot gemt: " + snapshot.getName());
            saveOrderButton.setDisable(orderNumberField.getText().trim().isEmpty());
        } else {
            notifier.showWarning("Kamera fejl", "Kunne ikke tage billede (ingen kamera eller intet billede).");
        }
    }

    // Gemmer ordren med tilhørende billeder i systemet (kører i baggrundstråd)

    @FXML
    protected void onSaveOrderClick(ActionEvent event) {
        String orderNumber = orderNumberField.getText().trim();
        String comment     = commentField.getText().trim();

        if (orderNumber.isEmpty() || uploadedPhotos.isEmpty()) {
            notifier.showWarning("Manglende data", "Indtast ordrenummer og vælg mindst ét billede.");
            return;
        }

        saveOrderButton.setDisable(true);
        statusLabel.setText("Gemmer ordre...");

        // Baggrundsopgave så GUI ikke fryser
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                operatorService.saveOrder(orderNumber, uploadedPhotos, comment, currentUser);
                return null;
            }
        };

        // Håndtering når det lykkes

        task.setOnSucceeded(e -> {
            notifier.showInfo("Ordre gemt", "Ordre nr. " + orderNumber + " er nu oprettet.");
            uploadedPhotos.clear();
            orderNumberField.clear();
            commentField.clear();
            statusLabel.setText("");
            saveOrderButton.setDisable(true);
            loadOrders(); // Opdater lister
        });

        // Håndtering hvis noget fejler

        task.setOnFailed(e -> {
            statusLabel.setText("");
            saveOrderButton.setDisable(false);
            notifier.showWarning("Fejl ved gem", String.valueOf(task.getException().getMessage()));
        });

        new Thread(task).start();
    }

    // Log ud: skift tilbage til rollevalgsskærmen

    @FXML
    protected void onLogoutButtonClick(ActionEvent event) {
        changeScene(View.ROLE_SELECTION, getStageFromEvent(event));
    }
}












