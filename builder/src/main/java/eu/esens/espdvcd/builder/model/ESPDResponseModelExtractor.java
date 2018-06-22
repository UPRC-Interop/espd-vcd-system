package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.*;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.RequirementType;
import eu.espd.schema.v1.commonaggregatecomponents_2.DocumentReferenceType;
import eu.espd.schema.v1.commonaggregatecomponents_2.PersonType;
import eu.espd.schema.v1.commonaggregatecomponents_2.ProcurementProjectLotType;
import eu.espd.schema.v1.espd_commonaggregatecomponents_1.EconomicOperatorPartyType;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import eu.espd.schema.v2.pre_award.commonaggregate.EvidenceType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionResponseType;
import eu.espd.schema.v2.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ESPDResponseModelExtractor implements ModelExtractor {

    /* package private constructor. Create only through factory */
    ESPDResponseModelExtractor() {
    }

    public ESPDResponse extractESPDResponse(ESPDResponseType resType) {

        RegulatedESPDResponse res = new RegulatedESPDResponse();

        res.getFullCriterionList().addAll(resType.getCriterion().stream()
                .map(c -> extractSelectableCriterion(c))
                .collect(Collectors.toList()));

        res.setCADetails(extractCADetails(resType.getContractingParty(),
                resType.getContractFolderID(),
                resType.getAdditionalDocumentReference()));

        res.setServiceProviderDetails(extractServiceProviderDetails(resType.getServiceProviderParty()));

        if (resType.getEconomicOperatorParty() != null) {
            res.setEODetails(extractEODetails(resType.getEconomicOperatorParty(), resType.getProcurementProjectLot().get(0)));
        } else {
            EODetails eod = new EODetails();
            eod.setContactingDetails(new ContactingDetails());
            eod.setPostalAddress(new PostalAddress());
            eod.setNaturalPersons(new ArrayList<>());

            NaturalPerson np = new NaturalPerson();
            np.setPostalAddress(new PostalAddress());
            np.setContactDetails(new ContactingDetails());

            eod.getNaturalPersons().add(np);
            res.setEODetails(eod);
        }

        if (resType.getCustomizationID().getValue().equals("urn:www.cenbii.eu:transaction:biitrns092:ver3.0")) {
            // ESPD response detected (by checking the customization id)

            if (resType.getAdditionalDocumentReference() != null && !resType.getAdditionalDocumentReference().isEmpty()) {

                // Find an entry with ESPD_REQUEST Value
                Optional<eu.espd.schema.v1.commonaggregatecomponents_2.DocumentReferenceType> optRef = resType.getAdditionalDocumentReference().stream().
                        filter(r -> r.getDocumentTypeCode() != null && r.getDocumentTypeCode().getValue().
                                equals("ESPD_REQUEST")).findFirst();
                optRef.ifPresent(documentReferenceType -> res.setESPDRequestDetails(extractESPDRequestDetails(documentReferenceType)));
            }
        } else {
            // else an ESPD request is assumed
            res.setESPDRequestDetails(extractESPDRequestDetails(resType));
        }


        return res;
    }

    public ESPDResponse extractESPDResponse(QualificationApplicationResponseType qarType) {

        RegulatedESPDResponse regulatedResponse = new RegulatedESPDResponse();

        regulatedResponse.getFullCriterionList().addAll(qarType.getTenderingCriterion().stream()
                .map(c -> extractSelectableCriterion(c))
                .collect(Collectors.toList()));

        regulatedResponse.setCADetails(extractCADetails(qarType.getContractingParty(),
                qarType.getContractFolderID(),
                qarType.getAdditionalDocumentReference()));

        regulatedResponse.setServiceProviderDetails(extractServiceProviderDetails(qarType.getContractingParty()));

        if (!qarType.getEconomicOperatorParty().isEmpty()) {
            // FIXME we assure 1 here
            regulatedResponse.setEODetails(extractEODetails(qarType.getEconomicOperatorParty().get(0), qarType.getProcurementProjectLot().get(0)));
        } else {
            EODetails eod = new EODetails();
            eod.setContactingDetails(new ContactingDetails());
            eod.setPostalAddress(new PostalAddress());
            eod.setNaturalPersons(new ArrayList<>());

            NaturalPerson np = new NaturalPerson();
            np.setPostalAddress(new PostalAddress());
            np.setContactDetails(new ContactingDetails());

            eod.getNaturalPersons().add(np);
            regulatedResponse.setEODetails(eod);
        }

        // extract all responses
        regulatedResponse.getFullCriterionList().forEach(sc -> sc.getRequirementGroups()
                .forEach(rg -> applyAllResponse(qarType, rg)));

        // extract all evidences

        if (qarType.getCustomizationID().getValue().equals("urn:www.cenbii.eu:transaction:biitrdm070:ver3.0")) {
            // ESPD response detected (by checking the customization id)

            if (qarType.getAdditionalDocumentReference() != null && !qarType.getAdditionalDocumentReference().isEmpty()) {

                // Find an entry with ESPD_REQUEST Value
                Optional<eu.espd.schema.v2.pre_award.commonaggregate.DocumentReferenceType> optRef = qarType.getAdditionalDocumentReference().stream().
                        filter(r -> r.getDocumentTypeCode() != null && r.getDocumentTypeCode().getValue().
                                equals("ESPD_REQUEST")).findFirst();
                optRef.ifPresent(documentReferenceType -> regulatedResponse.setESPDRequestDetails(extractESPDRequestDetails(documentReferenceType)));
            }
        } else {
            // else an ESPD request is assumed
            regulatedResponse.setESPDRequestDetails(extractESPDRequestDetails(qarType));
        }

        return regulatedResponse;
    }

    private void applyAllResponse(final QualificationApplicationResponseType qarType,
                                  final RequirementGroup rg) {

        for (Requirement rq : rg.getRequirements()) { //  loop through all requirements
            boolean isFound = false;

            for (TenderingCriterionResponseType res : qarType.getTenderingCriterionResponse()) { //  loop through all responses

                if (rq.getID().equals(res.getValidatedCriterionPropertyID().getValue())) {
                    rq.setResponse(extractResponse(res, rq.getResponseDataType()));
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {

                // workaround in order to avoid common PERIOD value with different meaning in v1 and v2 EDM
                if (rq.getResponseDataType() == ResponseTypeEnum.PERIOD) {
                    rq.setResponse(new ApplicablePeriodResponse());
                } else {
                    rq.setResponse(ResponseFactory.createResponse(rq.getResponseDataType()));
                }

            }
        }

        rg.getRequirementGroups().forEach(rqGroup -> applyAllResponse(qarType, rqGroup));
    }

    /**
     * @param rt The JAXB RequirementType class to be extracted
     * @return The Extracted @Requirement
     */
    @Override
    public Requirement extractRequirement(RequirementType rt) {

        Requirement r = ModelExtractor.super.extractRequirement(rt);

        if (!rt.getResponse().isEmpty()) {
            r.setResponse(extractResponse(rt.getResponse().get(0), ResponseTypeEnum.valueOf(rt.getResponseDataType())));
        } else {
            r.setResponse(ResponseFactory.createResponse(r.getResponseDataType()));
        }
        return r;
    }

    public Response extractResponse(TenderingCriterionResponseType res,
                                    ResponseTypeEnum theType) {

        if (res.getResponseValue().isEmpty()) {
            return null;
        }

        switch (theType) {

            case INDICATOR:
                IndicatorResponse resp = new IndicatorResponse();
                if (res.getResponseValue().get(0).getResponseIndicator() != null) {
                    resp.setIndicator(res.getResponseValue().get(0).getResponseIndicator().isValue());
                }
                return resp;

            case DATE:
                DateResponse dResp = new DateResponse();
                if (res.getResponseValue().get(0).getResponseDate() != null &&
                        res.getResponseValue().get(0).getResponseDate().getValue() != null) {
                    dResp.setDate(res.getResponseValue().get(0).getResponseDate().getValue());
                }
                return dResp;

            case DESCRIPTION:
                DescriptionResponse deResp = new DescriptionResponse();
                if (res.getResponseValue().get(0).getDescription().get(0) != null &&
                        res.getResponseValue().get(0).getDescription().get(0).getValue() != null) {
                    deResp.setDescription(res.getResponseValue().get(0).getDescription().get(0).getValue());
                }
                return deResp;

            case QUANTITY:
                QuantityResponse qResp = new QuantityResponse();
                if (res.getResponseValue().get(0).getResponseQuantity() != null &&
                        res.getResponseValue().get(0).getResponseQuantity().getValue() != null) {
                    qResp.setQuantity(res.getResponseValue().get(0).getResponseQuantity().getValue().floatValue());
                }
                return qResp;

            case QUANTITY_YEAR:
                QuantityYearResponse qyResp = new QuantityYearResponse();
                if (res.getResponseValue().get(0).getResponseQuantity() != null
                        && res.getResponseValue().get(0).getResponseQuantity().getValue() != null) {
                    qyResp.setYear(res.getResponseValue().get(0).getResponseQuantity().getValue().intValueExact());
                }
                return qyResp;

            case AMOUNT:
                AmountResponse aResp = new AmountResponse();
                if (res.getResponseValue().get(0).getResponseAmount() != null &&
                        res.getResponseValue().get(0).getResponseAmount().getValue() != null) {
                    aResp.setAmount(res.getResponseValue().get(0).getResponseAmount().getValue().floatValue());
                    if (res.getResponseValue().get(0).getResponseAmount().getCurrencyID() != null) {
                        aResp.setCurrency(res.getResponseValue().get(0).getResponseAmount().getCurrencyID());
                    }
                }
                return aResp;

            case CODE_COUNTRY:
                CountryCodeResponse cResp = new CountryCodeResponse();
                if (res.getResponseValue().get(0).getResponseCode() != null &&
                        res.getResponseValue().get(0).getResponseCode().getValue() != null) {
                    cResp.setCountryCode(res.getResponseValue().get(0).getResponseCode().getValue());
                }
                return cResp;

            case PERCENTAGE:
                PercentageResponse pResp = new PercentageResponse();
                if (res.getResponseValue().get(0).getResponseNumeric() != null &&
                        res.getResponseValue().get(0).getResponseNumeric().getValue() != null) {
                    pResp.setPercentage(res.getResponseValue().get(0).getResponseNumeric().getValue().floatValue());
                }
                return pResp;

            case PERIOD:
                ApplicablePeriodResponse apResp = new ApplicablePeriodResponse();
                if (!res.getApplicablePeriod().isEmpty()) {

                    if (res.getApplicablePeriod().get(0).getStartDate() != null
                            && res.getApplicablePeriod().get(0).getStartDate().getValue() != null) {

                        apResp.setStartDate(res.getApplicablePeriod().get(0).getStartDate().getValue());
                    }

                    if (res.getApplicablePeriod().get(0).getEndDate() != null
                            && res.getApplicablePeriod().get(0).getEndDate().getValue() != null) {

                        apResp.setEndDate(res.getApplicablePeriod().get(0).getEndDate().getValue());
                    }

                }
                return apResp;

            case CODE:
                EvidenceURLCodeResponse ecResp = new EvidenceURLCodeResponse();
                if (res.getResponseValue().get(0).getResponseCode() != null
                        && res.getResponseValue().get(0).getResponseCode().getValue() != null) {
                    ecResp.setEvidenceURLCode(res.getResponseValue().get(0).getResponseCode().getValue());
                }
                return ecResp;

            case EVIDENCE_IDENTIFIER:
                // in regulated v1 response this maps to EVIDENCE_URL, CODE, DESCRIPTION
                EvidenceIdentifierResponse eiResp = new EvidenceIdentifierResponse();
                if (!res.getEvidenceSupplied().isEmpty()
                        && res.getEvidenceSupplied().get(0).getID() != null
                        && res.getEvidenceSupplied().get(0).getID().getValue() != null) {

                    eiResp.setEvidenceSuppliedId(res.getEvidenceSupplied().get(0).getID().getValue());
                }
                return eiResp;

            case IDENTIFIER:
                // there is no example for this one and also no mapping in v1

            case URL:
                // in v1 this maps to DESCRIPTION

            default:
                return null;
        }

    }

    public Response extractResponse(eu.espd.schema.v1.ccv_commonaggregatecomponents_1.ResponseType res, ResponseTypeEnum theType) {

        switch (theType) {

            case INDICATOR:
                IndicatorResponse resp = new IndicatorResponse();
                if (res.getIndicator() != null) {
                    resp.setIndicator(res.getIndicator().isValue());
                }
                return resp;

            case DATE:
                DateResponse dResp = new DateResponse();
                if (res.getDate() != null && res.getDate().getValue() != null) {
                    dResp.setDate(res.getDate().getValue());
                }
                return dResp;

            case DESCRIPTION:
                DescriptionResponse deResp = new DescriptionResponse();
                if (res.getDescription() != null && res.getDescription().getValue() != null) {
                    deResp.setDescription(res.getDescription().getValue());
                }
                return deResp;

            case QUANTITY:
                QuantityResponse qResp = new QuantityResponse();
                if (res.getQuantity() != null && res.getQuantity().getValue() != null) {
                    qResp.setQuantity(res.getQuantity().getValue().floatValue());
                }
                return qResp;

            case QUANTITY_YEAR:
                QuantityYearResponse qyResp = new QuantityYearResponse();
                if (res.getQuantity() != null && res.getQuantity().getValue() != null) {
                    qyResp.setYear(res.getQuantity().getValue().intValueExact());
                }
                return qyResp;

            case QUANTITY_INTEGER:
                QuantityIntegerResponse qiResp = new QuantityIntegerResponse();
                if (res.getQuantity() != null && res.getQuantity().getValue() != null) {
                    qiResp.setQuantity(res.getQuantity().getValue().intValueExact());
                }
                return qiResp;

            case AMOUNT:
                AmountResponse aResp = new AmountResponse();
                if (res.getAmount() != null && res.getAmount().getValue() != null) {
                    aResp.setAmount(res.getAmount().getValue().floatValue());
                    if (res.getAmount().getCurrencyID() != null) {
                        aResp.setCurrency(res.getAmount().getCurrencyID());
                    }
                }

                return aResp;

            case CODE_COUNTRY:
                CountryCodeResponse cResp = new CountryCodeResponse();
                if (res.getCode() != null && res.getCode().getValue() != null) {
                    cResp.setCountryCode(res.getCode().getValue());
                }
                return cResp;

            case PERCENTAGE:
                PercentageResponse pResp = new PercentageResponse();
                if (res.getPercent() != null && res.getPercent().getValue() != null) {
                    pResp.setPercentage(res.getPercent().getValue().floatValue());
                }
                return pResp;

            case PERIOD:
                PeriodResponse perResp = new PeriodResponse();

                if (res.getPeriod() != null
                        && !res.getPeriod().getDescription().isEmpty()
                        && res.getPeriod().getDescription().get(0).getValue() != null) {

                    perResp.setDescription(res.getPeriod().getDescription().get(0).getValue());
                }
                return perResp;

            case EVIDENCE_URL:
                return extractEvidenceURLResponse(res);

            case CODE:
                EvidenceURLCodeResponse ecResp = new EvidenceURLCodeResponse();
                if (res.getCode() != null
                        && res.getCode().getValue() != null) {
                    ecResp.setEvidenceURLCode(res.getCode().getValue());
                }
                return ecResp;

            default:
                return null;
        }

    }

    /**
     * Extract the evidence uri response.
     * <p>
     * UL 2017-05-04
     *
     * @param res
     * @return
     */
    protected Response extractEvidenceURLResponse(eu.espd.schema.v1.ccv_commonaggregatecomponents_1.ResponseType res) {
        EvidenceURLResponse eResp = new EvidenceURLResponse();

        if (!res.getEvidence().isEmpty()
                && !res.getEvidence().get(0).getEvidenceDocumentReference().isEmpty()
                && res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getAttachment() != null
                && res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getAttachment().getExternalReference() != null
                && res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getAttachment().getExternalReference().getURI() != null
                && res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getAttachment().getExternalReference().getURI().getValue() != null) {

            eResp.setEvidenceURL(res.getEvidence()
                    .get(0)
                    .getEvidenceDocumentReference()
                    .get(0)
                    .getAttachment()
                    .getExternalReference()
                    .getURI().getValue());
        }

        return eResp;
    }

    public EODetails extractEODetails(EconomicOperatorPartyType eop, ProcurementProjectLotType pplt) {

        final EODetails eoDetails = new EODetails();

        if (eop != null) {

            if (eop.getSMEIndicator() != null) {
                eoDetails.setSmeIndicator(eop.getSMEIndicator().isValue());
            }

            if (eop.getParty() != null) {
                if (!eop.getParty().getPartyName().isEmpty()
                        && eop.getParty().getPartyName().get(0).getName() != null) {
                    eoDetails.setName(eop.getParty().getPartyName().get(0).getName().getValue());
                }

                // only criterion is used for tenderer role - changed also in BIS
                /*if (eop.getEconomicOperatorRoleCode() != null) {
                    eoDetails.setRole(eop.getEconomicOperatorRoleCode().getValue());
                }*/

                if (eop.getParty().getWebsiteURI() != null) {
                    eoDetails.setWebSiteURI(eop.getParty().getWebsiteURI().getValue());
                }

                if (eop.getParty().getEndpointID() != null) {
                    eoDetails.setElectronicAddressID(eop.getParty().getEndpointID().getValue());
                }

                if (!eop.getParty().getPartyIdentification().isEmpty()
                        && eop.getParty().getPartyIdentification().get(0).getID() != null) {
                    eoDetails.setID(eop.getParty().getPartyIdentification().get(0).getID().getValue());
                }

                if (eop.getParty().getPostalAddress() != null) {
                    PostalAddress eoAddress = new PostalAddress();

                    if (eop.getParty().getPostalAddress().getStreetName() != null) {
                        eoAddress.setAddressLine1(eop.getParty().getPostalAddress().getStreetName().getValue());
                    }

                    // read post code from cbc:PostalZone...
                    if (eop.getParty().getPostalAddress().getPostalZone() != null) {
                        eoAddress.setPostCode(eop.getParty().getPostalAddress().getPostalZone().getValue());
                    }
                    // ...if not available, try cbc:Postbox (for backwards compatibility)
                    else if (eop.getParty().getPostalAddress().getPostbox() != null) {
                        eoAddress.setPostCode(eop.getParty().getPostalAddress().getPostbox().getValue());
                    }

                    if (eop.getParty().getPostalAddress().getCityName() != null) {
                        eoAddress.setCity(eop.getParty().getPostalAddress().getCityName().getValue());
                    }

                    if (eop.getParty().getPostalAddress().getCountry() != null
                            && eop.getParty().getPostalAddress().getCountry().getIdentificationCode() != null) {
                        eoAddress.setCountryCode(eop.getParty().getPostalAddress().getCountry().getIdentificationCode().getValue());
                    }

                    eoDetails.setPostalAddress(eoAddress);

                    if (eop.getParty().getContact() != null) {
                        eoDetails.setContactingDetails(new ContactingDetails());
                        if (eop.getParty().getContact().getName() != null) {
                            eoDetails.getContactingDetails().setContactPointName(eop.getParty().getContact().getName().getValue());
                        }

                        if (eop.getParty().getContact().getElectronicMail() != null) {
                            eoDetails.getContactingDetails().setEmailAddress(eop.getParty().getContact().getElectronicMail().getValue());
                        }

                        if (eop.getParty().getContact().getTelephone() != null) {
                            eoDetails.getContactingDetails().setTelephoneNumber(eop.getParty().getContact().getTelephone().getValue());
                        }

                        if (eop.getParty().getContact().getTelefax() != null) {
                            eoDetails.getContactingDetails().setFaxNumber(eop.getParty().getContact().getTelefax().getValue());
                        }
                    }

                }
            }

            if (!eop.getRepresentativeNaturalPerson().isEmpty()) {
                eop.getRepresentativeNaturalPerson().forEach(npt -> {
                    NaturalPerson np = new NaturalPerson();
                    /* We assume only one. We arbitrarily fetch the first one from the list */

                    if (npt.getNaturalPersonRoleDescription() != null) {
                        np.setRole(npt.getNaturalPersonRoleDescription().getValue());
                    }

                    if (npt.getPowerOfAttorney() != null) {

                        /* in ESPD the only look for the person in agent party in power of attorney */
                        if (npt.getPowerOfAttorney().getAgentParty() != null && !npt.getPowerOfAttorney().getAgentParty().getPerson().isEmpty()) {
                            PersonType pt = npt.getPowerOfAttorney().getAgentParty().getPerson().get(0);

                            if (pt.getFirstName() != null) {
                                np.setFirstName(pt.getFirstName().getValue());
                            }

                            if (pt.getFamilyName() != null) {
                                np.setFamilyName(pt.getFamilyName().getValue());
                            }

                            if (pt.getBirthplaceName() != null) {
                                np.setBirthPlace(pt.getBirthplaceName().getValue());
                            }

                            if (pt.getBirthDate() != null) {
                                np.setBirthDate(pt.getBirthDate().getValue());
                            }

                            if (pt.getContact() != null) {
                                ContactingDetails cd = new ContactingDetails();

                                if (pt.getContact().getElectronicMail() != null) {
                                    cd.setEmailAddress(pt.getContact().getElectronicMail().getValue());
                                }

                                if (pt.getContact().getTelephone() != null) {
                                    cd.setTelephoneNumber(pt.getContact().getTelephone().getValue());
                                }
                                np.setContactDetails(cd);
                            }

                            if (pt.getResidenceAddress() != null) {

                                PostalAddress pa = new PostalAddress();

                                // read post code from cbc:PostalZone...
                                if (pt.getResidenceAddress().getPostalZone() != null) {
                                    pa.setPostCode(pt.getResidenceAddress().getPostalZone().getValue());
                                }
                                // ...if not available, try cbc:Postbox (for backwards compatibility)
                                else if (pt.getResidenceAddress().getPostbox() != null) {
                                    pa.setPostCode(pt.getResidenceAddress().getPostbox().getValue());
                                }

                                if (pt.getResidenceAddress().getCityName() != null) {
                                    pa.setCity(pt.getResidenceAddress().getCityName().getValue());
                                }

                                if (pt.getResidenceAddress().getStreetName() != null) {
                                    pa.setAddressLine1(pt.getResidenceAddress().getStreetName().getValue());
                                }

                                if (pt.getResidenceAddress().getCountry() != null
                                        && pt.getResidenceAddress().getCountry().getIdentificationCode() != null) {
                                    pa.setCountryCode(pt.getResidenceAddress().getCountry().getIdentificationCode().getValue());
                                }

                                np.setPostalAddress(pa);
                            }
                        }

                    }
                    eoDetails.getNaturalPersons().add(np);
                });
            } else {
                eoDetails.getNaturalPersons().add(new NaturalPerson());
            }
        } else {
            eoDetails.setNaturalPersons(new ArrayList<>());
            eoDetails.getNaturalPersons().add(new NaturalPerson());
        }

        // Procurement Project Lot
        if (pplt != null && pplt.getID() != null) {
            eoDetails.setProcurementProjectLot(pplt.getID().getValue());
        }

        return eoDetails;
    }

    public EODetails extractEODetails(eu.espd.schema.v2.pre_award.commonaggregate.EconomicOperatorPartyType eop,
                                      eu.espd.schema.v2.pre_award.commonaggregate.ProcurementProjectLotType pplt) {

        final EODetails eoDetails = new EODetails();

        if (eop != null) {

            if (eop.getQualifyingParty().get(0).getParty().getIndustryClassificationCode() != null) {
                eoDetails.setSmeIndicator(isSMEIndicator(eop));
            }

            if (eop.getParty() != null) {
                if (!eop.getParty().getPartyName().isEmpty()
                        && eop.getParty().getPartyName().get(0).getName() != null) {
                    eoDetails.setName(eop.getParty().getPartyName().get(0).getName().getValue());
                }

                // only criterion is used for tenderer role - changed also in BIS
                /*if (eop.getEconomicOperatorRoleCode() != null) {
                    eoDetails.setRole(eop.getEconomicOperatorRoleCode().getValue());
                }*/

                if (eop.getParty().getWebsiteURI() != null) {
                    eoDetails.setWebSiteURI(eop.getParty().getWebsiteURI().getValue());
                }

                if (eop.getParty().getEndpointID() != null) {
                    eoDetails.setElectronicAddressID(eop.getParty().getEndpointID().getValue());
                }

                if (!eop.getParty().getPartyIdentification().isEmpty()
                        && eop.getParty().getPartyIdentification().get(0).getID() != null) {
                    eoDetails.setID(eop.getParty().getPartyIdentification().get(0).getID().getValue());
                }

                if (eop.getParty().getPostalAddress() != null) {
                    PostalAddress eoAddress = new PostalAddress();

                    if (eop.getParty().getPostalAddress().getStreetName() != null) {
                        eoAddress.setAddressLine1(eop.getParty().getPostalAddress().getStreetName().getValue());
                    }

                    // read post code from cbc:PostalZone...
                    if (eop.getParty().getPostalAddress().getPostalZone() != null) {
                        eoAddress.setPostCode(eop.getParty().getPostalAddress().getPostalZone().getValue());
                    }
                    // ...if not available, try cbc:Postbox (for backwards compatibility)
                    else if (eop.getParty().getPostalAddress().getPostbox() != null) {
                        eoAddress.setPostCode(eop.getParty().getPostalAddress().getPostbox().getValue());
                    }

                    if (eop.getParty().getPostalAddress().getCityName() != null) {
                        eoAddress.setCity(eop.getParty().getPostalAddress().getCityName().getValue());
                    }

                    if (eop.getParty().getPostalAddress().getCountry() != null
                            && eop.getParty().getPostalAddress().getCountry().getIdentificationCode() != null) {
                        eoAddress.setCountryCode(eop.getParty().getPostalAddress().getCountry().getIdentificationCode().getValue());
                    }

                    eoDetails.setPostalAddress(eoAddress);

                    if (eop.getParty().getContact() != null) {
                        eoDetails.setContactingDetails(new ContactingDetails());
                        if (eop.getParty().getContact().getName() != null) {
                            eoDetails.getContactingDetails().setContactPointName(eop.getParty().getContact().getName().getValue());
                        }

                        if (eop.getParty().getContact().getElectronicMail() != null) {
                            eoDetails.getContactingDetails().setEmailAddress(eop.getParty().getContact().getElectronicMail().getValue());
                        }

                        if (eop.getParty().getContact().getTelephone() != null) {
                            eoDetails.getContactingDetails().setTelephoneNumber(eop.getParty().getContact().getTelephone().getValue());
                        }

                        if (eop.getParty().getContact().getTelefax() != null) {
                            eoDetails.getContactingDetails().setFaxNumber(eop.getParty().getContact().getTelefax().getValue());
                        }
                    }

                }
            }

            if (!eop.getParty().getPowerOfAttorney().isEmpty()) {
                eop.getParty().getPowerOfAttorney().forEach(poa -> {
                    NaturalPerson np = new NaturalPerson();
                    /* We assume only one. We arbitrarily fetch the first one from the list */

                    if (!poa.getDescription().isEmpty()) {
                        np.setRole(poa.getDescription().get(0).getValue());
                    }



                    /* in ESPD the only look for the person in agent party in power of attorney */
                    if (eop.getParty().getPowerOfAttorney().get(0).getAgentParty() != null
                            && !eop.getParty().getPowerOfAttorney().get(0).getAgentParty().getPerson().isEmpty()) {
                        eu.espd.schema.v2.pre_award.commonaggregate.PersonType pt = eop.getParty().getPowerOfAttorney().get(0).getAgentParty().getPerson().get(0);

                        if (pt.getFirstName() != null) {
                            np.setFirstName(pt.getFirstName().getValue());
                        }

                        if (pt.getFamilyName() != null) {
                            np.setFamilyName(pt.getFamilyName().getValue());
                        }

                        if (pt.getBirthplaceName() != null) {
                            np.setBirthPlace(pt.getBirthplaceName().getValue());
                        }

                        if (pt.getBirthDate() != null) {
                            np.setBirthDate(pt.getBirthDate().getValue());
                        }

                        if (pt.getContact() != null) {
                            ContactingDetails cd = new ContactingDetails();

                            if (pt.getContact().getElectronicMail() != null) {
                                cd.setEmailAddress(pt.getContact().getElectronicMail().getValue());
                            }

                            if (pt.getContact().getTelephone() != null) {
                                cd.setTelephoneNumber(pt.getContact().getTelephone().getValue());
                            }
                            np.setContactDetails(cd);
                        }

                        if (pt.getResidenceAddress() != null) {

                            PostalAddress pa = new PostalAddress();

                            // read post code from cbc:PostalZone...
                            if (pt.getResidenceAddress().getPostalZone() != null) {
                                pa.setPostCode(pt.getResidenceAddress().getPostalZone().getValue());
                            }
                            // ...if not available, try cbc:Postbox (for backwards compatibility)
                            else if (pt.getResidenceAddress().getPostbox() != null) {
                                pa.setPostCode(pt.getResidenceAddress().getPostbox().getValue());
                            }

                            if (pt.getResidenceAddress().getCityName() != null) {
                                pa.setCity(pt.getResidenceAddress().getCityName().getValue());
                            }

                            if (pt.getResidenceAddress().getStreetName() != null) {
                                pa.setAddressLine1(pt.getResidenceAddress().getStreetName().getValue());
                            }

                            if (pt.getResidenceAddress().getCountry() != null
                                    && pt.getResidenceAddress().getCountry().getIdentificationCode() != null) {
                                pa.setCountryCode(pt.getResidenceAddress().getCountry().getIdentificationCode().getValue());
                            }

                            np.setPostalAddress(pa);
                        }
                    }


                    eoDetails.getNaturalPersons().add(np);
                });
            } else {
                eoDetails.getNaturalPersons().add(new NaturalPerson());
            }
        } else {
            eoDetails.setNaturalPersons(new ArrayList<>());
            eoDetails.getNaturalPersons().add(new NaturalPerson());
        }

        // Procurement Project Lot
        if (pplt != null && pplt.getID() != null) {
            eoDetails.setProcurementProjectLot(pplt.getID().getValue());
        }

        return eoDetails;
    }

    /**
     * Extract ESPD Request details from existing Document Reference (when loading an ESPD Response)
     *
     * @param drt
     * @return
     */
    private ESPDRequestDetails extractESPDRequestDetails(DocumentReferenceType drt) {
        ESPDRequestDetails erd = new ESPDRequestDetails();

        if (drt.getID() != null) {
            erd.setID(drt.getID().getValue());
        }

        if (drt.getIssueDate() != null) {
            erd.setIssueDate(drt.getIssueDate().getValue());
        }

        if (drt.getIssueTime() != null) {
            erd.setIssueTime(drt.getIssueTime().getValue());
        }

        if (drt.getDocumentDescription() != null && !drt.getDocumentDescription().isEmpty()) {
            if (drt.getDocumentDescription().get(0) != null) {
                erd.setDescription(drt.getDocumentDescription().get(0).getValue());
            }
        }

        return erd;
    }

    /**
     * Extract ESPD Request details from existing Document Reference (when loading an ESPD Response)
     *
     * @param drt
     * @return
     */
    private ESPDRequestDetails extractESPDRequestDetails
    (eu.espd.schema.v2.pre_award.commonaggregate.DocumentReferenceType drt) {
        ESPDRequestDetails erd = new ESPDRequestDetails();

        if (drt.getID() != null) {
            erd.setID(drt.getID().getValue());
        }

        if (drt.getIssueDate() != null) {
            erd.setIssueDate(drt.getIssueDate().getValue());
        }

        if (drt.getIssueTime() != null) {
            erd.setIssueTime(drt.getIssueTime().getValue());
        }

        if (drt.getDocumentDescription() != null && !drt.getDocumentDescription().isEmpty()) {
            if (drt.getDocumentDescription().get(0) != null) {
                erd.setDescription(drt.getDocumentDescription().get(0).getValue());
            }
        }

        return erd;
    }

    /**
     * Extract ESPD Request details from request document.
     *
     * @param reqType
     * @return
     */
    private ESPDRequestDetails extractESPDRequestDetails(ESPDResponseType reqType) {
        ESPDRequestDetails erd = new ESPDRequestDetails();

        if (reqType.getID() != null) {
            erd.setID(reqType.getID().getValue());
        }

        if (reqType.getIssueDate() != null) {
            erd.setIssueDate(reqType.getIssueDate().getValue());
        }

        if (reqType.getIssueTime() != null) {
            erd.setIssueTime(reqType.getIssueTime().getValue());
        }

        if (reqType.getContractFolderID() != null) {
            erd.setDescription(reqType.getContractFolderID().getValue());
        }

        return erd;
    }

    /**
     * Extract ESPD Request details from request document.
     *
     * @param reqType
     * @return
     */
    private ESPDRequestDetails extractESPDRequestDetails(QualificationApplicationResponseType reqType) {
        ESPDRequestDetails erd = new ESPDRequestDetails();

        if (reqType.getID() != null) {
            erd.setID(reqType.getID().getValue());
        }

        if (reqType.getIssueDate() != null) {
            erd.setIssueDate(reqType.getIssueDate().getValue());
        }

        if (reqType.getIssueTime() != null) {
            erd.setIssueTime(reqType.getIssueTime().getValue());
        }

        if (reqType.getContractFolderID() != null) {
            erd.setDescription(reqType.getContractFolderID().getValue());
        }

        return erd;
    }

}
