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
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.SelfContainedCriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.criteria.resource.CriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.SelfContainedCriteriaTaxonomyResource;
import eu.esens.espdvcd.schema.enums.EDMVersion;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SelfContainedModelBuilder extends RegulatedModelBuilder {

    private static final Logger LOGGER = Logger.getLogger(SelfContainedModelBuilder.class.getName());

    private CriteriaTaxonomyResource taxonomyResource;

    SelfContainedModelBuilder() {
        super(EDMVersion.V2, QualificationApplicationTypeEnum.SELFCONTAINED);
    }

    @Override
    public RegulatedModelBuilder addDefaultESPDCriteriaList() {
        criteriaExtractor = new SelfContainedCriteriaExtractorBuilder().build();
        return this;
    }

    @Override
    protected void applyTaxonomyData(List<SelectableCriterion> criterionList) {

        if (taxonomyResource == null) {
            taxonomyResource = new SelfContainedCriteriaTaxonomyResource();
        }

        LOGGER.log(Level.INFO, "Applying self-contained taxonomy cardinalities and REQUIREMENT_GROUP Types to the imported artefact...");
        criterionList.forEach(sc -> taxonomyResource.applyTaxonomyData(sc));
    }


}
