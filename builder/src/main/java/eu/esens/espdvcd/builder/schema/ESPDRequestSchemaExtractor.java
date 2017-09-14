package eu.esens.espdvcd.builder.schema;

import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.requirement.Requirement;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import java.util.stream.Collectors;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ContractFolderIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CopyIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DescriptionType;

public class ESPDRequestSchemaExtractor implements SchemaExtractor {
    
    public ESPDRequestType extractESPDRequestType(ESPDRequest req) {

        ESPDRequestType reqType = new ESPDRequestType();
        
        if (req.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
            reqType.setContractFolderID(new ContractFolderIDType());
            reqType.getContractFolderID().setSchemeAgencyID("TeD");
            reqType.getContractFolderID().setValue(req.getCADetails().getProcurementProcedureFileReferenceNo());
        }

        reqType.getAdditionalDocumentReference().add(extractCADetailsDocumentReferece(req.getCADetails()));
        reqType.setContractingParty(extractContractingPartyType(req.getCADetails()));
        reqType.getProcurementProjectLot().add(extractProcurementProjectLot(req.getCADetails()));
        reqType.setServiceProviderParty(extractServiceProviderPartyType(req.getServiceProviderDetails()));
        reqType.getCriterion().addAll(req.getFullCriterionList().stream()
                .filter(cr -> cr.isSelected())
                .map(cr -> extractCriterion(cr))
                .collect(Collectors.toList()));
        
        reqType.setUBLVersionID(createUBL21VersionIdType());
        reqType.setCustomizationID(createBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrns070:ver3.0"));
        reqType.setVersionID(createVersionIDType("2017.01.01"));

        reqType.setCopyIndicator(new CopyIndicatorType());
        reqType.getCopyIndicator().setValue(false);
        
        return reqType;
    }
    
    @Override
    public RequirementType extractRequirementType(Requirement r) {

        RequirementType req = new RequirementType();

        req.setResponseDataType(r.getResponseDataType().name());
        req.setDescription(new DescriptionType());
        req.getDescription().setValue(r.getDescription());
        req.setID(createCriterionRelatedIDType(r.getID()));

        return req;
    }
}
