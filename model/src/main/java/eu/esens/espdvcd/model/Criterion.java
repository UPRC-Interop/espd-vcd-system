package eu.esens.espdvcd.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.esens.espdvcd.model.requirement.RequirementGroup;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * Criterion
 *
 *
 * Created by Ulf Lotzmann on 05/03/2016.
 */

@JsonIgnoreProperties(ignoreUnknown=true)

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
     * Cardinality: 1..1<br>
     * InfReqID: tir70-060, tir92-070<br>
     * BusReqID: tbr70-010, tbr70-009, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.ID<br>
     */
    @NotNull
    protected String ID;

    protected String UUID;

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUUID() {
        return UUID;
    }

    /**
     * Criterion type code
     * <p>
     * Code specifying the type of criterion.
     * <p>
     * Data type: Code<br>
     * Cardinality: 1..n - Remark: strings separated with delimiter<br>
     * InfReqID: tir70-061, tir92-071<br>
     * BusReqID: tbr70-013, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.TypeCode<br>
     */
    @NotNull
    protected String typeCode;

    /**
     * Criterion name
     * <p>
     * A short and descriptive name for a criterion. A criterion describes a fact that is used
     * by the contracting body to evaluate and compare tenders by economic operators and which
     * will be used in the award decision or to assess the eligibility of an economic operator.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-062, tir92-072<br>
     * BusReqID: tbr70-010, tbr70-009, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.Name<br>
     */
    protected String name;

    /**
     * Criterion description
     * <p>
     * An extended description of the criterion.
     * <p>
     * Data type: Text<br>
     * Cardinality:	0..1<br>
     * InfReqID: tir70-063, tir92-073<br>
     * BusReqID: tbr70-010, tbr70-009, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.Description<br>
     */
    protected String description;

    /**
     * Criterion legislation
     * <p>
     * Data type: Class<br>
     * Cardinality: 0..n<br>
     * InfReqID:<br>
     * BusReqID:<br>
     * UBL syntax path: ccv:Criterion.LegislationReference<br>
     */
    protected LegislationReference legislationReference;

    /**
     * Requirement group
     * <p>
     * Data type: Class<br>
     * Cardinality: 0..n<br>
     * InfReqID:<br>
     * BusReqID:<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup<br>
     */
    protected List<RequirementGroup> requirementGroups;

    /** */
    public Criterion() {
        this.ID = java.util.UUID.randomUUID().toString();
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
    

    public String getCriterionGroup() {
          String[] ar= this.typeCode.split("\\.",4);
        StringBuilder sb = new StringBuilder();
        int size = 3;
        if (ar.length < size) size = ar.length;
        for (int i=0; i<size; i++) {
            sb.append(ar[i]);
            sb.append(".");
        }
        return sb.delete(sb.lastIndexOf("."), sb.length()).toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.ID);
        hash = 29 * hash + Objects.hashCode(this.typeCode);
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.description);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Criterion other = (Criterion) obj;

        return Objects.equals(this.ID, other.ID);
    }

}
