package eu.esens.espdvcd.designer.services;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exceptions.ValidationException;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public interface ESPDResponseService {
    ESPDResponse XMLFileToObjectTransformer(File XML) throws RetrieverException, BuilderException, ValidationException, FileNotFoundException, JAXBException, SAXException;
    InputStream ResponseToXMLStreamTransformer(ESPDResponse request);
    String ResponseToXMLStringTransformer(ESPDResponse request);
}
