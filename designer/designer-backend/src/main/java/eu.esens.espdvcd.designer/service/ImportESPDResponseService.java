package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.enums.ArtefactType;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.util.DocumentDetails;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.schema.EDMVersion;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ImportESPDResponseService implements ImportESPDService<ESPDResponse> {
    private final ValidatorService schemaValidationService, schematronValidationService;

    public ImportESPDResponseService() {
        schematronValidationService = new SchematronValidatorService();
        schemaValidationService = new SchemaValidatorService();
    }

    @Override
    public ESPDResponse importESPDFile(File XML) throws BuilderException, JAXBException, SAXException, ValidationException, IOException {
        EDMVersion artefactVersion = ArtefactUtils.findEDMVersion(XML);

        if (Objects.isNull(artefactVersion))
            throw new ValidationException("Cannot determine artefact version.");

        ArtefactValidator schemaResult = schemaValidationService.validateESPDFile(XML);
        ArtefactValidator schematronResult = schematronValidationService.validateESPDFile(XML);

        if (!schemaResult.isValid())
            throw new ValidationException("XSD validation failed on the supplied xml document.", schemaResult.getValidationMessages());
        if (!schematronResult.isValid())
            throw new ValidationException("Schematron validation failed on the supplied xml document.", schematronResult.getValidationMessages());

        InputStream is = new FileInputStream(XML);
        ESPDResponse response = null;
        switch (artefactVersion) {
            case V1:
                response = BuilderFactory.EDM_V1.createRegulatedModelBuilder().importFrom(is).createESPDResponse();
                break;
            case V2:
                response = BuilderFactory.EDM_V2.createRegulatedModelBuilder().importFrom(is).createESPDResponse();
                break;
        }
        generateUUIDs(response);
        is.close();
        return response;
    }

    @Override
    public DocumentDetails getDocumentDetails(File XML) {
        return new DocumentDetails(ArtefactUtils.findEDMVersion(XML), ArtefactUtils.findArtefactType(XML));
    }


}
