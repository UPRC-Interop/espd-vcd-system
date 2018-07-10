package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.XMLDocumentBuilderV2;
import eu.esens.espdvcd.model.ESPDResponse;
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
    public void testNewCriteriaExtractorBuilder() throws Exception {

//        CriteriaTaxonomyResource taxonomyResource = new CriteriaTaxonomyResource();
//        ECertisResource eCertisResource = new ECertisResource();
//        ESPDArtefactResource artefactResource = new ESPDArtefactResource(SchemaVersion.V2);

        CriteriaExtractor extractor = new CriteriaExtractorBuilder()
//                .addCriteriaResource(taxonomyResource)
//                .addLegislationResource(eCertisResource)
//                .addRequirementGroupsResource(taxonomyResource)
                .build();

        ESPDResponse espdResponse = BuilderFactory.withEDMVersion2()
                .getRegulatedModelBuilder()
                .createESPDResponse();

        espdResponse.setCriterionList(extractor.getFullList());

        XMLDocumentBuilderV2 xmlDocumentBuilderV2 = BuilderFactory.withEDMVersion2()
                .getDocumentBuilderFor(espdResponse);
        System.out.println(xmlDocumentBuilderV2.getAsString());
    }

}
