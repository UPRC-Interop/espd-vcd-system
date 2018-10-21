package eu.esens.espdvcd.transformation;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.nio.file.Path;

public class ResponseTransformationIT extends AbstractTransformationTest{

    private static DOMSource source;

    @BeforeClass
    public static void transform() {
        String xmlRef = "espd-response-v2.xml";
        String xslRef = "espd_document_de.xsl";

        StreamSource xmlSource = new StreamSource(ResponseTransformationIT.class.getResource(xmlRef).toExternalForm());
        StreamSource xslSource = new StreamSource(ResponseTransformationIT.class.getResource(xslRef).toExternalForm());
        source =  new TransformationHelper().transform(xmlSource, xslSource);
    }

    @Test
    public void showHtml() throws Exception {
        final Path path = writeHtml(source);
        openPath(path);
    }

    @Test
    public void showPdf() throws Exception {
        final Path path = writePdf(source);
        openPath(path);
    }

}
