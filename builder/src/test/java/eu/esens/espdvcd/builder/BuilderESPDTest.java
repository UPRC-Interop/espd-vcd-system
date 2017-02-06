
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class BuilderESPDTest {
    
    InputStream isReq;
    InputStream isRes;
    
    public BuilderESPDTest() {
    }
    
    @Before
    public void setUp() {
        isReq = BuilderESPDTest.class.getResourceAsStream("/espd-request.xml");
        Assert.assertNotNull(isReq);
        
        isRes = BuilderESPDTest.class.getResourceAsStream("/espd-response.xml");
        Assert.assertNotNull(isRes);
    }

    @Ignore
    @Test
    public void createESPDRequestFromFile() throws Exception {
 
        // Here we load an ESPD Request generated by the EU Service
        ESPDRequest espd = new ModelBuilder().importFrom(isReq).createESPDRequest();
        Assert.assertNotNull(espd);

        // We create an XML from our simple model 
        String XML = new XMLDocumentBuilder(espd).getAsString();
        Assert.assertNotNull(XML);
    //    System.out.println(XML);
    }

    @Ignore    
    @Test
    public void createESPDResponseFromFile() throws Exception {
        
      
        // Here we load an ESPD Request generated by the EU Service
        ESPDResponse espd = new ModelBuilder().importFrom(isRes).createESPDResponse();
        Assert.assertNotNull(espd);

        // We create an XML from our simple model 
        String XML = new XMLDocumentBuilder(espd).getAsString();
        Assert.assertNotNull(XML);
//        System.out.println(XML);
    }
    
    @Ignore    
    @Test
    public void createESPDResponseFromESPDRequestFile() throws Exception {
        // Here we load an ESPD Request generated by the EU Service
        ESPDResponse espd = new ModelBuilder().importFrom(isReq).createESPDResponse();
        Assert.assertNotNull(espd);

        // We create an XML from our simple model 
        String XML = new XMLDocumentBuilder(espd).getAsString();
        Assert.assertNotNull(XML);
//        System.out.println(XML);
    }
    
    @Ignore
    @Test
    public void deselectExclusionCriterion() throws Exception {

        // Here we load an ESPD Request generated by the EU Service
      
        ESPDRequest originalEspd = new ModelBuilder().importFrom(isReq).createESPDRequest();
        Assert.assertNotNull(originalEspd);

        // Deselect the first exclusion criterion
        originalEspd.getExclusionCriteriaList().get(0).setSelected(false);
        
        Assert.assertEquals(62,originalEspd.getFullCriterionList().size());

        // Count the number of selected exclusion criteria and the total number of exclusion criteria in the original espd request
        int originalEspdExclusionCriteriaCount = originalEspd.getExclusionCriteriaList().size();
        long originalCriteriaSelectedCount = originalEspd.getFullCriterionList().stream()
                .filter(c -> c.isSelected())
                .count();
        
        long originalCriteriaNotSelectedCount = originalEspd.getFullCriterionList().stream()
                .filter(c -> !c.isSelected())
                .count();
        
        Assert.assertEquals(62, originalCriteriaNotSelectedCount + originalCriteriaSelectedCount);
        Assert.assertEquals(1, originalCriteriaNotSelectedCount);
        Assert.assertEquals(61, originalCriteriaSelectedCount);
       
        // Export the ESPD Request to Xml string
        
        ESPDRequest importedEspd;
            importedEspd = new ModelBuilder()
                    .importFrom(new XMLDocumentBuilder(originalEspd).getAsInputStream())
                    .addDefaultESPDCriteriaList()
                    .createESPDRequest();
        
            Assert.assertNotNull(importedEspd);

        // Count the number of selected exclusion criteria and the total number of exclusion criteria in the imported espd request
        long importedEspdExclusionCriteriaCount = importedEspd.getExclusionCriteriaList().size();
        long importedEspdExclusionCriteriaSelectedCount = importedEspd
                .getExclusionCriteriaList()
                .stream()
                .filter(c -> c.isSelected())
                .count();

        // Compare the number of selected exclusion criteria in espd and importedEspd
        Assert.assertEquals(originalEspdExclusionCriteriaCount, importedEspdExclusionCriteriaCount);
        Assert.assertNotEquals(originalCriteriaSelectedCount, importedEspdExclusionCriteriaSelectedCount);
    }
}
