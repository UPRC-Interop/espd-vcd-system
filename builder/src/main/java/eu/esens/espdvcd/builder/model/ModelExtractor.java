package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.requirement.RequestRequirement;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.RequirementGroup;
import eu.esens.espdvcd.model.SelectableCriterion;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.LegislationType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementGroupType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import java.util.List;
import java.util.stream.Collectors;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ContractingPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ExternalReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ProcurementProjectLotType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ContractFolderIDType;

public interface ModelExtractor {
  
    default CADetails extractCADetails(ContractingPartyType caParty, 
                                       ContractFolderIDType contractFolderId,
                                       ProcurementProjectLotType procurementProjectLot,
                                       List<DocumentReferenceType> additionalDocumentReferenceList) {
        
        CADetails cd = new CADetails();
       
        if (!caParty.getParty().getPartyName().isEmpty()) {
            cd.setCAOfficialName(caParty
                    .getParty().getPartyName()
                    .get(0).getName().getValue());
        } 

        cd.setCACountry(caParty
                .getParty().getPostalAddress().getCountry().getIdentificationCode().getValue());

        if (contractFolderId !=null && contractFolderId.getValue() != null) {
            cd.setProcurementProcedureFileReferenceNo(contractFolderId.getValue());
        }
        if (!additionalDocumentReferenceList.isEmpty()) {
            DocumentReferenceType ref = additionalDocumentReferenceList.get(0);
            if (ref.getID() != null) {
               cd.setProcurementPublicationNumber(ref.getID().getValue());
           }
            if (ref.getAttachment() != null && ref.getAttachment().getExternalReference() != null) {
                ExternalReferenceType ert = ref.getAttachment().getExternalReference();
                cd.setProcurementProcedureTitle(ert.getFileName().getValue());
                if (!ert.getDescription().isEmpty()) {
                    cd.setProcurementProcedureDesc(ert.getDescription().get(0).getValue());
                }
            }
        }
        
        if (procurementProjectLot != null) {
            cd.setProcurementProjectLot(procurementProjectLot.getID().getValue());
        }
        
        return cd;
    }
    
    default SelectableCriterion extractSelectableCriterion(CriterionType ct) {
        String id = ct.getID().getValue();
        String desc = ct.getDescription().getValue();
        String typeCode = ct.getTypeCode().getValue();
        String name = ct.getName().getValue();
        
        //TODO: Extract multiple values
        LegislationReference lr = extractDefaultLegalReference(ct.getLegislationReference());

        
        List<RequirementGroup> rgList =ct.getRequirementGroup().stream()
                .map(t -> extractRequirementGroup(t))
                .collect(Collectors.toList());
                
        SelectableCriterion selCr = new SelectableCriterion(id, typeCode, name, desc, lr, rgList);
        selCr.setSelected(true);
        return selCr;
    }
    
    default RequirementGroup extractRequirementGroup(RequirementGroupType rgType) {
       
        String id = rgType.getID().getValue();
        RequirementGroup rg = new RequirementGroup(id);
        List<Requirement> rList = rgType.getRequirement().stream()
                .map(r -> extractRequirement(r))
                .collect(Collectors.toList());
        List<RequirementGroup> childRg = rgType.getRequirementGroup().stream()
                .map(t -> extractRequirementGroup(t))
                .collect(Collectors.toList());
        rg.setRequirements(rList);
        rg.setRequirementGroups(childRg);
        return rg;
    }
    
    default LegislationReference extractDefaultLegalReference(List<LegislationType> lrList) {
        
        //First check if there is an EU_* jurisdiction
        LegislationReference lr;
        lr = extractEULegalReference(lrList);
        if (lr == null) {
            lr = extractNationalLegalReference(lrList);
        }
        return lr;
    }
    
    default  LegislationReference extractEULegalReference(List<LegislationType> lrList) {
        return lrList.stream()
                .filter(lr -> lr.getJurisdictionLevelCode().getValue().contains("EU_"))
                .findFirst().map(lr -> extractLegalReference(lr))
                .orElse(null);
    }   
    
    default LegislationReference extractNationalLegalReference(List<LegislationType> lrList) {
                return lrList.stream()
                .filter(lr -> lr.getJurisdictionLevelCode().getValue().contains("NATIONAL"))
                .findFirst().map(lr -> extractLegalReference(lr))
                .orElse(null);
    }   
    
    default LegislationReference extractLegalReference(LegislationType lt) {
        LegislationReference lr = new LegislationReference(
                lt.getTitle().getValue(), 
                lt.getDescription().getValue(),
                lt.getJurisdictionLevelCode().getValue(), 
                lt.getArticle().getValue(),
                lt.getURI().getValue());
        return lr;
    }

    default Requirement extractRequirement(RequirementType rt) {
        Requirement r = new RequestRequirement(
                rt.getID().getValue(),
                rt.getResponseDataType(),
                rt.getDescription().getValue());
        return r;
    }    
}
