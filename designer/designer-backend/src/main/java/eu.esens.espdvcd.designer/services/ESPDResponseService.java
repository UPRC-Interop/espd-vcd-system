package eu.esens.espdvcd.designer.services;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.io.InputStream;

public interface ESPDResponseService {
    ESPDResponse XMLStreamToResponseParser(InputStream XML) throws RetrieverException, BuilderException;
    ESPDResponse XMLStringToResponseParser(String XML) throws BuilderException, RetrieverException;
    InputStream ResponseToXMLStreamTransformer(ESPDResponse request);
    String ResponseToXMLStringTransformer(ESPDResponse request);
}
