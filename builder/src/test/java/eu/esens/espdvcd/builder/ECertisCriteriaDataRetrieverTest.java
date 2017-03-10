package eu.esens.espdvcd.builder;

import static eu.esens.espdvcd.builder.ECertisCriteriaExtractorTest.maxDepth;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementGroupType;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author konstantinos
 */
public class ECertisCriteriaDataRetrieverTest {

    private ECertisCriteriaExtractor extractor;

    private final String[] criterionIds = {
        // eu
        "d726bac9-e153-4e75-bfca-c5385587766d", // OK
        // national - italian origin
        "fdab2c29-ab6d-4ce1-92c2-5663732dd022", // OK
        // national - hungarian origin
        "ac610d21-1f2f-41b1-85bb-03ac43a305cb", // OK
        // eu
        "bdf0601d-2480-4250-b870-658d0ee95be6", "b61bbeb7-690e-4a40-bc68-d6d4ecfaa3d4"}; // OK OK

    public ECertisCriteriaDataRetrieverTest() {
    }

    @Before
    public void setUp() {
        extractor = new ECertisCriteriaExtractor();
    }

    @Test
    public void testGetCriterionV2() {

        Arrays.asList(criterionIds).forEach(ID -> {
            try {
                extractor.getCriterionV2(ID);
            } catch (RetrieverException ex) {
                Logger.getLogger(ECertisCriteriaDataRetrieverTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

//    @Ignore
//    @Test
//    public void testGetAllNationalEntities() {
//        try {
//            extractor.getAllNationalEntities().forEach(ne -> System.out.println(ne));
//        } catch (RetrieverException ex) {
//            System.err.println(ex);
//        }
//    }
    @Ignore
    @Test
    public void testGetAllEuropeanCriteriaIds() {
        try {
            List<String> ids = extractor.getAllEuropeanCriteriaIds();
            ids.forEach(id -> {
                try {
                    System.out.println(extractor.getCriterion(id).getID().getValue());
                } catch (RetrieverException ex) {
                    System.err.println(ex);
                }
            });
            System.out.println("Number of eu criteria is : " + ids.size());
        } catch (RetrieverException ex) {
            System.err.println(ex);
        }
    }

    @Ignore
    @Test
    public void testGetNationalCriterionMappings() {
        ECertisCriteriaExtractor extractor = new ECertisCriteriaExtractor();
        try {
            // extractor.getAllNationalEntities().forEach(ne -> testGetNationalCriterionMapping(ne.getId()));
            List<CriterionType> ct = extractor.getNationalCriterionMapping("d726bac9-e153-4e75-bfca-c5385587766d", "hu");

//            ct.stream()
//                    .forEach(c -> c.getRequirementGroup()
//                            .forEach(rg -> traverseRequirementGroup(ModelFactory.ESPD_REQUEST
//                                    .extractRequirementGroup(rg), 1))
//                    );
            CriterionType c = ct.get(0);
            c.getRequirementGroup().forEach(rg -> {
                System.out.print(rg.getID().getValue());
                System.out.print("\t" + rg.getRequirement().isEmpty() + "\n");
                rg.getRequirement().forEach(rt -> System.out.println(rt.getID()));
            });

        } catch (RetrieverException ex) {
            System.err.println(ex);
        }
    }

    @Ignore
    @Test
    public void testGetNationalCriterionMapping() {

        String countryCode = "it";
        ECertisCriteriaExtractor extractor = new ECertisCriteriaExtractor();
        String countryName = Codelists.CountryIdentification.getValueForId(countryCode.toUpperCase());

//        String[] ids = {"a34b70d6-c43d-4726-9a88-8e2b438424bf",
//                        "9fc69031-5b4d-440a-828c-2167e6f22328"};
        try {

            int total = 0;

            System.out.printf("Mapping European Criteria to %s National Criteria%n%n", countryName);
            System.out.printf("%-50s%-50s%n%n", "European Criterion", "National Criterion");

            for (String id : /*extractor.getAllEuropeanCriteriaIds()*/ criterionIds) {
                List<CriterionType> mappedCriterions = extractor.getNationalCriterionMapping(id, countryCode);

                System.out.printf("%-50s", id);
                boolean first = true;

                for (CriterionType c : mappedCriterions) {

                    if (first) {
                        System.out.printf("%-50s%n", c.getID().getValue());
                        first = false;
                    } else {
                        System.out.printf("%-50s%-50s%n", " ", c.getID().getValue());
                    }

                    total++;
                }

                System.out.printf("%n%n");
            }

            System.out.println("Total Mappings : " + total);

        } catch (RetrieverException ex) {
            System.err.println(ex);
        }

    }

    @Ignore
    @Test
    public void testGetCriterion() {
        ECertisCriteriaExtractor cdr = new ECertisCriteriaExtractor();
        try {
            for (String ctId : criterionIds) {
                displayCriterion(cdr.getCriterion(ctId));
                System.out.println();
            }

        } catch (RetrieverException e) {
            System.err.println(e);
        }
    }

    private void displayCriterion(CriterionType ct) {
        System.out.printf("%-25s:%s%n", "Id", ct.getID().getValue());
        System.out.printf("%-25s:%s%n", "Name", ct.getName().getValue());
        System.out.printf("%-25s:%s%n", "TypeCode", ct.getTypeCode().getValue());
        if (!ct.getLegislationReference().isEmpty()) {
            System.out.printf("%-25s:%s%n", "JurisdictionLevelCode", ct.getLegislationReference().get(0)
                    .getJurisdictionLevelCode().getValue());
        }
        System.out.printf("%-25s:%s%n", "SubCriterions #", ct.getSubCriterion().size());
    }

    @Ignore
    @Test
    public void testGetEvidences() {
        ECertisCriteriaExtractor cdr = new ECertisCriteriaExtractor();

        try {
            for (String ctId : criterionIds) {
                System.out.print("Displaying Evidences for Criterion with Id <<" + ctId + ">> :");
                List<RequirementGroupType> evidences = cdr.getEvidences(ctId);
                evidences.forEach(rgt -> displayRequirementGroupType(rgt));
                System.out.println();
            }

        } catch (RetrieverException ex) {
            System.err.println(ex);
        }
    }

    private void displayRequirementGroupType(RequirementGroupType rgt) {
        System.out.print(rgt.getID().getValue());
        System.out.print(rgt.getTypeCode().getValue());
    }

    private void traverseRequirementGroup(RequirementGroup rg, int depth) {

        if (depth > maxDepth) {
            maxDepth = depth;
        }

        String tabs = "";
        for (int i = 0; i < depth; i++) {
            tabs += "\t";
        }
        final String finalTabs = tabs;
        System.out.println(tabs + "RequirementGroup: " + rg.getID());
        System.out.println(tabs + "Requirements: ");
        rg.getRequirements().forEach(r -> {
            System.out.println(finalTabs + "\tReq ID: " + r.getID() + " Req Type:" + r.getResponseDataType() + " Req Desc:" + r.getDescription());
        });
        final int innerDepth = depth + 1;
        rg.getRequirementGroups().forEach(rg1 -> traverseRequirementGroup(rg1, innerDepth));
    }

}
