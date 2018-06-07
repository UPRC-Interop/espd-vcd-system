package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;

import java.io.*;

public class RegulatedESPDRequestV2Service implements ESPDService {
    private final CriteriaService criteriaService;
    private final ArtefactType artefactType;

    public RegulatedESPDRequestV2Service() throws RetrieverException {
        artefactType = ArtefactType.REQUEST;
        criteriaService = new PredefinedCriteriaService(SchemaVersion.V2);
    }

    @Override
    public ESPDRequest XMLFileToObjectTransformer(File XML) throws RetrieverException, BuilderException, FileNotFoundException {
        ESPDRequest request = BuilderFactory.getRegulatedModelBuilder().importFrom(new FileInputStream(XML)).createESPDRequest();
        request.setCriterionList(criteriaService.getUnselectedCriteria(request.getFullCriterionList()));
        return request;
    }

    @Override
    public InputStream ObjectToXMLStreamTransformer(Object document) {
        return BuilderFactory.withSchemaVersion2().getDocumentBuilderFor((ESPDRequest) document).getAsInputStream();
    }

    @Override
    public String ObjectToXMLStringTransformer(Object document) {
        return BuilderFactory.withSchemaVersion2().getDocumentBuilderFor((ESPDRequest) document).getAsString();
    }

    @Override
    public ArtefactType getArtefactType() {
        return artefactType;
    }
}
