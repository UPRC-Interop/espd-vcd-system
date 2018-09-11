package eu.esens.espdvcd.validator;

import eu.esens.espdvcd.builder.enums.ArtefactType;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.schema.EDMVersion;
import eu.esens.espdvcd.schema.XSD;
import eu.esens.espdvcd.schematron.SchematronV1;
import eu.esens.espdvcd.schematron.SchematronV2;
import eu.esens.espdvcd.validator.schema.ESPDSchemaValidator;
import eu.esens.espdvcd.validator.schematron.ESPDSchematronValidator;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import eu.espd.schema.v2.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;
import eu.espd.schema.v2.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ValidatorFactory is a Factory class that provides static methods for
 * creating different kinds of validator:<br>
 * - ESPD request edm validator<br>
 * - ESPD response edm validator<br>
 * - ESPD request schematron validator<br>
 * - ESPD response schematron validator<br>
 * <p>
 * Created by Ulf Lotzmann on 11/05/2016.
 */
public class ValidatorFactory {

    private static final Logger LOGGER = Logger.getLogger(ValidatorFactory.class.getName());

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
                        .addSchematron("/" + SchematronV1.ESPDReqCLAttributeRules.xslt())
                        .addSchematron("/" + SchematronV1.ESPDReqIDAttributeRules.xslt())
                        .addSchematron("/" + SchematronV1.ESPDReqCommonBRRules.xslt())
                        .build();

            case V2:
                LOGGER.log(Level.INFO, "Creating ESPD request V2 schematron validator for: " + espdRequest.getName());
                return new ESPDSchematronValidator.Builder(espdRequest)
                        // common
                        .addSchematron("/" + SchematronV2.ESPDCodelistsValues.xslt())
                        .addSchematron("/" + SchematronV2.ESPDCommonCLAttributes.xslt())
                        .addSchematron("/" + SchematronV2.ESPDCommonCriterionBR.xslt())
                        .addSchematron("/" + SchematronV2.ESPDCommonOtherBR.xslt())
                        // espd request 2.0.2
                        .addSchematron("/" + SchematronV2.ESPDReqCardinality.xslt())
                        .addSchematron("/" + SchematronV2.ESPDReqCriterionBR.xslt())
                        .addSchematron("/" + SchematronV2.ESPDReqOtherBR.xslt())
                        .addSchematron("/" + SchematronV2.ESPDReqProcurerBR.xslt())
                        .addSchematron("/" + SchematronV2.ESPDReqSelfContained.xslt())
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
                        .addSchematron("/" + SchematronV1.ESPDRespCLAttributeRules.xslt())
                        .addSchematron("/" + SchematronV1.ESPDRespIDAttributeRules.xslt())
                        .addSchematron("/" + SchematronV1.ESPDRespCommonBRRules.xslt())
                        .addSchematron("/" + SchematronV1.ESPDRespSpecBRRules.xslt())
                        .build();

            case V2:
                LOGGER.log(Level.INFO, "Creating ESPD response V2 schematron validator for: " + espdResponse.getName());
                return new ESPDSchematronValidator.Builder(espdResponse)
                        // common
                        .addSchematron("/" + SchematronV2.ESPDCodelistsValues.xslt())
                        .addSchematron("/" + SchematronV2.ESPDCommonCLAttributes.xslt())
                        .addSchematron("/" + SchematronV2.ESPDCommonCriterionBR.xslt())
                        .addSchematron("/" + SchematronV2.ESPDCommonOtherBR.xslt())
                        // espd response 2.0.2
                        .addSchematron("/" + SchematronV2.ESPDRespCardinalityBR.xslt())
                        .addSchematron("/" + SchematronV2.ESPDRespCriterionBR.xslt())
                        .addSchematron("/" + SchematronV2.ESPDRespOtherBR.xslt())
                        .addSchematron("/" + SchematronV2.ESPDRespEOBR.xslt())
                        .addSchematron("/" + SchematronV2.ESPDRespQualificationBR.xslt())
                        .addSchematron("/" + SchematronV2.ESPDRespRoleBR.xslt())
                        .addSchematron("/" + SchematronV2.ESPDRespSelfContainedBR.xslt())
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
