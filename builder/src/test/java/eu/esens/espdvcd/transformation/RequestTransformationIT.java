package eu.esens.espdvcd.transformation;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.nio.file.Path;

public class RequestTransformationIT  extends AbstractTransformationTest{

    private static DOMSource source;

    private static final String XML_REF = "espd-request-v2.xml";

    @BeforeClass
    public static void transform() {

        String xslRef = "espd_document_de.xsl";

        StreamSource xmlSource = new StreamSource(RequestTransformationIT.class.getResource(XML_REF).toExternalForm());
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
        final Path path = writePdf(new StreamSource(RequestTransformationIT.class.getResource(XML_REF).toExternalForm()), EULanguageCodeEnum.DE);
        openPath(path);
    }

}
