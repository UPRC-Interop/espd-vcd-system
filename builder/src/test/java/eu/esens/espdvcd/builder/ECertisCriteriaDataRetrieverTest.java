/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaDataRetriever;
import org.junit.Before;
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

    @Test
    public void testGetNationalCriteria() {
        String euCriterionId = "3aaca389-4a7b-406b-a4b9-080845d127e7";
        String countryCoude = "it";
        ECertisCriteriaDataRetriever cdr = new ECertisCriteriaDataRetriever();
        cdr.getNationalCriterionMapping(euCriterionId, countryCoude)
                .stream()
                .forEach(criterionType -> System.out.println(criterionType.getName().getValue()));
        System.out.println("Criterion #: " + cdr.getNationalCriterionMapping("3aaca389-4a7b-406b-a4b9-080845d127e7", "it").size());
    }

}
