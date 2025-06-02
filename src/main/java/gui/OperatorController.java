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
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;



//Controller for operator
public class OperatorController extends BaseController {

    //  FXML-komponenter
    @FXML private ListView<String> pendingOrdersList;     // Viser pr. ordrenr. + antal billeder (PENDING)
    @FXML private ListView<String> inReviewOrdersList;    // Viser ID + ordrenr. på IN_REVIEW‐fotos
    @FXML private ListView<String> completedOrdersList;   // Viser ID + ordrenr. + status på APPROVED/REJECTED
    @FXML private TextField  orderNumberField;            // Input‐felt til ordrenummer
    @FXML private TextField  commentField;                // Input‐felt til kommentar
    @FXML private Button     captureButton;               // “Tag billede”‐knap
    @FXML private Button     uploadButton;                // “Upload billede”‐knap
    @FXML private Button     saveOrderButton;             // “Gem ordren”‐knap
    @FXML private Label      statusLabel;                 // Kan vise statusbeskeder

    // bll
    private final OperatorService       operatorService  = new OperatorService();
    private final NotificationHelper    notifier         = new NotificationHelper(this);
    private final List<File>            uploadedPhotos   = new ArrayList<>();
    private final String                currentUser      = "demo_user"; // TODO: Erstat med rigtig bruger


    // Kaldes efter at view er loadet, og FXMl er forbundet
    @FXML
    protected void initialize() {
        loadOrders();

        // Deaktiver “Gem ordren”‐knappen til at begynde med
        saveOrderButton.setDisable(true);

        // Aktiver kun “Gem ordren” når der både er tekst i orderNumberField
        // og mindst ét billede i uploadedPhotos
        orderNumberField.textProperty().addListener((obs, oldV, newV) ->
                saveOrderButton.setDisable(
                        orderNumberField.getText().trim().isEmpty() || uploadedPhotos.isEmpty()
                )
        );
    }


    //Indlæser alle photos fra database - og inddeler demi kategorier
    private void loadOrders() {

        // --- 1) HENT ALLE PENDING‐FOTOS og grupér pr. ordrenummer ---
        List<Photo> pendingPhotos = operatorService.getPendingPhotos();
        // Gruppér på ordrenummer og tæl antal fotos pr. ordre
        var groupedPending = pendingPhotos.stream()
                .collect(Collectors.groupingBy(Photo::getOrderNumber, Collectors.counting()));

        // Byg en liste af tekstlinjer: "Ordre: <ordrenr> | Billeder: <antal>"
        ObservableList<String> pendingItems = FXCollections.observableArrayList(
                groupedPending.entrySet().stream()
                        .map(e -> "Ordre: " + e.getKey() + " | Billeder: " + e.getValue())
                        .collect(Collectors.toList())
        );
        pendingOrdersList.setItems(pendingItems);

        // 2 HENT ALLE IN_REVIEW‐FOTOS
        List<Photo> inReview = operatorService.getInReviewPhotos();
        // Byg en liste af tekstlinjer: "ID: <id> | Ordre: <ordrenr>"
        ObservableList<String> inReviewItems = FXCollections.observableArrayList(
                inReview.stream()
                        .map(p -> "ID: " + p.getId() + " | Ordre: " + p.getOrderNumber())
                        .collect(Collectors.toList())
        );
        inReviewOrdersList.setItems(inReviewItems);

        // HENT ALLE FÆRDIGE (APPROVED/REJECTED)
        List<Photo> done = operatorService.getCompletedPhotos();
        // Byg en liste af tekstlinjer: "ID: <id> | Ordre: <ordrenr> | <status>"
        ObservableList<String> doneItems = FXCollections.observableArrayList(
                done.stream()
                        .map(p -> "ID: " + p.getId()
                                + " | Ordre: " + p.getOrderNumber()
                                + " | " + p.getStatus())
                        .collect(Collectors.toList())
        );
        completedOrdersList.setItems(doneItems);
    }


    //Åbner Filechooser, og lader brugeren vælge et billede fra den fysiske enhed
    @FXML
    protected void onUploadButtonClick(ActionEvent event) {
        if (uploadedPhotos.size() >= 5) {
            notifier.showWarning("Maks billeder nået", "Du kan kun uploade op til 5 billeder.");
            return;
        }
        String orderNumber = orderNumberField.getText().trim();
        if (orderNumber.isEmpty()) {
            notifier.showWarning("Manglende ordrenummer", "Indtast ordrenummer før upload.");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vælg billede til upload");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Billeder", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File pickedFile = chooser.showOpenDialog(stage);
        if (pickedFile != null) {
            uploadedPhotos.add(pickedFile);
            notifier.showInfo("Billede valgt", "Fil: " + pickedFile.getName());
            // Nu har vi mindst ét billede + ordrenummer → aktiver “Gem ordren”
            saveOrderButton.setDisable(false);
        }
    }


    // Prøver at tage et billede ved hjælp af OpenCV. Hvis der ikke er adgang til et kamera, kommer der en advarsel
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

        String ts = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String tmpPath = System.getProperty("java.io.tmpdir")
                + File.separator + "capture_" + ts + ".jpg";

        boolean success = OpenCVHelper.getInstance().saveFrameToFile(tmpPath);
        if (success) {
            File snapshot = new File(tmpPath);
            uploadedPhotos.add(snapshot);
            notifier.showInfo("Billede taget", "Snapshot gemt: " + snapshot.getName());
            saveOrderButton.setDisable(false);
        } else {
            notifier.showWarning("Kamera fejl", "Kunne ikke tage billede (ingen kamera eller ingen frame).");
        }
    }


    // Ved et tryk på Gem ordren, oprettes ét photo med status pending
    @FXML
    protected void onSaveOrderClick(ActionEvent event) {
        String orderNumber = orderNumberField.getText().trim();
        String comment     = commentField.getText().trim();

        try {
            operatorService.saveOrder(orderNumber, uploadedPhotos, comment, currentUser);
            notifier.showInfo("Ordre gemt", "Ordre nr. " + orderNumber + " er nu oprettet.");

            // Ryd formular + midlertidige billeder
            uploadedPhotos.clear();
            orderNumberField.clear();
            commentField.clear();
            saveOrderButton.setDisable(true);

            // Opdater ALLE lister (PENDING, IN_REVIEW og COMPLETED)
            loadOrders();
        }
        catch (Exception e) {
            notifier.showWarning("Fejl ved gem", "Kunne ikke gemme ordren: " + e.getMessage());
        }
    }

    /**
     * “Log ud”‐knap: skift tilbage til rolle‐valget.
     */
    @FXML
    protected void onLogoutButtonClick(ActionEvent event) {
        changeScene(View.ROLE_SELECTION, getStageFromEvent(event));
    }
}











