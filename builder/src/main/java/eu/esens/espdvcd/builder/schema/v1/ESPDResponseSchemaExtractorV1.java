package eu.esens.espdvcd.builder.schema.v1;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.ESPDRequestDetails;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.response.*;
import grow.names.specification.ubl.schema.xsd.espd_commonaggregatecomponents_1.EconomicOperatorPartyType;
import grow.names.specification.ubl.schema.xsd.espd_commonaggregatecomponents_1.NaturalPersonType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.ResponseType;
import isa.names.specification.ubl.schema.xsd.ccv_commonbasiccomponents_1.IndicatorType;
import isa.names.specification.ubl.schema.xsd.cev_commonaggregatecomponents_1.EvidenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.*;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ESPDResponseSchemaExtractorV1 implements SchemaExtractorV1 {

    private final static Logger log = LoggerFactory.getLogger(ESPDResponseSchemaExtractorV1.class);

    public ESPDResponseType extractESPDResponseType(ESPDResponse res) {

        ESPDResponseType resType = new ESPDResponseType();

        if (res.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
            resType.setContractFolderID(new ContractFolderIDType());
            resType.getContractFolderID().setSchemeAgencyID("TeD");
            resType.getContractFolderID().setValue(res.getCADetails().getProcurementProcedureFileReferenceNo());
        }

        resType.getAdditionalDocumentReference().add(extractCADetailsDocumentReference(res.getCADetails()));

        // 2018-03-20 UL: add capabilities to handle National Official Journal
        DocumentReferenceType drt = extractCADetailsNationalDocumentReference(res.getCADetails());
        if (drt != null) {
            resType.getAdditionalDocumentReference().add(drt);
        }

        resType.setContractingParty(extractContractingPartyType(res.getCADetails()));
        resType.getProcurementProjectLot().add(extractProcurementProjectLot(res.getEODetails()));
        resType.setServiceProviderParty(extractServiceProviderPartyType(res.getServiceProviderDetails()));
        resType.getCriterion().addAll(res.getFullCriterionList().stream()
                .filter(cr -> cr.isSelected())
                .map(cr -> extractCriterion(cr))
                .collect(Collectors.toList()));

        resType.setEconomicOperatorParty(extractEODetails(res.getEODetails()));

        if (res.getESPDRequestDetails() != null) {
            resType.getAdditionalDocumentReference().add(extractESPDRequestDetails(res.getESPDRequestDetails()));
        }


        resType.setUBLVersionID(createUBL21VersionIdType());

        resType.setCustomizationID(createBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrns092:ver3.0"));
        resType.setVersionID(createVersionIDType("2017.01.01"));

        resType.setCopyIndicator(new CopyIndicatorType());
        resType.getCopyIndicator().setValue(false);
        return resType;
    }

    public EconomicOperatorPartyType extractEODetails(EODetails eod) {

        if (eod == null) {
            return null;
        }

        EconomicOperatorPartyType eopt = new EconomicOperatorPartyType();
        eopt.setSMEIndicator(new grow.names.specification.ubl.schema.xsd.espd_commonbasiccomponents_1.IndicatorType());
        eopt.getSMEIndicator().setValue(eod.isSmeIndicator());

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

        // only criterion is used for tenderer role - changed also in BIS
        /*if (eod.getRole() != null) {
            TypeCodeType tct = new TypeCodeType();
            tct.setListAgencyID("EU-COM-GROW");
            tct.setListID("TendererRole");
            tct.setListVersionID("1.0.2");
            tct.setValue(eod.getRole());
            eopt.setEconomicOperatorRoleCode(tct);
        }*/

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
            NaturalPersonType npt = new NaturalPersonType();

            npt.setNaturalPersonRoleDescription(new DescriptionType());
            npt.getNaturalPersonRoleDescription().setValue(np.getRole());

            npt.setPowerOfAttorney(new PowerOfAttorneyType());

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
            npt.getPowerOfAttorney().setAgentParty(apt);

            eopt.getRepresentativeNaturalPerson().add(npt);

        });
        return eopt;

    }

    @Override
    public RequirementType extractRequirementType(Requirement r) {

        RequirementType req = new RequirementType();

        req.setResponseDataType(r.getResponseDataType().name());
        req.setDescription(new DescriptionType());
        req.getDescription().setValue(r.getDescription());
        req.setID(createCriterionRelatedIDType(r.getID()));
        req.getResponse().add(extractResponse(r.getResponse(), r.getResponseDataType()));

        return req;
    }

    private isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.ResponseType extractResponse(Response response, ResponseTypeEnum respType) {

        isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.ResponseType rType = new ResponseType();
        if (response == null) {
            return rType;
        }

        rType.setID(new IDType());
        rType.getID().setSchemeAgencyID("EU-COM-GROW");
        rType.getID().setValue(UUID.randomUUID().toString());

        switch (respType) {

            case DESCRIPTION:
                String description = ((DescriptionResponse) response).getDescription();
                if (description != null && !description.isEmpty()) {
                    rType.setDescription(new DescriptionType());
                    rType.getDescription().setValue(description);
                }
                return rType;

            case QUANTITY_YEAR:
                if (((QuantityYearResponse) response).getYear() != 0) {
                    rType.setQuantity(new QuantityType());
                    rType.getQuantity().setUnitCode("YEAR");
                    rType.getQuantity().setValue(BigDecimal.valueOf(((QuantityYearResponse) response).getYear()));
                }
                return rType;

            case QUANTITY:
                rType.setQuantity(new QuantityType());
                //rType.getQuantity().setValue(BigDecimal.valueOf(((QuantityResponse) response).getQuantity()));
                // UL 2017-10-20: workaround for rounding issues with BigDecimal (e.g. 0.005 became 0.004999999888241291)
                rType.getQuantity().setValue(new BigDecimal(Float.toString(((QuantityResponse) response).getQuantity())));
                return rType;

            case QUANTITY_INTEGER:
                rType.setQuantity(new QuantityType());
                rType.getQuantity().setUnitCode("NUMBER");
                rType.getQuantity().setValue(BigDecimal.valueOf(((QuantityIntegerResponse) response).getQuantity()));
                return rType;

            case AMOUNT:
                float amount = ((AmountResponse) response).getAmount();
                String currency = ((AmountResponse) response).getCurrency();
                if ( (amount != 0) ||
                        ( currency != null && !currency.isEmpty() ) ) {
                    // Only generate a proper response if for at least one of the variables "amount" and
                    // "currency" a value different from the default is detected.

                    rType.setAmount(new AmountType());
                    //rType.getAmount().setValue(BigDecimal.valueOf(amount));
                    // UL 2017-10-20: workaround for rounding issues with BigDecimal
                    rType.getAmount().setValue(new BigDecimal(Float.toString(amount)));
                    rType.getAmount().setCurrencyID(currency);
                }
                return rType;

            case INDICATOR:
                rType.setIndicator(new IndicatorType());
                rType.getIndicator().setValue(((IndicatorResponse) response).isIndicator());
                return rType;

            case PERIOD:
                String descr = ((PeriodResponse) response).getDescription();
                if (descr != null && !descr.isEmpty()) {
                    rType.setPeriod(new PeriodType());
                    DescriptionType dt = new DescriptionType();
                    dt.setValue(descr);
                    rType.getPeriod().getDescription().add(dt);
                }
                return rType;

            case PERCENTAGE:
                rType.setPercent(new PercentType());
                //rType.getPercent().setValue(BigDecimal.valueOf(((PercentageResponse) response).getPercentage()));
                // UL 2017-10-20: workaround for rounding issues with BigDecimal
                rType.getPercent().setValue(new BigDecimal(Float.toString(((PercentageResponse) response).getPercentage())));
                return rType;

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
                        rType.setDate(new DateType());
                        rType.getDate().setValue(xcal.toGregorianCalendar().toZonedDateTime().toLocalDate());

                    } catch (DatatypeConfigurationException ex) {
                        log.error("Could not create XMLGregorialCalendar Date Object", ex);
                        return null;
                    }
                }
                return rType;

            case CODE:
                String evidenceURLCode = ((EvidenceURLCodeResponse) response).getEvidenceURLCode();
                if (evidenceURLCode != null && !evidenceURLCode.isEmpty()) {
                    rType.setCode(new TypeCodeType());
                    rType.getCode().setValue(evidenceURLCode);
                }
                return rType;

            case EVIDENCE_URL:
                isa.names.specification.ubl.schema.xsd.cev_commonaggregatecomponents_1.EvidenceType evType = extractEvidenceURLResponse(response);
                if (evType != null) {
                    rType.getEvidence().add(evType);
                }
                return rType;

            case CODE_COUNTRY:
                String countryCode = ((CountryCodeResponse) response).getCountryCode();
                if (countryCode != null && !countryCode.isEmpty()) {
                    rType.setCode(new TypeCodeType());
                    rType.getCode().setListAgencyID("ISO");
                    rType.getCode().setListID("ISO 3166-1");
                    rType.getCode().setListVersionID("1.0");
                    rType.getCode().setValue(countryCode);
                }
                return rType;


            default:
                return null;
        }

    }

    protected isa.names.specification.ubl.schema.xsd.cev_commonaggregatecomponents_1.EvidenceType extractEvidenceURLResponse(Response response) {
        if (((EvidenceURLResponse) response).getEvidenceURL() != null) {
            isa.names.specification.ubl.schema.xsd.cev_commonaggregatecomponents_1.EvidenceType evType = new EvidenceType();
            DocumentReferenceType drt = new DocumentReferenceType();
            drt.setID(new IDType());
            drt.getID().setSchemeAgencyID("EU-COM-GROW");
            drt.getID().setValue(UUID.randomUUID().toString());
            drt.setAttachment(new AttachmentType());
            drt.getAttachment().setExternalReference(new ExternalReferenceType());
            drt.getAttachment().getExternalReference().setURI(new URIType());
            drt.getAttachment().getExternalReference().getURI().setValue(((EvidenceURLResponse) response).getEvidenceURL());
            evType.getEvidenceDocumentReference().add(drt);
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
