package eu.esens.espdvcd.builder.schema.v2;

import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.requirement.Requirement;
import test.x.ubl.pre_award.commonaggregate.DocumentReferenceType;
import test.x.ubl.pre_award.commonaggregate.ProcurementProjectLotType;
import test.x.ubl.pre_award.commonaggregate.TenderingCriterionPropertyType;
import test.x.ubl.pre_award.commonbasic.*;
import test.x.ubl.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;

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
                .map(cr -> extractTenderingCriterion(cr))
                .collect(Collectors.toList()));

        reqType.setUBLVersionID(createUBL22VersionIdType());
        reqType.setCustomizationID(createCENBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrdm070:ver3.0"));
        // FIXME: version id should be updated here
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
    public TenderingCriterionPropertyType extractTenderingCriterionPropertyType(Requirement r) {

        TenderingCriterionPropertyType tcpt = new TenderingCriterionPropertyType();

        // FIXME: not sure for the configuration in the next 2 lines
        tcpt.setName(new NameType());
        tcpt.getName().setValue(r.getResponseDataType().name());
        tcpt.getDescription().add(new DescriptionType());
        tcpt.getDescription().get(0).setValue(r.getDescription());
        tcpt.setID(createCriteriaTaxonomyIDType(r.getID()));

        return tcpt;
    }
}
