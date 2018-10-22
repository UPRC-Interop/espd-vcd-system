/**
 * Copyright 2016-2018 University of Piraeus Research Center
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
package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.retriever.criteria.resource.RegulatedCriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.ECertisResource;
import eu.esens.espdvcd.retriever.criteria.resource.ESPDArtefactResource;
import eu.esens.espdvcd.retriever.criteria.resource.SelectableCriterionPrinter;
import eu.esens.espdvcd.schema.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CriteriaExtractorBuilderTest {

    private CriteriaExtractorBuilder b1;
    private CriteriaExtractorBuilder b2;

    @Before
    public void setUp() {
        b1 = new CriteriaExtractorBuilder(EDMVersion.V1);
        b2 = new CriteriaExtractorBuilder(EDMVersion.V2);
    }

    @Test
    public void testBuilderWithAllResources() throws Exception {

        RegulatedCriteriaTaxonomyResource r1 = new RegulatedCriteriaTaxonomyResource();
        ECertisResource r2 = new ECertisResource();
        ESPDArtefactResource r3 = new ESPDArtefactResource(EDMVersion.V2);

        CriteriaExtractor e = b2
                // Criteria resources
                .addCriteriaResource(r1)
                .addCriteriaResource(r2)
                .addCriteriaResource(r3)
                // Legislation resources
                .addLegislationResource(r3)
                // RequirementGroup resources
                .addRequirementsResource(r1)
                .addRequirementsResource(r3)
                .build();

        SelectableCriterionPrinter.print(e.getFullList());
    }


    @Test
    public void testBuilderWithESPDArtefactResource() throws Exception {

        ESPDArtefactResource r = new ESPDArtefactResource(EDMVersion.V1);

        CriteriaExtractor e = b2
                // Criteria resources
                .addCriteriaResource(r)
                // Legislation resources
                .addLegislationResource(r)
                // RequirementGroup resources
                .addRequirementsResource(r)
                .build();

        SelectableCriterionPrinter.print(e.getFullList());
    }

    @Test
    public void testDefaultCriteriaExtractorBuilderForEDMV1() throws Exception {

        CriteriaExtractor e1 = b1.build();
        Assert.assertNotNull(e1);
        Assert.assertFalse(e1.getFullList().isEmpty());
        SelectableCriterionPrinter.print(e1.getFullList());
    }

    @Test
    public void testDefaultCriteriaExtractorBuilderForEDMV2() throws Exception {

        CriteriaExtractor e2 = b2.build();
        Assert.assertNotNull(e2);
        Assert.assertFalse(e2.getFullList().isEmpty());
        SelectableCriterionPrinter.print(e2.getFullList());
    }

}
