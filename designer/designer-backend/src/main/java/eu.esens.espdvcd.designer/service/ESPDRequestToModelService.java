package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.EDMVersion;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.List;

public class ESPDRequestToModelService implements ESPDtoModelService {

    private final ValidatorService schemaValidationService, schematronValidationService;
    private final ArtefactType artefactType;
    private CriteriaService criteriaService;
    private int counter = 0;

    public ESPDRequestToModelService() {
        this.artefactType = ArtefactType.REQUEST;
        schematronValidationService = new SchematronValidatorService();
        schemaValidationService = new SchemaValidatorService();
    }

    @Override
    public Object CreateModelFromXML(File XML) throws RetrieverException, BuilderException, FileNotFoundException, JAXBException, SAXException, ValidationException, IOException {
        EDMVersion artefactVersion = ArtefactUtils.findEDMVersion(new FileInputStream(XML));
        if (artefactVersion == EDMVersion.V1) {
            criteriaService = new PredefinedCriteriaService(EDMVersion.V1);

            ArtefactValidator schemaResult = schemaValidationService.validateESPDRequest(XML);
            if (!schemaResult.isValid())
                throw new ValidationException("XSD validation failed on the supplied xml document.", schemaResult.getValidationMessages());

            ArtefactValidator schematronResult = schematronValidationService.validateESPDRequest(XML);
            if (!schematronResult.isValid())
                throw new ValidationException("Schematron validation failed on the supplied xml document.", schematronResult.getValidationMessages());
        } else if (artefactVersion == EDMVersion.V2) {
            criteriaService = new PredefinedCriteriaService(EDMVersion.V2);
        } else {
            throw new ValidationException("Cannot find artefact version");
        }

        InputStream is = new FileInputStream(XML);

        ESPDRequest request = null;
        switch (artefactVersion) {
            case V1:
                request = BuilderFactory.EDM_V1.createRegulatedModelBuilder().importFrom(is).createESPDResponse();
                break;
            case V2:
                request = BuilderFactory.EDM_V2.createRegulatedModelBuilder().importFrom(is).createESPDResponse();
                break;
        }
        request.setCriterionList(criteriaService.getUnselectedCriteria(request.getFullCriterionList()));

        counter = 0;
        request.getFullCriterionList().forEach(cr -> {
            cr.setUUID(cr.getID());
            idFix(cr.getRequirementGroups());
        });
        is.close();
        //is.flush();
        is = null;
        return request;
    }

    private void idFix(List<RequirementGroup> reqGroups) {
        counter++;
        for (RequirementGroup reqGroup : reqGroups) {
            reqGroup.setUUID(reqGroup.getID() + "-" + counter);
            idFix(reqGroup.getRequirementGroups());
            List<Requirement> reqs = reqGroup.getRequirements();
            reqs.forEach(req -> {
                req.setUUID(req.getID() + "-" + counter);
            });
        }
    }
}
