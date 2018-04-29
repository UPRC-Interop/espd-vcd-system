package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

public interface ValidatorService {
    ArtefactValidator validateESPDRequest(File request) throws JAXBException, SAXException, FileNotFoundException;
    ArtefactValidator validateESPDResponse(File response) throws JAXBException, SAXException, FileNotFoundException;
}
