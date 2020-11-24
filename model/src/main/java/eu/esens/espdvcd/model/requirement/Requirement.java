/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.model.requirement;

import eu.esens.espdvcd.codelist.enums.RequirementTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.response.Response;

import java.io.Serializable;
import java.util.Map;

/**
 * Criterion requirement interface
 * <p>
 * Requirement to fulfill an specific criterion.
 * <p>
 * Created by Ulf Lotzmann on 21/03/2016.
 */

public interface Requirement extends Serializable {
    String getID();

    void setID(String ID);

    String getUUID();

    void setUUID(String UUID);

    /**
     * In edm version 2 this is mapped to: cbc:ValueDataTypeCode
     *
     * @return
     */
    ResponseTypeEnum getResponseDataType();

    void setResponseDataType(ResponseTypeEnum responseDataType);

    /**
     * Possible types are 'CAPTION , REQUIREMENT, QUESTION'
     * <p>
     * The Regulated ESPD documents do not specify REQUIREMENTS,
     * only QUESTIONS. The SELF-CONTAINED version does.
     *
     * @return
     */
    RequirementTypeEnum getType();

    void setType(RequirementTypeEnum type);

    String getDescription();

    void setDescription(String description);

    Response getResponse();

    void setResponse(Response response);

    void setMandatory(boolean mandatory);

    void setMultiple(boolean multiple);

    boolean isMandatory();

    boolean isMultiple();

    String getResponseValuesRelatedArtefact();

    void setResponseValuesRelatedArtefact(String responseValuesRelatedArtefact);

    Map<String, String> getPropertyKeyMap();

    String getPropertyKeyOrNull();

    void setKeyAsResponseValue(boolean useKeyAsValue);

}
