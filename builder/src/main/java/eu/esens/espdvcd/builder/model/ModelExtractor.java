package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.codelist.enums.CriterionElementTypeEnum;
import eu.esens.espdvcd.codelist.enums.EOIndustryClassificationCodeEnum;
import eu.esens.espdvcd.codelist.enums.LegislationTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.CriterionType;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.LegislationType;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.RequirementGroupType;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.RequirementType;
import eu.espd.schema.v1.commonaggregatecomponents_2.ContractingPartyType;
import eu.espd.schema.v1.commonaggregatecomponents_2.DocumentReferenceType;
import eu.espd.schema.v1.commonaggregatecomponents_2.ExternalReferenceType;
import eu.espd.schema.v1.commonaggregatecomponents_2.ServiceProviderPartyType;
import eu.espd.schema.v1.commonbasiccomponents_2.ContractFolderIDType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionPropertyGroupType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionPropertyType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionType;

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


        if (contractFolderId != null && contractFolderId.getValue() != null) {
            cd.setProcurementProcedureFileReferenceNo(contractFolderId.getValue());
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
            return BuilderFactory.getRegulatedModelBuilder().createESPDRequest().getServiceProviderDetails();
        } catch (BuilderException e) {
            return null;
        }


    }

    default ServiceProviderDetails extractServiceProviderDetails(List<eu.espd.schema.v2.pre_award.commonaggregate.ContractingPartyType> sppt) {
        try {
            return BuilderFactory.getRegulatedModelBuilder().createESPDRequest().getServiceProviderDetails();
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

    default SelectableCriterion extractSelectableCriterion(TenderingCriterionType tct, boolean isSelected) {
        String id = tct.getID().getValue();
        String desc = tct.getDescription().isEmpty() ? "" : tct.getDescription().get(0).getValue();
        String typeCode = tct.getCriterionTypeCode().getValue();
        String name = tct.getName().getValue();

        LegislationReference lr = extractDefaultLegalReferenceV2(tct.getLegislation());

        List<RequirementGroup> rgList = tct.getTenderingCriterionPropertyGroup().stream()
                .map(t -> extractRequirementGroup(t))
                .collect(Collectors.toList());

        SelectableCriterion selCr = new SelectableCriterion(id, typeCode, name, desc, lr, rgList);
        selCr.setSelected(isSelected);
        return selCr;
    }

    default SelectableCriterion extractSelectableCriterion(TenderingCriterionType tct) {
        return extractSelectableCriterion(tct, true);
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

    default LegislationReference extractDefaultLegalReferenceV2(List<eu.espd.schema.v2.pre_award.commonaggregate.LegislationType> lrList) {

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

    default LegislationReference extractEULegalReferenceV2(List<eu.espd.schema.v2.pre_award.commonaggregate.LegislationType> lrList) {
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

    default LegislationReference extractNationalLegalReferenceV2(List<eu.espd.schema.v2.pre_award.commonaggregate.LegislationType> lrList) {
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

    default Requirement extractRequirement(TenderingCriterionPropertyType pt) {
        String theId = null;
        if (pt.getID() != null) {
            theId = pt.getID().getValue();
        }
        String theDescription = null;
        if (!pt.getDescription().isEmpty() && pt.getDescription().get(0) != null) {
            theDescription = pt.getDescription().get(0).getValue();
        }

        Requirement r = new ResponseRequirement(
                theId,
                CriterionElementTypeEnum.valueOf(pt.getTypeCode().getValue()),
                ResponseTypeEnum.valueOf(pt.getValueDataTypeCode().getValue()),
                theDescription
        );
        return r;
    }

    default Requirement extractRequirement(RequirementType rt) {
        String theId = null;
        if (rt.getID() != null) {
            theId = rt.getID().getValue();
        }
        String theDescription = null;
        if (rt.getDescription() != null) {
            theDescription = rt.getDescription().getValue();
        }

        Requirement r = new ResponseRequirement(
                theId,
                ResponseTypeEnum.valueOf(rt.getResponseDataType()),
                theDescription);
        return r;
    }

    default boolean isSMEIndicator(eu.espd.schema.v2.pre_award.commonaggregate.EconomicOperatorPartyType eop) {
        boolean isSME = false;
        EOIndustryClassificationCodeEnum code = EOIndustryClassificationCodeEnum.valueOf(eop.getQualifyingParty()
                .get(0).getParty().getIndustryClassificationCode().getValue());
        if (code != EOIndustryClassificationCodeEnum.LARGE) {
            isSME = true;
        }
        return isSME;
    }
}
