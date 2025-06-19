package bll;

import dal.PhotoRepository;
import model.Photo;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//Gemmer fotos, og inddeler dem i kategorier
public class OperatorService {
    private final PhotoRepository photoRepository = new PhotoRepository();
    private final PhotoService photoService = new PhotoService(); //


    //Gemmer en ny ordre med status pending
    public void saveOrder(String orderNumber, List<File> imageFiles, String comment, String uploadedBy) throws Exception {
        for (File imageFile : imageFiles) {
            Photo photo = new Photo(
                    0,
                    orderNumber,
                    uploadedBy,
                    "PENDING",
                    LocalDateTime.now(),
                    false,
                    comment
            );
            photo.setFilePath(imageFile.getAbsolutePath());
            photoRepository.save(photo);
        }
    }


    // Henter fotos, der er pending
    public List<Photo> getPendingPhotos() {
        return photoService.getPhotosWithStatus("PENDING");
    }


    // Henter fotos, der er in review
    public List<Photo> getInReviewPhotos() {
        return photoRepository.getAllPhotos().stream()
                .filter(p -> "IN_REVIEW".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.toList());
    }


    //Henter fotos, der har status approved eller rejected
    public List<Photo> getCompletedPhotos() {
        return photoRepository.getAllPhotos().stream()
                .filter(p -> {
                    String st = p.getStatus();
                    return "APPROVED".equalsIgnoreCase(st) || "REJECTED".equalsIgnoreCase(st);
                })
                .collect(Collectors.toList());
    }


    //Markerer et photo til at være in-review
    public void markInReview(int photoId) {
        photoRepository.updateStatusToInReview(photoId);
    }
}