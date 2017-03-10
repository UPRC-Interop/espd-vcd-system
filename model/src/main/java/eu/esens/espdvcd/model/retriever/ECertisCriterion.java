package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisCriterion;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceGroup;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisLegislationReference;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisText;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( {"ID", "typeCode", "name", "description", "domainID", "versionID", "legislationReference", "evidenceGroup",
"subCriterion", "parentCriterion"} )
public class ECertisCriterion implements IECertisCriterion {
    
    private String ID;
    private String typeCode;
    @JsonDeserialize(as = ECertisText.class)
    private IECertisText name;
    @JsonDeserialize(as = ECertisText.class)
    private IECertisText description;
    private String domainID;
    private String versionID;
    @JsonDeserialize(as = List.class, contentAs = ECertisLegislationReference.class)
    private List<IECertisLegislationReference> legislationReference;
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidenceGroup.class)
    private List<IECertisEvidenceGroup> evidenceGroup;
    @JsonDeserialize(as = List.class, contentAs = ECertisCriterion.class)
    private List<IECertisCriterion> subCriterion;
    @JsonDeserialize(as = ECertisCriterion.class)
    private IECertisCriterion parentCriterion;
    
    @Override
    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    @Override
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    @JsonProperty("TypeCode")
    public String getTypeCode() {
        return typeCode;
    }

    @Override
    public void setName(IECertisText name) {
        this.name = name;
    }

    @Override
    @JsonProperty("Name")
    public IECertisText getName() {
        return name;
    }
    
    @Override
    public void setDescription(IECertisText description) {
        this.description = description;
    }

    @Override
    @JsonProperty("Description")
    public IECertisText getDescription() {
        return description;
    }
    
    @Override
    public void setDomainID(String domainID) {
        this.domainID = domainID;
    }

    @Override
    @JsonProperty("DomainID")
    public String getDomainID() {
        return domainID;
    }

    @Override
    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    @Override
    @JsonProperty("VersionID")
    public String getVersionID() {
        return versionID;
    }

    @Override
    public void setLegislationReference(List<IECertisLegislationReference> legislationReference) {
        this.legislationReference = legislationReference;
    }

    @Override
    @JsonProperty("LegislationReference")
    public List<IECertisLegislationReference> getLegislationReference() {
        return legislationReference;
    }

    @Override
    public void setEvidenceGroup(List<IECertisEvidenceGroup> evidenceGroup) {
        this.evidenceGroup = evidenceGroup;
    }

    @Override
    @JsonProperty("RequirementGroup")
    public List<IECertisEvidenceGroup> getEvidenceGroup() {
        return evidenceGroup;
    }

    @Override
    public void setSubCriterion(List<IECertisCriterion> subCriterion) {
        this.subCriterion = subCriterion;
    }

    @Override
    @JsonProperty("SubCriterion")
    public List<IECertisCriterion> getSubCriterion() {
        return subCriterion;
    }
    
    @Override
    public void setParentCriterion(IECertisCriterion parentCriterion) {
        this.parentCriterion = parentCriterion;
    }

    @Override
    @JsonProperty("ParentCriterion")
    public IECertisCriterion getParentCriterion() {
        return parentCriterion;
    }
    
}
