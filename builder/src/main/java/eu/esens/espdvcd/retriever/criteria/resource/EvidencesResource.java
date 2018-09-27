package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

/**
 * @author konstantinos Raptis
 */
public interface EvidencesResource extends Resource {

    /**
     * Get Evidences of a National Criterion in default language (EN).
     *
     * @param ID The National Criterion ID
     * @return The List of Evidences
     */
    List<Evidence> getEvidencesForCriterion(String ID) throws RetrieverException;

    /**
     * Get Evidences of a National Criterion.
     *
     * @param ID   The National Criterion ID
     * @param lang The language code (ISO 639-1:2002)
     * @return The List of Evidences
     */
    List<Evidence> getEvidencesForCriterion(String ID, EULanguageCodeEnum lang) throws RetrieverException;

}
