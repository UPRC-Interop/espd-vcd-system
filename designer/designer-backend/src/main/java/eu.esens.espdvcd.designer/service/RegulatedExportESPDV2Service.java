package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public class RegulatedExportESPDV2Service implements ExportESPDService {

    @Override
    public InputStream exportESPDRequestAsInputStream(ESPDRequest model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criterions are not permitted.");
        return BuilderFactory.EDM_V2.createDocumentBuilderFor(model).getAsInputStream();
    }

    @Override
    public String exportESPDRequestAsString(ESPDRequest model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criterions are not permitted.");
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(model).getAsString();
    }

    @Override
    public InputStream exportESPDResponseAsInputStream(ESPDResponse model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criterions are not permitted.");
        finalizeV2Response(model);
        return BuilderFactory.EDM_V2.createDocumentBuilderFor(model).getAsInputStream();
    }

    @Override
    public String exportESPDResponseAsString(ESPDResponse model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criterions are not permitted.");
        finalizeV2Response(model);
        return BuilderFactory.EDM_V2.createDocumentBuilderFor(model).getAsString();
    }

    private void finalizeV2Response(final ESPDResponse document) {
        document.getEvidenceList().removeIf(e -> e.getEvidenceURL() == null);
        document.getFullCriterionList().forEach(cr -> fixResponses(cr.getRequirementGroups()));
    }

    private void fixResponses(final List<RequirementGroup> requirementGroupList) {
        for (RequirementGroup rg : requirementGroupList) {
            if (rg.getRequirements().get(0).getResponseDataType().equals(ResponseTypeEnum.INDICATOR) && rg.getRequirementGroups().size() > 0) {
                IndicatorResponse indicator = (IndicatorResponse) rg.getRequirements().get(0).getResponse();
                if (indicator != null) {
                    rg.getRequirementGroups().forEach(requirementGroup -> {
                        switch (requirementGroup.getCondition()) {
                            case "ONTRUE":
                                if (!indicator.isIndicator()) {
                                    requirementGroup.getRequirements().forEach(rq -> rq.setResponse(null));
                                }
                                break;
                            case "ONFALSE":
                                if (indicator.isIndicator()) {
                                    requirementGroup.getRequirements().forEach(rq -> rq.setResponse(null));
                                }
                                break;
                            default:
                                Logger.getLogger(ExportESPDService.class.getName()).warning("Ignoring condition " + requirementGroup.getCondition());
                                break;
                        }
                    });
                }
            }
            fixResponses(rg.getRequirementGroups());
        }
    }
}
