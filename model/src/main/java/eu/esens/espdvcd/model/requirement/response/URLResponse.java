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

public class URLResponse extends Response implements Serializable {

    private static final long serialVersionUID = 331803496620396887L;

    /**
     * Criterion fulfillment URI
     * <p>
     * A URI used as a reply to the criterion property.
     * <p>
     * Data type: URI<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-558<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.URL<br>
     */
    private String url;

    public URLResponse() {

    }

    public URLResponse(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
