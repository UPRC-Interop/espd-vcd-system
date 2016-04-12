package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.NaturalPerson;
import eu.esens.espdvcd.model.PostalAddress;
import eu.esens.espdvcd.model.requirement.response.Responses;
import eu.esens.espdvcd.model.SimpleESPDResponse;
import eu.esens.espdvcd.model.requirement.response.AmountResponse;
import eu.esens.espdvcd.model.requirement.response.CountryCodeResponse;
import eu.esens.espdvcd.model.requirement.response.DateResponse;
import eu.esens.espdvcd.model.requirement.response.DescriptionResponse;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLCodeResponse;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLResponse;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;
import eu.esens.espdvcd.model.requirement.response.PercentageResponse;
import eu.esens.espdvcd.model.requirement.response.PeriodResponse;
import eu.esens.espdvcd.model.requirement.response.QuantityIntegerResponse;
import eu.esens.espdvcd.model.requirement.response.QuantityResponse;
import eu.esens.espdvcd.model.requirement.response.QuantityYearResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.response.Response;
import grow.names.specification.ubl.schema.xsd.espd_commonaggregatecomponents_1.EconomicOperatorPartyType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.ResponseType;
import java.util.stream.Collectors;

public class ESPDResponseModelExtractor implements ModelExtractor {

    ESPDResponseModelExtractor() {
    }

    ;

    public SimpleESPDResponse extractESPDResponse(ESPDResponseType resType) {

        SimpleESPDResponse res = new SimpleESPDResponse();

        res.getFullCriterionList().addAll(resType.getCriterion().stream()
                .map(c -> extractSelectableCriterion(c))
                .collect(Collectors.toList()));

        res.setCADetails(extractCADetails(resType.getContractingParty(),
                resType.getContractFolderID(),
                resType.getProcurementProjectLot().get(0),
                resType.getAdditionalDocumentReference()));

        if (resType.getEconomicOperatorParty() != null) {
            res.setEODetails(extractEODetails(resType.getEconomicOperatorParty()));
        }
        return res;
    }

    @Override
    public Requirement extractRequirement(RequirementType rt) {

        Requirement r = ModelExtractor.super.extractRequirement(rt);

        if (!rt.getResponse().isEmpty()) {
            r.setResponse(extractResponse(rt.getResponse().get(0), Responses.Type.valueOf(rt.getResponseDataType())));
        }

        return r;
    }

    public Response extractResponse(ResponseType res, Responses.Type theType) {

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
                    dResp.setDate(res.getDate().getValue().toGregorianCalendar().getTime());
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
                //TODO: NULL Checks and empty list checks
                if (res.getPeriod() != null
                        && !res.getPeriod().getDescription().isEmpty()
                        && res.getPeriod().getDescription().get(0).getValue() != null) {

                    perResp.setDescription(res.getPeriod().getDescription().get(0).getValue());
                }
                return perResp;

            case EVIDENCE_URL:
                EvidenceURLResponse eResp = new EvidenceURLResponse();
                //TODO: NULL Checks and empty list checks
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

    public EODetails extractEODetails(EconomicOperatorPartyType eop) {

        EODetails eoDetails = null;
        
        if (eop != null) {
            eoDetails = new EODetails();
            
            if (eop.getSMEIndicator() != null) {
                eoDetails.setSmeIndicator(eop.getSMEIndicator().isValue());
            }
            
            if (eop.getParty() != null) {
                if (!eop.getParty().getPartyName().isEmpty()
                        && eop.getParty().getPartyName().get(0).getName() != null) {
                    eoDetails.setName(eop.getParty().getPartyName().get(0).getName().getValue());
                }

                if (eop.getParty().getWebsiteURI() != null) {
                    eoDetails.setElectronicAddressID(eop.getParty().getWebsiteURI().getValue());
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

                    if (eop.getParty().getPostalAddress().getPostbox() != null) {
                        eoAddress.setPostCode(eop.getParty().getPostalAddress().getPostbox().getValue());
                    }

                    if (eop.getParty().getPostalAddress().getCityName() != null) {
                        eoAddress.setCity(eop.getParty().getPostalAddress().getCityName().getValue());
                    }

                    if (eop.getParty().getPostalAddress().getCountry() != null
                            && eop.getParty().getPostalAddress().getCountry().getIdentificationCode() != null) {
                        eoAddress.setCountryCode(eop.getParty().getPostalAddress().getCountry().getIdentificationCode().getValue());
                    }

                    if (eop.getParty().getContact() != null) {
                        //TODO: Add Contact Details
                    }

                }
            }
            
            if (!eop.getRepresentativeNaturalPerson().isEmpty()) {
                NaturalPerson np = new NaturalPerson();
                np.setRole(eop.getRepresentativeNaturalPerson().get(0).getNaturalPersonRoleDescription().getValue());
            }
        }

        return eoDetails;
    }
}
