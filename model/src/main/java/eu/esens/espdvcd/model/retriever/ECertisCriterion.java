package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( {"ID", "typeCode", "name", "domainID", "versionID", "legislationReference", "evidenceGroups"} )
public class ECertisCriterion {
    
    private String ID;
    private String typeCode;
    private Text name;
    private String domainID;
    private String versionID;
    private List<LegislationReference> legislationReference;
    private List<EvidenceGroup> evidenceGroups;
//        
//    private ECertisCriterion parentCriterion;
//    private List<ECertisCriterion> subCriterions; 
    
    
    public ECertisCriterion() {
        
    }
    
    @JsonProperty(value = "ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @JsonProperty(value = "TypeCode")
    public String getTypeCode() {
        return typeCode;
    }
        
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
    
    @JsonProperty(value = "Name")
    public Text getName() {
        return name;
    }

    public void setName(Text name) {
        this.name = name;
    }
       
    @JsonProperty(value = "DomainID")
    public String getDomainID() {
        return domainID;
    }

    public void setDomainID(String domainID) {
        this.domainID = domainID;
    }
    
    @JsonProperty(value = "VersionID")
    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }
    
    @JsonProperty(value = "LegislationReference")
    public List<LegislationReference> getLegislationReference() {
        if (legislationReference == null) {
            legislationReference = new ArrayList<>();
        }
        return legislationReference;
    }

    public void setLegislationReference(List<LegislationReference> legislationReference) {
        this.legislationReference = legislationReference;
    }
          
    public static class Text {
        
        private String languageID;
        private String value;
        
        @JsonProperty("languageID")
        public String getLanguageID() {
            return languageID;
        }

        public void setLanguageID(String languageID) {
            this.languageID = languageID;
        }
        
        @JsonProperty("value")
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
           
    }
    
    public static class EvidenceIntendedUse {
        
        private Text description;

        @JsonProperty("Description")
        public Text getDescription() {
            return description;
        }

        public void setDescription(Text description) {
            this.description = description;
        }
               
    }
    
    @JsonPropertyOrder( {"title", "description", "jurisdictionLevelCode", "article", "URI"} )
    public static class LegislationReference {
                
        private Text title;
        private Text description;
        private String jurisdictionLevelCode;
        private Text article;
        private String URI;
        
        @JsonProperty("Title")
        public Text getTitle() {
            return title;
        }

        public void setTitle(Text title) {
            this.title = title;
        }
        
        @JsonProperty("Description")
        public Text getDescription() {
            return description;
        }

        public void setDescription(Text description) {
            this.description = description;
        }
        
        @JsonProperty("JurisdictionLevelCode")
        public String getJurisdictionLevelCode() {
            return jurisdictionLevelCode;
        }

        public void setJurisdictionLevelCode(String JurisdictionLevelCode) {
            this.jurisdictionLevelCode = JurisdictionLevelCode;
        }
        
        @JsonProperty("Article")
        public Text getArticle() {
            return article;
        }

        public void setArticle(Text article) {
            this.article = article;
        }
        
        @JsonProperty("URI")
        public String getURI() {
            return URI;
        }

        public void setURI(String URI) {
            this.URI = URI;
        }
        
    }
    
    @JsonPropertyOrder( {"ID", "evidences"} )
    public static class EvidenceGroup {
        
        private String ID;
        private List<Evidence> evidences;
        
        @JsonProperty("ID")
        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }
        
        @JsonProperty("TypeOfEvidence")
        public List<Evidence> getEvidences() {
            return evidences;
        }

        public void setEvidences(List<Evidence> evidences) {
            this.evidences = evidences;
        }
                       
    }
    
    @JsonPropertyOrder( {"ID", "typeCode", "name"} )
    public static class Evidence {
        
        private String ID;
        private String typeCode;
        private Text name;
        private Text description;
        private String versionID;
        private EvidenceIntendedUse evidenceIntendUse;
        
        
    }
    
}
