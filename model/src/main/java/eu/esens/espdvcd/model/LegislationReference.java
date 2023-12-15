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

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.esens.espdvcd.model.retriever.ECertisLegislationReference;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by Ulf Lotzmann on 05/03/2016.
 */
public class LegislationReference {


    /**
     * Legislation title
     * <p>
     * Title of the legislation.
     * <p>
     * Data type: Text<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir70-064, tir92-130<br>
     * BusReqID: tbr70-013, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.LegislationReference.Title<br>
     */

    @NotNull
    protected String title;

    /**
     * Legislation description
     * <p>
     * Textual description of the legislation.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-065, tir92-131<br>
     * BusReqID: tbr70-013, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.LegislationReference.Description<br>
     */
    protected String description;

    /**
     * Jurisdiction level
     * <p>
     * Jurisdictional level of a particular legislation.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-066, tir92-132<br>
     * BusReqID: tbr70-013, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.LegislationReference.JurisdictionLevelCode<br>
     */
    protected String jurisdictionLevelCode;

    /**
     * Legislation article
     * <p>
     * Textual description of the article of the legislation.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-067, tir92-133<br>
     * BusReqID: tbr70-013, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.LegislationReference.Article<br>
     */
    protected String article;

    /**
     * Legislation URI
     * <p>
     * URI that points to a particular legislation	Identifier
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-068, tir92-134<br>
     * BusReqID: tbr70-013, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.LegislationReference.URI<br>
     */
    protected String URI;

    public LegislationReference(@JsonProperty("Title") String title, @JsonProperty("Description") String description,
                                @JsonProperty("JurisdictionLevelCode") String jurisdictionLevelCode,
                                @JsonProperty("Article") String article, @JsonProperty("URI") String URI) {
        this.title = title;
        this.description = description;
        this.jurisdictionLevelCode = jurisdictionLevelCode;
        this.article = article;
        this.URI = URI;
    }

    public LegislationReference(ECertisLegislationReference legislationReference) {
        this.title = legislationReference.getTitle();
        this.description = legislationReference.getDescription();
        this.article = legislationReference.getArticle();
        this.URI = legislationReference.getURI();
        this.jurisdictionLevelCode = legislationReference.getJurisdictionLevelCode();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJurisdictionLevelCode() {
        return jurisdictionLevelCode;
    }

    public void setJurisdictionLevelCode(String jurisdictionLevelCode) {
        this.jurisdictionLevelCode = jurisdictionLevelCode;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LegislationReference that = (LegislationReference) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(jurisdictionLevelCode, that.jurisdictionLevelCode) &&
                Objects.equals(article, that.article) &&
                Objects.equals(URI, that.URI);
    }
}
