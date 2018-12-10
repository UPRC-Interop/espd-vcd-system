/**
 * Copyright 2016-2018 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.validator;

import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.codelist.enums.internal.ArtefactType;
import eu.esens.espdvcd.schema.XSD;
import eu.esens.espdvcd.schema.enums.EDMSubVersion;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import eu.esens.espdvcd.schematron.SchematronV102;
import eu.esens.espdvcd.schematron.SchematronV202;
import eu.esens.espdvcd.schematron.SchematronV210;
import eu.esens.espdvcd.validator.schema.ESPDSchemaValidator;
import eu.esens.espdvcd.validator.schematron.ClasspathURIResolver;
import eu.esens.espdvcd.validator.schematron.ESPDSchematronValidator;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import org.xml.sax.SAXException;

import javax.validation.constraints.NotNull;
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
    public static ArtefactValidator createESPDRequestSchemaValidator(File espdRequest,
                                                                     @NotNull EDMSubVersion version)
            throws SAXException, JAXBException {

        ArtefactValidator v = null;

        try (InputStream is = new FileInputStream(espdRequest)) {

            LOGGER.log(Level.INFO, "Creating ESPD request " + version.getTag()
                    + " edm validator for: " + espdRequest.getName());

            switch (version) {

                case V102:

                    v = new ESPDSchemaValidator(is, "/" + XSD.ESPD_REQUEST_V102.xsdPath(),
                            ESPDRequestType.class, version);
                    break;

                case V202:

                    v = new ESPDSchemaValidator(is, "/" + XSD.ESPD_REQUEST_V202.xsdPath(),
                            eu.espd.schema.v2.v202.pre_award.qualificationapplicationrequest
                                    .QualificationApplicationRequestType.class, version);
                    break;

                case V210:

                    v = new ESPDSchemaValidator(is, "/" + XSD.ESPD_REQUEST_V210.xsdPath(),
                            eu.espd.schema.v2.v210.qualificationapplicationrequest
                                    .QualificationApplicationRequestType.class, version);
                    break;

                default:

                    throw new IllegalStateException("Error... Unknown Exchange Data Model (EDM) version");

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
    public static ArtefactValidator createESPDResponseSchemaValidator(File espdResponse,
                                                                      @NotNull EDMSubVersion version)
            throws SAXException, JAXBException {

        ArtefactValidator v = null;

        try (InputStream is = new FileInputStream(espdResponse)) {

            LOGGER.log(Level.INFO, "Creating ESPD request " + version.getTag()
                    + " edm validator for: " + espdResponse.getName());

            switch (version) {

                case V102:
                    v = new ESPDSchemaValidator(is, "/" + XSD.ESPD_RESPONSE_V102.xsdPath(),
                            ESPDResponseType.class, version);
                    break;

                case V202:
                    v = new ESPDSchemaValidator(is, "/" + XSD.ESPD_RESPONSE_V202.xsdPath(),
                            eu.espd.schema.v2.v202.pre_award.qualificationapplicationresponse
                                    .QualificationApplicationResponseType.class, version);
                    break;

                case V210:
                    v = new ESPDSchemaValidator(is, "/" + XSD.ESPD_RESPONSE_V210.xsdPath(),
                            eu.espd.schema.v2.v210.qualificationapplicationresponse
                                    .QualificationApplicationResponseType.class, version);
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
        EDMSubVersion version = ArtefactUtils.findEDMSubVersion(espdArtefact);

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
    public static ArtefactValidator createESPDRequestSchematronValidator(File espdRequest,
                                                                         @NotNull EDMSubVersion version) {

        LOGGER.log(Level.INFO, "Creating ESPD request " + version.getTag()
                + " schematron validator for: " + espdRequest.getName());

        switch (version) {

            case V102:

                return new ESPDSchematronValidator.Builder(espdRequest)
                        .addSchematron("/" + SchematronV102.ESPDReqCLAttributeRules.xslt())
                        .addSchematron("/" + SchematronV102.ESPDReqIDAttributeRules.xslt())
                        .addSchematron("/" + SchematronV102.ESPDReqCommonBRRules.xslt())
                        .build();

            case V202:

                return new ESPDSchematronValidator.Builder(espdRequest, createTaxonomyURIResolver())
                        // common 2.0.2
                        .addSchematron("/" + SchematronV202.ESPDCodelistsValues.xslt())
                        .addSchematron("/" + SchematronV202.ESPDCommonCLAttributes.xslt())
                        .addSchematron("/" + SchematronV202.ESPDCommonCriterionBR.xslt())
                        .addSchematron("/" + SchematronV202.ESPDCommonOtherBR.xslt())
                        // espd request 2.0.2
                        .addSchematron("/" + SchematronV202.ESPDReqCardinality.xslt())
                        .addSchematron("/" + SchematronV202.ESPDReqCriterionBR.xslt())
                        .addSchematron("/" + SchematronV202.ESPDReqOtherBR.xslt())
                        .addSchematron("/" + SchematronV202.ESPDReqProcurerBR.xslt())
                        .addSchematron("/" + SchematronV202.ESPDReqSelfContained.xslt())
                        .build();

            case V210:

                return new ESPDSchematronValidator.Builder(espdRequest, createTaxonomyURIResolver())
                        // common 2.1.0
                        .addSchematron("/" + SchematronV210.ESPDCodelistsValues.xslt())
                        .addSchematron("/" + SchematronV210.ESPDCommonCLAttributes.xslt())
                        .addSchematron("/" + SchematronV210.ESPDCommonCLValuesRestrictions.xslt())
                        .addSchematron("/" + SchematronV210.ESPDCommonCriterionBR.xslt())
                        .addSchematron("/" + SchematronV210.ESPDCommonOtherBR.xslt())
                        // espd request 2.1.0
                        .addSchematron("/" + SchematronV210.ESPDReqCardinality.xslt())
                        .addSchematron("/" + SchematronV210.ESPDReqCriterionBR.xslt())
                        .addSchematron("/" + SchematronV210.ESPDReqOtherBR.xslt())
                        .addSchematron("/" + SchematronV210.ESPDReqProcurerBR.xslt())
                        .addSchematron("/" + SchematronV210.ESPDReqSelfContained.xslt())
                        .build();

            default:

                throw new IllegalStateException("Error... Unknown Exchange Data Model (EDM) version");

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
    public static ArtefactValidator createESPDResponseSchematronValidator(File espdResponse,
                                                                          @NotNull EDMSubVersion version) {

        LOGGER.log(Level.INFO, "Creating ESPD response " + version.getTag()
                + " schematron validator for: " + espdResponse.getName());

        switch (version) {

            case V102:
                return new ESPDSchematronValidator.Builder(espdResponse)
                        .addSchematron("/" + SchematronV102.ESPDRespCLAttributeRules.xslt())
                        .addSchematron("/" + SchematronV102.ESPDRespIDAttributeRules.xslt())
                        .addSchematron("/" + SchematronV102.ESPDRespCommonBRRules.xslt())
                        .addSchematron("/" + SchematronV102.ESPDRespSpecBRRules.xslt())
                        .build();

            case V202:
                return new ESPDSchematronValidator.Builder(espdResponse, createTaxonomyURIResolver())
                        // common 2.0.2
                        .addSchematron("/" + SchematronV202.ESPDCodelistsValues.xslt())
                        .addSchematron("/" + SchematronV202.ESPDCommonCLAttributes.xslt())
                        .addSchematron("/" + SchematronV202.ESPDCommonCriterionBR.xslt())
                        .addSchematron("/" + SchematronV202.ESPDCommonOtherBR.xslt())
                        // espd response 2.0.2
                        .addSchematron("/" + SchematronV202.ESPDRespCardinalityBR.xslt())
                        .addSchematron("/" + SchematronV202.ESPDRespCriterionBR.xslt())
                        .addSchematron("/" + SchematronV202.ESPDRespOtherBR.xslt())
                        .addSchematron("/" + SchematronV202.ESPDRespEOBR.xslt())
                        .addSchematron("/" + SchematronV202.ESPDRespQualificationBR.xslt())
                        .addSchematron("/" + SchematronV202.ESPDRespRoleBR.xslt())
                        .addSchematron("/" + SchematronV202.ESPDRespSelfContainedBR.xslt())
                        .build();

            case V210:
                return new ESPDSchematronValidator.Builder(espdResponse, createTaxonomyURIResolver())
                        // common 2.1.0
                        .addSchematron("/" + SchematronV210.ESPDCodelistsValues.xslt())
                        .addSchematron("/" + SchematronV210.ESPDCommonCLAttributes.xslt())
                        .addSchematron("/" + SchematronV210.ESPDCommonCLValuesRestrictions.xslt())
                        .addSchematron("/" + SchematronV210.ESPDCommonCriterionBR.xslt())
                        .addSchematron("/" + SchematronV210.ESPDCommonOtherBR.xslt())
                        // espd response 2.1.0
                        .addSchematron("/" + SchematronV210.ESPDRespCardinalityBR.xslt())
                        .addSchematron("/" + SchematronV210.ESPDRespCriterionBR.xslt())
                        .addSchematron("/" + SchematronV210.ESPDRespOtherBR.xslt())
                        .addSchematron("/" + SchematronV210.ESPDRespEOBR.xslt())
                        .addSchematron("/" + SchematronV210.ESPDRespQualificationBR.xslt())
                        .addSchematron("/" + SchematronV210.ESPDRespRoleBR.xslt())
                        .addSchematron("/" + SchematronV210.ESPDRespSelfContainedBR.xslt())
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
        // EDMVersion version = ArtefactUtils.findEDMVersion(espdArtefact);
        EDMSubVersion version = ArtefactUtils.findEDMSubVersion(espdArtefact);

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

    private static ClasspathURIResolver createTaxonomyURIResolver() {
        ClasspathURIResolver resolver = new ClasspathURIResolver();
        // 2.0.2 taxonomy
        resolver.addResource("schematron/v2/2.0.2/ESPDRequest/xsl/ESPD-CriteriaTaxonomy-REGULATED.V2.0.2.xml");
        resolver.addResource("schematron/v2/2.0.2/ESPDRequest/xsl/ESPD-CriteriaTaxonomy-SELFCONTAINED.V2.0.2.xml");
        // 2.1.0 taxonomy
        resolver.addResource("schematron/v2/2.1.0/ESPDRequest/xsl/ESPD-CriteriaTaxonomy-REGULATED.V2.1.0.xml");
        resolver.addResource("schematron/v2/2.1.0/ESPDRequest/xsl/ESPD-CriteriaTaxonomy-SELFCONTAINED.V2.1.0.xml");
        // Codelists 2.1.0
        resolver.addResource("gc/v2/ResponseDataType-CodeList.gc");
        resolver.addResource("gc/v2/WeightingType-CodeList.gc");
        resolver.addResource("gc/v2/EvaluationMethodType-CodeList.gc");
        resolver.addResource("gc/v2/ESPD-CriteriaTaxonomy_V2.1.0.gc");
        resolver.addResource("gc/v2/EORoleType-CodeList.gc");
        return resolver;
    }

}
