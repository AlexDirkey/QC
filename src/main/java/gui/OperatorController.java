package gui;

import javafx.scene.image.ImageView;
import util.OpenCVHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import dal.DataBaseConnector;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

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
    private TextField commentField;

    @FXML
    private Label statusLabel;

    private final List<File> uploadedPhotos = new ArrayList<>();
    private final DataBaseConnector databaseConnector = new DataBaseConnector();
    private final String currentUser = "demo_user"; // TODO: Replace with actual logged-in user


    static {
        try {
            System.load("C:/Users/45817/IdeaProjects/QC/src/main/resources/lib/opencv/opencv_java490.dll");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Kunne ikke loade OpenCV DLL: " + e.getMessage());
        }
    }

    @FXML
    private void onUploadButtonClick() {
        if (uploadedPhotos.size() >= 5) {
            statusLabel.setText("Maksimalt 5 billeder tilladt.");
            return;
        }

        String orderNumber = orderNumberField.getText().trim();
        String comment = commentField.getText().trim();

        if (orderNumber.isEmpty()) {
            statusLabel.setText("Indtast et ordrenummer.");
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Vælg billede til upload");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Billeder", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                uploadedPhotos.add(selectedFile);
                savePhotoToDatabase(selectedFile, orderNumber, comment);
                statusLabel.setText("Billede valgt (" + uploadedPhotos.size() + "/5): " + selectedFile.getName());
            } else {
                statusLabel.setText("Ingen fil valgt.");
            }
        }
    }

    @FXML
    private void onCameraCaptureClick() {
        if (uploadedPhotos.size() >= 5) {
            statusLabel.setText("Maksimalt 5 billeder tilladt.");
            return;
        }

        String orderNumber = orderNumberField.getText().trim();
        String comment = commentField.getText().trim();

        if (orderNumber.isEmpty()) {
            statusLabel.setText("Indtast et ordrenummer.");
            return;
        }

        File capturedImage = capturePhotoFromCamera();
        if (capturedImage != null) {
            uploadedPhotos.add(capturedImage);
            savePhotoToDatabase(capturedImage, orderNumber, comment);
            statusLabel.setText("Billede taget (" + uploadedPhotos.size() + "/5): " + capturedImage.getName());
        } else {
            statusLabel.setText("Kunne ikke tage billede.");
        }
    }

    private File capturePhotoFromCamera() {
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) return null;

        Mat frame = new Mat();
        File imageFile = null;

        if (camera.read(frame)) {
            try {
                String timestamp = LocalDateTime.now().toString().replaceAll("[:.]", "-");
                imageFile = File.createTempFile("photo_" + timestamp, ".jpg");
                Imgcodecs.imwrite(imageFile.getAbsolutePath(), frame);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        camera.release();
        return imageFile;
    }

    private void savePhotoToDatabase(File imageFile, String orderNumber, String comment) {
        String sql = "INSERT INTO photos (order_number, image_data, uploaded_by, uploaded_at, comment) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(imageFile)) {

            stmt.setString(1, orderNumber);
            stmt.setBinaryStream(2, fis, (int) imageFile.length());
            stmt.setString(3, currentUser);
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(5, comment);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            showWarning("Fejl ved upload", "Kunne ikke gemme billedet i databasen.");
        }
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        changeScene("/gui/RoleSelectionView.fxml", stage);
    }

    @FXML
    private ImageView imageView;

    OpenCVHelper cam = OpenCVHelper.getInstance();

    {
        if(cam.isCameraOpen())

        {
            Mat frame = cam.captureFrame();
            Image fxImage = cam.matToImage(frame);
            imageView.setImage(fxImage);
        }
    }
}








