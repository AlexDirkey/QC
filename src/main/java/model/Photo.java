package model;

import java.time.LocalDateTime;


//Denne class repræsenterer et photo
public class Photo {
    private int id;
    private String orderNumber;
    private String uploadedBy;
    private String status;
    private LocalDateTime uploadedAt;
    private boolean approved;
    private boolean inReview;
    private String comment;
    private String filePath;

    public Photo(int id, String orderNumber, String uploadedBy, String status,
                 LocalDateTime uploadedAt, boolean approved, String comment) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.uploadedBy = uploadedBy;
        this.status = status;
        this.uploadedAt = uploadedAt;
        this.approved = approved;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public boolean isApproved() {
        return approved;
    }

    public boolean isInReview() {
        return inReview;
    }

    public String getComment() {
        return comment;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void setInReview(boolean inReview) {
        this.inReview = inReview;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
