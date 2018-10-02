package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

/**
 * @author konstantinos Raptis
 */
public interface CriteriaExtractor {

    /**
     * @return The full criteria list with all the criteria selected
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<SelectableCriterion> getFullList() throws RetrieverException;

    /**
     * @param initialList
     * @return the full criteria list with the criteria in the initialList as not selected
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) throws RetrieverException;

    /**
     * @param initialList   if @isSelected is true, the criteria from the @initialList will be
     *                      included as selected, otherwise they will be included as not selected
     * @param addAsSelected
     * @return the full criteria list with the criteria in the initialList as selected
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) throws RetrieverException;

    /**
     * Specifies the language of the retrieved data.
     *
     * @param lang The language code (ISO 639-1:2002)
     * @throws RetrieverException In case language code does not exist in relevant codelists.
     */
    void setLang(EULanguageCodeEnum lang) throws RetrieverException;

}
