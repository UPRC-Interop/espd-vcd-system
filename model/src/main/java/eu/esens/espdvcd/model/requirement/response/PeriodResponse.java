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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Period response
 *
 * Description response for defining a period.
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class PeriodResponse extends DescriptionResponse implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    /**
     * Criterion response description - Criterion fulfillment period
     * <p>
     * Description of the period that is declared to fulfil this criterion.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-168<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path:
     * ccv:Criterion.RequirementGroup.Requirement.Response.Description<br>
     */
    private LocalDate startDate;
    private LocalDate endDate;

    public PeriodResponse() {
    }

    public PeriodResponse(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public PeriodResponse(@JsonProperty("period") String description) {
        this.description = description;
    }

    @Override
    @JsonProperty("period")
    public String getDescription() {  return this.description; }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
