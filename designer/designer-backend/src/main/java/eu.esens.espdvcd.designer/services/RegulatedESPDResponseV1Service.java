package eu.esens.espdvcd.designer.services;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class RegulatedESPDResponseV1Service implements ESPDResponseService {

    @Override
    public ESPDResponse XMLStreamToResponseParser(InputStream XML) throws RetrieverException, BuilderException {
        return BuilderFactory.V1.getModelBuilder().importFrom(XML).createRegulatedESPDResponse();
    }

    @Override
    public ESPDResponse XMLStringToResponseParser(String XML) throws BuilderException, RetrieverException {
        return BuilderFactory.V1.getModelBuilder().importFrom(new ByteArrayInputStream(XML.getBytes())).createRegulatedESPDResponse();
    }

    @Override
    public InputStream ResponseToXMLStreamTransformer(ESPDResponse response) {
        return BuilderFactory.V1.getDocumentBuilderFor(response).getAsInputStream();
    }

    @Override
    public String ResponseToXMLStringTransformer(ESPDResponse response) {
        return BuilderFactory.V1.getDocumentBuilderFor(response).getAsString();
    }
}
