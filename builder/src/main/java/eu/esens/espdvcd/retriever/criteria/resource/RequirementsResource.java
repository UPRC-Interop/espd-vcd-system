/**
 * Copyright 2016-2019 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
