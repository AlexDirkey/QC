package bll;

import model.Photo;

import java.io.File;
import java.io.IOException;
import java.util.List;


// Classen her henter fotos for en ordre, genererer PDF-rapport og evt. sende den via e-mail.

public class DocumentationService {

    private final ReportService reportService; // Ansvar: generere PDF-rapporter
    private final PhotoService photoService;   // Ansvar: hente fotos fra DB (via DAL)

    // Constructor der "arver" (komposition) services, så de kan bruges her

    public DocumentationService(ReportService reportService, PhotoService photoService) {
        this.reportService = reportService;
        this.photoService = photoService;
    }

    // Henter alle fotos for en ordre og genererer en PDF-rapport.

    public File generateReportForOrder(String orderNumber) throws IOException {
        List<Photo> photos = photoService.getPhotosByOrderNumber(orderNumber);
        return reportService.generatePdf(photos);
    }

    // Sender en eksisterende PDF-rapport til en given e-mailadresse.
    // Her bruges MailHelper (util) som en facade til Jakarta Mail API.

    public void sendReport(String email, String orderNumber, File pdfFile) throws Exception {
        util.MailHelper.sendEmailWithAttachment(
                email,
                "QC-rapport " + orderNumber,
                "Se vedhæftet rapport.",
                pdfFile
        );
    }
}

