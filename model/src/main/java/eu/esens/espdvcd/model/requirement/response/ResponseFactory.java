/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.esens.espdvcd.model.requirement.response;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;

public class ResponseFactory {
    
    public static Response createResponse(ResponseTypeEnum type) {
        switch(type) {

            case INDICATOR:
                return new IndicatorResponse();

            case DATE:
                return new DateResponse();

            case DESCRIPTION:
                return new DescriptionResponse();
                
            case QUANTITY:
                return new QuantityResponse();

            case QUANTITY_YEAR:
                return new QuantityYearResponse();
                
            case QUANTITY_INTEGER:
                return new QuantityIntegerResponse();

            case AMOUNT:
                return new AmountResponse();
                
            case CODE_COUNTRY:
                return new CountryCodeResponse();
                
            case PERCENTAGE:
                return new PercentageResponse();
                
            case PERIOD:
                return new PeriodResponse();
                
            case EVIDENCE_URL:
                return new EvidenceURLResponse();

            case CODE:
                return new EvidenceURLCodeResponse();

            default:
                return null;
                   
        }
    }
}
