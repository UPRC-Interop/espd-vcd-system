package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegulatedModeltoESPDRequestV1Service implements ModeltoESPDService {
    private final ArtefactType artefactType;


    public RegulatedModeltoESPDRequestV1Service() throws RetrieverException {
        artefactType = ArtefactType.REQUEST;
    }

    @Override
    public InputStream CreateXMLStreamFromModel(Object document) {
        return BuilderFactory.withSchemaVersion1().getDocumentBuilderFor((ESPDRequest) document).getAsInputStream();
    }

    @Override
    public String CreateXMLStringFromModel(Object document) {
        return BuilderFactory.withSchemaVersion1().getDocumentBuilderFor((ESPDRequest) document).getAsString();
    }

    @Override
    public ArtefactType getArtefactType() {
        return artefactType;
    }
}
