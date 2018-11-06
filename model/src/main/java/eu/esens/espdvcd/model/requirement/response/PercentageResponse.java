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
package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Percentage response
 *
 * (part of Period declaration fulfilment)
 *
 * Created by Ulf Lotzmann on 22/03/2016.
 */
public class PercentageResponse extends Response implements Serializable {

    private static final long serialVersionUID = -7417383925707504848L;

    /**
     * Criterion fulfillment percentage
     * <p>
     * Declared percentage that fulfills this criterion.
     * <p>
     * Data type: Percentage<br>
     * Cardinality: 0..1<br>
     * InfReqID:  tir92-164<br>
     * BusReqID: tbr92-018, tbr92-005<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Percent<br>
     */
    private BigDecimal percentage;

    public PercentageResponse() {
    }

    public PercentageResponse(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
