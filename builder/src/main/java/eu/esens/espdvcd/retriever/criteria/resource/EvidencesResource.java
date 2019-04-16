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
     * Get Evidences of a National Criterion in the selected language.
     *
     * @param ID   The National Criterion ID
     * @param lang The language code (ISO 639-1:2002)
     * @return The List of Evidences
     */
    List<Evidence> getEvidencesForCriterion(String ID, EULanguageCodeEnum lang) throws RetrieverException;

}
