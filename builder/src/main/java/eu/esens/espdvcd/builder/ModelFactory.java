package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.Requirement;
import eu.esens.espdvcd.model.RequirementGroup;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.SimpleESPDRequest;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.LegislationType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementGroupType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */
public interface ModelFactory {
    
    public static SimpleESPDRequest extractESPDRequest(ESPDRequestType reqType) {
        
        SimpleESPDRequest req = new SimpleESPDRequest();
        req.getFullCriterionList().addAll(reqType.getCriterion().stream()
        .map(c -> extractSelectableCriterion(c))
        .collect(Collectors.toList()));
        
        req.setCADetails(extractCADetails(reqType));
        
        return req;
    }
    
    public static CADetails extractCADetails(ESPDRequestType reqType) {
        
        CADetails cd = new CADetails();
        
       
        if (!reqType.getContractingParty()
                .getParty().getPartyName().isEmpty()) {
            cd.setCAOfficialName(reqType.getContractingParty()
                    .getParty().getPartyName()
                    .get(0).getName().getValue());
        } 

        cd.setCACountry(reqType.getContractingParty()
                .getParty().getPostalAddress().getCountry().getIdentificationCode().getValue());

        //TODO: Need to add null checks here
        cd.setProcurementProcedureFileReferenceNo(reqType.getContractFolderID().getValue());
        cd.setProcurementProcedureTitle(reqType.getAdditionalDocumentReference().get(0).getAttachment().getExternalReference().getFileName().getValue());
        cd.setProcurementProcedureDesc(reqType.getAdditionalDocumentReference().get(0).getAttachment().getExternalReference().getDescription().get(0).getValue());
        
        return cd;
        
    }
    
    public static SelectableCriterion extractSelectableCriterion(CriterionType ct) {
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
        return selCr;
    }
    
    public static RequirementGroup extractRequirementGroup(RequirementGroupType rgType) {
       
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
    
    public static LegislationReference extractDefaultLegalReference(List<LegislationType> lrList) {
        
        //First check if there is an EU_* jurisdiction
        LegislationReference lr;
        lr = extractEULegalReference(lrList);
        if (lr == null) {
            lr = extractNationalLegalReference(lrList);
        }
        return lr;
    }
    
    public static LegislationReference extractEULegalReference(List<LegislationType> lrList) {
        return lrList.stream()
                .filter(lr -> lr.getJurisdictionLevelCode().getValue().contains("EU_"))
                .findFirst().map(lr -> extractLegalReference(lr))
                .orElse(null);
    }   
    
    public static LegislationReference extractNationalLegalReference(List<LegislationType> lrList) {
                return lrList.stream()
                .filter(lr -> lr.getJurisdictionLevelCode().getValue().contains("NATIONAL"))
                .findFirst().map(lr -> extractLegalReference(lr))
                .orElse(null);
    }   
    
    public static LegislationReference extractLegalReference(LegislationType lt) {
        LegislationReference lr = new LegislationReference(
                lt.getTitle().getValue(), 
                lt.getDescription().getValue(),
                lt.getJurisdictionLevelCode().getValue(), 
                lt.getArticle().getValue(),
                lt.getURI().getValue());
        return lr;
    }

    public static Requirement extractRequirement(RequirementType rt) {
        Requirement r = new Requirement(
                rt.getID().getValue(),
                rt.getResponseDataType(),
                rt.getDescription().getValue());
        return r;
    }
}
