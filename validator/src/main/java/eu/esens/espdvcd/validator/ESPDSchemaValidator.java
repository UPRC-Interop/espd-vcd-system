package eu.esens.espdvcd.validator;

//import eu.esens.espdvcd.schema.XSD;
//import eu.esens.espdvcd.schema.SchemaUtil;
import eu.esens.espdvcd.builder.util.SchemaUtil;
import eu.esens.espdvcd.builder.util.XSD;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 * ESPD schema validator.
 *
 * Created by Ulf Lotzmann on 03/05/2016.
 */
public class ESPDSchemaValidator implements SchemaValidator {

    private List<String> validationMessages = new LinkedList<>();
    private String xsdPath;
    private Class jaxbClass;

    protected ESPDSchemaValidator(InputStream is, String xsdPath, Class jaxbClass) throws SAXException, JAXBException {

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
        unmarshaller.setEventHandler(validationEvent -> validationMessages.add(validationEvent.getMessage() +
               " (line " + validationEvent.getLocator().getLineNumber() +
              ", column " + validationEvent.getLocator().getColumnNumber() + ")"));
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
        }
        catch (Exception e) {
            validationMessages.add(e.getMessage());
        }

    }

    /**
     * Provides validation result.
     * @return true, if validation was successful
     */
    @Override
    public boolean isValid() {
        // the xml should be valid if there are no validation events reported
        return validationMessages.isEmpty();
    }

    /**
     * Provides list of validation events.
     * @return list of events where validation was not successful; empty, if validation was successful
     */
    @Override
    public List<String> getValidationMessages() {
        return validationMessages;
    }

    /**
     * Provides filtered list of validation events.
     * @param keyWord, for which the list entries are filtered
     * @return filtered list of validation events
     */
    @Override
    public List<String> getValidationMessagesFiltered(String keyWord) {
        return validationMessages.stream().filter(s -> s.contains(keyWord)).collect(Collectors.toList());
    }


}
