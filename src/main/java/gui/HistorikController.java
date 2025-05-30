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

//Controller til visning af fotohistorik af en ordre.

public class HistorikController {

    @FXML
    private ListView<String> historikListView;

    @FXML
    private ImageView historikPreview;

    @FXML
    private Label kommentarLabel;


    //Henter photo-ojekter
    private final PhotoService photoService = new PhotoService();
    private ObservableList<Photo> photos;

    //Indlæser historie for en given ordre
    public void loadHistorik(String orderNumber) {
        List<Photo> photoList = photoService.getPhotosByOrderNumber(orderNumber);
        photos = FXCollections.observableArrayList(photoList);

        //LAver en visningsliste med bruger og tidspunkt
        ObservableList<String> visning = FXCollections.observableArrayList();
        for (Photo photo : photos) {
            visning.add("Bruger: " + photo.getUploadedBy() + " | " + photo.getUploadedAt());
        }
        historikListView.setItems(visning);
    }

    @FXML
    //Initialiserer lister
    private void initialize() {
        historikListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> onPhotoSelected());
    }

    //Kaldes, når et foto vælges på listen
    private void onPhotoSelected() {
        int index = historikListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < photos.size()) {
            Photo selected = photos.get(index);
            File file = new File(selected.getFilePath());
            if (file.exists()) {
                historikPreview.setImage(new Image(file.toURI().toString()));
            } else {
                historikPreview.setImage(null);
            }
            //Viser kommentarer
            kommentarLabel.setText(selected.getComment() != null ? selected.getComment() : "Ingen kommentar.");
        }
    }
}
