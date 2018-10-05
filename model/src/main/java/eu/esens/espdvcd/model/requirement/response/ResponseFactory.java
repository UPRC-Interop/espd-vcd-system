/**
 * Copyright 2016-2018 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

            case URL:
                return new URLResponse();

            case IDENTIFIER:
                return new IdentifierResponse();

            default:
                return null;
                   
        }
    }
}
