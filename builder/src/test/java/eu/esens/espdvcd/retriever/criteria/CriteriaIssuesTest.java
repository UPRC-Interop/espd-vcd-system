/**
 * Copyright 2016-2020 University of Piraeus Research Center
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
package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.retriever.criteria.resource.CriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.RegulatedCriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.utils.SetDifferencesUtils;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

public class CriteriaIssuesTest {

    @Before
    public void setUp() {

    }

    /**
     * Compare V1 regulated artefact criteria with v2 regulated taxonomy criteria
     *
     * @throws Exception
     */
    @Ignore
    @Test
    public void testCompareRegulatedCriteria() throws Exception {

        CriteriaExtractor criteriaExtractorV1 = new RegulatedCriteriaExtractorBuilder(EDMVersion.V1).build();

        CriteriaTaxonomyResource taxonomyResourceV2 = new RegulatedCriteriaTaxonomyResource();

        Set<String> typeCodeV1Set = criteriaExtractorV1.getFullList().stream()
                .map(Criterion::getTypeCode)
                .collect(Collectors.toSet());

        Set<String> typeCodeV2Set = taxonomyResourceV2.getCriterionList().stream()
                .map(Criterion::getTypeCode)
                .collect(Collectors.toSet());

        boolean isSame = typeCodeV1Set.containsAll(typeCodeV2Set)
                && typeCodeV2Set.containsAll(typeCodeV1Set);

        if (!isSame) {
            System.out.println("TypeCodes that exist in typeCodeV1Set and does not exist in typeCodeV2Set");
            SetDifferencesUtils.printDifferences(typeCodeV1Set, typeCodeV2Set);
            System.out.println("TypeCodes that exist in typeCodeV2Set and does not exist in typeCodeV1Set");
            SetDifferencesUtils.printDifferences(typeCodeV2Set, typeCodeV1Set);
        }

    }

}
