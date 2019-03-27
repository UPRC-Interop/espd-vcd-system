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
import eu.esens.espdvcd.designer.typeEnum.CriteriaType;
import eu.esens.espdvcd.designer.util.CriteriaUtil;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.SelfContainedCriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

public enum SelfContainedCriteriaService implements CriteriaService {
    INSTANCE;

    private final CriteriaExtractor extractor = new SelfContainedCriteriaExtractorBuilder().build();

    public static SelfContainedCriteriaService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<SelectableCriterion> getCriteria() throws RetrieverException {
        extractor.setLang(EULanguageCodeEnum.EN);
        List<SelectableCriterion> criteria = extractor.getFullList();
        CriteriaUtil.generateUUIDs(criteria);
        return CriteriaUtil.markAsSelected(criteria);
    }

    @Override
    public List<SelectableCriterion> getUnselectedCriteria(List<SelectableCriterion> initialList) throws RetrieverException {
        extractor.setLang(EULanguageCodeEnum.EN);
        return extractor.getFullList(initialList);
    }

    @Override
    public List<SelectableCriterion> getTranslatedCriteria(String lang) throws RetrieverException {
        extractor.setLang(EULanguageCodeEnum.valueOf(lang.toUpperCase()));
        return extractor.getFullList();
    }

    @Override
    public CriteriaType[] getCriteriaFilters() {
        return CriteriaType.values();
    }
}
