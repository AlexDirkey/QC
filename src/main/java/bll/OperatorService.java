package bll;

import dal.PhotoRepository;
import model.Photo;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service-lag for operatør-logic: henter ordrer og opdaterer status,
 * samt gemmer nye ordrer med fotos.
 */
public class OperatorService {
    private final PhotoRepository repo = new PhotoRepository();

    /**
     * Gemmer en ordre med tilhørende fotos og kommentar.
     */
    public void saveOrder(String orderNumber, List<File> photos, String comment, String uploadedBy) {
        List<Photo> toSave = photos.stream()
                .map(file -> {
                    Photo p = new Photo(
                            0,
                            orderNumber,
                            uploadedBy,
                            "PENDING",
                            LocalDateTime.now(),
                            false,
                            comment
                    );
                    p.setFilePath(file.getAbsolutePath());
                    return p;
                })
                .collect(Collectors.toList());
        toSave.forEach(repo::save);
    }

    /**
     * Henter alle fotos med status = 'PENDING'.
     */
    public List<Photo> getPendingPhotos() {
        return repo.findUnapprovedPhotos();
    }

    /**
     * Henter alle fotos med status = 'APPROVED' eller 'REJECTED'.
     */
    public List<Photo> getCompletedPhotos() {
        return repo.findApprovedPhotos();
    }

    /**
     * Marker en foto-id som "IN_REVIEW".
     */
    public void markInReview(int photoId) {
        repo.updateStatus(photoId, false, "IN_REVIEW");
    }
}
