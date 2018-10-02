package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

/**
 * @author konstantinos Raptis
 */
public interface LegislationResource extends Resource {

    /**
     * Get Legislation of a Criterion in the default language (EN).
     *
     * @param ID The Criterion ID
     * @return The Criterion Legislation
     */
    LegislationReference getLegislationForCriterion(String ID) throws RetrieverException;

    /**
     * Get Legislation of a Criterion in the selected language.
     *
     * @param ID   The Criterion ID
     * @param lang The language code (ISO 639-1:2002)
     * @return The Criterion Legislation
     */
    LegislationReference getLegislationForCriterion(String ID, EULanguageCodeEnum lang) throws RetrieverException;

}
