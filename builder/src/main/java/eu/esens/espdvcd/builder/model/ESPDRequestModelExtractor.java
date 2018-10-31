/**
 * Copyright 2016-2018 University of Piraeus Research Center
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
package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.RegulatedESPDRequest;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.WeightIndicatorResponse;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionType;
import eu.espd.schema.v2.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ESPDRequestModelExtractor implements ModelExtractor {

    /* package private constructor. Create only through factory */
    ESPDRequestModelExtractor() {
    }

    public ESPDRequest extractESPDRequest(ESPDRequestType reqType) {

        RegulatedESPDRequest req = new RegulatedESPDRequest();

        req.getFullCriterionList().addAll(reqType.getCriterion().stream()
                .map(c -> extractSelectableCriterion(c))
                .collect(Collectors.toList()));
        req.setCADetails(extractCADetails(reqType.getContractingParty(),
                reqType.getContractFolderID(),
                reqType.getAdditionalDocumentReference()));

        req.setServiceProviderDetails(extractServiceProviderDetails(reqType.getServiceProviderParty()));

        return req;
    }

    public ESPDRequest extractESPDRequest(QualificationApplicationRequestType qarType) {

        RegulatedESPDRequest modelRequest = new RegulatedESPDRequest();

        modelRequest.getFullCriterionList().addAll(qarType.getTenderingCriterion().stream()
                .map(c -> extractSelectableCriterion(c))
                .collect(Collectors.toList()));

        modelRequest.setCADetails(extractCADetails(qarType.getContractingParty(),
                qarType.getContractFolderID(),
                qarType.getAdditionalDocumentReference()));

        // apply global weighting
        modelRequest.getCADetails().getWeightScoringMethodologyNoteList()
                .addAll(qarType.getWeightScoringMethodologyNote().stream()
                        .map(noteType -> noteType.getValue())
                        .collect(Collectors.toList()));

        if (qarType.getWeightingTypeCode() != null
                && qarType.getWeightingTypeCode().getValue() != null) {
            modelRequest.getCADetails().setWeightingType(qarType.getWeightingTypeCode().getValue());
        }

        // apply criterion level weighting
        Map<String, TenderingCriterionType> criterionTypeMap = qarType.getTenderingCriterion().stream()
                .collect(Collectors.toMap(criterionType -> criterionType.getID().getValue(), Function.identity()));

        modelRequest.getFullCriterionList()
                .forEach(sc -> sc.getRequirementGroups()
                        .forEach(rg -> applyCriterionWeightingData(rg, criterionTypeMap.get(sc.getID()))));

        modelRequest.setServiceProviderDetails(extractServiceProviderDetails(qarType.getContractingParty()));

        return modelRequest;
    }

    private void applyCriterionWeightingData(RequirementGroup rg,
                                             TenderingCriterionType criterionType) {

        if (criterionType != null) {
            rg.getRequirementGroups()
                    .forEach(subRg -> applyCriterionWeightingData(subRg, criterionType));

            rg.getRequirements()
                    .forEach(rq -> applyCriterionWeightingData(rq, criterionType));
        }

    }

    private void applyCriterionWeightingData(Requirement rq, TenderingCriterionType criterionType) {

        if (rq.getResponseDataType() == ResponseTypeEnum.WEIGHT_INDICATOR) {
            WeightIndicatorResponse weightIndResp = new WeightIndicatorResponse();
            applyCriterionWeightingData(weightIndResp, criterionType);
            rq.setResponse(weightIndResp);
        }
    }

}
