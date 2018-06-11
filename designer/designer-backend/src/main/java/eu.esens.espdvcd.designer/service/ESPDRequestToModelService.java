package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.RegulatedESPDRequest;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;

public class ESPDRequestToModelService implements ESPDtoModelService {

    private CriteriaService criteriaService;
    private final ValidatorService schemaValidationService, schematronValidationService;
    private final ArtefactType artefactType;
    private int counter=0;

    public ESPDRequestToModelService() {
        this.artefactType=ArtefactType.REQUEST;
        schematronValidationService = new SchematronValidatorService();
        schemaValidationService = new SchemaValidatorService();
    }

    @Override
    public Object CreateModelFromXML(File XML) throws RetrieverException, BuilderException, FileNotFoundException, JAXBException, SAXException, ValidationException, IOException {
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

        InputStream is = new FileInputStream(XML);

        ESPDRequest request = BuilderFactory.getRegulatedModelBuilder().importFrom(is).createESPDRequest();
        request.setCriterionList(criteriaService.getUnselectedCriteria(request.getFullCriterionList()));

        counter=0;
        request.getFullCriterionList().forEach(cr -> {
            cr.setUUID(cr.getID());
            idFix(cr.getRequirementGroups());
        });
        is.close();
        //is.flush();
        is=null;
        return request;
    }

    private void idFix(List<RequirementGroup> reqGroups){
        counter++;
        for(RequirementGroup reqGroup : reqGroups){
            reqGroup.setUUID(reqGroup.getID()+"-"+counter);
            idFix(reqGroup.getRequirementGroups());
            List<Requirement> reqs = reqGroup.getRequirements();
            reqs.forEach(req -> {
                req.setUUID(req.getID()+"-"+counter);
            });
        }
    }
}
