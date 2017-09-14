package eu.esens.espdvcd.builder.schema;

import eu.esens.espdvcd.builder.EvidenceHelper;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.requirement.response.DescriptionResponse;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.response.*;
import grow.names.specification.ubl.schema.xsd.espd_commonaggregatecomponents_1.EconomicOperatorPartyType;
import grow.names.specification.ubl.schema.xsd.espd_commonaggregatecomponents_1.NaturalPersonType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.ResponseType;
import isa.names.specification.ubl.schema.xsd.ccv_commonbasiccomponents_1.IndicatorType;
import isa.names.specification.ubl.schema.xsd.cev_commonaggregatecomponents_1.EvidenceType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AttachmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ContactType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ExternalReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PeriodType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PersonType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PowerOfAttorneyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESPDResponseSchemaExtractor implements SchemaExtractor {

    private final static Logger log = LoggerFactory.getLogger(ESPDRequestSchemaExtractor.class);

    public ESPDResponseType extractESPDResponseType(ESPDResponse res) {

        ESPDResponseType resType = new ESPDResponseType();

        if (res.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
            resType.setContractFolderID(new ContractFolderIDType());
            resType.getContractFolderID().setSchemeAgencyID("TeD");
            resType.getContractFolderID().setValue(res.getCADetails().getProcurementProcedureFileReferenceNo());
        }

        resType.getAdditionalDocumentReference().add(extractCADetailsDocumentReferece(res.getCADetails()));
        resType.setContractingParty(extractContractingPartyType(res.getCADetails()));
        resType.getProcurementProjectLot().add(extractProcurementProjectLot(res.getCADetails()));
        resType.setServiceProviderParty(extractServiceProviderPartyType(res.getServiceProviderDetails()));
        resType.getCriterion().addAll(res.getFullCriterionList().stream()
                .filter(cr -> cr.isSelected())
                .map(cr -> extractCriterion(cr))
                .collect(Collectors.toList()));

        resType.setEconomicOperatorParty(extracEODetails(res.getEODetails()));

        resType.setUBLVersionID(createUBL21VersionIdType());

        resType.setCustomizationID(createBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrns092:ver3.0"));
        resType.setVersionID(createVersionIDType("2017.01.01"));

        resType.setCopyIndicator(new CopyIndicatorType());
        resType.getCopyIndicator().setValue(false);
        return resType;
    }

    public EconomicOperatorPartyType extracEODetails(EODetails eod) {

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

        // UL bugfix: there was no code for creating EndpointID element for EO
        if (eod.getElectronicAddressID() != null) {
            EndpointIDType eid = new EndpointIDType();
            eid.setValue(eod.getElectronicAddressID());
            eopt.getParty().setEndpointID(eid);
        }

        // UL bugfix: there was no code for creating WebsiteURI element for EO
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

            at.setPostbox(new PostboxType());
            at.getPostbox().setValue(eod.getPostalAddress().getPostCode());

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
            DescriptionType dt = new DescriptionType();
            dt.setValue(np.getPowerOfAttorney());
            npt.getPowerOfAttorney().getDescription().add(dt);

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

                pt.getContact().setTelefax(new TelefaxType());
                pt.getContact().getTelefax().setValue(np.getContactDetails().getFaxNumber());

            }

            if (np.getPostalAddress() != null) {

                pt.setResidenceAddress(new AddressType());
                pt.getResidenceAddress().setPostbox(new PostboxType());
                pt.getResidenceAddress().getPostbox().setValue(np.getPostalAddress().getPostCode());

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

    private ResponseType extractResponse(Response response, ResponseTypeEnum respType) {

        ResponseType rType = new ResponseType();
        if (response == null) {
            return rType;
        }

        switch (respType) {

            case DESCRIPTION:
                if (((DescriptionResponse) response).getDescription() != null) {
                    rType.setDescription(new DescriptionType());
                    rType.getDescription().setValue(((DescriptionResponse) response).getDescription());
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
                rType.getQuantity().setValue(BigDecimal.valueOf(((QuantityResponse) response).getQuantity()));
                return rType;

            case QUANTITY_INTEGER:
                rType.setQuantity(new QuantityType());
                rType.getQuantity().setUnitCode("NUMBER");
                rType.getQuantity().setValue(BigDecimal.valueOf(((QuantityIntegerResponse) response).getQuantity()));
                return rType;

            case AMOUNT:
                rType.setAmount(new AmountType());
                rType.getAmount().setValue(BigDecimal.valueOf(((AmountResponse) response).getAmount()));
                rType.getAmount().setCurrencyID(((AmountResponse) response).getCurrency());
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
                rType.getPercent().setValue(BigDecimal.valueOf(((PercentageResponse) response).getPercentage()));
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
                EvidenceURLCodeResponse localResp = ((EvidenceURLCodeResponse) response);
                if (localResp.getEvidenceURLCode() != null && !localResp.getEvidenceURLCode().isEmpty()) {
                    rType.setCode(new TypeCodeType());
                    rType.getCode().setValue(localResp.getEvidenceURLCode());
                }
                return rType;

            case EVIDENCE_URL:
                if (((EvidenceURLResponse) response).getEvidenceURL() != null) {
                    EvidenceType evType = new EvidenceType();
                    DocumentReferenceType drt = new DocumentReferenceType();
                    drt.setID(new IDType());
                    drt.getID().setValue(UUID.randomUUID().toString());
                    drt.setAttachment(new AttachmentType());
                    drt.getAttachment().setExternalReference(new ExternalReferenceType());
                    drt.getAttachment().getExternalReference().setURI(new URIType());
                    //drt.getAttachment().getExternalReference().getURI().setValue(((EvidenceURLResponse) response).getEvidenceURL());
                    // UL: modification for handling VCD resources
                    drt.getAttachment().getExternalReference().getURI().setValue(
                            EvidenceHelper.transformEvidenceURIFromLocalResourceToASiCResource(
                                    ((EvidenceURLResponse) response).getEvidenceURL()
                            )
                    );
                    evType.getEvidenceDocumentReference().add(drt);
                    rType.getEvidence().add(evType);
                }
                return rType;

            case CODE_COUNTRY:
                rType.setCode(new TypeCodeType());
                rType.getCode().setListAgencyID("ISO");
                rType.getCode().setListID("ISO 3166-1");
                rType.getCode().setListVersionID("1.0");
                rType.getCode().setValue(((CountryCodeResponse) response).getCountryCode());
                return rType;

            default:
                return null;
        }

    }
}
