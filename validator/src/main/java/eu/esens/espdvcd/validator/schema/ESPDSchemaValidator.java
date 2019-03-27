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
package eu.esens.espdvcd.validator.schema;

import eu.esens.espdvcd.schema.enums.EDMSubVersion;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import eu.esens.espdvcd.schema.SchemaUtil;
import eu.esens.espdvcd.schema.XSD;
import eu.esens.espdvcd.validator.ArtefactValidator;
import eu.esens.espdvcd.validator.ValidationResult;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ESPD schema validator.
 * <p>
 * Created by Ulf Lotzmann on 03/05/2016.
 */
public class ESPDSchemaValidator implements ArtefactValidator {

    private List<ValidationResult> validationMessages = new LinkedList<>();
    private String xsdPath;
    private Class jaxbClass;

    public ESPDSchemaValidator(InputStream is, String xsdPath, Class jaxbClass, EDMSubVersion v) throws SAXException, JAXBException {

        this.xsdPath = xsdPath;
        this.jaxbClass = jaxbClass;

        // initialise schema from the specified xsd
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL xsdURL = XSD.class.getResource(xsdPath);
        Schema schema = sf.newSchema(xsdURL);

        // validate input stream against schema
        validateXML(is, schema, v);
    }

    private void validateXML(InputStream is, Schema schema, EDMSubVersion v) throws JAXBException {

        // creating unmarshaller
        Unmarshaller unmarshaller = SchemaUtil.getUnmarshaller(v);

        // setting schema
        unmarshaller.setSchema(schema);
        unmarshaller.setEventHandler(validationEvent -> {
            validationMessages.add(new ValidationResult.Builder(String.valueOf(validationMessages.size()),
                    "(line " + validationEvent.getLocator().getLineNumber() +
                            ", column " + validationEvent.getLocator().getColumnNumber() + ")",
                    validationEvent.getMessage())
                    .flag("error")
                    .build());
            return true;
        });

        // validate the given input stream against the specified schema
        try {
            unmarshaller.unmarshal(is);
        } catch (Exception e) {
            validationMessages.add(new ValidationResult.Builder(String.valueOf(validationMessages.size()),
                    "(line 0, column 0)", e.getMessage())
                    .flag("error")
                    .build());
        }

    }

    /**
     * Provides validation result.
     *
     * @return true, if validation was successful
     */
    @Override
    public boolean isValid() {
        // the xml should be valid if there are no validation events reported
        return validationMessages.isEmpty();
    }

    /**
     * Provides list of validation events.
     *
     * @return list of events where validation was not successful; empty, if validation was successful
     */
    @Override
    public List<ValidationResult> getValidationMessages() {
        return validationMessages;
    }

    /**
     * Provides filtered list of validation events.
     *
     * @param text, for which the list entries are filtered
     * @return filtered list of validation events
     */
    @Override
    public List<ValidationResult> getValidationMessagesFiltered(String text) {
        return validationMessages
                .stream()
                .filter(validationResult -> validationResult.getText().contains(text))
                .collect(Collectors.toList());
    }

}
