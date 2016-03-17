package eu.esens.espdvcd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * documentation scheme of data fields:
 * <p>
 * name
 * <p>
 * descr
 * <p>
 * Data type: <br>
 * Cardinality: <br>
 * InfReqID: <br>
 * BusReqID: <br>
 * UBL syntax path: <br>
 *
 *
 * Created by Ulf Lotzmann on 05/03/2016.
 */
public class Criterion implements Serializable {

    private static final long serialVersionUID = 4541034499184281949L;

    /**
     * Criterion identifier
     * <p>
     * A language-independent token, e.g., a number, that allows to identify a criterion uniquely
     * as well as allows to reference the criterion in other documents. A criterion describes a
     * fact that is used by the contracting body to evaluate and compare tenders by economic
     * operators and which will be used in the award decision.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-070<br>
     * BusReqID: tbr92-015<br>
     * UBL syntax path: ccv:Criterion.ID<br>
     */
    private String ID;

    /**
     * Criterion type code
     * <p>
     * Code specifying the type of criterion.
     * <p>
     * Data type: Code<br>
     * Cardinality: 1..n - Remark: strings separated with delimiter TODO<br>
     * InfReqID: tir92-071<br>
     * BusReqID: tbr92-015<br>
     * UBL syntax path: ccv:Criterion.TypeCode<br>
     */
    private String typeCode;

    /**
     * Criterion name
     * <p>
     * A short and descriptive name for a criterion. A criterion describes a fact that is used
     * by the contracting body to evaluate and compare tenders by economic operators and which
     * will be used in the award decision or to assess the eligibility of an economic operator.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-072<br>
     * BusReqID: tbr92-015<br>
     * UBL syntax path: ccv:Criterion.Name<br>
     */
    private String name;

    /**
     * Criterion description
     * <p>
     * An extended description of the criterion.
     * <p>
     * Data type: Text<br>
     * Cardinality:	0..1<br>
     * InfReqID: tir92-073<br>
     * BusReqID: tbr92-015<br>
     * UBL syntax path: ccv:Criterion.Description<br>
     */
    private String description;

    /**
     * Criterion legislation
     * <p>
     * Data type: Class<br>
     * Cardinality: 0..n<br>
     * InfReqID:<br>
     * BusReqID:<br>
     * UBL syntax path: ccv:Criterion.LegislationReference<br>
     */
    private LegislationReference legislationReference;

    /**
     * Requirement group
     * <p>
     * Data type: Class<br>
     * Cardinality: 0..n<br>
     * InfReqID:<br>
     * BusReqID:<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup<br>
     */
    private List<RequirementGroup> requirementGroups;

    /** */
    public Criterion() {
        this.ID = UUID.randomUUID().toString();    
    }
    
    public Criterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference, List<RequirementGroup> requirementGroups) {
        this.ID = ID;
        this.typeCode = typeCode;
        this.name = name;
        this.description = description;
        this.legislationReference = legislationReference;
        this.requirementGroups = requirementGroups;
    }
    
    public Criterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference) {
        this.ID = ID;
        this.typeCode = typeCode;
        this.name = name;
        this.description = description;
        this.legislationReference = legislationReference;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RequirementGroup> getRequirementGroups() {
        if (requirementGroups == null) {
            requirementGroups = new ArrayList<>();
        }
        return requirementGroups;
    }

    public void setRequirementGroups(List<RequirementGroup> requirementGroups) {
        this.requirementGroups = requirementGroups;
    }

    public LegislationReference getLegislationReference() {
        return legislationReference;
    }

    public void setLegislationReference(LegislationReference legislationReference) {
        this.legislationReference = legislationReference;
    }
    
}