package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDResponse;

import java.io.InputStream;

public class RegulatedModeltoESPDResponseV2Service implements ModeltoESPDService {
    private final ArtefactType artefactType;

    public RegulatedModeltoESPDResponseV2Service() {
        artefactType = ArtefactType.RESPONSE;
    }

    @Override
    public InputStream CreateXMLStreamFromModel(Object document) {
        ESPDResponse doc = (ESPDResponse) document;
        doc.getEvidenceList().removeIf(e -> e.getEvidenceURL() == null);
        return BuilderFactory.withEDMVersion2().getDocumentBuilderFor((ESPDResponse) document).getAsInputStream();
    }

    @Override
    public String CreateXMLStringFromModel(Object document) {
        ESPDResponse doc = (ESPDResponse) document;
        doc.getEvidenceList().removeIf(e -> e.getEvidenceURL() == null);
        return BuilderFactory.withEDMVersion2().getDocumentBuilderFor((ESPDResponse) document).getAsString();
    }

    @Override
    public ArtefactType getArtefactType() {
        return artefactType;
    }
}
