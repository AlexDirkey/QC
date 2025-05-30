package bll;

import model.Photo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import util.MailHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * ReportService håndterer generering af PDF-rapporter
 * og afsendelse via e-mail.
 */
public class ReportService {
    private static final PDFont FONT = PDType1Font.HELVETICA;
    private static final float FONT_SIZE = 12f;
    private static final float MARGIN_X = 50f;
    private static final float START_Y = 750f;
    private static final String OUTPUT_FILENAME = "approved_report.pdf";

    private final PhotoService photoService;

    /**
     * Dependency injection af PhotoService.
     * @param photoService service til at hente fotos
     */
    public ReportService(PhotoService photoService) {
        this.photoService = photoService;
    }

    /**
     * Genererer en PDF-rapport med alle godkendte fotos
     * og sender den til den angivne e-mail.
     *
     * @param email modtagerens e-mailadresse
     * @throws IOException ved IO- eller PDFBox-fejl
     */
    public void generateAndSendApprovedReport(String email) throws Exception {
        List<Photo> approved = photoService.getApprovedPhotos();
        if (approved.isEmpty()) {
            throw new IllegalStateException("Ingen godkendte billeder at rapportere");
        }

        File pdf = createPdfReport(approved);
        MailHelper.sendEmailWithAttachment(
                email,
                "Din godkendte ordrerapport",
                "Hej! Vedhæftet er din godkendte ordrerapport.",
                pdf
        );
    }

    /**
     * Genererer PDF-rapportfil og returnerer fil-reference.
     *
     * @param photos liste af godkendte Photo-objekter
     * @return den gemte PDF-fil
     * @throws IOException ved PDFBox-fejl
     */
    private File createPdfReport(List<Photo> photos) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.setFont(FONT, FONT_SIZE);
                cs.beginText();
                cs.setLeading(FONT_SIZE * 1.2f);
                cs.newLineAtOffset(MARGIN_X, START_Y);

                cs.showText("Godkendte Ordrer Rapport:");
                cs.newLine();
                cs.newLine();

                for (Photo p : photos) {
                    String line = String.format(
                            "Ordre: %s | Bruger: %s | Dato: %s",
                            p.getOrderNumber(),
                            p.getUploadedBy(),
                            p.getUploadedAt()
                    );
                    cs.showText(line);
                    cs.newLine();
                }
                cs.endText();
            }

            File output = new File(OUTPUT_FILENAME);
            doc.save(output);
            return output;
        }
    }
}