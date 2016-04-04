/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.SimpleESPDResponse;
import eu.esens.espdvcd.model.requirement.DescriptionResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.Response;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.ResponseType;
import java.util.stream.Collectors;

public class ESPDResponseModelFactory implements ModelFactory {

    public SimpleESPDResponse extractESPDResponse(ESPDResponseType resType) {

        SimpleESPDResponse res = new SimpleESPDResponse();

        res.getFullCriterionList().addAll(resType.getCriterion().stream()
                .map(c -> extractSelectableCriterion(c))
                .collect(Collectors.toList()));
        
        res.setCADetails(extractCADetails(resType.getContractingParty(),
                resType.getContractFolderID(),
                resType.getAdditionalDocumentReference()));

        return res;
    }

    @Override
    public Requirement extractRequirement(RequirementType rt) {
        Requirement r = new ResponseRequirement(
                rt.getID().getValue(),
                rt.getResponseDataType(),
                rt.getDescription().getValue());

        if (!rt.getResponse().isEmpty()) {
            r.setResponse(extractResponse(rt.getResponse().get(0)));
        }

        return r;
    }
    
    public Response extractResponse(ResponseType resType) {
     
        return new DescriptionResponse();
    }

}
