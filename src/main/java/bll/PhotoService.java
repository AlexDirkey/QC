package bll;

import dal.PhotoRepository;
import model.Photo;

import java.util.List;

//Henter godkendte og ikke godkendte ordrer, og opdaterer deres status
public class PhotoService {
    private final PhotoRepository repo = new PhotoRepository();


    // henter alle fotos, der endnu ikke er godkendte
    public List<Photo> getUnapprovedPhotos() {
        return repo.findUnapprovedPhotos();
    }


    // Henter alle fotos med status approved
    public List<Photo> getApprovedPhotos() {
        return repo.findApprovedPhotos();
    }


    // opdaterer fotos til approved eller rejected
    public void updatePhotoStatus(int photoId, boolean approved) {
        repo.updateStatus(photoId, approved);
    }


    //Henter alle fotos, uanset status - bruges til at filtrere
    public List<Photo> getAll() {
        return repo.getAllPhotos();
    }


    //Henter fotos til en given ordre
    public List<Photo> getPhotosByOrderNumber(String orderNumber) {
        return repo.findByOrderNumber(orderNumber);
    }


    //Henter photos, der har en status
    public List<Photo> getPhotosWithStatus(String status) {
        return repo.findByStatus(status);
    }
}
