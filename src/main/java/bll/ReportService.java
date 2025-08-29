package bll;

import model.Photo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Serviceklasse til at generere PDF-rapporter baseret på fotos

public class ReportService {

    private final PhotoService photoService; // Service til at hente fotos fra DAL

    public ReportService(PhotoService photoService) {
        this.photoService = photoService;
    }

    // Genererer en PDF-rapport for en liste af fotos
    // Returnerer en File, som peger på den færdige PDF

    public File generatePdf(List<Photo> photos) throws IOException {
        if (photos == null || photos.isEmpty()) {
            throw new IOException("Ingen fotos at generere rapport for.");
        }

        // Filnavn baseret på ordre

        String orderNumber = safe(photos.get(0).getOrderNumber());
        String baseName    = "QC_Report_" + (orderNumber != null ? orderNumber : "UNKNOWN");

        // Opret midlertidig mappe i systemets tmp-dir

        File outDir        = new File(System.getProperty("java.io.tmpdir"), "qc_reports");
        if (!outDir.exists()) outDir.mkdirs();

        // Midlertidig fil til rapporten

        File outFile       = File.createTempFile(baseName + "_", ".pdf", outDir);

        // Opretter selve dokumentet

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            // Start en content stream til at skrive tekst

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float margin = 50f;
                float y = page.getMediaBox().getHeight() - margin;

                // Header med overskrift

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.newLineAtOffset(margin, y);
                cs.showText("QC Report - Order " + (orderNumber != null ? orderNumber : ""));
                cs.endText();
                y -= 24;

                // Kolonneoverskrifter

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(margin, y);
                cs.showText("UploadedAt          Status      By         Comment");
                cs.endText();
                y -= 16;

                // Formatter til tidsstempler

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                cs.setFont(PDType1Font.HELVETICA, 9);

                // Skriver én linje pr. foto
                for (Photo p : photos) {
                    // Hvis der ikke er mere plads på siden, afbryd
                    if (y < margin + 50) {
                        break;
                    }

                    String ts = p.getUploadedAt() != null ? p.getUploadedAt().format(fmt) : "";
                    String line = String.format("%-18s  %-10s  %-10s  %s",
                            ts,
                            safe(p.getStatus()),
                            safe(p.getUploadedBy()),
                            truncate(safe(p.getComment()), 60)
                    );

                    cs.beginText();
                    cs.newLineAtOffset(margin, y);
                    cs.showText(line);
                    cs.endText();
                    y -= 14;
                }
            }

            // Gemmer dokumentet til fil
            doc.save(outFile);
        }

        return outFile;
    }

    // Overload: Generér rapport direkte ud fra et ordrenummer

    public File generatePdfForOrder(String orderNumber) throws IOException {
        List<Photo> photos = photoService.getPhotosByOrderNumber(orderNumber);
        return generatePdf(photos);
    }

    // Hjælpefunktion til null-sikring

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    // Hjælpefunktion til at afkorte tekst, så linjen ikke bliver for lang

    private static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}

