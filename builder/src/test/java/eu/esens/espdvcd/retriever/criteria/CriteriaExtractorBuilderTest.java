package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.retriever.criteria.resource.CriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.ECertisResource;
import eu.esens.espdvcd.retriever.criteria.resource.ESPDArtefactResource;
import eu.esens.espdvcd.retriever.criteria.resource.SelectableCriterionPrinter;
import eu.esens.espdvcd.schema.SchemaVersion;
import org.junit.Before;
import org.junit.Test;

public class CriteriaExtractorBuilderTest {

    private CriteriaExtractorBuilder b;

    @Before
    public void setUp() {
        b = new CriteriaExtractorBuilder();
    }

    @Test
    public void testBuilderWithAllResources() throws Exception {

        CriteriaTaxonomyResource r1 = new CriteriaTaxonomyResource();
        ECertisResource r2 = new ECertisResource();
        ESPDArtefactResource r3 = new ESPDArtefactResource(SchemaVersion.V2);

        CriteriaExtractor e = b
                // Criteria resources
                .addCriteriaResource(r1)
                .addCriteriaResource(r2)
                .addCriteriaResource(r3)
                // Legislation resources
                .addLegislationResource(r3)
                // RequirementGroup resources
                .addRequirementGroupsResource(r1)
                .addRequirementGroupsResource(r3)
                .build();

        SelectableCriterionPrinter.print(e.getFullList());
    }


    @Test
    public void testBuilderWithESPDArtefactResource() throws Exception {

        ESPDArtefactResource r = new ESPDArtefactResource(SchemaVersion.V1);

        CriteriaExtractor e = b
                // Criteria resources
                .addCriteriaResource(r)
                // Legislation resources
                .addLegislationResource(r)
                // RequirementGroup resources
                .addRequirementGroupsResource(r)
                .build();

        SelectableCriterionPrinter.print(e.getFullList());
    }

    @Test
    public void testDefaultCriteriaExtractorBuilderBasicInfo() throws Exception {
        CriteriaExtractor e = b.build();

        SelectableCriterionPrinter.print(e.getFullList());
    }

}
