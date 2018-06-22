package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.io.InputStream;

public class RegulatedModeltoESPDRequestV1Service implements ModeltoESPDService {
    private final ArtefactType artefactType;


    public RegulatedModeltoESPDRequestV1Service() throws RetrieverException {
        artefactType = ArtefactType.REQUEST;
    }

    @Override
    public InputStream CreateXMLStreamFromModel(Object document) {
        return BuilderFactory.withEDMVersion1().getDocumentBuilderFor((ESPDRequest) document).getAsInputStream();
    }

    @Override
    public String CreateXMLStringFromModel(Object document) {
        return BuilderFactory.withEDMVersion1().getDocumentBuilderFor((ESPDRequest) document).getAsString();
    }

    @Override
    public ArtefactType getArtefactType() {
        return artefactType;
    }
}
