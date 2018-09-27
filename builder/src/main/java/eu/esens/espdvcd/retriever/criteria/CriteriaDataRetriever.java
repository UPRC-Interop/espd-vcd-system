package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

/**
 * @author Konstantinos Raptis
 */
public interface CriteriaDataRetriever {

    /**
     * Identifies the origin of given criterion ID (European or National). If
     * the criterion ID found to belong to a European criterion, then method
     * return all its National sub-Criteria, filtered by given country code. If
     * the criterion ID found to belong to a National criterion, then method
     * first searches for the parent European Criterion and then return parent
     * European Criterion National sub-Criteria, filtered again by given country
     * code.
     *
     * @param ID   The source Criterion ID (European or National)
     * @param code The country identification Code (ISO 639-1:2002)
     * @return All National Criteria which mapped with source Criterion
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<SelectableCriterion> getNationalCriterionMapping(String ID, String code) throws RetrieverException;

    /**
     * Retrieves an e-Certis Criterion, which maps to
     * the related {@link SelectableCriterion} model class.
     *
     * @param ID The Criterion ID (European or National)
     * @return The Criterion with given ID
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    SelectableCriterion getCriterion(String ID) throws RetrieverException;

    /**
     * Retrieves all the Evidences of criterion with given ID.
     *
     * @param ID The Criterion ID (European or National)
     * @return The Evidences
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<Evidence> getEvidences(String ID) throws RetrieverException;

    /**
     * Specifies the language of the retrieved data.
     *
     * @param lang The language code (ISO 639-1:2002)
     * @throws RetrieverException In case language code does not exist in relevant codelists.
     */
    void setLang(EULanguageCodeEnum lang) throws RetrieverException;

}
