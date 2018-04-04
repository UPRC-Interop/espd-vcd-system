package eu.esens.espdvcd.designer.services;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class RegulatedESPDRequestV2Service implements ESPDRequestService {
    private final CriteriaService criteriaService;

    public RegulatedESPDRequestV2Service() throws RetrieverException {
        criteriaService = new PredefinedCriteriaService();
    }

    @Override
    public ESPDRequest XMLStreamToRequestParser(InputStream XML) throws RetrieverException, BuilderException {
        ESPDRequest request = BuilderFactory.V2.getModelBuilder().importFrom(XML).createRegulatedESPDRequest();
        request.setCriterionList(criteriaService.getUnselectedCriteria(request.getFullCriterionList()));
        return request;
    }

    @Override
    public ESPDRequest XMLStringToRequestParser(String XML) throws BuilderException, RetrieverException {
        ESPDRequest request = BuilderFactory.V2.getModelBuilder().importFrom(new ByteArrayInputStream(XML.getBytes())).createRegulatedESPDRequest();
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
