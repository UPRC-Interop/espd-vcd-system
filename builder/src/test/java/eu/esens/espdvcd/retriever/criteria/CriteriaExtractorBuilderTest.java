package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.retriever.criteria.resource.CriteriaTaxonomyResource;
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

        CriteriaTaxonomyResource r1 = new CriteriaTaxonomyResource();
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
