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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CriteriaTaxonomyResourceTest {

    private RegulatedCriteriaTaxonomyResource regulated;
    private SelfContainedCriteriaTaxonomyResource selfContained;

    @Before
    public void setUp() {
        regulated = new RegulatedCriteriaTaxonomyResource();
        Assert.assertNotNull(regulated);
        selfContained = new SelfContainedCriteriaTaxonomyResource();
        Assert.assertNotNull(selfContained);
    }

    @Test
    public void testGetFullList() {
        // SelectableCriterionPrinter.print(regulated.getCriterionList());
        SelectableCriterionPrinter.print(selfContained.getCriterionList());
        // Assert.assertTrue(regulated.getCriterionList().size() == 64);
        // Assert.assertTrue(selfContained.getCriterionList().size() == 64);
    }

}
