package eu.esens.espdvcd.transformation;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath;
import static org.junit.Assert.assertThat;

public class ResponseTransformationDeTest {

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
    public void hasLocalizedTitle() {
        assertThat(source, hasXPath("/html/body/div/div[1]/h2/text()", containsString("Einheitliche Europäische Eigenerklärung (EEE)")));
    }

    @Test
    public void hasLocalizedPartThreeTitle() {
        assertThat(source, hasXPath("/html/body/div/div[2]/h3/text()", containsString("Teil III: Ausschlussgründe")));
    }

    @Test
    public void hasLocalizedPartThreeATitle() {
        assertThat(source, hasXPath("/html/body/div/div[2]/div[1]/div[1]/text()", containsString("A: Gründe im Zusammenhang mit einer strafrechtlichen Verurteilung")));
    }

    @Test
    public void hasLocalizedPartThreeASubTitle() {
        assertThat(source, hasXPath("/html/body/div/div[2]/div[1]/div[2]/label[1]/text()", containsString("In Artikel 57 Absatz 1 der Richtlinie 2014/24/EU werden folgende Ausschlussgründe genannt:")));
    }

}
