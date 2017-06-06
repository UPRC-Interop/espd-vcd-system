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

        Assert.assertEquals("Greece", CodeListsFactory.CodeListsV1.COUNTRY_IDENTIFICATION.getValueForId("GR"));
        Assert.assertEquals("GR", CodeListsFactory.CodeListsV1.COUNTRY_IDENTIFICATION.getIdForData("Greece"));

        Assert.assertEquals("MT", CodeListsFactory.CodeListsV1.TENDERING_ROLE.getIdForData("Main tenderer"));
        Assert.assertEquals("Main tenderer", CodeListsFactory.CodeListsV1.TENDERING_ROLE.getValueForId("MT"));

    }

    @Test
    public void testBiMap() {
        Assert.assertEquals("Greece", CodeListsFactory.CodeListsV1.COUNTRY_IDENTIFICATION.getBiMap().get("GR"));
        Assert.assertEquals("GR", CodeListsFactory.CodeListsV1.COUNTRY_IDENTIFICATION.getBiMap().inverse().get("Greece"));
    }

    @Test
    public void testAllGC() {

        String theCodelist = "";
                
        try {
            
            for (CodeLists c : CodeListsFactory.CodeListsV1.values()) {
                theCodelist = c.getConstantName();
                Assert.assertNotNull(!c.getBiMap().isEmpty());
            }
                      
        } catch (Exception e) {
            Assert.fail("Error in CodeList " + theCodelist + ": " + e.getMessage());
        }
    }

}
