package gui;

import bll.PhotoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Photo;

import java.io.File;
import java.util.List;

// Controller for historik-vinduet.
// Viser alle billeder, der hører til en bestemt ordre, inkl. preview og kommentarer.

public class HistorikController {


    @FXML
    private ListView<String> historikListView; // Liste over fotos til en ordre, som string

    @FXML
    private ImageView historikPreview; // Preview af valgt billede

    @FXML
    private Label kommentarLabel; // Label til at vise evt. kommentarer


    private final PhotoService photoService = new PhotoService(); // Service til at hente billeder fra DB
    private ObservableList<Photo> photos; // Holder de faktiske Photo-objekter (ikke bare tekstvisning)

    // Indlæser historikken for et givent ordrenummer
    // → Henter billederne fra DB og fylder listen med bruger+tidspunkt

    public void loadHistorik(String orderNumber) {
        List<Photo> photoList = photoService.getPhotosByOrderNumber(orderNumber);
        photos = FXCollections.observableArrayList(photoList);

        // Bygger en tekstliste (hvem og hvornår)

        ObservableList<String> visning = FXCollections.observableArrayList();
        for (Photo photo : photos) {
            visning.add("Bruger: " + photo.getUploadedBy() + " | " + photo.getUploadedAt());
        }
        historikListView.setItems(visning);
    }

    // Kører når FXML er loadet: tilføjer en listener, så vi reagerer når brugeren vælger et foto

    @FXML
    private void initialize() {
        historikListView.getSelectionModel()
                .selectedIndexProperty()
                .addListener((obs, oldVal, newVal) -> onPhotoSelected());
    }

    // Kaldes når et foto vælges i listen

    private void onPhotoSelected() {
        int index = historikListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < photos.size()) {
            Photo selected = photos.get(index);
            File file = new File(selected.getFilePath());

            // Hvis filen findes, så vis den i ImageView

            if (file.exists()) {
                historikPreview.setImage(new Image(file.toURI().toString()));
            } else {
                historikPreview.setImage(null);
            }

            // Vis kommentar

            kommentarLabel.setText(
                    selected.getComment() != null ? selected.getComment() : "Ingen kommentar."
            );
        }
    }
}

