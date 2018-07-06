package eu.esens.espdvcd.builder.schema.v2;

import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.codelist.enums.EOIndustryClassificationCodeEnum;
import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
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
import java.util.UUID;
import java.util.stream.Collectors;

public class ESPDResponseSchemaExtractorV2 implements SchemaExtractorV2 {

    private final static Logger log = LoggerFactory.getLogger(ESPDResponseSchemaExtractorV2.class);

    public QualificationApplicationResponseType extractQualificationApplicationResponseType(ESPDResponse modelResponse) {

        final QualificationApplicationResponseType qarType = new QualificationApplicationResponseType();

        if (modelResponse.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
            qarType.setContractFolderID(new ContractFolderIDType());
            qarType.getContractFolderID().setSchemeAgencyID("TeD");
            qarType.getContractFolderID().setValue(modelResponse.getCADetails().getProcurementProcedureFileReferenceNo());
        }

        qarType.getAdditionalDocumentReference().add(extractCADetailsDocumentReference(modelResponse.getCADetails()));

        DocumentReferenceType drt = extractCADetailsNationalDocumentReference(modelResponse.getCADetails());
        if (drt != null) {
            qarType.getAdditionalDocumentReference().add(drt);
        }

        qarType.getContractingParty().add(extractContractingPartyType(modelResponse.getCADetails()));
        qarType.getProcurementProjectLot().add(extractProcurementProjectLot(modelResponse.getEODetails()));

        qarType.getContractingParty().get(0).getParty().getServiceProviderParty()
                .add(extractServiceProviderPartyType(modelResponse.getServiceProviderDetails()));

        qarType.getTenderingCriterion().addAll(modelResponse.getFullCriterionList().stream()
                .filter(sc -> sc.isSelected())
                .map(sc -> extractTenderingCriterion(sc))
                .collect(Collectors.toList()));

        qarType.getEconomicOperatorParty().add(extractEODetails(modelResponse.getEODetails()));

        qarType.getTenderingCriterionResponse().addAll(extractAllTenderingCriterionResponses(modelResponse));

        qarType.getEvidence().addAll(modelResponse.getEvidenceList().stream()
                .map(ev -> extractEvidenceType(ev))
                .collect(Collectors.toList()));

        if (modelResponse.getESPDRequestDetails() != null) {
            qarType.getAdditionalDocumentReference().add(extractESPDRequestDetails(modelResponse.getESPDRequestDetails()));
        }

        qarType.setUBLVersionID(createUBL22VersionIdType());
        qarType.setCustomizationID(createCENBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrdm092:ver3.0"));
        qarType.setVersionID(createVersionIDType("2018.01.01"));
        qarType.setProfileExecutionID(createProfileExecutionIDType(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED));
        qarType.setQualificationApplicationTypeCode(createQualificationApplicationTypeCodeType(QualificationApplicationTypeEnum.REGULATED));
        qarType.setCopyIndicator(new CopyIndicatorType());
        qarType.getCopyIndicator().setValue(false);

        return qarType;
    }

    public List<TenderingCriterionResponseType> extractAllTenderingCriterionResponses(final ESPDResponse response) {
        List<TenderingCriterionResponseType> tcrTypeList = new ArrayList<>();
        response.getFullCriterionList().forEach(sc -> tcrTypeList.addAll(extractAllTenderingCriterionResponses(sc.getRequirementGroups())));
        return tcrTypeList;
    }

    public List<TenderingCriterionResponseType> extractAllTenderingCriterionResponses(final List<RequirementGroup> rgList) {
        final List<TenderingCriterionResponseType> tcrTypeList = new ArrayList<>();
        rgList.forEach(rg -> tcrTypeList.addAll(extractAllTenderingCriterionResponses(rg)));
        return tcrTypeList;
    }

    public List<TenderingCriterionResponseType> extractAllTenderingCriterionResponses(final RequirementGroup rg) {
        return extractAllRequirements(rg, null).stream()
                .map(rq -> extractTenderingCriterionResponse(rq.getResponse(), rq.getResponseDataType()))
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

    public EconomicOperatorPartyType extractEODetails(EODetails eod) {

        if (eod == null) {
            return null;
        }

        EconomicOperatorPartyType eoPartyType = new EconomicOperatorPartyType();
        eoPartyType.getQualifyingParty().add(new QualifyingPartyType());
        eoPartyType.getQualifyingParty().get(0).setParty(new PartyType());
        eoPartyType.setParty(new PartyType());

        if (eod.isSmeIndicator() != null) {
            String icc = eod.isSmeIndicator() ? EOIndustryClassificationCodeEnum.SME.name()
                    : EOIndustryClassificationCodeEnum.LARGE.name();
            eoPartyType.getParty().setIndustryClassificationCode(createIndustryClassificationCodeType(icc));
        }

        if (eod.getID() != null) {
            PartyIdentificationType pit = new PartyIdentificationType();
            pit.setID(new IDType());
            pit.getID().setValue(eod.getID());
            pit.getID().setSchemeAgencyID("EU-COM-GROW");
            eoPartyType.getParty().getPartyIdentification().add(pit);
        }

        if (eod.getName() != null) {
            PartyNameType pnt = new PartyNameType();
            pnt.setName(new NameType());
            pnt.getName().setValue(eod.getName());
            eoPartyType.getParty().getPartyName().add(pnt);
        }

        if (eod.getElectronicAddressID() != null) {
            EndpointIDType eid = new EndpointIDType();
            eid.setSchemeID("ISO/IEC 9834-8:2008 - 4UUID");
            eid.setSchemeAgencyID("EU-COM-GROW");
            eid.setValue(eod.getElectronicAddressID());
            eoPartyType.getParty().setEndpointID(eid);
        }

        if (eod.getWebSiteURI() != null) {
            WebsiteURIType wsuri = new WebsiteURIType();
            wsuri.setValue(eod.getWebSiteURI());
            eoPartyType.getParty().setWebsiteURI(wsuri);
        }

        if (eod.getPostalAddress() != null) {
            AddressType at = new AddressType();

            at.setStreetName(new StreetNameType());
            at.getStreetName().setValue(eod.getPostalAddress().getAddressLine1());

            at.setCityName(new CityNameType());
            at.getCityName().setValue(eod.getPostalAddress().getCity());

            at.setPostalZone(new PostalZoneType());
            at.getPostalZone().setValue(eod.getPostalAddress().getPostCode());

            at.setCountry(new CountryType());
            at.getCountry().setIdentificationCode(createISOCountryIdCodeType(eod.getPostalAddress().getCountryCode()));

            eoPartyType.getParty().setPostalAddress(at);
        }

        if (eod.getContactingDetails() != null) {
            ContactType ct = new ContactType();
            ct.setName(new NameType());
            ct.getName().setValue(eod.getContactingDetails().getContactPointName());

            ct.setTelephone(new TelephoneType());
            ct.getTelephone().setValue(eod.getContactingDetails().getTelephoneNumber());

            ct.setElectronicMail(new ElectronicMailType());
            ct.getElectronicMail().setValue(eod.getContactingDetails().getEmailAddress());

            ct.setTelefax(new TelefaxType());
            ct.getTelefax().setValue(eod.getContactingDetails().getFaxNumber());

            eoPartyType.getParty().setContact(ct);
        }

        eod.getNaturalPersons().forEach(np -> {

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
                    log.error("Could not create XML Date Object", ex);
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
        TenderingCriterionPropertyType req = new TenderingCriterionPropertyType();

        req.setTypeCode(createTypeCodeType(rq.getTypeCode().name()));
        req.setValueDataTypeCode(createValueDataTypeCodeType(rq.getResponseDataType().name()));

        req.getDescription().add(new DescriptionType());
        req.getDescription().get(0).setValue(rq.getDescription());

        req.setID(createCriteriaTaxonomyIDType(rq.getID()));

        return req;
    }

    private TenderingCriterionResponseType extractTenderingCriterionResponse(Response response, ResponseTypeEnum respType) {

        TenderingCriterionResponseType tcrType = new TenderingCriterionResponseType();
        ResponseValueType rvType = new ResponseValueType();
        EvidenceSuppliedType evsType = new EvidenceSuppliedType();

        if (response == null) {
            // return tcrType;
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
        rvType.setID(createDefaultIDType(UUID.randomUUID().toString()));
        rvType.getID().setSchemeID("ISO/IEC 9834-8:2008 - 4UUID");

        switch (respType) {
            case DESCRIPTION:
                String description = ((DescriptionResponse) response).getDescription();
                if (description != null && !description.isEmpty()) {
                    rvType.getDescription().add(new DescriptionType());
                    rvType.getDescription().get(0).setValue(description);
                }
                tcrType.getResponseValue().add(rvType);
                return tcrType;

            case QUANTITY_YEAR:
                if (((QuantityYearResponse) response).getYear() != 0) {
                    rvType.setResponseQuantity(new ResponseQuantityType());
                    rvType.getResponseQuantity().setUnitCode("YEAR");
                    rvType.getResponseQuantity().setValue(BigDecimal.valueOf(((QuantityYearResponse) response).getYear()));
                }
                tcrType.getResponseValue().add(rvType);
                return tcrType;

            case QUANTITY:
                rvType.setResponseQuantity(new ResponseQuantityType());
                rvType.getResponseQuantity().setValue(new BigDecimal(Float.toString(((QuantityResponse) response).getQuantity())));
                tcrType.getResponseValue().add(rvType);
                return tcrType;

            case QUANTITY_INTEGER:
                rvType.setResponseQuantity(new ResponseQuantityType());
                rvType.getResponseQuantity().setUnitCode("NUMBER");
                rvType.getResponseQuantity().setValue(BigDecimal.valueOf(((QuantityIntegerResponse) response).getQuantity()));
                tcrType.getResponseValue().add(rvType);
                return tcrType;

            case AMOUNT:
                float amount = ((AmountResponse) response).getAmount();
                String currency = ((AmountResponse) response).getCurrency();
                if ((amount != 0) ||
                        (currency != null && !currency.isEmpty())) {
                    // Only generate a proper response if for at least one of the variables "amount" and
                    // "currency" a value different from the default is detected.

                    rvType.setResponseAmount(new ResponseAmountType());
                    rvType.getResponseAmount().setValue(new BigDecimal(Float.toString(amount)));
                    rvType.getResponseAmount().setCurrencyID(currency);
                }
                tcrType.getResponseValue().add(rvType);
                return tcrType;

            case INDICATOR:
                rvType.setResponseIndicator(new ResponseIndicatorType());
                rvType.getResponseIndicator().setValue(((IndicatorResponse) response).isIndicator());
                tcrType.getResponseValue().add(rvType);
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
                rvType.setResponseNumeric(new ResponseNumericType());
                rvType.getResponseNumeric().setValue(new BigDecimal(Float.toString(((PercentageResponse) response).getPercentage())));
                tcrType.getResponseValue().add(rvType);
                return tcrType;

            case DATE:
                if (((DateResponse) response).getDate() != null) {
                    LocalDate respDate = ((DateResponse) response).getDate();
                    try {
                        XMLGregorianCalendar xcal = DatatypeFactory.newInstance()
                                .newXMLGregorianCalendarDate(
                                        respDate.getYear(),
                                        respDate.getMonthValue(),
                                        respDate.getDayOfMonth(),
                                        DatatypeConstants.FIELD_UNDEFINED);
                        rvType.setResponseDate(new ResponseDateType());
                        rvType.getResponseDate().setValue(xcal.toGregorianCalendar().toZonedDateTime().toLocalDate());

                    } catch (DatatypeConfigurationException ex) {
                        log.error("Could not create XMLGregorialCalendar Date Object", ex);
                        return null;
                    }
                }
                tcrType.getResponseValue().add(rvType);
                return tcrType;

            case CODE:
                String evidenceURLCode = ((EvidenceURLCodeResponse) response).getEvidenceURLCode();
                if (evidenceURLCode != null && !evidenceURLCode.isEmpty()) {
                    rvType.setResponseCode(new ResponseCodeType());
                    rvType.getResponseCode().setValue(evidenceURLCode);
                }
                tcrType.getResponseValue().add(rvType);
                return tcrType;

            case CODE_COUNTRY:
                String countryCode = ((CountryCodeResponse) response).getCountryCode();
                if (countryCode != null && !countryCode.isEmpty()) {
                    rvType.setResponseCode(new ResponseCodeType());
                    rvType.getResponseCode().setListAgencyID("ISO");
                    rvType.getResponseCode().setListID("ISO 3166-1");
                    rvType.getResponseCode().setListVersionID("2.0.2");
                    rvType.getResponseCode().setValue(countryCode);
                }
                tcrType.getResponseValue().add(rvType);
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

            case URL:

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
            log.error("Could not create XMLGregorianCalendar Date Object", ex);
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
            log.error("Could not create XMLGregorianCalendar Date Object", ex);
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
