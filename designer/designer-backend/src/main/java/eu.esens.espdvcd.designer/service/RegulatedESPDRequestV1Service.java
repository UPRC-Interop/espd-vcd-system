package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegulatedESPDRequestV1Service implements ESPDService {
    private final CriteriaService criteriaService;
    private final ValidatorService schemaValidationService, schematronValidationService;
    private final ArtefactType artefactType;


    public RegulatedESPDRequestV1Service() throws RetrieverException {
        artefactType = ArtefactType.REQUEST;
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

        ESPDRequest request = BuilderFactory.getRegulatedModelBuilder().importFrom(new FileInputStream(XML)).createESPDRequest();
        request.setCriterionList(criteriaService.getUnselectedCriteria(request.getFullCriterionList()));
        return request;
    }

    @Override
    public InputStream ObjectToXMLStreamTransformer(Object document) {
        return BuilderFactory.withSchemaVersion1().getDocumentBuilderFor((ESPDRequest) document).getAsInputStream();
    }

    @Override
    public String ObjectToXMLStringTransformer(Object document) {
        return BuilderFactory.withSchemaVersion1().getDocumentBuilderFor((ESPDRequest) document).getAsString();
    }

    @Override
    public ArtefactType getArtefactType() {
        return artefactType;
    }
}
