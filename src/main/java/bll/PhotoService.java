package bll;

import dal.PhotoRepository;
import model.Photo;

import java.util.List;


//Henter godkendte og ikke godkendte ordrer, og opdaterer deres status
public class PhotoService {
    private final PhotoRepository repo = new PhotoRepository();

    /**
     * Henter alle fotos med status = 'PENDING' eller 'IN_REVIEW' (dvs. endnu ikke godkendte).
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
     * Opdaterer fotoets approved-flag (true/false) og sætter status til 'APPROVED' eller 'REJECTED'.
     */
    public void updatePhotoStatus(int photoId, boolean approved) {
        repo.updateStatus(photoId, approved);
    }

    /**
     * Henter alle fotos (uanset status).
     * Bruges fx, hvis man ønsker at filtrere IN_REVIEW, APPROVED, REJECTED i controlleren.
     */
    public List<Photo> getAll() {
        return repo.getAllPhotos();
    }

    /**
     * Henter alle fotos for en given ordre (bruges fx til historik-vinduet).
     */
    public List<Photo> getPhotosByOrderNumber(String orderNumber) {
        return repo.findByOrderNumber(orderNumber);
    }
}

