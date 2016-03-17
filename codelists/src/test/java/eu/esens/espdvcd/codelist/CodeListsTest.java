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
       
            Assert.assertEquals("GREECE", Codelists.CountryIdentification.getValueForId("GR"));
            Assert.assertEquals("GR", Codelists.CountryIdentification.getIdForData("GREECE"));
            
            Assert.assertEquals("MT", Codelists.TenderingRole.getIdForData("Main tenderer"));
            Assert.assertEquals("Main tenderer", Codelists.TenderingRole.getValueForId("MT"));
            
        }
        
        @Test 
        public void testBiMap() {
            Assert.assertEquals("GREECE",Codelists.CountryIdentification.getBiMap().get("GR"));
            Assert.assertEquals("GR",Codelists.CountryIdentification.getBiMap().inverse().get("GREECE"));
        }
    
        @Test 
        public void testAllGC() {
        
            for (Codelists c : Codelists.values()) {
                Assert.assertNotNull(!c.getBiMap().isEmpty());
            }
        }
}
