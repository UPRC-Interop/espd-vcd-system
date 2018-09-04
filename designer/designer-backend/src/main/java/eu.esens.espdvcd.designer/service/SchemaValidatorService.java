package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.validator.ArtefactValidator;
import eu.esens.espdvcd.validator.ValidationResult;
import eu.esens.espdvcd.validator.Validators;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class SchemaValidatorService implements ValidatorService {
    @Override
    public ArtefactValidator validateESPDFile(File request) throws JAXBException, SAXException {
//        return Validators.createESPDSchemaValidator(request);

        return new ArtefactValidator() {
            @Override
            public boolean isValid() {
                return true;
            }

            @Override
            public List<ValidationResult> getValidationMessages() {
                return null;
            }

            @Override
            public List<ValidationResult> getValidationMessagesFiltered(String keyWord) {
                return null;
            }
        };
    }
}
