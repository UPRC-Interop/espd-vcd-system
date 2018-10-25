package eu.esens.espdvcd.transformation;

import com.openhtmltopdf.pdfboxout.PdfBoxRenderer;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.endsWithAny;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.xmlunit.builder.Input.byTransforming;

/**
 * Servicemethoden fuer das Transformieren von XML-Dateien
 */
public class TransformationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformationService.class);

    private static final String ESPD_DOCUMENT = "espd_document_";
    private static final String XSL = ".xsl";

    /**
     * Transformiert das ?bergebene XML-Source mit der XSL-Source
     *
     * @param xmlSource
     *            XML als Source
     * @param xslSource
     *            XSL als Source
     * @return Ergebnis der Transformation als DomSource
     */
    public DOMSource transform(Source xmlSource, Source xslSource) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setURIResolver(new RelativeClasspathURIResolver());
        return (DOMSource) byTransforming(xmlSource).usingFactory(transformerFactory).withStylesheet(xslSource).build();
    }

    /**
     * Erzeugt aus der uebergebenen Referenz ein Source-Objekt.
     *
     * @param ref Die Referenz auf eine Datei, z.B. XSL
     * @return Die entstandene Source
     */
    private Source getSourceByReference(String ref) {
        if (isBlank(ref) || !endsWithAny(ref, ".xsl", ".xml")) {
            throw new IllegalArgumentException(format("Resource '%s' is not valid", ref));
        }

        URL resource = getClass().getResource(ref);
        if (resource == null) {
            throw new IllegalArgumentException(format("Resource '%s' could not be found.", ref));
        }

        return new StreamSource(resource.toExternalForm());
    }

    /**
     * Erstellt aus dem Document ein PDF und schreibt es in den OutputStream.
     */
    public void createPdf(Source documentSource, OutputStream outputStream, EULanguageCodeEnum lang) throws IOException {

        Source xslSource = getSourceByReference(MessageFormat.format("{0}{1}{2}", ESPD_DOCUMENT, lang.name().toLowerCase(), XSL));
        DOMSource transformed  = transform(documentSource, xslSource);

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withW3cDocument((Document)transformed.getNode(), null);
        createPdf(outputStream, builder);
    }

    public void createHtml(Source documentSource, OutputStream outputStream, EULanguageCodeEnum lang) {

        Source xslSource = getSourceByReference(MessageFormat.format("{0}{1}{2}", ESPD_DOCUMENT, lang.name().toLowerCase(), XSL));
        DOMSource transformed  = transform(documentSource, xslSource);

        Result outputTarget = new StreamResult(outputStream);
        try {
            TransformerFactory.newInstance().newTransformer().transform(transformed, outputTarget);
        } catch (TransformerException e) {
            LOGGER.error("Error while transforming document", e);
        }
    }

    private void createPdf(OutputStream outputStream, PdfRendererBuilder builder) throws IOException {
        try {
            builder.useFont(new File(getClass().getResource("\\font\\FreeSans.ttf").toURI()), "Free Sans");
            builder.toStream(outputStream);
            PdfBoxRenderer renderer = builder.buildPdfRenderer();
            renderer.layout();
            renderer.createPDF();
        } catch (URISyntaxException e) {
            LOGGER.error("Error while creating pdf document", e);
        }
    }

    public InputStream createHtmlStream(StreamSource documentSource, EULanguageCodeEnum languageCodeEnum) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStream os = new BufferedOutputStream(outputStream)) {
            createHtml(documentSource, os, languageCodeEnum);
        } catch (IOException e) {
            LOGGER.error("Error while transforming document", e);
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }


    public InputStream createPdfStream(StreamSource documentSource, EULanguageCodeEnum languageCodeEnum) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStream os = new BufferedOutputStream(outputStream)) {
            createPdf(documentSource, os, languageCodeEnum);
        } catch (IOException e) {
            LOGGER.error("Error while transforming document", e);
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

}