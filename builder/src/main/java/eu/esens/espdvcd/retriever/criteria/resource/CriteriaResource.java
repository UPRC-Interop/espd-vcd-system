package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;
import java.util.Map;

/**
 * @author konstantinos Raptis
 */
public interface CriteriaResource extends Resource {

    /**
     * Get European Criteria in the default language (EN).
     *
     * @return The Criterion List
     */
    List<SelectableCriterion> getCriterionList() throws RetrieverException;

    /**
     * Get European Criteria in the selected language.
     *
     * @param lang The language code (ISO 639-1:2002)
     * @return The Criterion List
     */
    List<SelectableCriterion> getCriterionList(EULanguageCodeEnum lang) throws RetrieverException;

    /**
     * Get European Criteria in the default language (EN).
     *
     * @return The Criterion Map
     */
    Map<String, SelectableCriterion> getCriterionMap() throws RetrieverException;

    /**
     * Get European Criteria in the selected language.
     *
     * @param lang The language code (ISO 639-1:2002)
     * @return The Criterion Map
     */
    Map<String, SelectableCriterion> getCriterionMap(EULanguageCodeEnum lang) throws RetrieverException;

}
