package bll;

import dal.PhotoRepository;
import model.Photo;

import java.util.List;

public class PhotoService {
    private final PhotoRepository photoRepository = new PhotoRepository();

    public void savePhoto(Photo photo) {
        photoRepository.save(photo);
    }

    public List<Photo> getPhotosByOrderNumber(String orderNumber) {
        return photoRepository.findByOrderNumber(orderNumber);
    }

    public List<Photo> getUnapprovedPhotos() {
        return photoRepository.findUnapprovedPhotos();
    }

    public void updatePhotoStatus(int photoId, boolean approved) {
        photoRepository.updateStatus(photoId, approved ? "approved" : "rejected");
    }
}
