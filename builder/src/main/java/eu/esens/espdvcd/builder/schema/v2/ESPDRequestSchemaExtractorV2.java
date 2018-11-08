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
package eu.esens.espdvcd.builder.schema.v2;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.WeightIndicatorResponse;
import eu.espd.schema.v2.pre_award.commonaggregate.DocumentReferenceType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionPropertyType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionType;
import eu.espd.schema.v2.pre_award.commonbasic.*;
import eu.espd.schema.v2.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ESPDRequestSchemaExtractorV2 implements SchemaExtractorV2 {

    public QualificationApplicationRequestType extractQualificationApplicationRequestType(ESPDRequest modelRequest) {

        QualificationApplicationRequestType qarType = new QualificationApplicationRequestType();

        if (modelRequest.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
            qarType.setContractFolderID(new ContractFolderIDType());
            qarType.getContractFolderID().setSchemeAgencyID("TeD");
            qarType.getContractFolderID().setValue(modelRequest.getCADetails().getProcurementProcedureFileReferenceNo());
        }

        if (modelRequest.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
            qarType.setContractFolderID(createContractFolderIDType(modelRequest.getCADetails().getProcurementProcedureFileReferenceNo()));
        }

        qarType.getAdditionalDocumentReference().add(extractCADetailsDocumentReference(modelRequest.getCADetails()));

        // apply global weighting info
        qarType.getWeightScoringMethodologyNote().addAll(modelRequest.getCADetails()
                .getWeightScoringMethodologyNoteList().stream()
                .map(this::createWeightScoringMethodologyNoteType)
                .collect(Collectors.toList()));

        if (modelRequest.getCADetails().getWeightingType() != null) {
            qarType.setWeightingTypeCode(createWeightingTypeCodeType(modelRequest
                    .getCADetails().getWeightingType()));
        }

        DocumentReferenceType drt = extractCADetailsNationalDocumentReference(modelRequest.getCADetails());
        if (drt != null) {
            qarType.getAdditionalDocumentReference().add(drt);
        }

        qarType.getContractingParty().add(extractContractingPartyType(modelRequest.getCADetails()));
        qarType.getContractingParty().get(0).getParty().getServiceProviderParty().add(extractServiceProviderPartyType(modelRequest.getServiceProviderDetails()));

        // extract criteria
        qarType.getTenderingCriterion().addAll(modelRequest.getFullCriterionList().stream()
                .filter(SelectableCriterion::isSelected)
                .map(this::extractTenderingCriterion)
                .collect(Collectors.toList()));

        // apply Criterion level weighting info
        Map<String, TenderingCriterionType> criterionTypeMap = qarType.getTenderingCriterion().stream()
                .collect(Collectors.toMap(criterionType -> criterionType.getID().getValue(), Function.identity()));

        modelRequest.getFullCriterionList()
                .forEach(sc -> sc.getRequirementGroups()
                        .forEach(rg -> applyTenderingCriterionWeightingData(rg, criterionTypeMap.get(sc.getID()))));

        qarType.setUBLVersionID(createUBL22VersionIdType());
        qarType.setCustomizationID(createCENBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrdm070:ver3.0"));
        // FIXME: version id should be updated here
        qarType.setVersionID(createVersionIDType("2018.01.01"));

        if (modelRequest.getDocumentDetails() != null
                && modelRequest.getDocumentDetails().getQualificationApplicationType() != null) {
            // Profile Execution ID
            qarType.setProfileExecutionID(createProfileExecutionIDType(modelRequest
                    .getDocumentDetails().getQualificationApplicationType()));
            // Qualification Application Type
            qarType.setQualificationApplicationTypeCode(createQualificationApplicationTypeCodeType(modelRequest
                    .getDocumentDetails().getQualificationApplicationType()));
            // Set Lots
            qarType.getProcurementProjectLot().addAll(createProcurementProjectLotType(modelRequest
                            .getDocumentDetails().getQualificationApplicationType() // REGULATED or SELF-CONTAINED
                    , modelRequest.getCADetails().getProcurementProjectLots()));    // Number of lots
        }
        // Procedure Code
        if (modelRequest.getCADetails() != null
                && modelRequest.getCADetails().getProcurementProcedureType() != null) {
            qarType.setProcedureCode(createProcedureCodeType(modelRequest
                    .getCADetails().getProcurementProcedureType()));
        }

        qarType.setCopyIndicator(new CopyIndicatorType());
        qarType.getCopyIndicator().setValue(false);

        return qarType;
    }

    private void applyTenderingCriterionWeightingData(RequirementGroup rg,
                                                      TenderingCriterionType criterionType) {

        if (criterionType != null) {
            rg.getRequirementGroups()
                    .forEach(subRg -> applyTenderingCriterionWeightingData(subRg, criterionType));

            rg.getRequirements()
                    .forEach(rq -> applyTenderingCriterionWeightingData(rq, criterionType));
        }
    }

    private void applyTenderingCriterionWeightingData(Requirement rq, TenderingCriterionType criterionType) {

        if (rq.getResponseDataType() == ResponseTypeEnum.WEIGHT_INDICATOR) {
            WeightIndicatorResponse weightIndResp = (WeightIndicatorResponse) rq.getResponse();
            applyTenderingCriterionWeightingData(weightIndResp, criterionType);
        }
    }

    @Override
    public TenderingCriterionPropertyType extractTenderingCriterionPropertyType(Requirement rq) {

        TenderingCriterionPropertyType rqType = new TenderingCriterionPropertyType();

        // tbr070-013
        rqType.setID(createCriteriaTaxonomyIDType(rq.getID()));
        // tbr070-013
        rqType.getDescription().add(new DescriptionType());
        rqType.getDescription().get(0).setValue(rq.getDescription());
        // tbr070-013
        rqType.setTypeCode(new TypeCodeType());
        rqType.getTypeCode().setValue(rq.getType().name());

        rqType.getTypeCode().setListID("CriterionElementType");
        rqType.getTypeCode().setListAgencyID("EU-COM-GROW");
        rqType.getTypeCode().setListVersionID("2.0.2");

        // tbr070-013
        rqType.setValueDataTypeCode(new ValueDataTypeCodeType());
        rqType.getValueDataTypeCode().setValue(rq.getResponseDataType().name());
        rqType.getValueDataTypeCode().setListID("ResponseDataType");
        rqType.getValueDataTypeCode().setListAgencyID("EU-COM-GROW");
        rqType.getValueDataTypeCode().setListVersionID("2.0.2");

        applyCAResponseToXML(rq, rqType);

        return rqType;
    }

}
