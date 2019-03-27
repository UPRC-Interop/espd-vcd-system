/**
 * Copyright 2016-2019 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.transformation;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath;


public class ResponseTransformationEnTest {

    private static DOMSource source;
    private final static Logger logger = LoggerFactory.getLogger(ResponseTransformationEnTest.class);

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

    /* Moves to Part III instead of Part II */
    @Ignore
    @Test
    public void hasLocalizedPartTwoTitle() {
        assertThat(source, hasXPath("/html/body/div/div[3]/h3/text()", containsString("Part II: Information concerning the economic operator")));
    }


}
