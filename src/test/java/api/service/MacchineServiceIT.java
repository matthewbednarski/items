package api.service;

import api.model.Items;
import api.model.Macchine;
import api.model.Macchine.Macchina;
import api.model.TipiMacchina;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by NICOLA on 06/06/2016.
 */
public class MacchineServiceIT {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    MacchineService service;
    @Inject
    Macchine macchine;

    @Before
    public void setUp() throws Exception {
        for (int i = 1; i <= 4; i++) {
            Macchina macchina = new Macchina();
            macchina.setTarga("BB42" + i + "SL");
            macchina.setColor("white");
            macchina.setTipo(TipiMacchina.BERLINA);
            macchine.getMacchine().add(macchina);
            logger.debug("Added car: " + macchina.toString());
        }
    }

    @Test
    public void add() throws Exception {
        Macchine.Macchina macchina = new Macchine.Macchina();
        macchina.setTarga("BB424SL");
        macchina.setProduttore("Seat");
        assertThat(macchina).isNotIn(macchine);
        service.add(macchina);
        assertThat(macchina).isIn(macchine);
    }

    @Test
    public void createPdfFile() throws IOException {

        // Il file viene creato dentro la cartella "items.api"
        String filename = "prova.pdf";
        String message = "Ho creato un pdf";

        PDDocument doc = new PDDocument();
        try {

            PDPage page = new PDPage();
            doc.addPage(page);

            PDFont font = PDType1Font.COURIER_BOLD;
            PDPageContentStream contents = new PDPageContentStream(doc, page);
            contents.beginText();
            contents.setFont(font, 20);
            contents.newLineAtOffset(100, 700);
            contents.showText(message);
            contents.endText();
            contents.close();
            doc.save(filename);

        } catch (IOException e) {
            logger.info("File not created or already exists!");
        } finally {
            if (doc != null) {
                doc.close();
            }

        }


    }


    @Test
    public void extractFromPdf() throws Exception {

        PDDocument pd = new PDDocument();
        PDDocument container;
        PDFTextStripper stripper = new PDFTextStripper();

        try {
            File input = new File("C:\\Users\\NICOLA\\items.api\\prova.pdf");
            File output = new File("C:\\Users\\NICOLA\\items.api\\SampleText.txt");
            container = pd.load(input);
            System.out.println(container.getNumberOfPages());
            System.out.println(container.isEncrypted());
            pd.save("copyOfprova.pdf");
            stripper.setStartPage(1);
            stripper.setEndPage(1);
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
            stripper.writeText(container, wr);
            if (pd != null) {
                pd.close();
            }
            wr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



