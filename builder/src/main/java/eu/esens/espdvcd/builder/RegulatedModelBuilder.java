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
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.EDMVersion;
import eu.esens.espdvcd.schema.SchemaUtil;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import eu.espd.schema.v2.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;
import eu.espd.schema.v2.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class RegulatedModelBuilder implements ModelBuilder {

    private static final Logger LOGGER = Logger.getLogger(RegulatedModelBuilder.class.getName());

    private EODetails eoDetails = null;
    private CADetails caDetails = null;
    private ServiceProviderDetails serviceProviderDetails = null;
    protected CriteriaExtractor criteriaExtractor = null;
    private InputStream importStream = null;

    /* package private constructor. Create only through factory */
    RegulatedModelBuilder() {
    }

    /**
     * Loads from an ESPD Request or an ESPD Response all the required data and
     * are used as the defaults for the creation of the ESPD(Request/Response)
     * POJO.
     *
     * @param is The input stream that will be read to create the Model POJO.
     *           The input stream must point to a valid ESPD Request or ESPD Response XML
     *           Artefact
     * @return the same RegulatedModelBuilder instance for incremental creation of the
     * required object.
     */
    public RegulatedModelBuilder importFrom(InputStream is) {
        importStream = ArtefactUtils.getBufferedInputStream(is);
        return this;
    }

    /**
     * Overrides the CA Details of the created Model POJO with the ones found in
     * the Provided input stream.
     *
     * @param is The input stream that will be read to extract the CA Details
     *           from. The input stream must point to a valid ESPD Request or ESPD
     *           Response XML Artefact
     * @return the same RegulatedModelBuilder instance for incremental creation of the
     * required object.
     * @throws BuilderException if the input stream is on a valid ESPD Request
     *                          or Response;
     */
    public RegulatedModelBuilder withCADetailsFrom(InputStream is) throws BuilderException {
        ESPDRequest request = createESPDRequestFromXML(is);
        caDetails = request.getCADetails();
        return this;
    }

    /**
     * Overrides the CA Details of the created Model POJO with the ones found in
     * the Provided input stream.
     *
     * @param caDetails The {@link CADetails} object hat will override the CA
     *                  Details of the created object
     * @return the same RegulatedModelBuilder instance for incremental creation of the
     * required object.
     */
    public RegulatedModelBuilder withCADetailsFrom(CADetails caDetails) {
        this.caDetails = caDetails;
        return this;
    }

    /**
     * Overrides the EO Details of the created Model POJO with the ones found in
     * the Provided input stream.
     *
     * @param is The input stream that will be read to extract the EO Details
     *           from. The input stream must point to a valid ESPD Request or ESPD
     *           Response XML Artefact
     * @return the same RegulatedModelBuilder instance for incremental creation of the
     * required object.
     * @throws BuilderException if the input stream is on a valid ESPD Request
     *                          or Response;
     */
    public RegulatedModelBuilder withEODetailsFrom(InputStream is) throws BuilderException {
        ESPDResponse res = createESPDResponseFromXML(is);
        eoDetails = res.getEODetails();
        return this;
    }

    /**
     * Overrides the CA Details of the created Model POJO with the ones found in
     * the Provided input stream.
     *
     * @param eoDetails The {@link EODetails} object hat will override the EO
     *                  Details of the created object
     * @return the same RegulatedModelBuilder instance for incremental creation of the
     * required object.
     */
    public RegulatedModelBuilder withEODetailsFrom(EODetails eoDetails) {
        this.eoDetails = eoDetails;
        return this;
    }

    /**
     * Adds the default criteria list of the ESPD Form as it is defined by the
     * European Commission.<br>
     * <p>
     * If the created object is an new (not imported) ESPD Request, then the
     * criteria added are pre-selected.<br>
     * If the created object comes from an imported ESPD Artefact, then the
     * default extra criteria added, are included as non-selected. <br> * If the
     * created object is an ESPD Response, then the criteria added will always
     * be pre-selected.
     *
     * @return the same ModelBuilder instance for incremental creation of the
     * required object.
     */
    public abstract RegulatedModelBuilder addDefaultESPDCriteriaList();

    /**
     * Apply taxonomy cardinalities to REQUIREMENT and REQUIREMENT_GROUP and
     * REQUIREMENT_GROUP type {@link eu.esens.espdvcd.codelist.enums.RequirementGroupTypeEnum}
     *
     * @param criterionList The Criterion List
     */
    protected abstract void applyTaxonomyData(List<SelectableCriterion> criterionList);

    public ESPDRequest createESPDRequest() throws BuilderException {
        ESPDRequest req;
        if (importStream != null) {
            req = createESPDRequestFromXML(importStream);
            if (criteriaExtractor != null) {
                try {
                    req.setCriterionList(criteriaExtractor.getFullList(req.getFullCriterionList()));
                } catch (RetrieverException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            } else {
                // imported XML Criteria have default cardinalities, therefore
                // taxonomy cardinalities will be applied to them in order to
                // comply with designer needs.
                applyTaxonomyData(req.getFullCriterionList());
            }
        } else {
            req = new RegulatedESPDRequest();
            handleNullCriteriaExtractor(req);
            req.setCADetails(createDefaultCADetails());
            req.setServiceProviderDetails(createDefaultServiceProviderDetails());
        }

        //Overriding the default/imported ca details
        if (caDetails != null) {
            req.setCADetails(caDetails);
        }

        // Overriding the default/imported service provider details
        if (serviceProviderDetails != null) {
            req.setServiceProviderDetails(serviceProviderDetails);
        }

        // Apply workaround
        req.getFullCriterionList().forEach(this::applyCriteriaWorkaround);
        return req;
    }

    /**
     * Terminal builder method that returns an {@link ESPDResponse} instance,
     *
     * @return the created ESPD Response
     * @throws BuilderException if the import failed.
     */
    public ESPDResponse createESPDResponse() throws BuilderException {
        ESPDResponse res;
        if (importStream != null) {
            res = createESPDResponseFromXML(importStream);
            if (criteriaExtractor != null) {
                try {
                    res.setCriterionList(criteriaExtractor.getFullList(res.getFullCriterionList(), true));
                } catch (RetrieverException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            } else {
                // imported XML Criteria have default cardinalities, therefore
                // taxonomy cardinalities will be applied to them in order to
                // comply with designer needs.
                applyTaxonomyData(res.getFullCriterionList());
            }

        } else {
            res = new RegulatedESPDResponse();
            handleNullCriteriaExtractor(res);
        }

        if (res.getCADetails() == null) {
            res.setCADetails(createDefaultCADetails());
        }

        if (res.getEODetails() == null) {
            res.setEODetails(createDefaultEODetails());
        }
        if (res.getServiceProviderDetails() == null) {
            res.setServiceProviderDetails(createDefaultServiceProviderDetails());
        }

        if (caDetails != null) {
            res.setCADetails(caDetails);
        }

        if (eoDetails != null) {
            res.setEODetails(eoDetails);
        }

        if (serviceProviderDetails != null) {
            res.setServiceProviderDetails(serviceProviderDetails);
        }

        // Apply workaround
        res.getFullCriterionList().forEach(this::applyCriteriaWorkaround);
        return res;
    }

    /**
     * Parses the input stream and creates an ESPDRequest model instance.
     *
     * @param xmlESPD The input stream of the XML document to be parsed
     * @return a prefilled ESPDRequest based on the input data
     * @throws BuilderException when the parsing from XML to ESPDRequest Model fails
     */
    private ESPDRequest createESPDRequestFromXML(InputStream xmlESPD) throws BuilderException {

        ESPDRequest req;

        try (InputStream bis = ArtefactUtils.getBufferedInputStream(xmlESPD)) {
            // Check and read the file in the JAXB Object
            // but first identify the artefact edm version
            switch (ArtefactUtils.findEDMVersion(xmlESPD)) {
                case V1:
                    LOGGER.log(Level.INFO, "v1 artefact has been imported...");
                    ESPDRequestType espdRequestType = readESPDRequestFromStream(bis);
                    req = ModelFactory.ESPD_REQUEST.extractESPDRequest(espdRequestType); // Create the Model Object
                    break;
                case V2:
                    LOGGER.log(Level.INFO, "v2 artefact has been imported...");
                    QualificationApplicationRequestType qualificationApplicationRequestType = readQualificationApplicationRequestFromStream(bis);
                    req = ModelFactory.ESPD_REQUEST.extractESPDRequest(qualificationApplicationRequestType); // Create the Model Object
                    break;
                default:
                    throw new BuilderException("Error... Imported artefact could not be identified as either v1 or v2.");
            }

        } catch (IOException | JAXBException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading XML Input Stream", ex);
        }

        return req;
    }

    /**
     * Parses the input stream and creates an ESPDResponse model instance.
     *
     * @param xmlESPDRes The input stream of the XML document to be parsed
     * @return a prefilled ESPDRequest based on the input data
     * @throws BuilderException when the parsing from XML to ESPDResponse Model
     *                          fails
     */
    protected ESPDResponse createESPDResponseFromXML(InputStream xmlESPDRes) throws BuilderException {

        ESPDResponse res;

        try (InputStream bis = ArtefactUtils.getBufferedInputStream(xmlESPDRes)) {
            // Check and read the file in the JAXB Object
            // but first identify the artefact edm version
            switch (ArtefactUtils.findEDMVersion(xmlESPDRes)) {
                case V1:
                    LOGGER.log(Level.INFO, "v1 artefact has been imported...");
                    // Check and read the file in the JAXB Object
                    ESPDResponseType espdResponseType = readESPDResponseFromStream(bis);
                    // Create the Model Object
                    res = ModelFactory.ESPD_RESPONSE.extractESPDResponse(espdResponseType);
                    break;
                case V2:
                    LOGGER.log(Level.INFO, "v2 artefact has been imported...");
                    // Check and read the file in the JAXB Object
                    QualificationApplicationResponseType qualificationApplicationResponseType = readQualificationApplicationResponseFromStream(bis);
                    // Create the Model Object
                    res = ModelFactory.ESPD_RESPONSE.extractESPDResponse(qualificationApplicationResponseType);
                    break;
                default:
                    throw new BuilderException("Error... Imported artefact could not be identified as either v1 or v2.");
            }

        } catch (IOException | JAXBException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading Input Stream for ESPD Response", ex);
        }

        return res;
    }

    private QualificationApplicationRequestType readQualificationApplicationRequestFromStream(InputStream is) throws JAXBException {
        // Start with the convenience methods provided by JAXB. If there are
        // performance issues we will switch back to the JAXB API Usage
        return SchemaUtil.getUnmarshaller(EDMVersion.V2).unmarshal(new StreamSource(is), QualificationApplicationRequestType.class).getValue();
    }

    protected QualificationApplicationResponseType readQualificationApplicationResponseFromStream(InputStream is) throws JAXBException {
        // Start with the convenience methods provided by JAXB. If there are
        // performance issues we will switch back to the JAXB API Usage
        return SchemaUtil.getUnmarshaller(EDMVersion.V2).unmarshal(new StreamSource(is), QualificationApplicationResponseType.class).getValue();
    }

    private ESPDRequestType readESPDRequestFromStream(InputStream is) throws JAXBException {
        // Start with the convenience methods provided by JAXB. If there are
        // performance issues we will switch back to the JAXB API Usage
        return SchemaUtil.getUnmarshaller(EDMVersion.V1).unmarshal(new StreamSource(is), ESPDRequestType.class).getValue();
    }

    protected ESPDResponseType readESPDResponseFromStream(InputStream is) throws JAXBException {
        // Start with the convenience methods provided by JAXB. If there are
        // performance issues we will switch back to the JAXB API Usage
        return SchemaUtil.getUnmarshaller(EDMVersion.V1).unmarshal(new StreamSource(is), ESPDResponseType.class).getValue();
    }

    private void handleNullCriteriaExtractor(ESPDRequest req) {
        if (criteriaExtractor != null) {
            try {
                req.setCriterionList(criteriaExtractor.getFullList());
            } catch (RetrieverException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        } else {
            req.setCriterionList(getEmptyCriteriaList());
        }
    }

}
