/**
 * Copyright 2016-2020 University of Piraeus Research Center
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
package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceConfig;
import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceType;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.CriterionType;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v2.v210.commonaggregate.TenderingCriterionType;
import eu.espd.schema.v2.v210.qualificationapplicationrequest.QualificationApplicationRequestType;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXB;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Konstantinos Raptis
 */
public class ESPDArtefactResource implements CriteriaResource, LegislationResource, RequirementsResource {

    private static final Logger LOGGER = Logger.getLogger(ESPDArtefactResource.class.getName());

    private Map<String, SelectableCriterion> criterionMap;

    public ESPDArtefactResource(@NotNull EDMVersion version) {
        criterionMap = new HashMap<>();

        switch (version) {
            case V1:
                ESPDRequestType requestV1Template = JAXB.unmarshal(CriteriaExtractor.class.getResourceAsStream(ResourceConfig.INSTANCE.getESPDArtefactRegulatedV102()), ESPDRequestType.class);
                requestV1Template.getCriterion()
                        .forEach(this::addToCriterionMap);
                break;
            case V2:
                QualificationApplicationRequestType requestV2Template = JAXB.unmarshal(CriteriaExtractor.class.getResourceAsStream(ResourceConfig.INSTANCE.getESPDArtefactRegulatedV210()), QualificationApplicationRequestType.class);
                requestV2Template.getTenderingCriterion()
                        .forEach(this::addToCriterionMap);
                break;
            default:
                throw new IllegalArgumentException("Error... Invalid edm version value.");
        }

    }

    private void addToCriterionMap(CriterionType ct) {

        if (ct.getID() != null) {
            criterionMap.put(ct.getID().getValue(), ModelFactory.ESPD_REQUEST.extractSelectableCriterion(ct));
        }
    }

    private void addToCriterionMap(TenderingCriterionType tct) {

        if (tct.getID() != null) {
            criterionMap.put(tct.getID().getValue(), ModelFactory.ESPD_REQUEST.extractSelectableCriterion(tct));
        }
    }

    @Override
    public List<SelectableCriterion> getCriterionList() {
        return getCriterionList(EULanguageCodeEnum.EN);
    }

    @Override
    public List<SelectableCriterion> getCriterionList(EULanguageCodeEnum lang) {

        // failback check
        if (lang != EULanguageCodeEnum.EN) {
            LOGGER.log(Level.WARNING, "Warning... European Criteria Multilinguality not supported yet. Language set back to English");
        }

        return new ArrayList<>(criterionMap.values());
    }

    @Override
    public Map<String, SelectableCriterion> getCriterionMap() {
        return getCriterionMap(EULanguageCodeEnum.EN);
    }

    @Override
    public Map<String, SelectableCriterion> getCriterionMap(EULanguageCodeEnum lang) {

        // failback check
        if (lang != EULanguageCodeEnum.EN) {
            LOGGER.log(Level.WARNING, "Warning... European Criteria Multilinguality not supported yet. Language set back to English");
        }

        return criterionMap;
    }

    @Override
    public LegislationReference getLegislationForCriterion(String ID) {
        return getLegislationForCriterion(ID, EULanguageCodeEnum.EN);
    }

    @Override
    public LegislationReference getLegislationForCriterion(String ID, EULanguageCodeEnum lang) {

        // failback check
        if (lang != EULanguageCodeEnum.EN) {
            LOGGER.log(Level.WARNING, "Warning... European Criteria Multilinguality not supported yet. Language set back to English");
        }

        return criterionMap.containsKey(ID)
                ? criterionMap.get(ID).getLegislationReference()
                : null;
    }

    @Override
    public List<RequirementGroup> getRequirementsForCriterion(String ID) {
        return getRequirementsForCriterion(ID, EULanguageCodeEnum.EN);
    }

    @Override
    public List<RequirementGroup> getRequirementsForCriterion(String ID, EULanguageCodeEnum lang) {

        // failback check
        if (lang != EULanguageCodeEnum.EN) {
            LOGGER.log(Level.WARNING, "Warning... European Criteria Multilinguality not supported yet. Language set back to English");
        }

        return criterionMap.containsKey(ID)
                ? criterionMap.get(ID).getRequirementGroups()
                : null;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.ARTEFACT;
    }

}

