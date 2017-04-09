package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Ignore;

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
  
    @Test
    public void testGetCriterionV2() {
        try {
            extractor.getCriterion("d726bac9-e153-4e75-bfca-c5385587766d");
        } catch (RetrieverException ex) {
            Logger.getLogger(ECertisCriteriaDataRetrieverTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // @Ignore
    @Test
    public void testEuToNationalMapping() throws RetrieverException {

        List<String> results = extractor.getNationalCriterionMapping("d726bac9-e153-4e75-bfca-c5385587766d", "hu")
                .stream()
                .map(c -> c.getID()).collect(Collectors.toList());

        assertThat(results, containsInAnyOrder(
                "fdab2c29-ab6d-4ce1-92c2-5663732dd022", "1f3a2b1f-0058-4d04-9f17-c555afba171e",
                "72781997-4a06-4e34-bd2e-af2ead51e206", "e20ea490-bc98-47ef-810c-551b3036da1b",
                "6221d104-865c-4a17-9df6-a84301b2fb79", "ac610d21-1f2f-41b1-85bb-03ac43a305cb",
                "db3f1a15-a2c2-453e-b69a-9d71f31c88ea", "14e8a872-b8ef-4063-865a-12c38581a03a",
                "f4ddebf8-2f53-40d4-ac4d-b43e3942f4db", "a5aac2ff-222b-4e83-9b63-f238748d4e00",
                "a628af85-e085-4fd7-8e4a-766802f728ce", "3f865345-9a7e-49a3-924a-ca77da6f2512",
                "194b9843-5c68-4b6b-a464-7ad3d356cd10", "ebbfeea4-a0c6-4df3-9941-17a5a7ad235e",
                "ee0ab847-444b-4fca-9437-7fc29adf3963", "3128bc3e-8176-4569-9d80-70aabaf97ac4",
                "63c2f7a0-63af-49c0-8251-80ec7bf43f94"));
    }

    // @Ignore
    @Test
    public void testNationalToNationalMapping() throws RetrieverException {

        List<String> results = extractor.getNationalCriterionMapping("fdab2c29-ab6d-4ce1-92c2-5663732dd022", "be")
                .stream()
                .map(c -> c.getID()).collect(Collectors.toList());

        assertThat(results, containsInAnyOrder(
                "b88e0b29-062c-4eb7-bdd4-f7292d20e53b", "8361b458-1365-4c89-8781-07cec9165be4",
                "db657c18-88e8-4a91-b645-214085f2eba8"));
    }

    // @Ignore
    @Test
    public void testGetCriterion() throws RetrieverException {

        ECertisCriterion c = extractor.getCriterion("3f865345-9a7e-49a3-924a-ca77da6f2512");

        assertEquals("3f865345-9a7e-49a3-924a-ca77da6f2512", c.getID());
    }

    // @Ignore
    @Test
    public void testGetEvidenceGroup() throws RetrieverException {

        List<String> results = extractor.getEvidences("fdab2c29-ab6d-4ce1-92c2-5663732dd022")
                .stream()
                .map(eg -> eg.getID())
                .collect(Collectors.toList());

        assertThat(results, containsInAnyOrder(
                "2ddf545a-b413-4fb8-b118-3c2ce4336bbe", "fb2fa739-02db-4e0d-854e-409d7c763d38"));
    }

}
