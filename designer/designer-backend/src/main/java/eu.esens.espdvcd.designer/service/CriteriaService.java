/**
 * Copyright 2016-2018 University of Piraeus Research Center
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
package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.designer.typeEnum.CriteriaType;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;
import java.util.stream.Collectors;

public interface CriteriaService {

    List<SelectableCriterion> getCriteria() throws RetrieverException;

    List<SelectableCriterion> getUnselectedCriteria(List<SelectableCriterion> initialList) throws RetrieverException;

    List<SelectableCriterion> getTranslatedCriteria(String lang) throws RetrieverException;

    CriteriaType[] getCriteriaFilters();

    default List<SelectableCriterion> getFilteredCriteriaList(String criteriaType) throws RetrieverException, IllegalArgumentException {
        return getCriteria().stream()
                .filter(cr -> cr.getTypeCode().matches(CriteriaType.valueOf(criteriaType).getRegex()))
                .collect(Collectors.toList());
    }

    default List<SelectableCriterion> getFilteredTranslatedCriteriaList(String criteriaType, String lang) throws RetrieverException, IllegalArgumentException {
        return getTranslatedCriteria(lang).stream()
                .filter(cr -> cr.getTypeCode().matches(CriteriaType.valueOf(criteriaType).getRegex()))
                .collect(Collectors.toList());
    }
}
