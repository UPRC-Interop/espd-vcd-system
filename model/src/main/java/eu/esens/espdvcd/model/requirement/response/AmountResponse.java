/**
 * Copyright 2016-2019 University of Piraeus Research Center
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
package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Amount response
 *
 * (part of Period declaration fulfilment)
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class AmountResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    /**
     * Criterion fulfillment amount
     * <p>
     * Declared amount that fulfills this criterion.
     * <p>
     * Data type: Amount<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-162<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Amount<br>
     */
    private BigDecimal amount;

    /**
     * Currency
     * <p>
     * (not specified in domain vocabulary)
     * <p>
     * Data type: <br>
     * Cardinality: <br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: <br>
     */
    private String currency;

    public AmountResponse() {
    }
    
    public AmountResponse(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    
    public String getCurrency() {
        return currency;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
