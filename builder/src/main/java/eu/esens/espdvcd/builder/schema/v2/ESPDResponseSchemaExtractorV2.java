package eu.esens.espdvcd.builder.schema.v2;

import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.ESPDRequestDetails;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.response.*;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ESPDResponseSchemaExtractorV2 implements SchemaExtractorV2 {

    private final static Logger log = LoggerFactory.getLogger(ESPDResponseSchemaExtractorV2.class);

    public QualificationApplicationResponseType extractQualificationApplicationResponseType(ESPDResponse res) {

        final QualificationApplicationResponseType responseType = new QualificationApplicationResponseType();

        if (res.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
            responseType.setContractFolderID(new ContractFolderIDType());
            responseType.getContractFolderID().setSchemeAgencyID("TeD");
            responseType.getContractFolderID().setValue(res.getCADetails().getProcurementProcedureFileReferenceNo());
        }

        responseType.getAdditionalDocumentReference().add(extractCADetailsDocumentReference(res.getCADetails()));

        DocumentReferenceType drt = extractCADetailsNationalDocumentReference(res.getCADetails());
        if (drt != null) {
            responseType.getAdditionalDocumentReference().add(drt);
        }

        responseType.getContractingParty().add(extractContractingPartyType(res.getCADetails()));
        responseType.getProcurementProjectLot().add(extractProcurementProjectLot(res.getEODetails()));
        responseType.getContractingParty().get(0).getParty().getServiceProviderParty()
                .add(extractServiceProviderPartyType(res.getServiceProviderDetails()));
        responseType.getTenderingCriterion().addAll(res.getFullCriterionList().stream()
                .filter(cr -> cr.isSelected())
                .map(cr -> extractTenderingCriterion(cr, responseType))
                .collect(Collectors.toList()));

        responseType.getEconomicOperatorParty().add(extractEODetails(res.getEODetails()));

        if (res.getESPDRequestDetails() != null) {
            responseType.getAdditionalDocumentReference().add(extractESPDRequestDetails(res.getESPDRequestDetails()));
        }


        responseType.setUBLVersionID(createUBL22VersionIdType());

        responseType.setCustomizationID(createCENBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrdm070:ver3.0"));
        responseType.setVersionID(createVersionIDType("2018.01.01"));

        responseType.setCopyIndicator(new CopyIndicatorType());
        responseType.getCopyIndicator().setValue(false);
        return responseType;
    }

    public EconomicOperatorPartyType extractEODetails(EODetails eod) {

        if (eod == null) {
            return null;
        }

        EconomicOperatorPartyType eopt = new EconomicOperatorPartyType();
        eopt.getQualifyingParty().add(new QualifyingPartyType());
        String icc = eod.isSmeIndicator() ? "SME" : "LARGE";

        eopt.getQualifyingParty().get(0).setParty(new PartyType());
        eopt.getQualifyingParty().get(0).getParty()
                .setIndustryClassificationCode(createIndustryClassificationCodeType(
                        CodelistsV2.EOIndustryClassification.getValueForId(icc)));

        eopt.setParty(new PartyType());

        if (eod.getID() != null) {
            PartyIdentificationType pit = new PartyIdentificationType();
            pit.setID(new IDType());
            pit.getID().setValue(eod.getID());
            pit.getID().setSchemeAgencyID("EU-COM-GROW");
            eopt.getParty().getPartyIdentification().add(pit);
        }

        if (eod.getName() != null) {
            PartyNameType pnt = new PartyNameType();
            pnt.setName(new NameType());
            pnt.getName().setValue(eod.getName());
            eopt.getParty().getPartyName().add(pnt);
        }

        if (eod.getElectronicAddressID() != null) {
            EndpointIDType eid = new EndpointIDType();
            eid.setSchemeAgencyID("EU-COM-GROW");
            eid.setValue(eod.getElectronicAddressID());
            eopt.getParty().setEndpointID(eid);
        }

        if (eod.getWebSiteURI() != null) {
            WebsiteURIType wsuri = new WebsiteURIType();
            wsuri.setValue(eod.getWebSiteURI());
            eopt.getParty().setWebsiteURI(wsuri);
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

            eopt.getParty().setPostalAddress(at);
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

            eopt.getParty().setContact(ct);
        }

        eod.getNaturalPersons().forEach(np -> {

            PowerOfAttorneyType poa = new PowerOfAttorneyType();

            poa.getDescription().add(new DescriptionType());
            poa.getDescription().get(0).setValue(np.getRole());

            PartyType apt = new PartyType();
            PersonType pt = new PersonType();
            pt.setFirstName(new FirstNameType());
            pt.getFirstName().setValue(np.getFirstName());

            pt.setFamilyName(new FamilyNameType());
            pt.getFamilyName().setValue(np.getFamilyName());
            if (np.getBirthDate() != null) {
                try {
                    pt.setBirthDate(new BirthDateType());

                    XMLGregorianCalendar xcal = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendarDate(
                                    np.getBirthDate().getYear(),
                                    np.getBirthDate().getMonthValue(),
                                    np.getBirthDate().getDayOfMonth(),
                                    DatatypeConstants.FIELD_UNDEFINED);
                    pt.getBirthDate().setValue(xcal.toGregorianCalendar().toZonedDateTime().toLocalDate());
                } catch (DatatypeConfigurationException ex) {
                    log.error("Could not create XML Date Object", ex);
                }
            }

            pt.setBirthplaceName(new BirthplaceNameType());
            pt.getBirthplaceName().setValue(np.getBirthPlace());

            if (np.getContactDetails() != null) {

                pt.setContact(new ContactType());
                pt.getContact().setTelephone(new TelephoneType());
                pt.getContact().getTelephone().setValue(np.getContactDetails().getTelephoneNumber());

                pt.getContact().setElectronicMail(new ElectronicMailType());
                pt.getContact().getElectronicMail().setValue(np.getContactDetails().getEmailAddress());
            }

            if (np.getPostalAddress() != null) {

                pt.setResidenceAddress(new AddressType());

                pt.getResidenceAddress().setPostalZone(new PostalZoneType());
                pt.getResidenceAddress().getPostalZone().setValue(np.getPostalAddress().getPostCode());

                pt.getResidenceAddress().setStreetName(new StreetNameType());
                pt.getResidenceAddress().getStreetName().setValue(np.getPostalAddress().getAddressLine1());

                pt.getResidenceAddress().setCityName(new CityNameType());
                pt.getResidenceAddress().getCityName().setValue(np.getPostalAddress().getCity());

                pt.getResidenceAddress().setCountry(new CountryType());
                pt.getResidenceAddress().getCountry().setIdentificationCode(createISOCountryIdCodeType(np.getPostalAddress().getCountryCode()));
            }

            apt.getPerson().add(pt);
            poa.setAgentParty(apt);

            eopt.getParty().getPowerOfAttorney().add(poa);
        });
        return eopt;
    }

    @Override
    public TenderingCriterionPropertyType extractTenderingCriterionPropertyType(Requirement r, QualificationApplicationResponseType qarType) {
        TenderingCriterionPropertyType req = new TenderingCriterionPropertyType();

        req.setTypeCode(new TypeCodeType());
        req.getTypeCode().setValue(r.getTypeCode().name());

        req.setValueDataTypeCode(new ValueDataTypeCodeType());
        req.getValueDataTypeCode().setValue(r.getResponseDataType().name());

        req.getDescription().add(new DescriptionType());
        req.getDescription().get(0).setValue(r.getDescription());

        req.setID(createCriteriaTaxonomyIDType(r.getID()));
        qarType.getTenderingCriterionResponse().add(extractTenderingCriterionResponse(qarType, r.getResponse(), r.getResponseDataType(), r.getID()));
        return req;
    }

    private TenderingCriterionResponseType extractTenderingCriterionResponse(QualificationApplicationResponseType qarType,
                                                                             Response response,
                                                                             ResponseTypeEnum respType,
                                                                             String rqID) {

        TenderingCriterionResponseType tcrType = new TenderingCriterionResponseType();
        tcrType.setValidatedCriterionPropertyID(createValidatedCriterionPropertyId(rqID));
        ResponseValueType rvType = new ResponseValueType();
        EvidenceSuppliedType evsType = new EvidenceSuppliedType();

        if (response == null) {
            return tcrType;
        }

        rvType.setID(new IDType());
        rvType.getID().setSchemeAgencyID("EU-COM-GROW");
        rvType.getID().setValue(UUID.randomUUID().toString());

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

    protected eu.espd.schema.v2.pre_award.commonaggregate.EvidenceType extractEvidenceIdentifierResponse(String evidenceId,
                                                                                                         Response response) {

        eu.espd.schema.v2.pre_award.commonaggregate.EvidenceType evType = new eu.espd.schema.v2.pre_award.commonaggregate.EvidenceType();
        eu.espd.schema.v2.pre_award.commonaggregate.DocumentReferenceType drt = new eu.espd.schema.v2.pre_award.commonaggregate.DocumentReferenceType();
        evType.setID(new IDType());
        evType.getID().setSchemeAgencyID("EU-COM-GROW");
        evType.getID().setValue(evidenceId);
        evType.getDocumentReference().add(drt);
        return evType;
    }

    protected EvidenceType extractEvidenceURLResponse(Response response) {
        if (((EvidenceURLResponse) response).getEvidenceURL() != null) {
            EvidenceType evType = new EvidenceType();
            DocumentReferenceType drt = new DocumentReferenceType();
            evType.setID(new IDType());
            evType.getID().setSchemeAgencyID("EU-COM-GROW");
            evType.getID().setValue(UUID.randomUUID().toString());
            drt.setAttachment(new AttachmentType());
            drt.getAttachment().setExternalReference(new ExternalReferenceType());
            drt.getAttachment().getExternalReference().setURI(new URIType());
            drt.getAttachment().getExternalReference().getURI().setValue(((EvidenceURLResponse) response).getEvidenceURL());
            evType.getDocumentReference().add(drt);
            return evType;
        }
        return null;
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
