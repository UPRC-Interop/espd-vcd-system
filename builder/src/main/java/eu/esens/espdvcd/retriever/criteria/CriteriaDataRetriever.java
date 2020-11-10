/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.retriever.criteria.resource.utils.CriterionUtils;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Konstantinos Raptis
 */
public interface CriteriaDataRetriever {

    /**
     * Identifies the origin of given criterion id (European or National). If
     * the criterion id found to belong to a European criterion, then method
     * return all its National sub-Criteria, filtered by given country code. If
     * the criterion id found to belong to a National criterion, then method
     * first searches for the parent European Criterion and then return parent
     * European Criterion National sub-Criteria, filtered again by given country
     * code.
     *
     * @param id          The source Criterion id (European or National)
     * @param countryCode The country identification Code (ISO 639-1:2002)
     * @return All National Criteria which mapped with source Criterion
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<SelectableCriterion> getNationalCriterionMapping(@NotNull String id,
                                                          @NotNull String countryCode) throws RetrieverException;

    /**
     * Retrieve a Criterion by given id.
     *
     * @param id The Criterion id (European or National)
     * @return The Criterion
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    SelectableCriterion getCriterion(@NotNull String id) throws RetrieverException;

    /**
     * Retrieve all the Evidences of a National Criterion.
     *
     * @param id The Criterion id (National)
     * @return The Evidences
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<Evidence> getEvidencesForNationalCriterion(@NotNull String id) throws RetrieverException;

    /**
     * Retrieve all the Evidences of a European Criterion filtered by specified country code.
     *
     * @param id          The Criterion id (European)
     * @param countryCode The country code (null to ignore filter)
     * @return The Evidences
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<Evidence> getEvidencesForEuropeanCriterion(@NotNull String id,
                                                    @Nullable String countryCode) throws RetrieverException;

    /**
     * Specify the language of the retrieved data.
     *
     * @param lang The language code (ISO 639-1:2002)
     * @throws RetrieverException In case the language code does not exists in the relevant codelist.
     */
    void setLang(@NotNull EULanguageCodeEnum lang) throws RetrieverException;

}
