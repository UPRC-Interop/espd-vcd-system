package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.RegulatedESPDRequest;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ESPDRequestToModelService implements ESPDtoModelService {

    private CriteriaService criteriaService;
    private final ValidatorService schemaValidationService, schematronValidationService;
    private final ArtefactType artefactType;

    public ESPDRequestToModelService() {
        this.artefactType=ArtefactType.REQUEST;
        schematronValidationService = new SchematronValidatorService();
        schemaValidationService = new SchemaValidatorService();
    }

    @Override
    public Object CreateModelFromXML(File XML) throws RetrieverException, BuilderException, FileNotFoundException, JAXBException, SAXException, ValidationException {
        SchemaVersion artifactVersion = ArtefactUtils.findSchemaVersion(new FileInputStream(XML));
        if (artifactVersion == SchemaVersion.V1){
            criteriaService = new PredefinedCriteriaService(SchemaVersion.V1);

            ArtefactValidator schemaResult = schemaValidationService.validateESPDRequest(XML);
            if (!schemaResult.isValid())
                throw new ValidationException("XSD validation failed on the supplied xml document.", schemaResult.getValidationMessages());

            ArtefactValidator schematronResult = schematronValidationService.validateESPDRequest(XML);
            if (!schematronResult.isValid())
                throw new ValidationException("Schematron validation failed on the supplied xml document.", schematronResult.getValidationMessages());
        }else if(artifactVersion == SchemaVersion.V2){
            criteriaService = new PredefinedCriteriaService(SchemaVersion.V2);
        }else {
            throw new ValidationException("Cannot find artifact version");
        }

        ESPDRequest request = BuilderFactory.getRegulatedModelBuilder().importFrom(new FileInputStream(XML)).createESPDRequest();
        request.setCriterionList(criteriaService.getUnselectedCriteria(request.getFullCriterionList()));
        return request;
    }
}
