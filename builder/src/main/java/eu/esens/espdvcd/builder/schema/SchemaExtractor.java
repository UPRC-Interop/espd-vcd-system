package eu.esens.espdvcd.builder.schema;

import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.ServiceProviderDetails;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.LegislationType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementGroupType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import java.util.stream.Collectors;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.*;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.*;


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
        rgType.setPi(rg.getCondition());

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
                    if (cd.getProcurementPublicationURI() != null) {
                        dr.getAttachment().getExternalReference().setURI(new URIType());
                        dr.getAttachment().getExternalReference().getURI().setValue(cd.getProcurementPublicationURI());
                    }

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

        if (cd == null) {
            return null;
        }

        ContractingPartyType cpp = new ContractingPartyType();
        PartyType pt = new PartyType();
        cpp.setParty(pt);

        if (cd.getCAOfficialName() != null) {
            PartyNameType nt = new PartyNameType();
            nt.setName(new NameType());
            nt.getName().setValue(cd.getCAOfficialName());
            pt.getPartyName().add(nt);
        }

        // UL: replaced by the respective PostalAddress model element, see below
        //pt.setPostalAddress(new AddressType());
        //pt.getPostalAddress().setCountry(new CountryType());
        //pt.getPostalAddress().getCountry().setIdentificationCode(createISOCountryIdCodeType(cd.getCACountry()));


        // UBL syntax path: cac:ContractingParty.Party.EndpointID
        if (cd.getElectronicAddressID() != null) {
            EndpointIDType eid = new EndpointIDType();
            eid.setValue(cd.getElectronicAddressID());
            pt.setEndpointID(eid);
        }

        // UBL syntax path: cac:ContractingParty.Party.WebsiteURIID
        if (cd.getWebSiteURI() != null) {
            WebsiteURIType wsuri = new WebsiteURIType();
            wsuri.setValue(cd.getWebSiteURI());
            pt.setWebsiteURI(wsuri);
        }

        if (cd.getPostalAddress() != null) {
            // UBL syntax path: cac:ContractingParty.Party.PostalAddress

            AddressType at = new AddressType();

            at.setStreetName(new StreetNameType());
            at.getStreetName().setValue(cd.getPostalAddress().getAddressLine1());

            at.setCityName(new CityNameType());
            at.getCityName().setValue(cd.getPostalAddress().getCity());

            at.setPostbox(new PostboxType());
            at.getPostbox().setValue(cd.getPostalAddress().getPostCode());

            at.setCountry(new CountryType());
            // FIXME: the country should be set using this model element; for compatibility the old method cd.getCACountry() is used
            //at.getCountry().setIdentificationCode(createISOCountryIdCodeType(cd.getPostalAddress().getCountryCode()));
            at.getCountry().setIdentificationCode(createISOCountryIdCodeType(cd.getCACountry()));

            cpp.getParty().setPostalAddress(at);
        }

        if (cd.getContactingDetails() != null) {
            // UBL syntax path: cac:ContractingParty.Party.Contact

            oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ContactType ct = new ContactType();
            ct.setName(new NameType());
            ct.getName().setValue(cd.getContactingDetails().getContactPointName());

            ct.setTelephone(new TelephoneType());
            ct.getTelephone().setValue(cd.getContactingDetails().getTelephoneNumber());

            ct.setElectronicMail(new ElectronicMailType());
            ct.getElectronicMail().setValue(cd.getContactingDetails().getEmailAddress());

            ct.setTelefax(new TelefaxType());
            ct.getTelefax().setValue(cd.getContactingDetails().getFaxNumber());

            cpp.getParty().setContact(ct);
        }

        return cpp;
    }

    default ServiceProviderPartyType  extractServiceProviderPartyType(ServiceProviderDetails spd) {
        if (spd == null) {
            return null;
        }

        ServiceProviderPartyType sppt = new ServiceProviderPartyType();
        PartyType pt = new PartyType();
        sppt.setParty(pt);

        if (spd.getEndpointID() != null) {
            EndpointIDType eid = new EndpointIDType();
            eid.setValue(spd.getEndpointID());
            pt.setEndpointID(eid);
        }

        if (spd.getId() != null) {
            PartyIdentificationType pid = new PartyIdentificationType();
            IDType idt = new IDType();
            idt.setValue(spd.getId());
            pid.setID(idt);
            pt.getPartyIdentification().add(pid);
        }

        if (spd.getName() != null) {
            PartyNameType pnt = new PartyNameType();
            NameType nt = new NameType();
            nt.setValue(spd.getName());
            pnt.setName(nt);
            pt.getPartyName().add(pnt);
        }

        if (spd.getWebsiteURI() != null) {
            WebsiteURIType wsuri = new WebsiteURIType();
            wsuri.setValue(spd.getWebsiteURI());
            pt.setWebsiteURI(wsuri);
        }

        return sppt;
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
        //countryCodeType.setListAgencyID("ISO");
        // modification UL_2016-12-22: updated ListAgencyID
        countryCodeType.setListAgencyID("EU-COM-GROW");

        //Updated to follow ESPD Service 2017.01.01 release
        countryCodeType.setListName("CountryCodeIdentifier");
        //countryCodeType.setListVersionID("1.0");
        // modification UL_2016-12-22: updated list version and added listID
        countryCodeType.setListVersionID("1.0.2");
        countryCodeType.setListID("CountryCodeIdentifier");


        countryCodeType.setValue(id);

        return countryCodeType;
    }

    default TypeCodeType createJurisdictionLevelCode(String code) {
        TypeCodeType tc = new TypeCodeType();
        tc.setListAgencyID("EU-COM-GROW");
        //tc.setListID("CriterionJurisdictionLevelCode");
        // modification UL_2016-12-22: new listID
        tc.setListID("CriterionJurisdictionLevel");

        //tc.setListVersionID("1.0");
        // modification UL_2016-12-22: updated list version
        tc.setListVersionID("1.0.2");

        tc.setValue(code);
        return tc;
    }

    default TypeCodeType createCriteriaTypeCode(String code) {
        TypeCodeType tc = new TypeCodeType();
        tc.setListAgencyID("EU-COM-GROW");
        tc.setListID("CriteriaTypeCode");
        //tc.setListVersionID("1.0");
        // modification UL_2016-12-22: updated list version
        tc.setListVersionID("1.0.2");

        tc.setValue(code);
        return tc;
    }

    default DocumentTypeCodeType createDocumentTypeCode(String code) {
        DocumentTypeCodeType dtc = new DocumentTypeCodeType();
        dtc.setListAgencyID("EU-COM-GROW");
        dtc.setListID("ReferencesTypeCodes");
        dtc.setListVersionID("1.0");
        // modification UL_2016-12-22: updated list version
        //dtc.setListVersionID("1.0.2");

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


    default ProcurementProjectLotType extractProcurementProjectLot(EODetails eoDetails) {

        ProcurementProjectLotType pplt = new ProcurementProjectLotType();
        pplt.setID(new IDType());

        //pplt.getID().setValue(caDetails.getProcurementProjectLot());
        // modification UL_2016-12-21: according to ESPD specification 1.0.2, procurement project lot needs to be "0"
        // and attribute schemeAgencyID needs to be set
        pplt.getID().setValue((eoDetails.getProcurementProjectLot() == null) ||
                eoDetails.getProcurementProjectLot().isEmpty() ? "0" : eoDetails.getProcurementProjectLot());
        pplt.getID().setSchemeAgencyID("EU-COM-GROW");

        return pplt;
    }
}
