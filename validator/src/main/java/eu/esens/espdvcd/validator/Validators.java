package eu.esens.espdvcd.validator;

import eu.esens.espdvcd.builder.enums.ArtefactType;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.schema.EDMVersion;
import eu.esens.espdvcd.schema.XSD;
import eu.esens.espdvcd.validator.schema.ESPDSchemaValidator;
import eu.esens.espdvcd.validator.schematron.ESPDSchematronValidator;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import eu.espd.schema.v2.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;
import eu.espd.schema.v2.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Validators is a Factory class that provides static methods for
 * creating different kinds of validator:<br>
 * - ESPD request edm validator<br>
 * - ESPD response edm validator<br>
 * - ESPD request schematron validator<br>
 * - ESPD response schematron validator<br>
 * <p>
 * Created by Ulf Lotzmann on 11/05/2016.
 */
public class Validators {

    private static final Logger LOGGER = Logger.getLogger(Validators.class.getName());

    /**
     * Factory method that creates an ESPD request edm validator object and
     * performs the edm validation for the XML provided by the specified file.
     *
     * @param espdRequest ESPD request file with XML data
     * @return edm validator object
     */
    public static ArtefactValidator createESPDRequestSchemaValidator(File espdRequest, EDMVersion version) throws SAXException, JAXBException {

        ArtefactValidator v = null;

        try (InputStream is = new FileInputStream(espdRequest)) {

            switch (version) {

                case V1:
                    LOGGER.log(Level.INFO, "Creating ESPD request V1 edm validator for: " + espdRequest.getName());
                    v = new ESPDSchemaValidator(is, "/" + XSD.ESPD_REQUEST.xsdPath(), ESPDRequestType.class, version);
                    break;

                case V2:
                    LOGGER.log(Level.INFO, "Creating ESPD request V2 edm validator for: " + espdRequest.getName());
                    v = new ESPDSchemaValidator(is, "/" + XSD.ESPD_REQUEST_V2.xsdPath(), QualificationApplicationRequestType.class, version);
                    break;

                default:
                    LOGGER.log(Level.SEVERE, "Error... Unknown Exchange Data Model (EDM) version");

            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        return v;
    }

    /**
     * Factory method that creates an ESPD response edm validator object and
     * performs the edm validation for the XML provided by the specified file.
     *
     * @param espdResponse ESPD response file with XML data
     * @return edm validator object
     */
    public static ArtefactValidator createESPDResponseSchemaValidator(File espdResponse, EDMVersion version) throws SAXException, JAXBException {

        ArtefactValidator v = null;

        try (InputStream is = new FileInputStream(espdResponse)) {

            switch (version) {

                case V1:
                    LOGGER.log(Level.INFO, "Creating ESPD request V1 edm validator for: " + espdResponse.getName());
                    v = new ESPDSchemaValidator(is, "/" + XSD.ESPD_RESPONSE.xsdPath(), ESPDResponseType.class, version);
                    break;

                case V2:
                    LOGGER.log(Level.INFO, "Creating ESPD request V2 edm validator for: " + espdResponse.getName());
                    v = new ESPDSchemaValidator(is, "/" + XSD.ESPD_RESPONSE_V2.xsdPath(), QualificationApplicationResponseType.class, version);
                    break;

                default:
                    LOGGER.log(Level.SEVERE, "Error... Unknown Exchange Data Model (EDM) version");

            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        return v;
    }

    public static ArtefactValidator createESPDSchemaValidator(File espdArtefact) throws SAXException, JAXBException {
        ArtefactType type = ArtefactUtils.findArtefactType(espdArtefact);
        EDMVersion version = ArtefactUtils.findEDMVersion(espdArtefact);

        switch (type) {

            case ESPD_REQUEST:
                return createESPDRequestSchemaValidator(espdArtefact, version);

            case ESPD_RESPONSE:
                return createESPDResponseSchemaValidator(espdArtefact, version);

            default:
                LOGGER.log(Level.SEVERE, "Error... Unknown artefact type (neither request nor response) for: " + espdArtefact.getName());
                return null;

        }
    }

//    /**
//     * Factory method that creates an ESPD artefact (request or response) schematron
//     * validator object and performs the schematron validation for the XML provided by
//     * the specified input stream.
//     *
//     * @param is input stream with XML data
//     * @return schematron validator object
//     */
//    static ArtefactValidator createESPDArtefactSchematronValidator(InputStream is, String schPath) {
//        return new ESPDSchematronValidator.Builder(is)
//                .addSchematron(schPath)
//                .build();
//    }

//    /**
//     * Factory method that creates an ESPD request schematron validator object
//     * and performs the schematron validation for the XML provided by the
//     * specified file.
//     *
//     * @param espdRequest file with XML data
//     * @param schOrigin   origin of schematron file
//     * @return schematron validator object
//     */
//    public static ArtefactValidator createESPDRequestSchematronValidator(InputStream espdRequest, SchematronOrigin schOrigin) {
//
//        switch (schOrigin) {
//            case EU:
//                return new ESPDSchematronValidator.Builder(espdRequest)
//                        .addSchematron("/rules/v1/eu/ESPDRequest/sch/02-ESPD-CL-attrb-rules.sch")
//                        .addSchematron("/rules/v1/eu/ESPDRequest/sch/03-ESPD-ID-attrb-rules.sch")
//                        .addSchematron("/rules/v1/eu/ESPDRequest/sch/04-ESPD-Common-BR-rules.sch")
//                        .build();
//            case EHF:
//                return new ESPDSchematronValidator.Builder(espdRequest)
//                        .addSchematron("/rules/v1/ehf/ESPDRequest/EHF-ESPD-REQUEST.sch")
//                        .build();
//            default:
//                throw new IllegalArgumentException(ERROR_UNKNOWN_SCHEMATRON_ORIGIN);
//        }
//    }

//    /**
//     * Factory method that creates an ESPD response schematron validator object
//     * and performs the schematron validation for the XML provided by the
//     * specified file.
//     *
//     * @param espdResponse file with XML data
//     * @param schOrigin    origin of schematron file
//     * @return schematron validator object
//     */
//    public static ArtefactValidator createESPDResponseSchematronValidator(InputStream espdResponse, SchematronOrigin schOrigin) {
//
//        switch (schOrigin) {
//            case EU:
//                return new ESPDSchematronValidator.Builder(espdResponse)
//                        .addSchematron("/rules/v1/eu/ESPDResponse/sch/02-ESPD-CL-attrb-rules.sch")
//                        .addSchematron("/rules/v1/eu/ESPDResponse/sch/03-ESPD-ID-attrb-rules.sch")
//                        .addSchematron("/rules/v1/eu/ESPDResponse/sch/04-ESPD-Common-BR-rules.sch")
//                        .addSchematron("/rules/v1/eu/ESPDResponse/sch/05-ESPD-Spec-BR-rules.sch")
//                        .build();
//            case EHF:
//                return new ESPDSchematronValidator.Builder(espdResponse)
//                        .addSchematron("/rules/v1/ehf/ESPDResponse/EHF-ESPD-RESPONSE.sch")
//                        .build();
//            default:
//                throw new IllegalArgumentException(ERROR_UNKNOWN_SCHEMATRON_ORIGIN);
//        }
//    }

    /**
     * Factory method that creates an ESPD artefact request schematron
     * validator object for the XML provided by the specified file
     * according to the {@link EDMVersion}.
     *
     * @param espdRequest ESPD request file with XML data
     * @return schematron validator object
     */
    public static ArtefactValidator createESPDRequestSchematronValidator(File espdRequest, EDMVersion version) {

        switch (version) {

            case V1:
                LOGGER.log(Level.INFO, "Creating ESPD request V1 schematron validator for: " + espdRequest.getName());
                return new ESPDSchematronValidator.Builder(espdRequest)
                        .addSchematron("/rules/v1/eu/ESPDRequest/sch/02-ESPD-CL-attrb-rules.sch")
                        .addSchematron("/rules/v1/eu/ESPDRequest/sch/03-ESPD-ID-attrb-rules.sch")
                        .addSchematron("/rules/v1/eu/ESPDRequest/sch/04-ESPD-Common-BR-rules.sch")
                        .build();

            case V2:
                LOGGER.log(Level.INFO, "Creating ESPD request V2 schematron validator for: " + espdRequest.getName());
                return new ESPDSchematronValidator.Builder(espdRequest)
                        // common
                        .addSchematron("/rules/v2/eu/common/sch/01-ESPD-codelist-values.sch")
                        .addSchematron("/rules/v2/eu/common/sch/01-ESPD-Common-CL-Attributes.sch")
                        .addSchematron("/rules/v2/eu/common/sch/03-ESPD-Common-Criterion-BR.sch")
                        .addSchematron("/rules/v2/eu/common/sch/04-ESPD-Common-Other-BR.sch")
                        // espd request 2.0.2
                        .addSchematron("/rules/v2/eu/ESPDRequest-2.0.2/sch/02-ESPD-Req-Cardinality-BR.sch")
                        .addSchematron("/rules/v2/eu/ESPDRequest-2.0.2/sch/03-ESPD-Req-Criterion-BR.sch")
                        .addSchematron("/rules/v2/eu/ESPDRequest-2.0.2/sch/04-ESPD-Req-Other-BR.sch")
                        .addSchematron("/rules/v2/eu/ESPDRequest-2.0.2/sch/05-ESPD-Req-Procurer-BR.sch")
                        .addSchematron("/rules/v2/eu/ESPDRequest-2.0.2/sch/05-ESPD-Req-Self-contained-BR.sch")
                        .build();

            default:
                LOGGER.log(Level.SEVERE, "Error... Unknown Exchange Data Model (EDM) version");
                return null;
        }

    }

    /**
     * Factory method that creates an ESPD artefact response schematron
     * validator object for the XML provided by the specified file
     * according to the {@link EDMVersion}.
     *
     * @param espdResponse ESPD response file with XML data
     * @return schematron validator object
     */
    public static ArtefactValidator createESPDResponseSchematronValidator(File espdResponse, EDMVersion version) {

        switch (version) {

            case V1:
                LOGGER.log(Level.INFO, "Creating ESPD response V1 schematron validator for: " + espdResponse.getName());
                return new ESPDSchematronValidator.Builder(espdResponse)
                        .addSchematron("/rules/v1/eu/ESPDResponse/sch/02-ESPD-CL-attrb-rules.sch")
                        .addSchematron("/rules/v1/eu/ESPDResponse/sch/03-ESPD-ID-attrb-rules.sch")
                        .addSchematron("/rules/v1/eu/ESPDResponse/sch/04-ESPD-Common-BR-rules.sch")
                        .addSchematron("/rules/v1/eu/ESPDResponse/sch/05-ESPD-Spec-BR-rules.sch")
                        .build();

            case V2:
                LOGGER.log(Level.INFO, "Creating ESPD response V2 schematron validator for: " + espdResponse.getName());
                return new ESPDSchematronValidator.Builder(espdResponse)
                        // common
                        .addSchematron("/rules/v2/eu/common/sch/01-ESPD-codelist-values.sch")
                        .addSchematron("/rules/v2/eu/common/sch/01-ESPD-Common-CL-Attributes.sch")
                        .addSchematron("/rules/v2/eu/common/sch/03-ESPD-Common-Criterion-BR.sch")
                        .addSchematron("/rules/v2/eu/common/sch/04-ESPD-Common-Other-BR.sch")
                        // espd response 2.0.2
                        .addSchematron("/rules/v2/eu/ESPDResponse-2.0.2/sch/02-ESPD-Resp-Cardinality-BR.sch")
                        .addSchematron("/rules/v2/eu/ESPDResponse-2.0.2/sch/03-ESPD-Resp-Criterion-BR.sch")
                        .addSchematron("/rules/v2/eu/ESPDResponse-2.0.2/sch/04-ESPD-Resp-Other-BR.sch")
                        .addSchematron("/rules/v2/eu/ESPDResponse-2.0.2/sch/05-ESPD-Resp-EO-BR.sch")
                        .addSchematron("/rules/v2/eu/ESPDResponse-2.0.2/sch/05-ESPD-Resp-Qualification-BR.sch")
                        .addSchematron("/rules/v2/eu/ESPDResponse-2.0.2/sch/05-ESPD-Resp-Role-BR.sch")
                        .addSchematron("/rules/v2/eu/ESPDResponse-2.0.2/sch/05-ESPD-Resp-Self-contained-BR.sch")
                        .build();

            default:
                LOGGER.log(Level.SEVERE, "Error... Unknown Exchange Data Model (EDM) version");
                return null;
        }

    }

    /**
     * Factory method that creates an ESPD artefact (request or response) schematron
     * validator object for the XML provided by the specified file
     * according to the {@link EDMVersion}.
     *
     * @param espdArtefact ESPD artefact (request or response) file with XML data
     * @return schematron validator object
     */
    public static ArtefactValidator createESPDSchematronValidator(File espdArtefact) {

        ArtefactType type = ArtefactUtils.findArtefactType(espdArtefact);
        EDMVersion version = ArtefactUtils.findEDMVersion(espdArtefact);

        switch (type) {

            case ESPD_REQUEST:
                return createESPDRequestSchematronValidator(espdArtefact, version);

            case ESPD_RESPONSE:
                return createESPDResponseSchematronValidator(espdArtefact, version);

            default:
                LOGGER.log(Level.SEVERE, "Error... Unknown artefact type (neither request nor response) for: " + espdArtefact.getName());
                return null;

        }
    }

}
