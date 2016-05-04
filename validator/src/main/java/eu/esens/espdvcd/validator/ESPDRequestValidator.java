package eu.esens.espdvcd.validator;

import eu.esens.espdvcd.schema.XSD;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;

/**
 * Created by Ulf Lotzmann on 03/05/2016.
 */
public class ESPDRequestValidator {

    private List<String> validationMessages = new LinkedList<>();

    public ESPDRequestValidator(InputStream is) throws SAXException, JAXBException {

        // initialise schema from the specified xsd
        //SchemaFactory sf = SchemaFactory.newInstance("grow.names.specification.ubl.schema.xsd.espdrequest-1");
        //SchemaFactory sf = SchemaFactory.newInstance(getXSD().namespaceURI());
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schema;
        //schema = sf.newSchema(new File(...getXSDPath()));
        URL xsdURL = XSD.class.getResource(getXSDPath());
        schema = sf.newSchema(xsdURL);

        // validate input stream against schema
        validateXML(is, schema);
    }

    protected String getXSDPath() {
        // FIXME: the path returned by XSD.ESPD_REQUEST.xsdPath() is probably incorrect, hence returning static string
        //return XSD.ESPD_REQUEST.xsdPath();
        return "/schema/maindoc/ESPDRequest-1.0.xsd";
    }

    protected Class getJaxbClass() {
        return ESPDRequestType.class;
    }

    private void validateXML(InputStream is, Schema schema) throws JAXBException {

        // FIXME (UL): just test code to check the unmarchalling process, which is throwing an exception...
        //ESPDRequestType er = JAXB.unmarshal(is, ESPDRequestType.class); // this is working

        JAXBContext jc = JAXBContext.newInstance( ESPDRequestType.class );
        Unmarshaller u = jc.createUnmarshaller();
        u.unmarshal( is );

        // FIXME (UL): ... below is what I intended the method to look like (several approaches I wanted to test)
/*
        JAXBContext jc = JAXBContext.newInstance(getJaxbClass());

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        // unmarshaller.setSchema(schema);
        // unmarshaller.setEventHandler(validationEvent -> validationMessages.add(validationEvent.getMessage() +
        //        " (line " + validationEvent.getLocator().getLineNumber() +
        //       ", column " + validationEvent.getLocator().getColumnNumber() + ")"));
        unmarshaller.setEventHandler(new ValidationEventHandler() {
            @Override
            public boolean handleEvent(ValidationEvent validationEvent) {
                // add all validation event messages to list for later evaluation
                validationMessages.add(validationEvent.getMessage() +
                        " (line " + validationEvent.getLocator().getLineNumber() +
                        ", column " + validationEvent.getLocator().getColumnNumber() + ")");
                return true;
            }
        });
        unmarshaller.unmarshal(is);*/
    }

    public boolean isValid() {
        // the xml should be valid if there are no validation events reported
        return validationMessages.isEmpty();
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    public List<String> getValidationMessagesFiltered(String keyWord) {
        return validationMessages.stream().filter(s -> s.contains(keyWord)).collect(Collectors.toList());
    }


}
