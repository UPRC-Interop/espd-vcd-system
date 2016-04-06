/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.requirement.AmountResponse;
import eu.esens.espdvcd.model.requirement.DateResponse;
import eu.esens.espdvcd.model.requirement.DescriptionResponse;
import eu.esens.espdvcd.model.requirement.Response;


public class Responses {

        public static enum Type {
        IDENTIFIER,
        DATE,
        RESPONSE_INDICATOR,
        DESCRIPTION,
        PERIOD,
        EVIDENCE_URL_CODE,
        AMOUNT,
        EVIDENCE_URL
    }
        
    public static Response createResponse(Responses.Type type) {
        switch(type) {
            case DESCRIPTION:
                return DescriptionResponse();
            case DATE:
                return DateResponse();
            case AMOUNT:
                return AmountResponse();
            default:
                return null;
        }
    }
    
    public static Response DescriptionResponse() {
        return new DescriptionResponse();
    }
    
    public static Response DateResponse() {
        return new DateResponse();
    }
    
    public static Response AmountResponse() {
        return new AmountResponse();
    }
    
    


}
