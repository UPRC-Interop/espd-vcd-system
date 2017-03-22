package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisCriterion;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceGroup;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisLegislationReference;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;
import eu.esens.espdvcd.model.util.CustomTextValueDeserializer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
            "ID",
            "typeCode",
            "name",
            "description",
            "domainID",
            "versionID",
            "legislationReference",
            "evidenceGroup",
            "subCriterion",
            "parentCriterion"
        })
public class ECertisCriterion extends Criterion implements IECertisCriterion {

    private String domainID;
    private String versionID;
    private List<IECertisLegislationReference> legislationReference;
    private List<IECertisEvidenceGroup> evidenceGroup;
    private List<IECertisCriterion> subCriterion;
    private IECertisCriterion parentCriterion;

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    @JsonProperty("TypeCode")
    public String getTypeCode() {
        return typeCode;
    }

    @Override
    @JsonDeserialize(using = CustomTextValueDeserializer.class)
    public void setName(String name) {
        this.name = name;
    }

    @Override
    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @Override
    @JsonDeserialize(using = CustomTextValueDeserializer.class)
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setDomainID(String domainID) {
        this.domainID = domainID;
    }

    @Override
    @JsonProperty("DomainID")
    public String getDomainID() {
        return domainID;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    @Override
    @JsonProperty("VersionID")
    public String getVersionID() {
        return versionID;
    }

    @Override
    @JsonSetter("LegislationReference")
    @JsonDeserialize(as = List.class, contentAs = ECertisLegislationReference.class)
    public void setTheLegislationReference(List<IECertisLegislationReference> legislationReference) {
        this.legislationReference = legislationReference;
    }

    @Override
    @JsonProperty("LegislationReference")
    public List<IECertisLegislationReference> getTheLegislationReference() {
        if (legislationReference == null) {
            legislationReference = new ArrayList<>();
        }
        return legislationReference;
    }

//    @Override
//    public LegislationReference getLegislationReference() {
//        return super.getLegislationReference(); //To change body of generated methods, choose Tools | Templates.
//    }
//        
//    @Override
//    public void setLegislationReference(LegislationReference legislationReference) {
//        super.setLegislationReference(legislationReference); //To change body of generated methods, choose Tools | Templates.
//    }
   
    @Override
    @JsonSetter("RequirementGroup")
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidenceGroup.class)
    public void setEvidenceGroup(List<IECertisEvidenceGroup> evidenceGroup) {
        this.evidenceGroup = evidenceGroup;
    }

    @Override
    @JsonProperty("RequirementGroup")
    public List<IECertisEvidenceGroup> getEvidenceGroup() {
        if (evidenceGroup == null) {
            evidenceGroup = new ArrayList<>();
        }
        return evidenceGroup;
    }

    @Override
    @JsonSetter("SubCriterion")
    @JsonDeserialize(as = List.class, contentAs = ECertisCriterion.class)
    public void setSubCriterion(List<IECertisCriterion> subCriterion) {
        this.subCriterion = subCriterion;
    }

    @Override
    @JsonProperty("SubCriterion")
    public List<IECertisCriterion> getSubCriterion() {
        if (subCriterion == null) {
            subCriterion = new ArrayList<>();
        }
        return subCriterion;
    }

    @Override
    @JsonSetter("ParentCriterion")
    @JsonDeserialize(as = ECertisCriterion.class)
    public void setParentCriterion(IECertisCriterion parentCriterion) {
        this.parentCriterion = parentCriterion;
    }

    @Override
    @JsonProperty("ParentCriterion")
    public IECertisCriterion getParentCriterion() {
        return parentCriterion;
    }
   
    
    // Ignore that property during deserialization process
    @Override
    @JsonIgnore
    public String getCriterionGroup() {
        return super.getCriterionGroup();
    }
    
}
