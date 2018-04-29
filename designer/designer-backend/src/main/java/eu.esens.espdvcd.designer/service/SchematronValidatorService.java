package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.validator.ArtefactValidator;
import eu.esens.espdvcd.validator.ValidatorFactory;
import eu.esens.espdvcd.validator.schematron.SchematronOrigin;

import java.io.File;

public class SchematronValidatorService implements ValidatorService {
    @Override
    public ArtefactValidator validateESPDRequest(File request) {
        return ValidatorFactory.createESPDRequestSchematronValidator(request, SchematronOrigin.EU);
    }

    @Override
    public ArtefactValidator validateESPDResponse(File response) {
        return ValidatorFactory.createESPDResponseSchematronValidator(response, SchematronOrigin.EU);
    }
}
