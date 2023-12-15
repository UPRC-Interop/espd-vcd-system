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
import eu.esens.espdvcd.codelist.enums.internal.ContractingOperatorEnum;
import eu.esens.espdvcd.designer.typeEnum.CriteriaType;
import eu.esens.espdvcd.designer.util.CriteriaUtil;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.SelfContainedCriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

public enum SelfContainedCriteriaService implements CriteriaService {
    INSTANCE;

    private final CriteriaExtractor CA_EXTRACTOR = new SelfContainedCriteriaExtractorBuilder()
            .withContractingOperator(ContractingOperatorEnum.CONTRACTING_AUTHORITY)
            .build();
    private final CriteriaExtractor CE_EXTRACTOR = new SelfContainedCriteriaExtractorBuilder()
            .withContractingOperator(ContractingOperatorEnum.CONTRACTING_ENTITY)
            .build();


    public static SelfContainedCriteriaService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<SelectableCriterion> getCriteria(ContractingOperatorEnum contractingOperatorEnum) throws RetrieverException {
        CriteriaExtractor extractor = contractingOperatorEnum.equals(ContractingOperatorEnum.CONTRACTING_AUTHORITY) ? CA_EXTRACTOR : CE_EXTRACTOR;
        extractor.setLang(EULanguageCodeEnum.EN);
        List<SelectableCriterion> criteria = extractor.getFullList();
        CriteriaUtil.generateUUIDs(criteria);
        return CriteriaUtil.markAsSelected(criteria);
    }

    @Override
    public List<SelectableCriterion> getUnselectedCriteria(List<SelectableCriterion> initialList,
                                                           ContractingOperatorEnum contractingOperatorEnum) throws RetrieverException {
        CriteriaExtractor extractor = contractingOperatorEnum.equals(ContractingOperatorEnum.CONTRACTING_AUTHORITY) ? CA_EXTRACTOR : CE_EXTRACTOR;
        extractor.setLang(EULanguageCodeEnum.EN);
        return extractor.getFullList(initialList);
    }

    @Override
    public CriteriaType[] getCriteriaFilters() {
        return CriteriaType.values();
    }
}
