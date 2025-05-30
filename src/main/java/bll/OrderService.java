package bll;

import dal.PhotoRepository;
import model.Photo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderService håndterer filtrering og sortering af ordrer.
 * Bruges af både Operator- og QA-controllere.
 */
public class OrderService {

    private final PhotoRepository photoRepo;

    public OrderService() {
        this.photoRepo = new PhotoRepository();
    }

    public List<Photo> getPendingOrders() {
        return photoRepo.getAllPhotos().stream()
                .filter(p -> !p.isApproved() && !p.isInReview())
                .collect(Collectors.toList());
    }

    public List<Photo> getInReviewOrders() {
        return photoRepo.getAllPhotos().stream()
                .filter(Photo::isInReview)
                .collect(Collectors.toList());
    }

    public List<Photo> getCompletedOrders() {
        return photoRepo.getAllPhotos().stream()
                .filter(Photo::isApproved)
                .collect(Collectors.toList());
    }

    public List<Photo> getUnapprovedPhotos() {
        return photoRepo.getAllPhotos().stream()
                .filter(p -> !p.isApproved())
                .collect(Collectors.toList());
    }

    public List<Photo> getPhotosByOrderNumber(String orderNumber) {
        return photoRepo.getAllPhotos().stream()
                .filter(p -> p.getOrderNumber().equals(orderNumber))
                .collect(Collectors.toList());
    }
}
