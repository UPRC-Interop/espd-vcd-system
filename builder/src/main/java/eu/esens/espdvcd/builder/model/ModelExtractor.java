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

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.codelist.enums.*;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import eu.esens.espdvcd.model.requirement.response.*;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisEvidence;
import eu.esens.espdvcd.model.retriever.ECertisEvidenceGroup;
import eu.esens.espdvcd.model.retriever.ECertisEvidenceIssuerParty;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.CriterionType;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.LegislationType;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.RequirementGroupType;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.RequirementType;
import eu.espd.schema.v1.commonaggregatecomponents_2.ContractingPartyType;
import eu.espd.schema.v1.commonaggregatecomponents_2.DocumentReferenceType;
import eu.espd.schema.v1.commonaggregatecomponents_2.ExternalReferenceType;
import eu.espd.schema.v1.commonaggregatecomponents_2.ServiceProviderPartyType;
import eu.espd.schema.v1.commonbasiccomponents_2.ContractFolderIDType;
import eu.espd.schema.v2.pre_award.commonaggregate.ProcurementProjectType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionPropertyGroupType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionPropertyType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionType;
import eu.espd.schema.v2.pre_award.commonbasic.ConfidentialityLevelCodeType;
import eu.espd.schema.v2.pre_award.commonbasic.ProcedureCodeType;
import eu.espd.schema.v2.pre_award.commonbasic.ValidatedCriterionPropertyIDType;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ModelExtractor {

    default CADetails extractCADetails(ContractingPartyType caParty,
                                       ContractFolderIDType contractFolderId,
                                       List<DocumentReferenceType> additionalDocumentReferenceList) {

        CADetails cd = new CADetails();

        if (caParty != null && caParty.getParty() != null) {
            if (!caParty.getParty().getPartyName().isEmpty()
                    && caParty.getParty().getPartyName().get(0).getName() != null) {
                cd.setCAOfficialName(caParty.getParty().getPartyName().get(0).getName().getValue());
            }

            if (!caParty.getParty().getPartyIdentification().isEmpty()
                    && caParty.getParty().getPartyIdentification().get(0).getID() != null) {
                cd.setID(caParty.getParty().getPartyIdentification().get(0).getID().getValue());
            }

            if (caParty.getParty().getEndpointID() != null) {
                cd.setElectronicAddressID(caParty.getParty().getEndpointID().getValue());
            }

            if (caParty.getParty().getWebsiteURI() != null) {
                cd.setWebSiteURI(caParty.getParty().getWebsiteURI().getValue());
            }


            if (caParty.getParty().getPostalAddress() != null) {
                PostalAddress caAddress = new PostalAddress();

                if (caParty.getParty().getPostalAddress().getStreetName() != null) {
                    caAddress.setAddressLine1(caParty.getParty().getPostalAddress().getStreetName().getValue());
                }

                //if (caParty.getParty().getPostalAddress().getPostbox() != null) {
                //    caAddress.setPostCode(caParty.getParty().getPostalAddress().getPostbox().getValue());
                //}
                // UL 2017-10-10: read post code from cbc:PostalZone
                if (caParty.getParty().getPostalAddress().getPostalZone() != null) {
                    caAddress.setPostCode(caParty.getParty().getPostalAddress().getPostalZone().getValue());
                }
                // if not available, try cbc:Postbox (for backwards compatibility)
                else if (caParty.getParty().getPostalAddress().getPostbox() != null) {
                    caAddress.setPostCode(caParty.getParty().getPostalAddress().getPostbox().getValue());
                }

                if (caParty.getParty().getPostalAddress().getCityName() != null) {
                    caAddress.setCity(caParty.getParty().getPostalAddress().getCityName().getValue());
                }

                if (caParty.getParty().getPostalAddress().getCountry() != null
                        && caParty.getParty().getPostalAddress().getCountry().getIdentificationCode() != null) {
                    caAddress.setCountryCode(caParty.getParty().getPostalAddress().getCountry().getIdentificationCode().getValue());
                }

                cd.setPostalAddress(caAddress);

            }

            if (caParty.getParty().getContact() != null) {
                cd.setContactingDetails(new ContactingDetails());
                if (caParty.getParty().getContact().getName() != null) {
                    cd.getContactingDetails().setContactPointName(caParty.getParty().getContact().getName().getValue());
                }

                if (caParty.getParty().getContact().getElectronicMail() != null) {
                    cd.getContactingDetails().setEmailAddress(caParty.getParty().getContact().getElectronicMail().getValue());
                }

                if (caParty.getParty().getContact().getTelephone() != null) {
                    cd.getContactingDetails().setTelephoneNumber(caParty.getParty().getContact().getTelephone().getValue());
                }

                if (caParty.getParty().getContact().getTelefax() != null) {
                    cd.getContactingDetails().setFaxNumber(caParty.getParty().getContact().getTelefax().getValue());
                }
            }
        }


        if (contractFolderId != null && contractFolderId.getValue() != null) {
            cd.setProcurementProcedureFileReferenceNo(contractFolderId.getValue());
        }
        if (!additionalDocumentReferenceList.isEmpty()) {

            // Find an entry with TED_CN Value
            Optional<DocumentReferenceType> optRef = additionalDocumentReferenceList.stream()
                    .filter(r -> r.getDocumentTypeCode() != null && r.getDocumentTypeCode().getValue().equals("TED_CN"))
                    .findFirst();
            optRef.ifPresent(ref -> {

                if (ref.getID() != null) {
                    cd.setProcurementPublicationNumber(ref.getID().getValue());
                }

                if (ref.getAttachment() != null && ref.getAttachment().getExternalReference() != null) {
                    ExternalReferenceType ert = ref.getAttachment().getExternalReference();

                    if (ert.getFileName() != null) {
                        cd.setProcurementProcedureTitle(ert.getFileName().getValue());
                    }

                    if (!ert.getDescription().isEmpty()) {
                        cd.setProcurementProcedureDesc(ert.getDescription().get(0).getValue());

                        // 2018-03-20 UL: modifications to add capabilities to handle Received Notice Number
                        if (ert.getDescription().size() > 1) {
                            cd.setReceivedNoticeNumber(ert.getDescription().get(1).getValue());
                        }

                    }

                    if (ert.getURI() != null) {
                        cd.setProcurementPublicationURI(ert.getURI().getValue());
                    }
                }

            });

            // 2018-03-20 UL: add capabilities to handle National Official Journal
            Optional<DocumentReferenceType> optNatRef = additionalDocumentReferenceList.stream()
                    .filter(r -> r.getDocumentTypeCode() != null && r.getDocumentTypeCode().getValue().equals("NGOJ"))
                    .findFirst();
            optNatRef.ifPresent(ref -> {

                if (ref.getID() != null) {
                    cd.setNationalOfficialJournal(ref.getID().getValue());
                }
            });

        }
        return cd;
    }

    default CADetails extractCADetails(List<eu.espd.schema.v2.pre_award.commonaggregate.ContractingPartyType> caParty,
                                       eu.espd.schema.v2.pre_award.commonbasic.ContractFolderIDType contractFolderId,
                                       eu.espd.schema.v2.pre_award.commonbasic.ProcedureCodeType procedureCodeType,
                                       eu.espd.schema.v2.pre_award.commonaggregate.ProcurementProjectType procurementProjectType,
                                       List<eu.espd.schema.v2.pre_award.commonaggregate.DocumentReferenceType> additionalDocumentReferenceList) {

        CADetails cd = new CADetails();

        if (!caParty.isEmpty() && caParty.get(0) != null) {
            if (!caParty.get(0).getParty().getPartyName().isEmpty()
                    && caParty.get(0).getParty().getPartyName().get(0).getName() != null) {
                cd.setCAOfficialName(caParty.get(0).getParty().getPartyName().get(0).getName().getValue());
            }

            if (!caParty.get(0).getParty().getPartyIdentification().isEmpty()
                    && caParty.get(0).getParty().getPartyIdentification().get(0).getID() != null) {
                cd.setID(caParty.get(0).getParty().getPartyIdentification().get(0).getID().getValue());
            }

            if (caParty.get(0).getParty().getEndpointID() != null) {
                cd.setElectronicAddressID(caParty.get(0).getParty().getEndpointID().getValue());
            }

            if (caParty.get(0).getParty().getWebsiteURI() != null) {
                cd.setWebSiteURI(caParty.get(0).getParty().getWebsiteURI().getValue());
            }


            if (caParty.get(0).getParty().getPostalAddress() != null) {
                PostalAddress caAddress = new PostalAddress();

                if (caParty.get(0).getParty().getPostalAddress().getStreetName() != null) {
                    caAddress.setAddressLine1(caParty.get(0).getParty().getPostalAddress().getStreetName().getValue());
                }

                //if (caParty.getParty().getPostalAddress().getPostbox() != null) {
                //    caAddress.setPostCode(caParty.getParty().getPostalAddress().getPostbox().getValue());
                //}
                // UL 2017-10-10: read post code from cbc:PostalZone
                if (caParty.get(0).getParty().getPostalAddress().getPostalZone() != null) {
                    caAddress.setPostCode(caParty.get(0).getParty().getPostalAddress().getPostalZone().getValue());
                }
                // if not available, try cbc:Postbox (for backwards compatibility)
                else if (caParty.get(0).getParty().getPostalAddress().getPostbox() != null) {
                    caAddress.setPostCode(caParty.get(0).getParty().getPostalAddress().getPostbox().getValue());
                }

                if (caParty.get(0).getParty().getPostalAddress().getCityName() != null) {
                    caAddress.setCity(caParty.get(0).getParty().getPostalAddress().getCityName().getValue());
                }

                if (caParty.get(0).getParty().getPostalAddress().getCountry() != null
                        && caParty.get(0).getParty().getPostalAddress().getCountry().getIdentificationCode() != null) {
                    caAddress.setCountryCode(caParty.get(0).getParty().getPostalAddress().getCountry().getIdentificationCode().getValue());
                }

                cd.setPostalAddress(caAddress);

            }

            if (caParty.get(0).getParty().getContact() != null) {
                cd.setContactingDetails(new ContactingDetails());
                if (caParty.get(0).getParty().getContact().getName() != null) {
                    cd.getContactingDetails().setContactPointName(caParty.get(0).getParty().getContact().getName().getValue());
                }

                if (caParty.get(0).getParty().getContact().getElectronicMail() != null) {
                    cd.getContactingDetails().setEmailAddress(caParty.get(0).getParty().getContact().getElectronicMail().getValue());
                }

                if (caParty.get(0).getParty().getContact().getTelephone() != null) {
                    cd.getContactingDetails().setTelephoneNumber(caParty.get(0).getParty().getContact().getTelephone().getValue());
                }

                if (caParty.get(0).getParty().getContact().getTelefax() != null) {
                    cd.getContactingDetails().setFaxNumber(caParty.get(0).getParty().getContact().getTelefax().getValue());
                }
            }
        }

        if (contractFolderId != null
                && contractFolderId.getValue() != null) {
            cd.setProcurementProcedureFileReferenceNo(contractFolderId.getValue());
        }

        if (procedureCodeType != null
                && procedureCodeType.getValue() != null) {
            cd.setProcurementProcedureType(procedureCodeType.getValue());
        }

        if (procurementProjectType != null) {

            // No need to set that here
            // if (!procurementProjectType.getName().isEmpty()
            //        && procurementProjectType.getName().get(0).getValue() != null) {
            //    cd.setCAOfficialName(procurementProjectType.getName().get(0).getValue());
            // }

            // No need to set that here
            // if (!procurementProjectType.getDescription().isEmpty()
            //        && procurementProjectType.getDescription().get(0).getValue() != null) {
            //    cd.setProcurementProcedureDesc(procurementProjectType.getDescription().get(0).getValue());
            // }

            // Project Type
            if (procurementProjectType.getProcurementTypeCode() != null
                    && procurementProjectType.getProcurementTypeCode().getValue() != null) {
                cd.setProjectType(procurementProjectType.getProcurementTypeCode().getValue());
            }

            // CPV codes
            cd.getClassificationCodes().addAll(procurementProjectType.getMainCommodityClassification().stream()
                    .filter(classificationType -> classificationType.getItemClassificationCode() != null
                            && classificationType.getItemClassificationCode().getValue() != null)
                    .map(classificationType -> classificationType.getItemClassificationCode().getValue())
                    .collect(Collectors.toList()));
        }

        if (!additionalDocumentReferenceList.isEmpty()) {

            // Find an entry with TED_CN Value
            Optional<eu.espd.schema.v2.pre_award.commonaggregate.DocumentReferenceType> optRef = additionalDocumentReferenceList.stream()
                    .filter(r -> r.getDocumentTypeCode() != null && r.getDocumentTypeCode().getValue().equals("TED_CN"))
                    .findFirst();

            optRef.ifPresent(ref -> {

                if (ref.getID() != null) {
                    cd.setProcurementPublicationNumber(ref.getID().getValue());
                }

                if (ref.getAttachment() != null && ref.getAttachment().getExternalReference() != null) {

                    eu.espd.schema.v2.pre_award.commonaggregate.ExternalReferenceType ert = ref.getAttachment().getExternalReference();

                    if (ert.getFileName() != null) {
                        cd.setProcurementProcedureTitle(ert.getFileName().getValue());
                    }

                    if (!ert.getDescription().isEmpty()) {
                        cd.setProcurementProcedureDesc(ert.getDescription().get(0).getValue());

                        // 2018-10-5 KR: modifications to add capabilities to handle Received Notice Number
                        if (ert.getDescription().size() > 1) {
                            cd.setReceivedNoticeNumber(ert.getDescription().get(1).getValue());
                        }

                    }

                    if (ert.getURI() != null) {
                        cd.setProcurementPublicationURI(ert.getURI().getValue());
                    }
                }

            });

        }
        return cd;

    }

    default ServiceProviderDetails extractServiceProviderDetails(ServiceProviderPartyType sppt) {
        // modification UL 2018-01-12: discard old service provider information, always use information of the current system

        /*ServiceProviderDetails spd = new ServiceProviderDetails();

        if (sppt != null && sppt.getParty() != null) {
            if (!sppt.getParty().getPartyName().isEmpty()
                    && sppt.getParty().getPartyName().get(0).getName() != null) {
                spd.setName(sppt.getParty().getPartyName().get(0).getName().getValue());
            }

            if (sppt.getParty().getWebsiteURI() != null) {
                spd.setWebsiteURI(sppt.getParty().getWebsiteURI().getValue());
            }

            if (sppt.getParty().getEndpointID() != null) {
                spd.setEndpointID(sppt.getParty().getEndpointID().getValue());
            }

            if (!sppt.getParty().getPartyIdentification().isEmpty()
                    && sppt.getParty().getPartyIdentification().get(0).getID() != null) {
                spd.setId(sppt.getParty().getPartyIdentification().get(0).getID().getValue());
            }
        }

        return spd;
        */

        try {
            // return the default service provider details
            return BuilderFactory.EDM_V1
                    .createRegulatedModelBuilder()
                    .createESPDRequest()
                    .getServiceProviderDetails();
        } catch (BuilderException e) {
            return null;
        }


    }

    default ServiceProviderDetails extractServiceProviderDetails
            (List<eu.espd.schema.v2.pre_award.commonaggregate.ContractingPartyType> sppt) {
        try {
            return BuilderFactory.EDM_V2
                    .createRegulatedModelBuilder()
                    .createESPDRequest()
                    .getServiceProviderDetails();
        } catch (BuilderException e) {
            return null;
        }
    }

    default SelectableCriterion extractSelectableCriterion(CriterionType ct, boolean isSelected) {

        String id = ct.getID().getValue();
        String desc = ct.getDescription().getValue();
        String typeCode = ct.getTypeCode().getValue();
        String name = ct.getName().getValue();

        LegislationReference lr = extractDefaultLegalReference(ct.getLegislationReference());

        List<RequirementGroup> rgList = ct.getRequirementGroup().stream()
                .map(t -> extractRequirementGroup(t))
                .collect(Collectors.toList());

        SelectableCriterion selCr = new SelectableCriterion(id, typeCode, name, desc, lr, rgList);
        selCr.setSelected(isSelected);
        return selCr;
    }

    default SelectableCriterion extractSelectableCriterion(CriterionType ct) {
        return extractSelectableCriterion(ct, true);
    }

    default SelectableCriterion extractSelectableCriterion(TenderingCriterionType tcType, boolean isSelected) {
        String id = tcType.getID().getValue();
        String desc = tcType.getDescription().isEmpty() ? "" : tcType.getDescription().get(0).getValue();
        String typeCode = tcType.getCriterionTypeCode().getValue();
        String name = tcType.getName().getValue();

        LegislationReference lr = extractDefaultLegalReferenceV2(tcType.getLegislation());

        List<RequirementGroup> rgList = tcType.getTenderingCriterionPropertyGroup().stream()
                .map(t -> extractRequirementGroup(t))
                .collect(Collectors.toList());

        SelectableCriterion selCr = new SelectableCriterion(id, typeCode, name, desc, lr, rgList);
        selCr.setSelected(isSelected);
        return selCr;
    }

    default SelectableCriterion extractSelectableCriterion(TenderingCriterionType tct) {
        return extractSelectableCriterion(tct, true);
    }

    /**
     * Create a new selectable criterion from an e-Certis criterion.
     *
     * @param ec         The e-Certis criterion
     * @param isSelected
     * @return
     */
    default SelectableCriterion extractSelectableCriterion(ECertisCriterion ec, boolean isSelected) {
        String id = ec.getID();
        String name = ec.getName();
        String desc = ec.getDescription();

        LegislationReference lr = ec.getLegislationReference();

        SelectableCriterion sc = new SelectableCriterion(id, null, name, desc, lr, null);
        sc.setSelected(isSelected);
        return sc;
    }

    default SelectableCriterion extractSelectableCriterion(ECertisCriterion ec) {
        return extractSelectableCriterion(ec, true);
    }

    default RequirementGroup extractRequirementGroup(TenderingCriterionPropertyGroupType rgType) {

        RequirementGroup rg = null;
        if (rgType.getID() != null) {
            String id = rgType.getID().getValue();
            rg = new RequirementGroup(id);
            if (rgType.getPropertyGroupTypeCode() != null) {
                rg.setCondition(rgType.getPropertyGroupTypeCode().getValue());
            }
        }

        if (rg != null) {
            List<Requirement> rList = rgType.getTenderingCriterionProperty().stream()
                    .map(r -> extractRequirement(r))
                    .collect(Collectors.toList());
            List<RequirementGroup> childRg = rgType.getSubsidiaryTenderingCriterionPropertyGroup().stream()
                    .map(t -> extractRequirementGroup(t))
                    .collect(Collectors.toList());
            rg.setRequirements(rList);
            rg.setRequirementGroups(childRg);
        }

        return rg;
    }

    default RequirementGroup extractRequirementGroup(RequirementGroupType rgType) {

        RequirementGroup rg = null;
        if (rgType.getID() != null) {
            String id = rgType.getID().getValue();
            rg = new RequirementGroup(id);
            if (rgType.getPi() != null)
                rg.setCondition(rgType.getPi());
        }

        if (rg != null) {
            List<Requirement> rList = rgType.getRequirement().stream()
                    .map(r -> extractRequirement(r))
                    .collect(Collectors.toList());
            List<RequirementGroup> childRg = rgType.getRequirementGroup().stream()
                    .map(t -> extractRequirementGroup(t))
                    .collect(Collectors.toList());
            rg.setRequirements(rList);
            rg.setRequirementGroups(childRg);
        }

        return rg;
    }

    default LegislationReference extractDefaultLegalReferenceV2
            (List<eu.espd.schema.v2.pre_award.commonaggregate.LegislationType> lrList) {

        //First check if there is an EU_* jurisdiction
        LegislationReference lr;
        lr = extractEULegalReferenceV2(lrList);
        if (lr == null) {
            lr = extractNationalLegalReferenceV2(lrList);
        }
        return lr;
    }

    default LegislationReference extractDefaultLegalReference(List<LegislationType> lrList) {

        //First check if there is an EU_* jurisdiction
        LegislationReference lr;
        lr = extractEULegalReference(lrList);
        if (lr == null) {
            lr = extractNationalLegalReference(lrList);
        }
        return lr;
    }

    default LegislationReference extractEULegalReferenceV2
            (List<eu.espd.schema.v2.pre_award.commonaggregate.LegislationType> lrList) {
        return lrList.stream()
                .filter(lr -> {
                    String jl = lr.getJurisdictionLevel().stream()
                            .findFirst().orElseThrow(NoSuchElementException::new)
                            .getValue();
                    return jl.equals(LegislationTypeEnum.EU_DIRECTIVE.name()) |
                            jl.equals(LegislationTypeEnum.EU_DECISION.name()) |
                            jl.equals(LegislationTypeEnum.EU_REGULATION.name());
                })
                .findFirst().map(lr -> extractLegalReference(lr))
                .orElse(null);
    }

    default LegislationReference extractEULegalReference(List<LegislationType> lrList) {
        return lrList.stream()
                .filter(lr -> lr.getJurisdictionLevelCode().getValue().contains("EU_"))
                .findFirst().map(lr -> extractLegalReference(lr))
                .orElse(null);
    }

    default LegislationReference extractNationalLegalReferenceV2
            (List<eu.espd.schema.v2.pre_award.commonaggregate.LegislationType> lrList) {
        return lrList.stream()
                .filter(lr -> {
                    String jl = lr.getJurisdictionLevel().stream()
                            .findFirst().orElseThrow(NoSuchElementException::new)
                            .getValue();
                    return jl.equals(LegislationTypeEnum.NATIONAL_LEGISLATION.name()) |
                            jl.equals(LegislationTypeEnum.SUBNATIONAL_LEGISLATION.name());
                })
                .findFirst().map(lr -> extractLegalReference(lr))
                .orElse(null);
    }

    default LegislationReference extractNationalLegalReference(List<LegislationType> lrList) {
        return lrList.stream()
                .filter(lr -> lr.getJurisdictionLevelCode().getValue().contains("NATIONAL"))
                .findFirst().map(lr -> extractLegalReference(lr))
                .orElse(null);
    }

    default LegislationReference extractLegalReference(eu.espd.schema.v2.pre_award.commonaggregate.LegislationType lt) {
        LegislationReference lr = new LegislationReference(
                lt.getTitle().get(0).getValue(),
                lt.getDescription().get(0).getValue(),
                lt.getJurisdictionLevel().get(0).getValue(),
                lt.getArticle().get(0).getValue(),
                lt.getURI().get(0).getValue()
        );
        return lr;
    }

    default LegislationReference extractLegalReference(LegislationType lt) {
        LegislationReference lr = new LegislationReference(
                lt.getTitle().getValue(),
                lt.getDescription().getValue(),
                lt.getJurisdictionLevelCode().getValue(),
                lt.getArticle().getValue(),
                lt.getURI().getValue());
        return lr;
    }

    default void applyCriterionWeightingData(WeightIndicatorResponse response, TenderingCriterionType criterionType) {

        if (response != null) {
            // Indicator
            if (criterionType.getEvaluationMethodTypeCode() != null
                    && criterionType.getEvaluationMethodTypeCode().getValue() != null) {

                response.setIndicator(criterionType.getEvaluationMethodTypeCode().getValue()
                        .equals(EvaluationMethodTypeEnum.WEIGHTED.name()));
            }
            // WeightingConsiderationDescription
            response.getEvaluationMethodDescriptionList()
                    .addAll(criterionType.getWeightingConsiderationDescription().stream()
                            .map(descType -> descType.getValue())
                            .collect(Collectors.toList()));
            // Weight
            if (criterionType.getWeightNumeric() != null
                    && criterionType.getWeightNumeric().getValue() != null) {

                response.setWeight(criterionType.getWeightNumeric().getValue());
            }
        }
    }

    default Requirement extractRequirement(TenderingCriterionPropertyType rqType) {
        String theId = null;
        if (rqType.getID() != null) {
            theId = rqType.getID().getValue();
        }
        String theDescription = null;
        if (!rqType.getDescription().isEmpty() && rqType.getDescription().get(0) != null) {
            theDescription = rqType.getDescription().get(0).getValue();
        }

        Requirement rq = new ResponseRequirement(
                theId,
                RequirementTypeEnum.valueOf(rqType.getTypeCode().getValue()),
                ResponseTypeEnum.valueOf(rqType.getValueDataTypeCode().getValue()),
                theDescription
        );

        applyCAResponseToModel(rqType, rq);
        return rq;
    }

    default void applyValidatedCriterionPropertyID(final ValidatedCriterionPropertyIDType vcpIDType, final Response resp) {

        if (vcpIDType != null && vcpIDType.getValue() != null) {
            applyValidatedCriterionPropertyID(vcpIDType.getValue(), resp);
        }
    }

    default void applyValidatedCriterionPropertyID(String ID, final Response resp) {
        resp.setValidatedCriterionPropertyID(ID);
    }

    default void applyConfidentialityLevelCode(final ConfidentialityLevelCodeType clcType, final Response resp) {

        if (clcType != null && clcType.getValue() != null) {
            applyConfidentialityLevelCode(clcType.getValue(), resp);
        }
    }

    default void applyConfidentialityLevelCode(String code, final Response resp) {
        resp.setConfidentialityLevelCode(code);
    }

    default void applyCAResponseToModel(TenderingCriterionPropertyType rqType, Requirement rq) {

        if (rqType.getTypeCode() != null
                && rqType.getTypeCode().getValue() != null
                && rqType.getTypeCode().getValue().equals(RequirementTypeEnum.REQUIREMENT.name())
                && rqType.getValueDataTypeCode() != null
                && rqType.getValueDataTypeCode().getValue() != null) {

            switch (ResponseTypeEnum.valueOf(rqType.getValueDataTypeCode().getValue())) {

                case DESCRIPTION:
                    if (rqType.getExpectedDescription() != null
                            && rqType.getExpectedDescription().getValue() != null) {

                        DescriptionResponse descResp = new DescriptionResponse();
                        descResp.setDescription(rqType.getExpectedDescription().getValue());
                        applyValidatedCriterionPropertyID(rqType.getID().getValue(), descResp);
                        applyConfidentialityLevelCode(ConfidentialityLevelEnum.PUBLIC.name(), descResp);
                        rq.setResponse(descResp);
                    }
                    break;

                case AMOUNT:
                    if (rqType.getExpectedAmount() != null
                            && rqType.getExpectedAmount().getValue() != null) {

                        AmountResponse amountResp = new AmountResponse();
                        amountResp.setAmount(rqType.getExpectedAmount().getValue());
                        if (rqType.getExpectedAmount().getCurrencyID() != null) {
                            amountResp.setCurrency(rqType.getExpectedAmount().getCurrencyID());
                        }
                        applyValidatedCriterionPropertyID(rqType.getID().getValue(), amountResp);
                        applyConfidentialityLevelCode(ConfidentialityLevelEnum.PUBLIC.name(), amountResp);
                        rq.setResponse(amountResp);
                    }
                    break;

                case CODE:
                    if (rqType.getExpectedCode() != null
                            && rqType.getExpectedCode().getValue() != null) {

                        EvidenceURLCodeResponse codeResp = new EvidenceURLCodeResponse();
                        codeResp.setEvidenceURLCode(rqType.getExpectedCode().getValue());
                        applyValidatedCriterionPropertyID(rqType.getID().getValue(), codeResp);
                        applyConfidentialityLevelCode(ConfidentialityLevelEnum.PUBLIC.name(), codeResp);
                        rq.setResponse(codeResp);
                    }
                    break;

                case LOT_IDENTIFIER:
                    if (rqType.getExpectedID() != null
                            && rqType.getExpectedID().getValue() != null) {

                        LotIdentifierResponse lotIdeResp = new LotIdentifierResponse();
                        lotIdeResp.setLots(rqType.getExpectedID().getValue());
                        applyValidatedCriterionPropertyID(rqType.getID().getValue(), lotIdeResp);
                        applyConfidentialityLevelCode(ConfidentialityLevelEnum.PUBLIC.name(), lotIdeResp);
                        rq.setResponse(lotIdeResp);
                    }
                    break;

                case QUANTITY_INTEGER:
                    if (rqType.getExpectedValueNumeric() != null
                            && rqType.getExpectedValueNumeric().getValue() != null) {

                        QuantityIntegerResponse quantityIntResp = new QuantityIntegerResponse();
                        quantityIntResp.setQuantity(rqType.getExpectedValueNumeric().getValue().intValueExact());
                        applyValidatedCriterionPropertyID(rqType.getID().getValue(), quantityIntResp);
                        applyConfidentialityLevelCode(ConfidentialityLevelEnum.PUBLIC.name(), quantityIntResp);
                        rq.setResponse(quantityIntResp);
                    }
                    break;

                case QUANTITY:
                    if (rqType.getExpectedValueNumeric() != null
                            && rqType.getExpectedValueNumeric().getValue() != null) {

                        QuantityResponse quantityResp = new QuantityResponse();
                        quantityResp.setQuantity(rqType.getExpectedValueNumeric().getValue());
                        applyValidatedCriterionPropertyID(rqType.getID().getValue(), quantityResp);
                        applyConfidentialityLevelCode(ConfidentialityLevelEnum.PUBLIC.name(), quantityResp);
                        rq.setResponse(quantityResp);
                    }
                    break;

                case PERIOD:
                    if (!rqType.getApplicablePeriod().isEmpty()
                            && rqType.getApplicablePeriod().get(0).getStartDate() != null
                            && rqType.getApplicablePeriod().get(0).getEndDate() != null
                            && rqType.getApplicablePeriod().get(0).getStartDate().getValue() != null
                            && rqType.getApplicablePeriod().get(0).getEndDate().getValue() != null) {

                        ApplicablePeriodResponse periodResp = new ApplicablePeriodResponse();
                        periodResp.setStartDate(rqType.getApplicablePeriod().get(0).getStartDate().getValue());
                        periodResp.setEndDate(rqType.getApplicablePeriod().get(0).getEndDate().getValue());
                        applyValidatedCriterionPropertyID(rqType.getID().getValue(), periodResp);
                        applyConfidentialityLevelCode(ConfidentialityLevelEnum.PUBLIC.name(), periodResp);
                        rq.setResponse(periodResp);
                    }
                    break;

                case URL:
                    if (rqType.getExpectedDescription() != null
                            && rqType.getExpectedDescription().getValue() != null) {

                        URLResponse urlResp = new URLResponse();
                        urlResp.setUrl(rqType.getExpectedDescription().getValue());
                        applyValidatedCriterionPropertyID(rqType.getID().getValue(), urlResp);
                        applyConfidentialityLevelCode(ConfidentialityLevelEnum.PUBLIC.name(), urlResp);
                        rq.setResponse(urlResp);
                    }
                    break;

            }
        }
    }

    default Requirement extractRequirement(RequirementType rqType) {
        String theId = null;
        if (rqType.getID() != null) {
            theId = rqType.getID().getValue();
        }
        String theDescription = null;
        if (rqType.getDescription() != null) {
            theDescription = rqType.getDescription().getValue();
        }

        Requirement r = new ResponseRequirement(
                theId,
                ResponseTypeEnum.valueOf(rqType.getResponseDataType()),
                theDescription);
        return r;
    }

    default Evidence extractEvidence(ECertisEvidence evidence) {
        Evidence e = new Evidence();
        e.setID(evidence.getID());
        e.setDescription(evidence.getDescription());
        e.setConfidentialityLevelCode(ConfidentialityLevelEnum.PUBLIC.name());

        if (!evidence.getEvidenceDocumentReference().isEmpty()
                && evidence.getEvidenceDocumentReference().get(0).getAttachment() != null
                && evidence.getEvidenceDocumentReference().get(0).getAttachment().getExternalReference() != null
                && evidence.getEvidenceDocumentReference().get(0).getAttachment().getExternalReference().getURI() != null) {

            e.setEvidenceURL(evidence.getEvidenceDocumentReference().get(0).getAttachment().getExternalReference().getURI());
        }

        if (!evidence.getEvidenceIssuerParty().isEmpty()) {
            e.setEvidenceIssuer(extractEvidenceIssuerDetails(evidence.getEvidenceIssuerParty().get(0)));
        }

        return e;
    }

    default List<Evidence> extractEvidences(ECertisEvidenceGroup eg) {
        return eg.getEvidences()
                .stream()
                .map(e -> extractEvidence(e))
                .collect(Collectors.toList());
    }

    default List<Evidence> extractEvidences(List<ECertisEvidenceGroup> egList) {
        List<Evidence> evidenceList = new ArrayList<>();

        for (ECertisEvidenceGroup eg : egList) {
            evidenceList.addAll(extractEvidences(eg));
        }

        return evidenceList;
    }

    default EvidenceIssuerDetails extractEvidenceIssuerDetails(ECertisEvidenceIssuerParty evidenceIssuerParty) {
        EvidenceIssuerDetails issuerDetails = new EvidenceIssuerDetails();

        if (!evidenceIssuerParty.getPartyName().isEmpty()
                && evidenceIssuerParty.getPartyName().get(0).getName() != null) {

            issuerDetails.setName(evidenceIssuerParty.getPartyName().get(0).getName());
        }

        issuerDetails.setWebsite(evidenceIssuerParty.getWebsiteURI());

        return issuerDetails;
    }

    default boolean isSME(String icc) {
        boolean isSME = false;
        EOIndustryClassificationCodeEnum code = EOIndustryClassificationCodeEnum.valueOf(icc);
        if (code != EOIndustryClassificationCodeEnum.LARGE) {
            isSME = true;
        }
        return isSME;
    }
}
