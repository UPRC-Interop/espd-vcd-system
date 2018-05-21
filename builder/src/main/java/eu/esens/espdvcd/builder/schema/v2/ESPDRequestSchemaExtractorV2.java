package eu.esens.espdvcd.builder.schema.v2;

import eu.esens.espdvcd.codelist.enums.CriterionElementTypeEnum;
import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.requirement.Requirement;
import test.x.ubl.pre_award.commonaggregate.DocumentReferenceType;
import test.x.ubl.pre_award.commonaggregate.ProcurementProjectLotType;
import test.x.ubl.pre_award.commonaggregate.TenderingCriterionPropertyType;
import test.x.ubl.pre_award.commonbasic.*;
import test.x.ubl.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;
import test.x.ubl.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;

import java.util.stream.Collectors;

public class ESPDRequestSchemaExtractorV2 implements SchemaExtractorV2 {

    public QualificationApplicationRequestType extractQualificationApplicationRequestType(ESPDRequest req) {

        QualificationApplicationRequestType reqType = new QualificationApplicationRequestType();

        if (req.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
            reqType.setContractFolderID(new ContractFolderIDType());
            reqType.getContractFolderID().setSchemeAgencyID("TeD");
            reqType.getContractFolderID().setValue(req.getCADetails().getProcurementProcedureFileReferenceNo());
        }

        reqType.getAdditionalDocumentReference().add(extractCADetailsDocumentReference(req.getCADetails()));

        DocumentReferenceType drt = extractCADetailsNationalDocumentReference(req.getCADetails());
        if (drt != null) {
            reqType.getAdditionalDocumentReference().add(drt);
        }

        reqType.getContractingParty().add(extractContractingPartyType(req.getCADetails()));
        reqType.getContractingParty().get(0).getParty().getServiceProviderParty().add(extractServiceProviderPartyType(req.getServiceProviderDetails()));

        reqType.getTenderingCriterion().addAll(req.getFullCriterionList().stream()
                .filter(cr -> cr.isSelected())
                .map(cr -> extractTenderingCriterion(cr, null))
                .collect(Collectors.toList()));

        reqType.setUBLVersionID(createUBL22VersionIdType());
        reqType.setCustomizationID(createCENBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrdm070:ver3.0"));
        // FIXME: version id should be updated here
        reqType.setVersionID(createVersionIDType("2017.01.01"));
        reqType.setProfileExecutionID(new ProfileExecutionIDType());
        reqType.getProfileExecutionID().setSchemeAgencyID("EU-COM-GROW");
        reqType.getProfileExecutionID().setSchemeVersionID("2.0.1");
        // FIXME (REGULATED / SELF-CONTAINED 2.0.1) Compulsory use of the CodeList ProfileExecutionID. Use the value "EU-COM-GROW" for th SchemeAgencyID attribute
        reqType.getProfileExecutionID().setValue("ESPD-EDMv2.0.1-REGULATED");
        // FIXME hardcoded value has to be replaced here
        reqType.setQualificationApplicationTypeCode(new QualificationApplicationTypeCodeType());
        reqType.getQualificationApplicationTypeCode().setValue(QualificationApplicationTypeEnum.REGULATED.name());
        reqType.getQualificationApplicationTypeCode().setListID("QualificationApplicationType");
        reqType.getQualificationApplicationTypeCode().setListAgencyID("EU-COM-GROW");
        reqType.getQualificationApplicationTypeCode().setListVersionID("2.0.2");

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
    public TenderingCriterionPropertyType extractTenderingCriterionPropertyType(Requirement r, QualificationApplicationResponseType responseType) {

        TenderingCriterionPropertyType propertyType = new TenderingCriterionPropertyType();

        // tbr070-013
        propertyType.setID(createCriteriaTaxonomyIDType(r.getID()));
        // tbr070-013
        propertyType.getDescription().add(new DescriptionType());
        propertyType.getDescription().get(0).setValue(r.getDescription());
        // tbr070-013
        // FIXME (SELF-CONTAINED 2.0.1) The Regulated ESPD documents do not specify REQUIREMENTS, only QUESTIONS. The SELF-CONTAINED version does
        propertyType.setTypeCode(new TypeCodeType());

        if (r.getTypeCode() != null) {
            propertyType.getTypeCode().setValue(r.getTypeCode().name());
        } else { // FIXME temporary fix to avoid NullPointerException when importFrom(V1.0.2 artefact)
            propertyType.getTypeCode().setValue(CriterionElementTypeEnum.QUESTION.name());
        }

        propertyType.getTypeCode().setListID("CriterionElementType");
        propertyType.getTypeCode().setListAgencyID("EU-COM-GROW");
        propertyType.getTypeCode().setListVersionID("2.0.1");

        // tbr070-013
        propertyType.setValueDataTypeCode(new ValueDataTypeCodeType());
        propertyType.getValueDataTypeCode().setValue(r.getResponseDataType().name());
        propertyType.getValueDataTypeCode().setListID("ResponseDataType");
        propertyType.getValueDataTypeCode().setListAgencyID("EU-COM-GROW");
        propertyType.getValueDataTypeCode().setListVersionID("2.0.1");

        return propertyType;
    }
}
