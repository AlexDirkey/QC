package bll;

import dal.PhotoRepository;
import model.Photo;

import java.util.List;

public class PhotoService {
    private final PhotoRepository repo = new PhotoRepository();

    public List<Photo> getPhotosWithStatus(String status) {
        return repo.findByStatus(status);
    }

    // Brug denne i QA, hvis PENDING er “afventer godkendelse”
    public List<Photo> getUnapprovedPhotos() {
        return repo.findByStatus("PENDING");
    }

    public void updatePhotoStatus(int photoId, boolean approved) {
        repo.updateStatus(photoId, approved);
    }

    public List<Photo> getPhotosByOrderNumber(String orderNumber) {
        return repo.findByOrderNumber(orderNumber);
    }
}

