package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.EDMVersion;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ESPDResponseToModelService implements ESPDtoModelService {
    private final ValidatorService schemaValidationService, schematronValidationService;
    private int counter;

    public ESPDResponseToModelService() {
        schematronValidationService = new SchematronValidatorService();
        schemaValidationService = new SchemaValidatorService();
    }

    @Override
    public Object CreateModelFromXML(File XML) throws RetrieverException, BuilderException, FileNotFoundException, JAXBException, SAXException, ValidationException, IOException {
        ArtefactValidator schemaResult, schematronResult;
        EDMVersion artefactVersion = ArtefactUtils.findEDMVersion(new FileInputStream(XML));

        if (artefactVersion == EDMVersion.V1) {
            if (isESPDRequest(XML)) {
                schemaResult = schemaValidationService.validateESPDRequest(XML);
                schematronResult = schematronValidationService.validateESPDRequest(XML);

            } else {
                schemaResult = schemaValidationService.validateESPDResponse(XML);
                schematronResult = schematronValidationService.validateESPDResponse(XML);
            }
            if (!schemaResult.isValid())
                throw new ValidationException("XSD validation failed on the supplied xml document.", schemaResult.getValidationMessages());
            if (!schematronResult.isValid())
                throw new ValidationException("Schematron validation failed on the supplied xml document.", schematronResult.getValidationMessages());
        }

        InputStream is = new FileInputStream(XML);

        ESPDResponse response = null;
        switch (artefactVersion) {
            case V1:
                response = BuilderFactory.withEDMVersion1().getRegulatedModelBuilder().importFrom(is).createESPDResponse();
                break;
            case V2:
                response = BuilderFactory.withEDMVersion2().getRegulatedModelBuilder().importFrom(is).createESPDResponse();
                break;
        }
        counter = 0;
        response.getFullCriterionList().forEach(cr -> {
            cr.setUUID(cr.getID());
            idFix(cr.getRequirementGroups());
        });
        is.close();
        //is.flush();
        is = null;
        return response;
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

    private boolean isESPDRequest(File Artefact) {
        String line = "";
        try (Scanner scanner = new Scanner(Artefact)) {
            int linesToSkip = 2;
            for (int i = 0; i < linesToSkip; i++) {
                if (scanner.hasNextLine())
                    line = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(ModeltoESPDService.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return line.trim().split("\\s+")[0].contains("ESPDRequest");
    }
}
