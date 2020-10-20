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
package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisNationalEntityEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetriever;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetrieverBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

public enum NationalCriteriaEvidenceService {
    INSTANCE;

    final CriteriaDataRetriever retriever = new CriteriaDataRetrieverBuilder().build();

    public static NationalCriteriaEvidenceService getInstance() {
        return INSTANCE;
    }

    public List<Evidence> getEvidence(String euCriterionID, String euCountryCode) throws RetrieverException {
        retriever.setLang(EULanguageCodeEnum.EN);
        retriever.setNationalEntity(ECertisNationalEntityEnum.valueOf(euCountryCode.toUpperCase()));
        return retriever.getEvidences(euCriterionID);
    }

    public List<Evidence> getTranslatedEvidence(String euCriterionID, String euCountryCode, String lang) throws RetrieverException, IllegalArgumentException {
        retriever.setLang(EULanguageCodeEnum.valueOf(lang.toUpperCase()));
        retriever.setNationalEntity(ECertisNationalEntityEnum.valueOf(euCountryCode.toUpperCase()));
        return retriever.getEvidences(euCriterionID);
    }
}
