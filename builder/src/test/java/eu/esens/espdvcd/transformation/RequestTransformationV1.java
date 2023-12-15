package eu.esens.espdvcd.transformation;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.ESPDRequest;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.transform.stream.StreamSource;
import java.nio.file.Path;

public class RequestTransformationV1 extends AbstractTransformationTest {
    private static ESPDRequest espdRequest;
    private static final String XML_REF = "artefacts/regulated/v1/ESPDRequest_DA_Test-1.0.2-v1.0.xml";

    @BeforeClass
    public static void transform() throws BuilderException {
        StreamSource xmlSource = new StreamSource(RequestTransformationV1.class.getClassLoader().getResourceAsStream(XML_REF));
        TransformationHelper th = new TransformationHelper();
        espdRequest = th.getESPDRequestV1(xmlSource);
    }

    @Test
    public void showHtml() throws Exception {
        final Path path = writeHtml(espdRequest, EULanguageCodeEnum.EL);
        openPath(path);
    }

    @Test
    public void showPdf() throws Exception {
        final Path path = writePdf(espdRequest, EULanguageCodeEnum.EL);
        openPath(path);
    }
}
