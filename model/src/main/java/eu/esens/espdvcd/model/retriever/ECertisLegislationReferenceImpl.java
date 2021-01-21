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
package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.util.CustomTextValueDeserializer;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
            "title",
            "description",
            "jurisdictionLevelCode",
            "article",
            "URI"
        })
@JsonIgnoreProperties(ignoreUnknown = true)
public class ECertisLegislationReferenceImpl extends LegislationReference implements ECertisLegislationReference {

    public ECertisLegislationReferenceImpl() {
        super(null, null, null, null, null);
    }
    
    public ECertisLegislationReferenceImpl(String title, String description, String jurisdictionLevelCode, String article, String URI) {
        super(title, description, jurisdictionLevelCode, article, URI);
    }
    
    @Override
    @JsonProperty("Title")
    @JsonAlias({"title"})
    public String getTitle() {
        return title;
    }

    @Override
    @JsonDeserialize(using = CustomTextValueDeserializer.class)
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    @JsonProperty("Description")
    @JsonAlias({"description"})
    public String getDescription() {
        return description;
    }

    @Override
    @JsonDeserialize(using = CustomTextValueDeserializer.class)
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    @JsonProperty("JurisdictionLevelCode")
    @JsonAlias({"jurisdictionLevelCode"})
    public String getJurisdictionLevelCode() {
        return jurisdictionLevelCode;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setJurisdictionLevelCode(String JurisdictionLevelCode) {
        this.jurisdictionLevelCode = JurisdictionLevelCode;
    }

    @Override
    @JsonProperty("Article")
    @JsonAlias({"article"})
    public String getArticle() {
        return article;
    }

    @Override
    @JsonDeserialize(using = CustomTextValueDeserializer.class)
    public void setArticle(String article) {
        this.article = article;
    }

    @Override
    @JsonProperty("URI")
    @JsonAlias({"uri"})
    public String getURI() {
        return URI;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setURI(String URI) {
        this.URI = URI;
    }

}
