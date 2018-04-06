package eu.esens.espdvcd.designer.services;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exceptions.ValidationException;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.*;

public class RegulatedESPDResponseV2Service implements ESPDResponseService {

    @Override
    public ESPDResponse XMLFileToObjectTransformer(File XML) throws RetrieverException, BuilderException, ValidationException, FileNotFoundException, JAXBException, SAXException {
        return BuilderFactory.V2.getModelBuilder().importFrom(new FileInputStream(XML)).createRegulatedESPDResponse();
    }

    @Override
    public InputStream ResponseToXMLStreamTransformer(ESPDResponse response) {
        return BuilderFactory.V2.getDocumentBuilderFor(response).getAsInputStream();
    }

    @Override
    public String ResponseToXMLStringTransformer(ESPDResponse response) {
        return BuilderFactory.V2.getDocumentBuilderFor(response).getAsString();
    }
}
