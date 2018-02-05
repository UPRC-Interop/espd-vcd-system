package eu.esens.espdvcd.validator;

import eu.esens.espdvcd.validator.schema.ESPDSchemaValidator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.esens.espdvcd.validator.schematron.ESPDSchematronValidator;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
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

    public enum SchOrigin {
        EU, EHF
    }

    /**
     * Factory method that creates an ESPD request schema validator object and
     * performs the schema validation for the XML provided by the specified
     * input stream.
     *
     * @param is input stream with XML data
     * @return schema validator object
     */
    public static ArtifactValidator createESPDRequestSchemaValidator(InputStream is) throws SAXException, JAXBException {
        // FIXME: the path returned by XSD.ESPD_REQUEST.xsdPath() is probably incorrect, hence returning static string
        //return new ESPDSchemaValidator(is, XSD.ESPD_REQUEST.xsdPath(), ESPDRequestType.class);
        return new ESPDSchemaValidator(is, "/schema/maindoc/ESPDRequest-1.0.xsd", ESPDRequestType.class);
    }

    /**
     * Factory method that creates an ESPD response schema validator object and
     * performs the schema validation for the XML provided by the specified
     * input stream.
     *
     * @param is input stream with XML data
     * @return schema validator object
     */
    public static ArtifactValidator createESPDResponseSchemaValidator(InputStream is) throws SAXException, JAXBException {
        // FIXME: the path returned by XSD.ESPD_REQUEST.xsdPath() is probably incorrect, hence returning static string
        //return new ESPDSchemaValidator(is, XSD.ESPD_RESPONSE.xsdPath(), ESPDRequestType.class);
        return new ESPDSchemaValidator(is, "/schema/maindoc/ESPDResponse-1.0.xsd", ESPDResponseType.class);
    }

    /**
     * @param is     input stream with XML data
     * @param origin The origin of schematron files
     * @return
     */
    public static ArtifactValidator createESPDRequestSchematronValidator(InputStream is, SchOrigin origin) {

        switch (origin) {
            case EU:
//                return new ESPDSchematronValidator(is,
//                        ValidatorFactory.class
//                                .getResource("/rules/v1/eu/ESPDRequest/sch/02-ESPD-CL-attrb-rules.sch")
//                                .getPath(),
//                        ValidatorFactory.class
//                                .getResource("/rules/v1/eu/ESPDRequest/sch/03-ESPD-ID-attrb-rules.sch")
//                                .getPath(),
//                        ValidatorFactory.class
//                                .getResource("/rules/v1/eu/ESPDRequest/sch/04-ESPD-Common BR-rules.sch")
//                                .getPath());
                return new ESPDSchematronValidator(is, ValidatorFactory.class
                        .getResource("/rules/v1/eu/ESPDRequest/sch/02-ESPD-CL-attrb-rules.sch")
                        .getPath());

//                return new ESPDSchematronValidator(is, ValidatorFactory.class
//                        .getResource("/rules/v1/eu/ESPDRequest/sch/03-ESPD-ID-attrb-rules.sch")
//                        .getPath());

//                return new ESPDSchematronValidator(is, ValidatorFactory.class
//                        .getResource("/rules/v1/eu/ESPDRequest/sch/04-ESPD-Common BR-rules.sch")
//                        .getPath());
            case EHF:
                return new ESPDSchematronValidator(is, ValidatorFactory.class
                        .getResource("/rules/v1/ehf/ESPDRequest/EHF-ESPD-REQUEST.sch")
                        .getPath());
            default:
                throw new UnsupportedOperationException("Unknown schematron origin");
        }

    }

    public static ArtifactValidator createESPDResponseSchematronValidator(InputStream is, SchOrigin origin) {
        throw new UnsupportedOperationException();
    }

}
