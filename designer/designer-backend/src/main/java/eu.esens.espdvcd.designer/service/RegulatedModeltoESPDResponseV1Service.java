package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;

import java.io.InputStream;

public class RegulatedModeltoESPDResponseV1Service implements ModeltoESPDService {
    private final ArtefactType artefactType;

    public RegulatedModeltoESPDResponseV1Service() {
        artefactType = ArtefactType.RESPONSE;
    }

    @Override
    public InputStream CreateXMLStreamFromModel(Object document) throws ValidationException {
        ESPDResponse response = (ESPDResponse) document;

        for (SelectableCriterion cr : response.getFullCriterionList()) {
            if(cr == null){
                throw new ValidationException("Null criterions are not supported.");
            }
        }
        return BuilderFactory.withEDMVersion1().getDocumentBuilderFor((ESPDResponse) document).getAsInputStream();
    }

    @Override
    public String CreateXMLStringFromModel(Object document) throws ValidationException {
        ESPDResponse response = (ESPDResponse) document;

        for (SelectableCriterion cr : response.getFullCriterionList()) {
            if(cr == null){
                throw new ValidationException("Null criterions are not supported.");
            }
        }

        return BuilderFactory.withEDMVersion1().getDocumentBuilderFor(response).getAsString();
    }

    @Override
    public ArtefactType getArtefactType() {
        return artefactType;
    }
}
