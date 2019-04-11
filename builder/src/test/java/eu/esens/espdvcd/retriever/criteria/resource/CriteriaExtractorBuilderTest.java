/**
 * Copyright 2016-2019 University of Piraeus Research Center
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

import eu.esens.espdvcd.codelist.enums.internal.ContractingOperatorEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.criteria.RegulatedCriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.criteria.SelfContainedCriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.criteria.resource.RegulatedCriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.ECertisResource;
import eu.esens.espdvcd.retriever.criteria.resource.ESPDArtefactResource;
import eu.esens.espdvcd.retriever.criteria.resource.SelectableCriterionPrinter;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CriteriaExtractorBuilderTest {

    private CriteriaExtractorBuilder regulatedBuilder1;
    private CriteriaExtractorBuilder regulatedBuilder2;
    private CriteriaExtractorBuilder selfContainedBuilder;

    @Before
    public void setUp() {
        regulatedBuilder1 = new RegulatedCriteriaExtractorBuilder(EDMVersion.V1);
        regulatedBuilder2 = new RegulatedCriteriaExtractorBuilder(EDMVersion.V2);
        selfContainedBuilder = new SelfContainedCriteriaExtractorBuilder();
    }

    @Test
    public void testBuilderWithAllResources() throws Exception {

        RegulatedCriteriaTaxonomyResource r1 = new RegulatedCriteriaTaxonomyResource();
        ECertisResource r2 = new ECertisResource();
        ESPDArtefactResource r3 = new ESPDArtefactResource(EDMVersion.V2);

        CriteriaExtractor e = regulatedBuilder2
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

        CriteriaExtractor e = regulatedBuilder2
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
    public void testBuilderWithOperatorParam() throws Exception {

        CriteriaExtractor e = new SelfContainedCriteriaExtractorBuilder()
                .withContractingOperator(ContractingOperatorEnum.CONTRACTING_AUTHORITY)
                .build();

        String id = "005eb9ed-1347-4ca3-bb29-9bc0db64e1ab";
        SelectableCriterion sc = e.getFullList().stream()
                .filter(c -> id.equals(c.getID())).findFirst()
                .orElse(null);

        Assert.assertTrue(sc.isCompulsory());
    }

    @Test
    public void testDefaultRegulatedV1CriteriaExtractorBuilder() throws Exception {

        CriteriaExtractor e = regulatedBuilder1.build();
        Assert.assertNotNull(e);
        Assert.assertFalse(e.getFullList().isEmpty());
        SelectableCriterionPrinter.print(e.getFullList());
    }

    @Test
    public void testDefaultRegulatedV2CriteriaExtractorBuilder() throws Exception {

        CriteriaExtractor e = regulatedBuilder2.build();
        Assert.assertNotNull(e);
        Assert.assertFalse(e.getFullList().isEmpty());
        SelectableCriterionPrinter.print(e.getFullList());
    }

    @Test
    public void testDefaultSelfContainedCriteriaExtractorBuilder() throws Exception {

        CriteriaExtractor e = selfContainedBuilder.build();
        Assert.assertNotNull(e);
        Assert.assertFalse(e.getFullList().isEmpty());
        SelectableCriterionPrinter.print(e.getFullList());
    }

}
