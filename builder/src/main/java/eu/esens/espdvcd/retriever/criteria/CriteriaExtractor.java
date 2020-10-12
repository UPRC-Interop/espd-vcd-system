/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
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
     * @return the full criteria list with the criteria in the initialList as selected
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
