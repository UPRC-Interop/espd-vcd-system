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

import eu.esens.espdvcd.codelist.enums.internal.ContractingOperatorEnum;
import eu.esens.espdvcd.designer.typeEnum.CriteriaType;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.RegulatedCriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.enums.EDMVersion;

import java.util.List;

import static eu.esens.espdvcd.designer.util.CriteriaUtil.generateUUIDs;
import static eu.esens.espdvcd.designer.util.CriteriaUtil.markAsSelected;

public enum RegulatedCriteriaService implements CriteriaService {
    V1(EDMVersion.V1), V2(EDMVersion.V2);

    private final CriteriaExtractor PREDEFINED_CA_EXTRACTOR;
    private final CriteriaExtractor PREDEFINED_CE_EXTRACTOR;

    RegulatedCriteriaService(EDMVersion version) {
        PREDEFINED_CA_EXTRACTOR = new RegulatedCriteriaExtractorBuilder(version)
                .withContractingOperator(ContractingOperatorEnum.CONTRACTING_AUTHORITY)
                .build();
        PREDEFINED_CE_EXTRACTOR = new RegulatedCriteriaExtractorBuilder(version)
                .withContractingOperator(ContractingOperatorEnum.CONTRACTING_ENTITY)
                .build();
    }

    public static RegulatedCriteriaService getV1Instance() {
        return V1;
    }

    public static RegulatedCriteriaService getV2Instance() {
        return V2;
    }

    @Override
    public List<SelectableCriterion> getCriteria(ContractingOperatorEnum contractingOperatorEnum) throws RetrieverException {
        CriteriaExtractor predefinedExtractor =
                contractingOperatorEnum.equals(ContractingOperatorEnum.CONTRACTING_AUTHORITY) ? PREDEFINED_CA_EXTRACTOR
                        : PREDEFINED_CE_EXTRACTOR;
        return markAsSelected(generateUUIDs(predefinedExtractor.getFullList()));
    }

    @Override
    public List<SelectableCriterion> getUnselectedCriteria(List<SelectableCriterion> initialList,
                                                           ContractingOperatorEnum contractingOperatorEnum) throws RetrieverException {
        CriteriaExtractor predefinedExtractor =
                contractingOperatorEnum.equals(ContractingOperatorEnum.CONTRACTING_AUTHORITY) ? PREDEFINED_CA_EXTRACTOR
                        : PREDEFINED_CE_EXTRACTOR;
        return predefinedExtractor.getFullList(initialList);
    }

    @Override
    public CriteriaType[] getCriteriaFilters() {
        return CriteriaType.values();
    }
}

