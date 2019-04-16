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

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.xmlunit.builder.Input.byTransforming;
import static org.xmlunit.builder.Input.fromString;

public class TransformationHelper {

    /**
     * Transformiert das ?bergebene XML-Source mit der XSL-Source
     *
     * @param xmlSource
     *            XML als Source
     * @param xslSource
     *            XSL als Source
     * @return Ergebnis der Transformation als DomSource
     */
    public DOMSource transform(Source xmlSource, Source xslSource) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setURIResolver(new RelativeClasspathURIResolver());
        return (DOMSource) byTransforming(xmlSource).usingFactory(transformerFactory).withStylesheet(xslSource).build();
    }

    /**
     * Gibt die DomSource als String in UTF-8 zurueck ohne INDENT
     */
    public String convertToString(Source source) throws TransformerException {
        StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        transformer.transform(source, new StreamResult(sw));
        return sw.toString();
    }

}
