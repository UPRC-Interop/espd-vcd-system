package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.requirement.RequirementGroup;

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
            fixNullResponses(cr.getRequirementGroups());
        });
        return BuilderFactory.withEDMVersion2().getDocumentBuilderFor((ESPDResponse) document).getAsInputStream();
    }

    @Override
    public String CreateXMLStringFromModel(Object document) {
        ESPDResponse doc = (ESPDResponse) document;
        doc.getEvidenceList().removeIf(e -> e.getEvidenceURL() == null);
        doc.getFullCriterionList().forEach(cr -> {
            fixNullResponses(cr.getRequirementGroups());
        });
        return BuilderFactory.withEDMVersion2().getDocumentBuilderFor((ESPDResponse) document).getAsString();
    }

    @Override
    public ArtefactType getArtefactType() {
        return artefactType;
    }

    /*
    FIXME: TEMPORARY FIX FOR MISSING RESPONSE DATA TYPES
     */
    private void fixNullResponses(List<RequirementGroup> requirementGroupList) {
        requirementGroupList.forEach(rg ->   {
            rg.getRequirements().removeIf(r -> r.getResponse() == null);
            fixNullResponses(rg.getRequirementGroups());
        });
    }
}
