package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.validator.ArtefactValidator;
import eu.esens.espdvcd.validator.ValidationResult;

import java.io.File;
import java.util.List;

public class SchematronValidatorService implements ValidatorService {
    @Override
    public ArtefactValidator validateESPDFile(File request) {
//        return ValidatorFactory.createESPDSchematronValidator(request);

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
