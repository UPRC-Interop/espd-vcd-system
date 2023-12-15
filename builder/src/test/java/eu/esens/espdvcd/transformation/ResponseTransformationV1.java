package eu.esens.espdvcd.transformation;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.ESPDResponse;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.transform.stream.StreamSource;
import java.nio.file.Path;

public class ResponseTransformationV1 extends AbstractTransformationTest {
    private static ESPDResponse espdResponse;
    private static final String XML_REF = "artefacts/regulated/v1/ESPDResponse_DA_Test-1.0.2-v1.0.xml";

    @BeforeClass
    public static void transform() throws BuilderException {
        StreamSource xmlSource = new StreamSource(ResponseTransformationV1.class.getClassLoader().getResourceAsStream(XML_REF));
        TransformationHelper th = new TransformationHelper();
        espdResponse = th.getESPDResponseV1(xmlSource);
    }

    @Test
    public void showHtml() throws Exception {
        final Path path = writeHtml(espdResponse, EULanguageCodeEnum.EL);
        openPath(path);
    }

    @Test
    public void showPdf() throws Exception {
        final Path path = writePdf(espdResponse, EULanguageCodeEnum.EL);
        openPath(path);
    }

}
