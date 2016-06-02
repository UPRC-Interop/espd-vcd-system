package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SimpleESPDRequest;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import java.util.stream.Collectors;


public class ESPDRequestModelExtractor implements ModelExtractor {

    /* package private constructor. Create only through factory */
    ESPDRequestModelExtractor() {}

    public ESPDRequest extractESPDRequest(ESPDRequestType reqType) {
        
        SimpleESPDRequest req = new SimpleESPDRequest();

        req.getFullCriterionList().addAll(reqType.getCriterion().stream()
        .map(c -> extractSelectableCriterion(c))
        .collect(Collectors.toList()));
        req.setCADetails(extractCADetails(reqType.getContractingParty(),
                                          reqType.getContractFolderID(),
                                          reqType.getProcurementProjectLot().get(0),
                                          reqType.getAdditionalDocumentReference()));
        
        return req;
    }
        
    
}
