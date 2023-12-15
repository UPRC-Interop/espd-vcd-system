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
