package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

/**
 * @author konstantinos Raptis
 */
public interface RequirementsResource extends Resource {

    /**
     * Get Requirements of a Criterion in the default language (EN).
     *
     * @param ID The criterion ID
     * @return The Requirements List
     * @throws RetrieverException
     */
    List<RequirementGroup> getRequirementsForCriterion(String ID) throws RetrieverException;

    /**
     * Get Requirements of a Criterion in the selected language.
     *
     * @param ID   The criterion ID
     * @param lang The language code (ISO 639-1:2002)
     * @return The Requirements List
     * @throws RetrieverException
     */
    List<RequirementGroup> getRequirementsForCriterion(String ID, EULanguageCodeEnum lang) throws RetrieverException;

}
