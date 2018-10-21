package eu.esens.espdvcd.transformation;

import com.openhtmltopdf.pdfboxout.PdfBoxRenderer;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.endsWithAny;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Servicemethoden fuer das Transformieren von XML-Dateien
 */
public class TransformationService {

    /**
     * Erzeugt aus der uebergebenen Referenz ein Source-Objekt.
     *
     * @param ref Die Referenz auf eine Datei, z.B. XSL
     * @return Die entstandene Source
     */
    private Source getSourceByReference(String ref) {
        if (isBlank(ref) || !endsWithAny(ref, ".xsl", ".xml")) {
            throw new IllegalArgumentException(format("Resource '%s' ist nicht valide", ref));
        }

        URL resource = getClass().getResource(ref);
        if (resource == null) {
            throw new IllegalArgumentException(format("Resource '%s' wurde nicht gefunden.", ref));
        }

        return new StreamSource(resource.toExternalForm());
    }

    /**
     * Erstellt aus dem Document ein PDF und schreibt es in den OutputStream.
     */
    void createPdf(org.w3c.dom.Document document, OutputStream outputStream) throws IOException {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withW3cDocument(document, null);
        createPdf(outputStream, builder);
    }

    private void createPdf(OutputStream outputStream, PdfRendererBuilder builder) throws IOException {
        builder.toStream(outputStream);
        PdfBoxRenderer renderer = builder.buildPdfRenderer();

        renderer.layout();
        renderer.createPDF();
    }

}