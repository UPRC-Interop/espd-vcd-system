package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceType;
import eu.esens.espdvcd.schema.EDMVersion;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.CriterionType;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionType;
import eu.espd.schema.v2.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXB;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Konstantinos Raptis
 */
public class ESPDArtefactResource implements CriteriaResource, LegislationResource, RequirementsResource {

    private static final Logger LOGGER = Logger.getLogger(ESPDArtefactResource.class.getName());

    private static final String ESPD_REQUEST_V1_REGULATED_RESOURCE = "/templates/v1_regulated/espd-request-2018.03.xml";
    private static final String ESPD_REQUEST_V2_REGULATED_RESOURCE = "/templates/v2_regulated/espd-request-v2_2018-05-30a.xml";

    private Map<String, SelectableCriterion> criterionMap;

    public ESPDArtefactResource(@NotNull EDMVersion version) {
        criterionMap = new HashMap<>();

        switch (version) {
            case V1:
                ESPDRequestType requestV1Template = JAXB.unmarshal(CriteriaExtractor.class.getResourceAsStream(ESPD_REQUEST_V1_REGULATED_RESOURCE), ESPDRequestType.class);
                requestV1Template.getCriterion()
                        .forEach(this::addToCriterionMap);
                break;
            case V2:
                QualificationApplicationRequestType requestV2Template = JAXB.unmarshal(CriteriaExtractor.class.getResourceAsStream(ESPD_REQUEST_V2_REGULATED_RESOURCE), QualificationApplicationRequestType.class);
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
        if (lang == null || lang != EULanguageCodeEnum.EN) {
            LOGGER.log(Level.WARNING, "Warning... European Criteria Multilinguality not supported yet. Language set back to English");
        }

        return criterionMap.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, SelectableCriterion> getCriterionMap() {
        return getCriterionMap(EULanguageCodeEnum.EN);
    }

    @Override
    public Map<String, SelectableCriterion> getCriterionMap(EULanguageCodeEnum lang) {

        // failback check
        if (lang == null || lang != EULanguageCodeEnum.EN) {
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
        if (lang == null || lang != EULanguageCodeEnum.EN) {
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
        if (lang == null || lang != EULanguageCodeEnum.EN) {
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

