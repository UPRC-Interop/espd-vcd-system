package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.enums.ArtefactType;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.util.DocumentDetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.EDMVersion;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ImportESPDRequestService implements ImportESPDService<ESPDRequest> {

    private final ValidatorService schemaValidationService, schematronValidationService;

    public ImportESPDRequestService() {
        schematronValidationService = new SchematronValidatorService();
        schemaValidationService = new SchemaValidatorService();
    }

    @Override
    public ESPDRequest importESPDFile(File XML) throws RetrieverException, BuilderException, JAXBException, SAXException, ValidationException, IOException {
        EDMVersion artefactVersion = ArtefactUtils.findEDMVersion(new FileInputStream(XML));

        if (Objects.isNull(artefactVersion))
            throw new ValidationException("Cannot determine artefact version.");

        ArtefactValidator schemaResult = schemaValidationService.validateESPDFile(XML);
        ArtefactValidator schematronResult = schematronValidationService.validateESPDFile(XML);

        if (!schemaResult.isValid())
            throw new ValidationException("XSD validation failed on the supplied xml document.", schemaResult.getValidationMessages());
        if (!schematronResult.isValid())
            throw new ValidationException("Schematron validation failed on the supplied xml document.", schematronResult.getValidationMessages());

        InputStream is = new FileInputStream(XML);
        ESPDRequest request = null;
        switch (artefactVersion) {
            case V1:
                request = BuilderFactory.EDM_V1.createRegulatedModelBuilder().importFrom(is).createESPDRequest();
                break;
            case V2:
                request = BuilderFactory.EDM_V2.createRegulatedModelBuilder().importFrom(is).createESPDRequest();
                break;
        }

        CriteriaService criteriaService = new RetrieverCriteriaService(artefactVersion);
        request.setCriterionList(criteriaService.getUnselectedCriteria(request.getFullCriterionList()));
        generateUUIDs(request);
        is.close();
        return request;
    }

    @Override
    public DocumentDetails getDocumentDetails(File XML) {
        return new DocumentDetails(ArtefactUtils.findEDMVersion(XML), ArtefactUtils.findArtefactType(XML));
    }
}
