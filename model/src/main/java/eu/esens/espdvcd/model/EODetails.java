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
package eu.esens.espdvcd.model;

import eu.esens.espdvcd.codelist.enums.EORoleTypeEnum;
import eu.esens.espdvcd.model.requirement.response.AmountResponse;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Economic operator
 * <p>
 * Any natural or legal person or public entity or group of such persons and/or
 * entities, including any temporary association of undertakings, which offers
 * the execution of works and/or a work, the supply of products or the provision
 * of services on the market. Information about the party submitting the
 * qualification.
 * <p>
 * BusReqID: tbr92-017, tbr92-010, tbr92-028, tbr92-029
 * <p>
 * <p>
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class EODetails {

    /**
     * Economic operator identifier
     * <p>
     * An identifier that identifies the economic operator, such as a legal
     * registration identifier.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-030<br>
     * BusReqID: tbr92-001<br>
     * UBL syntax path:
     * espd-cac:EconomicOperatorParty.Party.PartyIdentification<br>
     */
    @NotNull
    private String ID;

    /**
     * Economic operator electronic address identifier
     * <p>
     * Electronic address of the economic operator.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-031<br>
     * BusReqID: tbr92-001<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.EndpointID<br>
     */
    private String electronicAddressID;

    /**
     * Economic operator website
     * <p>
     * The website of the economic operator.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-316<br>
     * BusReqID: tbr041-002<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.WebsiteURIID<br>
     */
    private String webSiteURI;

    /**
     * Economic operator name
     * <p>
     * The name of the economic operator.
     * <p>
     * Data type: Text<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-033<br>
     * BusReqID: tbr92-001<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.PartyName.Name<br>
     */
    @NotNull
    private String name;

    /**
     * SME indicator
     * <p>
     * Indicates whether the economic operator can be categorized as an SME
     * (true) or not. According to the EC, SME are enterprises with less than
     * 250 employees, a turnover less than EUR 50 m and a balance sheet total
     * less than EUR 43 m.
     * <p>
     * Data type: Indicator<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-035<br>
     * BusReqID: tbr92-004<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.SMEIndicator<br>
     */
    private boolean smeIndicator;

    /**
     * Postal address
     * <p>
     * Address information.
     * <p>
     * Data type: <br>
     * Cardinality: 1..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.PostalAddress<br>
     */
    @NotNull
    private PostalAddress postalAddress;

    /**
     * Contacting details
     * <p>
     * Used to provide contacting information for a party in general or a
     * person.
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.Contact<br>
     */
    private ContactingDetails contactingDetails;

    /**
     * Representative natural person
     * <p>
     * Information about individuals who in one way or the other represent the
     * economic operator.
     * <p>
     * Data type: <br>
     * Cardinality: 0..n<br>
     * InfReqID: <br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path:
     * espd-cac: EconomicOperatorParty.RepresentativeNaturalPerson.PowerOfAttorney.Description.AgentParty.Person<br>
     */
    private List<NaturalPerson> naturalPersons;

    /**
     * Lot reference
     * <p>
     * An identifier for the lot.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1 (1..n)<br>
     * InfReqID: tir92-130<br>
     * BusReqID: tbr92-014<br>
     * UBL syntax path: cac:ProcurementProjectLot.ID<br>
     */
    @NotNull
    private String procurementProjectLot;

    /**
     * Employee Quantity
     * <p>
     * The quantity of the employees the Economic Operator has.
     * Needed in Self-Contained ESPD
     * </p>
     * Data type: Number<br>
     * Cardinality: 1..1<br>
     * UBL syntax path: cac:EconomicOperatorParty.QualifyingParty.EmployeeQuantity<br>
     */
    @NotNull
    private int employeeQuantity;

    /**
     * Economic operator general turnover
     * <p>
     * The general turnover of the Economic Operator.
     * Needed in Self-Contained ESPD
     * </p>
     * Data type: Amount<br>
     * Cardinality: 0..n<br>
     * UBL syntax path: cac:EconomicOperatorParty.QualifyingParty.FinancialCapability.ValueAmount<br>
     */
    @NotNull
    private AmountResponse generalTurnover;

    /**
     * Economic operator group name
     * <p>
     * The name of the Group of the Economic Operators.
     * Needed in Self-Contained ESPD
     * </p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * UBL syntax path: cbc:EconomicOperatorGroupName<br>
     */
    @NotNull
    private String eoGroupName;

    /**
     * EO Role Code
     * <p>
     * Identifies the role of the economic operator in the bid.
     * Rule: Compulsory use of the Code List EORoleType.
     * <p>
     * Data type: Code<br>
     * Cardinality: 1...1<br>
     * InfReqID:
     * BusReqID
     * UBL syntax path: EconomicOperatorParty.EconomicOperatorRole.RoleCode<br>
     */
    private EORoleTypeEnum eoRole;

    public String getProcurementProjectLot() {
        return procurementProjectLot;
    }

    public void setProcurementProjectLot(String procurementProjectLot) {
        this.procurementProjectLot = procurementProjectLot;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getElectronicAddressID() {
        return electronicAddressID;
    }

    public void setElectronicAddressID(String electronicAddressID) {
        this.electronicAddressID = electronicAddressID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSmeIndicator() {
        return smeIndicator;
    }

    public void setSmeIndicator(boolean smeIndicator) {
        this.smeIndicator = smeIndicator;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    public List<NaturalPerson> getNaturalPersons() {
        if (naturalPersons == null) {
            naturalPersons = new ArrayList<>();
        }
        return naturalPersons;
    }

    public void setNaturalPersons(List<NaturalPerson> naturalPersons) {
        this.naturalPersons = naturalPersons;
    }

    public ContactingDetails getContactingDetails() {
        return contactingDetails;
    }

    public void setContactingDetails(ContactingDetails contactingDetails) {
        this.contactingDetails = contactingDetails;
    }

    public String getWebSiteURI() {
        return webSiteURI;
    }

    public void setWebSiteURI(String webSiteURI) {
        this.webSiteURI = webSiteURI;
    }

    public EORoleTypeEnum getEoRole() {
        return eoRole;
    }

    public void setEoRole(EORoleTypeEnum eoRole) {
        this.eoRole = eoRole;
    }
}
