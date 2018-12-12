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
package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.codelist.enums.EORoleTypeEnum;
import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.codelist.enums.internal.ArtefactType;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.*;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.RequirementType;
import eu.espd.schema.v1.commonaggregatecomponents_2.DocumentReferenceType;
import eu.espd.schema.v1.commonaggregatecomponents_2.PersonType;
import eu.espd.schema.v1.commonaggregatecomponents_2.ProcurementProjectLotType;
import eu.espd.schema.v1.espd_commonaggregatecomponents_1.EconomicOperatorPartyType;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import eu.espd.schema.v2.v210.commonaggregate.EvidenceType;
import eu.espd.schema.v2.v210.commonaggregate.TenderingCriterionResponseType;
import eu.espd.schema.v2.v210.commonaggregate.TenderingCriterionType;
import eu.espd.schema.v2.v210.qualificationapplicationresponse.QualificationApplicationResponseType;

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

        ESPDResponseImpl modelResponse = new ESPDResponseImpl();

        // apply document details
        modelResponse.setDocumentDetails(new DocumentDetails(EDMVersion.V1, ArtefactType.ESPD_RESPONSE,
                QualificationApplicationTypeEnum.REGULATED));

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

        ESPDResponseImpl modelResponse = new ESPDResponseImpl();

        // apply document details
        if (qarType.getQualificationApplicationTypeCode() != null &&
                qarType.getQualificationApplicationTypeCode().getValue() != null) {
            modelResponse.setDocumentDetails(new DocumentDetails(EDMVersion.V2, ArtefactType.ESPD_RESPONSE
                    , QualificationApplicationTypeEnum.valueOf(qarType.getQualificationApplicationTypeCode().getValue())));
        } else {
            throw new IllegalStateException("Error... Unable to extract Qualification Application Type Code");
        }

        modelResponse.getFullCriterionList().addAll(qarType.getTenderingCriterion().stream()
                .map(c -> extractSelectableCriterion(c))
                .collect(Collectors.toList()));

        // Contracting Authority Details
        modelResponse.setCADetails(extractCADetails(qarType.getContractingParty(),
                qarType.getContractFolderID(),
                qarType.getProcedureCode(),
                qarType.getProcurementProject(),
                qarType.getAdditionalDocumentReference()));

        // Apply global weighting
        modelResponse.getCADetails().getWeightScoringMethodologyNoteList()
                .addAll(qarType.getWeightScoringMethodologyNote().stream()
                        .map(noteType -> noteType.getValue())
                        .collect(Collectors.toList()));

        // apply lots
        modelResponse.getCADetails().setProcurementProjectLots(qarType.getProcurementProjectLot().size());

        if (qarType.getWeightingTypeCode() != null
                && qarType.getWeightingTypeCode().getValue() != null) {
            modelResponse.getCADetails().setWeightingType(qarType.getWeightingTypeCode().getValue());
        }

        // Service Provider Party Details extraction
        if (!qarType.getContractingParty().isEmpty()
                && qarType.getContractingParty().get(0).getParty() != null
                && !qarType.getContractingParty().get(0).getParty().getServiceProviderParty().isEmpty()) {

            modelResponse.setServiceProviderDetails(extractServiceProviderDetails(qarType.getContractingParty()));
        }

        // Economic Operator Details
        if (!qarType.getEconomicOperatorParty().isEmpty() && !qarType.getProcurementProjectLot().isEmpty()) {
            modelResponse.setEODetails(extractEODetails(qarType.getEconomicOperatorParty().get(0)
                    , qarType.getProcurementProjectLot().get(0)
                    , qarType.getEconomicOperatorGroupName())); // This is for SELF-CONTAINED
        } else {
            applyEODetailsStructure(modelResponse);
        }

        // Create a Map with key -> ValidatedCriterionPropertyID , value -> TenderingCriterionResponseType in order to use it
        // during responses extraction process
        final Map<String, TenderingCriterionResponseType> responseTypeMap = qarType.getTenderingCriterionResponse().stream()
                .collect(Collectors.toMap(tcrType -> tcrType.getValidatedCriterionPropertyID().getValue(), Function.identity()));

        // Create a Map with key -> Criterion ID, value -> TenderingCriterion in order to use it during
        // weighting responses extraction process
        final Map<String, TenderingCriterionType> criterionTypeMap = qarType.getTenderingCriterion().stream()
                .collect(Collectors.toMap(criterionType -> criterionType.getID().getValue(), Function.identity()));

        // extract all responses
        modelResponse.getFullCriterionList()    // loop through all criteria
                .forEach(sc -> sc.getRequirementGroups()    // loop through all RequirementGroups of current criterion
                        .forEach(rg -> extractAllRequirements(rg, null) // extract all Requirements of current RequirementGroup
                                .forEach(rq -> { // loop thought all of the extracted Requirements

                                    if (responseTypeMap.containsKey(rq.getID())) { // try to find a response for that requirement
                                        rq.setResponse(extractResponse(responseTypeMap.get(rq.getID()), rq.getResponseDataType(),
                                                criterionTypeMap.get(sc.getID())));
                                    }

                                    // Apply Criterion Level Weighting data
                                    if (rq.getResponseDataType() == ResponseTypeEnum.WEIGHT_INDICATOR) {
                                        WeightIndicatorResponse weightIndResp = new WeightIndicatorResponse();
                                        applyCriterionWeightingData(weightIndResp, criterionTypeMap.get(sc.getID()));
                                        rq.setResponse(weightIndResp);
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
                Optional<eu.espd.schema.v2.v210.commonaggregate.DocumentReferenceType> optRef = qarType.getAdditionalDocumentReference().stream().
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

    private void applyEODetailsStructure(ESPDResponseImpl modelResponse) {
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

    public Response extractResponse(TenderingCriterionResponseType responseType, ResponseTypeEnum theType,
                                    TenderingCriterionType criterionType) {

        switch (theType) {

            case INDICATOR:
                IndicatorResponse indicatorResp = new IndicatorResponse();
                if (responseType.getResponseValue().get(0).getResponseIndicator() != null) {
                    indicatorResp.setIndicator(responseType.getResponseValue().get(0).getResponseIndicator().isValue());
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), indicatorResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), indicatorResp);
                return indicatorResp;

            case DATE:
                DateResponse dateResp = new DateResponse();
                if (responseType.getResponseValue().get(0).getResponseDate() != null &&
                        responseType.getResponseValue().get(0).getResponseDate().getValue() != null) {
                    dateResp.setDate(responseType.getResponseValue().get(0).getResponseDate().getValue());
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), dateResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), dateResp);
                return dateResp;

            case DESCRIPTION:
                DescriptionResponse descriptionResp = new DescriptionResponse();
                if (responseType.getResponseValue().get(0).getDescription().get(0) != null &&
                        responseType.getResponseValue().get(0).getDescription().get(0).getValue() != null) {
                    descriptionResp.setDescription(responseType.getResponseValue().get(0).getDescription().get(0).getValue());
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), descriptionResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), descriptionResp);
                return descriptionResp;

            case QUANTITY:
                QuantityResponse quantityResp = new QuantityResponse();
                if (responseType.getResponseValue().get(0).getResponseQuantity() != null &&
                        responseType.getResponseValue().get(0).getResponseQuantity().getValue() != null) {
                    quantityResp.setQuantity(responseType.getResponseValue().get(0).getResponseQuantity().getValue());
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), quantityResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), quantityResp);
                return quantityResp;

            case QUANTITY_YEAR:
                QuantityYearResponse quantityYearResp = new QuantityYearResponse();
                if (responseType.getResponseValue().get(0).getResponseQuantity() != null
                        && responseType.getResponseValue().get(0).getResponseQuantity().getValue() != null) {
                    quantityYearResp.setYear(responseType.getResponseValue().get(0).getResponseQuantity().getValue().intValueExact());
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), quantityYearResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), quantityYearResp);
                return quantityYearResp;

            case QUANTITY_INTEGER:
                QuantityIntegerResponse quantityIntResp = new QuantityIntegerResponse();
                if (responseType.getResponseValue().get(0).getResponseQuantity() != null
                        && responseType.getResponseValue().get(0).getResponseQuantity().getValue() != null) {
                    quantityIntResp.setQuantity(responseType.getResponseValue().get(0).getResponseQuantity().getValue().intValueExact());
                }
                return quantityIntResp;

            case AMOUNT:
                AmountResponse amountResp = new AmountResponse();
                if (responseType.getResponseValue().get(0).getResponseAmount() != null &&
                        responseType.getResponseValue().get(0).getResponseAmount().getValue() != null) {
                    amountResp.setAmount(responseType.getResponseValue().get(0).getResponseAmount().getValue());
                    if (responseType.getResponseValue().get(0).getResponseAmount().getCurrencyID() != null) {
                        amountResp.setCurrency(responseType.getResponseValue().get(0).getResponseAmount().getCurrencyID());
                    }
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), amountResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), amountResp);
                return amountResp;

            case CODE_COUNTRY:
                CountryCodeResponse codeCountryResp = new CountryCodeResponse();
                if (responseType.getResponseValue().get(0).getResponseCode() != null &&
                        responseType.getResponseValue().get(0).getResponseCode().getValue() != null) {
                    codeCountryResp.setCountryCode(responseType.getResponseValue().get(0).getResponseCode().getValue());
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), codeCountryResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), codeCountryResp);
                return codeCountryResp;

            case PERCENTAGE:
                PercentageResponse percentageResp = new PercentageResponse();
                if (responseType.getResponseValue().get(0).getResponseQuantity() != null &&
                        responseType.getResponseValue().get(0).getResponseQuantity().getValue() != null) {
                    percentageResp.setPercentage(responseType.getResponseValue().get(0).getResponseQuantity().getValue());
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), percentageResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), percentageResp);
                return percentageResp;

            case PERIOD:
                ApplicablePeriodResponse periodResp = new ApplicablePeriodResponse();
                if (!responseType.getApplicablePeriod().isEmpty()) {

                    if (responseType.getApplicablePeriod().get(0).getStartDate() != null
                            && responseType.getApplicablePeriod().get(0).getStartDate().getValue() != null) {

                        periodResp.setStartDate(responseType.getApplicablePeriod().get(0).getStartDate().getValue());
                    }

                    if (responseType.getApplicablePeriod().get(0).getEndDate() != null
                            && responseType.getApplicablePeriod().get(0).getEndDate().getValue() != null) {

                        periodResp.setEndDate(responseType.getApplicablePeriod().get(0).getEndDate().getValue());
                    }

                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), periodResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), periodResp);
                return periodResp;

            case CODE:
                EvidenceURLCodeResponse codeResp = new EvidenceURLCodeResponse();
                if (responseType.getResponseValue().get(0).getResponseCode() != null
                        && responseType.getResponseValue().get(0).getResponseCode().getValue() != null) {
                    codeResp.setEvidenceURLCode(responseType.getResponseValue().get(0).getResponseCode().getValue());
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), codeResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), codeResp);
                return codeResp;

            case EVIDENCE_IDENTIFIER:
                EvidenceIdentifierResponse evidenceIdeResp = new EvidenceIdentifierResponse();
                if (!responseType.getEvidenceSupplied().isEmpty()
                        && responseType.getEvidenceSupplied().get(0).getID() != null
                        && responseType.getEvidenceSupplied().get(0).getID().getValue() != null) {

                    evidenceIdeResp.setEvidenceSuppliedId(responseType.getEvidenceSupplied().get(0).getID().getValue());
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), evidenceIdeResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), evidenceIdeResp);
                return evidenceIdeResp;

            case IDENTIFIER:
                IdentifierResponse identifierResp = new IdentifierResponse();
                if (!responseType.getResponseValue().isEmpty()
                        && responseType.getResponseValue().get(0).getResponseID() != null
                        && responseType.getResponseValue().get(0).getResponseID().getValue() != null) {

                    identifierResp.setIdentifier(responseType.getResponseValue().get(0).getResponseID().getValue());
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), identifierResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), identifierResp);
                return identifierResp;

            case URL:
                URLResponse urlResp = new URLResponse();
                if (!responseType.getResponseValue().isEmpty()
                        && responseType.getResponseValue().get(0).getResponseURI() != null
                        && responseType.getResponseValue().get(0).getResponseURI().getValue() != null) {

                    urlResp.setUrl(responseType.getResponseValue().get(0).getResponseURI().getValue());
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), urlResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), urlResp);
                return urlResp;

            case LOT_IDENTIFIER:
                LotIdentifierResponse lotsIdeResp = new LotIdentifierResponse();
                responseType.getResponseValue().forEach(resValueType -> {

                    if (resValueType.getResponseID() != null
                            && resValueType.getResponseID().getValue() != null) {
                        lotsIdeResp.getLotsList().add(resValueType.getResponseID().getValue());
                    }
                });
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), lotsIdeResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), lotsIdeResp);
                return lotsIdeResp;

            case ECONOMIC_OPERATOR_IDENTIFIER:
                EOIdentifierResponse eoIdeResp = new EOIdentifierResponse();
                if (!responseType.getResponseValue().isEmpty()
                        && responseType.getResponseValue().get(0).getResponseID() != null
                        && responseType.getResponseValue().get(0).getResponseID().getValue() != null) {

                    eoIdeResp.setIdentifier(responseType.getResponseValue().get(0).getResponseID().getValue());

                    if (responseType.getResponseValue().get(0).getResponseID().getSchemeName() != null) {
                        eoIdeResp.setEOIDType(responseType.getResponseValue().get(0).getResponseID().getSchemeName());
                    }
                }
                applyValidatedCriterionPropertyID(responseType.getValidatedCriterionPropertyID(), eoIdeResp);
                applyConfidentialityLevelCode(responseType.getConfidentialityLevelCode(), eoIdeResp);
                return eoIdeResp;

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
                    qResp.setQuantity(res.getQuantity().getValue());
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
                    aResp.setAmount(res.getAmount().getValue());
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
                    pResp.setPercentage(res.getPercent().getValue());
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

    public EODetails extractEODetails(eu.espd.schema.v2.v210.commonaggregate.EconomicOperatorPartyType eoPartyType,
                                      eu.espd.schema.v2.v210.commonaggregate.ProcurementProjectLotType pplType,
                                      eu.espd.schema.v2.v210.commonbasic.EconomicOperatorGroupNameType eoGroupNameType) {

        final EODetails eoDetails = new EODetails();

        if (eoPartyType != null) {

            // Economic Operator Role
            if (eoPartyType.getEconomicOperatorRole() != null
                    && eoPartyType.getEconomicOperatorRole().getRoleCode() != null) {
                eoDetails.setEoRole(EORoleTypeEnum.valueOf(eoPartyType.getEconomicOperatorRole().getRoleCode().getValue()));
            }

            // Economic Operator Group Name
            if (eoGroupNameType != null
                    && eoGroupNameType.getValue() != null) {
                eoDetails.setEOGroupName(eoGroupNameType.getValue());
            }

            // Employee quantity
            if (!eoPartyType.getQualifyingParty().isEmpty()
                    && eoPartyType.getQualifyingParty().get(0).getEmployeeQuantity() != null
                    && eoPartyType.getQualifyingParty().get(0).getEmployeeQuantity().getValue() != null) {

                eoDetails.setEmployeeQuantity(eoPartyType.getQualifyingParty().get(0)
                        .getEmployeeQuantity().getValue().intValueExact());
            }

            // General turnover
            if (!eoPartyType.getQualifyingParty().isEmpty()
                    && !eoPartyType.getQualifyingParty().get(0).getFinancialCapability().isEmpty()
                    && eoPartyType.getQualifyingParty().get(0).getFinancialCapability().get(0).getValueAmount() != null
                    && eoPartyType.getQualifyingParty().get(0).getFinancialCapability().get(0).getValueAmount().getValue() != null
                    && eoPartyType.getQualifyingParty().get(0).getFinancialCapability().get(0).getValueAmount().getCurrencyID() != null) {

                eoDetails.setGeneralTurnover(new AmountResponse());
                eoDetails.getGeneralTurnover().setAmount(eoPartyType.getQualifyingParty().get(0)
                        .getFinancialCapability().get(0).getValueAmount().getValue());
                eoDetails.getGeneralTurnover().setCurrency(eoPartyType.getQualifyingParty().get(0)
                        .getFinancialCapability().get(0).getValueAmount().getCurrencyID());
            }

            if (eoPartyType.getParty() != null) {

                if (!eoPartyType.getParty().getPartyName().isEmpty()
                        && eoPartyType.getParty().getPartyName().get(0).getName() != null) {
                    eoDetails.setName(eoPartyType.getParty().getPartyName().get(0).getName().getValue());
                }

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
                    if (poa.getAgentParty() != null
                            && !poa.getAgentParty().getPerson().isEmpty()) {
                        eu.espd.schema.v2.v210.commonaggregate.PersonType pt = poa.getAgentParty().getPerson().get(0);

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
    private ESPDRequestDetails extractESPDRequestDetails(eu.espd.schema.v2.v210.commonaggregate.DocumentReferenceType drt) {
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
