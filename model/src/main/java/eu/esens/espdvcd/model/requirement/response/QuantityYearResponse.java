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

/**
 * Quantity year response
 *
 * (part of Period declaration fulfilment)
 *
 * Created by Ulf Lotzmann on 22/03/2016.
 */
public class QuantityYearResponse extends Response implements Serializable {

    private static final long serialVersionUID = -9178109536250269929L;

    /**
     * Criterion fulfillment date
     * <p>
     * Declared date where this criterion was fulfilled.
     * <p>
     * Data type: Date<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-166<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Date<br>
     */
    int year;
    
    public QuantityYearResponse() {
    }


    public QuantityYearResponse(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
