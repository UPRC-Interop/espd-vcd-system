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

import eu.esens.espdvcd.codelist.enums.*;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.*;
import eu.esens.espdvcd.schema.enums.EDMSubVersion;
import eu.espd.schema.v2.v210.commonaggregate.*;
import eu.espd.schema.v2.v210.commonbasic.*;
import eu.espd.schema.v2.v210.unqualifieddatatypes_2.CodeType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface SchemaExtractorV2 {

    Logger LOGGER = Logger.getLogger(SchemaExtractorV2.class.getCanonicalName());

    String CURRENT_VERSION_TAG = EDMSubVersion.V210.getTag();

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
            if (response.getWeight() != null) {
                criterionType.setWeightNumeric(createWeightNumericType(response.getWeight()));
            }
        }
    }

    default boolean isLotIdentifier(Requirement rq) {
        return rq.getResponseDataType() != null
                && rq.getResponseDataType() == ResponseTypeEnum.LOT_IDENTIFIER;
    }

    default boolean isRequirementType(Requirement rq) {
        return rq.getType() == RequirementTypeEnum.REQUIREMENT;
    }

    default boolean isQuestionType(Requirement rq) {
        return rq.getType() == RequirementTypeEnum.QUESTION;
    }

    default boolean isLotRequirementType(Requirement rq) {
        return isRequirementType(rq) && isLotIdentifier(rq);
    }

    default boolean isLotQuestionType(Requirement rq) {
        return isQuestionType(rq) && isLotIdentifier(rq);
    }

    default ExpectedIDType createExpectedIDType(String ID) {
        ExpectedIDType idType = new ExpectedIDType();
        idType.setSchemeAgencyID("EU-COM-GROW");
        idType.setValue(ID);
        return idType;
    }

    default List<TenderingCriterionPropertyType> createLotRequirements(Requirement rq) {

        if (isLotRequirementType(rq)
                && rq.getResponse() != null) {
            return ((LotIdentifierResponse) rq.getResponse()).getLotsList().stream()
                    .map(lot -> {
                        TenderingCriterionPropertyType rqType = extractTenderingCriterionPropertyType(rq);
                        rqType.setExpectedID(createExpectedIDType(lot));
                        return rqType;
                    })
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    default TenderingCriterionPropertyGroupType extractTenderingCriterionPropertyGroupType(RequirementGroup rg) {

        TenderingCriterionPropertyGroupType rgType = new TenderingCriterionPropertyGroupType();

        rgType.getSubsidiaryTenderingCriterionPropertyGroup().addAll(rg.getRequirementGroups().stream()
                .map(rg1 -> extractTenderingCriterionPropertyGroupType(rg1))
                .collect(Collectors.toList()));

        List<TenderingCriterionPropertyType> rqTypeList = new ArrayList<>();

        for (Requirement r1 : rg.getRequirements()) {

            // lot Requirement found
            // create all lot TenderingCriterionPropertyTypes
            if (isLotRequirementType(r1)) {
                rqTypeList.addAll(createLotRequirements(r1));
            } else {
                rqTypeList.add(extractTenderingCriterionPropertyType(r1));
            }

        }

        rgType.getTenderingCriterionProperty().addAll(rqTypeList);

        rgType.setID(createDefaultIDType(rg.getID()));
        // RequirementGroup "PI" attribute: the "processing instruction" attribute is not defined in UBL-2.2.
        // Instead, if needed, use the "cbc:PropertyGroupTypeCode" component
        rgType.setPropertyGroupTypeCode(createPropertyGroupTypeCodeType(rg.getCondition()));

        return rgType;
    }

    default DocumentReferenceType extractCADetailsDocumentReference(CADetails caDetails) {

        DocumentReferenceType dr = new DocumentReferenceType();

        if (caDetails != null) {

            if (caDetails.getProcurementPublicationNumber() != null) {
                dr.setID(createGROWTemporaryId(caDetails.getProcurementPublicationNumber()));
            } else {
                dr.setID(createGROWTemporaryId("0000/S 000-000000"));
            }

            dr.setDocumentTypeCode(createDocumentTypeCode("TED_CN"));

            //dr.setDocumentType(createDocumentType("")); // to be filled with official description, when available

            if (caDetails.getProcurementProcedureTitle() != null || caDetails.getProcurementProcedureDesc() != null) {
                dr.setAttachment(new AttachmentType());

                if (caDetails.getProcurementProcedureTitle() != null) {
                    dr.getAttachment().setExternalReference(new ExternalReferenceType());
                    dr.getAttachment().getExternalReference().setFileName(new FileNameType());
                    dr.getAttachment().getExternalReference().getFileName().setValue(caDetails.getProcurementProcedureTitle());
                    if (caDetails.getProcurementPublicationURI() != null) {
                        dr.getAttachment().getExternalReference().setURI(new URIType());
                        dr.getAttachment().getExternalReference().getURI().setValue(caDetails.getProcurementPublicationURI());
                    }

                }

                // 2018-03-20 UL: modifications to add capabilities to handle Received Notice Number
                if ((caDetails.getProcurementProcedureDesc() != null && !caDetails.getProcurementProcedureDesc().isEmpty())
                        || (caDetails.getReceivedNoticeNumber() != null && !caDetails.getReceivedNoticeNumber().isEmpty())) {

                    DescriptionType dt = new DescriptionType();

                    dt.setValue(caDetails.getProcurementProcedureDesc() != null
                            ? caDetails.getProcurementProcedureDesc()
                            : "_"); // dummy content for the first description


                    if (dr.getAttachment().getExternalReference() == null) {
                        dr.getAttachment().setExternalReference(new ExternalReferenceType());
                    }

                    dr.getAttachment().getExternalReference().getDescription().add(0, dt);

                    if (caDetails.getReceivedNoticeNumber() != null && !caDetails.getReceivedNoticeNumber().isEmpty()) {
                        DescriptionType dtRNN = new DescriptionType();
                        dtRNN.setValue(caDetails.getReceivedNoticeNumber());
                        dr.getAttachment().getExternalReference().getDescription().add(1, dtRNN);
                    }
                }
            }
        }
        return dr;
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
        }
        return dr;
    }

    default ContractingPartyType extractContractingPartyType(CADetails caDetails) {

        if (caDetails == null) {
            return null;
        }

        ContractingPartyType cpp = new ContractingPartyType();
        PartyType pt = new PartyType();
        cpp.setParty(pt);

        if (caDetails.getCAOfficialName() != null) {
            PartyNameType nt = new PartyNameType();
            nt.setName(new NameType());
            nt.getName().setValue(caDetails.getCAOfficialName());
            pt.getPartyName().add(nt);
        }

        if (caDetails.getID() != null) {
            PartyIdentificationType pit = new PartyIdentificationType();
            pit.setID(new IDType());
            pit.getID().setValue(caDetails.getID());
            pit.getID().setSchemeAgencyID("EU-COM-GROW");
            pt.getPartyIdentification().add(pit);
        }

        // UBL syntax path: cac:ContractingParty.Party.EndpointID
        if (caDetails.getElectronicAddressID() != null) {
            EndpointIDType eid = new EndpointIDType();
            eid.setSchemeAgencyID("EU-COM-GROW");
            eid.setValue(caDetails.getElectronicAddressID());
            eid.setSchemeID("ISO/IEC 9834-8:2008 - 4UUID");
            pt.setEndpointID(eid);
        }

        // UBL syntax path: cac:ContractingParty.Party.WebsiteURIID
        if (caDetails.getWebSiteURI() != null) {
            WebsiteURIType wsuri = new WebsiteURIType();
            wsuri.setValue(caDetails.getWebSiteURI());
            pt.setWebsiteURI(wsuri);
        }

        if (caDetails.getPostalAddress() != null) {
            // UBL syntax path: cac:ContractingParty.Party.PostalAddress

            AddressType at = new AddressType();

            at.setStreetName(new StreetNameType());
            at.getStreetName().setValue(caDetails.getPostalAddress().getAddressLine1());

            at.setCityName(new CityNameType());
            at.getCityName().setValue(caDetails.getPostalAddress().getCity());

            //at.setPostbox(new PostboxType());
            //at.getPostbox().setValue(cd.getPostalAddress().getPostCode());
            // UL 2017-10-10: write post code to cbc:PostalZone
            at.setPostalZone(new PostalZoneType());
            at.getPostalZone().setValue(caDetails.getPostalAddress().getPostCode());

            at.setCountry(new CountryType());
            at.getCountry().setIdentificationCode(createISOCountryIdCodeType(caDetails.getPostalAddress().getCountryCode()));
            at.getCountry().getIdentificationCode().setListID("CountryCodeIdentifier");
            cpp.getParty().setPostalAddress(at);
        }

        if (caDetails.getContactingDetails() != null) {
            // UBL syntax path: cac:ContractingParty.Party.Contact

            ContactType ct = new ContactType();
            ct.setName(new NameType());
            ct.getName().setValue(caDetails.getContactingDetails().getContactPointName());

            ct.setTelephone(new TelephoneType());
            ct.getTelephone().setValue(caDetails.getContactingDetails().getTelephoneNumber());

            ct.setElectronicMail(new ElectronicMailType());
            ct.getElectronicMail().setValue(caDetails.getContactingDetails().getEmailAddress());

            ct.setTelefax(new TelefaxType());
            ct.getTelefax().setValue(caDetails.getContactingDetails().getFaxNumber());

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
        dtc.setListVersionID(CURRENT_VERSION_TAG);
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
        propertyIDType.setSchemeVersionID(CURRENT_VERSION_TAG);
        propertyIDType.setValue(id);
        return propertyIDType;
    }

    default ConfidentialityLevelCodeType createConfidentialityLevelCode(String code) {
        ConfidentialityLevelCodeType codeType = new ConfidentialityLevelCodeType();
        codeType.setValue(code);
        codeType.setListID("ConfidentialityLevel");
        codeType.setListAgencyID("EU-COM-GROW");
        codeType.setListVersionID(CURRENT_VERSION_TAG);
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
        versionID.setSchemeVersionID(CURRENT_VERSION_TAG);
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
        tc.setListVersionID(CURRENT_VERSION_TAG);
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
        idType.setSchemeVersionID(CURRENT_VERSION_TAG);
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
        codeType.setListVersionID(CURRENT_VERSION_TAG);
        codeType.setValue(code);
        return codeType;
    }

    default PropertyGroupTypeCodeType createPropertyGroupTypeCodeType(String code) {
        PropertyGroupTypeCodeType ptc = new PropertyGroupTypeCodeType();
        ptc.setListID("PropertyGroupType");
        ptc.setListAgencyID("EU-COM-GROW");
        ptc.setListVersionID(CURRENT_VERSION_TAG);
        ptc.setValue(code);
        return ptc;
    }

    default IndustryClassificationCodeType createIndustryClassificationCodeType(String code) {
        IndustryClassificationCodeType icCodeType = new IndustryClassificationCodeType();
        icCodeType.setListID("EOIndustryClassificationCode");
        icCodeType.setListAgencyID("EU-COM-GROW");
        icCodeType.setListVersionID(CURRENT_VERSION_TAG);
        icCodeType.setValue(code);
        return icCodeType;
    }

    default ValueDataTypeCodeType createValueDataTypeCodeType(String code) {
        ValueDataTypeCodeType vdTypeCode = new ValueDataTypeCodeType();
        vdTypeCode.setListID("ResponseDataType");
        vdTypeCode.setListAgencyID("EU-COM-GROW");
        vdTypeCode.setListVersionID(CURRENT_VERSION_TAG);
        vdTypeCode.setValue(code);
        return vdTypeCode;
    }

    default TypeCodeType createTypeCodeType(String code) {
        TypeCodeType tCodeType = new TypeCodeType();
        tCodeType.setListID("CriterionElementType");
        tCodeType.setListAgencyID("EU-COM-GROW");
        tCodeType.setListVersionID(CURRENT_VERSION_TAG);
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
        qaTypeCode.setListVersionID(CURRENT_VERSION_TAG);
        return qaTypeCode;
    }

    default ProcurementProjectType createProcurementProjectType(String procurementProcedureTitle,
                                                                String procurementProcedureDesc,
                                                                String projectType,
                                                                List<String> classificationCodes) {

        ProcurementProjectType procurementProjectType = new ProcurementProjectType();

        // Name
        if (procurementProcedureTitle != null) {
            procurementProjectType.getName().add(new NameType());
            procurementProjectType.getName().get(0).setValue(procurementProcedureTitle);
        }

        // Description
        if (procurementProcedureDesc != null) {
            procurementProjectType.getDescription().add(new DescriptionType());
            procurementProjectType.getDescription().get(0).setValue(procurementProcedureDesc);
        }

        // CPV codes
        procurementProjectType.getMainCommodityClassification().addAll(classificationCodes.stream()
                .map(cpvCode -> {
                    CommodityClassificationType classificationType = new CommodityClassificationType();
                    classificationType.setItemClassificationCode(new ItemClassificationCodeType());
                    classificationType.getItemClassificationCode().setValue(cpvCode);
                    return classificationType;
                })
                .collect(Collectors.toList()));

        // Project Type
        if (projectType != null) {
            procurementProjectType.setProcurementTypeCode(new ProcurementTypeCodeType());
            procurementProjectType.getProcurementTypeCode().setListID("ProjectType");
            procurementProjectType.getProcurementTypeCode().setListAgencyID("EU-COM-OP");
            procurementProjectType.getProcurementTypeCode().setListVersionID("1.0");
            procurementProjectType.getProcurementTypeCode().setValue(projectType);
        }

        return procurementProjectType;
    }

    default ProfileExecutionIDType createProfileExecutionIDType(QualificationApplicationTypeEnum type) {
        ProfileExecutionIDType peIdType = new ProfileExecutionIDType();
        peIdType.setSchemeAgencyID("EU-COM-GROW");
        peIdType.setSchemeVersionID(CURRENT_VERSION_TAG);

        switch (type) {

            case REGULATED:
                peIdType.setValue(ProfileExecutionIDEnum.ESPD_EDM_V210_REGULATED.getValue());
                break;

            case SELFCONTAINED:
                peIdType.setValue(ProfileExecutionIDEnum.ESPD_EDM_V210_SELFCONTAINED.getValue());
                break;

        }
        return peIdType;
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
        peIdType.setSchemeVersionID(CURRENT_VERSION_TAG);
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
        codeType.setListID("EvaluationMethodType");
        codeType.setListAgencyID("EU-COM-GROW");
        codeType.setListVersionID(CURRENT_VERSION_TAG);
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
        codeType.setListID("WeightingType");
        codeType.setListAgencyID("EU-COM-GROW");
        codeType.setListVersionID(CURRENT_VERSION_TAG);
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

    default ProcedureCodeType createProcedureCodeType(String code) {
        ProcedureCodeType codeType = new ProcedureCodeType();
        codeType.setListID("ProcedureType");
        codeType.setListAgencyID("EU-COM-OP");
        codeType.setListVersionID("1.0");
        codeType.setValue(code);
        return codeType;
    }

    /**
     * Extract the lots for which the economic operator wishes to tender.
     * <p>
     *
     * @param eoDetails
     * @return The lot
     */
    default ProcurementProjectLotType extractProcurementProjectLot(EODetails eoDetails) {
        return createProcurementProjectLotType((eoDetails.getProcurementProjectLot() == null) ||
                eoDetails.getProcurementProjectLot().isEmpty() ? "0" : eoDetails.getProcurementProjectLot());
    }

    /**
     * Extract a list which contains the lots in which the
     * contracting authority wishes to divide the tender.
     * <p>
     *
     * @param caDetails
     * @return A list which contains contracting authority lots
     */
    default List<ProcurementProjectLotType> extractProcurementProjectLotType(CADetails caDetails) {
        return IntStream.rangeClosed(1, caDetails.getProcurementProjectLots())
                .mapToObj(number -> createProcurementProjectLotType("Lot" + number))
                .collect(Collectors.toList());
    }

    default ProcurementProjectLotType createProcurementProjectLotType(String value) {
        ProcurementProjectLotType lotType = new ProcurementProjectLotType();
        lotType.setID(new IDType());
        lotType.getID().setValue(value);
        lotType.getID().setSchemeAgencyID("EU-COM-GROW");
        return lotType;
    }

    default ResponseIDType createResponseIDType(String id) {
        ResponseIDType idType = new ResponseIDType();
        idType.setSchemeAgencyID("EU-COM-GROW");
        idType.setValue(id);
        return idType;
    }

    default void applyCAResponseToXML(Requirement rq,
                                      TenderingCriterionPropertyType rqType) {

        if (rq.getType() == RequirementTypeEnum.REQUIREMENT
                && rq.getResponse() != null
                && rq.getResponseDataType() != null) {

            switch (rq.getResponseDataType()) {

                case DESCRIPTION:
                    String description = ((DescriptionResponse) rq.getResponse()).getDescription();
                    if (description != null) {
                        rqType.setExpectedDescription(new ExpectedDescriptionType());
                        rqType.getExpectedDescription().setValue(description);
                    }
                    break;

                case AMOUNT:
//                    BigDecimal amount = ((AmountResponse) rq.getResponse()).getAmount();
//                    String currency = ((AmountResponse) rq.getResponse()).getCurrency();
//                    if ((amount != null) || (currency != null && !currency.isEmpty())) {
//                        // Only generate a proper response if for at least one of the variables "amount" and
//                        // "currency" a value different from the default is detected.
//
//                        rqType.setExpectedAmount(new ExpectedAmountType());
//                        rqType.getExpectedAmount().setValue(amount);
//                        rqType.getExpectedAmount().setCurrencyID(currency);
//                    }

                    BigDecimal amount = ((AmountResponse) rq.getResponse()).getAmount();
                    String currency = ((AmountResponse) rq.getResponse()).getCurrency();
                    if ((amount != null) || (currency != null && !currency.isEmpty())) {
                        // Only generate a proper response if for at least one of the variables "amount" and
                        // "currency" a value different from the default is detected.

                        rqType.setMinimumAmount(new MinimumAmountType());
                        rqType.getMinimumAmount().setValue(amount);
                        rqType.getMinimumAmount().setCurrencyID(currency);
                    }
                    break;

                case CODE:
                    String code = ((EvidenceURLCodeResponse) rq.getResponse()).getEvidenceURLCode();
                    if (code != null && !code.isEmpty()) {
                        rqType.setExpectedCode(new ExpectedCodeType());
                        if (rq.getResponseValuesRelatedArtefact() != null) {
                            rqType.getExpectedCode().setListID(rq.getResponseValuesRelatedArtefact());

                            switch (rq.getResponseValuesRelatedArtefact()) {

                                case "FinancialRatioType":
                                    rqType.getExpectedCode().setListAgencyID("BACH");
                                    break;

                                case "BidType":
                                    rqType.getExpectedCode().setListAgencyID("EU-COM-OP");
                                    break;

                                case "CPVCodes":
                                    rqType.getExpectedCode().setListAgencyID("EU-COM-OP");
                                    break;

                            }

                        } else {
                            LOGGER.log(Level.WARNING, "Requirement's: " + rq.getID() + " ResponseValuesRelatedArtefact is null ");
                        }
                        rqType.getExpectedCode().setListVersionID("1.0");
                        rqType.getExpectedCode().setListAgencyID("EU-COM-GROW");
                        rqType.getExpectedCode().setValue(code);
                    }
                    break;

                case QUANTITY_INTEGER:
                    int quantityInt = ((QuantityIntegerResponse) rq.getResponse()).getQuantity();
                    rqType.setExpectedValueNumeric(new ExpectedValueNumericType());
                    rqType.getExpectedValueNumeric().setValue(BigDecimal.valueOf(quantityInt));
                    break;

                case QUANTITY:
                    BigDecimal quantity = ((QuantityResponse) rq.getResponse()).getQuantity();
                    if (quantity != null) {
                        rqType.setExpectedValueNumeric(new ExpectedValueNumericType());
                        rqType.getExpectedValueNumeric().setValue(quantity);
                    }
                    break;

                case PERIOD:
                    LocalDate startDate = ((ApplicablePeriodResponse) rq.getResponse()).getStartDate();
                    LocalDate endDate = ((ApplicablePeriodResponse) rq.getResponse()).getStartDate();
                    if (startDate != null && endDate != null) {
                        PeriodType periodType = new PeriodType();
                        periodType.setStartDate(new StartDateType());
                        periodType.setEndDate(new EndDateType());
                        periodType.getStartDate().setValue(startDate);
                        periodType.getEndDate().setValue(endDate);
                        rqType.getApplicablePeriod().add(periodType);
                    }
                    break;

                case URL:
                    // https://github.com/ESPD/ESPD-EDM/issues/181
                    String url = ((URLResponse) rq.getResponse()).getUrl();
                    if (url != null) {
                        rqType.setExpectedID(new ExpectedIDType());
                        rqType.getExpectedID().setSchemeAgencyID("EU-COM-GROW");
                        rqType.getExpectedID().setSchemeID("URI");
                        rqType.getExpectedID().setValue(url);
                    }
                    break;

                case INDICATOR:
                    // https://github.com/ESPD/ESPD-EDM/issues/182
                    String indicator = ((IndicatorResponse) rq.getResponse()).isIndicator() ? "TRUE" : "FALSE";
                    rqType.setExpectedCode(new ExpectedCodeType());
                    rqType.getExpectedCode().setListID("IndicatorType");
                    rqType.getExpectedCode().setListAgencyID("EU-COM-OP");
                    rqType.getExpectedCode().setListVersionID("1.0");
                    rqType.getExpectedCode().setValue(indicator);
                    break;

                case CODE_BOOLEAN:
                    String codeBoolean = ((IndicatorResponse) rq.getResponse()).isIndicator()
                            ? BooleanGUIControlTypeEnum.RADIO_BUTTON_TRUE.name()
                            : BooleanGUIControlTypeEnum.RADIO_BUTTON_FALSE.name();
                    rqType.setExpectedCode(new ExpectedCodeType());
                    rqType.getExpectedCode().setListID("BooleanGUIControlType");
                    rqType.getExpectedCode().setListAgencyID("EU-COM-OP");
                    rqType.getExpectedCode().setListVersionID("1.0");
                    rqType.getExpectedCode().setValue(codeBoolean);
                    break;

            }
        }
    }

}
