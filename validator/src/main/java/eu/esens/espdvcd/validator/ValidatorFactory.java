package eu.esens.espdvcd.validator;

import java.io.InputStream;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;

/**
 * The ValidatorFactory is a Factory class that provides static methods for creating
 * different kinds of validator:<br>
 * - ESPD request schema validator<br>
 * - ESPD response schema validator<br>
 *
 * Created by Ulf Lotzmann on 11/05/2016.
 */
public class ValidatorFactory {

    /**
     * Factory method that creates an ESPD request schema validator object
     * and performs the schema validation for the XML provided by the specified input stream.
     *
     * @param is input stream with XML data
     * @return schema validator object
     */
    public static SchemaValidator createESPDRequestSchemaValidator(InputStream is) throws SAXException, JAXBException {
        // FIXME: the path returned by XSD.ESPD_REQUEST.xsdPath() is probably incorrect, hence returning static string
        //return new ESPDSchemaValidator(is, XSD.ESPD_REQUEST.xsdPath(), ESPDRequestType.class);
        return new ESPDSchemaValidator(is, "/schema/v1/maindoc/ESPDRequest-1.0.xsd", ESPDRequestType.class);
    }

    /**
     * Factory method that creates an ESPD response schema validator object
     * and performs the schema validation for the XML provided by the specified input stream.
     *
     * @param is input stream with XML data
     * @return schema validator object
     */
    public static SchemaValidator createESPDResponseSchemaValidator(InputStream is) throws SAXException, JAXBException {
        // FIXME: the path returned by XSD.ESPD_REQUEST.xsdPath() is probably incorrect, hence returning static string
        //return new ESPDSchemaValidator(is, XSD.ESPD_RESPONSE.xsdPath(), ESPDRequestType.class);
        return new ESPDSchemaValidator(is, "/schema/v1/maindoc/ESPDResponse-1.0.xsd", ESPDResponseType.class);
    }

}
