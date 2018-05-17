package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegulatedESPDResponseV1Service implements ESPDService {
    private final ValidatorService schemaValidationService, schematronValidationService;
    private final ArtefactType artefactType;

    public RegulatedESPDResponseV1Service() {
        artefactType = ArtefactType.RESPONSE;
        schematronValidationService = new SchematronValidatorService();
        schemaValidationService = new SchemaValidatorService();
    }

    @Override
    public ESPDResponse XMLFileToObjectTransformer(File XML) throws BuilderException, ValidationException, FileNotFoundException, JAXBException, SAXException {
        ArtefactValidator schemaResult, schematronResult;

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

        return BuilderFactory.getRegulatedModelBuilder().importFrom(new FileInputStream(XML)).createESPDResponse();
    }

    @Override
    public InputStream ObjectToXMLStreamTransformer(Object document) {
        return BuilderFactory.withSchemaVersion1().getDocumentBuilderFor((ESPDResponse) document).getAsInputStream();
    }

    @Override
    public String ObjectToXMLStringTransformer(Object document) {
        return BuilderFactory.withSchemaVersion1().getDocumentBuilderFor((ESPDResponse) document).getAsString();
    }

    @Override
    public ArtefactType getArtefactType() {
        return artefactType;
    }
}
