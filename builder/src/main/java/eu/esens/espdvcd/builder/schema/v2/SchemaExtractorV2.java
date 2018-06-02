package eu.esens.espdvcd.builder.schema.v2;

import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
//import test.x.ubl.pre_award.commonaggregate.*;
//import test.x.ubl.pre_award.commonbasic.*;
//import test.x.ubl.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;
import eu.espd.schema.v2.pre_award.commonaggregate.*;
import eu.espd.schema.v2.pre_award.commonbasic.*;
import eu.espd.schema.v2.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;

import java.util.stream.Collectors;

public interface SchemaExtractorV2 {

    /**
     * TenderingCriterionPropertyType replaces
     * {@link eu.espd.schema.v1.ccv_commonaggregatecomponents_1.RequirementType}
     * in schema version 2.0.x
     */
    TenderingCriterionPropertyType extractTenderingCriterionPropertyType(Requirement r, QualificationApplicationResponseType responseType);

    default TenderingCriterionType extractTenderingCriterion(Criterion c, QualificationApplicationResponseType responseType) {

        TenderingCriterionType tct = new TenderingCriterionType();

        if (c.getLegislationReference() != null) {
            tct.getLegislation().add(extractLegislationType(c.getLegislationReference()));
        }

        if (c.getDescription() != null) {
            tct.getDescription().add(new DescriptionType());
            tct.getDescription().get(0).setValue(c.getDescription());
        }

        tct.setID(createCriteriaIDType(c.getID()));

        tct.setName(new NameType());
        tct.getName().setValue(c.getName());

        tct.setCriterionTypeCode(createCriteriaTypeCode(c.getTypeCode()));

        tct.getTenderingCriterionPropertyGroup().addAll(c.getRequirementGroups().stream()
                .map(rg -> extractTenderingCriterionPropertyGroupType(rg, responseType))
                .collect(Collectors.toList()));

        return tct;
    }

    default LegislationType extractLegislationType(LegislationReference lr) {

        LegislationType lt = new LegislationType();
        lt.getJurisdictionLevel().add(createJurisdictionLevel(lr.getJurisdictionLevelCode()));

        lt.getArticle().add(new ArticleType());
        lt.getArticle().get(0).setValue(lr.getArticle());

        lt.getDescription().add(new DescriptionType());
        lt.getDescription().get(0).setValue(lr.getDescription());

        lt.getURI().add(new URIType());
        lt.getURI().get(0).setValue(lr.getURI());

        lt.getTitle().add(new TitleType());
        lt.getTitle().get(0).setValue(lr.getTitle());

        return lt;

    }

    // This is RequirementGroup equivalent in schema version 2.0.x
    default TenderingCriterionPropertyGroupType extractTenderingCriterionPropertyGroupType(RequirementGroup rg, QualificationApplicationResponseType responseType) {

        TenderingCriterionPropertyGroupType rgType = new TenderingCriterionPropertyGroupType();

        //TODO: Apply Defaults() method for adding default attributes etc
        rgType.getSubsidiaryTenderingCriterionPropertyGroup().addAll(rg.getRequirementGroups().stream()
                .map(rg1 -> extractTenderingCriterionPropertyGroupType(rg1, responseType))
                .collect(Collectors.toList()));
        // This is Requirement equivalent in schema version 2.0.x
        rgType.getTenderingCriterionProperty().addAll(rg.getRequirements().stream()
                .map(r1 -> extractTenderingCriterionPropertyType(r1, responseType))
                .collect(Collectors.toList()));

        rgType.setID(createDefaultIDType(rg.getID()));

        // RequirementGroup "PI" attribute: the "processing instruction" attribute is not defined in UBL-2.2.
        // Instead, if needed, use the "cbc:PropertyGroupTypeCode" component
        rgType.setPropertyGroupTypeCode(createPropertyGroupTypeCodeType(rg.getCondition()));

        return rgType;
    }

    /* @TODO code has to be checked again */
    default DocumentReferenceType extractCADetailsDocumentReference(CADetails cd) {

        DocumentReferenceType dr = new DocumentReferenceType();

        if (cd != null) {

            if (cd.getProcurementPublicationNumber() != null) {
                dr.setID(createGROWTemporaryId(cd.getProcurementPublicationNumber()));
            }

            dr.setDocumentTypeCode(createDocumentTypeCode("TED_CN"));

            //dr.setDocumentType(createDocumentType("")); // to be filled with official description, when available

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

                // 2018-03-20 UL: modifications to add capabilities to handle Received Notice Number
                if ((cd.getProcurementProcedureDesc() != null && !cd.getProcurementProcedureDesc().isEmpty()) ||
                        (cd.getReceivedNoticeNumber() != null && !cd.getReceivedNoticeNumber().isEmpty())) {
                    DescriptionType dt = new DescriptionType();
                    dt.setValue(cd.getProcurementProcedureDesc() != null ? cd.getProcurementProcedureDesc() : "");
                    if (dr.getAttachment().getExternalReference() == null) {
                        dr.getAttachment().setExternalReference(new ExternalReferenceType());
                    }
                    dr.getAttachment().getExternalReference().getDescription().add(0, dt);

                    if (cd.getReceivedNoticeNumber() != null && !cd.getReceivedNoticeNumber().isEmpty()) {
                        DescriptionType dtRNN = new DescriptionType();
                        dtRNN.setValue(cd.getReceivedNoticeNumber());
                        dr.getAttachment().getExternalReference().getDescription().add(1, dtRNN);
                    }
                }
            }
        }
        return dr;
    }

    default ProcurementProjectLotType extractProcurementProjectLot(EODetails eoDetails) {

        ProcurementProjectLotType pplt = new ProcurementProjectLotType();
        pplt.setID(new IDType());

        pplt.getID().setValue((eoDetails.getProcurementProjectLot() == null) ||
                eoDetails.getProcurementProjectLot().isEmpty() ? "0" : eoDetails.getProcurementProjectLot());

        pplt.getID().setSchemeAgencyID("EU-COM-GROW");

        return pplt;
    }

    /* @TODO code has to be checked again */
    default DocumentReferenceType extractCADetailsNationalDocumentReference(CADetails cd) {

        DocumentReferenceType dr = null;

        if (cd != null && cd.getNationalOfficialJournal() != null && !cd.getNationalOfficialJournal().isEmpty()) {

            dr = new DocumentReferenceType();

            if (cd.getProcurementPublicationNumber() != null) {
                dr.setID(createGROWNationalJournalId(cd.getNationalOfficialJournal()));
            }

            dr.setDocumentTypeCode(createDocumentTypeCode("NGOJ"));

            //dr.setDocumentType(createDocumentType("")); // to be filled with official description, when available

        }
        return dr;
    }

    /* @TODO code has to be checked again */
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


        if (cd.getID() != null) {
            PartyIdentificationType pit = new PartyIdentificationType();
            pit.setID(new IDType());
            pit.getID().setValue(cd.getID());
            pit.getID().setSchemeAgencyID("EU-COM-GROW");
            pt.getPartyIdentification().add(pit);
        }

        //FIXME: SCHEMEID ON CONTRACTINGPARTY>PARTY>ENDPOINTID MAY BE INCORRECT
        // UBL syntax path: cac:ContractingParty.Party.EndpointID
        if (cd.getElectronicAddressID() != null) {
            EndpointIDType eid = new EndpointIDType();
            eid.setSchemeAgencyID("EU-COM-GROW");
            eid.setValue(cd.getElectronicAddressID());
            eid.setSchemeID("ISO/IEC 9834-8:2008 - 4UUID");
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

            //at.setPostbox(new PostboxType());
            //at.getPostbox().setValue(cd.getPostalAddress().getPostCode());
            // UL 2017-10-10: write post code to cbc:PostalZone
            at.setPostalZone(new PostalZoneType());
            at.getPostalZone().setValue(cd.getPostalAddress().getPostCode());

            at.setCountry(new CountryType());
            at.getCountry().setIdentificationCode(createISOCountryIdCodeType(cd.getPostalAddress().getCountryCode()));
            at.getCountry().getIdentificationCode().setListID("CountryCodeIdentifier");
            cpp.getParty().setPostalAddress(at);
        }

        if (cd.getContactingDetails() != null) {
            // UBL syntax path: cac:ContractingParty.Party.Contact

            ContactType ct = new ContactType();
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

    default ServiceProviderPartyType extractServiceProviderPartyType(ServiceProviderDetails spd) {
        if (spd == null) {
            return null;
        }

        ServiceProviderPartyType sppt = new ServiceProviderPartyType();
        PartyType pt = new PartyType();
        sppt.setParty(pt);

        //FIXME: SCHEMEID ON SERVICEPROVIDERPARTYTYPE MAY BE INCORRECT
        if (spd.getEndpointID() != null) {
            EndpointIDType eid = new EndpointIDType();
            eid.setSchemeAgencyID("EU-COM-GROW");
            eid.setSchemeID("ISO/IEC 9834-8:2008 - 4UUID");
            eid.setValue(spd.getEndpointID());
            pt.setEndpointID(eid);
        }

        if (spd.getID() != null) {
            PartyIdentificationType pid = new PartyIdentificationType();
            IDType idt = new IDType();
            idt.setSchemeAgencyID("EU-COM-GROW");
            idt.setValue(spd.getID());
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

    /* @TODO code has to be checked again */
    default IDType createGROWTemporaryId(String id) {
        // mod 2018-01-16: changed schemeID to "ISO/IEC 9834-8:2008 - 4UUID" according to ESPD 1.0.2 EDM
        // remark: the DG GROW system uses "COM-GROW-TEMPORARY-ID", if no valid OJS number is entered
        //IDType reqGroupIDType = createCustomSchemeIDIDType(id, "COM-GROW-TEMPORARY-ID");
        IDType reqGroupIDType = createCustomSchemeIDIDType(id, "ISO/IEC 9834-8:2008 - 4UUID");

        reqGroupIDType.setSchemeAgencyID("EU-COM-GROW");
        reqGroupIDType.setSchemeAgencyName("DG GROW (European Commission)");
        reqGroupIDType.setSchemeVersionID("1.1");
        return reqGroupIDType;
    }

    /* @TODO code has to be checked again */
    default DocumentTypeCodeType createDocumentTypeCode(String code) {
        DocumentTypeCodeType dtc = new DocumentTypeCodeType();
        dtc.setListAgencyID("EU-COM-GROW");
        dtc.setListID("ReferencesTypeCodes");
        dtc.setListVersionID("2.0.1");
        // modification UL_2016-12-22: updated list version
        //dtc.setListVersionID("1.0.2");

        dtc.setValue(code);
        return dtc;
    }

    /* @TODO code has to be checked again */
    default IDType createGROWNationalJournalId(String id) {
        IDType reqGroupIDType = createCustomSchemeIDIDType(id, null);
        reqGroupIDType.setSchemeAgencyID("EU-COM-GROW");
        reqGroupIDType.setSchemeAgencyName("DG GROW (European Commission)");
        reqGroupIDType.setSchemeVersionID("1.1");
        return reqGroupIDType;
    }

    // (2.0.2) updated by KR_20-5-2018
    default ValidatedCriterionPropertyIDType createValidatedCriterionPropertyId(String id) {
        ValidatedCriterionPropertyIDType propertyIDType = new ValidatedCriterionPropertyIDType();
        propertyIDType.setValue(id);
        propertyIDType.setSchemeID("CriteriaTaxonomy");
        propertyIDType.setSchemeAgencyID("EU-COM-GROW");
        propertyIDType.setSchemeVersionID("2.0.2");
        return propertyIDType;
    }

    // (2.0.1) updated KR_1-4-2018
    default IdentificationCodeType createISOCountryIdCodeType(String id) {
        IdentificationCodeType countryCodeType = new IdentificationCodeType();
        countryCodeType.setListAgencyID("ISO");
        countryCodeType.setListVersionID("1.0");
        countryCodeType.setListName("CountryCodeIdentifier");
//        countryCodeType.setListName("ISO-1-ALPHA-2");
        countryCodeType.setValue(id);
        return countryCodeType;
    }

    // (2.0.1) updated by KR_1-4-2018
    default VersionIDType createVersionIDType(String id) {
        VersionIDType versionID = new VersionIDType();
        versionID.setSchemeAgencyID("EU-COM-GROW");
        versionID.setSchemeVersionID("2.0.1");
        versionID.setValue(id);
        return versionID;
    }

    // (2.0.1) updated by KR_1-4-2018
    default IDType createISOIECIDType(String id) {
        IDType reqGroupIDType = createCustomSchemeIDIDType(id, "ISO/IEC 9834-8:2008 - 4UUID");
        reqGroupIDType.setSchemeAgencyName("DG GROW (European Commission)");
        reqGroupIDType.setSchemeVersionID("2.0");
        return reqGroupIDType;
    }

    /**
     * (2.0.1) updated by KR_1-4-2018
     * replaces {@link eu.esens.espdvcd.builder.schema.v1.SchemaExtractorV1#createCriterionRelatedIDType(String)}
     */
    default IDType createCriteriaTaxonomyIDType(String id) {
        IDType reqGroupIDType = createCustomSchemeIDIDType(id, "CriteriaTaxonomy");
        return reqGroupIDType;
    }

    // (2.0.1) updated by KR_19-4-2018
    default UUIDType createISOIECUUIDType(String id) {
        UUIDType uuidType = new UUIDType();
        uuidType.setSchemeID("ISO/IEC 9834-8:2008 - 4UUID");
        uuidType.setSchemeAgencyName("EU-COM-GROW");
        uuidType.setSchemeVersionID("2.0");
        uuidType.setSchemeAgencyID("EU-COM-GROW");

        uuidType.setValue(id);
        return uuidType;
    }

    /**
     * Local id refers to cbc:ID 2.0.x QualificationApplicationRequest element and
     * represent the identifier of this document generally generated by the systems
     * that creates the ESPD
     * <p>
     * (2.0.1) updated by KR_26-4-2018
     *
     * @return
     */
    default IDType createLocalIDType(String id) {
        // FIXME schemeId value here may have to be changed (temporary value has been applied)
        IDType localIDType = new IDType();
        localIDType.setSchemeAgencyID("DGPE");
        localIDType.setValue(id);
        return localIDType;
    }

    // (2.0.1) updated by KR_1-4-2018
    default CriterionTypeCodeType createCriteriaTypeCode(String code) {
        CriterionTypeCodeType tc = new CriterionTypeCodeType();
        tc.setListAgencyID("EU-COM-GROW");
        tc.setListID("CriteriaTypeCode");
        tc.setListVersionID("2.0.1");
        tc.setValue(code);
        return tc;
    }

    default DocumentTypeType createDocumentType(String type) {
        DocumentTypeType dtt = new DocumentTypeType();
        dtt.setValue(type);
        return dtt;
    }

    // (2.0.1) updated by KR_1-4-2018
    default UBLVersionIDType createUBL22VersionIdType() {
        UBLVersionIDType id = new UBLVersionIDType();
        id.setSchemeAgencyID("OASIS-UBL-TC");
        id.setValue("2.2");
        return id;
    }

    // (2.0.1) updated by KR_1-4-2018
    default CustomizationIDType createCENBIICustomizationIdType(String id) {
        CustomizationIDType cid = new CustomizationIDType();
        cid.setSchemeName("CustomizationID");
        cid.setSchemeAgencyID("CEN-BII");
        cid.setSchemeVersionID("1.0");
        cid.setValue(id);
        return cid;
    }

    // (2.0.1) updated by KR_1-4-2018
    default JurisdictionLevelType createJurisdictionLevel(String code) {
        JurisdictionLevelType jlt = new JurisdictionLevelType();
        // FIXME: not sure if setLanguageID is mandatory here
        jlt.setLanguageID("en");
        jlt.setValue(code);
        return jlt;
    }

    // (2.0.1) updated by KR_1-4-2018
    default IDType createCriteriaIDType(String id) {
        IDType reqGroupIDType = createCustomSchemeIDIDType(id, "CriteriaTaxonomy");
        return reqGroupIDType;
    }

    // (2.0.1) updated by KR_1-4-2018
    default IDType createCustomSchemeIDIDType(String id, String schemeId) {
        IDType reqGroupIDType = createDefaultIDType(id);
        if (schemeId != null) {
            reqGroupIDType.setSchemeID(schemeId);
        }
        return reqGroupIDType;
    }

    // (2.0.1) updated by KR_1-4-2018
    default IDType createDefaultIDType(String id) {
        IDType reqGroupIDType = new IDType();
        reqGroupIDType.setSchemeAgencyID("EU-COM-GROW");
        reqGroupIDType.setSchemeVersionID("2.0.1");
        reqGroupIDType.setValue(id);
        return reqGroupIDType;
    }

    // (2.0.1) updated by KR_1-4-2018
    default PropertyGroupTypeCodeType createPropertyGroupTypeCodeType(String code) {
        PropertyGroupTypeCodeType ptc = new PropertyGroupTypeCodeType();
        ptc.setListID("PropertyGroupType");
        ptc.setListAgencyID("EU-COM-GROW");
        ptc.setListVersionID("2.0.1");
        ptc.setValue(code);
        return ptc;
    }

    default IndustryClassificationCodeType createIndustryClassificationCodeType(String code) {
        IndustryClassificationCodeType icct = new IndustryClassificationCodeType();
        icct.setListID("EOIndustryClassificationCode");
        icct.setListAgencyID("EU-COM-GROW");
        icct.setListVersionID("2.0.1");
        icct.setValue(code);
        return icct;
    }

}
