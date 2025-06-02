package bll;

import dal.PhotoRepository;
import model.Photo;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


//Gemmer fotoes, og i forskellige kategori
public class OperatorService {
    private final PhotoRepository photoRepository = new PhotoRepository();

    //Gemmer en ordre. Som udgangspunkt 'pendign'
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

    //Henter photos med 'pending'
    public List<Photo> getPendingPhotos() {
        return photoRepository.findUnapprovedPhotos();
    }

   //Henter photos med 'in-review'
    public List<Photo> getInReviewPhotos() {
        return photoRepository.getAllPhotos().stream()
                .filter(p -> "IN_REVIEW".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.toList());
    }

    //Henter photos, som er'approved', eller 'rejected'
    public List<Photo> getCompletedPhotos() {

        return photoRepository.getAllPhotos().stream()
                .filter(p -> {
                    String st = p.getStatus();
                    return "APPROVED".equalsIgnoreCase(st)
                            || "REJECTED".equalsIgnoreCase(st);
                })
                .collect(Collectors.toList());
    }


    //Markerer t photo, så QA kan behandle den
    public void markInReview(int photoId) {
        photoRepository.updateStatusToInReview(photoId);
    }
}
