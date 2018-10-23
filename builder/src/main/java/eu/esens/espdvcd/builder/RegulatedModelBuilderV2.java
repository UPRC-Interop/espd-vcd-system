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
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.RegulatedCriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.criteria.resource.CriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.RegulatedCriteriaTaxonomyResource;
import eu.esens.espdvcd.schema.EDMVersion;

import java.util.List;

public class RegulatedModelBuilderV2 extends RegulatedModelBuilder {

    private CriteriaTaxonomyResource taxonomyResource;

    @Override
    public RegulatedModelBuilder addDefaultESPDCriteriaList() {
        criteriaExtractor = new RegulatedCriteriaExtractorBuilder(EDMVersion.V2).build();
        return this;
    }

    @Override
    protected void applyTaxonomyCardinalities(List<SelectableCriterion> criterionList) {

        if (taxonomyResource == null) {
            taxonomyResource = new RegulatedCriteriaTaxonomyResource();
        }

        criterionList.forEach(sc -> taxonomyResource.applyCardinalities(sc));
    }

}
