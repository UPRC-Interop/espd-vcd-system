/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.model.requirement.response;

public abstract class Response {

    /**
     * Criterion response identifier
     * <p>
     * A language-independent token, e.g., a number, that allows to identify a
     * criterion response uniquely as well as allows to reference the criterion
     * response in other documents. A criterion response describes how an
     * economic operators fulfills an specific criterion.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-158<br>
     * BusReqID: tbr92-018, tbr92-007, tbr92-005, tbr92-006<br>
     * UBL syntax path:
     * ccv:Criterion.RequirementGroup.Requirement.DescriptionResponse.ID<br>
     */
    private String ID;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

}
