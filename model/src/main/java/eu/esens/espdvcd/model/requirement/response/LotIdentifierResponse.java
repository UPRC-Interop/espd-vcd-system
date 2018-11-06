/**
 * Copyright 2016-2018 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.model.requirement.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Lots Identifier Response
 */
public class LotIdentifierResponse extends Response implements Serializable {

    /**
     * Criterion Response for Self-Contained Lots
     * <p>
     * A list of Identifiers Representing the Lots that the EO applies for.
     * </p>
     * Data Type: List of Identifiers<br>
     * Cardinality: 0..1<br>
     * UBL syntax path:
     * ccv:Criterion.RequirementGroup.Requirement.Response.Identifier<br>
     */
    private static final long serialVersionUID = -7114827204655335550L;
    private List<String> lots;

    public LotIdentifierResponse() {
    }

    public LotIdentifierResponse(List<String> lots) {
        this.lots = lots;
    }

    private void initLotsList() {
        if (lots == null) {
            lots = new ArrayList<>();
        }
    }

    @JsonCreator
    @JsonProperty("lots")
    public List<String> getLotsList() {
        initLotsList();
        return lots;
    }

    @JsonIgnore
    public String getLots() {
        return String.join(",", getLotsList());
    }

    @JsonIgnore
    public void setLots(String lots) {
        getLotsList().clear();
        if (Objects.nonNull(lots)) {
            String[] lotsArray = lots.split(",");
            this.lots.addAll(Arrays.asList(lotsArray));
        }
    }

}