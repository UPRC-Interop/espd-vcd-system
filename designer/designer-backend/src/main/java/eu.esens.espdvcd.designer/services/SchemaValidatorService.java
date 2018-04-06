package eu.esens.espdvcd.designer.services;

import eu.esens.espdvcd.validator.ArtefactValidator;
import eu.esens.espdvcd.validator.ValidatorFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SchemaValidatorService implements ValidatorService {
    @Override
    public ArtefactValidator validateESPDRequest(File request) throws FileNotFoundException, JAXBException, SAXException {
        return ValidatorFactory.createESPDRequestSchemaValidator(new FileInputStream(request));
    }

    @Override
    public ArtefactValidator validateESPDResponse(File response) throws FileNotFoundException, JAXBException, SAXException {
        return ValidatorFactory.createESPDResponseSchemaValidator(new FileInputStream(response));
    }
}
