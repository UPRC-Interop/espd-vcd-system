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

/**
 * Economic Identifier Response Class
 * Added for storing an Identifier along with an EOIDType codelist value
 */
public class EOIdentifierResponse extends Response implements Serializable {
    private static final long serialVersionUID = -5360297563987726650L;

    /**
     * Economic Identifier Response
     * <p>
     * An Identifier that also contains information about it's type.
     * Necessary for describing an EO identifier with it's corresponding
     * EOIDType.
     * </p>
     * Data Type: Text, Code<br>
     * Cardinality: 0..n<br>
     * UBL syntax path:
     * ccv:Criterion.RequirementGroup.Requirement.Response.Identifier<br>
     */
    private String EOIDType;
    private String ID;

    public EOIdentifierResponse(String EOIDType, String ID) {
        this.EOIDType = EOIDType;
        this.ID = ID;
    }

    public EOIdentifierResponse() {
    }

    public String getEOIDType() {
        return EOIDType;
    }

    public void setEOIDType(String EOIDType) {
        this.EOIDType = EOIDType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
