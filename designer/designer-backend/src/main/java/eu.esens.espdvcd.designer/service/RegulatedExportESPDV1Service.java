package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;

import java.io.InputStream;

public class RegulatedExportESPDV1Service implements ExportESPDService {

    @Override
    public InputStream exportESPDRequestAsInputStream(ESPDRequest model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(model).getAsInputStream();
    }

    @Override
    public String exportESPDRequestAsString(ESPDRequest model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(model).getAsString();
    }

    @Override
    public InputStream exportESPDResponseAsInputStream(ESPDResponse model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(model).getAsInputStream();
    }

    @Override
    public String exportESPDResponseAsString(ESPDResponse model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(model).getAsString();
    }
}
