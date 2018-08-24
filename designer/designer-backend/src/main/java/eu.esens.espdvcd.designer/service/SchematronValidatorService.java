package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.validator.ArtefactValidator;
import eu.esens.espdvcd.validator.Validators;

import java.io.File;

public class SchematronValidatorService implements ValidatorService {
    @Override
    public ArtefactValidator validateESPDRequest(File request) {
        return Validators.createESPDRequestSchematronValidator(request, ArtefactUtils.findEDMVersion(request)); //,SchematronOrigin.EU);
    }

    @Override
    public ArtefactValidator validateESPDResponse(File response) {
        return Validators.createESPDResponseSchematronValidator(response, ArtefactUtils.findEDMVersion(response));
    }
}
