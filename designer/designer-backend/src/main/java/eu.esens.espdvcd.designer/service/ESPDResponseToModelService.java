package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ESPDResponseToModelService implements ESPDtoModelService {
    private final ValidatorService schemaValidationService, schematronValidationService;

    public ESPDResponseToModelService() {
        schematronValidationService = new SchematronValidatorService();
        schemaValidationService = new SchemaValidatorService();
    }

    @Override
    public Object CreateModelFromXML(File XML) throws RetrieverException, BuilderException, FileNotFoundException, JAXBException, SAXException, ValidationException {
        ArtefactValidator schemaResult, schematronResult;
        SchemaVersion artefactVersion = ArtefactUtils.findSchemaVersion(new FileInputStream(XML));

        if(artefactVersion == SchemaVersion.V1){
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
        return BuilderFactory.getRegulatedModelBuilder().importFrom(new FileInputStream(XML)).createESPDResponse();
    }

    private boolean isESPDRequest(File Artefact) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(Artefact);
        } catch (FileNotFoundException e) {
            Logger.getLogger(ModeltoESPDService.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        String line = "";
        int linesToSkip = 2;
        for (int i = 0; i < linesToSkip; i++) {
            if (scanner.hasNextLine())
                line = scanner.nextLine();
        }
        return line.trim().split("\\s+")[0].contains("ESPDRequest");
    }
}
