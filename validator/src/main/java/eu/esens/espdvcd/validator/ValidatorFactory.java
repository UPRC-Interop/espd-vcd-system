package eu.esens.espdvcd.validator;

import eu.esens.espdvcd.validator.schema.ESPDSchemaValidator;

import java.io.File;
import java.io.InputStream;

import eu.esens.espdvcd.validator.schematron.ESPDSchematronValidator;
import eu.esens.espdvcd.validator.schematron.SchematronOrigin;
//import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
//import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;

/**
 * The ValidatorFactory is a Factory class that provides static methods for
 * creating different kinds of validator:<br>
 * - ESPD request schema validator<br>
 * - ESPD response schema validator<br>
 * <p>
 * Created by Ulf Lotzmann on 11/05/2016.
 */
public class ValidatorFactory {

    private static final String ERROR_UNKNOWN_SCHEMATRON_ORIGIN = "Error... Unknown schematron origin";

    /**
     * Factory method that creates an ESPD request schema validator object and
     * performs the schema validation for the XML provided by the specified
     * input stream.
     *
     * @param is input stream with XML data
     * @return schema validator object
     */
    public static ArtefactValidator createESPDRequestSchemaValidator(InputStream is) throws SAXException, JAXBException {
        // FIXME: the path returned by XSD.ESPD_REQUEST.xsdPath() is probably incorrect, hence returning static string
        //return new ESPDSchemaValidator(is, XSD.ESPD_REQUEST.xsdPath(), ESPDRequestType.class);
        return new ESPDSchemaValidator(is, "/schema/v1/maindoc/ESPDRequest-1.0.xsd", ESPDRequestType.class);
    }

    /**
     * Factory method that creates an ESPD response schema validator object and
     * performs the schema validation for the XML provided by the specified
     * input stream.
     *
     * @param is input stream with XML data
     * @return schema validator object
     */
    public static ArtefactValidator createESPDResponseSchemaValidator(InputStream is) throws SAXException, JAXBException {
        // FIXME: the path returned by XSD.ESPD_REQUEST.xsdPath() is probably incorrect, hence returning static string
        //return new ESPDSchemaValidator(is, XSD.ESPD_RESPONSE.xsdPath(), ESPDRequestType.class);
        return new ESPDSchemaValidator(is, "/schema/v1/maindoc/ESPDResponse-1.0.xsd", ESPDResponseType.class);
    }

    /**
     * Factory method that creates an ESPD artefact (request or response) schematron
     * validator object and performs the schematron validation for the XML provided by
     * the specified input stream.
     *
     * @param is input stream with XML data
     * @return schematron validator object
     */
    static ArtefactValidator createESPDArtefactSchematronValidator(InputStream is, String schPath) {
        return new ESPDSchematronValidator(is, schPath);
    }

    /**
     * Factory method that creates an ESPD request schematron validator object
     * and performs the schematron validation for the XML provided by the
     * specified file.
     *
     * @param espdRequest file with XML data
     * @param schOrigin origin of schematron file
     * @return schematron validator object
     */
    public static ArtefactValidator createESPDRequestSchematronValidator(File espdRequest, SchematronOrigin schOrigin) {

        switch (schOrigin) {
            case EU:
                return new ESPDSchematronValidator(espdRequest,
                        "/rules/v1/eu/ESPDRequest/sch/02-ESPD-CL-attrb-rules.sch"
                        , "/rules/v1/eu/ESPDRequest/sch/03-ESPD-ID-attrb-rules.sch"
                        , "/rules/v1/eu/ESPDRequest/sch/04-ESPD-Common-BR-rules.sch");
            case EHF:
                return new ESPDSchematronValidator(espdRequest,
                        "/rules/v1/ehf/ESPDRequest/EHF-ESPD-REQUEST.sch");
            default:
                throw new IllegalArgumentException(ERROR_UNKNOWN_SCHEMATRON_ORIGIN);
        }
    }

    /**
     * Factory method that creates an ESPD response schematron validator object
     * and performs the schematron validation for the XML provided by the
     * specified file.
     *
     * @param espdResponse file with XML data
     * @param schOrigin origin of schematron file
     * @return schematron validator object
     */
    public static ArtefactValidator createESPDResponseSchematronValidator(File espdResponse, SchematronOrigin schOrigin) {

        switch (schOrigin) {
            case EU:
                return new ESPDSchematronValidator(espdResponse,
                        "/rules/v1/eu/ESPDResponse/sch/02-ESPD-CL-attrb-rules.sch"
                        , "/rules/v1/eu/ESPDResponse/sch/03-ESPD-ID-attrb-rules.sch"
                        , "/rules/v1/eu/ESPDResponse/sch/04-ESPD-Common-BR-rules.sch"
                        , "/rules/v1/eu/ESPDResponse/sch/05-ESPD-Spec-BR-rules.sch");
            case EHF:
                return new ESPDSchematronValidator(espdResponse,
                        "/rules/v1/ehf/ESPDResponse/EHF-ESPD-RESPONSE.sch");
            default:
                throw new IllegalArgumentException(ERROR_UNKNOWN_SCHEMATRON_ORIGIN);
        }
    }

}
