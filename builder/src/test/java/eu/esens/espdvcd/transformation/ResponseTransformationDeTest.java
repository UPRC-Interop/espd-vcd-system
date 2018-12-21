package eu.esens.espdvcd.transformation;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath;

public class ResponseTransformationDeTest {

    private static DOMSource source;

    @BeforeClass
    public static void transform() {
        String xmlRef = "espd-response-v2.xml";
        String xslRef = "espd_document_de.xsl";

        StreamSource xmlSource = new StreamSource(ResponseTransformationIT.class.getResource(xmlRef).toExternalForm());
        StreamSource xslSource = new StreamSource(ResponseTransformationIT.class.getResource(xslRef).toExternalForm());
        source = new TransformationHelper().transform(xmlSource, xslSource);
    }

    @Ignore
    @Test
    public void hasLocalizedTitle() {
        assertThat(source, hasXPath("/html/body/div/div[1]/h2/text()", containsString("Einheitliche Europ�ische Eigenerkl�rung (EEE)")));
    }

    @Ignore
    @Test
    public void hasLocalizedPartOneTitle() {
        assertThat(source, hasXPath("/html/body/div/div[2]/h3/text()", containsString("Teil I: Angaben zum Vergabeverfahren und zum �ffentlichen Auftraggeber oder Sektorenauftraggeber")));
    }

    @Ignore
    @Test
    public void hasLocalizedPublicationInfoTitle() {
        assertThat(source, hasXPath("/html/body/div/div[2]/div[1]/div[1]/text()", containsString("Angaben zur Ver�ffentlichung")));
    }

    @Ignore
    @Test
    public void hasLocalizedPartTwoTitle() {
        assertThat(source, hasXPath("/html/body/div/div[3]/h3/text()", containsString("Teil II: Angaben zum Wirtschaftsteilnehmer")));
    }

    @Ignore
    @Test
    public void hasLocalizedPartTwoThree() {
        assertThat(source, hasXPath("/html/body/div/div[4]/h3/text()", containsString("Teil III: Ausschlussgr�nde")));
    }

    @Ignore
    @Test
    public void hasLocalizedPartTwoFour() {
        assertThat(source, hasXPath("/html/body/div/div[5]/h3/text()", containsString("Teil IV: Eignungskriterien")));
    }

    @Ignore
    @Test
    public void hasLocalizedPartTwoFourSubTitle() {
        assertThat(source, hasXPath("/html/body/div/div[5]/div/div[1]/text()", containsString(": Globalvermerk zur Erf�llung aller Eignungskriterien")));
    }
}