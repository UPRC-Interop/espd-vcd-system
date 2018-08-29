package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;

import java.io.InputStream;
import java.util.List;

public class RegulatedModeltoESPDResponseV2Service implements ModeltoESPDService {
    private final ArtefactType artefactType;

    public RegulatedModeltoESPDResponseV2Service() {
        artefactType = ArtefactType.RESPONSE;
    }

    @Override
    public InputStream CreateXMLStreamFromModel(Object document) {
        ESPDResponse doc = (ESPDResponse) document;
        doc.getEvidenceList().removeIf(e -> e.getEvidenceURL() == null);
        doc.getFullCriterionList().forEach(cr -> {
            fixResponses(cr.getRequirementGroups());
        });
        return BuilderFactory.withEDMVersion2().getDocumentBuilderFor((ESPDResponse) document).getAsInputStream();
    }

    @Override
    public String CreateXMLStringFromModel(Object document) {
        ESPDResponse doc = (ESPDResponse) document;
        doc.getEvidenceList().removeIf(e -> e.getEvidenceURL() == null);
        doc.getFullCriterionList().forEach(cr -> {
            fixResponses(cr.getRequirementGroups());
        });
        return BuilderFactory.withEDMVersion2().getDocumentBuilderFor((ESPDResponse) document).getAsString();
    }

    @Override
    public ArtefactType getArtefactType() {
        return artefactType;
    }

    private void fixResponses(List<RequirementGroup> requirementGroupList) {

        for (RequirementGroup rg : requirementGroupList) {

            if (rg.getRequirements().get(0).getResponseDataType().equals(ResponseTypeEnum.INDICATOR) && rg.getRequirementGroups().size() > 0) {
                IndicatorResponse indicator = (IndicatorResponse) rg.getRequirements().get(0).getResponse();
                if (indicator != null) {
                    rg.getRequirementGroups().forEach(requirementGroup -> {
                        switch (requirementGroup.getCondition()) {
                            case "ONTRUE":
                                if (!indicator.isIndicator()) {
                                    requirementGroup.getRequirements().forEach(rq -> {
                                        rq.setResponse(null);
                                    });
                                }
                                break;
                            case "ONFALSE":
                                if (indicator.isIndicator()) {
                                    requirementGroup.getRequirements().forEach(rq -> {
                                        rq.setResponse(null);
                                    });
                                }
                                break;
                            default:
                                System.out.println("Ignoring condition " + requirementGroup.getCondition());
                                break;
                        }
                    });
                }
            }
            fixResponses(rg.getRequirementGroups());
        }

    }
}
