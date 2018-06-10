package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorFactory;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;

import java.util.List;

public class ECertisCriteriaService implements CriteriaService {
    private ECertisCriteriaExtractor ECERTIS_CRITERIA_EXTRACTOR;

    public ECertisCriteriaService(SchemaVersion version) {
        ECERTIS_CRITERIA_EXTRACTOR = new ECertisCriteriaExtractor(version);
    }

    @Override
    public List<SelectableCriterion> getCriteria() throws RetrieverException {
        return ECERTIS_CRITERIA_EXTRACTOR.getFullList();
    }

    @Override
    public List<SelectableCriterion> getUnselectedCriteria(List<SelectableCriterion> initialList) throws RetrieverException {
        return ECERTIS_CRITERIA_EXTRACTOR.getFullList(initialList);
    }

    @Override
    public List<SelectableCriterion> getTranslatedCriteria(String lang) throws RetrieverException {

        ECERTIS_CRITERIA_EXTRACTOR.setLang(lang);
        List<SelectableCriterion> criteria = ECERTIS_CRITERIA_EXTRACTOR.getFullList();

        //CODE FOR TRANSLATING CRITERIA ONE BY ONE
//        for (SelectableCriterion cr : criteria) {
//            ECertisCriterion eCertisCriterion;
//            try {
//                eCertisCriterion = ECERTIS_CRITERIA_EXTRACTOR.getCriterion(cr.getID());
//            } catch (RetrieverException e) {
//                Logger.getLogger(this.getClass().getName()).warning("Criteria not translated " + cr.getName());
//                continue;
//            }
//            cr.setDescription(eCertisCriterion.getDescription());
//            cr.setName(eCertisCriterion.getName());
//            cr.setLegislationReference(eCertisCriterion.getLegislationReference());
//            Logger.getLogger(this.getClass().getName()).info(eCertisCriterion.getDescription());
//        }

        ECERTIS_CRITERIA_EXTRACTOR.initLang();
        return criteria;
    }
}
