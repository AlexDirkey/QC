package bll;

import dal.PhotoRepository;
import model.Photo;

import java.util.List;

//Gemmer og henter photo-objekter
public class PhotoService {

    //Udfører databasetransaktioner

    private final PhotoRepository photoRepository = new PhotoRepository();

    //Gemmer 'photo' som objekt i databasen

    public void savePhoto(Photo photo) {
        photoRepository.save(photo);
    }

    //Henter fotos, som er tilknyttet et ordrenr

    public List<Photo> getPhotosByOrderNumber(String orderNumber) {
        //Returnerer en liste af photo-objekter. Eller ingen, hvis ingen er tilknyttet
        return photoRepository.findByOrderNumber(orderNumber);
    }

    //Henter photos, der ikke er godkendt
    public List<Photo> getUnapprovedPhotos() {
        return photoRepository.findUnapprovedPhotos();
    }

    //Opdaterer status for et foto
    public void updatePhotoStatus(int photoId, boolean approved) {
        photoRepository.updateStatus(photoId, approved);
    }

    //Henter alle godkendte fotos
    public List<Photo> getApprovedPhotos() {
        return photoRepository.findApprovedPhotos();
    }
}
