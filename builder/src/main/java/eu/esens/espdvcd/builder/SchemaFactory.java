package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.Requirement;
import eu.esens.espdvcd.model.RequirementGroup;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.LegislationType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementGroupType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import java.util.stream.Collectors;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AttachmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ContractingPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ExternalReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ContractFolderIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CustomizationIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.FileNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IdentificationCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TextType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.UBLVersionIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.URIType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.VersionIDType;

/**
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */
public interface SchemaFactory {

    public static CriterionType extractCriterion(Criterion c) {
        
        CriterionType ct = new CriterionType();
        
        if (c.getLegislationReference() != null)
            ct.getLegislationReference().add(extractLegislationType(c.getLegislationReference()));

        if (c.getDescription() != null ) {
            ct.setDescription(new DescriptionType());
            ct.getDescription().setValue(c.getDescription());
        }
        
        ct.setID(createCriteriaIDType(c.getID()));

        ct.setName(new NameType());
        ct.getName().setValue(c.getName());
        
        ct.setTypeCode(createCriteriaTypeCode(c.getTypeCode()));
        
        ct.getRequirementGroup().addAll(c.getRequirementGroups().stream()
                .map(rg -> extractRequirementGroupType(rg))
        .collect(Collectors.toList()));
        
        return ct;
    }
    
    public static RequirementType extractRequirementType(Requirement r) {

        RequirementType req = new RequirementType();

        req.setResponseDataType(r.getResponseDataType());
        req.setDescription(new DescriptionType());
        req.getDescription().setValue(r.getDescription());
        req.setID(createCriterionRelatedIDType(r.getID()));

        return req;
    }

    public static RequirementGroupType extractRequirementGroupType(RequirementGroup rg) {

        RequirementGroupType rgType = new RequirementGroupType();

        //TODO: Apply Defaults() method for adding default attributes etc
        rgType.getRequirementGroup().addAll(rg.getRequirementGroups().stream()
                .map(rg1 -> extractRequirementGroupType(rg1))
                .collect(Collectors.toList()));

        rgType.getRequirement().addAll(rg.getRequirements().stream()
                .map(r1 -> extractRequirementType(r1))
                .collect(Collectors.toList()));
        
        rgType.setID(createDefaultIDType(rg.getID()));

        return rgType;
    }

    public static LegislationType extractLegislationType(LegislationReference lr) {

        LegislationType lt = new LegislationType();
        lt.setJurisdictionLevelCode(createJurisdictionLevelCode(lr.getJurisdictionLevelCode()));

        lt.setArticle(new TextType());
        lt.getArticle().setValue(lr.getArticle());

        lt.setDescription(new DescriptionType());
        lt.getDescription().setValue(lr.getDescription());

        lt.setURI(new URIType());
        lt.getURI().setValue(lr.getURI());

        lt.setTitle(new TextType());
        lt.getTitle().setValue(lr.getTitle());

        return lt;

    }
    public static DocumentReferenceType extractCADetailsDocumentReferece(CADetails cd) {

        DocumentReferenceType dr = new DocumentReferenceType();
        
        if (cd != null && cd.getProcurementProcedureTitle() != null) {
         

            dr.setID(createISOIECIDType(""));
            dr.setDocumentTypeCode(createDocumentTypeCode("TED_CN"));

            dr.setAttachment(new AttachmentType());
            dr.getAttachment().setExternalReference(new ExternalReferenceType());
            dr.getAttachment().getExternalReference().setFileName(new FileNameType());

                dr.getAttachment().getExternalReference().getFileName().setValue(cd.getProcurementProcedureTitle());

            DescriptionType dt = new DescriptionType();
            dt.setValue(cd.getProcurementProcedureDesc());
            dr.getAttachment().getExternalReference().getDescription().add(dt);
        }
        return dr;
    }
    
    public static ESPDRequestType extractESPDRequestType(ESPDRequest req) {
    
        ESPDRequestType reqType = new ESPDRequestType();
        if (req.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
            reqType.setContractFolderID(new ContractFolderIDType());
            reqType.getContractFolderID().setSchemeAgencyID("TeD");                   
            reqType.getContractFolderID().setValue(req.getCADetails().getProcurementProcedureFileReferenceNo());
        }
        
        reqType.getAdditionalDocumentReference().add(extractCADetailsDocumentReferece(req.getCADetails()));
        reqType.setContractingParty(extractContractingPartyType(req.getCADetails()));
        reqType.getCriterion().addAll(req.getFullCriterionList().stream()
                .filter(cr -> cr.isSelected())
                .map(cr -> extractCriterion(cr))
                .collect(Collectors.toList()));
        return reqType;
    }
    
    
    public static ContractingPartyType extractContractingPartyType(CADetails cd) {
        
        ContractingPartyType cpp = new ContractingPartyType();
        PartyType pt = new PartyType();
        PartyNameType nt = new PartyNameType();
        nt.setName(new NameType());
        nt.getName().setValue(cd.getCAOfficialName());
        pt.getPartyName().add(nt);
        
        pt.setPostalAddress(new AddressType());
        pt.getPostalAddress().setCountry(new CountryType());
        pt.getPostalAddress().getCountry().setIdentificationCode(createISOCountryIdCodeType(cd.getCACountry()));
        cpp.setParty(pt);
        
        return cpp;      
    }

    public static IDType createDefaultIDType(String id) {

        IDType reqGroupIDType = new IDType();
        reqGroupIDType.setSchemeAgencyID("EU-COM-GROW");
        reqGroupIDType.setSchemeVersionID("1.0");
        reqGroupIDType.setValue(id);
        return reqGroupIDType;
    }
    
    public static VersionIDType createVersionIDType(String id) {

        VersionIDType versionID = new VersionIDType();
        versionID.setSchemeAgencyID("EU-COM-GROW");
        versionID.setValue(id);
        return versionID;
    }

    public static IDType createCriterionRelatedIDType(String id) {
        IDType reqGroupIDType = createCustomSchemeIDIDType(id, "CriterionRelatedIDs");
        return reqGroupIDType;
    }
    
    public static IDType createCriteriaIDType(String id) {
        IDType reqGroupIDType = createCustomSchemeIDIDType(id, "CriteriaID");
        return reqGroupIDType;
    }
    
     public static IDType createISOIECIDType(String id) {
        IDType reqGroupIDType = createCustomSchemeIDIDType(id, "ISO/IEC 9834-8:2008 - 4UUID");
        reqGroupIDType.setSchemeAgencyName("DG GROW (European Commission)");
        reqGroupIDType.setSchemeVersionID("1.1");
        return reqGroupIDType;
    }
    
    public static IdentificationCodeType createISOCountryIdCodeType(String id) {
        
        IdentificationCodeType countryCodeType = new IdentificationCodeType();
        countryCodeType.setListAgencyID("ISO");
        countryCodeType.setListName("ISO 3166-1");
        countryCodeType.setListVersionID("1.0");
        countryCodeType.setValue(id);

        return countryCodeType;
    }

    public static TypeCodeType createJurisdictionLevelCode(String code) {
        TypeCodeType tc = new TypeCodeType();
        tc.setListAgencyID("EU-COM-GROW");
        tc.setListID("CriterionJurisdictionLevelCode");
        tc.setListVersionID("1.0");
        tc.setValue(code);
        return tc;
    }
    
    public static TypeCodeType createCriteriaTypeCode(String code) {
        TypeCodeType tc = new TypeCodeType();
        tc.setListAgencyID("EU-COM-GROW");
        tc.setListID("CriteriaTypeCode");
        tc.setListVersionID("1.0");
        tc.setValue(code);
        return tc;
    }
    
    public static DocumentTypeCodeType createDocumentTypeCode(String code) {
        DocumentTypeCodeType dtc = new DocumentTypeCodeType();
        dtc.setListAgencyID("EU-COM-GROW");
        dtc.setListID("ReferencesTypeCodes");
        dtc.setListVersionID("1.0");
        dtc.setValue(code);
        return dtc;
    }
            
    public static IDType createCustomSchemeIDIDType(String id, String schemeId) {
        IDType reqGroupIDType = createDefaultIDType(id);
        reqGroupIDType.setSchemeID(schemeId);
        return reqGroupIDType;
    }
    
    public static UBLVersionIDType createUBL21VersionIdType() {
        
        UBLVersionIDType id = new UBLVersionIDType();
        
        id.setSchemeAgencyID("OASIS-UBL-TC");
        id.setValue("2.1");       
        return id;
        
    }

    public static CustomizationIDType createBIICustomizationIdType(String id) {

        CustomizationIDType cid = new CustomizationIDType();
        
        cid.setSchemeName("CustomizationID");
        cid.setSchemeAgencyID("BII");
        cid.setSchemeVersionID("1.0");
        cid.setValue(id);
        
        return cid;
        
    }
}
