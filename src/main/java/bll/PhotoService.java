package bll;

import dal.PhotoRepository;
import model.Photo;
import java.util.List;

/**
 * Service-lag for QA og historik: henter u-godkendte, godkendte fotos,
 * samt henter fotos pr. ordre og opdaterer status.
 */
public class PhotoService {
    private final PhotoRepository repo = new PhotoRepository();

    /**
     * Henter alle fotos med status = 'PENDING'.
     */
    public List<Photo> getUnapprovedPhotos() {
        return repo.findUnapprovedPhotos();
    }

    /**
     * Henter alle fotos med status = 'APPROVED'.
     */
    public List<Photo> getApprovedPhotos() {
        return repo.findApprovedPhotos();
    }

    /**
     * Opdaterer fotoets status til 'APPROVED' eller 'REJECTED'.
     */
    public void updatePhotoStatus(int photoId, boolean approved) {
        String status = approved ? "APPROVED" : "REJECTED";
        repo.updateStatus(photoId, approved, status);
    }

    /**
     * Henter alle fotos (uanset status).
     */
    public List<Photo> getAll() {
        return repo.getAllPhotos();
    }

    /**
     * Henter alle fotos for en given ordre.
     */
    public List<Photo> getPhotosByOrderNumber(String orderNumber) {
        return repo.findByOrderNumber(orderNumber);
    }
}

