package eu.esens.espdvcd.validator.schema;

import eu.esens.espdvcd.builder.util.SchemaUtil;
import eu.esens.espdvcd.builder.util.XSD;
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

    public ESPDSchemaValidator(InputStream is, String xsdPath, Class jaxbClass) throws SAXException, JAXBException {

        this.xsdPath = xsdPath;
        this.jaxbClass = jaxbClass;

        // initialise schema from the specified xsd
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL xsdURL = XSD.class.getResource(xsdPath);
        Schema schema = sf.newSchema(xsdURL);

        // validate input stream against schema
        validateXML(is, schema);
    }


    private void validateXML(InputStream is, Schema schema) throws JAXBException {

        // creating unmarshaller
        Unmarshaller unmarshaller = SchemaUtil.getUnmarshaller();

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
        /*unmarshaller.setEventHandler(new ValidationEventHandler() {
            @Override
            public boolean handleEvent(ValidationEvent validationEvent) {
                // add all validation event messages to list for later evaluation
                validationMessages.add(validationEvent.getMessage() +
                        " (line " + validationEvent.getLocator().getLineNumber() +
                        ", column " + validationEvent.getLocator().getColumnNumber() + ")");
                return true;
            }
        });*/

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