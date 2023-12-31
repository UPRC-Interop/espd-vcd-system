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
import eu.esens.espdvcd.retriever.criteria.RegulatedCriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.criteria.resource.CriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.RegulatedCriteriaTaxonomyResource;
import eu.esens.espdvcd.schema.enums.EDMVersion;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegulatedModelBuilderV1 extends RegulatedModelBuilder {

    private static final Logger LOGGER = Logger.getLogger(RegulatedModelBuilderV1.class.getName());

    /**
     * Because there is no taxonomy v1 resource,
     * taxonomy v2 regulated will be used in order
     * to add cardinalities to v1 Criteria.
     */
    private CriteriaTaxonomyResource taxonomyResource;

    RegulatedModelBuilderV1() {
        super(EDMVersion.V1, QualificationApplicationTypeEnum.REGULATED);
    }

    @Override
    public RegulatedModelBuilder addDefaultESPDCriteriaList() {
        criteriaExtractor = new RegulatedCriteriaExtractorBuilder(EDMVersion.V1).build();
        return this;
    }

    @Override
    protected void applyTaxonomyData(List<SelectableCriterion> criterionList) {

//        LOGGER.log(Level.WARNING, "V1 Taxonomy currently does not support cardinalities for Requirements/RequirementGroups");

        if (taxonomyResource == null) {
            taxonomyResource = new RegulatedCriteriaTaxonomyResource();
        }

        LOGGER.log(Level.WARNING, "V1 Taxonomy currently does not support cardinalities for"
                + " Requirements/RequirementGroups and Codelists data."
                + " It is also not currently readable from the Retriever."
                + " Nevertheless, regulated v2 taxonomy will be used instead...");

        criterionList.forEach(sc -> taxonomyResource.applyTaxonomyData(sc));
    }

}
