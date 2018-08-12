package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.validator.ArtefactValidator;
import eu.esens.espdvcd.validator.Validators;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SchemaValidatorService implements ValidatorService {
    @Override
    public ArtefactValidator validateESPDRequest(File request) throws FileNotFoundException, JAXBException, SAXException {
        return Validators.createESPDRequestSchemaValidator(new FileInputStream(request));
    }

    @Override
    public ArtefactValidator validateESPDResponse(File response) throws FileNotFoundException, JAXBException, SAXException {
        return Validators.createESPDResponseSchemaValidator(new FileInputStream(response));
    }
}
