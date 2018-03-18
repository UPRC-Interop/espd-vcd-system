package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.RegulatedESPDRequest;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;

import java.util.stream.Collectors;


public class ESPDRequestModelExtractor implements ModelExtractor {

    /* package private constructor. Create only through factory */
    ESPDRequestModelExtractor() {}

    public ESPDRequest extractESPDRequest(ESPDRequestType reqType) {
        
        RegulatedESPDRequest req = new RegulatedESPDRequest();

        req.getFullCriterionList().addAll(reqType.getCriterion().stream()
        .map(c -> extractSelectableCriterion(c))
        .collect(Collectors.toList()));
        req.setCADetails(extractCADetails(reqType.getContractingParty(),
                                          reqType.getContractFolderID(),
                                          reqType.getAdditionalDocumentReference()));

        req.setServiceProviderDetails(extractServiceProviderDetails(reqType.getServiceProviderParty()));
        
        return req;
    }
    
}
