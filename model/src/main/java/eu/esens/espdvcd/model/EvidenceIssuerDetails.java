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
package eu.esens.espdvcd.model;

/**
 * Evidence issuer party
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class EvidenceIssuerDetails {

    /**
     * Evidence issuer party identifier
     * <p>
     * The identifier of the party issuer of the evidence.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir041-143<br>
     * BusReqID: tbr041-017, tbr041-007, tbr041-006<br>
     * UBL syntax path:
     *      ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerDetails.PartyIdentification<br>
     */
    private String ID;


    /**
     * Evidence issuer party name
     * <p>
     * The name of the party issuer of the evidence.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir041-144<br>
     * BusReqID: tbr041-017, tbr041-007, tbr041-006<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerDetails.PartyName<br>
     */
    private String name;


    /**
     * Evidence issuer website
     * <p>
     * The website of the party issuer of the evidence.
     * <p>
     * Data type: WebsiteURIID<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir041-322<br>
     * BusReqID: <br>
     * UBL syntax path:
     *      ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerDetails.WebsiteURIID<br>
     */
    private String website;

    public EvidenceIssuerDetails() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() { return website; }

    public void setWebsite(String website) { this.website = website; }

}
