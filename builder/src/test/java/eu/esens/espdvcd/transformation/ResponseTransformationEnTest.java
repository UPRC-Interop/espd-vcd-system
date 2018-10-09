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
    public void hasLocalizedPartThreeTitle() {
        assertThat(source, hasXPath("/html/body/div/div[2]/h3/text()", containsString("Part III: Exclusion grounds")));
    }

    @Test
    public void hasLocalizedPartThreeATitle() {
        assertThat(source, hasXPath("/html/body/div/div[2]/div[1]/div[1]/text()", containsString("A: Grounds relating to criminal convictions")));
    }

    @Test
    public void hasLocalizedPartThreeASubTitle() {
        assertThat(source, hasXPath("/html/body/div/div[2]/div[1]/div[2]/label[1]/text()", containsString("Article 57(1) of Directive 2014/24/EU sets out the following reasons for exclusion")));
    }

}
