package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.*;

public class RegulatedESPDResponseV2Service implements ESPDService {
    private final ArtefactType artefactType;

    public RegulatedESPDResponseV2Service(){
        artefactType=ArtefactType.RESPONSE;
    }
    @Override
    public ESPDResponse XMLFileToObjectTransformer(File XML) throws RetrieverException, BuilderException, ValidationException, FileNotFoundException, JAXBException, SAXException {
        return BuilderFactory.V2.getModelBuilder().importFrom(new FileInputStream(XML)).createESPDResponse();
    }

    @Override
    public InputStream ObjectToXMLStreamTransformer(Object document) {
        return BuilderFactory.V2.getDocumentBuilderFor((ESPDResponse) document).getAsInputStream();
    }

    @Override
    public String ObjectToXMLStringTransformer(Object document) {
        return BuilderFactory.V2.getDocumentBuilderFor((ESPDResponse) document).getAsString();
    }

    @Override
    public ArtefactType getArtefactType() {
        return artefactType;
    }
}
