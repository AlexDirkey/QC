package gui;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import bll.PhotoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Photo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;



import java.io.File;
import java.io.IOException;
import java.util.List;

public class QAController extends BaseController {

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

    @FXML
    private void onGenerateReportClick() {
        List<Photo> approvedPhotos = photoService.getApprovedPhotos();

        if (approvedPhotos.isEmpty()) {
            showWarning("Ingen billeder", "Ingen godkendte billeder fundet.");
            return;
        }

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            PDFont font = PDType1Font.HELVETICA;
            contentStream.setFont(font, 12);
            contentStream.setFont(font, 12);
            contentStream.setFont(font, 12);
            contentStream.beginText();
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 750);

            contentStream.showText("Godkendte Ordrer Rapport:");
            contentStream.newLine();
            contentStream.newLine();

            for (Photo photo : approvedPhotos) {
                String line = "Ordre: " + photo.getOrderNumber()
                        + " | Bruger: " + photo.getUploadedBy()
                        + " | Dato: " + photo.getUploadedAt();
                contentStream.showText(line);
                contentStream.newLine();
            }

            contentStream.endText();
            contentStream.close();

            File outputFile = new File("approved_report.pdf");
            doc.save(outputFile);
            showInfo("Rapport genereret", "PDF gemt som " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            showWarning("Fejl", "Kunne ikke generere rapport: " + e.getMessage());
        }
    }

    @FXML
    private void onVisHistorikClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Historik.fxml"));
            Parent root = loader.load();

            HistorikController controller = loader.getController();
            controller.loadHistorik("1234"); // <- dynamisk ordre-ID senere!

            Stage stage = new Stage();
            stage.setTitle("Ordrehistorik");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
