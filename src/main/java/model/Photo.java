package model;

import java.time.LocalDateTime;

// Photo repræsenterer et enkelt foto, som er knyttet til en ordre.
// Objektet bruges på tværs af lagene (GUI, BLL, DAL)

public class Photo {
    private int id;                // Primærnøgle i databasen
    private String orderNumber;    // Ordrenummer som billedet tilhører
    private String uploadedBy;     // Brugernavn på den, der uploadede billedet
    private String status;         // Status for billedet: fx "IN_REVIEW", "APPROVED", "REJECTED"
    private LocalDateTime uploadedAt; // Tidsstempel for upload (bruges i historik/rapport)
    private boolean approved;      // true hvis billedet er godkendt
    private boolean inReview;      //  flag: true hvis billedet afventer QA
    private String comment;        // Eventuel kommentar fra operatør/QA (fx “Svejsning A ok”)
    private String filePath;       // Absolut filsti til billedfilen på disken (bruges til preview/rapport)

    // Konstruktor til at oprette et Photo-objekt med attributter

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

    // Getter for DB-id (bruges typisk i repositories og ved opdateringer)

    public int getId() {
        return id;
    }

    // Ordrenummer er nøgle for at gruppere billeder i rapporter og QA-lister

    public String getOrderNumber() {
        return orderNumber;
    }

    // Hvem uploadede billedet (bruges i historik/ansvar)

    public String getUploadedBy() {
        return uploadedBy;
    }

    // Status som string ("Approved", etc)

    public String getStatus() {
        return status;
    }

    // Hvornår blev billedet uploadet (bruges i GUI-visning og rapport)

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    // Er billedet godkendt

    public boolean isApproved() {
        return approved;
    }

    // Er billedet under review

    public boolean isInReview() {
        return inReview;
    }

    // Eventuel kommentar til billedet

    public String getComment() {
        return comment;
    }

    // Absolut sti til filen (kræves for at kunne vise billedet i ImageView og vedhæfte i PDF)

    public String getFilePath() {
        return filePath;
    }

    // Sæt godkendt (bruges typisk af QA)

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    // Sæt in-review  (bruges når operatør har uploadet og afventer QA)

    public void setInReview(boolean inReview) {
        this.inReview = inReview;
    }

    // Sæt filsti efter filen er gemt (fx fra OperatorController)

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

