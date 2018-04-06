package eu.esens.espdvcd.designer.services;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exceptions.ValidationException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegulatedESPDRequestV1Service implements ESPDRequestService {
    private final CriteriaService criteriaService;
    private final ValidatorService schemaValidationService, schematronValidationService;

    public RegulatedESPDRequestV1Service() throws RetrieverException {
        criteriaService = new PredefinedCriteriaService();
        schematronValidationService = new SchematronValidatorService();
        schemaValidationService = new SchemaValidatorService();
    }

    @Override
    public ESPDRequest XMLFileToObjectTransformer(File XML) throws RetrieverException, BuilderException, FileNotFoundException, JAXBException, SAXException, ValidationException {
        ArtefactValidator schemaResult = schemaValidationService.validateESPDRequest(XML);
        if (!schemaResult.isValid())
            throw new ValidationException("XSD validation failed on the supplied xml document.", schemaResult.getValidationMessages());

        ArtefactValidator schematronResult = schematronValidationService.validateESPDRequest(XML);
        if (!schematronResult.isValid())
            throw new ValidationException("Schematron validation failed on the supplied xml document.", schematronResult.getValidationMessages());

        ESPDRequest request = BuilderFactory.V1.getModelBuilder().importFrom(new FileInputStream(XML)).createRegulatedESPDRequest();
        request.setCriterionList(criteriaService.getUnselectedCriteria(request.getFullCriterionList()));
        return request;
    }

    @Override
    public InputStream RequestToXMLStreamTransformer(ESPDRequest request) {
        return BuilderFactory.V1.getDocumentBuilderFor(request).getAsInputStream();
    }

    @Override
    public String RequestToXMLStringTransformer(ESPDRequest request) {
        return BuilderFactory.V1.getDocumentBuilderFor(request).getAsString();
    }
}
