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
package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.RequirementGroup;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegulatedCriteriaTaxonomyResource extends CriteriaTaxonomyResource {

    private static final Logger LOGGER = Logger.getLogger(RegulatedCriteriaTaxonomyResource.class.getName());

    private static final String RESOURCE_PATH = "/templates/v2_regulated/ESPD-CriteriaTaxonomy-REGULATED-V2.0.2.xlsx";

    public RegulatedCriteriaTaxonomyResource() {
        super(RESOURCE_PATH);
    }

    @Override
    public void applyTaxonomyData(SelectableCriterion sc) {
        // find root RequirementGroup/s of that criterion from taxonomy
        final List<RequirementGroup> rgListFromTaxonomy = rgMap.get(sc.getID());

        if (rgListFromTaxonomy == null) {
            LOGGER.log(Level.SEVERE, "SC with ID " + sc.getID() + " cannot be found in rgMap");
            return;
        }

        // apply cardinalities to all root RequirementGroup/s
        sc.getRequirementGroups().forEach(rg -> applyTaxonomyData(
                rgListFromTaxonomy.stream()
                        .filter(rgFromTaxonomy -> rg.getID().equals(rgFromTaxonomy.getID()))
                        .findFirst().orElse(null) // from
                , rg));                                 //  to
    }

    private void applyTaxonomyData(RequirementGroup from, RequirementGroup to) {

        if (from != null && to != null) {

            // do the same for sub-RequirementGroup/s
            to.getRequirementGroups().forEach(rg -> applyTaxonomyData(
                    from.getRequirementGroups().stream()
                            .filter(rgFromTaxonomy -> rg.getID().equals(rgFromTaxonomy.getID()))
                            .findFirst().orElse(null) // from
                    , rg));                                 //  to

            // Requirements in Regulated are always of Cardinality 1 (default cardinality)

            // cardinalities
            to.setMultiple(from.isMultiple());
            to.setMandatory(from.isMandatory());
            // requirement group type
            to.setType(from.getType());
        }
    }

}
