package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;

import javax.el.MethodNotFoundException;
import java.io.InputStream;

import static eu.esens.espdvcd.designer.util.CriteriaUtil.hasNullCriterion;

public enum SelfContainedExportESPDV2Service implements ExportESPDService {
    INSTANCE;

    public static SelfContainedExportESPDV2Service getInstance() {
        return INSTANCE;
    }

    @Override
    public InputStream exportESPDRequestAsInputStream(ESPDRequest model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return BuilderFactory.EDM_V2.createDocumentBuilderFor(model).getAsInputStream();
//        throw new MethodNotFoundException("Not yet implemented");
    }

    @Override
    public String exportESPDRequestAsString(ESPDRequest model) throws ValidationException {
//        if (hasNullCriterion(model.getFullCriterionList()))
//            throw new ValidationException("Null criteria are not permitted.");
//        return BuilderFactory.EDM_V1.createDocumentBuilderFor(model).getAsString();
        throw new MethodNotFoundException("Not yet implemented");
    }

    @Override
    public InputStream exportESPDResponseAsInputStream(ESPDResponse model) throws ValidationException {
//        if (hasNullCriterion(model.getFullCriterionList()))
//            throw new ValidationException("Null criteria are not permitted.");
//        return BuilderFactory.EDM_V2.createDocumentBuilderFor(model).getAsInputStream();
        throw new MethodNotFoundException("Not yet implemented");
    }

    @Override
    public String exportESPDResponseAsString(ESPDResponse model) throws ValidationException {
//        if (hasNullCriterion(model.getFullCriterionList()))
//            throw new ValidationException("Null criteria are not permitted.");
//        return BuilderFactory.EDM_V2.createDocumentBuilderFor(model).getAsString();
        throw new MethodNotFoundException("Not yet implemented");
    }
}
