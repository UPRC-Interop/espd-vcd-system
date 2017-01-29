package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaDataRetriever;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author konstantinos
 */
public class ECertisCriteriaDataRetrieverTest {

    public ECertisCriteriaDataRetrieverTest() {
    }

    @Before
    public void setUp() {
    }

    @Ignore
    @Test
    public void testGetNationalCriteria() {
        String euCriterionId = "3aaca389-4a7b-406b-a4b9-080845d127e7";
        String countryCoude = "it";

        ECertisCriteriaDataRetriever cdr = new ECertisCriteriaDataRetriever();
        cdr.getNationalCriterionMapping(euCriterionId, countryCoude)
                .stream()
                .forEach(parentCriterion -> /*System.out.println(criterionType.getName().getValue()*/ {
                            parentCriterion.getSubCriterion().forEach((ct) -> {
                                System.out.println(ct.getName().getValue());
                            });
                        }
                );
        System.out.println("Criterion #: " + cdr.getNationalCriterionMapping("3aaca389-4a7b-406b-a4b9-080845d127e7", "it").size());
    }

    @Ignore
    @Test
    public void testGetCriterion() {
        String criterionId = "65da1473-2667-4d79-8e3b-01c6c4f39db3";

        ECertisCriteriaDataRetriever cdr = new ECertisCriteriaDataRetriever();
        CriterionType ct = cdr.getCriterion(criterionId);
        System.out.println(ct.getName().getValue());
        System.out.println(ct.getTypeCode().getValue());
        System.out.println(ct.getSubCriterion().size());

        System.out.println("");

        System.out.println("is criterion with id " + criterionId + " an eu criterion? :" + cdr.isEuCriterionIdExists(criterionId));
        System.out.println("is criterion with id " + criterionId + " a criterion? : " + (cdr.getCriterion(criterionId) != null));
    }

    @Test
    public void testGetEvidences() {
        // eu criterion
        String euCriterionId = "d726bac9-e153-4e75-bfca-c5385587766d";
        
        // national criterion
        String nationalCriterionId = "65da1473-2667-4d79-8e3b-01c6c4f39db3";
        
        ECertisCriteriaDataRetriever cdr = new ECertisCriteriaDataRetriever();
        cdr.getEvidences(euCriterionId);
        // cdr.getEvidences(nationalCriterionId).forEach(rg -> System.out.println(rg.getName().getValue()));
    }

}
