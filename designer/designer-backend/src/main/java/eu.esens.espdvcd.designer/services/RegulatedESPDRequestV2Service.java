package eu.esens.espdvcd.designer.services;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.io.*;

public class RegulatedESPDRequestV2Service implements ESPDRequestService {
    private final CriteriaService criteriaService;

    public RegulatedESPDRequestV2Service() throws RetrieverException {
        criteriaService = new PredefinedCriteriaService();
    }

    @Override
    public ESPDRequest XMLFileToObjectTransformer(File XML) throws RetrieverException, BuilderException, FileNotFoundException {
        ESPDRequest request = BuilderFactory.V2.getModelBuilder().importFrom(new FileInputStream(XML)).createRegulatedESPDRequest();
        request.setCriterionList(criteriaService.getUnselectedCriteria(request.getFullCriterionList()));
        return request;
    }

    @Override
    public InputStream RequestToXMLStreamTransformer(ESPDRequest request) {
        return BuilderFactory.V2.getDocumentBuilderFor(request).getAsInputStream();
    }

    @Override
    public String RequestToXMLStringTransformer(ESPDRequest request) {
        return BuilderFactory.V2.getDocumentBuilderFor(request).getAsString();
    }
}
