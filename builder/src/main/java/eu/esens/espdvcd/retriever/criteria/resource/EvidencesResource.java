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
package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author konstantinos Raptis
 */
public interface EvidencesResource extends Resource {

    /**
     * Get Evidences of a National Criterion in default language (English).
     *
     * @param id The Criterion id (National)
     * @return The List of Evidences
     */
    default List<Evidence> getEvidencesForNationalCriterion(@NotNull String id) throws RetrieverException {
        return getEvidencesForNationalCriterion(id, EULanguageCodeEnum.EN);
    }

    /**
     * Get Evidences of a National Criterion in the selected language.
     *
     * @param id   The Criterion id (National)
     * @param lang The language code (ISO 639-1:2002)
     * @return The List of Evidences
     */
    List<Evidence> getEvidencesForNationalCriterion(@NotNull String id,
                                                    @NotNull EULanguageCodeEnum lang) throws RetrieverException;

    /**
     * Get Evidences of a European Criterion in default language (English), filtered by the specified country code.
     *
     * @param id          The Criterion id (European)
     * @param countryCode The country code (ISO 2A)
     * @return
     * @throws RetrieverException
     */
    default List<Evidence> getEvidencesForEuropeanCriterion(@NotNull String id,
                                                            @Nullable String countryCode) throws RetrieverException {
        return getEvidencesForEuropeanCriterion(id, countryCode, EULanguageCodeEnum.EN);
    }

    /**
     * Get Evidences of a European Criterion in the selected language, filtered by the specified country code.
     *
     * @param id          The Criterion id (European)
     * @param countryCode The country code (ISO 2A)
     * @param lang        The language code (ISO 639-1:2002)
     * @return
     * @throws RetrieverException
     */
    List<Evidence> getEvidencesForEuropeanCriterion(@NotNull String id,
                                                    @Nullable String countryCode,
                                                    @NotNull EULanguageCodeEnum lang) throws RetrieverException;


}
