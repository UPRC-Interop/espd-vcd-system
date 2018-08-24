package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.builder.model.ModelFactory;
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
import java.util.stream.Collectors;

/**
 * @author Konstantinos Raptis
 */
public class ESPDArtefactResource implements CriteriaResource, LegislationResource, RequirementsResource {

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
                throw new IllegalArgumentException("Error... Invalid schema version value.");
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
        return criterionMap.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, SelectableCriterion> getCriterionMap() {
        return criterionMap;
    }

    @Override
    public LegislationReference getLegislationForCriterion(String ID) {
        return criterionMap.containsKey(ID)
                ? criterionMap.get(ID).getLegislationReference()
                : null;
    }

    @Override
    public List<RequirementGroup> getRequirementGroupsForCriterion(String ID) {
        return criterionMap.containsKey(ID)
                ? criterionMap.get(ID).getRequirementGroups()
                : null;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.ARTEFACT;
    }

}

