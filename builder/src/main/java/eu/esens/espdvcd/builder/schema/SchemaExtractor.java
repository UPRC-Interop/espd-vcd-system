package eu.esens.espdvcd.builder.schema;

import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
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
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ProcurementProjectLotType;
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

public interface SchemaExtractor {

    public RequirementType extractRequirementType(Requirement r);

    default CriterionType extractCriterion(Criterion c) {

        CriterionType ct = new CriterionType();

        if (c.getLegislationReference() != null) {
            ct.getLegislationReference().add(extractLegislationType(c.getLegislationReference()));
        }

        if (c.getDescription() != null) {
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

    default RequirementGroupType extractRequirementGroupType(RequirementGroup rg) {

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

    default LegislationType extractLegislationType(LegislationReference lr) {

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

    default DocumentReferenceType extractCADetailsDocumentReferece(CADetails cd) {

        DocumentReferenceType dr = new DocumentReferenceType();

        if (cd != null) {

            if (cd.getProcurementPublicationNumber() != null) {
                dr.setID(createGROWTemporaryId(cd.getProcurementPublicationNumber()));
            }

            dr.setDocumentTypeCode(createDocumentTypeCode("TED_CN"));

            if (cd.getProcurementProcedureTitle() != null || cd.getProcurementProcedureDesc() != null) {
                dr.setAttachment(new AttachmentType());

                if (cd.getProcurementProcedureTitle() != null) {
                    dr.getAttachment().setExternalReference(new ExternalReferenceType());
                    dr.getAttachment().getExternalReference().setFileName(new FileNameType());
                    dr.getAttachment().getExternalReference().getFileName().setValue(cd.getProcurementProcedureTitle());
                }

                if (cd.getProcurementProcedureDesc() != null) {
                    DescriptionType dt = new DescriptionType();
                    dt.setValue(cd.getProcurementProcedureDesc());
                    if (dr.getAttachment().getExternalReference() == null ) {
                      dr.getAttachment().setExternalReference(new ExternalReferenceType());
                    }
                    dr.getAttachment().getExternalReference().getDescription().add(dt);
                }
            }
        }
        return dr;
    }

    default ContractingPartyType extractContractingPartyType(CADetails cd) {

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

    default IDType createDefaultIDType(String id) {

        IDType reqGroupIDType = new IDType();
        reqGroupIDType.setSchemeAgencyID("EU-COM-GROW");
        reqGroupIDType.setSchemeVersionID("1.0");
        reqGroupIDType.setValue(id);
        return reqGroupIDType;
    }

    default VersionIDType createVersionIDType(String id) {

        VersionIDType versionID = new VersionIDType();
        versionID.setSchemeAgencyID("EU-COM-GROW");
        versionID.setValue(id);
        return versionID;
    }

    default IDType createCriterionRelatedIDType(String id) {
        IDType reqGroupIDType = createCustomSchemeIDIDType(id, "CriterionRelatedIDs");
        return reqGroupIDType;
    }

    default IDType createCriteriaIDType(String id) {
        IDType reqGroupIDType = createCustomSchemeIDIDType(id, "CriteriaID");
        return reqGroupIDType;
    }

    default IDType createISOIECIDType(String id) {
        IDType reqGroupIDType = createCustomSchemeIDIDType(id, "ISO/IEC 9834-8:2008 - 4UUID");
        reqGroupIDType.setSchemeAgencyName("DG GROW (European Commission)");
        reqGroupIDType.setSchemeVersionID("1.1");
        return reqGroupIDType;
    }
    
    default IDType createGROWTemporaryId(String id) {
        IDType reqGroupIDType = createCustomSchemeIDIDType(id, "COM-GROW-TEMPORARY-ID");
        reqGroupIDType.setSchemeAgencyID("EU-COM-GROW");
        reqGroupIDType.setSchemeAgencyName("DG GROW (European Commission)");
        reqGroupIDType.setSchemeVersionID("1.1");
        return reqGroupIDType;
    }

    default IdentificationCodeType createISOCountryIdCodeType(String id) {

        IdentificationCodeType countryCodeType = new IdentificationCodeType();
        countryCodeType.setListAgencyID("ISO");
        countryCodeType.setListName("ISO 3166-1");
        countryCodeType.setListVersionID("1.0");
        countryCodeType.setValue(id);

        return countryCodeType;
    }

    default TypeCodeType createJurisdictionLevelCode(String code) {
        TypeCodeType tc = new TypeCodeType();
        tc.setListAgencyID("EU-COM-GROW");
        tc.setListID("CriterionJurisdictionLevelCode");
        tc.setListVersionID("1.0");
        tc.setValue(code);
        return tc;
    }

    default TypeCodeType createCriteriaTypeCode(String code) {
        TypeCodeType tc = new TypeCodeType();
        tc.setListAgencyID("EU-COM-GROW");
        tc.setListID("CriteriaTypeCode");
        tc.setListVersionID("1.0");
        tc.setValue(code);
        return tc;
    }

    default DocumentTypeCodeType createDocumentTypeCode(String code) {
        DocumentTypeCodeType dtc = new DocumentTypeCodeType();
        dtc.setListAgencyID("EU-COM-GROW");
        dtc.setListID("ReferencesTypeCodes");
        dtc.setListVersionID("1.0");
        dtc.setValue(code);
        return dtc;
    }

    default IDType createCustomSchemeIDIDType(String id, String schemeId) {
        IDType reqGroupIDType = createDefaultIDType(id);
        reqGroupIDType.setSchemeID(schemeId);
        return reqGroupIDType;
    }

    default UBLVersionIDType createUBL21VersionIdType() {

        UBLVersionIDType id = new UBLVersionIDType();

        id.setSchemeAgencyID("OASIS-UBL-TC");
        id.setValue("2.1");
        return id;

    }

    default CustomizationIDType createBIICustomizationIdType(String id) {

        CustomizationIDType cid = new CustomizationIDType();

        cid.setSchemeName("CustomizationID");
        cid.setSchemeAgencyID("BII");
        cid.setSchemeVersionID("3.0");
        cid.setValue(id);

        return cid;

    }

    default ProcurementProjectLotType extractProcurementProjectLot(CADetails caDetails) {

        ProcurementProjectLotType pplt = new ProcurementProjectLotType();
        pplt.setID(new IDType());
        pplt.getID().setValue(caDetails.getProcurementProjectLot());
        return pplt;
    }
}
