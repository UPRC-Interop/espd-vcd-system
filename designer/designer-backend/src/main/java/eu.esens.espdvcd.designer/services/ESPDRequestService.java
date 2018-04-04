package eu.esens.espdvcd.designer.services;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.io.InputStream;

public interface ESPDRequestService {
    ESPDRequest XMLStreamToRequestParser(InputStream XML) throws RetrieverException, BuilderException;
    ESPDRequest XMLStringToRequestParser(String XML) throws BuilderException, RetrieverException;
    InputStream RequestToXMLStreamTransformer(ESPDRequest request);
    String RequestToXMLStringTransformer(ESPDRequest request);
}
