package eu.esens.espdvcd.designer.routes;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.PredefinedESPDCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

public class CriteriaRoute {

    private final CriteriaExtractor eCertisExtractor, predefinedExtractor;

    public CriteriaRoute(){
        eCertisExtractor = new ECertisCriteriaExtractor();
        predefinedExtractor = new PredefinedESPDCriteriaExtractor();
    }

    public List<SelectableCriterion> getPredefinedCriteria() throws RetrieverException {
        return predefinedExtractor.getFullList();
    }

    public List<SelectableCriterion> getECertisCriteria() throws RetrieverException {
        return eCertisExtractor.getFullList();
    }
}
