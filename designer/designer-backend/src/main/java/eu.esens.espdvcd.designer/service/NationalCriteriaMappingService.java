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
package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetriever;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetrieverBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

public enum NationalCriteriaMappingService {
    INSTANCE;

    final CriteriaDataRetriever retriever = new CriteriaDataRetrieverBuilder().build();

    public static NationalCriteriaMappingService getInstance() {
        return INSTANCE;
    }

    public List<SelectableCriterion> getNationalCriteria(String euCriterionID, String euCountryCode) throws RetrieverException {
        retriever.setLang(EULanguageCodeEnum.EN);
        return retriever.getNationalCriterionMapping(euCriterionID, euCountryCode);
    }

    public List<SelectableCriterion> getTranslatedNationalCriteria(String euCriterionID, String euCountryCode, String lang) throws RetrieverException, IllegalArgumentException {
        retriever.setLang(EULanguageCodeEnum.valueOf(lang.toUpperCase()));
        return retriever.getNationalCriterionMapping(euCriterionID, euCountryCode);
    }
}
