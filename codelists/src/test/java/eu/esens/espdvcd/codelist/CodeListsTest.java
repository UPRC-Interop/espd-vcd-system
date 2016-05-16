package eu.esens.espdvcd.codelist;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CodeListsTest {
    
    public CodeListsTest() {
    }
    
    @Before
    public void setUp() {
    }
     
    @Test 
        public void testGC() {
       
            Assert.assertEquals("Greece", Codelists.CountryIdentification.getValueForId("GR"));
            Assert.assertEquals("GR", Codelists.CountryIdentification.getIdForData("Greece"));
            
            Assert.assertEquals("MT", Codelists.TenderingRole.getIdForData("Main tenderer"));
            Assert.assertEquals("Main tenderer", Codelists.TenderingRole.getValueForId("MT"));
            
        }
        
        @Test 
        public void testBiMap() {
            Assert.assertEquals("Greece",Codelists.CountryIdentification.getBiMap().get("GR"));
            Assert.assertEquals("GR",Codelists.CountryIdentification.getBiMap().inverse().get("Greece"));            
        }
    
        @Test 
        public void testAllGC() {
        
            String theCodelist = "";
            try {
                for (Codelists c : Codelists.values()) {
                    theCodelist = c.name();
                    Assert.assertNotNull(!c.getBiMap().isEmpty());
                }
            } catch (Exception e) {
                Assert.fail("Error in Codelist "+theCodelist+": "+e.getMessage());
            }
        }
}
