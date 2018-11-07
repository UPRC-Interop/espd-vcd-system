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
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.WeightIndicatorResponse;
import eu.espd.schema.v2.pre_award.commonaggregate.*;
import eu.espd.schema.v2.pre_award.commonbasic.*;
import eu.espd.schema.v2.unqualifieddatatypes_2.CodeType;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

public interface SchemaExtractorV2 {

    TenderingCriterionPropertyType extractTenderingCriterionPropertyType(Requirement rq);

    default TenderingCriterionType extractTenderingCriterion(Criterion c) {

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
                .map(rg -> extractTenderingCriterionPropertyGroupType(rg))
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

    default void applyTenderingCriterionWeightingData(WeightIndicatorResponse response, TenderingCriterionType criterionType) {

        if (response != null) {
            // EvaluationMethodTypeCode
            if (response.getEvaluationMethodType() != null) {
                criterionType.setEvaluationMethodTypeCode((createEvaluationMethodTypeCodeType(response
                        .getEvaluationMethodType())));
            }
            // WeightingConsiderationDescription
            criterionType.getWeightingConsiderationDescription().addAll(response
                    .getEvaluationMethodDescriptionList().stream()
                    .map(desc -> createWeightingConsiderationDescriptionType(desc))
                    .collect(Collectors.toList()));
            // Weight
            criterionType.setWeightNumeric(createWeightNumericType(response.getWeight()));
        }
    }

    default TenderingCriterionPropertyGroupType extractTenderingCriterionPropertyGroupType(RequirementGroup rg) {

        TenderingCriterionPropertyGroupType rgType = new TenderingCriterionPropertyGroupType();

        rgType.getSubsidiaryTenderingCriterionPropertyGroup().addAll(rg.getRequirementGroups().stream()
                .map(rg1 -> extractTenderingCriterionPropertyGroupType(rg1))
                .collect(Collectors.toList()));

        rgType.getTenderingCriterionProperty().addAll(rg.getRequirements().stream()
                .map(r1 -> extractTenderingCriterionPropertyType(r1))
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
            } else {
                dr.setID(createGROWTemporaryId("0000/S 000-000000"));
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
                if ((cd.getProcurementProcedureDesc() != null && !cd.getProcurementProcedureDesc().isEmpty())
                        || (cd.getReceivedNoticeNumber() != null && !cd.getReceivedNoticeNumber().isEmpty())) {

                    DescriptionType dt = new DescriptionType();

                    dt.setValue(cd.getProcurementProcedureDesc() != null
                            ? cd.getProcurementProcedureDesc()
                            : "_"); // dummy content for the first description


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

        if (spd.getWebsiteURI() != null) {
            WebsiteURIType wsuri = new WebsiteURIType();
            wsuri.setValue(spd.getWebsiteURI());
            pt.setWebsiteURI(wsuri);
        }

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

        if (spd.getPostalAddress() != null) {
            AddressType addressType = new AddressType();
            addressType.setCountry(new CountryType());
            addressType.getCountry().setIdentificationCode(new IdentificationCodeType());
            addressType.getCountry().getIdentificationCode().setListID("CountryCodeIdentifier");
            addressType.getCountry().getIdentificationCode().setListAgencyID("ISO");
            addressType.getCountry().getIdentificationCode().setListVersionID("1.0");
            addressType.getCountry().getIdentificationCode().setValue(spd.getPostalAddress().getCountryCode());
            pt.setPostalAddress(addressType);
        }

        return sppt;
    }

    /* @TODO code has to be checked again */
    default IDType createGROWTemporaryId(String id) {
        // mod 2018-01-16: changed schemeID to "ISO/IEC 9834-8:2008 - 4UUID" according to ESPD 1.0.2 EDM
        // remark: the DG GROW system uses "COM-GROW-TEMPORARY-ID", if no valid OJS number is entered
        //IDType reqGroupIDType = createCustomSchemeIDIDType(id, "COM-GROW-TEMPORARY-ID");
        IDType idType = createCustomSchemeIDIDType(id, "ISO/IEC 9834-8:2008 - 4UUID");
        idType.setSchemeAgencyID("EU-COM-GROW");
        idType.setSchemeAgencyName("DG GROW (European Commission)");
        idType.setSchemeVersionID("1.1");
        return idType;
    }

    /* @TODO code has to be checked again */
    default DocumentTypeCodeType createDocumentTypeCode(String code) {
        DocumentTypeCodeType dtc = new DocumentTypeCodeType();
        dtc.setListAgencyID("EU-COM-GROW");
        dtc.setListID("ReferencesTypeCodes");
        dtc.setListVersionID("2.0.2");
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

    default ValidatedCriterionPropertyIDType createValidatedCriterionPropertyId(String id) {
        ValidatedCriterionPropertyIDType propertyIDType = new ValidatedCriterionPropertyIDType();
        propertyIDType.setSchemeID("CriteriaTaxonomy");
        propertyIDType.setSchemeAgencyID("EU-COM-GROW");
        propertyIDType.setSchemeVersionID("2.0.2");
        propertyIDType.setValue(id);
        return propertyIDType;
    }

    default ConfidentialityLevelCodeType createConfidentialityLevelCode(String code) {
        ConfidentialityLevelCodeType codeType = new ConfidentialityLevelCodeType();
        codeType.setValue(code);
        codeType.setListID("ConfidentialityLevel");
        codeType.setListAgencyID("EU-COM-GROW");
        codeType.setListVersionID("2.0.2");
        return codeType;
    }

    default IdentificationCodeType createISOCountryIdCodeType(String id) {
        IdentificationCodeType countryCodeType = new IdentificationCodeType();
        countryCodeType.setListAgencyID("ISO");
        countryCodeType.setListVersionID("1.0");
        countryCodeType.setListID("CountryCodeIdentifier");
        countryCodeType.setListName("CountryCodeIdentifier");
        countryCodeType.setValue(id);
        return countryCodeType;
    }

    default VersionIDType createVersionIDType(String id) {
        VersionIDType versionID = new VersionIDType();
        versionID.setSchemeAgencyID("EU-COM-GROW");
        versionID.setSchemeVersionID("2.0.2");
        versionID.setValue(id);
        return versionID;
    }

    default IDType createISOIECIDType(String id) {
        IDType idType = createCustomSchemeIDIDType(id, "ISO/IEC 9834-8:2008 - 4UUID");
        idType.setSchemeAgencyName("DG GROW (European Commission)");
        idType.setSchemeVersionID("1.1");
        return idType;
    }

    default IDType createCriteriaTaxonomyIDType(String id) {
        IDType idType = createCustomSchemeIDIDType(id, "CriteriaTaxonomy");
        return idType;
    }

    default UUIDType createISOIECUUIDType(String id) {
        UUIDType uuidType = new UUIDType();
        uuidType.setSchemeID("ISO/IEC 9834-8:2008 - 4UUID");
        uuidType.setSchemeVersionID("2.0");
        uuidType.setSchemeAgencyID("EU-COM-GROW");
        uuidType.setValue(id);
        return uuidType;
    }

    default CriterionTypeCodeType createCriteriaTypeCode(String code) {
        CriterionTypeCodeType tc = new CriterionTypeCodeType();
        tc.setListAgencyID("EU-COM-GROW");
        tc.setListID("CriteriaTypeCode");
        tc.setListVersionID("2.0.2");
        tc.setValue(code);
        return tc;
    }

    default DocumentTypeType createDocumentType(String type) {
        DocumentTypeType dtt = new DocumentTypeType();
        dtt.setValue(type);
        return dtt;
    }

    default UBLVersionIDType createUBL22VersionIdType() {
        UBLVersionIDType ublvIdType = new UBLVersionIDType();
        ublvIdType.setSchemeAgencyID("OASIS-UBL-TC");
        ublvIdType.setValue("2.2");
        return ublvIdType;
    }

    default CustomizationIDType createCENBIICustomizationIdType(String id) {
        CustomizationIDType cid = new CustomizationIDType();
        cid.setSchemeName("CustomizationID");
        cid.setSchemeAgencyID("CEN-BII");
        cid.setSchemeVersionID("2.0");
        cid.setValue(id);
        return cid;
    }

    default JurisdictionLevelType createJurisdictionLevel(String code) {
        JurisdictionLevelType jlType = new JurisdictionLevelType();
        // FIXME: not sure if setLanguageID is mandatory here
        jlType.setLanguageID("en");
        jlType.setValue(code);
        return jlType;
    }

    default IDType createCriteriaIDType(String id) {
        IDType idType = createCustomSchemeIDIDType(id, "CriteriaTaxonomy");
        return idType;
    }

    default IDType createCustomSchemeIDIDType(String id, String schemeId) {
        IDType idType = createDefaultIDType(id);
        if (schemeId != null) {
            idType.setSchemeID(schemeId);
        }
        return idType;
    }

    default IDType createDefaultIDType(String id) {
        IDType idType = new IDType();
        idType.setSchemeAgencyID("EU-COM-GROW");
        idType.setSchemeVersionID("2.0.2");
        idType.setValue(id);
        return idType;
    }

    default CodeType createCustomListIDCodeType(String code, String listId) {
        CodeType codeType = createDefaultCodeType(code);
        codeType.setListID(listId);
        return codeType;
    }

    default CodeType createDefaultCodeType(String code) {
        CodeType codeType = new CodeType();
        codeType.setListAgencyID("EU-COM-GROW");
        codeType.setListVersionID("2.0.2");
        codeType.setValue(code);
        return codeType;
    }

    default PropertyGroupTypeCodeType createPropertyGroupTypeCodeType(String code) {
        PropertyGroupTypeCodeType ptc = new PropertyGroupTypeCodeType();
        ptc.setListID("PropertyGroupType");
        ptc.setListAgencyID("EU-COM-GROW");
        ptc.setListVersionID("2.0.2");
        ptc.setValue(code);
        return ptc;
    }

    default IndustryClassificationCodeType createIndustryClassificationCodeType(String code) {
        IndustryClassificationCodeType icCodeType = new IndustryClassificationCodeType();
        icCodeType.setListID("EOIndustryClassificationCode");
        icCodeType.setListAgencyID("EU-COM-GROW");
        icCodeType.setListVersionID("2.0.2");
        icCodeType.setValue(code);
        return icCodeType;
    }

    default ValueDataTypeCodeType createValueDataTypeCodeType(String code) {
        ValueDataTypeCodeType vdTypeCode = new ValueDataTypeCodeType();
        vdTypeCode.setListID("ResponseDataType");
        vdTypeCode.setListAgencyID("EU-COM-GROW");
        vdTypeCode.setListVersionID("2.0.2");
        vdTypeCode.setValue(code);
        return vdTypeCode;
    }

    default TypeCodeType createTypeCodeType(String code) {
        TypeCodeType tCodeType = new TypeCodeType();
        tCodeType.setListID("CriterionElementType");
        tCodeType.setListAgencyID("EU-COM-GROW");
        tCodeType.setListVersionID("2.0.2");
        tCodeType.setValue(code);
        return tCodeType;
    }

    /**
     * Compulsory use of the code list QualificationApplicationType.
     *
     * @param code
     * @return
     */
    default QualificationApplicationTypeCodeType createQualificationApplicationTypeCodeType(QualificationApplicationTypeEnum code) {
        QualificationApplicationTypeCodeType qaTypeCode = new QualificationApplicationTypeCodeType();
        qaTypeCode.setValue(code.name());
        qaTypeCode.setListID("QualificationApplicationType");
        qaTypeCode.setListAgencyID("EU-COM-GROW");
        qaTypeCode.setListVersionID("2.0.2");
        return qaTypeCode;
    }

    /**
     * Compulsory use of the CodeList ProfileExecutionID.
     *
     * @param id
     * @return
     */
    default ProfileExecutionIDType createProfileExecutionIDType(ProfileExecutionIDEnum id) {
        ProfileExecutionIDType peIdType = new ProfileExecutionIDType();
        peIdType.setSchemeAgencyID("EU-COM-GROW");
        peIdType.setSchemeVersionID("2.0.2");
        peIdType.setValue(id.getValue());
        return peIdType;
    }

    default ContractFolderIDType createContractFolderIDType(String id) {
        ContractFolderIDType cfIdType = new ContractFolderIDType();
        cfIdType.setSchemeAgencyID("TeD");
        cfIdType.setValue(id);
        return cfIdType;
    }

    default WeightingConsiderationDescriptionType createWeightingConsiderationDescriptionType(String desc) {
        WeightingConsiderationDescriptionType descType = new WeightingConsiderationDescriptionType();
        descType.setValue(desc);
        return descType;
    }

    default EvaluationMethodTypeCodeType createEvaluationMethodTypeCodeType(String code) {
        EvaluationMethodTypeCodeType codeType = new EvaluationMethodTypeCodeType();
        codeType.setValue(code);
        return codeType;
    }

    default WeightNumericType createWeightNumericType(BigDecimal weight) {
        WeightNumericType numericType = new WeightNumericType();
        numericType.setValue(weight);
        return numericType;
    }

    default WeightScoringMethodologyNoteType createWeightScoringMethodologyNoteType(String note) {
        WeightScoringMethodologyNoteType noteType = new WeightScoringMethodologyNoteType();
        noteType.setValue(note);
        return noteType;
    }

    default WeightingTypeCodeType createWeightingTypeCodeType(String code) {
        WeightingTypeCodeType codeType = new WeightingTypeCodeType();
        codeType.setListID("ResponseDataType");
        codeType.setListAgencyID("EU-COM-GROW");
        codeType.setListVersionID("2.0.2");
        codeType.setValue(code);
        return codeType;
    }

    default ResponseValueType createResponseValueType() {
        ResponseValueType valueType = new ResponseValueType();
        valueType.setID(createDefaultIDType(UUID.randomUUID().toString()));
        valueType.getID().setSchemeID("ISO/IEC 9834-8:2008 - 4UUID");
        return valueType;
    }

    default ResponseIndicatorType createResponseIndicatorType(boolean indicator) {
        ResponseIndicatorType indicatorType = new ResponseIndicatorType();
        indicatorType.setValue(indicator);
        return indicatorType;
    }

}
