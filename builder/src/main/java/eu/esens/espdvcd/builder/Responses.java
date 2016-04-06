/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.requirement.AmountResponse;
import eu.esens.espdvcd.model.requirement.CountryCodeResponse;
import eu.esens.espdvcd.model.requirement.DateResponse;
import eu.esens.espdvcd.model.requirement.DescriptionResponse;
import eu.esens.espdvcd.model.requirement.EvidenceURLCodeResponse;
import eu.esens.espdvcd.model.requirement.EvidenceURLResponse;
import eu.esens.espdvcd.model.requirement.IndicatorResponse;
import eu.esens.espdvcd.model.requirement.PercentageResponse;
import eu.esens.espdvcd.model.requirement.PeriodResponse;
import eu.esens.espdvcd.model.requirement.QuantityIntegerResponse;
import eu.esens.espdvcd.model.requirement.QuantityResponse;
import eu.esens.espdvcd.model.requirement.QuantityYearResponse;
import eu.esens.espdvcd.model.requirement.Response;


public class Responses {

    public enum Type {

        INDICATOR,
        DATE,
        DESCRIPTION,
        QUANTITY,
        QUANTITY_YEAR,
        QUANTITY_INTEGER,
        AMOUNT,
        CODE_COUNTRY,
        PERCENTAGE,
        PERIOD,
        EVIDENCE_URL,
        CODE;
    }
    
    public static Response createResponse(Type type) {
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
