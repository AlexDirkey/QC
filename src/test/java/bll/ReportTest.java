package bll;
import model.Photo;
import java.io.File;
import java.io.IOException;
import java.util.List;

//Enhedstest for ReportTest - verificerer at en pdf-rapport kan genereres, samt at metoden kan håndtere en tom inputliste.


import static org.junit.jupiter.api.Assertions.*;

public class ReportTest {

    @org.junit.jupiter.api.Test
    void generatePdf_withValidPhotos_createsPdfFile() throws IOException {
        ReportService service = new ReportService(new PhotoService());

        //Bygger et Photo-objekt, der repræsenterer et godkendt foto på en ordre

        Photo p = new Photo(0, "ORDER123", "alice", "APPROVED",
                java.time.LocalDateTime.now(), false, "Svejsning A ok");

        //Arrangerer en pdf med en liste og ét foto
        File pdf = service.generatePdf(List.of(p));

        assertNotNull(pdf);
        assertTrue(pdf.exists());
        assertTrue(pdf.length() > 0);
        assertTrue(pdf.getName().toLowerCase().endsWith(".pdf"));
    }

    @org.junit.jupiter.api.Test
    void generatePdf_withEmptyList_throwsIOException() {
        ReportService service = new ReportService(new PhotoService());
        assertThrows(IOException.class, () -> service.generatePdf(List.of()));
    }
}

