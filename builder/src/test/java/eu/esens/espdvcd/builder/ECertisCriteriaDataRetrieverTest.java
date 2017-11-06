package eu.esens.espdvcd.builder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.retriever.ECertisEvidenceGroup;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Ignore;
import eu.esens.espdvcd.model.retriever.ECertisSelectableCriterion;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author konstantinos
 */
public class ECertisCriteriaDataRetrieverTest {

    private ECertisCriteriaExtractor extractor;
    
    @Before
    public void setUp() {
        extractor = new ECertisCriteriaExtractor();
    }

    /**
     * JLC = jurisdiction level code
     * LR = legislation reference
     *
     * @throws JsonProcessingException
     * @throws RetrieverException
     */
    @Test
    public void testCriteriaForNoLegislationReferenceOrNoJurisdictionLevelCode()
            throws JsonProcessingException, RetrieverException {

        // init counters
        int noLegislationReference = 0;
        int noJurisdictionLevelCode = 0;

        // set lang to someting
        extractor.setLang("el");

        // Get all european criteria
        List<SelectableCriterion> europeanCriteriaList = extractor.getFullList();

        // run through european criteria
        for (SelectableCriterion c : europeanCriteriaList) {

            try {

                // extract european criterion id
                String id = c.getID();

                // get that european criterion from eCertis
                ECertisSelectableCriterion currentEUCriterion = extractor.getCriterion(id);

                // run thought subcriterion list of current european criterion
                for (ECertisSelectableCriterion subc : currentEUCriterion.getSubCriterions()) {

                    // make sure that there is a legislation reference
                    if (subc.getLegislationReference() != null) {

                        // print criterion if it hasn't a JLC
                        if (subc.getLegislationReference().getJurisdictionLevelCode() == null) {

                            System.out.printf("Parent European Criterion's id: %s\n", id);
                            System.out.printf("\tSubcriteria without jurisdiction level code\n");
                            System.out.printf("\t\tCriterion id: %s\n", subc.getID());

                            // no jurisdiction level code ++
                            noJurisdictionLevelCode++;

                        }

                    } else {

                        // print criterion if it hasn't a LR
                        System.out.printf("Parent European Criterion's id: %s\n", id);
                        System.out.printf("\tSubcriteria without legislation reference\n");
                        System.out.printf("\t\tCriterion id: %s\n", subc.getID());

                        // no legislation reference counter ++
                        noLegislationReference++;
                        // no legislation reference means no jurisdiction level code so increase the relative counter
                        noJurisdictionLevelCode++;
                    }

                }

            } catch (RetrieverException ex) {
                Logger.getLogger(ECertisCriteriaDataRetrieverTest.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        // Print counters result
        System.out.printf("\nNumber of Criteria with no Legislation Reference: %d\n", noLegislationReference);
        System.out.printf("Number of Criteria with no Jurisdiction Level Code: %d", noJurisdictionLevelCode);
    }

    @Ignore
    @Test
    public void testGetCriterionV2() throws JsonProcessingException {
        try {
            extractor.setLang("el");

            ECertisSelectableCriterion theCriterion = extractor.getCriterion("7c351fc0-1fd0-4bad-bfd8-1717a9dcf9d1");

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

            // Print JSON String
            String prettyCt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(theCriterion);
            System.out.println(prettyCt);

        } catch (RetrieverException ex) {
            Logger.getLogger(ECertisCriteriaDataRetrieverTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Ignore
    @Test
    public void testEuToNationalMapping() throws RetrieverException {

        String ID = "14df34e8-15a9-411c-8c05-8c051693e277";
        String code = "gr";

        List<String> results = extractor.getNationalCriterionMapping(ID, code)
                .stream()
                .map(c -> c.getID()).collect(Collectors.toList());

        System.out.println("- EU to National (" + code + ") Criterion Mapping");
        System.out.printf("%s --> %s%n", ID, results);
    }

    @Ignore
    @Test
    public void testNationalToNationalMapping() throws RetrieverException {

        String ID = "fdab2c29-ab6d-4ce1-92c2-5663732dd022";
        String code = "be";

        List<String> results = extractor.getNationalCriterionMapping(ID, code)
                .stream()
                .map(c -> c.getID()).collect(Collectors.toList());

        System.out.println("- Natinal to National (" + code + ") Criterion Mapping");
        System.out.printf("%s --> %s%n", ID, results);
    }

    @Ignore
    @Test
    public void testGetCriterion() throws RetrieverException {

        ECertisSelectableCriterion c = extractor.getCriterion("3f865345-9a7e-49a3-924a-ca77da6f2512");

        assertEquals("3f865345-9a7e-49a3-924a-ca77da6f2512", c.getID());
    }

    @Ignore
    @Test
    public void testGetEvidenceGroup() throws RetrieverException {

        final String ID = "fdab2c29-ab6d-4ce1-92c2-5663732dd022";

        extractor.getEvidences(ID).forEach(eg -> traverseEvidenceGroup(eg));
    }

    private void traverseEvidenceGroup(ECertisEvidenceGroup eg) {
        System.out.println("Not implemented");
    }

}
