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
package eu.esens.espdvcd.builder.schema.v1;

import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.RequirementType;
import eu.espd.schema.v1.commonaggregatecomponents_2.DocumentReferenceType;
import eu.espd.schema.v1.commonaggregatecomponents_2.ProcurementProjectLotType;
import eu.espd.schema.v1.commonbasiccomponents_2.ContractFolderIDType;
import eu.espd.schema.v1.commonbasiccomponents_2.CopyIndicatorType;
import eu.espd.schema.v1.commonbasiccomponents_2.DescriptionType;
import eu.espd.schema.v1.commonbasiccomponents_2.IDType;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import java.util.stream.Collectors;

public class ESPDRequestSchemaExtractorV1 implements SchemaExtractorV1 {

    public ESPDRequestType extractESPDRequestType(ESPDRequest req) {

        ESPDRequestType reqType = new ESPDRequestType();

        if (req.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
            reqType.setContractFolderID(new ContractFolderIDType());
            reqType.getContractFolderID().setSchemeAgencyID("TeD");
            reqType.getContractFolderID().setValue(req.getCADetails().getProcurementProcedureFileReferenceNo());
        }

        reqType.getAdditionalDocumentReference().add(extractCADetailsDocumentReference(req.getCADetails()));

        // 2018-03-20 UL: add capabilities to handle National Official Journal
        DocumentReferenceType drt = extractCADetailsNationalDocumentReference(req.getCADetails());
        if (drt != null) {
            reqType.getAdditionalDocumentReference().add(drt);
        }

        reqType.setContractingParty(extractContractingPartyType(req.getCADetails()));
        reqType.setServiceProviderParty(extractServiceProviderPartyType(req.getServiceProviderDetails()));
        reqType.getCriterion().addAll(req.getFullCriterionList().stream()
                .filter(cr -> cr.isSelected())
                .map(cr -> extractCriterion(cr))
                .collect(Collectors.toList()));

        reqType.setUBLVersionID(createUBL21VersionIdType());
        reqType.setCustomizationID(createBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrns070:ver3.0"));
        reqType.setVersionID(createVersionIDType("2017.01.01"));

        //Procurement Project Lot is always 0 in Request and not part of the UI
        ProcurementProjectLotType pplt = new ProcurementProjectLotType();
        pplt.setID(new IDType());
        pplt.getID().setValue("0");
        pplt.getID().setSchemeAgencyID("EU-COM-GROW");
        reqType.getProcurementProjectLot().add(pplt);

        reqType.setCopyIndicator(new CopyIndicatorType());
        reqType.getCopyIndicator().setValue(false);

        return reqType;
    }

    @Override
    public RequirementType extractRequirementType(Requirement r) {

        RequirementType req = new RequirementType();

        req.setResponseDataType(r.getResponseDataType().name());
        req.setDescription(new DescriptionType());
        req.getDescription().setValue(r.getDescription());
        req.setID(createCriterionRelatedIDType(r.getID()));

        return req;
    }

}
