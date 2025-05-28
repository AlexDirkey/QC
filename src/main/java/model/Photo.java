package model;

import java.time.LocalDateTime;

public class Photo {
    private int id;
    private String orderNumber;
    private String filePath;
    private String uploadedBy;
    private LocalDateTime uploadedAt;
    private String comment;
    private String status;

    public Photo(int id, String orderNumber, String filePath, String uploadedBy, LocalDateTime uploadedAt, String comment, String status) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.filePath = filePath;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
        this.comment = comment;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public String getComment() {
        return comment;
    }

    public String getStatus() {
        return status;
    }
}
