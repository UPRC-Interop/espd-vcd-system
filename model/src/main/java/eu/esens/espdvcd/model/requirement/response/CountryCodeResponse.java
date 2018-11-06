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
import javax.validation.constraints.NotNull;

/**
 * Country code response
 *
 * Is used in version 1.0.2 for country code and code to access the online evidence.
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class CountryCodeResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    /**
     * Criterion fulfillment code
     * <p>
     * Event declared to fulfil this criterion, expressed as a code.
     * <p>
     * Data type: Code<br>
     * Cardinality: 0..1<br>
     * InfReqID:  tir92-167<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Code<br>
     */
    @NotNull
    private String countryCode;

    public CountryCodeResponse() {
    }

    public CountryCodeResponse(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
