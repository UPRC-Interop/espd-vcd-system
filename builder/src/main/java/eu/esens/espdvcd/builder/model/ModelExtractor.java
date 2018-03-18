package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.LegislationType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementGroupType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.*;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ContractFolderIDType;

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
            return BuilderFactory.V1.getModelBuilder().createRegulatedESPDRequest().getServiceProviderDetails();
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

    default LegislationReference extractDefaultLegalReference(List<LegislationType> lrList) {

        //First check if there is an EU_* jurisdiction
        LegislationReference lr;
        lr = extractEULegalReference(lrList);
        if (lr == null) {
            lr = extractNationalLegalReference(lrList);
        }
        return lr;
    }

    default LegislationReference extractEULegalReference(List<LegislationType> lrList) {
        return lrList.stream()
                .filter(lr -> lr.getJurisdictionLevelCode().getValue().contains("EU_"))
                .findFirst().map(lr -> extractLegalReference(lr))
                .orElse(null);
    }

    default LegislationReference extractNationalLegalReference(List<LegislationType> lrList) {
        return lrList.stream()
                .filter(lr -> lr.getJurisdictionLevelCode().getValue().contains("NATIONAL"))
                .findFirst().map(lr -> extractLegalReference(lr))
                .orElse(null);
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
}
