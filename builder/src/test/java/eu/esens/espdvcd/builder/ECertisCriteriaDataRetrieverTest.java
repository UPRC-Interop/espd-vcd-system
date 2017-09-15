package eu.esens.espdvcd.builder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    
    @Ignore
    @Test
    public void testGetCriterionV2() throws JsonProcessingException {
        try {
            ECertisSelectableCriterion theCriterion = extractor.getCriterion("d726bac9-e153-4e75-bfca-c5385587766d");
            
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
    
//    @Ignore
    @Test
    public void testEuToNationalMapping() throws RetrieverException {
        
        String ID = "d726bac9-e153-4e75-bfca-c5385587766d";
        String code = "hu";
                
        List<String> results = extractor.getNationalCriterionMapping(ID, code)
                .stream()
                .map(c -> c.getID()).collect(Collectors.toList());
        
        System.out.println("- EU to National (" + code + ") Criterion Mapping");
        System.out.printf("%s --> %s%n", ID, results);
    }
    
//    @Ignore
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
    
//    @Ignore
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
