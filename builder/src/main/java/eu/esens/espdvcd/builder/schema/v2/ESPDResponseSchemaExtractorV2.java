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

import eu.esens.espdvcd.codelist.enums.EOIndustryClassificationCodeEnum;
import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.ESPDRequestDetails;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.*;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.espd.schema.v2.pre_award.commonaggregate.*;
import eu.espd.schema.v2.pre_award.commonbasic.*;
import eu.espd.schema.v2.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ESPDResponseSchemaExtractorV2 implements SchemaExtractorV2 {

    private final static Logger LOGGER = LoggerFactory.getLogger(ESPDResponseSchemaExtractorV2.class);

    public QualificationApplicationResponseType extractQualificationApplicationResponseType(ESPDResponse modelResponse) {

        final QualificationApplicationResponseType qarType = new QualificationApplicationResponseType();

        if (modelResponse.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
            qarType.setContractFolderID(createContractFolderIDType(modelResponse.getCADetails().getProcurementProcedureFileReferenceNo()));
        }

        qarType.getAdditionalDocumentReference().add(extractCADetailsDocumentReference(modelResponse.getCADetails()));

        DocumentReferenceType drt = extractCADetailsNationalDocumentReference(modelResponse.getCADetails());
        if (drt != null) {
            qarType.getAdditionalDocumentReference().add(drt);
        }

        qarType.getContractingParty().add(extractContractingPartyType(modelResponse.getCADetails()));
        // Economic Operator Details
        qarType.getProcurementProjectLot().add(extractProcurementProjectLot(modelResponse.getEODetails()));

        // Economic Operator Group Name (This is in EODetails)
        if (modelResponse.getEODetails().getEOGroupName() != null) {
            qarType.setEconomicOperatorGroupName(new EconomicOperatorGroupNameType());
            qarType.getEconomicOperatorGroupName().setValue((modelResponse
                    .getEODetails().getEOGroupName()));
        }

        // Service Provider Details
        qarType.getContractingParty().get(0).getParty().getServiceProviderParty()
                .add(extractServiceProviderPartyType(modelResponse.getServiceProviderDetails()));

        // apply global weighting
        qarType.getWeightScoringMethodologyNote().addAll(modelResponse.getCADetails()
                .getWeightScoringMethodologyNoteList().stream()
                .map(note -> createWeightScoringMethodologyNoteType(note))
                .collect(Collectors.toList()));

        if (modelResponse.getCADetails().getWeightingType() != null) {
            qarType.setWeightingTypeCode(createWeightingTypeCodeType(modelResponse
                    .getCADetails().getWeightingType()));
        }

        qarType.getTenderingCriterion().addAll(modelResponse.getFullCriterionList().stream()
                .filter(sc -> sc.isSelected())
                .map(sc -> extractTenderingCriterion(sc))
                .collect(Collectors.toList()));

        qarType.getEconomicOperatorParty().add(extractEODetails(modelResponse.getEODetails()));

        // create a map with key = Criterion ID, value = TenderingCriterion in order to use it during weighting
        // responses extraction process
        Map<String, TenderingCriterionType> criterionTypeMap = qarType.getTenderingCriterion().stream()
                .collect(Collectors.toMap(criterionType -> criterionType.getID().getValue(), Function.identity()));

        qarType.getTenderingCriterionResponse().addAll(extractAllTenderingCriterionResponses(modelResponse, criterionTypeMap));

        qarType.getEvidence().addAll(modelResponse.getEvidenceList().stream()
                .map(ev -> extractEvidenceType(ev))
                .collect(Collectors.toList()));

        if (modelResponse.getESPDRequestDetails() != null) {
            qarType.getAdditionalDocumentReference().add(extractESPDRequestDetails(modelResponse.getESPDRequestDetails()));
        }

        qarType.setUBLVersionID(createUBL22VersionIdType());
        qarType.setCustomizationID(createCENBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrdm092:ver3.0"));
        qarType.setVersionID(createVersionIDType("2018.01.01"));

        if (modelResponse.getDocumentDetails() != null
                && modelResponse.getDocumentDetails().getQualificationApplicationType() != null) {
            // Profile Execution ID
            qarType.setProfileExecutionID(createProfileExecutionIDType(modelResponse
                    .getDocumentDetails().getQualificationApplicationType()));
            // Qualification Application Type
            qarType.setQualificationApplicationTypeCode(createQualificationApplicationTypeCodeType(modelResponse
                    .getDocumentDetails().getQualificationApplicationType()));
            // Set Lots
            qarType.getProcurementProjectLot().addAll(createProcurementProjectLotType(modelResponse
                            .getDocumentDetails().getQualificationApplicationType() // REGULATED or SELF-CONTAINED
                    , modelResponse.getCADetails().getProcurementProjectLots()));    // Number of lots
            // Procurement Project (only in SELF-CONTAINED)
            if (modelResponse.getDocumentDetails().getQualificationApplicationType()
                    == QualificationApplicationTypeEnum.SELFCONTAINED) {

                qarType.setProcurementProject(createProcurementProjectType(modelResponse.getCADetails().getProcurementProcedureTitle()
                        , modelResponse.getCADetails().getProcurementProcedureDesc()
                        , modelResponse.getCADetails().getProjectType()              // Procurement typeCode
                        , modelResponse.getCADetails().getClassificationCodes()));   // CPV codes
            }
        }
        // Procedure Code
        if (modelResponse.getCADetails().getProcurementProcedureType() != null) {
            qarType.setProcedureCode(createProcedureCodeType(modelResponse
                    .getCADetails().getProcurementProcedureType()));
        }

        qarType.setCopyIndicator(new CopyIndicatorType());
        qarType.getCopyIndicator().setValue(false);

        return qarType;
    }

    public List<TenderingCriterionResponseType> extractAllTenderingCriterionResponses(final ESPDResponse response,
                                                                                      final Map<String, TenderingCriterionType> criterionTypeMap) {
        List<TenderingCriterionResponseType> tcrTypeList = new ArrayList<>();
        response.getFullCriterionList().forEach(sc -> tcrTypeList.addAll(extractAllTenderingCriterionResponses(sc.getRequirementGroups(),
                criterionTypeMap.get(sc.getID()))));
        return tcrTypeList;
    }

    public List<TenderingCriterionResponseType> extractAllTenderingCriterionResponses(final List<RequirementGroup> rgList,
                                                                                      final TenderingCriterionType criterionType) {
        final List<TenderingCriterionResponseType> tcrTypeList = new ArrayList<>();
        rgList.forEach(rg -> tcrTypeList.addAll(extractAllTenderingCriterionResponses(rg, criterionType)));
        return tcrTypeList;
    }

    public List<TenderingCriterionResponseType> extractAllTenderingCriterionResponses(final RequirementGroup rg,
                                                                                      final TenderingCriterionType criterionType) {
        return extractAllRequirements(rg, null).stream()
                .map(rq -> {

                    if (rq.getResponse() != null) {
                        rq.getResponse().setValidatedCriterionPropertyID(rq.getID());
                    }
                    return extractTenderingCriterionResponse(rq.getResponse(), rq.getResponseDataType(), criterionType);
                })
                .collect(Collectors.toList());
    }

    public List<Requirement> extractAllRequirements(final RequirementGroup rg, List<Requirement> requirementList) {

        if (requirementList == null) {
            requirementList = new ArrayList<>();
        }

        requirementList.addAll(rg.getRequirements());

        for (RequirementGroup subRg : rg.getRequirementGroups()) {
            extractAllRequirements(subRg, requirementList);
        }

        return requirementList;
    }

    public EvidenceType extractEvidenceType(Evidence evidence) {

        if (evidence == null) {
            return null;
        }

        EvidenceType evType = new EvidenceType();
        evType.getDocumentReference().add(new DocumentReferenceType());

        if (evidence.getID() != null) {
            evType.setID(new IDType());
            evType.getID().setSchemeAgencyID("EU-COM-GROW");
            evType.getID().setValue(evidence.getID());
        }

        if (evidence.getDescription() != null) {
            evType.getDescription().add(new DescriptionType());
            evType.getDescription().get(0).setValue(evidence.getDescription());
        }

        if (evidence.getConfidentialityLevelCode() != null) {
            evType.setConfidentialityLevelCode(new ConfidentialityLevelCodeType());
            evType.getConfidentialityLevelCode().setListID("ConfidentialityLevel");
            evType.getConfidentialityLevelCode().setListAgencyID("EU-COM-GROW");
            evType.getConfidentialityLevelCode().setListVersionID("2.0.2");
            evType.getConfidentialityLevelCode().setValue(evidence.getConfidentialityLevelCode());
        }

        if (evidence.getEvidenceURL() != null) {
            // FIXME document reference ID added as an empty element here
            evType.getDocumentReference().get(0).setID(new IDType());
            evType.getDocumentReference().get(0).getID().setSchemeAgencyID("EU-COM-GROW");
            evType.getDocumentReference().get(0).setAttachment(new AttachmentType());
            evType.getDocumentReference().get(0).getAttachment().setExternalReference(new ExternalReferenceType());
            evType.getDocumentReference().get(0).getAttachment().getExternalReference().setURI(new URIType());
            evType.getDocumentReference().get(0).getAttachment().getExternalReference().getURI().setValue(evidence.getEvidenceURL());
        }

        if (evidence.getEvidenceIssuer() != null) {

            evType.getDocumentReference().get(0).setIssuerParty(new PartyType());

            if (evidence.getEvidenceIssuer().getName() != null) {
                evType.getDocumentReference().get(0).getIssuerParty().getPartyName().add(new PartyNameType());
                evType.getDocumentReference().get(0).getIssuerParty().getPartyName().get(0).setName(new NameType());
                evType.getDocumentReference().get(0).getIssuerParty().getPartyName().get(0).getName().setValue(evidence.getEvidenceIssuer().getName());
            }

            if (evidence.getEvidenceIssuer().getID() != null) {
                evType.getDocumentReference().get(0).getIssuerParty().getPartyIdentification().add(new PartyIdentificationType());
                evType.getDocumentReference().get(0).getIssuerParty().getPartyIdentification().get(0).setID(new IDType());
                evType.getDocumentReference().get(0).getIssuerParty().getPartyIdentification().get(0).getID().setValue(evidence.getEvidenceIssuer().getID());
            }

            if (evidence.getEvidenceIssuer().getWebsite() != null) {
                evType.getDocumentReference().get(0).getIssuerParty().setWebsiteURI(new WebsiteURIType());
                evType.getDocumentReference().get(0).getIssuerParty().getWebsiteURI().setValue(evidence.getEvidenceIssuer().getWebsite());
            }
        }

        return evType;
    }

    public EconomicOperatorPartyType extractEODetails(EODetails eoDetails) {

        if (eoDetails == null) {
            return null;
        }

        EconomicOperatorPartyType eoPartyType = new EconomicOperatorPartyType();
        eoPartyType.getQualifyingParty().add(new QualifyingPartyType());
        eoPartyType.getQualifyingParty().get(0).setParty(new PartyType());
        eoPartyType.setParty(new PartyType());

        String icc = eoDetails.isSmeIndicator() ? EOIndustryClassificationCodeEnum.SME.name()
                : EOIndustryClassificationCodeEnum.LARGE.name();
        eoPartyType.getParty().setIndustryClassificationCode(createIndustryClassificationCodeType(icc));

        // Employee quantity
        if (eoDetails.getEmployeeQuantity() > 0) {
            eoPartyType.getQualifyingParty().get(0)
                    .setEmployeeQuantity(new EmployeeQuantityType());
            eoPartyType.getQualifyingParty().get(0)
                    .getEmployeeQuantity().setValue(BigDecimal.valueOf(eoDetails.getEmployeeQuantity()));
        }

        // General turnover
        if (eoDetails.getGeneralTurnover() != null
                && eoDetails.getGeneralTurnover().getAmount() != null
                && eoDetails.getGeneralTurnover().getCurrency() != null) {
            eoPartyType.getQualifyingParty().get(0)
                    .getFinancialCapability().get(0).setValueAmount(new ValueAmountType());
            eoPartyType.getQualifyingParty().get(0)
                    .getFinancialCapability().get(0).getValueAmount()
                    .setValue(eoDetails.getGeneralTurnover().getAmount());
        }

        if (eoDetails.getEoRole() != null) {
            eoPartyType.setEconomicOperatorRole(new EconomicOperatorRoleType());
            eoPartyType.getEconomicOperatorRole().setRoleCode(new RoleCodeType());
            eoPartyType.getEconomicOperatorRole().getRoleCode().setListID("EORoleType");
            eoPartyType.getEconomicOperatorRole().getRoleCode().setListAgencyName("DG GROW (European Commission)");
            eoPartyType.getEconomicOperatorRole().getRoleCode().setListAgencyID("EU-COM-GROW");
            eoPartyType.getEconomicOperatorRole().getRoleCode().setListVersionID("2.0.2");
            eoPartyType.getEconomicOperatorRole().getRoleCode().setValue(eoDetails.getEoRole().name());
        }

        if (eoDetails.getID() != null) {
            PartyIdentificationType pit = new PartyIdentificationType();
            pit.setID(new IDType());
            pit.getID().setValue(eoDetails.getID());
            pit.getID().setSchemeAgencyID("EU-COM-GROW");
            eoPartyType.getParty().getPartyIdentification().add(pit);
        }

        if (eoDetails.getName() != null) {
            PartyNameType pnt = new PartyNameType();
            pnt.setName(new NameType());
            pnt.getName().setValue(eoDetails.getName());
            eoPartyType.getParty().getPartyName().add(pnt);
        }

        if (eoDetails.getElectronicAddressID() != null) {
            EndpointIDType eid = new EndpointIDType();
            eid.setSchemeID("ISO/IEC 9834-8:2008 - 4UUID");
            eid.setSchemeAgencyID("EU-COM-GROW");
            eid.setValue(eoDetails.getElectronicAddressID());
            eoPartyType.getParty().setEndpointID(eid);
        }

        if (eoDetails.getWebSiteURI() != null) {
            WebsiteURIType wsuri = new WebsiteURIType();
            wsuri.setValue(eoDetails.getWebSiteURI());
            eoPartyType.getParty().setWebsiteURI(wsuri);
        }

        if (eoDetails.getPostalAddress() != null) {
            AddressType at = new AddressType();

            at.setStreetName(new StreetNameType());
            at.getStreetName().setValue(eoDetails.getPostalAddress().getAddressLine1());

            at.setCityName(new CityNameType());
            at.getCityName().setValue(eoDetails.getPostalAddress().getCity());

            at.setPostalZone(new PostalZoneType());
            at.getPostalZone().setValue(eoDetails.getPostalAddress().getPostCode());

            at.setCountry(new CountryType());
            at.getCountry().setIdentificationCode(createISOCountryIdCodeType(eoDetails.getPostalAddress().getCountryCode()));

            eoPartyType.getParty().setPostalAddress(at);
        }

        if (eoDetails.getContactingDetails() != null) {
            ContactType ct = new ContactType();
            ct.setName(new NameType());
            ct.getName().setValue(eoDetails.getContactingDetails().getContactPointName());

            ct.setTelephone(new TelephoneType());
            ct.getTelephone().setValue(eoDetails.getContactingDetails().getTelephoneNumber());

            ct.setElectronicMail(new ElectronicMailType());
            ct.getElectronicMail().setValue(eoDetails.getContactingDetails().getEmailAddress());

            ct.setTelefax(new TelefaxType());
            ct.getTelefax().setValue(eoDetails.getContactingDetails().getFaxNumber());

            eoPartyType.getParty().setContact(ct);
        }

        eoDetails.getNaturalPersons().forEach(np -> {

            PowerOfAttorneyType poa = new PowerOfAttorneyType();

            poa.getDescription().add(new DescriptionType());
            poa.getDescription().get(0).setValue(np.getRole());

            PartyType agentPartyType = new PartyType();
            PersonType personType = new PersonType();
            personType.setFirstName(new FirstNameType());
            personType.getFirstName().setValue(np.getFirstName());

            personType.setFamilyName(new FamilyNameType());
            personType.getFamilyName().setValue(np.getFamilyName());
            if (np.getBirthDate() != null) {
                try {
                    personType.setBirthDate(new BirthDateType());

                    XMLGregorianCalendar xcal = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendarDate(
                                    np.getBirthDate().getYear(),
                                    np.getBirthDate().getMonthValue(),
                                    np.getBirthDate().getDayOfMonth(),
                                    DatatypeConstants.FIELD_UNDEFINED);
                    personType.getBirthDate().setValue(xcal.toGregorianCalendar().toZonedDateTime().toLocalDate());
                } catch (DatatypeConfigurationException ex) {
                    LOGGER.error("Could not create XML Date Object", ex);
                }
            }

            personType.setBirthplaceName(new BirthplaceNameType());
            personType.getBirthplaceName().setValue(np.getBirthPlace());

            if (np.getContactDetails() != null) {

                personType.setContact(new ContactType());
                personType.getContact().setTelephone(new TelephoneType());
                personType.getContact().getTelephone().setValue(np.getContactDetails().getTelephoneNumber());

                personType.getContact().setElectronicMail(new ElectronicMailType());
                personType.getContact().getElectronicMail().setValue(np.getContactDetails().getEmailAddress());
            }

            if (np.getPostalAddress() != null) {

                personType.setResidenceAddress(new AddressType());

                personType.getResidenceAddress().setPostalZone(new PostalZoneType());
                personType.getResidenceAddress().getPostalZone().setValue(np.getPostalAddress().getPostCode());

                personType.getResidenceAddress().setStreetName(new StreetNameType());
                personType.getResidenceAddress().getStreetName().setValue(np.getPostalAddress().getAddressLine1());

                personType.getResidenceAddress().setCityName(new CityNameType());
                personType.getResidenceAddress().getCityName().setValue(np.getPostalAddress().getCity());

                personType.getResidenceAddress().setCountry(new CountryType());
                personType.getResidenceAddress().getCountry().setIdentificationCode(createISOCountryIdCodeType(np.getPostalAddress().getCountryCode()));
            }

            agentPartyType.getPerson().add(personType);
            poa.setAgentParty(agentPartyType);

            eoPartyType.getParty().getPowerOfAttorney().add(poa);
        });
        return eoPartyType;
    }

    @Override
    public TenderingCriterionPropertyType extractTenderingCriterionPropertyType(Requirement rq) {
        TenderingCriterionPropertyType reqType = new TenderingCriterionPropertyType();

        reqType.setTypeCode(createTypeCodeType(rq.getType().name()));
        reqType.setValueDataTypeCode(createValueDataTypeCodeType(rq.getResponseDataType().name()));

        reqType.getDescription().add(new DescriptionType());
        reqType.getDescription().get(0).setValue(rq.getDescription());

        reqType.setID(createCriteriaTaxonomyIDType(rq.getID()));

        applyCAResponseToXML(rq, reqType);

        return reqType;
    }

    private TenderingCriterionResponseType extractTenderingCriterionResponse(Response response,
                                                                             ResponseTypeEnum respType,
                                                                             final TenderingCriterionType criterionType) {

        TenderingCriterionResponseType tcrType = new TenderingCriterionResponseType();
        // ResponseValueType rvType = new ResponseValueType();
        EvidenceSuppliedType evsType = new EvidenceSuppliedType();

        if (response == null) {
            return null;
        }

        if (response.getValidatedCriterionPropertyID() != null) {
            tcrType.setValidatedCriterionPropertyID(createValidatedCriterionPropertyId(response.getValidatedCriterionPropertyID()));
        }

        if (response.getConfidentialityLevelCode() != null) {
            tcrType.setConfidentialityLevelCode(createConfidentialityLevelCode(response.getConfidentialityLevelCode()));
        }

        tcrType.setID(createDefaultIDType(UUID.randomUUID().toString()));
        tcrType.getID().setSchemeID("ISO/IEC 9834-8:2008 - 4UUID");
        // rvType.setID(createDefaultIDType(UUID.randomUUID().toString()));
        // rvType.getID().setSchemeID("ISO/IEC 9834-8:2008 - 4UUID");

        switch (respType) {
            case DESCRIPTION:
                ResponseValueType descRvType = createResponseValueType();
                String description = ((DescriptionResponse) response).getDescription();
                descRvType.getDescription().add(new DescriptionType());
                if (description != null && !description.isEmpty()) {
                    // rvType.getDescription().add(new DescriptionType());
                    descRvType.getDescription().get(0).setValue(description);
                }
                tcrType.getResponseValue().add(descRvType);
                return tcrType;

            case QUANTITY_YEAR:
                ResponseValueType quaYearRvType = createResponseValueType();
                if (((QuantityYearResponse) response).getYear() != 0) {
                    quaYearRvType.setResponseQuantity(new ResponseQuantityType());
                    quaYearRvType.getResponseQuantity().setUnitCode("YEAR");
                    quaYearRvType.getResponseQuantity().setValue(BigDecimal.valueOf(((QuantityYearResponse) response).getYear()));
                }
                tcrType.getResponseValue().add(quaYearRvType);
                return tcrType;

            case QUANTITY:
                ResponseValueType quaRvType = createResponseValueType();
                quaRvType.setResponseQuantity(new ResponseQuantityType());
                quaRvType.getResponseQuantity().setValue(((QuantityResponse) response).getQuantity());
                tcrType.getResponseValue().add(quaRvType);
                return tcrType;

            case QUANTITY_INTEGER:
                ResponseValueType quaIntRvType = createResponseValueType();
                quaIntRvType.setResponseQuantity(new ResponseQuantityType());
                quaIntRvType.getResponseQuantity().setUnitCode("NUMBER");
                quaIntRvType.getResponseQuantity().setValue(BigDecimal.valueOf(((QuantityIntegerResponse) response).getQuantity()));
                tcrType.getResponseValue().add(quaIntRvType);
                return tcrType;

            case AMOUNT:
                ResponseValueType amRvType = createResponseValueType();
                BigDecimal amount = ((AmountResponse) response).getAmount();
                String currency = ((AmountResponse) response).getCurrency();
                if ((amount != null) || (currency != null && !currency.isEmpty())) {
                    // Only generate a proper response if for at least one of the variables "amount" and
                    // "currency" a value different from the default is detected.

                    amRvType.setResponseAmount(new ResponseAmountType());
                    amRvType.getResponseAmount().setValue(amount);
                    amRvType.getResponseAmount().setCurrencyID(currency);
                }
                tcrType.getResponseValue().add(amRvType);
                return tcrType;

            case INDICATOR:
                ResponseValueType indRvType = createResponseValueType();
                indRvType.setResponseIndicator(new ResponseIndicatorType());
                indRvType.getResponseIndicator().setValue(((IndicatorResponse) response).isIndicator());
                tcrType.getResponseValue().add(indRvType);
                return tcrType;

            case PERIOD:
                PeriodType periodType = new PeriodType();
                if (((ApplicablePeriodResponse) response).getStartDate() != null
                        && ((ApplicablePeriodResponse) response).getEndDate() != null) {

                    periodType.setStartDate(new StartDateType());
                    periodType.setEndDate(new EndDateType());
                    periodType.getStartDate().setValue(((ApplicablePeriodResponse) response).getStartDate());
                    periodType.getEndDate().setValue(((ApplicablePeriodResponse) response).getEndDate());

                }
                tcrType.getApplicablePeriod().add(periodType);
                return tcrType;

            case PERCENTAGE:
                ResponseValueType perRvType = createResponseValueType();
                perRvType.setResponseQuantity(new ResponseQuantityType());
                perRvType.getResponseQuantity().setUnitCode("PERCENTAGE");
                perRvType.getResponseQuantity().setValue(((PercentageResponse) response).getPercentage());
                tcrType.getResponseValue().add(perRvType);
                return tcrType;

            case DATE:
                ResponseValueType dateRvType = createResponseValueType();
                if (((DateResponse) response).getDate() != null) {
                    LocalDate respDate = ((DateResponse) response).getDate();
                    try {
                        XMLGregorianCalendar xcal = DatatypeFactory.newInstance()
                                .newXMLGregorianCalendarDate(
                                        respDate.getYear(),
                                        respDate.getMonthValue(),
                                        respDate.getDayOfMonth(),
                                        DatatypeConstants.FIELD_UNDEFINED);
                        dateRvType.setResponseDate(new ResponseDateType());
                        dateRvType.getResponseDate().setValue(xcal.toGregorianCalendar().toZonedDateTime().toLocalDate());

                    } catch (DatatypeConfigurationException ex) {
                        LOGGER.error("Could not create XMLGregorianCalendar Date Object", ex);
                        return null;
                    }
                }
                tcrType.getResponseValue().add(dateRvType);
                return tcrType;

            case CODE:
                ResponseValueType codeRvType = createResponseValueType();
                String evidenceURLCode = ((EvidenceURLCodeResponse) response).getEvidenceURLCode();
                if (evidenceURLCode != null && !evidenceURLCode.isEmpty()) {
                    codeRvType.setResponseCode(new ResponseCodeType());
                    codeRvType.getResponseCode().setValue(evidenceURLCode);
                }
                tcrType.getResponseValue().add(codeRvType);
                return tcrType;

            case CODE_COUNTRY:
                ResponseValueType codeCouRvType = createResponseValueType();
                String countryCode = ((CountryCodeResponse) response).getCountryCode();
                if (countryCode != null && !countryCode.isEmpty()) {
                    codeCouRvType.setResponseCode(new ResponseCodeType());
                    codeCouRvType.getResponseCode().setListAgencyID("ISO");
                    codeCouRvType.getResponseCode().setListID("ISO 3166-1");
                    codeCouRvType.getResponseCode().setListVersionID("2.0.2");
                    codeCouRvType.getResponseCode().setValue(countryCode);
                }
                tcrType.getResponseValue().add(codeCouRvType);
                return tcrType;

            case EVIDENCE_IDENTIFIER:
                String evidenceSuppliedId = ((EvidenceIdentifierResponse) response).getEvidenceSuppliedId();
                if (evidenceSuppliedId != null) {
                    evsType.setID(new IDType());
                    evsType.getID().setSchemeAgencyID("ISO/IEC 9834-8:2008 - 4UUID");
                    evsType.getID().setValue(evidenceSuppliedId);
                }
                tcrType.getEvidenceSupplied().add(evsType);
                return tcrType;

            case IDENTIFIER:
                ResponseValueType ideRvType = createResponseValueType();
                String responseID = ((IdentifierResponse) response).getIdentifier();
                if (responseID != null) {
                    ideRvType.setResponseID(new ResponseIDType());
                    ideRvType.getResponseID().setSchemeAgencyID("EU-COM-GROW");
                    ideRvType.getResponseID().setValue(responseID);
                }
                tcrType.getResponseValue().add(ideRvType);
                return tcrType;

            case URL:
                ResponseValueType urlRvType = createResponseValueType();
                String url = ((URLResponse) response).getUrl();
                if (url != null) {
                    urlRvType.setResponseURI(new ResponseURIType());
                    urlRvType.getResponseURI().setValue(url);
                }
                tcrType.getResponseValue().add(urlRvType);
                return tcrType;

            case WEIGHT_INDICATOR:
                ResponseValueType weightIndRvType = createResponseValueType();
                WeightIndicatorResponse weightIndResp = (WeightIndicatorResponse) response;
                applyTenderingCriterionWeightingData(weightIndResp, criterionType);
                // Indicator
                weightIndRvType.setResponseIndicator(createResponseIndicatorType(weightIndResp.isIndicator()));
                tcrType.getResponseValue().add(weightIndRvType);
                return tcrType;

            case LOT_IDENTIFIER:
                LotIdentifierResponse lotIdeResp = (LotIdentifierResponse) response;
                lotIdeResp.getLotsList().forEach(lot -> {
                    ResponseValueType lotIdeRvType = createResponseValueType();
                    lotIdeRvType.setResponseID(new ResponseIDType());
                    lotIdeRvType.getResponseID().setValue(lot);
                    tcrType.getResponseValue().add(lotIdeRvType);
                });
                return tcrType;

            case ECONOMIC_OPERATOR_IDENTIFIER:
                ResponseValueType eoIdeRvType = createResponseValueType();
                String eoResponseID = ((EOIdentifierResponse) response).getIdentifier();
                String eoIDType = ((EOIdentifierResponse) response).getEOIDType();
                if (eoResponseID != null) {
                    eoIdeRvType.setResponseID(new ResponseIDType());
                    eoIdeRvType.getResponseID().setSchemeAgencyID("EU-COM-GROW");
                    if (eoIDType != null) {
                        eoIdeRvType.getResponseID().setSchemeName(eoIDType);
                    }
                    eoIdeRvType.getResponseID().setValue(eoResponseID);
                }
                tcrType.getResponseValue().add(eoIdeRvType);
                return tcrType;

            default:
                return null;
        }

    }

    private DocumentReferenceType extractESPDRequestDetails(ESPDRequestDetails espdRequestDetails) {
        if (espdRequestDetails == null) {
            return null;
        }

        DocumentReferenceType drt = new DocumentReferenceType();

        drt.setID(createISOIECIDType(espdRequestDetails.getID()));

        try {
            XMLGregorianCalendar xcalDate = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendarDate(
                            espdRequestDetails.getIssueDate().getYear(),
                            espdRequestDetails.getIssueDate().getMonthValue(),
                            espdRequestDetails.getIssueDate().getDayOfMonth(),
                            DatatypeConstants.FIELD_UNDEFINED);
            IssueDateType idt = new IssueDateType();
            idt.setValue(xcalDate.toGregorianCalendar().toZonedDateTime().toLocalDate());
            drt.setIssueDate(idt);
        } catch (DatatypeConfigurationException ex) {
            LOGGER.error("Could not create XMLGregorianCalendar Date Object", ex);
        }


        try {
            XMLGregorianCalendar xcalTime = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendarTime(
                            espdRequestDetails.getIssueTime().getHour(),
                            espdRequestDetails.getIssueTime().getMinute(),
                            espdRequestDetails.getIssueTime().getSecond(),
                            DatatypeConstants.FIELD_UNDEFINED);
            IssueTimeType itt = new IssueTimeType();
            itt.setValue(xcalTime.toGregorianCalendar().toZonedDateTime().toLocalTime());
            drt.setIssueTime(itt);
        } catch (DatatypeConfigurationException ex) {
            LOGGER.error("Could not create XMLGregorianCalendar Date Object", ex);
        }


        List<DocumentDescriptionType> ddtList = drt.getDocumentDescription();
        if (ddtList != null) {
            DocumentDescriptionType ddt = new DocumentDescriptionType();
            ddt.setValue(espdRequestDetails.getDescription());
            ddtList.add(ddt);
        }

        drt.setDocumentTypeCode(createDocumentTypeCode("ESPD_REQUEST"));

        return drt;
    }

}
