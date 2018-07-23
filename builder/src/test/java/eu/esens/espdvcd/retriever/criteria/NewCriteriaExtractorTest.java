package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.XMLDocumentBuilderV2;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.newretriever.CriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.CriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.ECertisResource;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.ESPDArtefactResource;
import eu.esens.espdvcd.schema.SchemaVersion;
import org.junit.Before;
import org.junit.Test;

public class NewCriteriaExtractorTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testTaxonomyResource() throws Exception {

        CriteriaTaxonomyResource taxonomyResource = new CriteriaTaxonomyResource();
        int index = 1;

        for (SelectableCriterion sc : taxonomyResource.getCriterionList()) {
            System.out.printf("%-2d %-36s %b %s %s %s\n", index++, sc.getID(), sc.isSelected(), sc.getName(), sc.getDescription(), sc.getTypeCode());
        }

    }

    @Test
    public void testAllResources() throws Exception {

        CriteriaTaxonomyResource taxonomyResource = new CriteriaTaxonomyResource();
        ECertisResource eCertisResource = new ECertisResource();
        ESPDArtefactResource artefactResource = new ESPDArtefactResource(SchemaVersion.V2);
        int index = 1;

        CriteriaExtractor extractor = new CriteriaExtractorBuilder()
                .addCriteriaResource(taxonomyResource)
                .addCriteriaResource(artefactResource)
                .addCriteriaResource(eCertisResource)
                .addLegislationResource(artefactResource)
                .addRequirementGroupsResource(taxonomyResource)
                .addRequirementGroupsResource(artefactResource)
                .build();

        for (SelectableCriterion sc : extractor.getFullList()) {
            System.out.printf("%-2d %-36s %b %s %s %s\n", index++, sc.getID(), sc.isSelected(), sc.getName(), sc.getDescription(), sc.getTypeCode());
        }

    }

    @Test
    public void testDefaultCriteriaExtractorBuilderBuild() throws Exception {

        int index = 1;

        CriteriaExtractor extractor = new CriteriaExtractorBuilder().build();

        for (SelectableCriterion sc : extractor.getFullList()) {
            System.out.printf("%-2d %-36s %b\n%s\n%s\n%s\n\n", index++, sc.getID(), sc.isSelected(), sc.getName(), sc.getTypeCode(), sc.getDescription());
        }

    }

    @Test
    public void testNewCriteriaExtractorBuilder() throws Exception {

        CriteriaTaxonomyResource taxonomyResource = new CriteriaTaxonomyResource();
        ECertisResource eCertisResource = new ECertisResource();
        ESPDArtefactResource artefactResource = new ESPDArtefactResource(SchemaVersion.V2);

        CriteriaExtractor extractor = new CriteriaExtractorBuilder()
                .addCriteriaResource(taxonomyResource)
                .addCriteriaResource(eCertisResource)
                .addLegislationResource(artefactResource)
                .addLegislationResource(eCertisResource)
                .addRequirementGroupsResource(taxonomyResource)
                .addRequirementGroupsResource(artefactResource)
                .build();

        ESPDRequest espdRequest = BuilderFactory.withEDMVersion2()
                .getRegulatedModelBuilder()
                .createESPDRequest();

        espdRequest.setCriterionList(extractor.getFullList());

        XMLDocumentBuilderV2 xmlDocumentBuilderV2 = BuilderFactory.withEDMVersion2()
                .getDocumentBuilderFor(espdRequest);
        System.out.println(xmlDocumentBuilderV2.getAsString());
    }

}
