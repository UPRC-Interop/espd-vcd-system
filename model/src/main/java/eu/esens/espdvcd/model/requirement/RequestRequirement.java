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
import eu.esens.espdvcd.codelist.enums.RequirementTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.response.Response;

import javax.validation.constraints.NotNull;

/**
 * Criterion requirement
 * <p>
 * Requirement to fulfill an specific criterion.
 * <p>
 * Created by ixuz on 2/24/16.
 */
public class RequestRequirement implements Requirement {

    private static final long serialVersionUID = 528517963577425517L;

    /**
     * Criterion requirement identifier
     * <p>
     * Identifier of the requirement that fulfills an specific criterion.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir70-080, tir92-135<br>
     * BusReqID: tbr70-013, tbr70-004, tbr92-015, tbr92-016, tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.ID<br>
     */
    @NotNull
    private String ID;

    private String UUID;

    /**
     * Criterion requirement description
     * <p>
     * Description of the requirement that fulfills an specific criterion.
     * <p>
     * Data type: Text<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir70-081, tir92-136<br>
     * BusReqID: tbr70-013, tbr70-004, tbr92-015, tbr92-016, tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Description<br>
     */
    @NotNull
    private String description;

    /**
     * Criterion response indicator
     * <p>
     * An indicator which points to the expected data type of the response.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: tbr70-013, tbr70-004, tbr92-015, tbr92-016, tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.ResponseDataType<br>
     */
    private ResponseTypeEnum responseDataType;

    /**
     * The type of property. Used to verify that structure of the property is correct
     * UBL syntax path:
     */
    private RequirementTypeEnum type = RequirementTypeEnum.QUESTION;

    private boolean mandatory;

    private boolean multiple;

    private String responseValuesRelatedArtefact;

    public RequestRequirement(String ID,
                              ResponseTypeEnum responseDataType,
                              String description) {
        this.ID = ID;
        this.responseDataType = responseDataType;
        this.description = description;
        // apply default cardinality 1
        this.mandatory = true;
        this.multiple = false;
    }

    public RequestRequirement(@JsonProperty("ID") String ID,
                              @JsonProperty("type") RequirementTypeEnum type,
                              @JsonProperty("responseDataType") ResponseTypeEnum responseDataType,
                              @JsonProperty("description") String description) {
        this.ID = ID;
        this.type = type;
        this.responseDataType = responseDataType;
        this.description = description;
        // apply default cardinality 1
        this.mandatory = true;
        this.multiple = false;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String getUUID() {
        return UUID;
    }

    @Override
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Response getResponse() {
        throw new UnsupportedOperationException("Not supported in ESPD request.");
    }

    @Override
    public void setResponse(Response response) {
        throw new UnsupportedOperationException("Not supported in ESPD request.");
    }

    @Override
    public void setResponseDataType(ResponseTypeEnum responseDataType) {
        this.responseDataType = responseDataType;
    }

    @Override
    public RequirementTypeEnum getType() {
        return type;
    }

    @Override
    public ResponseTypeEnum getResponseDataType() {
        return this.responseDataType;
    }

    @Override
    public void setType(RequirementTypeEnum type) {
        this.type = type;
    }

    @Override
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    @Override
    public boolean isMultiple() {
        return multiple;
    }

    @Override
    public String getResponseValuesRelatedArtefact() {
        return responseValuesRelatedArtefact;
    }

    @Override
    public void setResponseValuesRelatedArtefact(String responseValuesRelatedArtefact) {
        this.responseValuesRelatedArtefact = responseValuesRelatedArtefact;
    }

}