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

import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.espd.schema.v2.pre_award.commonaggregate.DocumentReferenceType;
import eu.espd.schema.v2.pre_award.commonaggregate.ProcurementProjectLotType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionPropertyType;
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
        } else {
            qarType.setContractFolderID(createContractFolderIDType("PPID-test1"));
        }

        qarType.getAdditionalDocumentReference().add(extractCADetailsDocumentReference(modelRequest.getCADetails()));

        // apply global weighting info
        qarType.getWeightScoringMethodologyNote().addAll(modelRequest.getCADetails()
                .getWeightScoringMethodologyNoteList().stream()
                .map(note -> createWeightScoringMethodologyNoteType(note))
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
                .filter(cr -> cr.isSelected())
                .map(cr -> extractTenderingCriterion(cr))
                .collect(Collectors.toList()));

        // create a Map<Key = Criterion ID, Value = The Criterion> in order to use it during criteria level weighting info extraction
//        Map<String, SelectableCriterion> criterionMap = modelRequest.getFullCriterionList().stream()
//                .collect(Collectors.toMap(sc -> sc.getID(), Function.identity()));

        // apply criteria weighting info
//        qarType.getTenderingCriterion().forEach(criterionType -> {
//            /* the logic here is: find the 1st rq with WEIGHT_INDICATOR. That rq contains the TenderingCriterionType level weighting info */
//            SelectableCriterion sc = criterionMap.get(criterionType.getID().getValue());
//            if (sc != null) {
//
//            }
//        });

        qarType.setUBLVersionID(createUBL22VersionIdType());
        qarType.setCustomizationID(createCENBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrdm070:ver3.0"));
        // FIXME: version id should be updated here
        qarType.setVersionID(createVersionIDType("2018.01.01"));
        qarType.setProfileExecutionID(createProfileExecutionIDType(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED));

        qarType.setQualificationApplicationTypeCode(createQualificationApplicationTypeCodeType(QualificationApplicationTypeEnum.REGULATED));

        //Procurement Project Lot is always 0 in Request and not part of the UI
        ProcurementProjectLotType pplt = new ProcurementProjectLotType();
        pplt.setID(new IDType());
        pplt.getID().setValue("0");
        pplt.getID().setSchemeAgencyID("EU-COM-GROW");
        qarType.getProcurementProjectLot().add(pplt);

        qarType.setCopyIndicator(new CopyIndicatorType());
        qarType.getCopyIndicator().setValue(false);

        return qarType;
    }

    @Override
    public TenderingCriterionPropertyType extractTenderingCriterionPropertyType(Requirement rq) {

        TenderingCriterionPropertyType propertyType = new TenderingCriterionPropertyType();

        // tbr070-013
        propertyType.setID(createCriteriaTaxonomyIDType(rq.getID()));
        // tbr070-013
        propertyType.getDescription().add(new DescriptionType());
        propertyType.getDescription().get(0).setValue(rq.getDescription());
        // tbr070-013
        // FIXME (SELF-CONTAINED 2.0.2) The Regulated ESPD documents do not specify REQUIREMENTS, only QUESTIONS. The SELF-CONTAINED version does
        propertyType.setTypeCode(new TypeCodeType());
        propertyType.getTypeCode().setValue(rq.getType().name());

        propertyType.getTypeCode().setListID("CriterionElementType");
        propertyType.getTypeCode().setListAgencyID("EU-COM-GROW");
        propertyType.getTypeCode().setListVersionID("2.0.2");

        // tbr070-013
        propertyType.setValueDataTypeCode(new ValueDataTypeCodeType());
        propertyType.getValueDataTypeCode().setValue(rq.getResponseDataType().name());
        propertyType.getValueDataTypeCode().setListID("ResponseDataType");
        propertyType.getValueDataTypeCode().setListAgencyID("EU-COM-GROW");
        propertyType.getValueDataTypeCode().setListVersionID("2.0.2");

        return propertyType;
    }
}
