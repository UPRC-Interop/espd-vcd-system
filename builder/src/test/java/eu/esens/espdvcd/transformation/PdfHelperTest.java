package eu.esens.espdvcd.transformation;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import org.w3c.dom.Document;

import javax.xml.transform.Source;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PdfHelperTest {

    private final TransformationService transformationService = new TransformationService();

    private void generatePDF(Path target, Source doc, EULanguageCodeEnum lang) throws IOException {
        try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(target))) {
            transformationService.createPdf(doc, os, lang);
        }
    }

    /**
     * Erzeugt zwei Tempfiles mit der HTML und der PDF und gibt den Path der PDF zur?ck.
     */
    public Path generateTempPDF(Source content, EULanguageCodeEnum lang) throws IOException {
        final Path target = Files.createTempFile("testpdf", ".pdf");
        generatePDF(target, content, lang);

        return target;
    }
}
