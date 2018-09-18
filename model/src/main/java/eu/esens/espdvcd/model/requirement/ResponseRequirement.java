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
package eu.esens.espdvcd.model.requirement;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.esens.espdvcd.codelist.enums.CriterionElementTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.response.*;

/**
 * Criterion requirement
 * <p>
 * Requirement to fulfill an specific criterion.
 * <p>
 * Extends RequestRequirement by response and evidences.
 * <p>
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class ResponseRequirement extends RequestRequirement {

    private static final long serialVersionUID = 2750116567195274279L;

    /**
     * Criterion requirement response
     * <p>
     * <p>
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response<br>
     */
    private Response response;

    public ResponseRequirement(@JsonProperty("ID") String ID,
                               @JsonProperty("responseDataType") ResponseTypeEnum responseDataType,
                               @JsonProperty("description") String description) {
        super(ID, responseDataType, description);
    }

    public ResponseRequirement(@JsonProperty("ID") String ID,
                               @JsonProperty("typeCode") CriterionElementTypeEnum typeCode,
                               @JsonProperty("responseDataType") ResponseTypeEnum responseDataType,
                               @JsonProperty("description") String description) {
        super(ID, typeCode, responseDataType, description);
    }

    @Override
    public Response getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(Response response) {
        this.response = response;
    }

}
