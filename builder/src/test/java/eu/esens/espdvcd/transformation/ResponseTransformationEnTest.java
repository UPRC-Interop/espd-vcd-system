package eu.esens.espdvcd.transformation;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath;

public class ResponseTransformationEnTest {

    private static DOMSource source;

    @BeforeClass
    public static void transform() {
        String xmlRef = "espd-response-v2.xml";
        String xslRef = "espd_document_en.xsl";

        StreamSource xmlSource = new StreamSource(ResponseTransformationIT.class.getResource(xmlRef).toExternalForm());
        StreamSource xslSource = new StreamSource(ResponseTransformationIT.class.getResource(xslRef).toExternalForm());
        source =  new TransformationHelper().transform(xmlSource, xslSource);
    }

    @Test
    public void hasLocalizedTitle() {
        assertThat(source, hasXPath("/html/body/div/div[1]/h2/text()", containsString("European Single Procurement Document (ESPD)")));
    }

    @Test
    public void hasLocalizedPartOneTitle() {
        assertThat(source, hasXPath("/html/body/div/div[2]/h3/text()", containsString("Part I: Information concerning the procurement procedure and the contracting authority or contracting entity")));
    }

    @Test
    public void hasLocalizedPublicationInfoTitle() {
        assertThat(source, hasXPath("/html/body/div/div[2]/div[1]/div[1]/text()", containsString("Information about publication")));
    }

    @Test
    public void hasLocalizedPartTwoTitle() {
        assertThat(source, hasXPath("/html/body/div/div[3]/h3/text()", containsString("Part II: Information concerning the economic operator")));
    }


}
