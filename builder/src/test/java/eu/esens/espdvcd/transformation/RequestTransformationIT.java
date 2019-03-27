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
