package eu.esens.espdvcd.transformation;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.nio.file.Path;

public class RequestTransformationIT  extends AbstractTransformationTest{

    private static DOMSource source;

    @BeforeClass
    public static void transform() {
        String xmlRef = "espd-request-v2.xml";
        String xslRef = "espd_document.xsl";

        StreamSource xmlSource = new StreamSource(RequestTransformationIT.class.getResource(xmlRef).toExternalForm());
        StreamSource xslSource = new StreamSource(RequestTransformationIT.class.getResource(xslRef).toExternalForm());
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
