package eu.esens.espdvcd.designer.services;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exceptions.ValidationException;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.*;

public class RegulatedESPDResponseV1Service implements ESPDResponseService {
    private final ValidatorService schemaValidationService, schematronValidationService;

    public RegulatedESPDResponseV1Service(){
        schematronValidationService = new SchematronValidatorService();
        schemaValidationService = new SchemaValidatorService();
    }

    @Override
    public ESPDResponse XMLFileToObjectTransformer(File XML) throws RetrieverException, BuilderException, ValidationException, FileNotFoundException, JAXBException, SAXException {
        ArtefactValidator schemaResult = schemaValidationService.validateESPDResponse(XML);
        if (!schemaResult.isValid())
            throw new ValidationException("XSD validation failed on the supplied xml document.", schemaResult.getValidationMessages());

        ArtefactValidator schematronResult = schematronValidationService.validateESPDResponse(XML);
        if (!schematronResult.isValid())
            throw new ValidationException("Schematron validation failed on the supplied xml document.", schematronResult.getValidationMessages());

        return BuilderFactory.V1.getModelBuilder().importFrom(new FileInputStream(XML)).createRegulatedESPDResponse();
    }

    @Override
    public InputStream ResponseToXMLStreamTransformer(ESPDResponse response) {
        return BuilderFactory.V1.getDocumentBuilderFor(response).getAsInputStream();
    }

    @Override
    public String ResponseToXMLStringTransformer(ESPDResponse response) {
        return BuilderFactory.V1.getDocumentBuilderFor(response).getAsString();
    }
}
