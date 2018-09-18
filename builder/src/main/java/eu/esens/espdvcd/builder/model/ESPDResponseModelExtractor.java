/**
 * Copyright 2016-2018 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.codelist.enums.EORoleTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.*;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.RequirementType;
import eu.espd.schema.v1.commonaggregatecomponents_2.DocumentReferenceType;
import eu.espd.schema.v1.commonaggregatecomponents_2.PersonType;
import eu.espd.schema.v1.commonaggregatecomponents_2.ProcurementProjectLotType;
import eu.espd.schema.v1.espd_commonaggregatecomponents_1.EconomicOperatorPartyType;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import eu.espd.schema.v2.pre_award.commonaggregate.EvidenceType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionResponseType;
import eu.espd.schema.v2.pre_award.commonbasic.ConfidentialityLevelCodeType;
import eu.espd.schema.v2.pre_award.commonbasic.ValidatedCriterionPropertyIDType;
import eu.espd.schema.v2.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ESPDResponseModelExtractor implements ModelExtractor {

    Logger logger = Logger.getLogger(ESPDResponseModelExtractor.class.getCanonicalName());

    /* package private constructor. Create only through factory */
    ESPDResponseModelExtractor() {
    }

    public ESPDResponse extractESPDResponse(ESPDResponseType resType) {

        RegulatedESPDResponse modelResponse = new RegulatedESPDResponse();

        modelResponse.getFullCriterionList().addAll(resType.getCriterion().stream()
                .map(c -> extractSelectableCriterion(c))
                .collect(Collectors.toList()));

        modelResponse.setCADetails(extractCADetails(resType.getContractingParty(),
                resType.getContractFolderID(),
                resType.getAdditionalDocumentReference()));

        modelResponse.setServiceProviderDetails(extractServiceProviderDetails(resType.getServiceProviderParty()));

        if (resType.getEconomicOperatorParty() != null) {
            modelResponse.setEODetails(extractEODetails(resType.getEconomicOperatorParty(), resType.getProcurementProjectLot().get(0)));
        } else {
            applyEODetailsStructure(modelResponse);
        }

        if (resType.getCustomizationID().getValue().equals("urn:www.cenbii.eu:transaction:biitrns092:ver3.0")) {
            // ESPD response detected (by checking the customization id)

            if (resType.getAdditionalDocumentReference() != null && !resType.getAdditionalDocumentReference().isEmpty()) {

                // Find an entry with ESPD_REQUEST Value
                Optional<eu.espd.schema.v1.commonaggregatecomponents_2.DocumentReferenceType> optRef = resType.getAdditionalDocumentReference().stream().
                        filter(r -> r.getDocumentTypeCode() != null && r.getDocumentTypeCode().getValue().
                                equals("ESPD_REQUEST")).findFirst();
                optRef.ifPresent(documentReferenceType -> modelResponse.setESPDRequestDetails(extractESPDRequestDetails(documentReferenceType)));
            }
        } else {
            // else an ESPD request is assumed
            modelResponse.setESPDRequestDetails(extractESPDRequestDetails(resType));
        }


        return modelResponse;
    }

    public ESPDResponse extractESPDResponse(QualificationApplicationResponseType qarType) {

        RegulatedESPDResponse modelResponse = new RegulatedESPDResponse();

        modelResponse.getFullCriterionList().addAll(qarType.getTenderingCriterion().stream()
                .map(c -> extractSelectableCriterion(c))
                .collect(Collectors.toList()));

        // Contracting Authority Details
        modelResponse.setCADetails(extractCADetails(qarType.getContractingParty(),
                qarType.getContractFolderID(),
                qarType.getAdditionalDocumentReference()));

        // Service Provider Party Details extraction
        if (!qarType.getContractingParty().isEmpty()
                && qarType.getContractingParty().get(0).getParty() != null
                && !qarType.getContractingParty().get(0).getParty().getServiceProviderParty().isEmpty()) {

            modelResponse.setServiceProviderDetails(extractServiceProviderDetails(qarType.getContractingParty()));
        }

        // Economic Operator Details
        if (!qarType.getEconomicOperatorParty().isEmpty()) {
            modelResponse.setEODetails(extractEODetails(qarType.getEconomicOperatorParty().get(0), qarType.getProcurementProjectLot().get(0)));
        } else {
            applyEODetailsStructure(modelResponse);
        }

        // Create a Map with key -> ValidatedCriterionPropertyID , value -> TenderingCriterionResponseType in order to use it
        // during responses extraction process
        final Map<String, TenderingCriterionResponseType> tcrTypeMap = qarType.getTenderingCriterionResponse().stream()
                .collect(Collectors.toMap(tcrType -> tcrType.getValidatedCriterionPropertyID().getValue(), Function.identity()));

        // extract all responses
        modelResponse.getFullCriterionList()    // loop through all criteria
                .forEach(sc -> sc.getRequirementGroups()    // loop through all RequirementGroups of current criterion
                        .forEach(rg -> extractAllRequirements(rg, null) // extract all Requirements of current RequirementGroup
                                .forEach(rq -> { // loop thought all of the extracted Requirements

                                    if (tcrTypeMap.containsKey(rq.getID())) { // try to find a response for that requirement
                                        rq.setResponse(extractResponse(tcrTypeMap.get(rq.getID()), rq.getResponseDataType()));
                                    }

                                })));


        // extract all evidences
        modelResponse.getEvidenceList().addAll(qarType.getEvidence().stream()
                .map(evType -> extractEvidence(evType))
                .collect(Collectors.toList()));

        if (qarType.getCustomizationID().getValue().equals("urn:www.cenbii.eu:transaction:biitrdm070:ver3.0")) {
            // ESPD response detected (by checking the customization id)

            if (qarType.getAdditionalDocumentReference() != null && !qarType.getAdditionalDocumentReference().isEmpty()) {

                // Find an entry with ESPD_REQUEST Value
                Optional<eu.espd.schema.v2.pre_award.commonaggregate.DocumentReferenceType> optRef = qarType.getAdditionalDocumentReference().stream().
                        filter(r -> r.getDocumentTypeCode() != null && r.getDocumentTypeCode().getValue().
                                equals("ESPD_REQUEST")).findFirst();
                optRef.ifPresent(documentReferenceType -> modelResponse.setESPDRequestDetails(extractESPDRequestDetails(documentReferenceType)));
            }
        } else {
            // else an ESPD request is assumed
            modelResponse.setESPDRequestDetails(extractESPDRequestDetails(qarType));
        }

        return modelResponse;
    }

    private void applyEODetailsStructure(RegulatedESPDResponse modelResponse) {
        EODetails eod = new EODetails();
        eod.setContactingDetails(new ContactingDetails());
        eod.setPostalAddress(new PostalAddress());
        eod.setNaturalPersons(new ArrayList<>());

        NaturalPerson np = new NaturalPerson();
        np.setPostalAddress(new PostalAddress());
        np.setContactDetails(new ContactingDetails());

        eod.getNaturalPersons().add(np);
        modelResponse.setEODetails(eod);
    }

    protected List<Requirement> extractAllRequirements(RequirementGroup rg, List<Requirement> requirementList) {

        if (requirementList == null) {
            requirementList = new ArrayList<>();
        }

        requirementList.addAll(rg.getRequirements());

        for (RequirementGroup subRg : rg.getRequirementGroups()) {
            extractAllRequirements(subRg, requirementList);
        }

        return requirementList;
    }

    public Evidence extractEvidence(@NotNull EvidenceType evType) {

        Evidence evidence = new Evidence();

        if (evType.getID() != null
                && evType.getID().getValue() != null) {

            evidence.setID(evType.getID().getValue());
        }

        if (!evType.getDescription().isEmpty()
                && evType.getDescription().get(0).getValue() != null) {

            evidence.setDescription(evType.getDescription().get(0).getValue());
        }

        if (evType.getConfidentialityLevelCode() != null
                && evType.getConfidentialityLevelCode().getValue() != null) {

            evidence.setConfidentialityLevelCode(evType.getConfidentialityLevelCode().getValue());
        }

        if (!evType.getDocumentReference().isEmpty()) {

            if (evType.getDocumentReference().get(0).getAttachment() != null
                    && evType.getDocumentReference().get(0).getAttachment().getExternalReference() != null
                    && evType.getDocumentReference().get(0).getAttachment().getExternalReference().getURI() != null
                    && evType.getDocumentReference().get(0).getAttachment().getExternalReference().getURI().getValue() != null) {

                evidence.setEvidenceURL(evType.getDocumentReference().get(0).getAttachment().getExternalReference().getURI().getValue());
            }

            if (evType.getDocumentReference().get(0).getIssuerParty() != null) {

                if (!evType.getDocumentReference().get(0).getIssuerParty().getPartyName().isEmpty()
                        && evType.getDocumentReference().get(0).getIssuerParty().getPartyName().get(0).getName() != null
                        && evType.getDocumentReference().get(0).getIssuerParty().getPartyName().get(0).getName().getValue() != null) {

                    evidence.setEvidenceIssuer(new EvidenceIssuerDetails());
                    evidence.getEvidenceIssuer().setName(evType.getDocumentReference().get(0).getIssuerParty().getPartyName().get(0).getName().getValue());
                }

                if (!evType.getDocumentReference().get(0).getIssuerParty().getPartyIdentification().isEmpty()
                        && evType.getDocumentReference().get(0).getIssuerParty().getPartyIdentification().get(0).getID() != null
                        && evType.getDocumentReference().get(0).getIssuerParty().getPartyIdentification().get(0).getID().getValue() != null) {

                    if (evidence.getEvidenceIssuer() == null) {
                        evidence.setEvidenceIssuer(new EvidenceIssuerDetails());
                    }
                    evidence.getEvidenceIssuer().setID(evType.getDocumentReference().get(0).getIssuerParty().getPartyIdentification().get(0).getID().getValue());
                }

                if (evType.getDocumentReference().get(0).getIssuerParty().getWebsiteURI() != null
                        && evType.getDocumentReference().get(0).getIssuerParty().getWebsiteURI().getValue() != null) {

                    if (evidence.getEvidenceIssuer() == null) {
                        evidence.setEvidenceIssuer(new EvidenceIssuerDetails());
                    }
                    evidence.getEvidenceIssuer().setWebsite(evType.getDocumentReference().get(0).getIssuerParty().getWebsiteURI().getValue());
                }

            }

        }

        return evidence;
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

    private void applyValidatedCriterionPropertyID(final ValidatedCriterionPropertyIDType vcpIDType, final Response resp) {

        if (vcpIDType != null && vcpIDType.getValue() != null) {
            resp.setValidatedCriterionPropertyID(vcpIDType.getValue());
        }
    }

    private void applyConfidentialityLevelCode(final ConfidentialityLevelCodeType clcType, final Response resp) {

        if (clcType != null && clcType.getValue() != null) {
            resp.setConfidentialityLevelCode(clcType.getValue());
        }
    }

    public Response extractResponse(TenderingCriterionResponseType res, ResponseTypeEnum theType) {

        switch (theType) {

            case INDICATOR:
                IndicatorResponse resp = new IndicatorResponse();
                if (res.getResponseValue().get(0).getResponseIndicator() != null) {
                    resp.setIndicator(res.getResponseValue().get(0).getResponseIndicator().isValue());
                }
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), resp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), resp);
                resp.setResponseType(theType);
                return resp;

            case DATE:
                DateResponse dResp = new DateResponse();
                if (res.getResponseValue().get(0).getResponseDate() != null &&
                        res.getResponseValue().get(0).getResponseDate().getValue() != null) {
                    dResp.setDate(res.getResponseValue().get(0).getResponseDate().getValue());
                }
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), dResp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), dResp);
                dResp.setResponseType(theType);
                return dResp;

            case DESCRIPTION:
                DescriptionResponse deResp = new DescriptionResponse();
                if (res.getResponseValue().get(0).getDescription().get(0) != null &&
                        res.getResponseValue().get(0).getDescription().get(0).getValue() != null) {
                    deResp.setDescription(res.getResponseValue().get(0).getDescription().get(0).getValue());
                }
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), deResp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), deResp);
                deResp.setResponseType(theType);
                return deResp;

            case QUANTITY:
                QuantityResponse qResp = new QuantityResponse();
                if (res.getResponseValue().get(0).getResponseQuantity() != null &&
                        res.getResponseValue().get(0).getResponseQuantity().getValue() != null) {
                    qResp.setQuantity(res.getResponseValue().get(0).getResponseQuantity().getValue().floatValue());
                }
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), qResp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), qResp);
                qResp.setResponseType(theType);
                return qResp;

            case QUANTITY_YEAR:
                QuantityYearResponse qyResp = new QuantityYearResponse();
                if (res.getResponseValue().get(0).getResponseQuantity() != null
                        && res.getResponseValue().get(0).getResponseQuantity().getValue() != null) {
                    qyResp.setYear(res.getResponseValue().get(0).getResponseQuantity().getValue().intValueExact());
                }
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), qyResp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), qyResp);
                qyResp.setResponseType(theType);
                return qyResp;

            case QUANTITY_INTEGER:
                QuantityIntegerResponse qiResp = new QuantityIntegerResponse();
                if (res.getResponseValue().get(0).getResponseQuantity() != null
                        && res.getResponseValue().get(0).getResponseQuantity().getValue() != null) {
                    qiResp.setQuantity(res.getResponseValue().get(0).getResponseQuantity().getValue().intValueExact());
                }
                return qiResp;

            case AMOUNT:
                AmountResponse aResp = new AmountResponse();
                if (res.getResponseValue().get(0).getResponseAmount() != null &&
                        res.getResponseValue().get(0).getResponseAmount().getValue() != null) {
                    aResp.setAmount(res.getResponseValue().get(0).getResponseAmount().getValue().floatValue());
                    if (res.getResponseValue().get(0).getResponseAmount().getCurrencyID() != null) {
                        aResp.setCurrency(res.getResponseValue().get(0).getResponseAmount().getCurrencyID());
                    }
                }
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), aResp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), aResp);
                aResp.setResponseType(theType);
                return aResp;

            case CODE_COUNTRY:
                CountryCodeResponse cResp = new CountryCodeResponse();
                if (res.getResponseValue().get(0).getResponseCode() != null &&
                        res.getResponseValue().get(0).getResponseCode().getValue() != null) {
                    cResp.setCountryCode(res.getResponseValue().get(0).getResponseCode().getValue());
                }
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), cResp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), cResp);
                cResp.setResponseType(theType);
                return cResp;

            case PERCENTAGE:
                PercentageResponse pResp = new PercentageResponse();
                if (res.getResponseValue().get(0).getResponseNumeric() != null &&
                        res.getResponseValue().get(0).getResponseNumeric().getValue() != null) {
                    pResp.setPercentage(res.getResponseValue().get(0).getResponseNumeric().getValue().floatValue());
                }
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), pResp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), pResp);
                pResp.setResponseType(theType);
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
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), apResp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), apResp);
                apResp.setResponseType(theType);
                return apResp;

            case CODE:
                EvidenceURLCodeResponse ecResp = new EvidenceURLCodeResponse();
                if (res.getResponseValue().get(0).getResponseCode() != null
                        && res.getResponseValue().get(0).getResponseCode().getValue() != null) {
                    ecResp.setEvidenceURLCode(res.getResponseValue().get(0).getResponseCode().getValue());
                }
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), ecResp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), ecResp);
                ecResp.setResponseType(theType);
                return ecResp;

            case EVIDENCE_IDENTIFIER:
                // in regulated v1 response this maps to EVIDENCE_URL, CODE, DESCRIPTION
                EvidenceIdentifierResponse eiResp = new EvidenceIdentifierResponse();
                if (!res.getEvidenceSupplied().isEmpty()
                        && res.getEvidenceSupplied().get(0).getID() != null
                        && res.getEvidenceSupplied().get(0).getID().getValue() != null) {

                    eiResp.setEvidenceSuppliedId(res.getEvidenceSupplied().get(0).getID().getValue());
                }
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), eiResp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), eiResp);
                eiResp.setResponseType(theType);
                return eiResp;

            case IDENTIFIER:
                IdentifierResponse iResp = new IdentifierResponse();
                if (!res.getResponseValue().isEmpty()
                        && res.getResponseValue().get(0).getResponseID() != null
                        && res.getResponseValue().get(0).getResponseID().getValue() != null) {

                    iResp.setIdentifier(res.getResponseValue().get(0).getResponseID().getValue());
                }
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), iResp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), iResp);
                iResp.setResponseType(theType);
                return iResp;

            case URL:
                URLResponse urlResp = new URLResponse();
                if (!res.getResponseValue().isEmpty()
                        && res.getResponseValue().get(0).getResponseURI() != null
                        && res.getResponseValue().get(0).getResponseURI().getValue() != null) {

                    urlResp.setUrl(res.getResponseValue().get(0).getResponseURI().getValue());
                }
                applyValidatedCriterionPropertyID(res.getValidatedCriterionPropertyID(), urlResp);
                applyConfidentialityLevelCode(res.getConfidentialityLevelCode(), urlResp);
                urlResp.setResponseType(theType);
                return urlResp;

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

    public EODetails extractEODetails(eu.espd.schema.v2.pre_award.commonaggregate.EconomicOperatorPartyType eoPartyType,
                                      eu.espd.schema.v2.pre_award.commonaggregate.ProcurementProjectLotType pplType) {

        final EODetails eoDetails = new EODetails();

        if (eoPartyType != null) {

            // (1) Path: .../cac:EconomicOperatorParty/cac:QualifyingParty
//            if (!eoPartyType.getQualifyingParty().isEmpty()
//                    && eoPartyType.getQualifyingParty().get(0).getParty() != null) {
//
//            }

            // (2) Path: .../cac:EconomicOperatorParty/cac:EconomicOperatorRole
            if (eoPartyType.getEconomicOperatorRole() != null) {

                if (eoPartyType.getEconomicOperatorRole().getRoleCode() != null) {
                    eoDetails.setEoRole(EORoleTypeEnum.valueOf(eoPartyType.getEconomicOperatorRole().getRoleCode().getValue()));
                }

                /*
                 * FIXME Rule: Software applications should retrieve and reuse the description from the Code List EORoleType. There is no description in the referenced codelist
                 */
//                if (!eoPartyType.getEconomicOperatorRole().getRoleDescription().isEmpty()
//                        && eoPartyType.getEconomicOperatorRole().getRoleDescription().get(0).getValue() != null) {
//
//                }

            }

            // (3) Path: .../cac:EconomicOperatorParty/cac:Party
            if (eoPartyType.getParty() != null) {

                if (!eoPartyType.getParty().getPartyName().isEmpty()
                        && eoPartyType.getParty().getPartyName().get(0).getName() != null) {
                    eoDetails.setName(eoPartyType.getParty().getPartyName().get(0).getName().getValue());
                }

                // only criterion is used for tenderer role - changed also in BIS
                /*if (eoPartyType.getEconomicOperatorRoleCode() != null) {
                    eoDetails.setRole(eoPartyType.getEconomicOperatorRoleCode().getValue());
                }*/

                if (eoPartyType.getParty().getIndustryClassificationCode() != null
                        && eoPartyType.getParty().getIndustryClassificationCode().getValue() != null) {
                    eoDetails.setSmeIndicator(isSME(eoPartyType.getParty().getIndustryClassificationCode().getValue()));
                }

                if (eoPartyType.getParty().getWebsiteURI() != null) {
                    eoDetails.setWebSiteURI(eoPartyType.getParty().getWebsiteURI().getValue());
                }

                if (eoPartyType.getParty().getEndpointID() != null) {
                    eoDetails.setElectronicAddressID(eoPartyType.getParty().getEndpointID().getValue());
                }

                if (!eoPartyType.getParty().getPartyIdentification().isEmpty()
                        && eoPartyType.getParty().getPartyIdentification().get(0).getID() != null) {
                    eoDetails.setID(eoPartyType.getParty().getPartyIdentification().get(0).getID().getValue());
                }

                if (eoPartyType.getParty().getPostalAddress() != null) {

                    PostalAddress eoAddress = new PostalAddress();

                    if (eoPartyType.getParty().getPostalAddress().getStreetName() != null) {
                        eoAddress.setAddressLine1(eoPartyType.getParty().getPostalAddress().getStreetName().getValue());
                    }

                    // read post code from cbc:PostalZone...
                    if (eoPartyType.getParty().getPostalAddress().getPostalZone() != null) {
                        eoAddress.setPostCode(eoPartyType.getParty().getPostalAddress().getPostalZone().getValue());
                    }
                    // ...if not available, try cbc:Postbox (for backwards compatibility)
                    else if (eoPartyType.getParty().getPostalAddress().getPostbox() != null) {
                        eoAddress.setPostCode(eoPartyType.getParty().getPostalAddress().getPostbox().getValue());
                    }

                    if (eoPartyType.getParty().getPostalAddress().getCityName() != null) {
                        eoAddress.setCity(eoPartyType.getParty().getPostalAddress().getCityName().getValue());
                    }

                    if (eoPartyType.getParty().getPostalAddress().getCountry() != null
                            && eoPartyType.getParty().getPostalAddress().getCountry().getIdentificationCode() != null) {
                        eoAddress.setCountryCode(eoPartyType.getParty().getPostalAddress().getCountry().getIdentificationCode().getValue());
                    }

                    eoDetails.setPostalAddress(eoAddress);

                    if (eoPartyType.getParty().getContact() != null) {
                        eoDetails.setContactingDetails(new ContactingDetails());
                        if (eoPartyType.getParty().getContact().getName() != null) {
                            eoDetails.getContactingDetails().setContactPointName(eoPartyType.getParty().getContact().getName().getValue());
                        }

                        if (eoPartyType.getParty().getContact().getElectronicMail() != null) {
                            eoDetails.getContactingDetails().setEmailAddress(eoPartyType.getParty().getContact().getElectronicMail().getValue());
                        }

                        if (eoPartyType.getParty().getContact().getTelephone() != null) {
                            eoDetails.getContactingDetails().setTelephoneNumber(eoPartyType.getParty().getContact().getTelephone().getValue());
                        }

                        if (eoPartyType.getParty().getContact().getTelefax() != null) {
                            eoDetails.getContactingDetails().setFaxNumber(eoPartyType.getParty().getContact().getTelefax().getValue());
                        }
                    }

                }
            }

            if (!eoPartyType.getParty().getPowerOfAttorney().isEmpty()) {

                eoPartyType.getParty().getPowerOfAttorney().forEach(poa -> {
                    NaturalPerson np = new NaturalPerson();
                    /* We assume only one. We arbitrarily fetch the first one from the list */

                    if (!poa.getDescription().isEmpty()) {
                        np.setRole(poa.getDescription().get(0).getValue());
                    }

                    /* in ESPD the only look for the person in agent party in power of attorney */
                    if (eoPartyType.getParty().getPowerOfAttorney().get(0).getAgentParty() != null
                            && !eoPartyType.getParty().getPowerOfAttorney().get(0).getAgentParty().getPerson().isEmpty()) {
                        eu.espd.schema.v2.pre_award.commonaggregate.PersonType pt = eoPartyType.getParty().getPowerOfAttorney().get(0).getAgentParty().getPerson().get(0);

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
                NaturalPerson np = new NaturalPerson();
                np.setPostalAddress(new PostalAddress());
                np.getPostalAddress().setCountryCode("NO");
                eoDetails.getNaturalPersons().add(np);
            }
        } else {
            eoDetails.setNaturalPersons(new ArrayList<>());
            NaturalPerson np = new NaturalPerson();
            np.setPostalAddress(new PostalAddress());
            np.getPostalAddress().setCountryCode("NO");
            eoDetails.getNaturalPersons().add(np);
        }

        // Procurement Project Lot
        if (pplType != null && pplType.getID() != null) {
            eoDetails.setProcurementProjectLot(pplType.getID().getValue());
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
    private ESPDRequestDetails extractESPDRequestDetails(eu.espd.schema.v2.pre_award.commonaggregate.DocumentReferenceType drt) {
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
